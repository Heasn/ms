package im.cave.ms.network.server.channel.handler;

import im.cave.ms.client.MapleClient;
import im.cave.ms.client.character.MapleCharacter;
import im.cave.ms.client.field.Foothold;
import im.cave.ms.client.field.MapleMap;
import im.cave.ms.client.field.obj.Drop;
import im.cave.ms.client.items.Equip;
import im.cave.ms.client.items.Inventory;
import im.cave.ms.client.items.Item;
import im.cave.ms.client.items.ItemBuffs;
import im.cave.ms.client.items.ItemInfo;
import im.cave.ms.client.items.ScrollUpgradeInfo;
import im.cave.ms.constants.GameConstants;
import im.cave.ms.constants.ItemConstants;
import im.cave.ms.enums.EquipAttribute;
import im.cave.ms.enums.EquipBaseStat;
import im.cave.ms.enums.EquipSpecialAttribute;
import im.cave.ms.enums.EquipmentEnchantType;
import im.cave.ms.enums.InventoryType;
import im.cave.ms.enums.ScrollStat;
import im.cave.ms.enums.SpecStat;
import im.cave.ms.network.netty.InPacket;
import im.cave.ms.network.packet.UserPacket;
import im.cave.ms.provider.data.ItemData;
import im.cave.ms.scripting.item.ItemScriptManager;
import im.cave.ms.tools.Position;
import im.cave.ms.tools.Util;

import java.util.List;
import java.util.Map;

import static im.cave.ms.enums.ChatType.SystemNotice;
import static im.cave.ms.enums.EquipBaseStat.cuc;
import static im.cave.ms.enums.EquipBaseStat.tuc;
import static im.cave.ms.enums.InventoryOperation.MOVE;
import static im.cave.ms.enums.InventoryOperation.REMOVE;
import static im.cave.ms.enums.InventoryOperation.UPDATE_QUANTITY;
import static im.cave.ms.enums.InventoryType.EQUIP;
import static im.cave.ms.enums.InventoryType.EQUIPPED;

;

/**
 * @author fair
 * @version V1.0
 * @Package im.cave.ms.net.handler.channel
 * @date 11/29 22:00
 */
public class InventoryHandler {

    public static void handleChangeInvPos(MapleClient c, InPacket inPacket) {
        MapleCharacter player = c.getPlayer();
        player.setTick(inPacket.readInt());
        InventoryType invType = InventoryType.getTypeById(inPacket.readByte());
        if (invType == null) {
            return;
        }
        short oldPos = inPacket.readShort();
        short newPos = inPacket.readShort();
        short quantity = inPacket.readShort();
        InventoryType invTypeFrom = invType == EQUIP ? oldPos < 0 ? EQUIPPED : EQUIP : invType;
        InventoryType invTypeTo = invType == EQUIP ? newPos < 0 ? EQUIPPED : EQUIP : invType;
        Item item = player.getInventory(invTypeFrom).getItem(oldPos < 0 ? (short) -oldPos : oldPos);
        if (item == null) {
            return;
        }
        if (newPos == 0) {
            Drop drop;
            boolean fullDrop;
            if (!item.getInvType().isStackable() || quantity >= item.getQuantity()
                    || ItemConstants.isThrowingStar(item.getItemId()) || ItemConstants.isBullet(item.getItemId())) {
                fullDrop = true;
                player.getInventory(invTypeFrom).removeItem(item);
                item.drop();
                drop = new Drop(-1, item);
            } else {
                fullDrop = false;
                Item dropItem = ItemData.getItemCopy(item.getItemId(), false);
                dropItem.setQuantity(quantity);
                item.removeQuantity(quantity);
                drop = new Drop(-1, dropItem);
            }
            MapleMap map = player.getMap();
            Position position = player.getPosition();
            Foothold fh = map.findFootHoldBelow(new Position(position.getX(), position.getY() - GameConstants.DROP_HEIGHT));
            drop.setCanBePickedUpByPet(false);
            map.drop(drop, position, new Position(position.getX(), fh.getYFromX(position.getX())));
            if (fullDrop) {
                c.announce(UserPacket.inventoryOperation(true, false, REMOVE, oldPos, newPos, 0, item));
            } else {
                c.announce(UserPacket.inventoryOperation(true, false, UPDATE_QUANTITY, oldPos, newPos, 0, item));
            }
        } else {
            Item swapItem = player.getInventory(invTypeTo).getItem(newPos < 0 ? (short) -newPos : newPos);
            if (invType == EQUIP && invTypeFrom != invTypeTo) {
                // TODO: verify job (see item.RequiredJob), level, stat, unique equip requirements
                if (invTypeFrom == EQUIPPED) {
                    player.unequip(item);
                } else {
                    player.equip(item);
                    if (swapItem != null) {
                        player.unequip(swapItem);
                    }
                }
            }
            item.setPos(newPos < 0 ? -newPos : newPos);
            if (swapItem != null) {
                swapItem.setPos(oldPos);
            }
            c.announce(UserPacket.inventoryOperation(true, false, MOVE, oldPos, newPos, 0, item));
        }

    }

    public static void handleUseItem(InPacket inPacket, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        if (player == null) {
            return;
        }
        player.setTick(inPacket.readInt());
        short pos = inPacket.readShort();
        int itemId = inPacket.readInt();
        Item item = player.getConsumeInventory().getItem(pos);
        if (item == null || item.getItemId() != itemId) {
            return;
        }
        ItemInfo itemInfo = ItemData.getItemInfoById(itemId);
        Map<SpecStat, Integer> specStats = itemInfo.getSpecStats();
        if (specStats.size() > 0) {
            ItemBuffs.giveItemBuffsFromItemID(player, itemId);
            player.consumeItem(item);
        } else {
            player.consumeItem(item);
        }
    }

    public static void handleEquipEnchanting(InPacket inPacket, MapleClient c) {
        byte val = inPacket.readByte();
        EquipmentEnchantType type = EquipmentEnchantType.getByVal(val);
        MapleCharacter player = c.getPlayer();
        if (type == null) {
            player.dropMessage("未知的装备强化请求:" + val);
            return;
        }
        switch (type) {
            case ScrollUpgradeRequest: {
                player.setTick(inPacket.readInt());
                short pos = inPacket.readShort();
                int scrollId = inPacket.readInt();
                Inventory iv = pos < 0 ? player.getEquippedInventory() : player.getEquipInventory();
                Equip equip = (Equip) iv.getItem((short) (pos < 0 ? -pos : pos));
                Equip prevEquip = equip.deepCopy();
                List<ScrollUpgradeInfo> scrolls = ItemConstants.getScrollUpgradeInfosByEquip(equip);
                ScrollUpgradeInfo scrollUpgradeInfo = scrolls.get(scrollId);
                player.consumeItem(ItemConstants.SPELL_TRACE_ID, scrollUpgradeInfo.getCost());
                boolean success = scrollUpgradeInfo.applyTo(equip);
                equip.reCalcEnchantmentStats();
                player.announce(UserPacket.showScrollUpgradeResult(false, success ? 1 : 0, scrollUpgradeInfo.getTitle(), prevEquip, equip));
                equip.updateToChar(player);
                if (equip.getBaseStat(tuc) > 0) {
                    scrolls = ItemConstants.getScrollUpgradeInfosByEquip(equip);
                    c.announce(UserPacket.scrollUpgradeDisplay(false, scrolls));
                }
                break;
            }
            case ScrollUpgradeDisplay:
                int pos = inPacket.readInt();
                Inventory iv = pos < 0 ? player.getEquippedInventory() : player.getEquipInventory();
                Equip equip = (Equip) iv.getItem((short) (pos < 0 ? -pos : pos));
                if (equip == null) {
                    return;
                }
                List<ScrollUpgradeInfo> scrolls = ItemConstants.getScrollUpgradeInfosByEquip(equip);
                c.announce(UserPacket.scrollUpgradeDisplay(false, scrolls));
                break;
            case ScrollTimerEffective:
                break;
        }
    }

    public static void handleUserPortalScrollUseRequest(InPacket inPacket, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        if (player == null) {
            return;
        }
        player.setTick(inPacket.readInt());
        short pos = inPacket.readShort();
        Item item = player.getConsumeInventory().getItem(pos);
        int itemId = inPacket.readInt();
        if (item.getItemId() != itemId) {
            return;
        }
        ItemInfo itemInfo = ItemData.getItemInfoById(itemId);
        int moveTo = itemInfo.getMoveTo();
        player.changeMap(moveTo);
        player.consumeItem(itemId, 1);
    }

    public static void handleUserUpgradeItemUseRequest(InPacket inPacket, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        player.setTick(inPacket.readInt());
        short uPos = inPacket.readShort(); //Use Position
        short ePos = inPacket.readShort(); //Eqp Position
        Item scroll = player.getInventory(InventoryType.CONSUME).getItem(uPos);
        InventoryType invType = ePos < 0 ? EQUIPPED : EQUIP;
        Equip equip = (Equip) player.getInventory(invType).getItem(ePos < 0 ? (short) -ePos : ePos);
        if (scroll == null || equip == null || equip.hasSpecialAttribute(EquipSpecialAttribute.Vestige)) {
            player.chatMessage(SystemNotice, "Could not find scroll or equip.");
            return;
        }
        int scrollId = scroll.getItemId();
        boolean success = false;
        boolean boom = false;
        Map<ScrollStat, Integer> vals = ItemData.getItemInfoById(scrollId).getScrollStats();
        if (vals.size() > 0) {
            if (equip.getBaseStat(tuc) <= 0) {
                player.announce(UserPacket.inventoryRefresh(true));
                return;
            }
            int chance = vals.getOrDefault(ScrollStat.success, 100);
            int curse = vals.getOrDefault(ScrollStat.cursed, 0);
            success = Util.succeedProp(chance);
            if (success) {
                boolean chaos = vals.containsKey(ScrollStat.randStat);
                if (chaos) {
                    boolean noNegative = vals.containsKey(ScrollStat.noNegative);
                    int max = vals.containsKey(ScrollStat.incRandVol) ? ItemConstants.RAND_CHAOS_MAX : ItemConstants.INC_RAND_CHAOS_MAX;
                    for (EquipBaseStat ebs : ScrollStat.getRandStats()) {
                        int cur = (int) equip.getBaseStat(ebs);
                        if (cur == 0) {
                            continue;
                        }
                        int randStat = Util.getRandom(max);
                        randStat = !noNegative && Util.succeedProp(50) ? -randStat : randStat;
                        equip.addStat(ebs, randStat);
                    }
                } else {
                    for (Map.Entry<ScrollStat, Integer> entry : vals.entrySet()) {
                        ScrollStat ss = entry.getKey();
                        int val = entry.getValue();
                        if (ss.getEquipStat() != null) {
                            equip.addStat(ss.getEquipStat(), val);
                        }
                    }
                }
                equip.addStat(tuc, -1);
                equip.addStat(cuc, 1);
            } else {
                if (curse > 0) {
                    boom = Util.succeedProp(curse);
                    if (boom && !equip.hasAttribute(EquipAttribute.ProtectionScroll)) {
                        player.consumeItem(equip);
                    } else {
                        boom = false;
                    }
                }
                if (!equip.hasAttribute(EquipAttribute.UpgradeCountProtection)) {
                    equip.addStat(tuc, -1);
                }
            }
            equip.removeAttribute(EquipAttribute.ProtectionScroll);
            equip.removeAttribute(EquipAttribute.LuckyDay);
            equip.removeAttribute(EquipAttribute.UpgradeCountProtection);
            c.announce(UserPacket.showItemUpgradeEffect(player.getId(), success, false, scrollId, equip.getItemId(), boom));
            if (!boom) {
                equip.reCalcEnchantmentStats();
                equip.updateToChar(player);
            }
            player.consumeItem(scroll);
        } else {
            player.chatMessage("Could not find scroll data.");
        }

    }

    public static void handleUserScriptItemUseRequest(InPacket inPacket, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        if (player == null) {
            return;
        }
        player.setTick(inPacket.readInt());
        short pos = inPacket.readShort();
        int itemId = inPacket.readInt();
        int quantity = inPacket.readInt();
        Item item = player.getConsumeInventory().getItem(pos);
        if (item == null || item.getItemId() != itemId) {
            item = player.getCashInventory().getItem(pos);
        }
        if (item == null || item.getItemId() != itemId) {
            return;
        }
        String script = String.valueOf(itemId);
        int npcId = 0;
        ItemInfo ii = ItemData.getItemInfoById(itemId);
        if (ii.getScript() != null && !"".equals(ii.getScript())) {
            script = ii.getScript();
            npcId = ii.getNpcID();
        }
        ItemScriptManager.getInstance().startScript(itemId, script, npcId, c);
    }

    public static void handleUserFlameItemUseRequest(InPacket inPacket, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        player.setTick(inPacket.readInt());
        short uPos = inPacket.readShort();
        short ePos = inPacket.readShort();
        Item flame = player.getInventory(InventoryType.CONSUME).getItem(uPos);
        if (flame == null) {
            flame = player.getCashInventory().getItem(uPos);
        }
        InventoryType invType = ePos < 0 ? EQUIPPED : EQUIP;
        Equip equip = (Equip) player.getInventory(invType).getItem(ePos < 0 ? (short) -ePos : ePos);
        if (flame == null || equip == null) {
            player.chatMessage(SystemNotice, "Could not find flame or equip.");
            return;
        }
        Map<ScrollStat, Integer> vals = ItemData.getItemInfoById(flame.getItemId()).getScrollStats();
        if (vals.size() > 0) {
            int reqEquipLevelMax = vals.getOrDefault(ScrollStat.reqEquipLevelMax, 250);

            if (equip.getRLevel() + equip.getIIncReq() > reqEquipLevelMax) {
                //
                return;
            }

            boolean success = Util.succeedProp(vals.getOrDefault(ScrollStat.success, 100));

            if (success) {
                boolean eternalFlame = vals.getOrDefault(ScrollStat.createType, 6) >= 7;
                equip.randomizeFlameStats(eternalFlame); // Generate high stats if it's an eternal/RED flame only.
            }

            equip.updateToChar(player);
            c.announce(UserPacket.showItemUpgradeEffect(player.getId(), success, false, flame.getItemId(), equip.getItemId(), false));

            player.consumeItem(flame);
        }

    }

    //todo 谜之蛋
    public static void handleUserOpenMysteryEgg(InPacket inPacket, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        short pos = inPacket.readShort();
        Item item = player.getConsumeInventory().getItem(pos);
        int itemId = inPacket.readInt();
        byte unk = inPacket.readByte();
        player.consumeItem(itemId, 1);
    }
}

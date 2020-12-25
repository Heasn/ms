package im.cave.ms.client.character.temp;

import im.cave.ms.client.Job.MapleJob;
import im.cave.ms.client.character.MapleCharacter;
import im.cave.ms.client.character.Option;
import im.cave.ms.client.skill.SkillInfo;
import im.cave.ms.constants.GameConstants;
import im.cave.ms.constants.JobConstants;
import im.cave.ms.enums.BaseStat;
import im.cave.ms.enums.ChatType;
import im.cave.ms.enums.TSIndex;
import im.cave.ms.network.netty.OutPacket;
import im.cave.ms.network.packet.PlayerPacket;
import im.cave.ms.network.server.service.EventManager;
import im.cave.ms.provider.data.SkillData;
import im.cave.ms.tools.Randomizer;
import im.cave.ms.tools.Tuple;
import im.cave.ms.tools.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import static im.cave.ms.client.character.temp.CharacterTemporaryStat.CombatOrders;
import static im.cave.ms.client.character.temp.CharacterTemporaryStat.ComboCounter;
import static im.cave.ms.client.character.temp.CharacterTemporaryStat.IndieMaxDamageOver;
import static im.cave.ms.client.character.temp.CharacterTemporaryStat.IndieMaxDamageOverR;
import static im.cave.ms.client.character.temp.CharacterTemporaryStat.LifeTidal;
import static im.cave.ms.client.character.temp.CharacterTemporaryStat.RideVehicle;
import static im.cave.ms.client.character.temp.CharacterTemporaryStat.RideVehicleExpire;
import static im.cave.ms.client.character.temp.CharacterTemporaryStat.Speed;

/**
 * Created on 1/3/2018.
 */
public class TemporaryStatManager {
    private static final Logger log = LoggerFactory.getLogger(TemporaryStatManager.class);
    private final Map<CharacterTemporaryStat, List<Option>> currentStats = new HashMap<>();
    private final Map<CharacterTemporaryStat, List<Option>> newStats = new HashMap<>();
    private final Map<CharacterTemporaryStat, List<Option>> removedStats = new HashMap<>();
    private final Map<CharacterTemporaryStat, ScheduledFuture> schedules = new HashMap<>();
    private final HashMap<Tuple<CharacterTemporaryStat, Option>, ScheduledFuture> indieSchedules = new HashMap<>();
    private int pvpDamage;
    private byte defenseState;
    private byte defenseAtt;
    private int[] diceInfo = new int[22];
    private final int[] diceOption = new int[8];
    private List<Integer> mobZoneStates;
    private int viperEnergyCharge;
    //    private StopForceAtom stopForceAtom;
    private final MapleCharacter chr;
    private final List<TemporaryStatBase> twoStates = new ArrayList<>();
    //    private final Set<AffectedArea> affectedAreas = new HashSet<>();
    private final Map<BaseStat, Integer> baseStats = new HashMap<>();

    public TemporaryStatManager(MapleCharacter chr) {
        this.chr = chr;
//        for (CharacterTemporaryStat cts : TSIndex.getAllCTS()) {
//            switch (cts) {
//                case PartyBooster:
////                    twoStates.add(new PartyBooster());
//                    break;
//                case GuidedBullet:
////                    twoStates.add(new GuidedBullet());
//                    break;
//                case EnergyCharged:
//                    twoStates.add(new TemporaryStatBase(true));
//                    break;
//                case RideVehicle:
//                    twoStates.add(new TwoStateTemporaryStat(false));
//                    break;
//                default:
//                    twoStates.add(new TwoStateTemporaryStat(true));
//                    break;
//            }
//        }

    }

    public List<TemporaryStatBase> getTwoStates() {
        return twoStates;
    }

    public TemporaryStatBase getTSBByTSIndex(TSIndex tsi) {
        return getTwoStates().get(tsi.getIndex());
    }

    public void putCharacterStatValue(CharacterTemporaryStat cts, Option option) {
        boolean indie = cts.isIndie();
        option.setTimeToMillis();
        SkillInfo skillinfo = SkillData.getSkillInfo(indie ? option.nReason : option.rOption);
        if (skillinfo != null && !skillinfo.isNotIncBuffDuration()) {
            int duration = (indie ? option.tTerm : option.tOption);
            long buffTimeR = getChr().getTotalStat(BaseStat.buffTimeR); // includes the 100% base
            if (indie) {
                option.tTerm = (int) ((buffTimeR * duration) / 100);
            } else {
                option.tOption = (int) ((buffTimeR * duration) / 100);
            }
        }
        //战斗指令
        if (cts == CombatOrders) {
            chr.setCombatOrders(option.nOption);
        }
        //独立：最大伤害
        if (cts == IndieMaxDamageOver || cts == IndieMaxDamageOverR) {
            long base = getTotalNOptionOfStat(IndieMaxDamageOver) + (cts == IndieMaxDamageOver ? option.nValue : 0);
            long rate = getTotalNOptionOfStat(IndieMaxDamageOverR) + (cts == IndieMaxDamageOverR ? option.nValue : 0);
            if ((GameConstants.DAMAGE_CAP + base) * rate > Integer.MAX_VALUE) {
                chr.chatMessage(ChatType.Tip, "Not adding max damage over, as it would bypass the hard cap of damage.");
                return;
            }
        }
        if (!indie) {
            List<Option> optList = new ArrayList<>();
            optList.add(option);
            if (hasStat(cts)) {
                Option oldOption = getCurrentStats().get(cts).get(0);
                // remove old base stats from map
                for (Map.Entry<BaseStat, Integer> stats : BaseStat.getFromCTS(cts, oldOption).entrySet()) {
                    removeBaseStat(stats.getKey(), stats.getValue());
                }
            }
            //新增加buff
            getNewStats().put(cts, optList);
            getCurrentStats().put(cts, optList);
            for (Map.Entry<BaseStat, Integer> stats : BaseStat.getFromCTS(cts, option).entrySet()) {
                //新增属性
                addBaseStat(stats.getKey(), stats.getValue());
            }
            //持续时间
            if (option.tOption > 0) {
                if (getSchedules().containsKey(cts)) {
                    getSchedules().get(cts).cancel(false);
                }
                ScheduledFuture sf = EventManager.addEvent(() -> removeStat(cts, true), option.tOption);
                getSchedules().put(cts, sf);
            }
        } else {
            List<Option> optList;
            if (hasStat(cts)) {
                optList = getCurrentStats().get(cts);
            } else {
                optList = new ArrayList<>();
            }
            if (optList.contains(option)) {
                // remove old option of the same skill
                Option oldOption = getOptByCTSAndSkill(cts, option.nReason);
                for (Map.Entry<BaseStat, Integer> stats : BaseStat.getFromCTS(cts, oldOption).entrySet()) {
                    removeBaseStat(stats.getKey(), stats.getValue());
                }
                optList.remove(oldOption);
            }
            optList.add(option);
            getNewStats().put(cts, optList);
            getCurrentStats().put(cts, optList);
            // Add stats to base stat
            for (Map.Entry<BaseStat, Integer> stats : BaseStat.getFromCTS(cts, option).entrySet()) {
                addBaseStat(stats.getKey(), stats.getValue());
            }
            if (option.tTerm > 0) {
                Tuple tuple = new Tuple(cts, option);
                if (getIndieSchedules().containsKey(tuple)) {
                    getIndieSchedules().get(tuple).cancel(false);
                }
                ScheduledFuture sf = EventManager.addEvent(() -> removeIndieStat(cts, option, true), option.tTerm);
                getIndieSchedules().put(tuple, sf);
            }
        }
        if (cts != LifeTidal && JobConstants.isDemonAvenger(chr.getJobId())) {
//            ((Demon) chr.getJobHandler()).sendHpUpdate();
        }
    }

    public Option getOptByCTSAndSkill(CharacterTemporaryStat cts, int skillID) {
        Option res = null;
        if (getCurrentStats().containsKey(cts)) {
            for (Option o : getCurrentStats().get(cts)) {
                if (o.rOption == skillID || o.nReason == skillID) {
                    res = o;
                    break;
                }
            }
        }
        return res;
    }

    public synchronized void removeStat(CharacterTemporaryStat cts, boolean fromSchedule) {
        if (cts == CombatOrders) {
            chr.setCombatOrders(0);
        }
        Option opt = getOption(cts);
        //去除属性
        for (Map.Entry<BaseStat, Integer> stats : BaseStat.getFromCTS(cts, opt).entrySet()) {
            removeBaseStat(stats.getKey(), stats.getValue());
        }
        getRemovedStats().put(cts, getCurrentStats().get(cts));
        getCurrentStats().remove(cts);
        sendResetStatPacket(cts == RideVehicle || cts == RideVehicleExpire);
        if (TSIndex.isTwoStat(cts)) {
            getTSBByTSIndex(TSIndex.getTSEFromCTS(cts)).reset();
        }
        if (!fromSchedule && getSchedules().containsKey(cts)) {
            getSchedules().get(cts).cancel(false);
        } else {
            getSchedules().remove(cts);
        }
        if (JobConstants.isDemonAvenger(chr.getJobId())) {
//            ((Demon) chr.getJobHandler()).sendHpUpdate();
        }
    }

    public synchronized void removeIndieStat(CharacterTemporaryStat cts, Option option, boolean fromSchedule) {
        List<Option> optList = new ArrayList<>();
        optList.add(option);
        getRemovedStats().put(cts, optList);
        for (Map.Entry<BaseStat, Integer> stats : BaseStat.getFromCTS(cts, option).entrySet()) {
            removeBaseStat(stats.getKey(), stats.getValue());
        }
        if (getCurrentStats().containsKey(cts)) {
//            if (option.summon != null) {
//                getChr().getField().broadcastPacket(Summoned.summonedRemoved(option.summon, LeaveType.ANIMATION));
//                option.summon = null;
//            }
            getCurrentStats().get(cts).remove(option);
            if (getCurrentStats().get(cts).size() == 0) {
                getCurrentStats().remove(cts);
            }
        }
        sendResetStatPacket();
        Tuple tuple = new Tuple(cts, option);
        if (!fromSchedule && getIndieSchedules().containsKey(tuple)) {
            getIndieSchedules().get(tuple).cancel(false);
        } else {
            getIndieSchedules().remove(tuple);
        }
    }

    public boolean hasNewStat(CharacterTemporaryStat cts) {
        return newStats.containsKey(cts);
    }

    public boolean hasStat(CharacterTemporaryStat cts) {
        return getCurrentStats().containsKey(cts);
    }

    public Option getOption(CharacterTemporaryStat cts) {
        if (hasStat(cts)) {
            return getCurrentStats().get(cts).get(0);
        }
        return new Option();
    }

    public List<Option> getOptions(CharacterTemporaryStat cts) {
        if (hasStat(cts)) {
            return getCurrentStats().get(cts);
        }
        return new ArrayList<>();
    }

    public int[] getMaskByCollection(Map<CharacterTemporaryStat, List<Option>> map) {
        int[] res = new int[CharacterTemporaryStat.length];
        for (int i = 0; i < res.length; i++) {
            for (CharacterTemporaryStat cts : map.keySet()) {
                if (cts.getPos() == i) {
                    res[i] |= cts.getVal();
                }
            }
        }
        return res;
    }

    public int[] getTotalMask() {
        return getMaskByCollection(getCurrentStats());
    }

    public int[] getNewMask() {
        return getMaskByCollection(getNewStats());
    }

    public int[] getRemovedMask() {
        return getMaskByCollection(getRemovedStats());
    }

    public void encodeForLocal(OutPacket outPacket) {
        int[] mask = getNewMask();
        for (int i = 0; i < getNewMask().length; i++) {
            outPacket.writeInt(mask[i]);
        }
        List<CharacterTemporaryStat> orderedAndFilteredCtsList = new ArrayList<>(getNewStats().keySet()).stream()
                .filter(cts -> cts.getOrder() != -1)
                .sorted(Comparator.comparingInt(CharacterTemporaryStat::getOrder))
                .collect(Collectors.toList());
        for (CharacterTemporaryStat cts : orderedAndFilteredCtsList) {
            if (cts.getOrder() != -1) {
                Option o = getOption(cts);
                if (cts.isEncodeInt()) {
                    outPacket.writeInt(o.nOption);
                } else {
                    outPacket.writeShort(o.nOption);
                }
                outPacket.writeInt(o.rOption);
                outPacket.writeInt(o.tOption);
            }
        }

        outPacket.writeZeroBytes(13);
        if (hasNewStat(ComboCounter)) { //todo
            outPacket.writeInt(getOption(ComboCounter).bOption);
        }
        encodeIndieTempStat(outPacket);

        outPacket.write(1);
        outPacket.write(1);
        outPacket.write(1);
        outPacket.writeInt(0);
        outPacket.write(0);  //sometimes

        getNewStats().clear();
    }

    private void encodeIndieTempStat(OutPacket outPacket) {
        Map<CharacterTemporaryStat, List<Option>> stats = getCurrentStats().entrySet().stream()
                .filter(stat -> stat.getKey().isIndie() && getNewStats().containsKey(stat.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        TreeMap<CharacterTemporaryStat, List<Option>> sortedStats = new TreeMap<>(stats);
        if (sortedStats.size() == 0) {
            outPacket.writeInt(0);
        }
        for (Map.Entry<CharacterTemporaryStat, List<Option>> stat : sortedStats.entrySet()) {
            int curTime = (int) System.currentTimeMillis();
            List<Option> options = stat.getValue();
            if (options == null) {
                outPacket.writeInt(0);
                continue;
            }
            outPacket.writeInt(options.size());
            for (Option option : options) {
                outPacket.writeInt(option.nReason);
                outPacket.writeInt(option.nValue);
                outPacket.writeInt(curTime);
                outPacket.writeInt(0);
                outPacket.writeInt(option.tTerm);
                outPacket.writeZeroBytes(16);
            }
        }
    }


    public void encodeForRemote(OutPacket outPacket, Map<CharacterTemporaryStat, List<Option>> collection) {
        int[] mask = getMaskByCollection(collection);
        for (int maskElem : mask) {
            outPacket.writeInt(maskElem);
        }
        List<CharacterTemporaryStat> orderedAndFilteredCtsList = new ArrayList<>(collection.keySet()).stream()
                .filter(cts -> cts.getOrder() != -1)
                .sorted(Comparator.comparingInt(CharacterTemporaryStat::getOrder))
                .collect(Collectors.toList());
        for (CharacterTemporaryStat cts : orderedAndFilteredCtsList) {
//            if (cts.getRemoteOrder() != -1) {
//                Option o = getOption(cts);
//                if (cts == CharacterTemporaryStat.Unk82) {
//                    outPacket.writeShort(o.nOption);
//                }
//                if (!cts.isNotEncodeAnything()) {
//                    if (cts.isRemoteEncode1()) {
//                        outPacket.writeShort(o.nOption);
//                    } else if (cts.isRemoteEncode4()) {
//                        outPacket.writeInt(o.nOption);
//                    } else {
//                        outPacket.writeShort(o.nOption);
//                    }
//                    if (!cts.isNotEncodeReason()) {
//                        outPacket.writeInt(o.rOption);
//                    }
//                }
//
//                outPacket.writeZeroBytes(11);
//            }
        }

        final int MAX_INT = 2147483647;
        final int RANDOM_INT = Randomizer.nextInt(MAX_INT);

        outPacket.writeInt(-1); // PyramidEffect

        outPacket.write(0); // KillingPoint


        outPacket.writeInt(0); // 神圣迅捷
        outPacket.writeInt(0);
        outPacket.writeInt(0); // 战法灵气

        outPacket.writeInt(0); // 激素狂飙

        outPacket.writeInt(0); // 忍耐之盾

        outPacket.writeInt(0); // SECONDARY_STAT_UNK476

        outPacket.writeInt(0); // 结合灵气
        outPacket.writeInt(0);
        outPacket.writeInt(0);

        outPacket.writeInt(0); // SECONDARY_STAT_BattlePvP_LangE_Protection
        outPacket.writeInt(0);

        outPacket.write(1); //AranSmashSwing 激素狂飙

        outPacket.writeInt(0);
        outPacket.writeInt(0);
        outPacket.writeInt(0);
        outPacket.writeInt(0);
        outPacket.writeInt(0);
        outPacket.writeInt(0);
        outPacket.writeInt(0);
        outPacket.writeInt(0);
        outPacket.writeInt(0);
        outPacket.writeInt(0);
        outPacket.writeInt(0);
        outPacket.writeInt(0);

        outPacket.writeInt(0); // 能量
        //能量获得
        outPacket.writeLong(0);
        outPacket.write(1);
        outPacket.writeInt(MAX_INT);
        //疾驰速度
        outPacket.writeLong(0);
        outPacket.write(1);
        outPacket.writeInt(RANDOM_INT);
        outPacket.writeShort(0);
        //疾驰跳跃
        outPacket.writeLong(0);
        outPacket.write(1);
        outPacket.writeInt(RANDOM_INT);
        outPacket.writeShort(0);
        //骑兽技能
        outPacket.writeLong(0);
        outPacket.write(1);
        outPacket.writeInt(MAX_INT);
        //极速领域
        outPacket.writeLong(0);
        outPacket.writeInt(1);
        outPacket.writeLong(0);
        //导航辅助
        outPacket.writeLong(0);
        outPacket.write(1);
        outPacket.writeInt(MAX_INT);
        outPacket.writeLong(0);
        //SECONDARY_STAT_Undead
        outPacket.writeLong(0);
        outPacket.write(1);
        outPacket.writeInt(RANDOM_INT);
        outPacket.writeShort(0);
        //SECONDARY_STAT_RideVehicleExpire
        outPacket.writeLong(0);
        outPacket.write(1);
        outPacket.writeInt(RANDOM_INT);
        outPacket.writeShort(0);
        //
        outPacket.writeLong(0);
        outPacket.write(1);
        outPacket.writeInt(MAX_INT);
        //
        outPacket.writeLong(0);
        outPacket.write(1);
        outPacket.writeInt(MAX_INT);


        outPacket.writeLong(0);
        outPacket.writeLong(0);
        outPacket.writeLong(0);
        outPacket.write(0);

    }

//    public void encodeRemovedIndieTempStat(OutPacket outPacket) {
//        Map<CharacterTemporaryStat, List<Option>> stats = getRemovedStats().entrySet().stream()
//                .filter(stat -> stat.getKey().isIndie())
//                .sorted(Comparator.comparingInt(stat -> stat.getKey().getVal()))
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//
//        for (Map.Entry<CharacterTemporaryStat, List<Option>> stat : stats.entrySet()) {
//            int curTime = (int) System.currentTimeMillis();
//            // encode remaining stats
//            CharacterTemporaryStat key = stat.getKey();
//            List<Option> options = getOptions(key);
//            if (options == null) {
//                outPacket.encodeInt(0);
//                continue;
//            }
//            outPacket.encodeInt(options.size());
//            for (Option option : options) {
//                outPacket.encodeInt(option.nReason);
//                outPacket.encodeInt(option.nValue);
//                outPacket.encodeInt(option.nKey); // nKey
//                outPacket.encodeInt(curTime - option.tStart);
//                outPacket.encodeInt(option.tTerm); // tTerm
//                outPacket.encodeInt(0); // size
//                // pw.writeInt(0); // nMValueKey
//                // pw.writeInt(0); // nMValue
//            }
//        }
//    }


    public boolean hasNewMovingEffectingStat() {
        return getNewStats().keySet().stream().anyMatch(CharacterTemporaryStat::isMovingEffectingStat);
    }

    public boolean hasMovingEffectingStat() {
        return getCurrentStats().keySet().stream().anyMatch(CharacterTemporaryStat::isMovingEffectingStat);
    }

    public boolean hasRemovedMovingEffectingStat() {
        return getRemovedStats().keySet().stream().anyMatch(CharacterTemporaryStat::isMovingEffectingStat);
    }

    public Map<CharacterTemporaryStat, List<Option>> getCurrentStats() {
        return currentStats;
    }

    public Map<CharacterTemporaryStat, List<Option>> getNewStats() {
        return newStats;
    }

    public Map<CharacterTemporaryStat, List<Option>> getRemovedStats() {
        return removedStats;
    }

    public int getPvpDamage() {
        return pvpDamage;
    }

    public void setPvpDamage(int pvpDamage) {
        this.pvpDamage = pvpDamage;
    }

    public byte getDefenseState() {
        return defenseState;
    }

    public void setDefenseState(byte defenseState) {
        this.defenseState = defenseState;
    }

    public byte getDefenseAtt() {
        return defenseAtt;
    }

    public void setDefenseAtt(byte defenseAtt) {
        this.defenseAtt = defenseAtt;
    }

    public int[] getDiceInfo() {
        return diceInfo;
    }

    public void setDiceInfo(int[] diceInfo) {
        this.diceInfo = diceInfo;
    }

    public void throwDice(int roll) {
        throwDice(roll, 0);
    }

    public void throwDice(int roll, int secondRoll) {
        int[] array = {0, 0, 30, 20, 15, 20, 30, 20}; // Stats for Normal Rolls
        int[] arrayDD = {0, 0, 40, 30, 25, 30, 40, 30}; // Stats for Double Down Rolls
        for (int i = 0; i < diceOption.length; i++) {
            diceOption[i] = 0;
        }
        if (roll == secondRoll) {
            diceOption[roll] = arrayDD[roll];
        } else {
            diceOption[roll] = array[roll];
            diceOption[secondRoll] = array[secondRoll];
        }
        int[] diceinfo = new int[]{
                diceOption[3],  //nOption 3 (MHPR)
                diceOption[3],  //nOption 3 (MMPR)
                diceOption[4],  //nOption 4 (Cr)
                0,  // CritDamage Min
                0,  // ???  ( CritDamage Max (?) )
                0,  // EVAR
                0,  // AR
                0,  // ER
                diceOption[2],  //nOption 2 (PDDR)
                diceOption[2],  //nOption 2 (MDDR)
                0,  // PDR
                0,  // MDR
                diceOption[5],  //nOption 5 (PIDR)
                0,  // PDamR
                0,  // MDamR
                0,  // PADR
                0,  // MADR
                diceOption[6], //nOption 6 (EXP)
                diceOption[7], //nOption 7 (IED)
                0,  // ASRR
                0,  // TERR
                0,  // MesoRate
                0,
        };
        setDiceInfo(diceinfo);
    }

    public List<Integer> getMobZoneStates() {
        return mobZoneStates;
    }

    public void setMobZoneStates(List<Integer> mobZoneStates) {
        this.mobZoneStates = mobZoneStates;
    }

    public int getViperEnergyCharge() {
        return viperEnergyCharge;
    }

    public void setViperEnergyCharge(int viperEnergyCharge) {
        this.viperEnergyCharge = viperEnergyCharge;
    }

//    public StopForceAtom getStopForceAtom() {
//        return stopForceAtom;
//    }

//    public void setStopForceAtom(StopForceAtom stopForceAtom) {
//        this.stopForceAtom = stopForceAtom;
//    }

    public MapleCharacter getChr() {
        return chr;
    }

    public Map<CharacterTemporaryStat, ScheduledFuture> getSchedules() {
        return schedules;
    }

    public Map<Tuple<CharacterTemporaryStat, Option>, ScheduledFuture> getIndieSchedules() {
        return indieSchedules;
    }

    public void sendSetStatPacket() {
//        getChr().getField().broadcastPacket(UserRemote.setTemporaryStat(getChr(), (short) 0), getChr());
        getChr().announce(PlayerPacket.giveBuff(this));
    }

    public void sendResetStatPacket() {
        sendResetStatPacket(false);
    }

    public void sendResetStatPacket(boolean demount) {
        for (CharacterTemporaryStat characterTemporaryStat : getRemovedStats().keySet()) {
            if (characterTemporaryStat == Speed) {
                demount = true;
                break;
            }
        }
        getChr().announce(PlayerPacket.removeBuff(this, demount));
        getRemovedStats().clear();
    }

    public void removeAllDebuffs() {
        removeStat(CharacterTemporaryStat.Stun, false);
        removeStat(CharacterTemporaryStat.Poison, false);
        removeStat(CharacterTemporaryStat.Seal, false);
        removeStat(CharacterTemporaryStat.Darkness, false);
        removeStat(CharacterTemporaryStat.Thaw, false);
        removeStat(CharacterTemporaryStat.Weakness, false);
        removeStat(CharacterTemporaryStat.Curse, false);
        removeStat(CharacterTemporaryStat.Slow, false);
        removeStat(CharacterTemporaryStat.Blind, false);
        sendResetStatPacket();
    }

//    public Set<AffectedArea> getAffectedAreas() {
//        return affectedAreas;
//    }

//    public void addAffectedArea(AffectedArea aa) {
//        getAffectedAreas().add(aa);
//    }
//
//    public void removeAffectedArea(AffectedArea aa) {
//        getAffectedAreas().remove(aa);
//
//        if (aa.getRemoveSkill()) {
//            removeStatsBySkill(aa.getSkillID());
//        }
//    }
//
//    public boolean hasAffectedArea(AffectedArea affectedArea) {
//        return getAffectedAreas().contains(affectedArea);
//    }

    public boolean hasStatBySkillId(int skillId) {
        for (CharacterTemporaryStat cts : getCurrentStats().keySet()) {
            if (getOption(cts).rOption == skillId || getOption(cts).nReason == skillId) {
                return true;
            }
        }
        return false;
    }

    public void removeStatsBySkill(int skillId) {
        Map<CharacterTemporaryStat, Option> removedMap = new HashMap<>();
        for (CharacterTemporaryStat cts : getCurrentStats().keySet()) {
            Option checkOpt = new Option();
            checkOpt.nReason = skillId;
            if (cts.isIndie() && getOptions(cts) != null && getOptions(cts).contains(checkOpt)) {
                Option o = Util.findWithPred(getOptions(cts), opt -> opt.equals(checkOpt));
                if (o == null) {
                    log.error("Found option null, yet the options contained it?");
                } else {
                    removedMap.put(cts, o);
                }
            } else if (getOption(cts).rOption == skillId || getOption(cts).nReason == skillId) {
                removedMap.put(cts, getOption(cts));
            }
        }
        removedMap.forEach((cts, opt) -> {
            if (cts.isIndie()) {
                removeIndieStat(cts, opt, false);
            } else {
                removeStat(cts, false);
            }
        });
    }

    public void addSoulMPFromMobDeath() {
//        if (hasStat(SoulMP)) {
//            Option o = getOption(SoulMP);
//            o.nOption = Math.min(ItemConstants.MAX_SOUL_CAPACITY, o.nOption + ItemConstants.MOB_DEATH_SOUL_MP_COUNT);
//            putCharacterStatValue(SoulMP, o);
//            if (o.nOption >= ItemConstants.MAX_SOUL_CAPACITY && !hasStat(FullSoulMP)) {
//                Option o2 = new Option();
//                o2.rOption = ItemConstants.getSoulSkillFromSoulID(((Equip) chr.getEquippedItemByBodyPart(BodyPart.Weapon)).getSoulOptionId());
//                if (o2.rOption == 0) {
//                    chr.chatMessage(String.format("Unknown corresponding skill for soul socket id %d!",
//                            ((Equip) chr.getEquippedItemByBodyPart(BodyPart.Weapon)).getSoulOptionId()));
//                }
//                o2.nOption = ItemConstants.MAX_SOUL_CAPACITY;
//                o2.xOption = ItemConstants.MAX_SOUL_CAPACITY;
//                putCharacterStatValue(FullSoulMP, o2);
//            }
//            sendSetStatPacket();
//        }
    }

    public void putCharacterStatValueFromMobSkill(CharacterTemporaryStat cts, Option o) {
        o.rOption |= o.slv << 16; // mob skills are encoded differently: not an int, but short (skill ID), then short (slv).
        putCharacterStatValue(cts, o);
        MapleJob sourceJobHandler = chr.getJobHandler();
        sourceJobHandler.handleMobDebuffSkill(chr);
    }

    public void removeAllStats() {
        Set<CharacterTemporaryStat> currentStats = new HashSet<>();
        currentStats.addAll(getNewStats().keySet());
        currentStats.addAll(getCurrentStats().keySet());
        currentStats.forEach(stat -> removeStat(stat, false));
    }

    public Map<BaseStat, Integer> getBaseStats() {
        return baseStats;
    }

    public void addBaseStat(BaseStat bs, int value) {
        getBaseStats().put(bs, getBaseStats().getOrDefault(bs, 0) + value);
    }

    public void removeBaseStat(BaseStat bs, int value) {
        addBaseStat(bs, -value);
    }

    public long getTotalNOptionOfStat(CharacterTemporaryStat cts) {
        if (cts.isIndie()) {
            return getOptions(cts).stream().mapToLong(o -> o.nValue).sum();
        } else {
            return getOptions(cts).stream().mapToLong(o -> o.nOption).sum();
        }
    }
}

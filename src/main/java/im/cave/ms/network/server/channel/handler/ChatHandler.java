package im.cave.ms.network.server.channel.handler;

import im.cave.ms.client.MapleClient;
import im.cave.ms.client.character.MapleCharacter;
import im.cave.ms.client.social.friend.Friend;
import im.cave.ms.enums.WhisperType;
import im.cave.ms.network.netty.InPacket;
import im.cave.ms.network.packet.opcode.ChatPacket;
import im.cave.ms.network.server.CommandHandler;

/**
 * @author fair
 * @version V1.0
 * @Package im.cave.ms.network.server.channel.handler
 * @date 1/9 23:14
 */
public class ChatHandler {
    public static void handleUserGeneralChat(InPacket in, MapleClient c) {
        int tick = in.readInt();
        MapleCharacter player = c.getPlayer();
        if (player == null) {
            c.close();
            return;
        }
        player.setTick(tick);
        String content = in.readMapleAsciiString();

        if (content.startsWith("@")) {
            CommandHandler.handle(c, content);
            return;
        }

        player.getMap().broadcastMessage(player, ChatPacket.getChatText(player, content), true);
    }

    public static void handleWhisper(InPacket in, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        byte val = in.readByte();
        WhisperType type = WhisperType.getByVal(val);
        if (type == null) {
            player.dropMessage("Whisper val " + val + "未处理");
            player.enableAction();
            return;
        }
        player.setTick(in.readInt());
        String destName = in.readMapleAsciiString();
        MapleCharacter dest = player.getMapleWorld().getCharByName(destName);
        switch (type) {
            case Req_Find_Friend:
                if (dest == null) {
                    return;
                } else if (dest.getChannel() != player.getChannel()) {
                    player.announce(ChatPacket.whisper(WhisperType.Res_Find_Friend, destName, 3, dest.getChannel()));
                }
                player.announce(ChatPacket.whisper(WhisperType.Res_Find_Friend, destName, 1, dest.getMapId()));
                break;
            case Req_Whisper:
                String msg = in.readMapleAsciiString();
                if (dest == null) {
                    player.announce(ChatPacket.whisper(WhisperType.Res_Whisper, destName, 0, 0));
                } else {
                    player.announce(ChatPacket.whisper(WhisperType.Res_Whisper, destName, 1, 0));
                }
                break;
        }
    }


    //todo
    public static void handleGroupMessage(InPacket in, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        byte type = in.readByte(); //0 好友
        in.readByte();
        int groupId = in.readInt();
        if (type == 0) {
            for (Friend friend : player.getFriends()) {
                MapleCharacter friendChar = player.getMapleWorld().getCharById(friend.getId());
                if (friendChar != null) {
//                    friendChar.announce();
                }
            }
        } else if (type == 1) {

        } else if (type == 2) {

        }
    }
}

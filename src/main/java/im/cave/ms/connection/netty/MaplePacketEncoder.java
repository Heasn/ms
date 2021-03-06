package im.cave.ms.connection.netty;

import im.cave.ms.client.MapleClient;
import im.cave.ms.connection.crypto.AESCipher;
import im.cave.ms.connection.crypto.CIGCipher;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static im.cave.ms.client.MapleClient.CLIENT_KEY;
import static im.cave.ms.constants.ServerConstants.LOGIN_PORT;
import static im.cave.ms.constants.ServerConstants.VERSION;

/**
 * @author fair
 * @version V1.0
 * @Package im.cave.ms.connection.netty
 * @date 11/19 19:32
 */
public class MaplePacketEncoder extends MessageToByteEncoder<Packet> {
    private static final Logger log = LoggerFactory.getLogger(MaplePacketEncoder.class);
    private static final int uSeqBase = (short) ((((0xFFFF - VERSION) >> 8) & 0xFF) | (((0xFFFF - VERSION) << 8) & 0xFF00));

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) {
        byte[] data = packet.getData();
        MapleClient client = ctx.channel().attr(CLIENT_KEY).get();
        AESCipher aesCipher = ctx.channel().attr(MapleClient.AES_CIPHER).get();
        if (client != null) {
            int uSeqSend = client.getSendIv();
            short uDataLen = (short) (((data.length << 8) & 0xFF00) | (data.length >>> 8));
            short uRawSeq = (short) ((((uSeqSend >> 24) & 0xFF) | (((uSeqSend >> 16) << 8) & 0xFF00)) ^ uSeqBase);
            client.acquireEncoderState();
            try {
                uDataLen ^= uRawSeq;
                if (client.getPort() == LOGIN_PORT) {
                    aesCipher.Crypt(data, uSeqSend);
                } else {
                    CIGCipher.Crypt(data, uSeqSend);
                }
                client.setSendIv(CIGCipher.InnoHash(uSeqSend, 4, 0));
            } finally {
                client.releaseEncoderState();
            }
            out.writeShort(uRawSeq);
            out.writeShort(uDataLen);
        }
        out.writeBytes(data);
        packet.release();
    }
}

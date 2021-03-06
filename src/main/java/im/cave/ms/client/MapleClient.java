package im.cave.ms.client;

import im.cave.ms.client.character.MapleCharacter;
import im.cave.ms.connection.crypto.AESCipher;
import im.cave.ms.connection.netty.Packet;
import im.cave.ms.connection.packet.LoginPacket;
import im.cave.ms.connection.server.Server;
import im.cave.ms.connection.server.channel.MapleChannel;
import im.cave.ms.connection.server.world.World;
import im.cave.ms.enums.LoginStatus;
import im.cave.ms.enums.LoginType;
import im.cave.ms.enums.ServerType;
import im.cave.ms.scripting.map.MapScriptManager;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.mindrot.jbcrypt.BCrypt;

import javax.script.ScriptEngine;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author fair
 * @version V1.0
 * @Package im.cave.ms.client
 * @date 11/19 17:16
 */
public class MapleClient {

    public static final AttributeKey<MapleClient> CLIENT_KEY = AttributeKey.newInstance("C");
    public static final AttributeKey<AESCipher> AES_CIPHER = AttributeKey.valueOf("A");
    public Map<Integer, Integer> mEncryptedOpcode = new LinkedHashMap<>();
    private int sendIv;
    private int recvIv;
    private final Channel ch;
    private final ReentrantLock lock = new ReentrantLock(true);
    private Account account;
    private byte worldId;
    private byte channelId = -1;
    private byte[] machineID;
    private MapleCharacter player;
    private int storeLength = -1;
    private LoginStatus loginStatus = LoginStatus.NOTLOGGEDIN;
    private long lastPong;
    private final ReentrantLock scriptLock = new ReentrantLock(true);
    private final Map<String, ScriptEngine> engines = new HashMap<>();

    public MapleClient(Channel ch, int sendIv, int recvIv) {
        this.ch = ch;
        this.sendIv = sendIv;
        this.recvIv = recvIv;
    }


    public ReentrantLock getLock() {
        return lock;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public byte getChannelId() {
        return channelId;
    }

    public void setChannelId(byte channelId) {
        this.channelId = channelId;
    }

    public void setChannel(int channel) {
        this.channelId = (byte) channel;
    }

    public byte[] getMachineID() {
        return machineID;
    }

    public void setMachineID(byte[] machineID) {
        this.machineID = machineID;
    }

    public MapleCharacter getPlayer() {
        return player;
    }

    public void setPlayer(MapleCharacter player) {
        this.player = player;
    }

    public LoginStatus getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(LoginStatus loginStatus) {
        this.loginStatus = loginStatus;
    }

    public byte getWorldId() {
        return worldId;
    }

    public void setWorldId(byte world) {
        this.worldId = world;
    }

    public void setWorldId(int world) {
        this.worldId = (byte) world;
    }

    public int getRecvIv() {
        return recvIv;
    }

    public void setRecvIv(int recvIv) {
        this.recvIv = recvIv;
    }

    public int getSendIv() {
        return sendIv;
    }

    public void setSendIv(int sendIv) {
        this.sendIv = sendIv;
    }

    public int getStoreLength() {
        return storeLength;
    }

    public void setStoreLength(int storeLength) {
        this.storeLength = storeLength;
    }

    public void close() {
        ch.close();
    }

    public void acquireEncoderState() {
        lock.lock();
    }

    public void releaseEncoderState() {
        lock.unlock();
    }

    public void acquireScriptState() {
        scriptLock.lock();
    }

    public void releaseScriptState() {
        scriptLock.unlock();
    }

    public int getPort() {
        return Integer.parseInt(ch.localAddress().toString().split(":")[1]);
    }

    public void announce(Packet out) {
        if (out == null) {
            return;
        }
        ch.writeAndFlush(out);
    }

    public void write(Packet out) {
        if (out == null) {
            return;
        }
        ch.writeAndFlush(out);
    }

    public void pongReceived() {
        setLastPong(System.currentTimeMillis());
    }

    public Channel getCh() {
        return ch;
    }

    public LoginType login(String username, String password) {
        Account account = Account.getFromDB(username);
        if (account == null) {
            return LoginType.NotRegistered;
        }
        String hashPwd = account.getPassword();
        if (BCrypt.checkpw(password, hashPwd)) {
            if (Server.getInstance().isAccountLoggedIn(account)) {
                return LoginType.AlreadyConnected;
            }
            Server.getInstance().addAccount(account);
            setAccount(account);
            setLoginStatus(LoginStatus.LOGGEDIN);
            account.save();
            return LoginType.Success;
        }
        return LoginType.IncorrectPassword;
    }

    public List<MapleCharacter> loadCharacters(int worldId) {
        return this.getAccount().getCharacters().stream().filter(character -> character.getWorldId() == worldId).collect(Collectors.toList());
    }

    public void sendPing() {
        announce(LoginPacket.ping(channelId == -1 ? ServerType.LOGIN : ServerType.CHANNEL));
    }

    public MapleChannel getChannel() {
        return Server.getInstance().getChannel(worldId, channelId);
    }

    public long getLastPong() {
        return lastPong;
    }

    public void setLastPong(long lastPong) {
        this.lastPong = lastPong;
    }

    public Map<String, ScriptEngine> getEngines() {
        return engines;
    }

    public void setScriptEngine(String name, ScriptEngine e) {
        engines.put(name, e);
    }

    public ScriptEngine getScriptEngine(String name) {
        return engines.get(name);
    }

    public void removeScriptEngine(String name) {
        engines.remove(name);
    }

    public World getWorld() {
        return Server.getInstance().getWorldById(worldId);
    }

    public void resetScripts() {
        engines.clear();
        MapScriptManager.getInstance().reloadScripts();
    }
}

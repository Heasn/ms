package im.cave.ms.scripting;


import im.cave.ms.client.MapleClient;
import im.cave.ms.client.Record;
import im.cave.ms.client.RecordManager;
import im.cave.ms.client.character.MapleCharacter;
import im.cave.ms.client.field.MapleMap;
import im.cave.ms.client.field.Portal;
import im.cave.ms.connection.packet.WorldPacket;
import im.cave.ms.constants.JobConstants;
import im.cave.ms.enums.ChatType;
import im.cave.ms.enums.JobEnum;
import im.cave.ms.enums.RecordType;

import java.util.Calendar;
import java.util.List;

public class AbstractPlayerInteraction {

    public MapleClient c;

    public AbstractPlayerInteraction(MapleClient c) {
        this.c = c;
    }

    public MapleClient getClient() {
        return c;
    }

    public MapleCharacter getChar() {
        return getChar();
    }

    public int getJob() {
        return getChar().getJob();
    }

    public int getLevel() {
        return getChar().getLevel();
    }

    public MapleMap getMap() {
        return getChar().getMap();
    }

    public int getHourOfDay() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public void serverMsg(String content) {
        c.announce(WorldPacket.chatMessage(content, ChatType.Tip));
    }

    public void dropMessage(Integer content) {
        c.announce(WorldPacket.chatMessage(String.valueOf(content), ChatType.Tip));
    }

    public void dropMessage(String content) {
        getChar().dropMessage(content);
    }

    public boolean forceCompleteQuest(int questId) {
        getChar().getQuestManager().completeQuest(questId);
        return true;
    }

    public boolean forceStartQuest(int questId) {
        getChar().getQuestManager().addQuest(questId);
        return true;
    }

    public void openUnityPortal() {
        c.announce(WorldPacket.unityPortal());
    }

    public void warp(int mapId) {
        getChar().changeMap(mapId);
    }

    public void warp(int mapId, String portalName) {
        MapleCharacter player = getChar();
        Portal portal = player.getMap().getPortal(portalName);
        player.changeMap(mapId, portal != null ? portal.getId() : 0);
    }

    public int getMapId() {
        return getChar().getMapId();
    }

    public void updateRecord(String typeStr, int key, int value) throws ScriptException {
        RecordType type = RecordType.getByName(typeStr);
        if (type == null) {
            throw new ScriptException("记录类型名称错误.");
        }
        RecordManager recordManager = getChar().getRecordManager();
        Record record = recordManager.getRecord(type, key);
        if (record == null) {
            record = Record.builder()
                    .type(type)
                    .key(key)
                    .value(value)
                    .lastReset(System.currentTimeMillis())
                    .lastUpdated(System.currentTimeMillis())
                    .build();
        } else {
            record.setValue(value);
        }
        recordManager.addRecord(record);
    }

    public int getRecordValue(String typeStr, int key) throws ScriptException {
        RecordType type = RecordType.getByName(typeStr);
        if (type == null) {
            throw new ScriptException("记录类型名称错误.");
        }
        RecordManager recordManager = getChar().getRecordManager();
        Record record = recordManager.getRecord(type, key);
        return record != null ? record.getValue() : 0;
    }

    public void openUI() {
        MapleCharacter player = getChar();
        c.announce(WorldPacket.openUI(player.getCombo()));
        player.setCombo(player.getCombo() + 1);
    }

    public List<JobEnum> getAdvancedJobs(int jobId) {
        return JobEnum.getAdvancedJobs(jobId);
    }

    public int getJobReqLev(int jobId) {
        return JobConstants.getJobReqLev(jobId);
    }

    public boolean changeJob(int jobId) {
        return getChar().changeJob(jobId);
    }
}
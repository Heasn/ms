package im.cave.ms.client.quest.reward;


import im.cave.ms.client.character.MapleCharacter;
import im.cave.ms.connection.packet.UserPacket;


public class QuestMoneyReward implements QuestReward {
    private long money;

    public QuestMoneyReward(long money) {
        this.money = money;
    }

    public QuestMoneyReward() {

    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }


    @Override
    public void giveReward(MapleCharacter chr) {
        chr.addMeso(money);
        chr.announce(UserPacket.incMoneyMessage((int) money));
    }

}

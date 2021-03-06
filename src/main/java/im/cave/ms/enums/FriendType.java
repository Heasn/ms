package im.cave.ms.enums;

import im.cave.ms.tools.Util;

import java.util.Arrays;


public enum FriendType {
    // Not sure about requests
    FriendReq_LoadFriend(0),
    FriendReq_SetFriend(1),
    FriendReq_AcceptFriend(2),
    FriendReq_AcceptAccountFriend(3),
    FriendReq_DeleteFriend(4),
    FriendReq_DeleteAccountFriend(5),
    FriendReq_RefuseFriend(6),
    FriendReq_RefuseAccountFriend(7),
    FriendReq_NotifyLogin(8),
    FriendReq_NotifyLogout(9),
    FriendReq_IncMaxCount(10),
    FriendReq_ConvertAccountFriend(11),
    FriendReq_ModifyFriend(12),
    FriendReq_ModifyFriendGroup(13),
    FriendReq_ModifyAccountFriendGroup(14),
    FriendReq_SetOffline(15),
    FriendReq_SetOnline(16),
    FriendReq_SetBlackList(17),
    FriendReq_DeleteBlackList(18),
    FriendReq_LoadFriendPointInfo(19),
    //FriendReq_LoadFriendChatN(20),
    FriendReq_InviteEventBestFriend(20),
    FriendReq_AcceptEventBestFriend(21),
    FriendReq_RefuseEventBestFriend(22),

    // v203.2
    FriendRes_LoadFriend_Done(23),
    FriendRes_LoadAccountIDOfCharacterFriend_Done(25),
    FriendRes_NotifyChange_FriendInfo(26),
    FriendRes_Invite(27),
    FriendRes_SetFriend_Done(28),
    FriendRes_SetFriend_FullMe(28),
    FriendRes_SetFriend_FullOther(29),
    FriendRes_SetFriend_AlreadySet(30),
    FriendRes_SetFriend_AlreadyRequested(31),
    FriendRes_SetFriend_Ready(32),
    FriendRes_SetFriend_CantSelf(33),
    FriendRes_SetFriend_Master(34),
    FriendRes_SetFriend_UnknownUser(36),
    FriendRes_SetFriend_Unknown(36),
    FriendRes_UnknownError(37),
    FriendRes_SetMessengerMode(38),
    FriendRes_SendSingleFriendInfo(41),
    FriendRes_AcceptFriend_Unknown(42),
    FriendRes_DeleteFriend_Done(43),
    FriendRes_DeleteFriend_Unknown(44),
    FriendRes_Notify(45),
    FriendRes_NotifyNewFriend(46),
    FriendRes_IncMaxCount_Done(47),
    FriendRes_IncMaxCount_Unknown(46),

    FriendRes_SetFriend_Done_ForFarm(47),
    FriendRes_Invite_ForFarm(48),
    FriendRes_Accept_ForFarm(49),
    FriendRes_Unknown(50),
    FriendRes_SetFriend_BlockedBehavior(51),
    FriendRes_Notice_Deleted(52),
    FriendRes_InviteEventBestFriend(53),
    FriendRes_RefuseEventBestFriend(54),
    // Star planet after this, not interesting
    ;

    private int val;

    FriendType(int val) {
        this.val = val;
    }

    public static FriendType getByVal(int val) {
        return Util.findWithPred(Arrays.asList(values()), ft -> ft.getVal() == val);
    }

    public int getVal() {
        return val;
    }
}

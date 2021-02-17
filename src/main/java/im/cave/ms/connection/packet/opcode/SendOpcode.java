package im.cave.ms.connection.packet.opcode;

import im.cave.ms.tools.Util;

public enum SendOpcode {
    LOGIN_STATUS(0x00),
    SERVERLIST(0x01),
    REQUEST_SHARE_INFORMATION(0x04),
    SERVERSTATUS(0x05),//CHECK_USER_LIMIT_RESULT
    CHARLIST(0x06),
    SELECT_CHARACTER_RESULT(0x07),
    AUTH_SUCCESS(0x08),
    CHECK_DUPLICATED_ID_RESULT(0x0A),
    CREATE_NEW_CHARACTER_RESULT(0x0B),
    DELETE_CHAR_TIME(0x0D),
    CANCEL_DELETE_CHAR(0x0E),
    CHANGE_CHANNEL(0x10),
    PING(0x11),
    CPING(0x12),
    //
    //
    //
    //////////
    CLIENT_START(0x2A),//2c-2
    INIT_OPCODE_ENCRYPTION(0x2B), //177 2d - 2
    TOS_ACCEPT_RESULT(0xB9), //177
    REQUIRE_SET_ACC_GENDER(0x3E), //177
    SET_ACC_GENDER_RESULT(0x3F), //177
    CHAR_SLOTS_EXPAND_RESULT(0x4B),
    OPEN_CREATE_CHAR_LAYOUT_RESULT(0x4F), //56 - 7
    SERVER_LIST_BG(0x56), // 5D-7
    INVENTORY_OPERATION(0x68),  //6F - 7
    INVENTORY_GROW(0x69),//
    UPDATE_STATS(0x6A),//
    GIVE_BUFF(0x6B),//
    REMOVE_BUFF(0x6C),//
    //
    //
    //
    CHANGE_SKILL_RESULT(0x70), //77-7
    FAME_RESPONSE(0x7A), //
    MESSAGE(0x7C), //83 - 7
    MAPLE_NOTES(0x7D), // -7
    MAP_TRANSFER_RESULT(0x7E),//-7
    GATHER_ITEM_RESULT(0x94), //-7
    SORT_ITEM_RESULT(0x95),//
    UNK96(0x96),//
    CHAR_INFO(0x97), //9F- 8
    PARTY_RESULT(0x98),//A0-8
    RECOMMEND_PLAYER(0x99),//a1-8
    //9A
    //9B
    //9C
    //9D
    //9F
    //A0
    //A1
    FRIEND_RESULT(0xA2), //AA-8
    GUILD_REQUEST(0xA3),//
    GUILD_RESULT(0xA4), //AD - 9
    ALLIANCE_RESULT(0xA5),//
    TOWN_PORTAL(0xA6),//
    BROADCAST_MSG(0xA7), //B0 - 9 //broadcast message
    ACHIEVEMENT(0xAE),
    CASH_PET_PICK_UP_ON_OFF_RESULT(0xB0),//b9 - 9
    PET_AUTO_EAT_MSG(0xB1),//
    LIMITED_NPC_DISABLE_INFO(0xB9), //hide?
    //c1  0A 00 6B 69 6C 6C 5F 63 6F 75 6E 74 01 00 30 ..kill_count..0
    //c5
    EVENT_MESSAGE(0xD1),//
    SERVER_NOTICE(0xCD),//
    DEBUG_MSG(0xD4),//
    RESULT_INSTANCE_TABLE(0xDE),//e7-9
    LINKED_MESSAGE(0xE5),
    REMOVE_SON_OF_LINKED_SKILL_RESULT(0xE6),
    SET_SON_OF_LINKED_SKILL_RESULT(0xE7),
    DOJO_RANK(0xEE),//F7-9
    CHARACTER_POTENTIAL_SET(0xF9), //102-9
    CHARACTER_POTENTIAL_RESET(0xFA),//
    CHARACTER_HONOR_POINT(0xFB),//
    MEMORIAL_CUBE_RESULT(0x11B),
    BLACK_CUBE_RESULT(0x11C),
    MEMORIAL_CUBE_MODIFIED(0x11E),
    CHANGE_CHAR_KEY(0x125),// - 9
    GUILD_RANK(0x129),
    GUILD_SEARCH_RESULT(0x12A),
    SCAN_RUNNING_PROCESS(0x130),  //maybe  01 bool
    UPDATE_QUEST_EX(0x132),// - 9
    SET_ACCOUNT_INFO(0x133), //13C - 9
    SET_AVATAR_MEGAPHONE(0x136), //-9
    CANCEL_TITLE_EFFECT(0x13C), //-9
    //
    //
    //
    //
    //
    //
    //
    //
    //
    EQUIPMENT_ENCHANT(0x161), //16A-9
    CASH_SHOP_EVENT_INFO(0x170),//179-9
    CHAR_AVATAR_CHANGE_EYES_COLOR(0x1D6),
    CHAR_AVATAR_CHANGE_RESULT(0x1D7), //38
    CHAR_AVATAR_CHANGE_SELECT(0x1DC),//0x1E9-d
    POTION_POT_MESSAGE(0x1E6),//
    //
    POTION_POT_UPDATE(0x1E8),//
    CHARACTER_MODIFIED(0x1F0), //1fb-b
    UPDATE_MAPLE_POINT(0x1F6),//201-b

    CASH_POINT_RESULT(0x20E), //219-b
    SLOT_EXPAND_RESULT(0x20F),//
    ONLINE_REWARD_RESULT(0x212),
    MACRO_SYS_DATA_INIT(0x22A),//235-b
    SET_MAP(0x22B), //236 - B
    SET_AUCTION(0x22C),
    SET_CASH_SHOP(0x22E),//-b
    SET_CASH_SHOP_INFO(0x22F),//
    WHISPER(0x237),//
    FIELD_EFFECT(0x239),//
    FIELD_MESSAGE(0x23A), //
    CLOCK(0x23F),//
    QUICKSLOT_INIT(0x24D),  //258 -B
    nnnnn(0x252),
    QUICK_MOVE(0x260),//26b - b
    //28e user_stand_up
    USER_SIT(0x28F), //29B-C
    USER_ENTER_FIELD(0x2AB), //2b5 -a
    USER_LEAVE_FIELD(0x2AC), // 2b6 - a
    CHAT(0x2AD), //2b7 - a
    UNK2AE(0x2AE),//
    BLACK_BOARD(0x2AF),//2b9 - a //小黑板
    SHOW_ITEM_UPGRADE_EFFECT(0x2B2),//
    SHOW_ITEM_SKILL_SOCKET_UPGRADE_EFFECT(0x2B3),
    SHOW_ITEM_RELEASE_EFFECT(0x2B6),//
    SHOW_CUBE_EFFECT(0x2B9), //唯一魔方 8F 1D 00 00  01  8A 3D 4D 00 00 00 00 00 00 00 00 00
    SHOW_ITEM_ADDITIONAL_RELEASE_EFFECT(0x2BA), //8F 1D 00 00 char Id| 01 | 64 3F 4D 00 itemId|00 00 00 00 00 00 00 00
    ADDITIONAL_CUBE_RESULT(0x2BF),
    SET_DAMAGE_SKIN(0x2D2),//
    SET_SOUL_EFFECT(0x2D6),//
    SIT_RESULT(0x2D7), //2e1-A
    FAMILIAR(0x2D8),//
    LEGACY_CUBE_RESULT(0x2E2),
    //2e6
    PET_TRAINING_EFFECT(0x306),
    HIDDEN_EFFECT_EQUIP(0x30F), // -7
    LIMIT_BREAK_UI(0x310),//-7
    PET_ACTIVATED(0x311),//-7
    PET_MOVE(0x312),//-7
    PET_ACTION_SPEAK(0x313),//-7
    PET_NAME_CHANGED(0x315),//-7
    PET_LOAD_EXCEPTION_LIST(0x316),//-7
    //
    //
    //
    //
    PET_ACTION(0x31A),
    PET_ACTION_COMMAND(0x31B),//
    ANDROID_CREATED(0x320),//
    ANDROID_MOVE(0x321),//
    ANDROID_ACTION_SET(0x322),//
    ANDROID_MODIFIED(0x323),//
    ANDROID_REMOVED(0x324), //
    SKILL_PET_MOVE(0x32C),

    REMOTE_MOVE(0x33E), //345-7
    REMOTE_CLOSE_RANGE_ATTACK(0x33F),//
    REMOTE_RANGED_ATTACK(0x340),//
    REMOTE_MAGIC_ATTACK(341),//
    //
    //
    //
    REMOTE_SKILL_CANCEL(0x345), //charId + skillId
    REMOTE_HIT(0x346),//
    REMOTE_EMOTION(0x347),//
    //
    //
    //
    //
    //
    //
    //
    //
    REMOTE_SET_ACTIVE_PORTABLE_CHAIR(0x350),//
    REMOTE_AVATAR_MODIFIED(0x351), //358-7
    REMOTE_EFFECT(0x352),//359-7
    REMOTE_SET_TEMPORARY_STAT(0x353),
    REMOTE_RESET_TEMPORARY_STAT(0x354),
    REMOTE_RECEIVE_HP(0x355),//
    REMOTE_GUILD_NAME_CHANGED(0x357),
    REMOTE_GUILD_MARK_CHANGED(0x358),
    EFFECT(0x37C), //0x383 - 7
    QUEST_RESULT(0x381),//
    PET_SKILL_CHANGED(0x383),//38A-7
    OPEN_UI(0x389),//-7 1121 服装回收
    CLOSE_UI(0x38A),//
    OPEN_UI_WITH_OPTION(0x38B),//  00 00 00 00 06 00 00 00 00 00 00 00 背包:特殊栏
    SET_DIRECTION_MODE(0x38C),//393-7
    SET_IN_GAME_DIRECTION_MODE(0x38D),//
    SET_STAND_ALONE_MODE(0x38E),//395-7
    NOTICE_MSG(0x39B),//3a2-7
    CHAT_MSG(0x39C),//3a3-7
    BAG_ITEM_USE_RESULT(0x3A9),// //BAG_ITEM_USE_RESULT
    DODGE_SKILL_READY(0x3AE),//闪避技能准备
    IN_GAME_DIRECTION_EVENT(0x3B2),
    DEATH_COUNT(0x3BA),
    CHECK_TRICK_OR_TREAT_RESULT(0x3C1),
    SET_DEAD(0x3C5),//
    OPEN_DEAD_UI(0x3C6),//
    //
    //
    //
    //
    //
    //
    //
    //
    FINAL_ATTACK(0x3CF), //3d5 - 6
    ILLUSTRATION_MSG(0x3FF), //405-6
    LEOPARD_SKILL_USE(0x401),//
    SKILL_COOLTIME_SET(0x409), //40F-6
    ADDITIONAL_ATTACK(0x424),//
    DAMAGE_SKIN_SAVE_RESULT(0x427),//42D-6
    OPEN_WORLDMAP(0x48F), //没变
    UNK498(0x498),
    SPAWN_SUMMON(0x4BA),//+3
    REMOVE_SUMMON(0x4BB), //03 00 00 00 01 00 00 00 01
    SUMMON_MOVE(0x4BC),//+3
    SUMMONED_ATTACK(0x4BD),
    SUMMONED_ATTACK_PVP(0x4BE),
    SUMMONED_SET_REFERENCE(0x4BF),
    SUMMON_SKILL(0x4C0),
    SPAWN_MOB(0x4D8), //4d5+3
    REMOVE_MOB(0x4D9),//4d6+3
    MOB_CHANGE_CONTROLLER(0x4DA),//4d7+3
    MOB_MOVE(0x4E1), //4de+3
    MOB_CONTROL_ACK(0x4E2), //4DF+3
    //MOB_CONTROL_HINT
    MOB_STAT_SET(0x4E4),//
    MOB_STAT_RESET(0x4E5),//
    MOB_SUSPEND_RESET(0x4E6),//
    MOB_AFFECTED(0x4E7),//
    MOB_DAMAGED(0x4E8),//
    HP_INDICATOR(0x4EF), //4ec+3
    MOB_SKILL_DELAY(0x4F5),
    SPAWN_NPC(0x54B), //542 + 9
    REMOVE_NPC(0x54C),//543+9
    //54D    544+9
    SPAWN_NPC_REQUEST_CONTROLLER(0x54E), //545+9
    //
    //
    //
    NPC_ANIMATION(0x552), //549 + 9
    DROP_ENTER_FIELD(0x571), //569+8
    DROP_LEAVE_FIELD(0x573), //56b+8
    REACTOR_CHANGE_STATE(0x582),
    REACTOR_ENTER_FIELD(0x584),//
    REACTOR_REMOVE(0x586),//
    NPC_TALK(0x729), //741-18
    NPC_SHOP_OPEN(0x72A),//
    NPC_SHOP_RESULT(0x72B),//

    TRUNK_OPERATION(0x742),//75a-18
    CHAT_ROOM(0x747), //-19
    TRADE_ROOM(0x748), //761-19
    EXPRESS(0x7AA),
    CASH_SHOP_QUERY_CASH_RESULT(0X7AC), //0x7be-12
    CASH_SHOP_CASH_ITEM_RESULT(0x7AD),//
    CASH_SHOP_SAVE_COLLOCATION_RESULT(0x7B3), //
    POTION_POT_CREATE(0x7C8), //
    AUCTION(0x7CB),
    SIGNIN_INIT(0x7DB), //
    OPEN_UNITY_PORTAL(0x7DF), //7f3-14
    LEGION(0x7E1),
    KEYMAP_INIT(0x831), //84A - 19
    BATTLE_ANALYSIS(0x863),//879-16
    REMAINING_MAP_TRANSFER_COUPON(0x882),//897-15
    ;
    private final int code;

    SendOpcode(int code) {
        this.code = code;
    }

    public short getValue() {
        return (short) code;
    }

    public static Object getByValue(short op) {
        return Util.findWithPred(values(), sendOpcode -> sendOpcode.code == op);
    }

}

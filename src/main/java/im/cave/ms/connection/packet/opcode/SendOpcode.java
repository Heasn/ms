package im.cave.ms.connection.packet.opcode;

import im.cave.ms.tools.Util;

public enum SendOpcode {
    LOGIN_STATUS(0x00),
    WORLD_INFORMATION(0x01),
    //LATEST_CONNECTED_WORLD
    //RECOMMENDED_WORLD_MESSAGE
    REQUEST_SHARE_INFORMATION(0x04), // 00 00 00 00
    SELECT_WORLD_BUTTON(0x05),//CHECK_USER_LIMIT_RESULT
    SELECT_WORLD_RESULT(0x06),
    SELECT_CHARACTER_RESULT(0x07),
    ACCOUNT_INFO_RESULT(0x08),
    //CREATE_MAPLE_ACCOUNT_RESULT
    CHECK_DUPLICATED_ID_RESULT(0x0A),
    CREATE_NEW_CHARACTER_RESULT(0x0B),
    //DELETE_CHARACTER_RESULT
    RESERVED_DELETE_CHARACTER_RESULT(0x0D),
    RESERVED_DELETE_CHARACTER_CANCEL_RESULT(0x0E),
    //SET_CHARACTER_ID
    CHANGE_CHANNEL(0x10),
    PING(0x11),
    C_PING(0x12),
    UNK13(0x13),
    AUTHEN_CODE_CHANGED(0x14),
    AUTHEN_MESSAGE(0x15),
    UNK16(0x16),
    SECURITY_PACKET(0x17),
    PRIVATE_SERVER_PACKET(0x18),
    //
    //
    //
    CLIENT_START(0x2A),
    INIT_OPCODE_ENCRYPTION(0x2B),
    //
    //
    //
    //
    //
    SERVER_STATE_RESULT(0x32),
    SERVER_KEY_VALUE(0x33),
    REQUIRE_SET_ACC_GENDER(0x3E),
    SET_ACC_GENDER_RESULT(0x3F),
    CHAR_SLOTS_EXPAND_RESULT(0x4B),
    OPEN_CREATE_CHAR_LAYOUT_RESULT(0x4F),
    UNK55(0x55),
    SERVER_LIST_BG(0x56),
    UNK5F(0x5F),
    UNK60(0x60),
    UNK61(0x61),
    UNK62(0x62),
    UNK63(0x63),
    UNK64(0x64),
    UNK65(0x65),
    UNK66(0x66),
    RELOAD_BACK(0x67),
    INVENTORY_OPERATION(0x68),  //6F - 7
    INVENTORY_GROW(0x69),//
    STAT_CHANGED(0x6A),//
    GIVE_BUFF(0x6B),//
    REMOVE_BUFF(0x6C),//
    FORCED_STAT_SET(0x6D),
    FORCED_STAT_RESET(0x6E),
    UNK6F(0x6F),
    CHANGE_SKILL_RECORD_RESULT(0x70),
    CHANGE_STEAL_MEMORY_RESULT(0x71),
    USER_DAMAGE_ON_FALLING_CHECK(0x72),
    PERSONAL_SHOP_BUY_CHECK(0x73),
    MOB_DROP_MESO_PICKUP(0x74),
    BREAK_TIME_FIELD_ENTER(0x75),
    RUNE_ACT_SUCCESS(0x76),
    RESULT_STEAL_SKILL_LIST(0x77),
    SKILL_USE_RESULT(0x78),
    EXCL_REQUEST(0x79),
    FAME_RESPONSE(0x7A),
    UNK7B(0x7B),
    MESSAGE(0x7C),
    MAPLE_NOTES(0x7D),
    MAP_TRANSFER_RESULT(0x7E),
    ANTI_MACRO_RESULT(0x7F),
    UNK80(0x80),
    UNK81(0x81),
    UNK82(0x82),
    UNK83(0x83),
    UNK84(0x84),
    CLAIM_RESULT(0x85),
    SET_CLAIM_SVR_AVAILABLE_TIME(0x86), //ClientException
    CLAIM_SVR_STATUS_CHANGED(0x88),
    UNK89(0x89),
    STAR_PLANET_USER_COUNT(0x8A),
    SET_TAMING_MOB_INFO(0x8B),
    QUEST_CLEAR(0x8C),
    ENTRUSTED_SHOP_CHECK_RESULT(0x8D),
    SKILL_LEARN_ITEM_RESULT(0x8E),
    SKILL_RESET_ITEM_RESULT(0x8F),
    ABILITY_RESET_ITEM_RESULT(0x90),
    EXP_CONSUME_ITEM_RESULT(0x91),
    EXP_ITEM_GET_RESULT(0x92),
    CHAR_SLOT_INC_RESULT(0x93),
    GATHER_ITEM_RESULT(0x94), //-7
    SORT_ITEM_RESULT(0x95),//
    UNK96(0x96),//
    CHAR_INFO(0x97), //9F- 8
    PARTY_RESULT(0x98),//A0-8
    PARTY_MEMBER_CANDIDATE_RESULT(0x99),
    URUS_PARTY_MEMBER_CANDIDATE_RESULT(0x9A),
    PARTY_CANDIDATE_RESULT(0x9B),
    URUS_PARTY_RESULT(0x9C),
    INTRUSION_FRIEND_CANDIDATE_RESULT(0x9D),
    INTRUSION_LOBBY_CANDIDATE_RESULT(0x9E),
    EXPEDITION_RESULT(0x9F),
    STAR_FRIEND_RESULT(0xA0),
    LOAD_ACCOUNT_ID_OF_CHARACTER_FRIEND_RESULT(0xA1),
    FRIEND_RESULT(0xA2),
    GUILD_REQUEST(0xA3),
    GUILD_RESULT(0xA4),
    ALLIANCE_RESULT(0xA5),
    TOWN_PORTAL(0xA6),
    BROADCAST_MSG(0xA7),
    INCUBATOR_RESULT(0xA8),
    INCUBATOR_HOT_ITEM_RESULT(0xA9),
    SHOP_SCANNER_RESULT(0xAA),
    SHOP_LINK_RESULT(0xAB),
    MARRIAGE_REQUEST(0xAC),
    MARRIAGE_RESULT(0xAD),
    ACHIEVEMENT(0xAE),//成就系统
    CASH_PET_FOOD_RESULT(0xAF),
    CASH_PET_PICK_UP_ON_OFF_RESULT(0xB0),
    CASH_PET_SKILL_SETTING_RESULT(0xB1),//宠物自动喂养
    CASH_LOOK_CHANGE_RESULT(0xB2),
    CASH_PET_DYEING_RESULT(0xB3),
    SET_WEEK_EVENT_MESSAGE(0xB4),
    SET_POTION_DISCOUNT_RATE(0xB5),
    BRIDLE_MOB_CATCH_FAIL(0xB6),
    IMITATED_NPC_RESULT(0xB7),
    IMITATED_NPC_DATA(0xB8),
    LIMITED_NPC_DISABLE_INFO(0xB9), //hide?
    MONSTER_BOOK_SET_CARD(0xBA),
    MONSTER_BOOK_SET_COVER(0xBB),
    HOUR_CHANGE(0xBC),
    MINIMAP_ON_OFF(0xBD), // 01
    CONSULT_AUTH_KEY_UPDATE(0xBF),
    CLASS_COMPETITION_AUTH_KEY_UPDATE(0xC0),
    WEB_BOARD_AUTH_KEY_UPDATE(0xC1),
    SESSION_VALUE(0xC2),
    PARTY_VALUE(0xC3),
    FIELD_SET_VARIABLE(0xC4),
    FIELD_VALUE(0xC5),
    BONUS_EXP_RATE_CHANGED(0xC6),
    NOTIFY_LEVEL_UP(0xC7), //check
    NOTIFY_WEDDING(0xC8),
    NOTIFY_JOB_CHANGE(0xC9),
    SET_BUY_EQUIP_EXT(0xCA),
    SET_PASSENSER_REQUEST(0xCB),
    SCRIPT_PROGRESS_MESSAGE_BY_SOUL(0xCC),
    SCRIPT_PROGRESS_MESSAGE(0xCD),
    SCRIPT_PROGRESS_ITEM_MESSAGE(0xCE),
    STATIC_SCREEN_MESSAGE(0xCF),
    OFF_STATIC_SCREEN_MESSAGE(0xD0),
    WEATHER_EFFECT_NOTICE(0xD1),
    WEATHER_EFFECT_NOTICE_Y(0xD2),
    D3(0xD3),
    PROGRESS_MESSAGE_FONT(0xD4),//
    DATA_CRC_CHECK_FAILED(0xD5),
    SHOW_SLOT_MESSAGE(0xD6),
    WILD_HUNTER_INFO(0xD7),
    ZERO_INFO(0xD8),
    ZERO_WP(0xD9),
    ZERO_INFO_SUB_HP(0xDA),
    UI_OPEN(0xDB),
    CLEAR_ANNOUNCED_QUEST(0xDC),
    RESULT_INSTANCE_TABLE(0xDE),//e7-9
    COOL_TIME_SET(0xDF),
    ITEM_POT_CHANGE(0xE0),
    SET_ITEM_COOL_TIME(0xE1),
    SET_AD_DISPLAY_INFO(0xE2),
    SET_AD_DISPLAY_STATUS(0xE3),
    //
    LINKED_MESSAGE(0xE5),
    REMOVE_SON_OF_LINKED_SKILL_RESULT(0xE6),
    SET_SON_OF_LINKED_SKILL_RESULT(0xE7),
    SON_OF_LINKED_SKILL_MESSAGE(0xE8),
    E9(0xE9),
    EA(0xEA),
    SET_MAPLE_STYLE_INFO(0xEB),
    EC(0xEC),
    ED(0xED),
    DOJO_RANK_RESULT(0xEE),//F7-9
    //EF
    //F0
    //F1
    //F2
    //F3
    //F4
    //F5
    //F6
    START_NAVIGATION(0xF7),
    FUNC_KEY_SET_BY_SCRIPT(0xF8),
    CHARACTER_POTENTIAL_SET(0xF9), //102-9
    CHARACTER_POTENTIAL_RESET(0xFA),//
    CHARACTER_HONOR_POINT(0xFB),//
    READY_FOR_RESPAWN(0xFC),
    READY_FOR_RESPAWN_BY_POINT(0xFD),
    OPEN_READY_FOR_RESPAWN_UI(0xFE),
    CHARACTER_HONOR_GIFT(0xFF),
    CROSS_HUNTER_COMPLETE_RESULT(0x100),
    CROSS_HUNTER_SHOP_RESULT(0x101),
    UNK_102(0x102),
    SET_CASH_ITEM_NOTICE(0x103),
    SET_SPECIAL_CASH_ITEM(0x104),
    SHOW_EVENT_NOTICE(0x105),
    BOARD_GAME_RESULT(0x106),
    YUT_GAME_RESULT(0x107),
    VALUE_PACK_RESULT(0x108),
    NAVI_FLYING_RESULT(0x109),
    SET_EXCL_REQUEST_SENT(0x10A),
    CHECK_WEDDING_EX_RESULT(0x10B),
    BINGO_RESULT(0x10C),
    BINGO_CASSANDRA_RESULT(0x10D),
    UPDATE_VIP_GRADE(0x10E),
    MESO_RANGER_RESULT(0x10F),
    SET_MAPLE_POINT(0x110),
    SET_MIRACLE_TIME_INFO(0x111),
    UNK_112(0x112),
    HYPER_SKILL_RESET_RESULT(0x113),
    GET_SERVER_TIME(0x114),
    GET_CHARACTER_POSITION(0x115),
    UNK_116(0x116),
    UNK_117(0x117),
    SET_FIX_DAMAGE(0x118),
    RETURN_EFFECT_CONFIRM(0x119),
    RETURN_EFFECT_MODIFIED(0x11A),
    MEMORIAL_CUBE_RESULT(0x11B),
    BLACK_CUBE_RESULT(0x11C),
    //
    MEMORIAL_CUBE_MODIFIED(0x11E),
    //
    //
    DRESS_UP_INFO_MODIFIED(0x121),
    //
    RESET_STATE_FOR_OFF_SKILL(0x123),
    SET_OFF_STATE_FOR_OFF_SKILL(0x124),
    RELOGIN_AUTH_KEY(0x125),
    AVATAR_PACK_TEST(0x126),
    EVOLVING_RESULT(0x127),
    ACTION_BAR_RESULT(0x128),
    GUILD_CONTENT_RESULT(0x129),
    GUILD_SEARCH_RESULT(0x12A),
    //
    //
    //
    //
    //
    CHECK_PROCESS_RESULT(0x130),  //  01 bool
    //
    UPDATE_QUEST_EX(0x132),
    SET_ACCOUNT_INFO(0x133),
    //
    //
    SET_AVATAR_MEGAPHONE(0x136), //-9
    //
    //
    //
    //
    //
    CANCEL_TITLE_EFFECT(0x13C),

    EQUIPMENT_ENCHANT(0x161),

    CASH_SHOP_EVENT_INFO(0x170),

    NICK_ITEM_CHANGED(0x185),
    OPEN_V_UI(0x1A4),
    LEGIONS(0x1AE),
    CHAR_AVATAR_CHANGE_EYES_COLOR(0x1D7),
    CHAR_AVATAR_CHANGE_RESULT(0x1D8),
    CHAR_AVATAR_CHANGE_SELECT(0x1DC),

    POTION_POT_MESSAGE(0x1E9),
    //
    POTION_POT_UPDATE(0x1EB),
    //
    //
    //
    //
    //
    //
    //
    CHARACTER_MODIFIED(0x1F3),
    //
    //
    //
    //
    //
    UPDATE_MAPLE_POINT(0x1F9),


    CASH_POINT_RESULT(0x211),
    SLOT_EXPAND_RESULT(0x212),
    //
    //
    HOTTIME_REWARD_RESULT(0x215),

    MACRO_SYS_DATA_INIT(0x22D),
    SET_MAP(0x22E),
    SET_AUCTION(0x22F),
    //
    SET_CASH_SHOP(0x231),
    SET_CASH_SHOP_INFO(0x232),
    //
    //
    //
    //
    //
    //
    //
    //
    WHISPER(0x23A),
    //
    FIELD_EFFECT(0x23C),
    FIELD_MESSAGE(0x23D),
    //
    //
    //
    //
    CLOCK(0x242),
    //
    //
    //
    UNK246(0x246),
    //
    //
    //
    QUICKSLOT_INIT(0x250),
    //
    //
    //
    //
    UNK255(0x255),
    //
    //
    //
    //
    //
    //
    //
    //
    CREATE_FORCE_ATOM(0x25E),
    //
    //
    //
    PROGRESS(0x262),
    QUICK_MOVE(0x263),
    CREATE_OBSTACLE(0x264),
    UNK265(0x265),
    CLEAR_OBSTACLE(0x266),
    UNK267(0x267),
    B2_FOOT_HOLD_CREATE(0x268),
    DEBUFF_OBJ_ON(0X269),
    CREATE_FALLING_CATCHER(0X26A),
    CHASE_EFFECT_SET(0X26B),
    MESO_EXCHANGE_RESULT(0X26C),
    SET_MIRROR_DUNGEINFO(0X26D),
    SET_INTRUSION(0x26E),
    CANNOT_DROP(0x26F),
    FOOT_HOLD_OFF(0x270),
    LADDER_ROPE_OFF(0x271),
    MOMENT_AREA_OFF(0x272),
    MOMENT_AREA_OFF_ALL(0x273),
    CHAT_LET_CLIENT_CONNECT(0x274),
    CHAT_INDUCE_CLIENT_CONNECT(0x275),
    PACKET(0x276),
    ELITE_STATE(0x277),
    PLAY_SOUND(0x278),
    STACK_EVENT_GAUGE(0x279),
    SET_UNIFIELD(0x27A),
    STAR_PLANET_BURNING_TIME_INFO(0x27B),

    UNK290(0x290),
    UNK291(0x291), //和其他角色相关的 【多人】
    USER_SIT(0x292),

    USER_ENTER_FIELD(0x2AE),
    USER_LEAVE_FIELD(0x2AF),
    CHAT(0x2B0),
    AD_BOARD(0x2B1),
    BLACK_BOARD(0x2B2),
    //
    //
    SHOW_ITEM_UPGRADE_EFFECT(0x2B5),
    SHOW_ITEM_SKILL_SOCKET_UPGRADE_EFFECT(0x2B6),
    SHOW_ITEM_SKILL_OPTION_UPGRADE_EFFECT(0x2B7),
    SHOW_ITEM_RELEASE_EFFECT(0x2B9),
    //
    SHOW_CUBE_EFFECT(0x2BC),
    SHOW_ITEM_ADDITIONAL_RELEASE_EFFECT(0x2BD),
    SHOW_ITEM_SLOT_OPTION_EXTEND_EFFECT(0x2BE),
    //
    //
    //
    //
    ADDITIONAL_CUBE_RESULT(0x2C2),
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    SET_DAMAGE_SKIN(0x2D5),
    //
    //
    //
    SET_SOUL_EFFECT(0x2D9),
    SIT_RESULT(0x2DA),
    FAMILIAR(0x2DB),


    LEGACY_CUBE_RESULT(0x2E8),

    REFLECTION_CUBE_RESULT(0X307),
    PET_TRAINING_EFFECT(0x309),
    //
    //
    //
    //
    //
    HIDDEN_EFFECT_EQUIP(0x312),
    LIMIT_BREAK_UI(0x313),
    PET_ACTIVATED(0x314),
    PET_MOVE(0x315),
    PET_ACTION_SPEAK(0x316),
    //
    PET_NAME_CHANGED(0x318),
    PET_LOAD_EXCEPTION_LIST(0x319),
    //
    //
    //
    //
    PET_ACTION_COMMAND(0x31E),
    //
    //
    //
    PET_ACTION(0x322),
    ANDROID_CREATED(0x323),
    ANDROID_MOVE(0x324),
    ANDROID_ACTION_SET(0x325),
    ANDROID_MODIFIED(0x326),
    ANDROID_REMOVED(0x327),
    //
    //
    //
    //
    SKILL_PET_MOVE(0x32C),
    //
    //
    //
    //
    //
    //
    //
    //
    //
    REMOTE_MOVE(0x341),
    REMOTE_CLOSE_RANGE_ATTACK(0x342),
    REMOTE_RANGED_ATTACK(0x343),
    REMOTE_MAGIC_ATTACK(344),
    //
    //
    //
    REMOTE_SKILL_CANCEL(0x348),
    REMOTE_HIT(0x349),
    REMOTE_EMOTION(0x34A),
    //
    //
    //
    REMOTE_NICK_ITEM(0x34E),
    //
    //
    //
    //
    REMOTE_SET_ACTIVE_PORTABLE_CHAIR(0x353),
    REMOTE_AVATAR_MODIFIED(0x354),
    REMOTE_EFFECT(0x355),
    REMOTE_SET_TEMPORARY_STAT(0x356),
    REMOTE_RESET_TEMPORARY_STAT(0x357),
    REMOTE_RECEIVE_HP(0x358),
    REMOTE_GUILD_NAME_CHANGED(0x359),
    REMOTE_GUILD_MARK_CHANGED(0x35A),

    ADELE_POWER(0x37B),

    EFFECT(0x37F),
    TELEPORT(0x380),
    //
    //
    //
    QUEST_RESULT(0x384),
    //
    PET_SKILL_CHANGED(0x386),
    BALLOON_MSG(0x387),
    //
    //
    //
    //
    //
    OPEN_UI(0x38C),
    CLOSE_UI(0x38D),
    OPEN_UI_WITH_OPTION(0x38E),//  00 00 00 00 06 00 00 00 00 00 00 00 背包:特殊栏
    SET_DIRECTION_MODE(0x38F),
    SET_IN_GAME_DIRECTION_MODE(0x390),
    SET_STAND_ALONE_MODE(0x391),

    NOTICE_MSG(0x39E),
    CHAT_MSG(0x39F),

    BAG_ITEM_USE_RESULT(0x3A9),// //BAG_ITEM_USE_RESULT
    DODGE_SKILL_READY(0x3AE),//闪避技能准备
    IN_GAME_DIRECTION_EVENT(0x3B2),

    LIFE_COUNT(0x3BD),
    //
    //
    //
    //
    //
    //
    CHECK_TRICK_OR_TREAT_RESULT(0x3C4),
    //
    //
    USER_B2_BODY(0x3C7),
    SET_DEAD(0x3C8),//
    OPEN_DEAD_UI(0x3C9),//
    //
    //
    //
    //
    //
    //
    //
    //
    FINAL_ATTACK(0x3D2),


    ILLUSTRATION_MSG(0x402),
    //
    LEOPARD_SKILL_USE(0x404),
    //
    //
    //
    //
    //
    //
    //
    SKILL_COOLTIME_SET(0x40C),

    ADDITIONAL_ATTACK(0x424),
    DAMAGE_SKIN_SAVE_RESULT(0x427),

    OPEN_WORLDMAP(0x496),

    MULTI_PERSON_CHAIR_AREA(0x49F),

    SPAWN_SUMMON(0x4C1),
    REMOVE_SUMMON(0x4C2),
    SUMMON_MOVE(0x4C3),
    SUMMONED_ATTACK(0x4C4),
    SUMMONED_ATTACK_PVP(0x4C5),
    SUMMONED_SET_REFERENCE(0x4C6),
    SUMMON_SKILL(0x4C7),
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    SPAWN_MOB(0x4DF),
    REMOVE_MOB(0x4E0),
    MOB_CHANGE_CONTROLLER(0x4E1),
    //
    //
    //
    //
    //
    //
    MOB_MOVE(0x4E8),
    MOB_CONTROL_ACK(0x4E9),
    //4EA
    MOB_STAT_SET(0x4EB),
    MOB_STAT_RESET(0x4EC),
    MOB_SUSPEND_RESET(0x4ED),
    MOB_AFFECTED(0x4EE),
    MOB_DAMAGED(0x4EF),

    HP_INDICATOR(0x4F6),

    MOB_SKILL_DELAY(0x4F5),
    ESCORT_FULL_PATH(0x4F6),
    ESCORT_STOP_END_PERMISSION(0x4F7),
    ESCORT_STOP_BY_SCRIPT(0x4F8),
    ESCORT_STOP_SAY(0x4F9),
    SPAWN_NPC(0x552),
    REMOVE_NPC(0x553),
    //
    SPAWN_NPC_REQUEST_CONTROLLER(0x54E), //545+9
    //
    //
    //
    NPC_ANIMATION(0x552), //549 + 9


    DROP_ENTER_FIELD(0x578),
    //
    DROP_LEAVE_FIELD(0x57A),
    //
    //
    //
    AFFECTED_AREA_CREATED(0x57E),
    INSTALLED_AREA_FIRE(0x57F),
    AFFECTED_AREA_REMOVED(0x580),
    TOWN_PORTAL_CREATED(0x581),
    TOWN_PORTAL_REMOVED(0x582),
    RANDOM_PORTAL_CREATED(0x583),
    RANDOM_PORTAL_TRY_ENTER_REQUEST(0x584),
    RANDOM_PORTAL_REMOVED(0x585),
    OPEN_GATE_CREATED(0x586),
    OPEN_GATE_CLOSE(0x587),
    OPEN_GATE_REMOVED(0x588),
    REACTOR_CHANGE_STATE(0x589),
    //
    REACTOR_ENTER_FIELD(0x58B),//
    REACTOR_RESET_STATE(0x58C),
    REACTOR_OWNER_INFO(0x58D),
    REACTOR_REMOVE(0x58E),//
    //
    //
    //
    //
    //
    REACTOR_LEAVE_FIELD(0x593),


    BINGO(0x6A5),
    BINGO_NUMBER(0x6A6),
    //6A7
    //6AC Bingo


    NPC_TALK(0x734),
    NPC_SHOP_OPEN(0x735),
    NPC_SHOP_RESULT(0x736),
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    TRUNK_OPERATION(0x74D),
    //
    //
    //
    //
    CHAT_ROOM(0x752), //MINI ROOM
    TRADE_ROOM(0x753), //MINI ROOM2


    EXPRESS(0x7AA),
    //
    CASH_SHOP_QUERY_CASH_RESULT(0X7B8),
    CASH_SHOP_CASH_ITEM_RESULT(0x7B9),
    //
    //
    //
    //
    //
    CASH_SHOP_SAVE_COLLOCATION_RESULT(0x7C3),
    CASH_SHOP_UPDATE_STATS(0x7D0), //不对
    POTION_POT_CREATE(0x7D4),

    AUCTION(0x7D7),


    DAILY_GIFT_RESULT(0x7E7),
    //
    //
    //
    OPEN_UNITY_PORTAL(0x7EB),

    HEXAGONAL_CUBE_RESULT(0x804),
    UNIQUE_CUBE_RESULT(0x805),

    CMS_LIMIT(0x814),
    KEYMAP_INIT(0x83F),
    UNK832(0x840), //85 84 1E 00
    UNK833(0x841), //00 00 00 00
    UNK834(0x842), //00 00 00 00
    GOLD_HAMMER_RESULT(0x84B),

    FIELD_ATTACK_CREATE(0x85E),
    FIELD_ATTACK_REMOVE_BY_KEY(0x85F),
    FIELD_ATTACK_REMOVE_LIST(0x860),
    FIELD_ATTACK_REMOVE_ALL(0x861),
    //
    //
    FIELD_ATTACK_SET_ATTACK(0x864),
    FIELD_ATTACK_RESULT_BOARD(0x865),
    FIELD_ATTACK_RESULT_GET_OFF(0x866),
    FIELD_ATTACK_PUSH_ACT(0x867),
    //
    //
    //
    //
    //
    //
    //
    //
    //
    BATTLE_ANALYSIS(0x871),
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

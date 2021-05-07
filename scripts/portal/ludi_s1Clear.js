function enter(pm) {
    let chr = pm.getChar();
    let party = chr.getParty();
    let partyQuest = party.getPartyQuest();
    if (partyQuest.hasPassed(1)) {
        let nextMap = party.getOrCreateFieldById(922010400);
        partyQuest.addMap(nextMap);
        pm.warp(nextMap);
        return true
    }
    return false;
}
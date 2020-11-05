package eu.revamp.hcf.factions.utils.struction;

import lombok.Getter;

public enum Role
{
    LEADER("LEADER", 0, "Leader", "***"), 
    COLEADER("COLEADER", 1, "Co-Leader", "**"), 
    CAPTAIN("CAPTAIN", 2, "Captain", "*"), 
    MEMBER("MEMBER", 3, "Member", "");
    
    @Getter private String name;
    @Getter private String astrix;
    
    Role(String s, int n, String name, String astrix) {
        this.name = name;
        this.astrix = astrix;
    }
}

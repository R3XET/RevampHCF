package eu.revamp.hcf.factions;

public enum FactionType
{
    KOTH("KOTH", 0), 
    CONQUEST("CONQUEST", 1), 
    PLAYER("PLAYER", 2), 
    SAFEZONE("SAFEZONE", 3), 
    ROAD("ROAD", 4), 
    WARZONE("WARZONE", 5), 
    WILDERNESS("WILDERNESS", 6), 
    END_PORTAL("END_PORTAL", 7), 
    GLOWSTONE("GLOWSTONE", 8);
    
    FactionType(String s, int n) {
    }
}

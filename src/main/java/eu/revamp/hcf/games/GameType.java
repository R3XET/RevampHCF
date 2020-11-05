package eu.revamp.hcf.games;

import com.google.common.collect.ImmutableBiMap;
import lombok.Getter;
import eu.revamp.hcf.RevampHCF;
import com.google.common.collect.ImmutableMap;

public enum GameType
{
    CONQUEST("CONQUEST", 0, "", new ConquestType(RevampHCF.getInstance())),
    KOTH("KOTH", 1, "KoTH", new KothType(RevampHCF.getInstance()));
    @Getter private EventGameType eventTracker;
    @Getter private String displayName;
    private static ImmutableMap<String, GameType> byDisplayName;
    private static ImmutableMap.Builder<String, GameType> builder = new ImmutableBiMap.Builder<>();
    
    static {
        GameType[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            GameType eventType = values[i];
            builder.put(eventType.displayName.toLowerCase(), eventType);
        }
        GameType.byDisplayName = builder.build();
    }
    GameType(String s, int n, String displayName, EventGameType eventTracker) {
        this.displayName = displayName;
        this.eventTracker = eventTracker;
    }

    @Deprecated
    public static GameType getByDisplayName(String name) {
        return GameType.byDisplayName.get(name.toLowerCase());
    }
}

package eu.revamp.hcf.factions.utils.zone;

import javax.annotation.Nullable;

import eu.revamp.spigot.utils.time.TimeUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.time.DurationFormatUtils;
import eu.revamp.hcf.utils.Utils;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import eu.revamp.hcf.games.cuboid.Cuboid;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class CaptureZone implements ConfigurationSerializable
{
    public static int MINIMUM_SIZE_AREA = 2;
    private final Object lock;
    private String scoreboardRemaining;
    @Getter @Setter private String name;
    private String prefix;
    @Getter private Cuboid cuboid;
    @Getter private Player cappingPlayer;
    @Getter private long defaultCaptureMillis;
    @Getter private String defaultCaptureWords;
    private long endMillis;
    
    public CaptureZone(String name, Cuboid cuboid, long defaultCaptureMillis) {
        this(name, "", cuboid, defaultCaptureMillis);
    }
    
    public CaptureZone(String name, String prefix, Cuboid cuboid, long defaultCaptureMillis) {
        this.lock = new Object();
        this.name = name;
        this.prefix = prefix;
        this.cuboid = cuboid;
        this.setDefaultCaptureMillis(defaultCaptureMillis);
    }
    
    public CaptureZone(Map<String, Object> map) {
        this.lock = new Object();
        this.name = (String) map.get("name");
        Object obj = map.get("prefix");
        if (obj instanceof String) {
            this.prefix = (String)obj;
        }
        obj = map.get("cuboid");
        if (obj instanceof Cuboid) {
            this.cuboid = (Cuboid)obj;
        }
        this.setDefaultCaptureMillis(Long.parseLong((String) map.get("captureMillis")));
    }
    
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", this.name);
        if (this.prefix != null) {
            map.put("prefix", this.prefix);
        }
        if (this.cuboid != null) {
            map.put("cuboid", this.cuboid);
        }
        map.put("captureMillis", Long.toString(this.defaultCaptureMillis));
        return map;
    }
    
    public String getScoreboardRemaining() {
        synchronized (this.lock) {
            // monitorexit(this.lock)
            return this.scoreboardRemaining;
        }
    }
    
    public void updateScoreboardRemaining() {
        synchronized (this.lock) {
            this.scoreboardRemaining = TimeUtils.getRemaining(this.getRemainingCaptureMillis(), false);
        }
        // monitorexit(this.lock)
    }
    
    public boolean isActive() {
        return this.getRemainingCaptureMillis() > 0L;
    }
    
    public String getPrefix() {
        if (this.prefix == null) {
            this.prefix = "";
        }
        return this.prefix;
    }
    
    public String getDisplayName() {
        return this.getPrefix() + this.name;
    }

    
    public long getRemainingCaptureMillis() {
        if (this.endMillis == Long.MIN_VALUE) {
            return -1L;
        }
        if (this.cappingPlayer == null) {
            return this.defaultCaptureMillis;
        }
        return this.endMillis - System.currentTimeMillis();
    }
    
    public void setRemainingCaptureMillis(long millis) {
        this.endMillis = System.currentTimeMillis() + millis;
    }

    
    public void setDefaultCaptureMillis(long millis) {
        if (this.defaultCaptureMillis != millis) {
            this.defaultCaptureMillis = millis;
            this.defaultCaptureWords = DurationFormatUtils.formatDurationWords(millis, true, true);
        }
    }

    public void setCappingPlayer(@Nullable Player player) {
        this.cappingPlayer = player;
        if (player == null) {
            this.endMillis = this.defaultCaptureMillis;
        }
        else {
            this.endMillis = System.currentTimeMillis() + this.defaultCaptureMillis;
        }
    }
}

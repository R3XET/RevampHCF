package eu.revamp.hcf.factions.type;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.games.cuboid.Cuboid;
import eu.revamp.spigot.utils.time.TimeUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Getter @Setter
public class GlowstoneFaction extends ClaimableFaction implements ConfigurationSerializable
{
    private Long defaultMillisTillReset;
    private Long lastReset;
    private Long timeTillNextReset;
    private Cuboid glowstoneArea;
    boolean active;
    
    public GlowstoneFaction() {
        super("Glowstone");
        this.defaultMillisTillReset = TimeUnit.HOURS.toMillis(1L);
        this.lastReset = 0L;
        this.timeTillNextReset = System.currentTimeMillis() + this.defaultMillisTillReset;
        this.active = false;
        this.glowstoneArea = null;
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        this.defaultMillisTillReset = TimeUnit.HOURS.toMillis(1L);
        this.lastReset = 0L;
        this.timeTillNextReset = System.currentTimeMillis() + this.defaultMillisTillReset;
        map.put("glowstoneArea", this.glowstoneArea);
        return map;
    }
    
    public GlowstoneFaction(Map<String, Object> map) {
        super(map);
        this.defaultMillisTillReset = TimeUnit.HOURS.toMillis(1L);
        this.lastReset = 0L;
        this.timeTillNextReset = System.currentTimeMillis() + this.defaultMillisTillReset;
        this.setDeathban(true);
        this.glowstoneArea = (Cuboid) map.get("glowstoneArea");
    }
    
    public void start() {
        this.lastReset = System.currentTimeMillis();
        this.timeTillNextReset = System.currentTimeMillis() + this.defaultMillisTillReset;
        this.active = true;
        System.out.println("Glowstone Faction is now ACTIVE");
        if (this.glowstoneArea == null) return;
        for (Block block : this.glowstoneArea) {
            block.setType(Material.GLOWSTONE);
        }
        RevampHCF.getInstance().getHandlerManager().getGlowstoneHandler().cancel();
        RevampHCF.getInstance().getHandlerManager().getGlowstoneHandler().start(TimeUtils.convert(RevampHCF.getInstance().getConfig().getInt("GLOWSTONE_MOUNTAIN_RESET_TIME"), 'm'));

    }
}

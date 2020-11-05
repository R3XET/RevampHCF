package eu.revamp.hcf.handlers.games;

import com.sk89q.worldedit.CuboidClipboard;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.hcf.utils.chat.JavaUtils;
import eu.revamp.spigot.utils.time.TimeUtils;
import eu.revamp.spigot.utils.world.WorldUtils;
import org.bukkit.World;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Ghast;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import com.sk89q.worldedit.MaxChangedBlocksException;
import java.io.IOException;
import com.sk89q.worldedit.world.DataException;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.type.GlowstoneFaction;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import java.io.File;
import org.bukkit.Location;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;
@SuppressWarnings("deprecation")
public class GlowstoneMountainHandler extends Handler implements Listener
{
    public GlowstoneMountainHandler(RevampHCF plugin) {
        super(plugin);
        this.reset();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @SuppressWarnings("deprecation")
    public void loadGlowstone(Location loc) throws DataException, IOException, MaxChangedBlocksException {
        File file = new File(this.getInstance().getDataFolder(), "glowstone-mountain.schematic");
        if (!file.exists()) {
            System.out.println("§cPLEASE UPLOAD A GLOWSTONE-MOUNTAIN.SCHEMATIC SCHEMATIC!");
            return;
        }
        EditSession session = new EditSession(new BukkitWorld(loc.getWorld()), 999999999);
        @Deprecated CuboidClipboard clipboard = CuboidClipboard.loadSchematic(file);
        clipboard.paste(session, BukkitUtil.toVector(loc), false);
        Faction faction = RevampHCF.getInstance().getFactionManager().getFaction("Glowstone");
        Location location = ((GlowstoneFaction)faction).getGlowstoneArea().getCenter();
        if (location == null) {
            System.out.println("§cGLOWSTONE LOCATION IS NULL!");
            return;
        }
        RevampHCF.getInstance().getHandlerManager().getGlowstoneHandler().cancel();
        RevampHCF.getInstance().getHandlerManager().getGlowstoneHandler().start(TimeUtils.convert(RevampHCF.getInstance().getConfig().getInt("GLOWSTONE_MOUNTAIN_RESET_TIME"), 'm'));
    }
    
    public void reset() {
        new BukkitRunnable() {
            public void run() {
                Location loc = WorldUtils.destringifyLocation(RevampHCF.getInstance().getLocation().getString("glowstone-location"));
                if (loc == null) {
                    System.out.println("§cGLOWSTONE MOUNTAIN IS NOT SET!");
                }
                else {
                    try {
                        GlowstoneMountainHandler.this.loadGlowstone(loc);
                    }
                    catch (MaxChangedBlocksException | DataException | IOException ex2) {
                        ex2.printStackTrace();
                    }
                }
            }
        }.runTaskTimer(this.getInstance(), 36000L, 36000L);
    }

    @EventHandler
    public void onGhastSpawn(CreatureSpawnEvent event){
        if (event.getEntity() instanceof Ghast){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onChickenSpawn(CreatureSpawnEvent event) {
        if (event.getEntity().getLocation().getWorld().getEnvironment() == World.Environment.NETHER) {
            if (event.getEntity() instanceof Chicken) {
                event.setCancelled(true);
            }
        }
    }
}

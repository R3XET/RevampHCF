package eu.revamp.hcf.handlers.fix.glitch;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.player.PlayerUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import eu.revamp.hcf.utils.inventory.BukkitUtils;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.World;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;

import com.google.common.collect.Sets;
import org.bukkit.Material;
import com.google.common.collect.ImmutableSet;
import java.util.UUID;
import java.util.Map;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;
@Getter @Setter
public class AntiBlockHitHandler extends Handler implements Listener
{
    public Map<UUID, Long> lastInteractTimes;
    public static ImmutableSet<Material> FENCE_NO_GLITCH = Sets.immutableEnumSet(Material.FENCE, Material.ACACIA_FENCE_GATE, Material.BIRCH_FENCE_GATE, Material.DARK_OAK_FENCE_GATE, Material.JUNGLE_FENCE_GATE, Material.SPRUCE_FENCE_GATE, Material.IRON_DOOR, Material.WOODEN_DOOR, Material.WOOD_DOOR, Material.ACACIA_DOOR, Material.BIRCH_DOOR, Material.JUNGLE_DOOR, Material.SPRUCE_DOOR, Material.BIRCH_DOOR_ITEM, Material.ACACIA_DOOR_ITEM, Material.DARK_OAK_DOOR_ITEM, Material.IRON_DOOR_BLOCK, Material.JUNGLE_DOOR_ITEM, Material.SPRUCE_DOOR_ITEM, Material.IRON_TRAPDOOR, Material.TRAP_DOOR, Material.IRON_DOOR_BLOCK);
    
    public AntiBlockHitHandler(RevampHCF plugin) {
        super(plugin);
        setLastInteractTimes(new HashMap<>());
    }
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void denyRoofTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("revamphcf.netherroof")) return;
        Location location = event.getTo();
        if (location.getWorld().getEnvironment() != World.Environment.NETHER) return;
        if (location.getY() < 126.0) return;
        event.setCancelled(true);
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void denyDestroy(BlockBreakEvent event) {
        if (this.denyBlockInteract(event.getPlayer(), event.getBlock())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void denyPlace(BlockPlaceEvent event) {
        if (this.denyBlockInteract(event.getPlayer(), event.getBlock())) {
            event.setCancelled(true);
        }
    }
    
    private boolean denyBlockInteract(Player player, Block block) {
        return block.getY() >= 126 && block.getWorld().getEnvironment() == World.Environment.NETHER;
    }
    
    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasBlock() && event.getAction() != Action.PHYSICAL && event.getClickedBlock().getType().isSolid() && event.isCancelled()) {
            this.cancelAttackingMillis(event.getPlayer().getUniqueId(), 850L);
        }
        if (event.hasBlock() && event.getAction() == Action.RIGHT_CLICK_BLOCK && AntiBlockHitHandler.FENCE_NO_GLITCH.contains(event.getClickedBlock().getType()) && event.isCancelled()) {
            this.cancelAttackingMillis(event.getPlayer().getUniqueId(), 850L);
        }
    }
    
    @EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled() && event.getBlock().getType().isSolid()) {
            this.cancelAttackingMillis(event.getPlayer().getUniqueId(), 850L);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageEvent event) {
        Player attacker = PlayerUtils.getFinalAttacker(event, true);
        if (attacker != null) {
            Long lastInteractTime = getLastInteractTimes().get(attacker.getUniqueId());
            if (lastInteractTime != null && lastInteractTime - System.currentTimeMillis() > 0L) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerLogout(PlayerQuitEvent event) {
        getLastInteractTimes().remove(event.getPlayer().getUniqueId());
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event) {
        getLastInteractTimes().remove(event.getPlayer().getUniqueId());
    }
    
    public void cancelAttackingMillis(UUID uuid, long delay) {
        getLastInteractTimes().put(uuid, System.currentTimeMillis() + delay);
    }
}

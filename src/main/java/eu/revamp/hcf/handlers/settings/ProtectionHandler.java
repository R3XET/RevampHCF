package eu.revamp.hcf.handlers.settings;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.events.FactionPlayerClaimEnterEvent;
import eu.revamp.hcf.factions.events.capzone.CaptureZoneEnterEvent;
import eu.revamp.hcf.factions.events.capzone.CaptureZoneLeaveEvent;
import eu.revamp.hcf.factions.type.*;
import eu.revamp.hcf.factions.utils.games.CapturableFaction;
import eu.revamp.hcf.factions.utils.struction.Raidable;
import eu.revamp.hcf.factions.utils.struction.Role;
import eu.revamp.hcf.factions.utils.zone.CaptureZone;
import eu.revamp.hcf.games.cuboid.Cuboid;
import eu.revamp.hcf.managers.CooldownManager;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.hcf.utils.inventory.BukkitUtils;
import eu.revamp.spigot.utils.player.PlayerUtils;
import eu.revamp.spigot.utils.potion.PotionUtils;
import eu.revamp.spigot.utils.world.WorldUtils;
import eu.revamp.system.api.player.PlayerData;
import eu.revamp.system.plugin.RevampSystem;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Cauldron;
import org.bukkit.material.Gate;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class ProtectionHandler extends Handler implements Listener
{
    public static ArrayList<Player> players = new ArrayList<>();
    private static ImmutableMultimap<Object, Object> ITEM_BLOCK_INTERACTABLES = ImmutableMultimap.builder().put(Material.DIAMOND_HOE, Material.GRASS).put(Material.GOLD_HOE, Material.GRASS).put(Material.IRON_HOE, Material.GRASS).put(Material.STONE_HOE, Material.GRASS).put(Material.WOOD_HOE, Material.GRASS).build();
    private static ImmutableSet<Material> BLOCK_INTERACTABLES = Sets.immutableEnumSet(Material.BED, Material.ACACIA_FENCE_GATE, Material.BIRCH_FENCE_GATE, Material.DARK_OAK_FENCE_GATE, Material.FENCE_GATE, Material.JUNGLE_FENCE_GATE, Material.SPRUCE_FENCE_GATE, Material.IRON_TRAPDOOR, Material.BED_BLOCK, Material.BEACON, Material.IRON_DOOR, Material.ACACIA_DOOR, Material.BIRCH_DOOR, Material.DARK_OAK_DOOR, Material.JUNGLE_DOOR, Material.SPRUCE_DOOR, Material.TRAP_DOOR, Material.WOOD_DOOR, Material.WOODEN_DOOR, Material.IRON_DOOR_BLOCK, Material.CHEST, Material.TRAPPED_CHEST, Material.FURNACE, Material.BURNING_FURNACE, Material.BREWING_STAND, Material.HOPPER, Material.DROPPER, Material.DISPENSER, Material.STONE_BUTTON, Material.WOOD_BUTTON, Material.ENCHANTMENT_TABLE, Material.WORKBENCH, Material.ANVIL, Material.LEVER, Material.FIRE);
    
    public ProtectionHandler(RevampHCF plugin) {
        super(plugin);
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    private void handleMove(PlayerMoveEvent event, FactionPlayerClaimEnterEvent.EnterCause enterCause) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ()) return;
        Player player = event.getPlayer();
        boolean cancelled = false;
        Faction fromFaction = RevampHCF.getInstance().getFactionManager().getFactionAt(from);
        Faction toFaction = RevampHCF.getInstance().getFactionManager().getFactionAt(to);
        if (fromFaction != toFaction) {
            FactionPlayerClaimEnterEvent calledEvent = new FactionPlayerClaimEnterEvent(player, from, to, fromFaction, toFaction, enterCause);
            Bukkit.getPluginManager().callEvent(calledEvent);
            cancelled = calledEvent.isCancelled();
        }
        else if (toFaction instanceof CapturableFaction) {
            CapturableFaction capturableFaction = (CapturableFaction)toFaction;
            for (CaptureZone captureZone : capturableFaction.getCaptureZones()) {
                Cuboid cuboid = captureZone.getCuboid();
                if (cuboid != null) {
                    boolean containsFrom = cuboid.contains(from);
                    boolean containsTo = cuboid.contains(to);
                    if (containsFrom && !containsTo) {
                        CaptureZoneLeaveEvent calledEvent2 = new CaptureZoneLeaveEvent(player, capturableFaction, captureZone);
                        Bukkit.getPluginManager().callEvent(calledEvent2);
                        cancelled = calledEvent2.isCancelled();
                        break;
                    }
                    if (!containsFrom && containsTo) {
                        CaptureZoneEnterEvent calledEvent3 = new CaptureZoneEnterEvent(player, capturableFaction, captureZone);
                        Bukkit.getPluginManager().callEvent(calledEvent3);
                        cancelled = calledEvent3.isCancelled();
                        break;
                    }
                }
            }
        }
        if (cancelled) {
            if (enterCause == FactionPlayerClaimEnterEvent.EnterCause.TELEPORT) {
                event.setCancelled(true);
            }
            else {
                from.setX(from.getBlockX() + 0.5);
                from.setZ(from.getBlockZ() + 0.5);
                event.setTo(from);
            }
        }
    }
    
    @EventHandler
    public void HidePlayersAtSpawn(PlayerMoveEvent event) {
        Location location = event.getPlayer().getLocation();
        Player player = event.getPlayer();
        if (event.isCancelled()) return;
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockZ() == event.getTo().getBlockZ() && event.getFrom().getBlockY() == event.getTo().getBlockY()) return;
        if (RevampHCF.getInstance().getConfig().getBoolean("HIDE_PLAYERS_AT_SPAWN")) {
            if ((!player.hasPermission("hcf.staff") && RevampHCF.getInstance().getHandlerManager().getSotwHandler().getSotwRunnable() != null && Math.abs(location.getBlockX()) <= RevampHCF.getInstance().getConfig().getInt("HIDE-PLAYERS-RANGE") && Math.abs(location.getBlockZ()) <= RevampHCF.getInstance().getConfig().getInt("HIDE-PLAYERS-RANGE")) || (!player.hasPermission("hcf.staff") && !RevampHCF.getInstance().getConfiguration().isKitMap() && RevampHCF.getInstance().getHandlerManager().getTimerManager().getPvpTimerHandler().hasCooldown(player) && Math.abs(location.getBlockX()) <= RevampHCF.getInstance().getConfig().getInt("HIDE-PLAYERS-RANGE") && Math.abs(location.getBlockZ()) <= RevampHCF.getInstance().getConfig().getInt("HIDE-PLAYERS-RANGE"))) {
                ProtectionHandler.players.add(event.getPlayer());
                for (Player players : Bukkit.getServer().getOnlinePlayers()) {
                    players.hidePlayer(event.getPlayer());
                }
            } else if (!player.hasPermission("hcf.staff") && ProtectionHandler.players.contains(event.getPlayer())) {
                ProtectionHandler.players.remove(event.getPlayer());
                for (Player players : Bukkit.getServer().getOnlinePlayers()) {
                    players.showPlayer(event.getPlayer());
                }
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        this.handleMove(event, FactionPlayerClaimEnterEvent.EnterCause.MOVEMENT);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerTeleportEvent event) {
        this.handleMove(event, FactionPlayerClaimEnterEvent.EnterCause.TELEPORT);
    }
    
    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        switch (event.getCause()) {
            case FLINT_AND_STEEL:
            case ENDER_CRYSTAL: {}
            default: {
                Faction factionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(event.getBlock().getLocation());
                if (factionAt instanceof ClaimableFaction && !(factionAt instanceof PlayerFaction)) {
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (RevampHCF.getInstance().getConfiguration().isKitMap()) {
            Faction factionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation());
            if (!player.getGameMode().equals(GameMode.CREATIVE) && factionAt instanceof SpawnFaction) {
                e.getItemDrop().remove();
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL) @SuppressWarnings("deprecation")
    public void onStickyPistonExtend(BlockPistonExtendEvent event) {
        Block block = event.getBlock();
        Block targetBlock = block.getRelative(event.getDirection(), event.getLength() + 1);
        if (targetBlock.isEmpty() || targetBlock.isLiquid()) {
            Faction targetFaction = RevampHCF.getInstance().getFactionManager().getFactionAt(targetBlock.getLocation());
            if (targetFaction instanceof Raidable && !((Raidable)targetFaction).isRaidable() && targetFaction != RevampHCF.getInstance().getFactionManager().getFactionAt(block)) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void checkBlock(BlockPlaceEvent e) {
        Faction factionatt = RevampHCF.getInstance().getFactionManager().getFactionAt(e.getPlayer().getLocation());
        if (factionatt instanceof GlowstoneFaction && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE) && !e.getPlayer().isOp()) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(CC.translate("&eYou may not place blocks in the territory of &6Glowstone Mountain&e."));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL) @SuppressWarnings("deprecation")
    public void onStickyPistonRetract(BlockPistonRetractEvent event) {
        if (!event.isSticky()) return;
        Location retractLocation = event.getRetractLocation();
        Block retractBlock = retractLocation.getBlock();
        if (!retractBlock.isEmpty() && !retractBlock.isLiquid()) {
            Block block = event.getBlock();
            Faction targetFaction = RevampHCF.getInstance().getFactionManager().getFactionAt(retractLocation);
            if (targetFaction instanceof Raidable && !((Raidable)targetFaction).isRaidable() && targetFaction != RevampHCF.getInstance().getFactionManager().getFactionAt(block)) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        Block toBlock = event.getToBlock();
        Block fromBlock = event.getBlock();
        if (!RevampHCF.getInstance().getConfiguration().isObsidianGeneratorsEnabled()) {
            Material fromType = fromBlock.getType();
            Material toType = toBlock.getType();
            if ((toType == Material.REDSTONE_WIRE || toType == Material.TRIPWIRE) && (fromType == Material.AIR || fromType == Material.STATIONARY_LAVA || fromType == Material.LAVA)) {
                toBlock.setType(Material.AIR);
            }
        }
        Material fromType = fromBlock.getType();
        if ((fromType == Material.WATER || fromType == Material.STATIONARY_WATER || fromType == Material.LAVA || fromType == Material.STATIONARY_LAVA) && !canBuildAt(fromBlock.getLocation(), toBlock.getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Material gate1 = Material.ACACIA_FENCE_GATE;
        Material gate2 = Material.BIRCH_FENCE_GATE;
        Material gate3 = Material.DARK_OAK_FENCE_GATE;
        Material gate4 = Material.FENCE_GATE;
        Material gate5 = Material.JUNGLE_FENCE_GATE;
        Material gate6 = Material.SPRUCE_FENCE_GATE;
        Player player = e.getPlayer();
        if ((player.getItemInHand().getAmount() > 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL && (e.getAction() == Action.RIGHT_CLICK_BLOCK || (player.getItemInHand().getAmount() > 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getAction() == Action.RIGHT_CLICK_AIR)) && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() != gate1) || (player.getItemInHand().getAmount() > 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL && (e.getAction() == Action.RIGHT_CLICK_BLOCK || (player.getItemInHand().getAmount() > 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getAction() == Action.RIGHT_CLICK_AIR)) && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() != gate2) || (player.getItemInHand().getAmount() > 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL && (e.getAction() == Action.RIGHT_CLICK_BLOCK || (player.getItemInHand().getAmount() > 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getAction() == Action.RIGHT_CLICK_AIR)) && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() != gate3) || (player.getItemInHand().getAmount() > 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL && (e.getAction() == Action.RIGHT_CLICK_BLOCK || (player.getItemInHand().getAmount() > 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getAction() == Action.RIGHT_CLICK_AIR)) && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() != gate4) || (player.getItemInHand().getAmount() > 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL && (e.getAction() == Action.RIGHT_CLICK_BLOCK || (player.getItemInHand().getAmount() > 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getAction() == Action.RIGHT_CLICK_AIR)) && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() != gate5) || (player.getItemInHand().getAmount() > 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL && (e.getAction() == Action.RIGHT_CLICK_BLOCK || (player.getItemInHand().getAmount() > 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getAction() == Action.RIGHT_CLICK_AIR)) && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() != gate6)) {
            player.getInventory().getItemInHand().setAmount(player.getInventory().getItemInHand().getAmount() - 1);
            player.launchProjectile(EnderPearl.class);
            player.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 0.5f, 0.4f);
            e.setCancelled(true);
        }
        if ((player.getItemInHand().getAmount() == 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL && (e.getAction() == Action.RIGHT_CLICK_BLOCK || (player.getItemInHand().getAmount() == 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getAction() == Action.RIGHT_CLICK_AIR)) && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() != gate1) || (player.getItemInHand().getAmount() == 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL && (e.getAction() == Action.RIGHT_CLICK_BLOCK || (player.getItemInHand().getAmount() == 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getAction() == Action.RIGHT_CLICK_AIR)) && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() != gate2) || (player.getItemInHand().getAmount() == 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL && (e.getAction() == Action.RIGHT_CLICK_BLOCK || (player.getItemInHand().getAmount() == 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getAction() == Action.RIGHT_CLICK_AIR)) && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() != gate3) || (player.getItemInHand().getAmount() == 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL && (e.getAction() == Action.RIGHT_CLICK_BLOCK || (player.getItemInHand().getAmount() == 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getAction() == Action.RIGHT_CLICK_AIR)) && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() != gate4) || (player.getItemInHand().getAmount() == 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL && (e.getAction() == Action.RIGHT_CLICK_BLOCK || (player.getItemInHand().getAmount() == 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getAction() == Action.RIGHT_CLICK_AIR)) && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() != gate5) || (player.getItemInHand().getAmount() == 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL && (e.getAction() == Action.RIGHT_CLICK_BLOCK || (player.getItemInHand().getAmount() == 1 && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && e.getAction() == Action.RIGHT_CLICK_AIR)) && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() != gate6)) {
            player.setItemInHand(new ItemStack(Material.AIR, 1));
            player.launchProjectile(EnderPearl.class);
            player.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 0.5f, 0.4f);
            e.setCancelled(true);
        }
        /*
        if ((e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == gate1 && !((Gate)e.getClickedBlock().getState().getData()).isOpen()) || (e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == gate2 && !((Gate)e.getClickedBlock().getState().getData()).isOpen()) || (e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == gate3 && !((Gate)e.getClickedBlock().getState().getData()).isOpen()) || (e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == gate4 && !((Gate)e.getClickedBlock().getState().getData()).isOpen()) || (e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == gate5 && !((Gate)e.getClickedBlock().getState().getData()).isOpen()) || (e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == gate6 && !((Gate)e.getClickedBlock().getState().getData()).isOpen())) {
            player.sendMessage(CC.translate("&c&lInvalid Pearl!"));
            RevampHCF.getInstance().getHandlerManager().getEnderpearlHandler().quit(player);
            e.setCancelled(true);
        }
         */
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            Faction toFactionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(event.getTo());
            if (toFactionAt.isSafezone() && !RevampHCF.getInstance().getFactionManager().getFactionAt(event.getFrom()).isSafezone()) {
                Player player = event.getPlayer();
                player.sendMessage(CC.translate("&c&lInvalid Pearl!"));
                RevampHCF.getInstance().getHandlerManager().getEnderpearlHandler().quit(player);
                player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
                event.setCancelled(true);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerPortal(PlayerPortalEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            Location from = event.getFrom();
            Location to = event.getTo();
            Player player = event.getPlayer();
            Faction fromFac = RevampHCF.getInstance().getFactionManager().getFactionAt(from);
            PlayerData targetProfile = RevampSystem.getINSTANCE().getPlayerManagement().getPlayerData(player.getUniqueId());
            if (fromFac.isSafezone()) {
                if (targetProfile.isFrozen() || targetProfile.isGuiFrozen()) return;
                Location netherSpawn = WorldUtils.destringifyLocation(RevampHCF.getInstance().getLocation().getString("World-Spawn.nether-spawn"));
                if (netherSpawn == null) {
                    System.out.print("NETHER SPAWN IS NULL!");
                    player.sendMessage(CC.translate("&c&lNether Spawn is not set please contact a Staff Member!"));
                    return;
                }
                //event.useTravelAgent(false);
                event.setCancelled(true);
                player.teleport(netherSpawn);
                //System.out.println(WorldUtils.destringifyLocation(RevampHCF.getInstance().getLocation().getString("World-Spawn.nether-spawn")));
            }
            else if (event.useTravelAgent() && to.getWorld().getEnvironment() == World.Environment.NORMAL) {
                TravelAgent travelAgent = event.getPortalTravelAgent();
                if (!travelAgent.getCanCreatePortal()) return;
                Location foundPortal = travelAgent.findPortal(to);
                if (foundPortal != null) return;
                Faction factionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(to);
                if (factionAt instanceof ClaimableFaction) {
                    Faction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
                    if (playerFaction != factionAt) {
                        player.sendMessage(CC.translate("&ePortal would have created portal in territory of " + factionAt.getDisplayName(player) + "&e."));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }


    /*
    @EventHandler
    public void onPortalCreate(EntityCreatePortalEvent event){
        if (event.getPortalType() == PortalType.NETHER && event.getEntity().getType() == EntityType.PLAYER){
            Player player = (Player) event.getEntity();
            if (player.getWorld().getEnvironment() == World.Environment.NORMAL){

                event.setCancelled(true);

            }
        }
    }*/
    
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();
        if (reason == CreatureSpawnEvent.SpawnReason.SLIME_SPLIT) return;
        if (event.getEntity() instanceof Slime) return;
        Location location = event.getLocation();
        Faction factionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(location);
        if ((factionAt.isSafezone() && reason == CreatureSpawnEvent.SpawnReason.SPAWNER) || (!factionAt.isSafezone() && reason == CreatureSpawnEvent.SpawnReason.SPAWNER && event.getEntity() instanceof Monster && location.getWorld().getEnvironment().equals(World.Environment.NETHER))) return;
        if (factionAt instanceof ClaimableFaction && (!(factionAt instanceof Raidable) || !((Raidable)factionAt).isRaidable()) && event.getEntity() instanceof Monster) {
            switch (reason) {
                case SPAWNER:
                case EGG:
                case BUILD_SNOWMAN:
                case BUILD_IRONGOLEM:
                case BUILD_WITHER:
                case CUSTOM: {}
                default: {
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player)entity;
            Faction playerFactionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation());
            EntityDamageEvent.DamageCause cause = event.getCause();
            if (playerFactionAt.isSafezone() && cause != EntityDamageEvent.DamageCause.SUICIDE && cause != EntityDamageEvent.DamageCause.VOID) {
                event.setCancelled(true);
            }
            Player attacker = PlayerUtils.getFinalAttacker(event, true);
            if (attacker != null) {
                Faction attackerFactionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(attacker.getLocation());
                if (attackerFactionAt.isSafezone()) {
                    event.setCancelled(true);
                    return;
                }
                if (playerFactionAt.isSafezone()) return;
                PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
                PlayerFaction attackerFaction;
                if (playerFaction != null && (attackerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(attacker)) != null) {
                    Role role = playerFaction.getMember(player).getRole();
                    String hiddenAstrixedName = role.getAstrix() + (player.hasPotionEffect(PotionEffectType.INVISIBILITY) ? "???" : player.getName());
                    if (attackerFaction == playerFaction) {
                        attacker.sendMessage(RevampHCF.getInstance().getConfiguration().getTeammateColor() + hiddenAstrixedName + " &eis in your faction.");
                        event.setCancelled(true);
                    }
                    else if (attackerFaction.getAllied().contains(playerFaction.getUniqueID())) {
                        event.setCancelled(true);
                        attacker.sendMessage(RevampHCF.getInstance().getConfiguration().getAllyColor() + hiddenAstrixedName + " &eis an ally.");
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        Entity entered = event.getEntered();
        if (entered instanceof Player) {
            Vehicle vehicle = event.getVehicle();
            if (vehicle instanceof Horse) {
                Horse horse = (Horse)event.getVehicle();
                AnimalTamer owner = horse.getOwner();
                if (owner != null && !owner.equals(entered)) {
                    entered.sendMessage(CC.translate("&eYou can't enter a Horse that belongs to &c" + owner.getName() + "&e."));
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player && ((Player)entity).getFoodLevel() > event.getFoodLevel() && RevampHCF.getInstance().getFactionManager().getFactionAt(entity.getLocation()).isSafezone()) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onFoodLevelChangeKitMap(FoodLevelChangeEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player && ((Player)entity).getFoodLevel() > event.getFoodLevel() && RevampHCF.getInstance().getConfiguration().isKitMap()) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPotionSplash(PotionSplashEvent event) {
        ThrownPotion potion = event.getEntity();
        if (!PotionUtils.isDebuff(potion)) return;
        Faction factionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(potion.getLocation());
        if (factionAt.isSafezone()) {
            event.setCancelled(true);
            return;
        }
        ProjectileSource source = potion.getShooter();
        if (source instanceof Player) {
            Player player = (Player)source;
            for (LivingEntity affected : event.getAffectedEntities()) {
                if (affected instanceof Player && !player.equals(affected)) {
                    Player target = (Player)affected;
                    if (target.equals(source)) continue;
                    if (!RevampHCF.getInstance().getFactionManager().getFactionAt(target.getLocation()).isSafezone()) continue;
                    event.setIntensity(affected, 0.0);
                }
            }
        }
    }
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        switch (event.getReason()) {
            case CLOSEST_PLAYER:
            case RANDOM_TARGET: {
                Entity target = event.getTarget();
                if (!(event.getEntity() instanceof LivingEntity) || !(target instanceof Player)) break;
                Faction factionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(target.getLocation());
                Faction playerFaction;
                if (factionAt.isSafezone() || ((playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction((Player)target)) != null && factionAt == playerFaction)) {
                    event.setCancelled(true);
                    break;
                }
                break;
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.hasBlock()) return;
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        Action action = event.getAction();
        if ((block.getType() == Material.BED_BLOCK && player.getWorld().getEnvironment().equals(World.Environment.NETHER) || (block.getType() == Material.BED && player.getWorld().getEnvironment().equals(World.Environment.NETHER)))) {
            player.sendMessage(CC.translate("&cYou can't use beds in nether!"));
            event.setCancelled(true);
        }
        if (action == Action.PHYSICAL) {
            if (!attemptBuild(player, block.getLocation(), null)) {
                if (this.getInstance().getConfiguration().isKitMap() && (block.getLocation().getBlock().getType() == Material.STONE_PLATE || block.getLocation().getBlock().getType() == Material.WOOD_PLATE || block.getLocation().getBlock().getType() == Material.WORKBENCH || block.getLocation().getBlock().getType() == Material.IRON_PLATE || block.getLocation().getBlock().getType() == Material.GOLD_PLATE)) return;
                if (block.getType() == Material.PAPER) {
                    event.setCancelled(false);
                }
                event.setCancelled(true);
            }
        }
        else if (action == Action.RIGHT_CLICK_BLOCK) {
            boolean canBuild = !ProtectionHandler.BLOCK_INTERACTABLES.contains(block.getType());
            if (canBuild) {
                Material itemType = event.hasItem() ? event.getItem().getType() : null;
                if (itemType != null && ProtectionHandler.ITEM_BLOCK_INTERACTABLES.containsKey(itemType) && ProtectionHandler.ITEM_BLOCK_INTERACTABLES.get(itemType).contains(event.getClickedBlock().getType())) {
                    if (block.getType() != Material.WORKBENCH || !RevampHCF.getInstance().getFactionManager().getFactionAt(block).isSafezone()) {
                        canBuild = false;
                    }
                }
                else {
                    MaterialData materialData = block.getState().getData();
                    if (materialData instanceof Cauldron) {
                        Cauldron cauldron = (Cauldron)materialData;
                        if (!cauldron.isEmpty() && event.hasItem() && event.getItem().getType() == Material.GLASS_BOTTLE) {
                            canBuild = false;
                        }
                    }
                }
            }
            if (!player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.DISPENSER && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.STONE_BUTTON && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.WOOD_BUTTON && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.LEVER && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.FIRE && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.TRAP_DOOR && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.WOOD_DOOR && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.WOODEN_DOOR && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.BREWING_STAND && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.HOPPER && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.DROPPER && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.IRON_DOOR_BLOCK && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.FURNACE && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.BURNING_FURNACE && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.ANVIL && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.BEACON && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.ENCHANTMENT_TABLE && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.BED && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.BED_BLOCK && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.CHEST && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.ENDER_CHEST && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.TRAPPED_CHEST && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.WORKBENCH && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && !CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.FENCE_GATE && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL) {
                player.getInventory().getItemInHand().setAmount(player.getInventory().getItemInHand().getAmount() - 1);
                player.launchProjectile(EnderPearl.class);
                player.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 0.5f, 0.4f);
                event.setCancelled(true);
            }
            /*
            else if (!player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.DISPENSER && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.STONE_BUTTON && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.WOOD_BUTTON && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.LEVER && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.FIRE && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.TRAP_DOOR && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.WOOD_DOOR && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.WOODEN_DOOR && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.BREWING_STAND && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.HOPPER && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.DROPPER && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.IRON_DOOR_BLOCK && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.FURNACE && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.BURNING_FURNACE && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.ANVIL && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.BEACON && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.ENCHANTMENT_TABLE && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.BED && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.BED_BLOCK && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.CHEST && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.ENDER_CHEST && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.TRAPPED_CHEST && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.WORKBENCH && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL || !player.getGameMode().equals(GameMode.CREATIVE) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) && block.getType() == Material.FENCE_GATE && player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL) {
                event.setCancelled(true);
            }
            */
            else if (block.getType() == Material.DISPENSER && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.STONE_BUTTON && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.WOOD_BUTTON && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.LEVER && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.FIRE && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.TRAP_DOOR && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.WOOD_DOOR && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.WOODEN_DOOR && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.BREWING_STAND && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.HOPPER && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.DROPPER && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.IRON_DOOR_BLOCK && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.FURNACE && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.BURNING_FURNACE && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.ANVIL && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.BEACON && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.ENCHANTMENT_TABLE && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.BED && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.BED_BLOCK && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.CHEST && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.ENDER_CHEST && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.TRAPPED_CHEST && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.WORKBENCH && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking() || block.getType() == Material.FENCE_GATE && player.getInventory().getItemInHand().getType() == Material.POTION && player.isSneaking()) {
                event.setCancelled(false);
            }
            else if (player.getInventory().getItemInHand().getType() != Material.ENDER_PEARL && block.getType() != Material.WORKBENCH && !canBuild && !attemptBuild(event.getPlayer(), block.getLocation(), ChatColor.YELLOW + "You can't do this in the territory of %1$s" + ChatColor.YELLOW + '.', true)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        Faction factionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(event.getBlock().getLocation());
        if (factionAt instanceof WarzoneFaction || (factionAt instanceof Raidable && !((Raidable)factionAt).isRaidable())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        Faction factionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(event.getBlock().getLocation());
        if (factionAt instanceof ClaimableFaction && !(factionAt instanceof PlayerFaction)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onLeavesDelay(LeavesDecayEvent event) {
        Faction factionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(event.getBlock().getLocation());
        if (factionAt instanceof ClaimableFaction && !(factionAt instanceof PlayerFaction)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        Faction factionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(event.getBlock().getLocation());
        if (factionAt instanceof ClaimableFaction && !(factionAt instanceof PlayerFaction)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity && !attemptBuild(entity, event.getBlock().getLocation(), null)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation()) instanceof GlowstoneFaction || (RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation()) != null && RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation()).getName().equalsIgnoreCase("Glowstone"))) {
            if (event.getBlock().getType().equals(Material.GLOWSTONE)) {
                PlayerData targetProfile = RevampSystem.getINSTANCE().getPlayerManagement().getPlayerData(player.getUniqueId());
                if (targetProfile.isInStaffMode()) {
                    event.setCancelled(true);
                }
                event.setCancelled(false);
                return;
            }
            if (player.getGameMode().equals(GameMode.CREATIVE) && player.isOp()) return;
            event.setCancelled(true);
        }
        else if (!attemptBuild(player, event.getBlock().getLocation(), CC.translate("&eYou can't break blocks in the territory of %1$s&e."))) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Faction factionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation());
        if (!attemptBuild(player, event.getBlockPlaced().getLocation(), CC.translate("&eYou can't place blocks in the territory of %1$s&e."))) {
            event.setCancelled(true);
        }
        if (!(factionAt instanceof SpawnFaction) && !(factionAt instanceof CapturableFaction) && RevampHCF.getInstance().getConfiguration().isKitMap() && player.getItemInHand().getType() == Material.WEB) {
            event.setCancelled(false);
        }
    }
    
    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        if (!attemptBuild(event.getPlayer(), event.getBlockClicked().getLocation(), CC.translate("&eYou can't build in the territory of %1$s&e."))) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (!attemptBuild(event.getPlayer(), event.getBlockClicked().getLocation(), CC.translate("&eYou can't build in the territory of %1$s&e."))) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        Entity remover = event.getRemover();
        if (remover instanceof Player && !attemptBuild(remover, event.getEntity().getLocation(), CC.translate("&eYou can't build in the territory of %1$s&e."))) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        if (!attemptBuild(event.getPlayer(), event.getEntity().getLocation(), CC.translate("&eYou can't build in the territory of %1$s&e."))) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onHangingDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Hanging) {
            Player attacker = PlayerUtils.getFinalAttacker(event, false);
            if (!attemptBuild(attacker, entity.getLocation(), CC.translate("&eYou can't build in the territory of %1$s&e."))) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onHangingInteractByPlayer(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity instanceof Hanging && !attemptBuild(event.getPlayer(), entity.getLocation(), CC.translate("&eYou can't build in the territory of %1$s&e."))) {
            event.setCancelled(true);
        }
    }
    
    public static boolean attemptBuild(Entity entity, Location location, @Nullable String denyMessage) {
        return attemptBuild(entity, location, denyMessage, false);
    }
    @SuppressWarnings("deprecation")
    public static boolean attemptBuild(Entity entity, Location location, @Nullable String denyMessage, boolean isInteraction) {
        Player player = entity instanceof Player ? (Player) entity : null;
        if (player != null && player.getGameMode() == GameMode.CREATIVE && player.hasPermission("revamphcf.op")) {
            return true;
        }
        if (player != null && player.getWorld().getEnvironment() == World.Environment.THE_END) {
            player.sendMessage(CC.translate("&cYou can't build in the end!"));
            return false;
        }
        boolean result = false;
        Faction factionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(location);
        if (!(factionAt instanceof ClaimableFaction)) {
            result = true;
        }
        else if (factionAt instanceof Raidable && ((Raidable)factionAt).isRaidable()) {
            result = true;
        }
        if (player != null && factionAt instanceof PlayerFaction && RevampHCF.getInstance().getFactionManager().getPlayerFaction(player) == factionAt) {
            result = true;
        }
        if (factionAt instanceof GlowstoneFaction && entity.getWorld().getBlockAt(location).getType() == Material.GLOWSTONE && ((GlowstoneFaction)factionAt).getGlowstoneArea().contains(location)) {
            result = true;
        }
        if (result) {
            if (RevampHCF.getInstance().getConfiguration().isKitMap()) {
                if (!isInteraction && Math.abs(location.getBlockX()) <= RevampHCF.getInstance().getConfig().getInt("FACTIONS-SETTINGS.BUILDABLE-AT") && Math.abs(location.getBlockZ()) <= RevampHCF.getInstance().getConfig().getInt("FACTIONS-SETTINGS.BUILDABLE-AT")) {
                    if (denyMessage != null) {
                        player.sendMessage(CC.translate("&eYou cannot build near the spawn."));
                    }
                    return false;
                }
            }
            else if (!isInteraction && Math.abs(location.getBlockX()) <= RevampHCF.getInstance().getConfig().getInt("FACTIONS-SETTINGS.BUILDABLE-AT") && Math.abs(location.getBlockZ()) <= RevampHCF.getInstance().getConfig().getInt("FACTIONS-SETTINGS.BUILDABLE-AT")) {
                if (denyMessage != null) {
                    player.sendMessage(CC.translate("&eYou cannot build near the spawn."));
                }
                return false;
            }
        }
        else if (denyMessage != null && player != null) {
            player.sendMessage(String.format(denyMessage, factionAt.getDisplayName(player)));
        }
        return result;
    }
    
    public static boolean canBuildAt(Location from, Location to) {
        Faction toFactionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(to);
        return !(toFactionAt instanceof Raidable) || ((Raidable)toFactionAt).isRaidable() || toFactionAt == RevampHCF.getInstance().getFactionManager().getFactionAt(from);
    }
}

package eu.revamp.hcf.handlers.items;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.utils.FactionMember;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import eu.revamp.hcf.handlers.settings.ProtectionHandler;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.hcf.utils.inventory.Crowbar;
import eu.revamp.spigot.utils.item.ItemBuilder;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Optional;

public class CrowbarHandler extends Handler implements Listener
{
    public CrowbarHandler(RevampHCF plugin) {
        super(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler @SuppressWarnings("deprecation")
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasItem()) {
            Optional<Crowbar> crowbarOptional = Crowbar.fromStack(event.getItem());
            if (crowbarOptional.isPresent()) {
                event.setCancelled(true);
                Player player = event.getPlayer();
                World world = player.getWorld();
                if (world.getEnvironment() != World.Environment.NORMAL) {
                    player.sendMessage(ChatColor.RED + "Crowbars may only be used in the overworld.");
                    return;
                }
                Block block = event.getClickedBlock();
                Location blockLocation = block.getLocation();
                if (!ProtectionHandler.attemptBuild(player, blockLocation, ChatColor.YELLOW + "You cannot do this in the territory of %1$s" + ChatColor.YELLOW + '.')) return;
                Crowbar crowbar = crowbarOptional.get();
                BlockState blockState = block.getState();
                if (blockState instanceof CreatureSpawner) {
                    int remainingUses = crowbar.getSpawnerUses();
                    if (remainingUses <= 0) {
                        player.sendMessage(ChatColor.RED + "This crowbar has no more Spawner uses.");
                        return;
                    }
                    crowbar.setSpawnerUses(remainingUses - 1);
                    player.setItemInHand(crowbar.getItemIfPresent());
                    CreatureSpawner spawner = (CreatureSpawner)blockState;
                    block.setType(Material.AIR);
                    blockState.update();
                    world.dropItemNaturally(blockLocation, new ItemBuilder(Material.MOB_SPAWNER, 1).setName(ChatColor.GREEN + "Spawner").setLore(ChatColor.WHITE + WordUtils.capitalizeFully(spawner.getSpawnedType().name())).toItemStack());
                }
                else {
                    if (block.getType() != Material.ENDER_PORTAL_FRAME) return;
                    int remainingUses = crowbar.getEndFrameUses();
                    if (remainingUses <= 0) {
                        player.sendMessage(ChatColor.RED + "This crowbar has no more End Portal Frame uses.");
                        return;
                    }
                    boolean destroyed = false;
                    int blockX = blockLocation.getBlockX();
                    int blockY = blockLocation.getBlockY();
                    int blockZ = blockLocation.getBlockZ();
                    for (int searchRadius = 4, x = blockX - searchRadius; x <= blockX + searchRadius; ++x) {
                        for (int z = blockZ - searchRadius; z <= blockZ + searchRadius; ++z) {
                            Block next = world.getBlockAt(x, blockY, z);
                            if (next.getType() == Material.ENDER_PORTAL) {
                                next.setType(Material.AIR);
                                next.getState().update();
                                destroyed = true;
                            }
                        }
                    }
                    if (destroyed) {
                        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
                        player.sendMessage(String.valueOf(ChatColor.RED.toString()) + ChatColor.GOLD + "Ender Portal is no longer active");
                        if (playerFaction != null) {
                            boolean informFaction = false;
                            for (ClaimZone claim : playerFaction.getClaims()) {
                                if (claim.contains(blockLocation)) {
                                    informFaction = true;
                                    break;
                                }
                            }
                            if (informFaction) {
                                FactionMember factionMember = playerFaction.getMember(player);
                                String astrix = factionMember.getRole().getAstrix();
                                playerFaction.broadcast(String.valueOf(astrix) + RevampHCF.getInstance().getConfiguration().getTeammateColor() + " has used a Crowbar de-activating one of the factions' end portals.", player.getUniqueId());
                            }
                        }
                    }
                    crowbar.setEndFrameUses(remainingUses - 1);
                    player.setItemInHand(crowbar.getItemIfPresent());
                    block.setType(Material.AIR);
                    blockState.update();
                    world.dropItemNaturally(blockLocation, new ItemStack(Material.ENDER_PORTAL_FRAME, 1));
                }
                if (event.getItem().getType() == Material.AIR) {
                    player.playSound(blockLocation, Sound.ITEM_BREAK, 1.0f, 1.0f);
                }
                else {
                    player.playSound(blockLocation, Sound.LEVEL_UP, 1.0f, 1.0f);
                }
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() != Material.ENDER_PORTAL_FRAME) return;
        int blockX = block.getLocation().getBlockX();
        int blockY = block.getLocation().getBlockY();
        int blockZ = block.getLocation().getBlockZ();
        for (int searchRadius = 4, x = blockX - searchRadius; x <= blockX + searchRadius; ++x) {
            for (int z = blockZ - searchRadius; z <= blockZ + searchRadius; ++z) {
                Block next = block.getWorld().getBlockAt(x, blockY, z);
                if (next.getType() == Material.ENDER_PORTAL) {
                    next.setType(Material.AIR);
                    next.getState().update();
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        ItemStack stack = event.getItemInHand();
        Player player = event.getPlayer();
        if (block.getState() instanceof CreatureSpawner && stack.hasItemMeta()) {
            ItemMeta meta = stack.getItemMeta();
            if (meta.hasLore() && meta.hasDisplayName()) {
                CreatureSpawner spawner = (CreatureSpawner)block.getState();
                List<String> lore = meta.getLore();
                if (!lore.isEmpty()) {
                    String spawnerName = ChatColor.stripColor(lore.get(0).toUpperCase());
                    Optional<EntityType> entityTypeOptional = Utils.getIfPresent(EntityType.class, spawnerName);
                    if (entityTypeOptional.isPresent()) {
                        spawner.setSpawnedType(entityTypeOptional.get());
                        spawner.update(true, true);
                        player.sendMessage(ChatColor.YELLOW + "Placed a " + ChatColor.RESET + spawnerName + ChatColor.YELLOW + " spawner.");
                    }
                }
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPrepareCrowbarCraft(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();
        if (event.isRepair() && event.getRecipe().getResult().getType() == Crowbar.CROWBAR_TYPE) {
            int endFrameUses = 0;
            int spawnerUses = 0;
            boolean changed = false;
            ItemStack[] matrix = inventory.getMatrix();
            ItemStack[] array;
            for (int length = (array = matrix).length, i = 0; i < length; ++i) {
                ItemStack ingredient = array[i];
                Optional<Crowbar> crowbarOptional = Crowbar.fromStack(ingredient);
                if (crowbarOptional.isPresent()) {
                    Crowbar crowbar = crowbarOptional.get();
                    spawnerUses += crowbar.getSpawnerUses();
                    endFrameUses += crowbar.getEndFrameUses();
                    changed = true;
                }
            }
            if (changed) {
                inventory.setResult(new Crowbar(spawnerUses, endFrameUses).getItemIfPresent());
            }
        }
    }
}

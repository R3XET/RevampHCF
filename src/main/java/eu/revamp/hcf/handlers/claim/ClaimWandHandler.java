package eu.revamp.hcf.handlers.claim;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.hcf.visualise.VisualBlock;
import eu.revamp.hcf.visualise.VisualType;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.base.Predicate;

import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.utils.ClaimSelection;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import eu.revamp.hcf.utils.Handler;

public class ClaimWandHandler extends Handler implements Listener {
	
	public ClaimWandHandler(RevampHCF plugin) {
		super(plugin);
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();

        // They didn't use a claiming wand for this action, so ignore.
        if (action == Action.PHYSICAL || !event.hasItem() || !isClaimingWand(event.getItem())) return;
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        // Clearing the claim selection of player.
        if (action == Action.RIGHT_CLICK_AIR && player.isSneaking() || action == Action.RIGHT_CLICK_BLOCK && player.isSneaking()) {
            RevampHCF.getInstance().getHandlerManager().getClaimHandler().clearClaimSelection(player);
            player.setItemInHand(new ItemStack(Material.AIR, 1));
            player.sendMessage(CC.translate("&aYou have cleared your claim selection."));
            return;
        }
        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(uuid);
        // Purchasing the claim from the selections.
        if ((action == Action.LEFT_CLICK_AIR && player.isSneaking() || action == Action.LEFT_CLICK_BLOCK && player.isSneaking()) ) {
            ClaimSelection claimSelection = RevampHCF.getInstance().getHandlerManager().getClaimHandler().claimSelectionMap.get(uuid);
            if (claimSelection == null || !claimSelection.hasBothPositionsSet()) {
                player.sendMessage(CC.translate("&c&lYou have not set both positions of this claim selection."));
                return;
            }
            if (RevampHCF.getInstance().getHandlerManager().getClaimHandler().tryPurchasing(player, claimSelection.toClaim(playerFaction))) {
                RevampHCF.getInstance().getHandlerManager().getClaimHandler().clearClaimSelection(player);
                player.setItemInHand(new ItemStack(Material.AIR, 1));
            }
            return;
        }
        // Setting the positions for the claim selection;
        if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            Location blockLocation = block.getLocation();
            // Don't hoe the soil block.
            if (action == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
            }
            if (RevampHCF.getInstance().getHandlerManager().getClaimHandler().canClaimHere(player, blockLocation)) {
                ClaimSelection revert;
                ClaimSelection claimSelection = RevampHCF.getInstance().getHandlerManager().getClaimHandler().claimSelectionMap.putIfAbsent(uuid, revert = new ClaimSelection(blockLocation.getWorld()));
                if (claimSelection == null)
                    claimSelection = revert;
                Location oldPosition;
                Location opposite;
                int selectionId;
                switch (action) {
                case LEFT_CLICK_BLOCK:
                    oldPosition = claimSelection.getPos1();
                    opposite = claimSelection.getPos2();
                    selectionId = 1;
                    break;
                case RIGHT_CLICK_BLOCK:
                    oldPosition = claimSelection.getPos2();
                    opposite = claimSelection.getPos1();
                    selectionId = 2;
                    break;
                default:
                    return; // This should never happen.
                }
                // Prevent players clicking in the same spot twice.
                int blockX = blockLocation.getBlockX();
                int blockZ = blockLocation.getBlockZ();
                if (oldPosition != null && blockX == oldPosition.getBlockX() && blockZ == oldPosition.getBlockZ()) return;
                // Allow at least 1 tick before players can update one of the positions to prevent lag/visual glitches with delayed task below.
                if ((System.currentTimeMillis() - claimSelection.getLastUpdateMillis()) <= ClaimHandler.PILLAR_BUFFER_DELAY_MILLIS) return;
                if (opposite != null) {
                    int xDiff = Math.abs(opposite.getBlockX() - blockX) + 1; // Add one as it gets a weird offset
                    int zDiff = Math.abs(opposite.getBlockZ() - blockZ) + 1; // Add one as it gets a weird offset
                    if (xDiff < ClaimHandler.MIN_CLAIM_RADIUS || zDiff < ClaimHandler.MIN_CLAIM_RADIUS) {
                        player.sendMessage(CC.translate("&cClaim selections must be at least &l" + ClaimHandler.MIN_CLAIM_RADIUS + 'x' + ClaimHandler.MIN_CLAIM_RADIUS + " &cblocks."));
                        return;
                    }
                }

                if (oldPosition != null) {
                    RevampHCF.getInstance().getHandlerManager().getVisualiseHandler().clearVisualBlocks(player, VisualType.CREATE_CLAIM_SELECTION, new Predicate<VisualBlock>() {
                        @Override
                        public boolean apply(VisualBlock visualBlock) {
                            Location location = visualBlock.getLocation();
                            return location.getBlockX() == oldPosition.getBlockX() && location.getBlockZ() == oldPosition.getBlockZ();
                        }
                    });
                }

                if (selectionId == 1)
                    claimSelection.setPos1(blockLocation);
                if (selectionId == 2)
                    claimSelection.setPos2(blockLocation);

                player.sendMessage(CC.translate("&eSet the location of claim selection &f" + selectionId + " &eto: &f" + '(' +  blockX + ", " + blockZ + ')'));

                if (claimSelection.hasBothPositionsSet()) {
                    ClaimZone claim = claimSelection.toClaim(playerFaction);
                    int selectionPrice = claimSelection.getPrice(playerFaction, false);
                    player.sendMessage(CC.translate("&eClaim selection cost: &f$" + (selectionPrice > playerFaction.getBalance() ? ChatColor.RED : ChatColor.GREEN) + selectionPrice + "&e. Current size: &f(" + claim.getWidth() + ", " + claim.getLength() + "), " + claim.getArea() + " &eblocks."));
                }

                int blockY = block.getY();
                int maxHeight = player.getWorld().getMaxHeight();
                List<Location> locations = new ArrayList<>(maxHeight);
                for (int i = blockY; i < maxHeight; i++) {
                    Location other = blockLocation.clone();
                    other.setY(i);
                    locations.add(other);
                }

                // Generate the new claiming pillar a tick later as right clicking using this
                // event doesn't update the bottom block clicked occasionally.
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        RevampHCF.getInstance().getHandlerManager().getVisualiseHandler().generate(player, locations, VisualType.CREATE_CLAIM_SELECTION, true);
                    }
                }.runTask(RevampHCF.getInstance());
            }
        }
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        if (isClaimingWand(event.getPlayer().getItemInHand())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.NORMAL)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (isClaimingWand(player.getItemInHand())) {
                player.setItemInHand(new ItemStack(Material.AIR, 1));
                RevampHCF.getInstance().getHandlerManager().getClaimHandler().clearClaimSelection(player);
            }
        }
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.NORMAL)
    public void onPlayerKick(PlayerKickEvent event) {
        event.getPlayer().getInventory().remove(ClaimHandler.claimWand);
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.getPlayer().getInventory().remove(ClaimHandler.claimWand);
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.NORMAL)
    public void onPlayerDrop(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        if (isClaimingWand(item.getItemStack())) {
            item.remove();
            RevampHCF.getInstance().getHandlerManager().getClaimHandler().clearClaimSelection(event.getPlayer());
        }
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.NORMAL)
    public void onPlayerPickup(PlayerPickupItemEvent event) {
        Item item = event.getItem();
        if (isClaimingWand(item.getItemStack())) {
            item.remove();
            RevampHCF.getInstance().getHandlerManager().getClaimHandler().clearClaimSelection(event.getPlayer());
        }
    }

    // Prevents dropping Claiming Wands on death.
    @EventHandler(ignoreCancelled = false, priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getDrops().remove(ClaimHandler.claimWand)) {
            RevampHCF.getInstance().getHandlerManager().getClaimHandler().clearClaimSelection(event.getEntity());
        }
    }

    // Doesn't get called when opening own inventory.
    @EventHandler(ignoreCancelled = false, priority = EventPriority.NORMAL)
    public void onInventoryOpen(InventoryOpenEvent event) {
        HumanEntity humanEntity = event.getPlayer();
        if (humanEntity instanceof Player) {
            Player player = (Player) humanEntity;
            if (player.getInventory().contains(ClaimHandler.claimWand)) {
                player.getInventory().remove(ClaimHandler.claimWand);
                RevampHCF.getInstance().getHandlerManager().getClaimHandler().clearClaimSelection(player);
            }
        }
    }

    /**
     * Checks if an {@link ItemStack} is a Claiming Wand.
     *
     * @param stack
     *            the {@link ItemStack} to check
     * @return true if the {@link ItemStack} is a claiming wand
     */
    public boolean isClaimingWand(ItemStack stack) {
        return stack != null && stack.isSimilar(ClaimHandler.claimWand);
    }
}
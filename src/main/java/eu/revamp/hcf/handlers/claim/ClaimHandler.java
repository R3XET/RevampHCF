package eu.revamp.hcf.handlers.claim;

import java.util.Collection;

import eu.revamp.hcf.Language;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.hcf.visualise.VisualType;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.games.cuboid.CuboidDirection;
import com.google.common.base.Preconditions;
import eu.revamp.hcf.factions.FactionManager;
import eu.revamp.hcf.factions.type.RoadFaction;
import eu.revamp.hcf.factions.type.ClaimableFaction;
import eu.revamp.hcf.factions.type.WildernessFaction;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import eu.revamp.spigot.utils.item.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.World;
import eu.revamp.hcf.factions.utils.zone.SubclaimZone;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.utils.struction.Role;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import eu.revamp.hcf.games.cuboid.Cuboid;
import java.util.HashMap;

import org.bukkit.ChatColor;
import eu.revamp.hcf.factions.utils.ClaimSelection;
import java.util.UUID;
import java.util.Map;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class ClaimHandler extends Handler implements Listener
{
    public static int MIN_CLAIM_HEIGHT = 0;
    public static int MAX_CLAIM_HEIGHT = 256;
    public static long PILLAR_BUFFER_DELAY_MILLIS = 200L;
    public static ItemStack claimWand = new ItemBuilder(RevampHCF.getInstance().getConfig().getMaterial("CLAIM_WAND.MATERIAL")).setName(RevampHCF.getInstance().getConfig().getString("CLAIM_WAND.NAME")).setLore(RevampHCF.getInstance().getConfig().getStringList("CLAIM_WAND.LORE")).addUnsafeEnchantment(Enchantment.DURABILITY, 10).toItemStack();
    private static int NEXT_PRICE_MULTIPLIER_AREA = 250;
    private static int NEXT_PRICE_MULTIPLIER_CLAIM = 500;
    public static int MIN_SUBCLAIM_RADIUS = 2;
    public static int MIN_CLAIM_RADIUS = 5;
    public static int MAX_CHUNKS_PER_LIMIT = 16;
    public static int CLAIM_BUFFER_RADIUS = 4;
    @Getter @Setter public Map<UUID, ClaimSelection> claimSelectionMap;
    private static double CLAIM_SELL_MULTIPLIER = 0.8;
    private static double CLAIM_PRICE_PER_BLOCK = 0.5;
    
    public ClaimHandler(RevampHCF plugin) {
        super(plugin);
        setClaimSelectionMap(new HashMap<>());
    }
    
    public int calculatePrice(Cuboid claim, int currentClaims, boolean selling) {
        if (currentClaims == -1 || !claim.hasBothPositionsSet()) {
            return 0;
        }
        int multiplier = 1;
        int remaining = claim.getArea();
        double price = 0.0;
        while (remaining > 0) {
            if (--remaining % ClaimHandler.NEXT_PRICE_MULTIPLIER_AREA == 0) {
                ++multiplier;
            }
            price += ClaimHandler.CLAIM_PRICE_PER_BLOCK * multiplier;
        }
        if (currentClaims != 0) {
            currentClaims = Math.max(currentClaims + (selling ? -1 : 0), 0);
            price += currentClaims * ClaimHandler.NEXT_PRICE_MULTIPLIER_CLAIM;
        }
        if (selling) {
            price *= ClaimHandler.CLAIM_SELL_MULTIPLIER;
        }
        return (int)price;
    }
    
    public boolean clearClaimSelection(Player player) {
        ClaimSelection claimSelection = RevampHCF.getInstance().getHandlerManager().getClaimHandler().getClaimSelectionMap().remove(player.getUniqueId());
        if (claimSelection != null) {
            RevampHCF.getInstance().getHandlerManager().getVisualiseHandler().clearVisualBlocks(player, VisualType.CREATE_CLAIM_SELECTION, null);
            return true;
        }
        return false;
    }
    @SuppressWarnings("deprecation")
    public boolean canSubclaimHere(Player player, Location location) {
        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            player.sendMessage(CC.translate("&cYour cannot do this with this role."));
            return false;
        }
        if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
            player.sendMessage(CC.translate("&cYour cannot do this with this role."));
            return false;
        }
        if (RevampHCF.getInstance().getFactionManager().getFactionAt(location) != playerFaction) {
            player.sendMessage(ChatColor.RED + "This location is not part of your factions' territory.");
            return false;
        }
        return true;
    }
    @SuppressWarnings("deprecation")
    public boolean tryCreatingSubclaim(Player player, SubclaimZone subclaim) {
        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            player.sendMessage(Language.FACTIONS_FACTION_NOT_FOUND.toString());
            return false;
        }
        if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
            player.sendMessage(CC.translate("&cYour cannot do this with this role."));
            return true;
        }
        World world = subclaim.getWorld();
        int minimumX = subclaim.getMinimumX();
        int maximumX = subclaim.getMaximumX();
        int minimumZ = subclaim.getMinimumZ();
        int maximumZ = subclaim.getMaximumZ();
        ClaimZone foundClaim = null;
        for (int x = minimumX; x < maximumX; ++x) {
            for (int z = minimumZ; z < maximumZ; ++z) {
                ClaimZone claimAt = RevampHCF.getInstance().getFactionManager().getClaimAt(world, x, z);
                Faction factionAt;
                if (claimAt == null || (playerFaction == (factionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(world, x, z)) && !(factionAt instanceof PlayerFaction))) {
                    player.sendMessage(ChatColor.RED + "This subclaim selection contains a location outside of your faction.");
                    return false;
                }
                if (foundClaim == null) {
                    foundClaim = claimAt;
                }
                else if (claimAt != foundClaim) {
                    player.sendMessage(ChatColor.RED + "This subclaim selection is inside more than one of your faction claims.");
                    return false;
                }
            }
        }
        if (foundClaim == null) {
            player.sendMessage(ChatColor.RED + "This subclaim selection is not inside your faction territory.");
            return false;
        }
        subclaim.getAccessibleMembers().add(player.getUniqueId());
        player.sendMessage(ChatColor.GOLD + "You have created a subclaim named " + ChatColor.AQUA + subclaim.getName() + ChatColor.GOLD + '.');
        return true;
    }
    @SuppressWarnings("deprecation")
    public boolean canClaimHere(Player player, Location location) {
        World world = location.getWorld();
        if (world.getEnvironment() != World.Environment.NORMAL) {
            player.sendMessage(CC.translate("&cYou cannot claim in this world."));
            return false;
        }
        if (!(RevampHCF.getInstance().getFactionManager().getFactionAt(location) instanceof WildernessFaction)) {
            player.sendMessage(CC.translate("&cYou are currently in the Warzone and can't claim land here. The Warzone ends at " + RevampHCF.getInstance().getConfig().getString("FACTIONS-SETTINGS.WARZONE-RADIUS") + "."));
            return false;
        }
        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            player.sendMessage(Language.FACTIONS_NOFACTION.toString());
            return false;
        }
        if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
            player.sendMessage(CC.translate("&cYour cannot do this with this role."));
            return false;
        }
        if (playerFaction.getClaims().size() >= RevampHCF.getInstance().getConfiguration().getMaxClaimsPerFaction()) {
            player.sendMessage(ChatColor.RED + "Your faction has maximum claims - " + RevampHCF.getInstance().getConfiguration().getMaxClaimsPerFaction());
            return false;
        }
        int locX = location.getBlockX();
        int locZ = location.getBlockZ();
        FactionManager factionManager = RevampHCF.getInstance().getFactionManager();
        for (int x = locX - ClaimHandler.CLAIM_BUFFER_RADIUS; x < locX + ClaimHandler.CLAIM_BUFFER_RADIUS; ++x) {
            for (int z = locZ - ClaimHandler.CLAIM_BUFFER_RADIUS; z < locZ + ClaimHandler.CLAIM_BUFFER_RADIUS; ++z) {
                Faction factionAtNew = factionManager.getFactionAt(world, x, z);
                if (!RevampHCF.getInstance().getConfiguration().isAllowClaimingOnRoads() && factionAtNew instanceof ClaimableFaction && playerFaction != factionAtNew && !(factionAtNew instanceof RoadFaction)) {
                    player.sendMessage(ChatColor.RED + "This position contains enemy claims within a " + ClaimHandler.CLAIM_BUFFER_RADIUS + " block buffer radius.");
                    return false;
                }
            }
        }
        return true;
    }
    @SuppressWarnings("deprecation")
    public boolean tryPurchasing(Player player, ClaimZone claim) {
        Preconditions.checkNotNull((Object)claim, "Claim is null");
        World world = claim.getWorld();
        if (world.getEnvironment() != World.Environment.NORMAL) {
            player.sendMessage(CC.translate("&cYou cannot use this command in this world."));
            return false;
        }
        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            player.sendMessage(Language.FACTIONS_NOFACTION.toString());
            return false;
        }
        if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
            player.sendMessage(CC.translate("&cYour cannot do this with this role."));
            return false;
        }
        if (playerFaction.getClaims().size() >= RevampHCF.getInstance().getConfiguration().getMaxClaimsPerFaction()) {
            player.sendMessage(CC.translate("&cYour faction has maximum claims - &l" + RevampHCF.getInstance().getConfiguration().getMaxClaimsPerFaction() + "&c."));
            return false;
        }
        int factionBalance = playerFaction.getBalance();
        int claimPrice = this.calculatePrice(claim, playerFaction.getClaims().size(), false);
        if (claimPrice > factionBalance) {
            player.sendMessage(CC.translate("&cYour faction bank only has &l$" + factionBalance + "&c, the price of this claim is &l$" + claimPrice + "&c."));
            return false;
        }
        if (claim.getChunks().size() > ClaimHandler.MAX_CHUNKS_PER_LIMIT) {
            player.sendMessage(CC.translate("&cClaims cannot exceed &l" + ClaimHandler.MAX_CHUNKS_PER_LIMIT + " &cchunks."));
            return false;
        }
        if (claim.getWidth() < ClaimHandler.MIN_CLAIM_RADIUS || claim.getLength() < ClaimHandler.MIN_CLAIM_RADIUS) {
            player.sendMessage(CC.translate("&cClaims must be at least &l" + ClaimHandler.MIN_CLAIM_RADIUS + "&cx&l" + ClaimHandler.MIN_CLAIM_RADIUS + " &cblocks."));
            return false;
        }
        int minimumX = claim.getMinimumX();
        int maximumX = claim.getMaximumX();
        int minimumZ = claim.getMinimumZ();
        int maximumZ = claim.getMaximumZ();
        FactionManager factionManager = RevampHCF.getInstance().getFactionManager();
        for (int x = minimumX; x < maximumX; ++x) {
            for (int z = minimumZ; z < maximumZ; ++z) {
                Faction factionAt = factionManager.getFactionAt(world, x, z);
                if (factionAt != null && !(factionAt instanceof WildernessFaction)) {
                    player.sendMessage(CC.translate("&cThis claim contains a location not within the &7The Wilderness&c."));
                    return false;
                }
            }
        }
        for (int x = minimumX - ClaimHandler.CLAIM_BUFFER_RADIUS; x < maximumX + ClaimHandler.CLAIM_BUFFER_RADIUS; ++x) {
            for (int z = minimumZ - ClaimHandler.CLAIM_BUFFER_RADIUS; z < maximumZ + ClaimHandler.CLAIM_BUFFER_RADIUS; ++z) {
                Faction factionAtNew = factionManager.getFactionAt(world, x, z);
                if (!RevampHCF.getInstance().getConfiguration().isAllowClaimingOnRoads() && factionAtNew instanceof ClaimableFaction && playerFaction != factionAtNew && !(factionAtNew instanceof RoadFaction)) {
                    player.sendMessage(CC.translate("&cThis claim contains enemy claims within a &l" + ClaimHandler.CLAIM_BUFFER_RADIUS + "&c block buffer radius."));
                    return false;
                }
            }
        }
        Location minimum = claim.getMinimumPoint();
        Location maximum = claim.getMaximumPoint();
        Collection<ClaimZone> otherClaims = playerFaction.getClaims();
        boolean conjoined = otherClaims.isEmpty();
        if (!conjoined) {
            for (ClaimZone otherClaim : otherClaims) {
                Cuboid outset = otherClaim.clone().outset(CuboidDirection.HORIZONTAL, 1);
                if (outset.contains(minimum) || outset.contains(maximum)) {
                    conjoined = true;
                    break;
                }
            }
            if (!conjoined) {
                player.sendMessage(CC.translate("&cAll claims in your faction must be conjoined."));
                return false;
            }
        }
        claim.setY1(ClaimHandler.MIN_CLAIM_HEIGHT);
        claim.setY2(ClaimHandler.MAX_CLAIM_HEIGHT);
        if (!playerFaction.addClaim(claim, player)) {
            return false;
        }
        Location center = claim.getCenter();
        player.sendMessage(CC.translate("&eClaim has been purchased for &f$" + claimPrice));
        playerFaction.setBalance(factionBalance - claimPrice);
        playerFaction.broadcast(CC.translate("&f" + player.getName() + " &eclaimed land for your faction at &f" + '(' + center.getBlockX() + ", " + center.getBlockZ() + ')'), player.getUniqueId());
        return true;
    }
}

package eu.revamp.hcf.factions.utils;

import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.FactionManager;
import eu.revamp.hcf.utils.inventory.BukkitUtils;
import eu.revamp.hcf.factions.type.PlayerFaction;
import java.util.Objects;
import java.util.List;
import java.util.Set;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import java.util.Map;
import eu.revamp.hcf.visualise.VisualBlockData;
import org.bukkit.Location;
import java.util.ArrayList;
import eu.revamp.hcf.handlers.claim.ClaimHandler;
import org.bukkit.ChatColor;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import java.util.LinkedHashSet;
import eu.revamp.hcf.visualise.VisualType;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.entity.Player;

public class LandMap
{
    private static int FACTION_MAP_RADIUS_BLOCKS = 22;
    @SuppressWarnings("deprecation")
    public static boolean updateMap(Player player, RevampHCF plugin, VisualType visualType, boolean inform) {
        Location location = player.getLocation();
        World world = player.getWorld();
        int locationX = location.getBlockX();
        int locationZ = location.getBlockZ();
        int minimumX = locationX - 22;
        int minimumZ = locationZ - 22;
        int maximumX = locationX + 22;
        int maximumZ = locationZ + 22;
        Set<ClaimZone> board = new LinkedHashSet<>();
        for (int x = minimumX; x <= maximumX; ++x) {
            for (int z = minimumZ; z <= maximumZ; ++z) {
                ClaimZone claim = plugin.getFactionManager().getClaimAt(world, x, z);
                if (claim != null) {
                    board.add(claim);
                }
            }
        }
        if (board.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Nothing to visualise for " + visualType.name().toLowerCase() + " within " + 22 + " blocks of you.");
            return false;
        }
        for (ClaimZone claim2 : board) {
            int maxHeight = Math.min(world.getMaxHeight(), ClaimHandler.MAX_CLAIM_HEIGHT);
            Location[] corners = claim2.getCornerLocations();
            List<Location> shown = new ArrayList<>(maxHeight * corners.length);
            Location[] array;
            for (int length = (array = corners).length, i = 0; i < length; ++i) {
                Location corner = array[i];
                for (int y = 0; y < maxHeight; ++y) {
                    shown.add(world.getBlockAt(corner.getBlockX(), y, corner.getBlockZ()).getLocation());
                }
            }
            Map<Location, VisualBlockData> dataMap = plugin.getHandlerManager().getVisualiseHandler().generate(player, shown, visualType, true);
            if (dataMap.isEmpty()) {
                continue;
            }
            String materialName = RevampHCF.getInstance().getItemDB().getName(new ItemStack(dataMap.entrySet().iterator().next().getValue().getItemType(), 1));
            if (!inform) {
                continue;
            }
            player.sendMessage(ChatColor.YELLOW + claim2.getFaction().getDisplayName(player) + ChatColor.YELLOW + " owns land " + ChatColor.WHITE + claim2.getName() + ChatColor.GRAY + " (displayed with " + materialName + ')' + ChatColor.YELLOW + '.');
        }
        return true;
    }
    
    public static Location getNearestSafePosition(Player player, Location origin, int searchRadius) {
        FactionManager factionManager = RevampHCF.getInstance().getFactionManager();
        Faction playerFaction = factionManager.getPlayerFaction(player.getUniqueId());
        int minX = origin.getBlockX() - searchRadius;
        int maxX = origin.getBlockX() + searchRadius;
        int minZ = origin.getBlockZ() - searchRadius;
        int maxZ = origin.getBlockZ() + searchRadius;
        for (int x = minX; x < maxX; ++x) {
            for (int z = minZ; z < maxZ; ++z) {
                Location atPos = origin.clone().add(x, 0.0, z);
                Faction factionAtPos = factionManager.getFactionAt(atPos);
                if (Objects.equals(factionAtPos, playerFaction) || !(factionAtPos instanceof PlayerFaction)) {
                    return BukkitUtils.getHighestLocation(atPos, atPos);
                }
                Location atNeg = origin.clone().add(x, 0.0, z);
                Faction factionAtNeg = factionManager.getFactionAt(atNeg);
                if (Objects.equals(factionAtNeg, playerFaction) || !(factionAtNeg instanceof PlayerFaction)) {
                    return BukkitUtils.getHighestLocation(atNeg, atNeg);
                }
            }
        }
        return null;
    }
}

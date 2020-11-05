package eu.revamp.hcf.utils;

import com.google.common.base.Preconditions;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.date.Formats;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.PacketPlayOutSetSlot;
import net.minecraft.server.v1_8_R3.PlayerInventory;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Utils
{
    private static long MINUTE = TimeUnit.MINUTES.toMillis(1L);
    private static long HOUR = TimeUnit.HOURS.toMillis(1L);

    private static Map<UUID, String> uuidToName = new ConcurrentHashMap<>();
    private static Map<String, UUID> nameToUUID = new ConcurrentHashMap<>();

    public static UUID uuid(String name) {
        return Utils.nameToUUID.get(name.toLowerCase());
    }
    
    public static String name(UUID uuid) {
        return Utils.uuidToName.get(uuid);
    }

    public static Location getLocation(String warp){
        Location location;
        World world = Bukkit.getWorld(RevampHCF.getInstance().getUtilities().getString("warps." + warp + ".world"));
        double x = RevampHCF.getInstance().getUtilities().getDouble("warps." + warp + ".x");
        double y = RevampHCF.getInstance().getUtilities().getDouble("warps." + warp + ".y");
        double z = RevampHCF.getInstance().getUtilities().getDouble("warps." + warp + ".z");
        float yaw = RevampHCF.getInstance().getUtilities().getFloat("warps." + warp + ".yaw");
        float pitch = RevampHCF.getInstance().getUtilities().getFloat("warps." + warp + ".pitch");
        location = new Location(world, x, y, z , yaw, pitch);
        return location;
    }

    public static String TimerFormat(double data) {
        int minutes = (int)(data / 60.0);
        int seconds = (int)(data % 60.0);
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    public static String formatSecondsToHours(double d) {
        int i = (int)(d / 3600.0);
        int j = (int)(d % 3600.0 / 60.0);
        int k = (int)(d % 60.0);
        return String.format("%02d:%02d:%02d", i, j, k);
    }

    public static <T extends Enum<T>> Optional<T> getIfPresent(Class<T> enumClass, String value) {
        Preconditions.checkNotNull((Object)enumClass);
        Preconditions.checkNotNull((Object)value);
        try {
            return (Optional.of(Enum.valueOf(enumClass, value)));
        }
        catch (IllegalArgumentException iae) {
            return Optional.empty();
        }
    }
    
    public static <T> T firstNonNull(@Nullable T first, @Nullable T second) {
        return (first != null) ? first : Preconditions.checkNotNull(second);
    }
    
    public static String handleBardFormat(long millis, boolean trailingZero, boolean showMillis) {
        return (showMillis ? (trailingZero ? Formats.REMAINING_SECONDS_TRAILING : Formats.REMAINING_SECONDS) : Formats.SECONDS).get().format(millis * 0.001);
    }
    
    public static void resendHeldItemPacket(Player player) {
        sendItemPacketAtHeldSlot(player, getCleanHeldItem(player));
    }
    
    public static void sendItemPacketAtHeldSlot(Player player, ItemStack stack) {
        sendItemPacketAtSlot(player, stack, player.getInventory().getHeldItemSlot());
    }
    
    public static void sendItemPacketAtSlot(Player player, ItemStack stack, int index) {
        sendItemPacketAtSlot(player, stack, index, ((CraftPlayer)player).getHandle().defaultContainer.windowId);
    }
    
    public static void sendItemPacketAtSlot(Player player, ItemStack stack, int index, int windowID) {
        EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
        if (entityPlayer.playerConnection != null) {
            if (index < PlayerInventory.getHotbarSize()) {
                index += 36;
            }
            else if (index > 35) {
                index = 8 - (index - 36);
            }
            entityPlayer.playerConnection.sendPacket(new PacketPlayOutSetSlot(windowID, index, stack));
        }
    }
    
    public static ItemStack getCleanItem(Inventory inventory, int slot) {
        return ((CraftInventory)inventory).getInventory().getItem(slot);
    }
    
    public static ItemStack getCleanItem(Player player, int slot) {
        return getCleanItem(player.getInventory(), slot);
    }
    
    public static ItemStack getCleanHeldItem(Player player) {
        return getCleanItem(player, player.getInventory().getHeldItemSlot());
    }
    
    public static boolean isOnline(CommandSender sender, Player player) {
        return player != null && (!(sender instanceof Player) || ((Player)sender).canSee(player));
    }
    
    public static int getPing(Player player) {
        CraftPlayer craft = (CraftPlayer)player;
        int ping = craft.getHandle().ping - 20;
        return Math.max(ping, 0);
    }
    
    public static List<Entity> getNearby(Location loc, int distance) {
        List<Entity> list = new ArrayList<>();
        for (Entity e : loc.getWorld().getEntities()) {
            if (e instanceof Player) {
                continue;
            }
            if (!e.getType().isAlive()) {
                continue;
            }
            if (loc.distance(e.getLocation()) > distance) {
                continue;
            }
            list.add(e);
        }
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            if (online.getWorld() == loc.getWorld() && loc.distance(online.getLocation()) <= distance) {
                list.add(online);
            }
        }
        return list;
    }
}

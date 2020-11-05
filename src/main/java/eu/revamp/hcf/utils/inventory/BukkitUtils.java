package eu.revamp.hcf.utils.inventory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import gnu.trove.list.TCharList;
import gnu.trove.list.array.TCharArrayList;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BukkitUtils {
    public static ImmutableMap<Object, Object> CHAT_DYE_COLOUR_MAP = ImmutableMap.builder().put(ChatColor.AQUA, DyeColor.LIGHT_BLUE).put(ChatColor.BLACK, DyeColor.BLACK).put(ChatColor.BLUE, DyeColor.LIGHT_BLUE).put(ChatColor.DARK_AQUA, DyeColor.CYAN).put(ChatColor.DARK_BLUE, DyeColor.BLUE).put(ChatColor.DARK_GRAY, DyeColor.GRAY).put(ChatColor.DARK_GREEN, DyeColor.GREEN).put(ChatColor.DARK_PURPLE, DyeColor.PURPLE).put(ChatColor.DARK_RED, DyeColor.RED).put(ChatColor.GOLD, DyeColor.ORANGE).put(ChatColor.GRAY, DyeColor.SILVER).put(ChatColor.GREEN, DyeColor.LIME).put(ChatColor.LIGHT_PURPLE, DyeColor.MAGENTA).put(ChatColor.RED, DyeColor.RED).put(ChatColor.WHITE, DyeColor.WHITE).put(ChatColor.YELLOW, DyeColor.YELLOW).build();
    private static int DEFAULT_COMPLETION_LIMIT = 80;
    private static String STRAIGHT_LINE_TEMPLATE = ChatColor.STRIKETHROUGH.toString() + Strings.repeat("-", 256);
    public static String STRAIGHT_LINE_DEFAULT = BukkitUtils.STRAIGHT_LINE_TEMPLATE.substring(0, 55);
    private static TCharList COLOUR_CHARACTER_LIST;


    static {
        ChatColor[] values = ChatColor.values();
        BukkitUtils.COLOUR_CHARACTER_LIST = new TCharArrayList(values.length);
        ChatColor[] array;
        for (int length = (array = values).length, i = 0; i < length; ++i) {
            ChatColor colour = array[i];
            BukkitUtils.COLOUR_CHARACTER_LIST.add(colour.getChar());
        }
    }
    
    public static int countColoursUsed(String id, boolean ignoreDuplicates) {
        int count = 0;
        Set<ChatColor> found = new HashSet<>();
        for (int i = 1; i < id.length(); ++i) {
            char current = id.charAt(i);
            if (BukkitUtils.COLOUR_CHARACTER_LIST.contains(current) && id.charAt(i - 1) == '&' && (ignoreDuplicates || found.add(ChatColor.getByChar(current)))) {
                ++count;
            }
        }
        return count;
    }
    
    public static List<String> getCompletions(String[] args, List<String> input) {
        return getCompletions(args, input, 80);
    }
    
    public static List<String> getCompletions(String[] args, List<String> input, int limit) {
        Preconditions.checkNotNull(args);
        Preconditions.checkArgument(args.length != 0);
        String argument = args[args.length - 1];
        String s = null;
        return input.stream().filter(string -> string.regionMatches(true, 0, s, 0, s.length())).limit(limit).collect(Collectors.toList());
    }
    
    public static boolean isWithinX(Location location, Location other, double distance) {
        return location.getWorld().equals(other.getWorld()) && Math.abs(other.getX() - location.getX()) <= distance && Math.abs(other.getZ() - location.getZ()) <= distance;
    }
    
    public static Location getHighestLocation(Location origin) {
        return getHighestLocation(origin, null);
    }
    
    public static Location getHighestLocation(Location origin, Location def) {
        Preconditions.checkNotNull(origin, "The location cannot be null");
        Location cloned = origin.clone();
        World world = cloned.getWorld();
        int x = cloned.getBlockX();
        int y = world.getMaxHeight();
        int z = cloned.getBlockZ();
        while (y > origin.getBlockY()) {
            Block block = world.getBlockAt(x, --y, z);
            if (!block.isEmpty()) {
                Location next = block.getLocation();
                next.setPitch(origin.getPitch());
                next.setYaw(origin.getYaw());
                return next;
            }
        }
        return def;
    }
}

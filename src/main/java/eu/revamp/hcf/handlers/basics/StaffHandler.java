package eu.revamp.hcf.handlers.basics;

import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.GameMode;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;
import java.util.Date;
import java.text.SimpleDateFormat;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import eu.revamp.hcf.RevampHCF;
import java.io.File;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class StaffHandler extends Handler implements Listener
{
    private final File staffCommandDirectory;
    private final File staffBlockDirectory;
    private final File staffDropDirectory;
    private final File staffChatDirectory;
    private final File staffJLDirectory;
    
    public StaffHandler(RevampHCF plugin) {
        super(plugin);
        this.staffCommandDirectory = new File(this.getInstance().getDataFolder(), "staff" + File.separator + "command");
        this.staffBlockDirectory = new File(this.getInstance().getDataFolder(), "staff" + File.separator + "block");
        this.staffDropDirectory = new File(this.getInstance().getDataFolder(), "staff" + File.separator + "drop");
        this.staffChatDirectory = new File(this.getInstance().getDataFolder(), "staff" + File.separator + "chat");
        this.staffJLDirectory = new File(this.getInstance().getDataFolder(), "staff" + File.separator + "joinleave");
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy - HH:mm:ss");
        Date date = new Date();
        if (player == null) return;
        if (player.hasPermission("revamphcf.staff")) {
            File file = new File(this.staffCommandDirectory, player.getName() + ".yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                    configuration.set(sdf.format(date), event.getMessage());
                    configuration.save(file);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                configuration.set(sdf.format(date), event.getMessage());
                try {
                    configuration.save(file);
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy - HH:mm:ss");
        Date date = new Date();
        if (player == null) return;
        if (player.hasPermission("revamphcf.staff")) {
            File file = new File(this.staffJLDirectory, player.getName() + ".yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                    configuration.set(sdf.format(date), "Join");
                    configuration.save(file);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                configuration.set(sdf.format(date), "Join");
                try {
                    configuration.save(file);
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy - HH:mm:ss");
        Date date = new Date();
        if (player == null) return;
        if (player.hasPermission("revamphcf.staff")) {
            File file = new File(this.staffJLDirectory, player.getName() + ".yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                    configuration.set(sdf.format(date), "Left");
                    configuration.save(file);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                configuration.set(sdf.format(date), "Left");
                try {
                    configuration.save(file);
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy - HH:mm:ss");
        Date date = new Date();
        if (player == null) return;
        if (player.hasPermission("revamphcf.staff")) {
            File file = new File(this.staffChatDirectory, player.getName() + ".yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                    configuration.set(sdf.format(date), event.getMessage());
                    configuration.save(file);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                configuration.set(sdf.format(date), event.getMessage());
                try {
                    configuration.save(file);
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy - HH:mm:ss");
        Date date = new Date();
        if ((player.isOp() || player.getGameMode() == GameMode.CREATIVE) && (block.getType() == Material.MOB_SPAWNER || block.getType() == Material.BEACON || block.getType() == Material.ENDER_PORTAL_FRAME || block.getType() == Material.EMERALD_BLOCK || block.getType() == Material.DIAMOND_BLOCK || block.getType() == Material.BEDROCK)) {
            File file = new File(this.staffBlockDirectory, player.getName() + ".yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                    configuration.set(sdf.format(date), block.getType().toString() + " " + block.getX() + " " + block.getY() + " " + block.getZ());
                    configuration.save(file);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                configuration.set(sdf.format(date), block.getType().toString() + " " + block.getX() + " " + block.getY() + " " + block.getZ());
                try {
                    configuration.save(file);
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Item item = event.getItemDrop();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy - HH:mm:ss");
        Date date = new Date();
        if ((player.isOp() || player.getGameMode() == GameMode.CREATIVE) && (item.getItemStack().getType() == Material.EMERALD || item.getItemStack().getType() == Material.EMERALD_BLOCK || item.getItemStack().getType() == Material.POTION || item.getItemStack().getType() == Material.GOLDEN_APPLE || item.getItemStack().getType() == Material.ENDER_PEARL || item.getItemStack().getType() == Material.SULPHUR || item.getItemStack().getType() == Material.BEACON || item.getItemStack().getType() == Material.DIAMOND || item.getItemStack().getType() == Material.DIAMOND_BLOCK || item.getItemStack().getType() == Material.GOLD_INGOT || item.getItemStack().getType() == Material.GOLD_BLOCK || item.getItemStack().getType() == Material.IRON_INGOT || item.getItemStack().getType() == Material.IRON_BLOCK || item.getItemStack().getType() == Material.ENDER_PORTAL_FRAME || item.getItemStack().getType() == Material.BEDROCK)) {
            File file = new File(this.staffDropDirectory, player.getName() + ".yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                    configuration.set(sdf.format(date), item.getItemStack().getType().toString() + " " + item.getItemStack().getAmount());
                    configuration.save(file);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                configuration.set(sdf.format(date), item.getItemStack().getType().toString() + " " + item.getItemStack().getAmount());
                try {
                    configuration.save(file);
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
}

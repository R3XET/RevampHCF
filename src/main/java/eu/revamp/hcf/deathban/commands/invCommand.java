package eu.revamp.hcf.deathban.commands;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.chat.message.MessageUtils;
import eu.revamp.spigot.utils.serialize.BukkitSerilization;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;

import java.io.File;
import org.bukkit.event.Listener;
import eu.revamp.hcf.commands.BaseCommand;

public class invCommand extends BaseCommand implements Listener
{
    public static File deathbanFolder = new File(RevampHCF.getInstance().getDataFolder(), "deathban");
    public static File deathbansFolder = new File(invCommand.deathbanFolder, "deathbans");
    private final File inventoriesFolder;

    public invCommand(RevampHCF plugin) {
        super(plugin);
        this.inventoriesFolder = new File(invCommand.deathbanFolder, "inventories");
        this.command = "inv";
        this.permission = "revamphcf.rollback";
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (!RevampHCF.getInstance().getConfiguration().isKitMap()) {
                if (args.length == 0) {
                    this.sendUsage(player);
                    return;
                }
                Player target = Bukkit.getPlayer(args[0]);
                this.checkPlayerInv(player, target);
            }
            else if (RevampHCF.getInstance().getConfiguration().isKitMap()) {
                player.sendMessage(Language.COMMANDS_NO_KITMAP.toString());
            }
        }
    }
    
    public void rollbackPlayerInv(Player player, Player target) {
        File targetFile = new File(this.inventoriesFolder, target.getUniqueId().toString() + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(targetFile);
        boolean used = configuration.getBoolean("used");
        if (targetFile.exists()) {
            if (!used) {
                try {
                    ItemStack[] contents = BukkitSerilization.itemStackArrayFromBase64(configuration.getString("inventory"));
                    ItemStack[] armor = BukkitSerilization.itemStackArrayFromBase64(configuration.getString("armor"));
                    target.getInventory().setContents(contents);
                    target.getInventory().setArmorContents(armor);
                    configuration.set("used", true);
                    configuration.save(targetFile);
                }
                catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                target.sendMessage(CC.translate("&eYour inventory has been rollbacked by &f" + player.getName() + "&e."));
                MessageUtils.sendMessage(CC.translate("&a&l" + player.getName() + "&arollbacked inventory of &l" + target.getName() + "&a."), "hcf.admin");
            }
            else {
                player.sendMessage(CC.translate("&cInventory of &l" + target.getName() + " &c is already rollbacked!"));
            }
        }
        else {
            player.sendMessage(CC.translate("&c&l" + target.getName() + " &chas not died yet."));
        }
    }
    
    public void checkPlayerInv(Player player, Player target) {
        File targetFile = new File(this.inventoriesFolder, target.getUniqueId().toString() + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(targetFile);
        boolean used = configuration.getBoolean("used");
        Inventory inv = Bukkit.createInventory(null, 54, target.getName() + "'s Inventory");
        if (targetFile.exists()) {
            if (!used) {
                ItemStack[] contents = BukkitSerilization.itemStackArrayFromBase64(configuration.getString("inventory"));
                ItemStack[] armor = BukkitSerilization.itemStackArrayFromBase64(configuration.getString("armor"));
                inv.setContents(contents);
                inv.setItem(45, armor[0]);
                inv.setItem(46, armor[1]);
                inv.setItem(47, armor[2]);
                inv.setItem(48, armor[3]);
                inv.setItem(36, this.createGlass(ChatColor.RED + "Inventory Preview"));
                inv.setItem(37, this.createGlass(ChatColor.RED + "Inventory Preview"));
                inv.setItem(38, this.createGlass(ChatColor.RED + "Inventory Preview"));
                inv.setItem(39, this.createGlass(ChatColor.RED + "Inventory Preview"));
                inv.setItem(40, this.createGlass(ChatColor.RED + "Inventory Preview"));
                inv.setItem(41, this.createGlass(ChatColor.RED + "Inventory Preview"));
                inv.setItem(42, this.createGlass(ChatColor.RED + "Inventory Preview"));
                inv.setItem(43, this.createGlass(ChatColor.RED + "Inventory Preview"));
                inv.setItem(44, this.createGlass(ChatColor.RED + "Inventory Preview"));
                inv.setItem(49, this.createGlass(ChatColor.RED + "Inventory Preview"));
                inv.setItem(52, this.createWool(ChatColor.RED + "Close Preview", 14));
                inv.setItem(53, this.createWool(ChatColor.GREEN + "Rollback Inventory (" + target.getName() + ")", 5));
                player.openInventory(inv);
            }
            else {
                player.sendMessage(CC.translate("&cInventory of &l" + target.getName() + " &c is already rollbacked!"));
            }
        }
        else {
            player.sendMessage(CC.translate("&c&l" + target.getName() + " &chas not died yet."));
        }
    }
    
    public ItemStack createWool(String name, int value) {
        ItemStack item = new ItemStack(Material.WOOL, 1, (short)value);
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(name);
        item.setItemMeta(itemmeta);
        return item;
    }
    
    public ItemStack createGlass(String name) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)7);
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(name);
        item.setItemMeta(itemmeta);
        return item;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        Inventory inventory = event.getInventory();
        if (!inventory.getName().endsWith("'s Inventory")) return;
        if (event.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) return;
        if (item.getType().equals(Material.AIR)) return;
        if (inventory.getName().endsWith("'s Inventory")) {
            event.setCancelled(true);
            if (item.getItemMeta().getDisplayName().contains("Close")) {
                new BukkitRunnable() {
                    public void run() {
                        player.closeInventory();
                    }
                }.runTaskLater(RevampHCF.getInstance(), 1L);
            }
            else if (item.getItemMeta().getDisplayName().contains("Rollback")) {
                String[] name = event.getCurrentItem().getItemMeta().getDisplayName().split("\\(");
                Player target = Bukkit.getPlayer(name[1].replaceAll("\\)", ""));
                if (target != null) {
                    this.rollbackPlayerInv(player, target);
                }
                else {
                    player.sendMessage("\u00ef§cPlayer " + name[1].replaceAll("\\)", "") + " is not online!");
                }
                new BukkitRunnable() {
                    public void run() {
                        player.closeInventory();
                    }
                }.runTaskLater(RevampHCF.getInstance(), 1L);
            }
        }
    }
    
    public void sendUsage(CommandSender sender) {
        sender.sendMessage(CC.translate("&cUsage: /inv (playerName)"));
    }
}

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
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;

import java.io.File;
import org.bukkit.event.Listener;
import eu.revamp.hcf.commands.BaseCommand;

public class DeathBanCommand extends BaseCommand implements Listener
{
    public static File deathbanFolder = new File(RevampHCF.getInstance().getDataFolder(), "deathban");
    public static File deathbansFolder = new File(DeathBanCommand.deathbanFolder, "deathbans");
    private final File inventoriesFolder;

    public DeathBanCommand(RevampHCF plugin) {
        super(plugin);
        this.inventoriesFolder = new File(DeathBanCommand.deathbanFolder, "inventories");
        this.command = "deathban";
        this.permission = "revamphcf.staff";
    }
    
    @Override @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if ((player.hasPermission("hcf.staff") && !RevampHCF.getInstance().getConfiguration().isKitMap()) || (player.hasPermission("hcf.revive") && !RevampHCF.getInstance().getConfiguration().isKitMap()) || (player.hasPermission("hcf.rollback") && !RevampHCF.getInstance().getConfiguration().isKitMap())) {
                if (args.length == 0) {
                    this.sendUsage(player);
                }
                else if (args.length == 1) {
                    switch (args[0]) {
                        case "check":
                            if (player.hasPermission("hcf.staff")) {
                                player.sendMessage(CC.translate("&cCorrect Usage: /deathban check <playerName>."));
                            } else {
                                player.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
                            }
                            break;
                        case "revive":
                            if (player.hasPermission("hcf.revive")) {
                                player.sendMessage(CC.translate("&cCorrect Usage: /deathban revive <playerName>."));
                            } else {
                                player.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
                            }
                            break;
                        case "rollback":
                            if (player.hasPermission("hcf.rollback")) {
                                player.sendMessage(CC.translate("&cCorrect Usage: /deathban rollback <playerName>."));
                            } else {
                                player.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
                            }
                            break;
                        default:
                            this.sendUsage(player);
                            break;
                    }
                }
                else if (args.length == 2) {
                    switch (args[0]) {
                        case "check":
                            if (player.hasPermission("hcf.staff")) {
                                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                                this.checkPlayer(player, target);
                            } else {
                                player.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
                            }
                            break;
                        case "revive":
                            if (player.hasPermission("hcf.revive")) {
                                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                                this.revivePlayer(sender, target);
                            } else {
                                player.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
                            }
                            break;
                        case "rollback":
                            if (player.hasPermission("hcf.rollback")) {
                                Player target2 = Bukkit.getPlayer(args[1]);
                                if (target2 != null) {
                                    this.checkPlayerInv(player, target2);
                                } else {
                                    player.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
                                }
                            } else {
                                player.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
                            }
                            break;
                        default:
                            this.sendUsage(player);
                            break;
                    }
                }
                else {
                    this.sendUsage(player);
                }
            }
            else if ((!player.hasPermission("hcf.staff") && !RevampHCF.getInstance().getConfiguration().isKitMap()) || (!player.hasPermission("hcf.revive") && !RevampHCF.getInstance().getConfiguration().isKitMap()) || (!player.hasPermission("hcf.rollback") && !RevampHCF.getInstance().getConfiguration().isKitMap())) {
                player.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
            }
            else if (RevampHCF.getInstance().getConfiguration().isKitMap()) {
                player.sendMessage(Language.COMMANDS_NO_KITMAP.toString());
            }
        }
        else if (sender instanceof ConsoleCommandSender) {
            if (args.length == 0) {
                this.sendUsage(sender);
            }
            else if (args.length == 1) {
                if (args[0].equals("check")) {
                    sender.sendMessage(CC.translate("&cCorrect Usage: /deathban check <playerName>."));
                }
                else if (args[0].equals("revive")) {
                    sender.sendMessage(CC.translate("&cCorrect Usage: /deathban revive <playerName>."));
                }
                else {
                    sender.sendMessage(CC.translate("&cNo console."));
                }
            }
            else if (args.length == 2) {
                if (args[0].equals("revive")) {
                    OfflinePlayer target3 = Bukkit.getOfflinePlayer(args[1]);
                    this.revivePlayer(sender, target3);
                }
                else if (args[0].equals("check")) {
                    OfflinePlayer target3 = Bukkit.getOfflinePlayer(args[1]);
                    this.checkPlayer(sender, target3);
                }
                else {
                    sender.sendMessage(CC.translate("&cNo console."));
                }
            }
            else {
                this.sendUsage(sender);
            }
        }
    }
    
    public void checkPlayer(CommandSender sender, OfflinePlayer target) {
        File targetFile = new File(DeathBanCommand.deathbansFolder, target.getUniqueId().toString() + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(targetFile);
        long banTime = configuration.getLong("ban_until");
        if (targetFile.exists()) {
            String duration = "";
            if (banTime - System.currentTimeMillis() <= 0L) {
                duration = "Unbanned";
            }
            else {
                duration = DurationFormatUtils.formatDurationWords(banTime - System.currentTimeMillis(), true, true);
            }
            sender.sendMessage(CC.translate("&7&m-----------------------------"));
            sender.sendMessage(CC.translate(" &c&lDeathBan Check"));
            sender.sendMessage(CC.translate("  &ePlayer: &f" + target.getName()));
            sender.sendMessage(CC.translate("  &eDuration: &f" + duration));
            sender.sendMessage(CC.translate("  &eReason: &f" + configuration.getString("death_message")));
            sender.sendMessage(CC.translate("  &eLocation: &f" + configuration.getString("coords")));
            sender.sendMessage(CC.translate("&7&m-----------------------------"));
        }
        else {
            sender.sendMessage(CC.translate("&c&l" + target.getName() + " &cis not death-banned."));
        }
    }
    
    public void revivePlayer(CommandSender sender, OfflinePlayer target) {
        File targetFile = new File(DeathBanCommand.deathbansFolder, target.getUniqueId().toString() + ".yml");
        if (targetFile.exists()) {
            targetFile.delete();
            MessageUtils.sendMessage(CC.translate("&a&l" + sender.getName() + " &asuccessfully revived &l" + target.getName() + "&a."), "hcf.admin");
            RevampHCF.getInstance().getFactionManager().getPlayerFaction(target.getUniqueId()).setDeathsUntilRaidable(RevampHCF.getInstance().getFactionManager().getPlayerFaction(target.getUniqueId()).getDeathsUntilRaidable() + RevampHCF.getInstance().getConfig().getDouble("FACTIONS-SETTINGS.dtrLossMultiplier"));
            MessageUtils.sendMessage(CC.translate("&a&l" + sender.getName() + " &aadded &l" + RevampHCF.getInstance().getConfig().getDouble("FACTIONS-SETTINGS.dtrLossMultiplier") + "&a DTR &l" + target.getName() + "&a."), "hcf.admin");
        }
        else {
            sender.sendMessage(CC.translate("&c&l" + target.getName() + " &cis not death-banned."));
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
                MessageUtils.sendMessage(CC.translate("&a&l" + player.getName() + " &arollbacked inventory of &l" + target.getName() + "&a."), "hcf.admin");
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
    
    public void sendUsage(CommandSender sender) {
        sender.sendMessage(CC.translate("&cDeathBan - Help Commands"));
        sender.sendMessage(CC.translate("&c/deathban check <playerName> - Check player's deathban."));
        sender.sendMessage(CC.translate("&c/deathban revive <playerName> - Remove player's deathban."));
        sender.sendMessage(CC.translate("&c/deathban rollback <playerName> - Rollback player's inventory."));
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
                    player.sendMessage("§cPlayer " + name[1].replaceAll("\\)", "") + " is not online!");
                }
                new BukkitRunnable() {
                    public void run() {
                        player.closeInventory();
                    }
                }.runTaskLater(RevampHCF.getInstance(), 1L);
            }
        }
    }
}

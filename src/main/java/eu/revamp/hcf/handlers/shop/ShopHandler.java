package eu.revamp.hcf.handlers.shop;

import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShopHandler extends Handler implements Listener
{
    private String[] lines;
    private String[] error;
    private ItemStack BLANK;
    @SuppressWarnings("deprecation")
    public ShopHandler(RevampHCF instance) {
        super(instance);
        this.lines = new String[] { CC.translate("&a&l- Shop -"), "", CC.translate("&eClick to buy") };
        this.error = new String[] { CC.translate("&c- Shop -"), "", CC.translate("&cError") };
        this.BLANK = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(DyeColor.GRAY.getData()).setName(" ").toItemStack();
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, RevampHCF.getInstance());
    }

    public Inventory openMainInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, "Welcome to Shop");
        for (int i = 0; i < inv.getSize(); ++i) {
            inv.setItem(i, this.BLANK);
        }
        inv.setItem(2, this.getWeaponsItem());
        inv.setItem(4, this.getPotions());
        inv.setItem(6, this.getOther());
        player.openInventory(inv);
        return inv;
    }
    
    public Inventory openWeaponInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 36, "Weapon Shop");
        for (int i = 0; i < inv.getSize(); ++i) {
            inv.setItem(i, this.BLANK);
        }
        inv.setItem(10, this.getKitMapFire());
        inv.setItem(11, this.getKitMapSharpness());
        inv.setItem(12, this.getKitMapGod());
        inv.setItem(15, this.getKitMapBow());
        inv.setItem(16, this.getKitMapFROD());
        inv.setItem(19, this.setPaper("&7Cost: &f2,500$"));
        inv.setItem(20, this.setPaper("&7Cost: &f4,000$"));
        inv.setItem(21, this.setPaper("&7Cost: &f10,000$"));
        inv.setItem(24, this.setPaper("&7Cost: &f5,000$"));
        inv.setItem(25, this.setPaper("&7Cost: &f1,600$"));
        player.openInventory(inv);
        return inv;
    }
    
    public Inventory openPotionInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 36, "Potion Shop");
        for (int i = 0; i < inv.getSize(); ++i) {
            inv.setItem(i, this.BLANK);
        }
        inv.setItem(10, this.getRegeneration());
        inv.setItem(11, this.getStrength2());
        inv.setItem(12, this.getInvisibillity());
        inv.setItem(14, this.getSlowness());
        inv.setItem(15, this.getPoison());
        inv.setItem(16, this.getHarming());
        inv.setItem(19, this.setPaper("&7Cost: &f1,200$"));
        inv.setItem(20, this.setPaper("&7Cost: &f2,000$"));
        inv.setItem(21, this.setPaper("&7Cost: &f1,000$"));
        inv.setItem(23, this.setPaper("&7Cost: &f700$"));
        inv.setItem(24, this.setPaper("&7Cost: &f1,000$"));
        inv.setItem(25, this.setPaper("&7Cost: &f1,700$"));
        player.openInventory(inv);
        return inv;
    }
    
    public Inventory openOtherInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 36, "Other Shop");
        for (int i = 0; i < inv.getSize(); ++i) {
            inv.setItem(i, this.BLANK);
        }
        inv.setItem(12, this.getSuperGapple());
        inv.setItem(13, this.getGoldenHead());
        inv.setItem(14, this.getGapple());
        inv.setItem(21, this.setPaper("&7Cost: &f2,000$"));
        inv.setItem(22, this.setPaper("&7Cost: &f1,200$"));
        inv.setItem(23, this.setPaper("&7Cost: &f600$"));
        player.openInventory(inv);
        return inv;
    }
    
    @EventHandler
    public void onSignPlace(SignChangeEvent event) {
        if (event.getLine(0).equals("[Shop]")) {
            Player player = event.getPlayer();
            if (player.hasPermission("revamphcf.op")) {
                for (int i = 0; i < this.lines.length; ++i) {
                    event.setLine(i, this.lines[i]);
                }
            }
            else {
                for (int i = 0; i < this.error.length; ++i) {
                    event.setLine(i, this.error[i]);
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (event.useInteractedBlock() == Event.Result.ALLOW && block.getState() instanceof Sign) {
            Sign sign = (Sign)block.getState();
            for (int i = 0; i < this.lines.length; ++i) {
                if (!sign.getLine(i).equals(this.lines[i])) return;
            }
            this.openMainInventory(player);
        }
    }
    
    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR || !event.getCurrentItem().hasItemMeta()) return;
        if (event.getCurrentItem().getItemMeta() == null) return;
        if (event.getClickedInventory().getTitle().equalsIgnoreCase(CC.translate("Welcome to Shop"))) {
            event.setCancelled(true);
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&b&lWeapon Inventory"))) {
                event.setCancelled(true);
                this.openWeaponInventory(player);
            }
            else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&b&lPotion Inventory"))) {
                event.setCancelled(true);
                this.openPotionInventory(player);
            }
            else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&b&lOther Inventory"))) {
                event.setCancelled(true);
                this.openOtherInventory(player);
            }
        }
        else if (event.getClickedInventory().getTitle().equalsIgnoreCase(CC.translate("Weapon Shop"))) {
            event.setCancelled(true);
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&C&k|&6&k|&r &e&lFire Sword &C&k|&6&k|&r"))) {
                event.setCancelled(true);
                if (2500 > data.getBalance()) {
                    player.sendMessage(CC.translate("&cYou can't afford this!"));
                    player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.0f);
                }
                else {
                    data.setBalance(data.getBalance() - 2500);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                    player.sendMessage(CC.translate("&aSuccessful purchase!"));
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(CC.translate("&cYour inventory is full!"));
                    }
                    else {
                        player.getInventory().addItem(this.getKitMapFire());
                        player.updateInventory();
                    }
                }
            }
            else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&b&k|&r &c&lHercules Sword &b&k|&r"))) {
                event.setCancelled(true);
                if (4000 > data.getBalance()) {
                    player.sendMessage(CC.translate("&cYou can't afford this!"));
                    player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.0f);
                }
                else {
                    data.setBalance(data.getBalance() - 4000);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                    player.sendMessage(CC.translate("&aSuccessful purchase!"));
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(CC.translate("&cYour inventory is full!"));
                    }
                    else {
                        player.getInventory().addItem(this.getKitMapSharpness());
                        player.updateInventory();
                    }
                }
            }
            else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&C&k|&6&k&l|&d&k|&r &e&lGod Fire &C&k|&6&k&l|&d&k|&r"))) {
                event.setCancelled(true);
                if (10000 > data.getBalance()) {
                    player.sendMessage(CC.translate("&cYou can't afford this!"));
                    player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.0f);
                }
                else {
                    data.setBalance(data.getBalance() - 10000);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                    player.sendMessage(CC.translate("&aSuccessful purchase!"));
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(CC.translate("&cYour inventory is full!"));
                    }
                    else {
                        player.getInventory().addItem(this.getKitMapGod());
                        player.updateInventory();
                    }
                }
            }
            else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&3&k|&r &d&lLegolas Bow &3&k|&r"))) {
                event.setCancelled(true);
                if (5000 > data.getBalance()) {
                    player.sendMessage(CC.translate("&cYou can't afford this!"));
                    player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.0f);
                }
                else {
                    data.setBalance(data.getBalance() - 5000);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                    player.sendMessage(CC.translate("&aSuccessful purchase!"));
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(CC.translate("&cYour inventory is full!"));
                    }
                    else {
                        player.getInventory().addItem(this.getKitMapBow());
                        player.updateInventory();
                    }
                }
            }
            else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&5&k|&2&k|&r &6&lPescanova Captain &5&k|&2&k|&r"))) {
                event.setCancelled(true);
                if (1600 > data.getBalance()) {
                    player.sendMessage(CC.translate("&cYou can't afford this!"));
                    player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.0f);
                }
                else {
                    data.setBalance(data.getBalance() - 1600);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                    player.sendMessage(CC.translate("&aSuccessful purchase!"));
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(CC.translate("&cYour inventory is full!"));
                    }
                    else {
                        player.getInventory().addItem(this.getKitMapFROD());
                        player.updateInventory();
                    }
                }
            }
        }
        else if (event.getClickedInventory().getTitle().equalsIgnoreCase(CC.translate("Potion Shop"))) {
            event.setCancelled(true);
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&7[&e&lKitMap Strength&7]"))) {
                event.setCancelled(true);
                if (1200 > data.getBalance()) {
                    player.sendMessage(CC.translate("&cYou can't afford this!"));
                    player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.0f);
                }
                else {
                    data.setBalance(data.getBalance() - 1200);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                    player.sendMessage(CC.translate("&aSuccessful purchase!"));
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(CC.translate("&cYour inventory is full!"));
                    }
                    else {
                        player.getInventory().addItem(this.getStrength2());
                        player.updateInventory();
                    }
                }
            }
            else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&7[&e&lKitMap Invisibillity&7]"))) {
                event.setCancelled(true);
                if (2000 > data.getBalance()) {
                    player.sendMessage(CC.translate("&cYou can't afford this!"));
                    player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.0f);
                }
                else {
                    data.setBalance(data.getBalance() - 2000);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                    player.sendMessage(CC.translate("&aSuccessful purchase!"));
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(CC.translate("&cYour inventory is full!"));
                    }
                    else {
                        player.getInventory().addItem(this.getInvisibillity());
                        player.updateInventory();
                    }
                }
            }
            else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&7[&e&lKitMap Regeneration&7]"))) {
                event.setCancelled(true);
                if (1000 > data.getBalance()) {
                    player.sendMessage(CC.translate("&cYou can't afford this!"));
                    player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.0f);
                }
                else {
                    data.setBalance(data.getBalance() - 1000);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                    player.sendMessage(CC.translate("&aSuccessful purchase!"));
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(CC.translate("&cYour inventory is full!"));
                    }
                    else {
                        player.getInventory().addItem(this.getRegeneration());
                        player.updateInventory();
                    }
                }
            }
            else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&7[&e&lKitMap Slowness&7]"))) {
                event.setCancelled(true);
                if (700 > data.getBalance()) {
                    player.sendMessage(CC.translate("&cYou can't afford this!"));
                    player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.0f);
                }
                else {
                    data.setBalance(data.getBalance() - 700);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                    player.sendMessage(CC.translate("&aSuccessful purchase!"));
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(CC.translate("&cYour inventory is full!"));
                    }
                    else {
                        player.getInventory().addItem(this.getSlowness());
                        player.updateInventory();
                    }
                }
            }
            else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&7[&e&lKitMap Poison&7]"))) {
                event.setCancelled(true);
                if (1000 > data.getBalance()) {
                    player.sendMessage(CC.translate("&cYou can't afford this!"));
                    player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.0f);
                }
                else {
                    data.setBalance(data.getBalance() - 1000);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                    player.sendMessage(CC.translate("&aSuccessful purchase!"));
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(CC.translate("&cYour inventory is full!"));
                    }
                    else {
                        player.getInventory().addItem(this.getPoison());
                        player.updateInventory();
                    }
                }
            }
            else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&7[&e&lKitMap Harming&7]"))) {
                event.setCancelled(true);
                if (1700 > data.getBalance()) {
                    player.sendMessage(CC.translate("&cYou can't afford this!"));
                    player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.0f);
                }
                else {
                    data.setBalance(data.getBalance() - 1700);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                    player.sendMessage(CC.translate("&aSuccessful purchase!"));
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(CC.translate("&cYour inventory is full!"));
                    }
                    else {
                        player.getInventory().addItem(this.getHarming());
                        player.updateInventory();
                    }
                }
            }
        }
        else if (event.getClickedInventory().getTitle().equalsIgnoreCase(CC.translate("Other Shop"))) {
            event.setCancelled(true);
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&7[&e&lCrapple&7]"))) {
                event.setCancelled(true);
                if (600 > data.getBalance()) {
                    player.sendMessage(CC.translate("&cYou can't afford this!"));
                    player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.0f);
                }
                else {
                    data.setBalance(data.getBalance() - 600);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                    player.sendMessage(CC.translate("&aSuccessful purchase!"));
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(CC.translate("&cYour inventory is full!"));
                    }
                    else {
                        player.getInventory().addItem(this.getGapple());
                        player.updateInventory();
                    }
                }
            }
            else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&7[&e&lGapple&7]"))) {
                event.setCancelled(true);
                if (2000 > data.getBalance()) {
                    player.sendMessage(CC.translate("&cYou can't afford this!"));
                    player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.0f);
                }
                else {
                    data.setBalance(data.getBalance() - 2000);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                    player.sendMessage(CC.translate("&aSuccessful purchase!"));
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(CC.translate("&cYour inventory is full!"));
                    }
                    else {
                        player.getInventory().addItem(this.getSuperGapple());
                        player.updateInventory();
                    }
                }
            }
            else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&6&lGolden Head"))) {
                event.setCancelled(true);
                if (1200 > data.getBalance()) {
                    player.sendMessage(CC.translate("&cYou can't afford this!"));
                    player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.0f);
                }
                else {
                    data.setBalance(data.getBalance() - 1200);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                    player.sendMessage(CC.translate("&aSuccessful purchase!"));
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(CC.translate("&cYour inventory is full!"));
                    }
                    else {
                        player.getInventory().addItem(this.getGoldenHead());
                        player.updateInventory();
                    }
                }
            }
        }
    }
    
    public ItemStack setPaper(String string) {
        ItemStack stack = new ItemStack(Material.PAPER);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate(string));
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getWeaponsItem() {
        ItemStack stack = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&b&lWeapon Inventory"));
        List<String> lore = new ArrayList<>();
        lore.add(CC.translate("&7Weapon items inventory."));
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getPotions() {
        ItemStack stack = new ItemStack(Material.POTION);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&b&lPotion Inventory"));
        List<String> lore = new ArrayList<>();
        lore.add(CC.translate("&7Potion items inventory."));
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getOther() {
        ItemStack stack = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&b&lOther Inventory"));
        List<String> lore = new ArrayList<>();
        lore.add(CC.translate("&7Other items inventory."));
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getKitMapFire() {
        ItemStack stack = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&C&k|&6&k|&r &e&lFire Sword &C&k|&6&k|&r"));
        meta.addEnchant(Enchantment.DAMAGE_ALL, RevampHCF.getInstance().getConfig().getInt("KITS.SHARPNESS"), true);
        meta.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getKitMapSharpness() {
        ItemStack stack = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&b&k|&r &c&lHercules Sword &b&k|&r"));
        meta.addEnchant(Enchantment.DAMAGE_ALL, RevampHCF.getInstance().getConfig().getInt("KITS.SHARPNESS") + 1, true);
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getKitMapGod() {
        ItemStack stack = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&C&k|&6&k&l|&d&k|&r &e&lGod Fire &C&k|&6&k&l|&d&k|&r"));
        meta.addEnchant(Enchantment.DAMAGE_ALL, RevampHCF.getInstance().getConfig().getInt("KITS.SHARPNESS") + 1, true);
        meta.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getKitMapBow() {
        ItemStack stack = new ItemStack(Material.BOW);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&3&k|&r &d&lLegolas Bow &3&k|&r"));
        meta.addEnchant(Enchantment.ARROW_DAMAGE, RevampHCF.getInstance().getConfig().getInt("KITS.POWER") + 1, true);
        meta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.addEnchant(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING") + 1, true);
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getKitMapFROD() {
        ItemStack stack = new ItemStack(Material.FISHING_ROD);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&5&k|&2&k|&r &6&lPescanova Captain &5&k|&2&k|&r"));
        meta.addEnchant(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING") + 1, true);
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getStrength2() {
        ItemStack stack = new ItemStack(Material.POTION, 1, (short)16425);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&7[&e&lKitMap Strength&7]"));
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getInvisibillity() {
        ItemStack stack = new ItemStack(Material.POTION, 1, (short)16462);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&7[&e&lKitMap Invisibillity&7]"));
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getRegeneration() {
        ItemStack stack = new ItemStack(Material.POTION, 1, (short)16449);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&7[&e&lKitMap Regeneration&7]"));
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getSlowness() {
        ItemStack stack = new ItemStack(Material.POTION, 1, (short)16426);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&7[&e&lKitMap Slowness&7]"));
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getPoison() {
        ItemStack stack = new ItemStack(Material.POTION, 1, (short)16388);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&7[&e&lKitMap Poison&7]"));
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getHarming() {
        ItemStack stack = new ItemStack(Material.POTION, 1, (short)16428);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&7[&e&lKitMap Harming&7]"));
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getSuperGapple() {
        ItemStack stack = new ItemStack(Material.GOLDEN_APPLE, 1, (short)1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&7[&e&lGapple&7]"));
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getGoldenHead() {
        ItemStack stack = new ItemStack(Material.GOLDEN_APPLE, 1, (short)0);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&6&lGolden Head"));
        meta.setLore(Collections.singletonList(CC.translate("&7This will grant you better effects than a Gapple!")));
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getGapple() {
        ItemStack stack = new ItemStack(Material.GOLDEN_APPLE, 1, (short)0);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&7[&e&lCrapple&7]"));
        stack.setItemMeta(meta);
        return stack;
    }
}

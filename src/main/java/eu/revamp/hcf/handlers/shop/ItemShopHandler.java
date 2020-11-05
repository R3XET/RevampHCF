package eu.revamp.hcf.handlers.shop;

import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.item.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
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

import java.util.Collections;
@Getter @Setter
public class ItemShopHandler extends Handler implements Listener
{
    private String[] lines;
    private String[] error;
    private ItemStack BLANK;
    @SuppressWarnings("deprecation")
    public ItemShopHandler(RevampHCF instance) {
        super(instance);
        setLines(new String[] { CC.translate("&b&l- Items -"), "", CC.translate("&eClick to buy") });
        setError(new String[] { CC.translate("&c&l- Items -"), "", CC.translate("&cError") });
        setBLANK(new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(DyeColor.GRAY.getData()).setName(" ").toItemStack());
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, RevampHCF.getInstance());
    }

    public Inventory openMainInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 45, "Welcome to Shop");
        for (int i = 0; i < inv.getSize(); ++i) {
            inv.setItem(i, this.BLANK);
        }
        inv.setItem(10, this.getGrapplingHook());
        inv.setItem(12, this.getPartnerHitch());
        inv.setItem(14, this.getStormBreaker());
        inv.setItem(16, this.getPyroBall());
        inv.setItem(29, this.getUltimatePearl());
        inv.setItem(31, this.getMalEgg());
        inv.setItem(33, this.getThrowCobweb());
        inv.setItem(19, this.setPaper("&7Cost: &f3,000$"));
        inv.setItem(21, this.setPaper("&7Cost: &f4,000$"));
        inv.setItem(23, this.setPaper("&7Cost: &f10,000$"));
        inv.setItem(25, this.setPaper("&7Cost: &f2,000$"));
        inv.setItem(38, this.setPaper("&7Cost: &f800$"));
        inv.setItem(40, this.setPaper("&7Cost: &f600$"));
        inv.setItem(42, this.setPaper("&7Cost: &f400$"));
        player.openInventory(inv);
        return inv;
    }
    
    @EventHandler
    public void onSignPlace(SignChangeEvent event) {
        if (event.getLine(0).equals("[Items]")) {
            Player player = event.getPlayer();
            if (player.hasPermission("revamphcf.op")) {
                for (int i = 0; i < getLines().length; ++i) {
                    event.setLine(i, getLines()[i]);
                }
            }
            else {
                for (int i = 0; i < getError().length; ++i) {
                    event.setLine(i, getError()[i]);
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
            for (int i = 0; i < getLines().length; ++i) {
                if (!sign.getLine(i).equals(getLines()[i])) return;
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
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&6&lGrappling Hook"))) {
                event.setCancelled(true);
                if (3000 > data.getBalance()) {
                    player.sendMessage(CC.translate("&cYou can't afford this!"));
                    player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.0f);
                }
                else {
                    data.setBalance(data.getBalance() - 3000);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                    player.sendMessage(CC.translate("&aSuccessful purchase!"));
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(CC.translate("&cYour inventory is full!"));
                    }
                    else {
                        player.getInventory().addItem(this.getGrapplingHook());
                        player.updateInventory();
                    }
                }
            }
            else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&a&lPartner Hitch"))) {
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
                        player.getInventory().addItem(this.getPartnerHitch());
                        player.updateInventory();
                    }
                }
            }
            else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&6&lStormBreaker"))) {
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
                        player.getInventory().addItem(this.getStormBreaker());
                        player.updateInventory();
                    }
                }
            }
            else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&4&lPyro Ball"))) {
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
                        player.getInventory().addItem(this.getPyroBall());
                        player.updateInventory();
                    }
                }
            }
            else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&b&lUltimate Pearl"))) {
                event.setCancelled(true);
                if (800 > data.getBalance()) {
                    player.sendMessage(CC.translate("&cYou can't afford this!"));
                    player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.0f);
                }
                else {
                    data.setBalance(data.getBalance() - 800);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                    player.sendMessage(CC.translate("&aSuccessful purchase!"));
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(CC.translate("&cYour inventory is full!"));
                    }
                    else {
                        player.getInventory().addItem(this.getUltimatePearl());
                        player.updateInventory();
                    }
                }
            }
            else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&c&lMalignant Egg"))) {
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
                        player.getInventory().addItem(this.getMalEgg());
                        player.updateInventory();
                    }
                }
            }
            else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&6&lThrowableCobweb"))) {
                event.setCancelled(true);
                if (400 > data.getBalance()) {
                    player.sendMessage(CC.translate("&cYou can't afford this!"));
                    player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.0f);
                }
                else {
                    data.setBalance(data.getBalance() - 400);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                    player.sendMessage(CC.translate("&aSuccessful purchase!"));
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(CC.translate("&cYour inventory is full!"));
                    }
                    else {
                        player.getInventory().addItem(this.getThrowCobweb());
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
    
    public ItemStack getGrapplingHook() {
        ItemStack stack = new ItemStack(Material.FISHING_ROD);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&6&lGrappling Hook"));
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getPartnerHitch() {
        ItemStack stack = new ItemStack(Material.FISHING_ROD);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&a&lPartner Hitch"));
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getStormBreaker() {
        ItemStack stack = new ItemStack(Material.DIAMOND_AXE);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&6&lStormBreaker"));
        meta.setLore(Collections.singletonList(CC.translate("&7Right click to launch the hammer")));
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getPyroBall() {
        ItemStack stack = new ItemStack(Material.SNOW_BALL, 16);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&4&lPyro Ball"));
        meta.setLore(Collections.singletonList(CC.translate("&7You will receive 8s of cooldown")));
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getUltimatePearl() {
        ItemStack stack = new ItemStack(Material.ENDER_PEARL, 16);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&b&lUltimate Pearl"));
        meta.setLore(Collections.singletonList(CC.translate("&7You will receive 5s of cooldown")));
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getMalEgg() {
        ItemStack stack = new ItemStack(Material.EGG, 16);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&c&lMalignant Egg"));
        meta.setLore(Collections.singletonList(CC.translate("&7You will receive 20s of cooldown")));
        stack.setItemMeta(meta);
        return stack;
    }
    
    public ItemStack getThrowCobweb() {
        ItemStack stack = new ItemStack(Material.WEB, 16);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate("&6&lThrowableCobweb"));
        meta.setLore(Collections.singletonList(CC.translate("&7You will receive 10s of cooldown")));
        stack.setItemMeta(meta);
        return stack;
    }
}

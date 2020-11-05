package eu.revamp.hcf.handlers.custom;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CustomEnchantHandler extends Handler implements Listener {
    public CustomEnchantHandler(RevampHCF instance) {
        super(instance);
    }

    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }

    public static Inventory gui = Bukkit.createInventory(null, 45, CC.translate(RevampHCF.getInstance().getConfig().getString("ENCHANT-GUI-NAME")));

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        ItemStack speed = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("ENCHANTS.SPEED.NAME")).setLore(this.getInstance().getConfig().getStringList("ENCHANTS.SPEED.LORE")).toItemStack();
        ItemStack fire = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("ENCHANTS.FIRE.NAME")).setLore(this.getInstance().getConfig().getStringList("ENCHANTS.FIRE.LORE")).toItemStack();
        ItemStack str = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("ENCHANTS.STRENGTH.NAME")).setLore(this.getInstance().getConfig().getStringList("ENCHANTS.STRENGTH.LORE")).toItemStack();
        ItemStack jump = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("ENCHANTS.JUMP.NAME")).setLore(this.getInstance().getConfig().getStringList("ENCHANTS.JUMP.LORE")).toItemStack();
        ItemStack haste = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("ENCHANTS.HASTE.NAME")).setLore(this.getInstance().getConfig().getStringList("ENCHANTS.HASTE.LORE")).toItemStack();
        ItemStack water = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("ENCHANTS.WATERBREATHING.NAME")).setLore(this.getInstance().getConfig().getStringList("ENCHANTS.WATERBREATHING.LORE")).toItemStack();
        ItemStack vision = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("ENCHANTS.NIGHTVISION.NAME")).setLore(this.getInstance().getConfig().getStringList("ENCHANTS.NIGHTVISION.LORE")).toItemStack();
        ItemStack feast = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("ENCHANTS.IMPLANTS.NAME")).setLore(this.getInstance().getConfig().getStringList("ENCHANTS.IMPLANTS.LORE")).toItemStack();
        ItemStack smelt = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("ENCHANTS.SMELT.NAME")).setLore(this.getInstance().getConfig().getStringList("ENCHANTS.SMELT.LORE")).toItemStack();
        ItemStack speed2 = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("SPEED.NAME")).setLore(this.getInstance().getConfig().getStringList("SPEED.LORE")).toItemStack();
        ItemStack fire2 = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("FIRE.NAME")).setLore(this.getInstance().getConfig().getStringList("FIRE.LORE")).toItemStack();
        ItemStack str2 = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("STRENGTH.NAME")).setLore(this.getInstance().getConfig().getStringList("STRENGTH.LORE")).toItemStack();
        ItemStack jump2 = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("JUMP.NAME")).setLore(this.getInstance().getConfig().getStringList("JUMP.LORE")).toItemStack();
        ItemStack haste2 = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("HASTE.NAME")).setLore(this.getInstance().getConfig().getStringList("HASTE.LORE")).toItemStack();
        ItemStack water2 = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("WATERBREATHING.NAME")).setLore(this.getInstance().getConfig().getStringList("WATERBREATHING.LORE")).toItemStack();
        ItemStack vision2 = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("NIGHTVISION.NAME")).setLore(this.getInstance().getConfig().getStringList("NIGHTVISION.LORE")).toItemStack();
        ItemStack feast2 = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("IMPLANTS.NAME")).setLore(this.getInstance().getConfig().getStringList("IMPLANTS.LORE")).toItemStack();
        ItemStack smelt2 = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("SMELT.NAME")).setLore(this.getInstance().getConfig().getStringList("SMELT.LORE")).toItemStack();
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        if (e.getInventory().equals(CustomEnchantHandler.gui)) {
            if (e.isLeftClick() || e.isRightClick() || e.isShiftClick() || e.getClick().isKeyboardClick() || e.getAction().equals(InventoryAction.HOTBAR_SWAP) || e.getAction().equals(InventoryAction.DROP_ALL_SLOT) || e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) || e.getAction().equals(InventoryAction.CLONE_STACK)) {
                e.setCancelled(true);
            }
            if ((e.getClick().isRightClick() || e.getClick().isLeftClick()) && item != null) {
                if (item.equals(speed)) {
                    if (p.getLevel() >= this.getInstance().getConfig().getInt("ENCHANTS.SPEED.LEVEL")) {
                        p.setLevel(p.getLevel() - this.getInstance().getConfig().getInt("ENCHANTS.SPEED.LEVEL"));
                        p.getInventory().addItem(speed2);
                        p.sendMessage(Language.CUSTOM_ENCHANTS_PURCHASE_SUCCESS.toString());
                    } else {
                        p.sendMessage(Language.CUSTOM_ENCHANTS_NOT_ENOUGH_MONEY.toString());
                    }
                }
                if (item.equals(fire)) {
                    if (p.getLevel() >= this.getInstance().getConfig().getInt("ENCHANTS.FIRE.LEVEL")) {
                        p.setLevel(p.getLevel() - this.getInstance().getConfig().getInt("ENCHANTS.FIRE.LEVEL"));
                        p.getInventory().addItem(fire2);
                        p.sendMessage(Language.CUSTOM_ENCHANTS_PURCHASE_SUCCESS.toString());
                    } else {
                        p.sendMessage(Language.CUSTOM_ENCHANTS_NOT_ENOUGH_MONEY.toString());
                    }
                }
                if (item.equals(str)) {
                    if (p.getLevel() >= this.getInstance().getConfig().getInt("ENCHANTS.STRENGTH.LEVEL")) {
                        p.setLevel(p.getLevel() - this.getInstance().getConfig().getInt("ENCHANTS.STRENGTH.LEVEL"));
                        p.getInventory().addItem(str2);
                        p.sendMessage(Language.CUSTOM_ENCHANTS_PURCHASE_SUCCESS.toString());
                    } else {
                        p.sendMessage(Language.CUSTOM_ENCHANTS_NOT_ENOUGH_MONEY.toString());
                    }
                }
                if (item.equals(jump)) {
                    if (p.getLevel() >= this.getInstance().getConfig().getInt("ENCHANTS.JUMP.LEVEL")) {
                        p.setLevel(p.getLevel() - this.getInstance().getConfig().getInt("ENCHANTS.JUMP.LEVEL"));
                        p.getInventory().addItem(jump2);
                        p.sendMessage(Language.CUSTOM_ENCHANTS_PURCHASE_SUCCESS.toString());
                    } else {
                        p.sendMessage(Language.CUSTOM_ENCHANTS_NOT_ENOUGH_MONEY.toString());
                    }
                }
                if (item.equals(haste)) {
                    if (p.getLevel() >= this.getInstance().getConfig().getInt("ENCHANTS.HASTE.LEVEL")) {
                        p.setLevel(p.getLevel() - this.getInstance().getConfig().getInt("ENCHANTS.HASTE.LEVEL"));
                        p.getInventory().addItem(haste2);
                        p.sendMessage(Language.CUSTOM_ENCHANTS_PURCHASE_SUCCESS.toString());
                    } else {
                        p.sendMessage(Language.CUSTOM_ENCHANTS_NOT_ENOUGH_MONEY.toString());
                    }
                }
                if (item.equals(water)) {
                    if (p.getLevel() >= this.getInstance().getConfig().getInt("ENCHANTS.WATERBREATHING.LEVEL")) {
                        p.setLevel(p.getLevel() - this.getInstance().getConfig().getInt("ENCHANTS.WATERBREATHING.LEVEL"));
                        p.getInventory().addItem(water2);
                        p.sendMessage(Language.CUSTOM_ENCHANTS_PURCHASE_SUCCESS.toString());
                    } else {
                        p.sendMessage(Language.CUSTOM_ENCHANTS_NOT_ENOUGH_MONEY.toString());
                    }
                }
                if (item.equals(vision)) {
                    if (p.getLevel() >= this.getInstance().getConfig().getInt("ENCHANTS.NIGHTVISION.LEVEL")) {
                        p.setLevel(p.getLevel() - this.getInstance().getConfig().getInt("ENCHANTS.NIGHTVISION.LEVEL"));
                        p.getInventory().addItem(vision2);
                        p.sendMessage(Language.CUSTOM_ENCHANTS_PURCHASE_SUCCESS.toString());
                    } else {
                        p.sendMessage(Language.CUSTOM_ENCHANTS_NOT_ENOUGH_MONEY.toString());
                    }
                }
                if (item.equals(feast)) {
                    if (p.getLevel() >= this.getInstance().getConfig().getInt("ENCHANTS.IMPLANTS.LEVEL")) {
                        p.setLevel(p.getLevel() - this.getInstance().getConfig().getInt("ENCHANTS.IMPLANTS.LEVEL"));
                        p.getInventory().addItem(feast2);
                        p.sendMessage(Language.CUSTOM_ENCHANTS_PURCHASE_SUCCESS.toString());
                    } else {
                        p.sendMessage(Language.CUSTOM_ENCHANTS_NOT_ENOUGH_MONEY.toString());
                    }
                }
                if (item.equals(smelt)) {
                    if (p.getLevel() >= this.getInstance().getConfig().getInt("ENCHANTS.SMELT.LEVEL")) {
                        p.setLevel(p.getLevel() - this.getInstance().getConfig().getInt("ENCHANTS.SMELT.LEVEL"));
                        p.getInventory().addItem(smelt2);
                        p.sendMessage(Language.CUSTOM_ENCHANTS_PURCHASE_SUCCESS.toString());
                    } else {
                        p.sendMessage(Language.CUSTOM_ENCHANTS_NOT_ENOUGH_MONEY.toString());
                    }
                }
            }
        }
    }
}

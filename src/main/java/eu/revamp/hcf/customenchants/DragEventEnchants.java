package eu.revamp.hcf.customenchants;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DragEventEnchants extends Handler implements Listener
{
    public DragEventEnchants(RevampHCF instance) {
        super(instance);
    }

    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }

    @EventHandler @SuppressWarnings("deprecation")
    public void onClick(InventoryClickEvent e) {
        ItemStack speed1 = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("SPEED.NAME")).setLore(this.getInstance().getConfig().getStringList("SPEED.LORE")).toItemStack();
        ItemStack fire1 = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("FIRE.NAME")).setLore(this.getInstance().getConfig().getStringList("FIRE.LORE")).toItemStack();
        ItemStack str1 = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("STRENGTH.NAME")).setLore(this.getInstance().getConfig().getStringList("STRENGTH.LORE")).toItemStack();
        ItemStack jump1 = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("JUMP.NAME")).setLore(this.getInstance().getConfig().getStringList("JUMP.LORE")).toItemStack();
        ItemStack haste1 = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("HASTE.NAME")).setLore(this.getInstance().getConfig().getStringList("HASTE.LORE")).toItemStack();
        ItemStack water1 = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("WATERBREATHING.NAME")).setLore(this.getInstance().getConfig().getStringList("WATERBREATHING.LORE")).toItemStack();
        ItemStack vision1 = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("NIGHTVISION.NAME")).setLore(this.getInstance().getConfig().getStringList("NIGHTVISION.LORE")).toItemStack();
        ItemStack feast1 = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("IMPLANTS.NAME")).setLore(this.getInstance().getConfig().getStringList("IMPLANTS.LORE")).toItemStack();
        ItemStack smelt1 = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("SMELT.NAME")).setLore(this.getInstance().getConfig().getStringList("SMELT.LORE")).toItemStack();
        Player p = (Player)e.getWhoClicked();
        if (e.getCurrentItem() != null) {
            Material item = e.getCurrentItem().getType();
            if (e.getAction().equals(InventoryAction.SWAP_WITH_CURSOR)) {
                if (e.getCursor().isSimilar(speed1) && (e.getClick().isLeftClick() || e.getClick().isRightClick())) {
                    if (item.equals(Material.DIAMOND_BOOTS) || item.equals(Material.LEATHER_BOOTS) || item.equals(Material.GOLD_BOOTS) || item.equals(Material.IRON_BOOTS) || item.equals(Material.CHAINMAIL_BOOTS)) {
                        ItemStack current = e.getCurrentItem();
                        ItemMeta meta = e.getCurrentItem().getItemMeta();
                        List<String> lore = new ArrayList<>();
                        if (!meta.hasLore()) {
                            lore.add(CC.translate("&cSpeed"));
                        }
                        else {
                            if (meta.getLore().contains(CC.translate("&cSpeed"))) {
                                e.getWhoClicked().sendMessage(Language.CUSTOM_ENCHANTS_HAS_ALREADY_ENCHANT.toString());
                                return;
                            } else {
                                lore.addAll(meta.getLore());
                                lore.add(CC.translate("&cSpeed"));
                            }
                        }
                        meta.setLore(lore);
                        current.setItemMeta(meta);
                        e.setCancelled(true);
                        e.setCursor(new ItemStack(Material.AIR));
                        e.getWhoClicked().sendMessage(Language.CUSTOM_ENCHANTS_ENCHANTMENT_SUCCESS.toString());
                    }
                    else {
                        p.sendMessage(CC.translate("&c&l(!) &cThis Enchantment Is Only For The &c&nBoots!"));
                    }
                }
                if (e.getCursor().isSimilar(jump1) && (e.getClick().isLeftClick() || e.getClick().isRightClick())) {
                    if (item.equals(Material.DIAMOND_BOOTS) || item.equals(Material.LEATHER_BOOTS) || item.equals(Material.GOLD_BOOTS) || item.equals(Material.IRON_BOOTS) || item.equals(Material.CHAINMAIL_BOOTS)) {
                        ItemStack current = e.getCurrentItem();
                        ItemMeta meta = e.getCurrentItem().getItemMeta();
                        List<String> lore = new ArrayList<>();
                        if (!meta.hasLore()) {
                            lore.add(CC.translate("&cJump"));
                        }
                        else {
                            if (meta.getLore().contains(CC.translate("&cJump"))) {
                                e.getWhoClicked().sendMessage(Language.CUSTOM_ENCHANTS_HAS_ALREADY_ENCHANT.toString());
                                return;
                            } else {
                                lore.addAll(meta.getLore());
                                lore.add(CC.translate("&cJump"));
                            }
                        }
                        meta.setLore(lore);
                        current.setItemMeta(meta);
                        e.setCancelled(true);
                        e.setCursor(new ItemStack(Material.AIR));
                        e.getWhoClicked().sendMessage(Language.CUSTOM_ENCHANTS_ENCHANTMENT_SUCCESS.toString());
                    }
                    else {
                        p.sendMessage(CC.translate("&c&l(!) &cThis Enchantment Is Only For The &c&nBoots!"));
                    }
                }
                if (e.getCursor().isSimilar(fire1) && (e.getClick().isLeftClick() || e.getClick().isRightClick())) {
                    if (item.equals(Material.DIAMOND_LEGGINGS) || item.equals(Material.LEATHER_LEGGINGS) || item.equals(Material.GOLD_LEGGINGS) || item.equals(Material.IRON_LEGGINGS) || item.equals(Material.CHAINMAIL_LEGGINGS)) {
                        ItemStack current = e.getCurrentItem();
                        ItemMeta meta = e.getCurrentItem().getItemMeta();
                        List<String> lore = new ArrayList<>();
                        if (!meta.hasLore()) {
                            lore.add(CC.translate("&cFire Resistance"));
                        }
                        else {
                            if (meta.getLore().contains(CC.translate("&cFire Resistance"))) {
                                e.getWhoClicked().sendMessage(Language.CUSTOM_ENCHANTS_HAS_ALREADY_ENCHANT.toString());
                                return;
                            } else {
                                lore.addAll(meta.getLore());
                                lore.add(CC.translate("&cFire Resistance"));
                            }
                        }
                        meta.setLore(lore);
                        current.setItemMeta(meta);
                        e.setCancelled(true);
                        e.setCursor(new ItemStack(Material.AIR));
                        e.getWhoClicked().sendMessage(Language.CUSTOM_ENCHANTS_ENCHANTMENT_SUCCESS.toString());
                    }
                    else {
                        p.sendMessage(CC.translate("&c&l(!) &cThis Enchantment Is Only For The &c&nLeggings!"));
                    }
                }
                if (e.getCursor().isSimilar(str1) && (e.getClick().isLeftClick() || e.getClick().isRightClick())) {
                    if (item.equals(Material.DIAMOND_CHESTPLATE) || item.equals(Material.LEATHER_CHESTPLATE) || item.equals(Material.GOLD_CHESTPLATE) || item.equals(Material.IRON_CHESTPLATE) || item.equals(Material.CHAINMAIL_CHESTPLATE)) {
                        ItemStack current = e.getCurrentItem();
                        ItemMeta meta = e.getCurrentItem().getItemMeta();
                        List<String> lore = new ArrayList<>();
                        if (!meta.hasLore()) {
                            lore.add(CC.translate("&cStrength"));
                        }
                        else {
                            if (meta.getLore().contains(CC.translate("&cStrength"))) {
                                e.getWhoClicked().sendMessage(Language.CUSTOM_ENCHANTS_HAS_ALREADY_ENCHANT.toString());
                                return;
                            }
                            else {
                                lore.addAll(meta.getLore());
                                lore.add(CC.translate("&cStrength"));
                            }
                        }
                        meta.setLore(lore);
                        current.setItemMeta(meta);
                        e.setCancelled(true);
                        e.setCursor(new ItemStack(Material.AIR));
                        e.getWhoClicked().sendMessage(Language.CUSTOM_ENCHANTS_ENCHANTMENT_SUCCESS.toString());
                    }
                    else {
                        p.sendMessage(CC.translate("&c&l(!) &cThis Enchantment Is Only For The &c&nChestPlate!"));
                    }
                }
                if (e.getCursor().isSimilar(water1) && (e.getClick().isLeftClick() || e.getClick().isRightClick())) {
                    if (item.equals(Material.DIAMOND_HELMET) || item.equals(Material.LEATHER_HELMET) || item.equals(Material.GOLD_HELMET) || item.equals(Material.IRON_HELMET) || item.equals(Material.CHAINMAIL_HELMET)) {
                        ItemStack current = e.getCurrentItem();
                        ItemMeta meta = e.getCurrentItem().getItemMeta();
                        List<String> lore = new ArrayList<>();
                        if (!meta.hasLore()) {
                            lore.add(CC.translate("&cWater Breathing"));
                        }
                        else {
                            if (meta.getLore().contains(CC.translate("&cWater Breathing"))) {
                                e.getWhoClicked().sendMessage(Language.CUSTOM_ENCHANTS_HAS_ALREADY_ENCHANT.toString());
                                return;
                            } else {
                                lore.addAll(meta.getLore());
                                lore.add(CC.translate("&cWater Breathing"));
                            }
                        }
                        meta.setLore(lore);
                        current.setItemMeta(meta);
                        e.setCancelled(true);
                        e.setCursor(new ItemStack(Material.AIR));
                        e.getWhoClicked().sendMessage(Language.CUSTOM_ENCHANTS_ENCHANTMENT_SUCCESS.toString());
                    }
                    else {
                        p.sendMessage(CC.translate("&c&l(!) &cThis Enchantment Is Only For The &c&nHelmet!"));
                    }
                }
                if (e.getCursor().isSimilar(vision1) && (e.getClick().isLeftClick() || e.getClick().isRightClick())) {
                    if (item.equals(Material.DIAMOND_HELMET) || item.equals(Material.LEATHER_HELMET) || item.equals(Material.GOLD_HELMET) || item.equals(Material.IRON_HELMET) || item.equals(Material.CHAINMAIL_HELMET)) {
                        ItemStack current = e.getCurrentItem();
                        ItemMeta meta = e.getCurrentItem().getItemMeta();
                        List<String> lore = new ArrayList<>();
                        if (!meta.hasLore()) {
                            lore.add(CC.translate("&cNight Vision"));
                        }
                        else {
                            if (meta.getLore().contains(CC.translate("&cNight Vision"))) {
                                e.getWhoClicked().sendMessage(Language.CUSTOM_ENCHANTS_HAS_ALREADY_ENCHANT.toString());
                                return;
                            } else {
                                lore.addAll(meta.getLore());
                                lore.add(CC.translate("&cNight Vision"));
                            }
                        }
                        meta.setLore(lore);
                        current.setItemMeta(meta);
                        e.setCancelled(true);
                        e.setCursor(new ItemStack(Material.AIR));
                        e.getWhoClicked().sendMessage(Language.CUSTOM_ENCHANTS_ENCHANTMENT_SUCCESS.toString());
                    }
                    else {
                        p.sendMessage(CC.translate("&c&l(!) &cThis Enchantment Is Only For The &c&nHelmet!"));
                    }
                }
                if (e.getCursor().isSimilar(feast1) && (e.getClick().isLeftClick() || e.getClick().isRightClick())) {
                    if (item.equals(Material.DIAMOND_HELMET) || item.equals(Material.LEATHER_HELMET) || item.equals(Material.GOLD_HELMET) || item.equals(Material.IRON_HELMET) || item.equals(Material.CHAINMAIL_HELMET)) {
                        ItemStack current = e.getCurrentItem();
                        ItemMeta meta = e.getCurrentItem().getItemMeta();
                        List<String> lore = new ArrayList<>();
                        if (!meta.hasLore()) {
                            lore.add(CC.translate("&cImplants"));
                        }
                        else {
                            if (meta.getLore().contains(CC.translate("&cImplants"))) {
                                e.getWhoClicked().sendMessage(Language.CUSTOM_ENCHANTS_HAS_ALREADY_ENCHANT.toString());
                                return;
                            } else {
                                lore.addAll(meta.getLore());
                                lore.add(CC.translate("&cImplants"));
                            }
                        }
                        meta.setLore(lore);
                        current.setItemMeta(meta);
                        e.setCancelled(true);
                        e.setCursor(new ItemStack(Material.AIR));
                        e.getWhoClicked().sendMessage(Language.CUSTOM_ENCHANTS_ENCHANTMENT_SUCCESS.toString());
                    }
                    else {
                        p.sendMessage(CC.translate("&c&l(!) &cThis Enchantment Is Only For The &c&nHelmet!"));
                    }
                }
                if (e.getCursor().isSimilar(haste1) && (e.getClick().isLeftClick() || e.getClick().isRightClick())) {
                    if (item.equals(Material.DIAMOND_AXE) || item.equals(Material.DIAMOND_PICKAXE) || item.equals(Material.DIAMOND_SPADE) || item.equals(Material.IRON_AXE) || item.equals(Material.IRON_PICKAXE) || item.equals(Material.IRON_SPADE) || item.equals(Material.GOLD_AXE) || item.equals(Material.GOLD_PICKAXE) || item.equals(Material.GOLD_SPADE) || item.equals(Material.STONE_AXE) || item.equals(Material.STONE_PICKAXE) || item.equals(Material.STONE_SPADE) || item.equals(Material.WOOD_AXE) || item.equals(Material.WOOD_PICKAXE) || item.equals(Material.WOOD_SPADE)) {
                        ItemStack current = e.getCurrentItem();
                        ItemMeta meta = e.getCurrentItem().getItemMeta();
                        List<String> lore = new ArrayList<>();
                        if (!meta.hasLore()) {
                            lore.add(CC.translate("&cHaste"));
                        }
                        else {
                            if (meta.getLore().contains(CC.translate("&cHaste"))) {
                                e.getWhoClicked().sendMessage(Language.CUSTOM_ENCHANTS_HAS_ALREADY_ENCHANT.toString());
                                return;
                            }
                            else {
                                lore.addAll(meta.getLore());
                                lore.add(CC.translate("&cHaste"));
                            }
                        }
                        meta.setLore(lore);
                        current.setItemMeta(meta);
                        e.setCancelled(true);
                        e.setCursor(new ItemStack(Material.AIR));
                        e.getWhoClicked().sendMessage(Language.CUSTOM_ENCHANTS_ENCHANTMENT_SUCCESS.toString());
                    }
                    else {
                        p.sendMessage(CC.translate("&c&l(!) &cThis Enchantment Is Only For &c&nTools!"));
                    }
                }
                if (e.getCursor().isSimilar(smelt1) && (e.getClick().isLeftClick() || e.getClick().isRightClick())) {
                    if (item.equals(Material.DIAMOND_AXE) || item.equals(Material.DIAMOND_PICKAXE) || item.equals(Material.DIAMOND_SPADE) || item.equals(Material.IRON_AXE) || item.equals(Material.IRON_PICKAXE) || item.equals(Material.IRON_SPADE) || item.equals(Material.GOLD_AXE) || item.equals(Material.GOLD_PICKAXE) || item.equals(Material.GOLD_SPADE) || item.equals(Material.STONE_AXE) || item.equals(Material.STONE_PICKAXE) || item.equals(Material.STONE_SPADE) || item.equals(Material.WOOD_AXE) || item.equals(Material.WOOD_PICKAXE) || item.equals(Material.WOOD_SPADE)) {
                        ItemStack current = e.getCurrentItem();
                        ItemMeta meta = e.getCurrentItem().getItemMeta();
                        List<String> lore = new ArrayList<>();
                        if (!meta.hasLore()) {
                            lore.add(CC.translate("&cAuto Smelt"));
                        }
                        else {
                            if (meta.getLore().contains(CC.translate("&cAuto Smelt"))) {
                                e.getWhoClicked().sendMessage(Language.CUSTOM_ENCHANTS_HAS_ALREADY_ENCHANT.toString());
                                return;
                            } else {
                                lore.addAll(meta.getLore());
                                lore.add(CC.translate("&cAuto Smelt"));
                            }
                        }
                        meta.setLore(lore);
                        current.setItemMeta(meta);
                        e.setCancelled(true);
                        e.setCursor(new ItemStack(Material.AIR));
                        e.getWhoClicked().sendMessage(Language.CUSTOM_ENCHANTS_ENCHANTMENT_SUCCESS.toString());
                    }
                    else {
                        p.sendMessage(CC.translate("&c&l(!) &cThis Enchantment Is Only For &c&nTools!"));
                    }
                }
            }
        }
    }
}
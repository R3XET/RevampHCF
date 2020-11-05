package eu.revamp.hcf.handlers.signs;

import eu.revamp.hcf.RevampHCF;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;
@Getter @Setter
public class KitSignHandler extends Handler implements Listener {

    private ArrayList<String> cooldown;
    private ItemStack healPot;
    private ItemStack speedPot;
    private ItemStack fastPot;
    
    public KitSignHandler(RevampHCF plugin) {
        super(plugin);
        this.cooldown = new ArrayList<>();
        this.healPot = new ItemStack(Material.POTION, 1, (short)16421);
        this.speedPot = new ItemStack(Material.POTION, 1, (short)8226);
        this.fastPot = new ItemStack(Material.POTION, 1, (short)8259);
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    /*
    public void giveDiamondKit(Player p) {
        final ItemStack diamondhelmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        diamondhelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        diamondhelmet.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack diamondplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        diamondplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        diamondplate.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack diamondleggs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        diamondleggs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        diamondleggs.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack diamondboots = new ItemStack(Material.DIAMOND_BOOTS, 1);
        diamondboots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        diamondboots.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        diamondboots.addEnchantment(Enchantment.PROTECTION_FALL, RevampHCF.getInstance().getConfig().getInt("KITS.FEATHER_FALLING"));
        final ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, RevampHCF.getInstance().getConfig().getInt("KITS.SHARPNESS"));
        sword.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final PlayerInventory pi = p.getInventory();
        pi.setItem(0, sword);
        pi.setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
        pi.setItem(2, getSpeedPot());
        pi.setItem(3, getHealPot());
        pi.setItem(4, getHealPot());
        pi.setItem(5, getHealPot());
        pi.setItem(6, getHealPot());
        pi.setItem(7, getHealPot());
        pi.setItem(8, getHealPot());
        pi.setItem(9, getHealPot());
        pi.setItem(10, getHealPot());
        pi.setItem(11, getHealPot());
        pi.setItem(12, getHealPot());
        pi.setItem(13, getHealPot());
        pi.setItem(14, getHealPot());
        pi.setItem(15, getHealPot());
        pi.setItem(16, getSpeedPot());
        pi.setItem(17, getSpeedPot());
        pi.setItem(18, getHealPot());
        pi.setItem(19, getHealPot());
        pi.setItem(20, getHealPot());
        pi.setItem(21, getHealPot());
        pi.setItem(22, getHealPot());
        pi.setItem(23, getHealPot());
        pi.setItem(24, getHealPot());
        pi.setItem(25, getSpeedPot());
        pi.setItem(26, getSpeedPot());
        pi.setItem(27, getHealPot());
        pi.setItem(28, getHealPot());
        pi.setItem(29, getHealPot());
        pi.setItem(30, getHealPot());
        pi.setItem(31, getHealPot());
        pi.setItem(32, getHealPot());
        pi.setItem(33, getHealPot());
        pi.setItem(34, getSpeedPot());
        pi.setItem(35, getSpeedPot());
        pi.setHelmet(diamondhelmet);
        pi.setChestplate(diamondplate);
        pi.setLeggings(diamondleggs);
        pi.setBoots(diamondboots);
        p.updateInventory();
        p.sendMessage(CC.translate(" &7» &a&lEquipped PvP Kit"));
    }
    
    public void giveRogueKit(Player p) {
        final ItemStack diamondhelmet = new ItemStack(Material.CHAINMAIL_HELMET, 1);
        diamondhelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        diamondhelmet.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack diamondplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
        diamondplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        diamondplate.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack diamondleggs = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
        diamondleggs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        diamondleggs.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack diamondboots = new ItemStack(Material.CHAINMAIL_BOOTS, 1);
        diamondboots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        diamondboots.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        diamondboots.addEnchantment(Enchantment.PROTECTION_FALL, RevampHCF.getInstance().getConfig().getInt("KITS.FEATHER_FALLING"));
        final ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, RevampHCF.getInstance().getConfig().getInt("KITS.SHARPNESS"));
        sword.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack gold = new ItemStack(Material.GOLD_SWORD, 1);
        final PlayerInventory pi = p.getInventory();
        pi.setItem(0, sword);
        pi.setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
        pi.setItem(2, gold);
        pi.setItem(3, getHealPot());
        pi.setItem(4, getHealPot());
        pi.setItem(5, getHealPot());
        pi.setItem(6, getHealPot());
        pi.setItem(7, new ItemStack(Material.SUGAR, 16));
        pi.setItem(8, new ItemStack(Material.FEATHER, 16));
        pi.setItem(9, gold);
        pi.setItem(10, gold);
        pi.setItem(11, getHealPot());
        pi.setItem(12, getHealPot());
        pi.setItem(13, getHealPot());
        pi.setItem(14, getHealPot());
        pi.setItem(15, getHealPot());
        pi.setItem(16, getHealPot());
        pi.setItem(17, getHealPot());
        pi.setItem(18, gold);
        pi.setItem(19, gold);
        pi.setItem(20, getHealPot());
        pi.setItem(21, getHealPot());
        pi.setItem(22, getHealPot());
        pi.setItem(23, getHealPot());
        pi.setItem(24, getHealPot());
        pi.setItem(25, getHealPot());
        pi.setItem(26, getHealPot());
        pi.setItem(27, gold);
        pi.setItem(28, gold);
        pi.setItem(29, getHealPot());
        pi.setItem(30, getHealPot());
        pi.setItem(31, getHealPot());
        pi.setItem(32, getHealPot());
        pi.setItem(33, getHealPot());
        pi.setItem(34, getHealPot());
        pi.setItem(35, getHealPot());
        pi.setHelmet(diamondhelmet);
        pi.setChestplate(diamondplate);
        pi.setLeggings(diamondleggs);
        pi.setBoots(diamondboots);
        p.updateInventory();
        p.sendMessage(CC.translate(" &7» &a&lEquipped Rogue Kit"));
    }
    
    public void giveBardKit(Player p) {
        final ItemStack goldhelmet = new ItemStack(Material.GOLD_HELMET, 1);
        goldhelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        goldhelmet.addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack goldplate = new ItemStack(Material.GOLD_CHESTPLATE, 1);
        goldplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        goldplate.addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack goldleggs = new ItemStack(Material.GOLD_LEGGINGS, 1);
        goldleggs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        goldleggs.addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack goldboots = new ItemStack(Material.GOLD_BOOTS, 1);
        goldboots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        goldboots.addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        goldboots.addEnchantment(Enchantment.PROTECTION_FALL, RevampHCF.getInstance().getConfig().getInt("KITS.FEATHER_FALLING"));
        final ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, RevampHCF.getInstance().getConfig().getInt("KITS.SHARPNESS"));
        sword.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final PlayerInventory pi = p.getInventory();
        pi.setItem(0, sword);
        pi.setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
        pi.setItem(2, new ItemStack(Material.BLAZE_POWDER, 32));
        pi.setItem(3, new ItemStack(Material.SUGAR, 32));
        pi.setItem(4, getHealPot());
        pi.setItem(5, getHealPot());
        pi.setItem(6, getHealPot());
        pi.setItem(7, getHealPot());
        pi.setItem(8, getHealPot());
        pi.setItem(9, new ItemStack(Material.IRON_INGOT, 32));
        pi.setItem(10, new ItemStack(Material.GHAST_TEAR, 32));
        pi.setItem(11, getHealPot());
        pi.setItem(12, getHealPot());
        pi.setItem(13, getHealPot());
        pi.setItem(14, getHealPot());
        pi.setItem(15, getHealPot());
        pi.setItem(16, getHealPot());
        pi.setItem(17, getHealPot());
        pi.setItem(18, new ItemStack(Material.FEATHER, 32));
        pi.setItem(19, new ItemStack(Material.MAGMA_CREAM, 32));
        pi.setItem(20, getHealPot());
        pi.setItem(21, getHealPot());
        pi.setItem(22, getHealPot());
        pi.setItem(23, getHealPot());
        pi.setItem(24, getHealPot());
        pi.setItem(25, getHealPot());
        pi.setItem(26, getHealPot());
        pi.setItem(27, getHealPot());
        pi.setItem(28, getHealPot());
        pi.setItem(29, getHealPot());
        pi.setItem(30, getHealPot());
        pi.setItem(31, getHealPot());
        pi.setItem(32, getHealPot());
        pi.setItem(33, getHealPot());
        pi.setItem(34, getHealPot());
        pi.setItem(35, getHealPot());
        pi.setHelmet(goldhelmet);
        pi.setChestplate(goldplate);
        pi.setLeggings(goldleggs);
        pi.setBoots(goldboots);
        p.updateInventory();
        p.sendMessage(CC.translate(" &7» &a&lEquipped Bard Kit"));
    }
    
    public void giveArcherKit(Player p) {
        final ItemStack lhelmet = new ItemStack(Material.LEATHER_HELMET, 1);
        lhelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        lhelmet.addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack lplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        lplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        lplate.addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack lleggs = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        lleggs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        lleggs.addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack lboots = new ItemStack(Material.LEATHER_BOOTS, 1);
        lboots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        lboots.addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        lboots.addEnchantment(Enchantment.PROTECTION_FALL, RevampHCF.getInstance().getConfig().getInt("KITS.FEATHER_FALLING"));
        final ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, RevampHCF.getInstance().getConfig().getInt("KITS.SHARPNESS"));
        sword.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, RevampHCF.getInstance().getConfig().getInt("KITS.POWER"));
        bow.addEnchantment(Enchantment.ARROW_FIRE, RevampHCF.getInstance().getConfig().getInt("FLAME"));
        bow.addEnchantment(Enchantment.ARROW_INFINITE, RevampHCF.getInstance().getConfig().getInt("KITS.INFINITE"));
        bow.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final PlayerInventory pi = p.getInventory();
        pi.setItem(0, sword);
        pi.setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
        pi.setItem(2, bow);
        pi.setItem(3, getHealPot());
        pi.setItem(4, getHealPot());
        pi.setItem(5, getHealPot());
        pi.setItem(6, getHealPot());
        pi.setItem(7, new ItemStack(Material.FEATHER, 16));
        pi.setItem(7, new ItemStack(Material.SUGAR, 16));
        pi.setItem(8, new ItemStack(Material.FEATHER, 16));
        pi.setItem(9, new ItemStack(Material.ARROW, 1));
        pi.setItem(10, getHealPot());
        pi.setItem(11, getHealPot());
        pi.setItem(12, getHealPot());
        pi.setItem(13, getHealPot());
        pi.setItem(14, getHealPot());
        pi.setItem(15, getHealPot());
        pi.setItem(16, getHealPot());
        pi.setItem(17, getHealPot());
        pi.setItem(18, getHealPot());
        pi.setItem(19, getHealPot());
        pi.setItem(20, getHealPot());
        pi.setItem(21, getHealPot());
        pi.setItem(22, getHealPot());
        pi.setItem(23, getHealPot());
        pi.setItem(24, getHealPot());
        pi.setItem(25, getHealPot());
        pi.setItem(26, getHealPot());
        pi.setItem(27, getHealPot());
        pi.setItem(28, getHealPot());
        pi.setItem(29, getHealPot());
        pi.setItem(30, getHealPot());
        pi.setItem(31, getHealPot());
        pi.setItem(32, getHealPot());
        pi.setItem(33, getHealPot());
        pi.setItem(34, getHealPot());
        pi.setItem(35, getHealPot());
        pi.setHelmet(lhelmet);
        pi.setChestplate(lplate);
        pi.setLeggings(lleggs);
        pi.setBoots(lboots);
        p.updateInventory();
        p.sendMessage(CC.translate(" &7» &a&lEquipped Archer Kit"));
    }
    
    public void giveBuilderKit(Player p) {
        final ItemStack diamondhelmet = new ItemStack(Material.IRON_HELMET, 1);
        diamondhelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        diamondhelmet.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack diamondplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
        diamondplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        diamondplate.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack diamondleggs = new ItemStack(Material.IRON_LEGGINGS, 1);
        diamondleggs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        diamondleggs.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack diamondboots = new ItemStack(Material.IRON_BOOTS, 1);
        diamondboots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        diamondboots.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        diamondboots.addEnchantment(Enchantment.PROTECTION_FALL, RevampHCF.getInstance().getConfig().getInt("KITS.FEATHER_FALLING"));
        final ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, RevampHCF.getInstance().getConfig().getInt("KITS.SHARPNESS"));
        sword.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        pick.addUnsafeEnchantment(Enchantment.DIG_SPEED, RevampHCF.getInstance().getConfig().getInt("KITS.EFFICIENCY"));
        pick.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack showel = new ItemStack(Material.DIAMOND_SPADE, 1);
        showel.addUnsafeEnchantment(Enchantment.DIG_SPEED, RevampHCF.getInstance().getConfig().getInt("KITS.EFFICIENCY"));
        showel.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack axe = new ItemStack(Material.DIAMOND_AXE, 1);
        axe.addUnsafeEnchantment(Enchantment.DIG_SPEED, RevampHCF.getInstance().getConfig().getInt("KITS.EFFICIENCY"));
        axe.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final PlayerInventory pi = p.getInventory();
        pi.setItem(0, sword);
        pi.setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
        pi.setItem(2, pick);
        pi.setItem(3, axe);
        pi.setItem(4, showel);
        pi.setItem(5, getHealPot());
        pi.setItem(6, getHealPot());
        pi.setItem(7, getHealPot());
        pi.setItem(8, getSpeedPot());
        pi.setItem(9, getSpeedPot());
        pi.setItem(10, new ItemStack(Material.STONE, 64));
        pi.setItem(11, new ItemStack(Material.COBBLESTONE, 64));
        pi.setItem(12, new ItemStack(Material.SMOOTH_BRICK, 64));
        pi.setItem(13, new ItemStack(Material.QUARTZ_BLOCK, 64));
        pi.setItem(14, new ItemStack(Material.DIRT, 64));
        pi.setItem(15, new ItemStack(Material.LOG, 64));
        pi.setItem(16, new ItemStack(Material.LOG, 64));
        pi.setItem(17, new ItemStack(Material.WATER_BUCKET, 2));
        pi.setItem(18, getSpeedPot());
        pi.setItem(19, new ItemStack(Material.GLASS, 64));
        pi.setItem(20, new ItemStack(Material.PISTON_STICKY_BASE, 64));
        pi.setItem(21, new ItemStack(Material.PISTON_BASE, 64));
        pi.setItem(22, new ItemStack(Material.REDSTONE_COMPARATOR, 64));
        pi.setItem(23, new ItemStack(Material.DIODE, 64));
        pi.setItem(24, new ItemStack(Material.HOPPER, 64));
        pi.setItem(25, new ItemStack(Material.REDSTONE_BLOCK, 64));
        pi.setItem(26, new ItemStack(Material.STRING, 64));
        pi.setItem(27, getSpeedPot());
        pi.setItem(28, new ItemStack(Material.TRAPPED_CHEST, 64));
        pi.setItem(29, new ItemStack(Material.CHEST, 64));
        pi.setItem(30, new ItemStack(Material.FENCE_GATE, 64));
        pi.setItem(31, new ItemStack(Material.WOOD_PLATE, 64));
        pi.setItem(32, getHealPot());
        pi.setItem(33, getHealPot());
        pi.setItem(34, getHealPot());
        pi.setItem(35, getHealPot());
        pi.setHelmet(diamondhelmet);
        pi.setChestplate(diamondplate);
        pi.setLeggings(diamondleggs);
        pi.setBoots(diamondboots);
        p.updateInventory();
        p.sendMessage(CC.translate(" &7» &a&lEquipped Builder Kit"));
    }
    
    public void giveRangerKit(Player p) {
        final ItemStack diamondhelmet = new ItemStack(Material.IRON_HELMET, 1);
        diamondhelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        diamondhelmet.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack diamondplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        diamondplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        diamondplate.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack diamondleggs = new ItemStack(Material.IRON_LEGGINGS, 1);
        diamondleggs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        diamondleggs.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack diamondboots = new ItemStack(Material.DIAMOND_BOOTS, 1);
        diamondboots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION"));
        diamondboots.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        diamondboots.addEnchantment(Enchantment.PROTECTION_FALL, RevampHCF.getInstance().getConfig().getInt("KITS.FEATHER_FALLING"));
        final ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, RevampHCF.getInstance().getConfig().getInt("KITS.SHARPNESS"));
        sword.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final PlayerInventory pi = p.getInventory();
        pi.setItem(0, sword);
        pi.setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
        pi.setItem(2, getHealPot());
        pi.setItem(3, getHealPot());
        pi.setItem(4, getHealPot());
        pi.setItem(5, getHealPot());
        pi.setItem(6, getHealPot());
        pi.setItem(7, new ItemStack(Material.MAGMA_CREAM, 32));
        pi.setItem(8, new ItemStack(Material.QUARTZ, 32));
        pi.setItem(9, getHealPot());
        pi.setItem(10, getHealPot());
        pi.setItem(11, getHealPot());
        pi.setItem(12, getHealPot());
        pi.setItem(13, getHealPot());
        pi.setItem(14, getHealPot());
        pi.setItem(15, getHealPot());
        pi.setItem(16, getHealPot());
        pi.setItem(17, getHealPot());
        pi.setItem(18, getHealPot());
        pi.setItem(19, getHealPot());
        pi.setItem(20, getHealPot());
        pi.setItem(21, getHealPot());
        pi.setItem(22, getHealPot());
        pi.setItem(23, getHealPot());
        pi.setItem(24, getHealPot());
        pi.setItem(25, getHealPot());
        pi.setItem(26, getHealPot());
        pi.setItem(27, getHealPot());
        pi.setItem(28, getHealPot());
        pi.setItem(29, getHealPot());
        pi.setItem(30, getHealPot());
        pi.setItem(31, getHealPot());
        pi.setItem(32, getHealPot());
        pi.setItem(33, getHealPot());
        pi.setItem(34, getHealPot());
        pi.setItem(35, getHealPot());
        pi.setHelmet(diamondhelmet);
        pi.setChestplate(diamondplate);
        pi.setLeggings(diamondleggs);
        pi.setBoots(diamondboots);
        p.updateInventory();
        p.sendMessage(CC.translate(" &7» &a&lEquipped Ranger Kit"));
    }
    
    public void giveScreamKit(Player p) {
        final ItemStack lhelmet = new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(org.bukkit.Color.fromRGB(3361970)).addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION")).addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING")).toItemStack();
        final ItemStack lplate = new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(org.bukkit.Color.fromRGB(3361970)).addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION")).addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING")).toItemStack();
        final ItemStack lleggs = new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(org.bukkit.Color.fromRGB(3361970)).addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION")).addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING")).toItemStack();
        final ItemStack lboots = new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(org.bukkit.Color.fromRGB(3361970)).addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION")).addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING")).addUnsafeEnchantment(Enchantment.PROTECTION_FALL, RevampHCF.getInstance().getConfig().getInt("KITS.FEATHER_FALLING")).toItemStack();
        final ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, RevampHCF.getInstance().getConfig().getInt("KITS.SHARPNESS"));
        sword.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, RevampHCF.getInstance().getConfig().getInt("KITS.POWER"));
        bow.addEnchantment(Enchantment.ARROW_FIRE, RevampHCF.getInstance().getConfig().getInt("FLAME"));
        bow.addEnchantment(Enchantment.ARROW_INFINITE, RevampHCF.getInstance().getConfig().getInt("KITS.INFINITE"));
        bow.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final PlayerInventory pi = p.getInventory();
        pi.setItem(0, sword);
        pi.setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
        pi.setItem(2, bow);
        pi.setItem(3, getHealPot());
        pi.setItem(4, getHealPot());
        pi.setItem(5, getHealPot());
        pi.setItem(6, getHealPot());
        pi.setItem(7, new ItemStack(Material.FEATHER, 16));
        pi.setItem(7, new ItemStack(Material.SUGAR, 16));
        pi.setItem(8, new ItemStack(Material.FEATHER, 16));
        pi.setItem(9, new ItemStack(Material.ARROW, 1));
        pi.setItem(10, getHealPot());
        pi.setItem(11, getHealPot());
        pi.setItem(12, getHealPot());
        pi.setItem(13, getHealPot());
        pi.setItem(14, getHealPot());
        pi.setItem(15, getHealPot());
        pi.setItem(16, getHealPot());
        pi.setItem(17, getHealPot());
        pi.setItem(18, getHealPot());
        pi.setItem(19, getHealPot());
        pi.setItem(20, getHealPot());
        pi.setItem(21, getHealPot());
        pi.setItem(22, getHealPot());
        pi.setItem(23, getHealPot());
        pi.setItem(24, getHealPot());
        pi.setItem(25, getHealPot());
        pi.setItem(26, getHealPot());
        pi.setItem(27, getHealPot());
        pi.setItem(28, getHealPot());
        pi.setItem(29, getHealPot());
        pi.setItem(30, getHealPot());
        pi.setItem(31, getHealPot());
        pi.setItem(32, getHealPot());
        pi.setItem(33, getHealPot());
        pi.setItem(34, getHealPot());
        pi.setItem(35, getHealPot());
        pi.setHelmet(lhelmet);
        pi.setChestplate(lplate);
        pi.setLeggings(lleggs);
        pi.setBoots(lboots);
        p.updateInventory();
        p.sendMessage(CC.translate(" &7» &a&lEquipped Scream Kit"));
    }
    
    public void giveLasherKit(Player p) {
        final ItemStack lhelmet = new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(org.bukkit.Color.fromRGB(5000268)).addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION")).addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING")).toItemStack();
        final ItemStack lplate = new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(org.bukkit.Color.fromRGB(5000268)).addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION")).addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING")).toItemStack();
        final ItemStack lleggs = new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(org.bukkit.Color.fromRGB(5000268)).addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION")).addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING")).toItemStack();
        final ItemStack lboots = new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(org.bukkit.Color.fromRGB(5000268)).addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION")).addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING")).addUnsafeEnchantment(Enchantment.PROTECTION_FALL, RevampHCF.getInstance().getConfig().getInt("KITS.FEATHER_FALLING")).toItemStack();
        final ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, RevampHCF.getInstance().getConfig().getInt("KITS.SHARPNESS"));
        sword.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, RevampHCF.getInstance().getConfig().getInt("KITS.POWER"));
        bow.addEnchantment(Enchantment.ARROW_FIRE, RevampHCF.getInstance().getConfig().getInt("FLAME"));
        bow.addEnchantment(Enchantment.ARROW_INFINITE, RevampHCF.getInstance().getConfig().getInt("KITS.INFINITE"));
        bow.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final PlayerInventory pi = p.getInventory();
        pi.setItem(0, sword);
        pi.setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
        pi.setItem(2, bow);
        pi.setItem(3, getHealPot());
        pi.setItem(4, getHealPot());
        pi.setItem(5, getHealPot());
        pi.setItem(6, getHealPot());
        pi.setItem(7, new ItemStack(Material.FEATHER, 16));
        pi.setItem(7, new ItemStack(Material.SUGAR, 16));
        pi.setItem(8, new ItemStack(Material.FEATHER, 16));
        pi.setItem(9, new ItemStack(Material.ARROW, 1));
        pi.setItem(10, getHealPot());
        pi.setItem(11, getHealPot());
        pi.setItem(12, getHealPot());
        pi.setItem(13, getHealPot());
        pi.setItem(14, getHealPot());
        pi.setItem(15, getHealPot());
        pi.setItem(16, getHealPot());
        pi.setItem(17, getHealPot());
        pi.setItem(18, getHealPot());
        pi.setItem(19, getHealPot());
        pi.setItem(20, getHealPot());
        pi.setItem(21, getHealPot());
        pi.setItem(22, getHealPot());
        pi.setItem(23, getHealPot());
        pi.setItem(24, getHealPot());
        pi.setItem(25, getHealPot());
        pi.setItem(26, getHealPot());
        pi.setItem(27, getHealPot());
        pi.setItem(28, getHealPot());
        pi.setItem(29, getHealPot());
        pi.setItem(30, getHealPot());
        pi.setItem(31, getHealPot());
        pi.setItem(32, getHealPot());
        pi.setItem(33, getHealPot());
        pi.setItem(34, getHealPot());
        pi.setItem(35, getHealPot());
        pi.setHelmet(lhelmet);
        pi.setChestplate(lplate);
        pi.setLeggings(lleggs);
        pi.setBoots(lboots);
        p.updateInventory();
        p.sendMessage(CC.translate(" &7» &a&lEquipped Lasher Kit"));
    }
    
    public void giveAgonyKit(Player p) {
        final ItemStack lhelmet = new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(org.bukkit.Color.fromRGB(6717235)).addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION")).addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING")).toItemStack();
        final ItemStack lplate = new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(org.bukkit.Color.fromRGB(6717235)).addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION")).addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING")).toItemStack();
        final ItemStack lleggs = new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(org.bukkit.Color.fromRGB(6717235)).addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION")).addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING")).toItemStack();
        final ItemStack lboots = new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(org.bukkit.Color.fromRGB(6717235)).addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION")).addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING")).addUnsafeEnchantment(Enchantment.PROTECTION_FALL, RevampHCF.getInstance().getConfig().getInt("KITS.FEATHER_FALLING")).toItemStack();
        final ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, RevampHCF.getInstance().getConfig().getInt("KITS.SHARPNESS"));
        sword.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, RevampHCF.getInstance().getConfig().getInt("KITS.POWER"));
        bow.addEnchantment(Enchantment.ARROW_FIRE, RevampHCF.getInstance().getConfig().getInt("FLAME"));
        bow.addEnchantment(Enchantment.ARROW_INFINITE, RevampHCF.getInstance().getConfig().getInt("KITS.INFINITE"));
        bow.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final PlayerInventory pi = p.getInventory();
        pi.setItem(0, sword);
        pi.setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
        pi.setItem(2, bow);
        pi.setItem(3, getHealPot());
        pi.setItem(4, getHealPot());
        pi.setItem(5, getHealPot());
        pi.setItem(6, getHealPot());
        pi.setItem(7, new ItemStack(Material.FEATHER, 16));
        pi.setItem(7, new ItemStack(Material.SUGAR, 16));
        pi.setItem(8, new ItemStack(Material.FEATHER, 16));
        pi.setItem(9, new ItemStack(Material.ARROW, 1));
        pi.setItem(10, getHealPot());
        pi.setItem(11, getHealPot());
        pi.setItem(12, getHealPot());
        pi.setItem(13, getHealPot());
        pi.setItem(14, getHealPot());
        pi.setItem(15, getHealPot());
        pi.setItem(16, getHealPot());
        pi.setItem(17, getHealPot());
        pi.setItem(18, getHealPot());
        pi.setItem(19, getHealPot());
        pi.setItem(20, getHealPot());
        pi.setItem(21, getHealPot());
        pi.setItem(22, getHealPot());
        pi.setItem(23, getHealPot());
        pi.setItem(24, getHealPot());
        pi.setItem(25, getHealPot());
        pi.setItem(26, getHealPot());
        pi.setItem(27, getHealPot());
        pi.setItem(28, getHealPot());
        pi.setItem(29, getHealPot());
        pi.setItem(30, getHealPot());
        pi.setItem(31, getHealPot());
        pi.setItem(32, getHealPot());
        pi.setItem(33, getHealPot());
        pi.setItem(34, getHealPot());
        pi.setItem(35, getHealPot());
        pi.setHelmet(lhelmet);
        pi.setChestplate(lplate);
        pi.setLeggings(lleggs);
        pi.setBoots(lboots);
        p.updateInventory();
        p.sendMessage(CC.translate(" &7» &a&lEquipped Agony Kit"));
    }
    
    public void giveToxinKit(Player p) {
        final ItemStack lhelmet = new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(org.bukkit.Color.fromRGB(1644825)).addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION")).addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING")).toItemStack();
        final ItemStack lplate = new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(org.bukkit.Color.fromRGB(1644825)).addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION")).addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING")).toItemStack();
        final ItemStack lleggs = new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(org.bukkit.Color.fromRGB(1644825)).addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION")).addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING")).toItemStack();
        final ItemStack lboots = new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(org.bukkit.Color.fromRGB(1644825)).addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, RevampHCF.getInstance().getConfig().getInt("KITS.PROTECTION")).addUnsafeEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING")).addUnsafeEnchantment(Enchantment.PROTECTION_FALL, RevampHCF.getInstance().getConfig().getInt("KITS.FEATHER_FALLING")).toItemStack();
        final ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, RevampHCF.getInstance().getConfig().getInt("KITS.SHARPNESS"));
        sword.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, RevampHCF.getInstance().getConfig().getInt("KITS.POWER"));
        bow.addEnchantment(Enchantment.ARROW_FIRE, RevampHCF.getInstance().getConfig().getInt("FLAME"));
        bow.addEnchantment(Enchantment.ARROW_INFINITE, RevampHCF.getInstance().getConfig().getInt("KITS.INFINITE"));
        bow.addEnchantment(Enchantment.DURABILITY, RevampHCF.getInstance().getConfig().getInt("KITS.UNBREAKING"));
        final PlayerInventory pi = p.getInventory();
        pi.setItem(0, sword);
        pi.setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
        pi.setItem(2, bow);
        pi.setItem(3, getHealPot());
        pi.setItem(4, getHealPot());
        pi.setItem(5, getHealPot());
        pi.setItem(6, getHealPot());
        pi.setItem(7, new ItemStack(Material.FEATHER, 16));
        pi.setItem(7, new ItemStack(Material.SUGAR, 16));
        pi.setItem(8, new ItemStack(Material.FEATHER, 16));
        pi.setItem(9, new ItemStack(Material.ARROW, 1));
        pi.setItem(10, getHealPot());
        pi.setItem(11, getHealPot());
        pi.setItem(12, getHealPot());
        pi.setItem(13, getHealPot());
        pi.setItem(14, getHealPot());
        pi.setItem(15, getHealPot());
        pi.setItem(16, getHealPot());
        pi.setItem(17, getHealPot());
        pi.setItem(18, getHealPot());
        pi.setItem(19, getHealPot());
        pi.setItem(20, getHealPot());
        pi.setItem(21, getHealPot());
        pi.setItem(22, getHealPot());
        pi.setItem(23, getHealPot());
        pi.setItem(24, getHealPot());
        pi.setItem(25, getHealPot());
        pi.setItem(26, getHealPot());
        pi.setItem(27, getHealPot());
        pi.setItem(28, getHealPot());
        pi.setItem(29, getHealPot());
        pi.setItem(30, getHealPot());
        pi.setItem(31, getHealPot());
        pi.setItem(32, getHealPot());
        pi.setItem(33, getHealPot());
        pi.setItem(34, getHealPot());
        pi.setItem(35, getHealPot());
        pi.setHelmet(lhelmet);
        pi.setChestplate(lplate);
        pi.setLeggings(lleggs);
        pi.setBoots(lboots);
        p.updateInventory();
        p.sendMessage(CC.translate(" &7» &a&lEquipped Toxin Kit"));
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        if (RevampHCF.getInstance().getConfiguration().isKitMap() && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            final BlockState state = e.getClickedBlock().getState();
            final HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(p);
            if (state instanceof Sign) {
                final Sign s = (Sign)state;
                if (getCooldown().contains(p.getName())) {
                    p.sendMessage(CC.translate("&cPlease wait before select kits!"));
                    return;
                }
                if ((!RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().hasCooldown(p.getUniqueId()) && s.getLine(1).contains("Kit") && s.getLine(2).equalsIgnoreCase("PvP")) || (!RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().hasCooldown(p.getUniqueId()) && s.getLine(1).contains("Kit") && s.getLine(2).equalsIgnoreCase("Diamond"))) {
                    this.giveDiamondKit(p);
                    getCooldown().add(p.getName());
                    new BukkitRunnable() {
                        public void run() {
                            getCooldown().remove(p.getName());
                        }
                    }.runTaskLater(this.getInstance(), 100L);
                }
                else if (!RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().hasCooldown(p.getUniqueId()) && s.getLine(1).contains("Kit") && s.getLine(2).equalsIgnoreCase("Bard")) {
                    this.giveBardKit(p);
                    getCooldown().add(p.getName());
                    new BukkitRunnable() {
                        public void run() {
                            getCooldown().remove(p.getName());
                        }
                    }.runTaskLater(this.getInstance(), 100L);
                }
                else if (!RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().hasCooldown(p.getUniqueId()) && s.getLine(1).contains("Kit") && s.getLine(2).equalsIgnoreCase("Ranger")) {
                    this.giveRangerKit(p);
                    getCooldown().add(p.getName());
                    new BukkitRunnable() {
                        public void run() {
                            getCooldown().remove(p.getName());
                        }
                    }.runTaskLater(this.getInstance(), 100L);
                }
                else if (!RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().hasCooldown(p.getUniqueId()) && s.getLine(1).contains("Kit") && s.getLine(2).equalsIgnoreCase("Archer")) {
                    this.giveArcherKit(p);
                    getCooldown().add(p.getName());
                    new BukkitRunnable() {
                        public void run() {
                            getCooldown().remove(p.getName());
                        }
                    }.runTaskLater(this.getInstance(), 100L);
                }
                else if (!RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().hasCooldown(p.getUniqueId()) && s.getLine(1).contains("Kit") && s.getLine(2).equalsIgnoreCase("Builder")) {
                    this.giveBuilderKit(p);
                    getCooldown().add(p.getName());
                    new BukkitRunnable() {
                        public void run() {
                            getCooldown().remove(p.getName());
                        }
                    }.runTaskLater(this.getInstance(), 100L);
                }
                else if (!RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().hasCooldown(p.getUniqueId()) && s.getLine(1).contains("Kit") && s.getLine(2).equalsIgnoreCase("Rogue")) {
                    this.giveRogueKit(p);
                    getCooldown().add(p.getName());
                    new BukkitRunnable() {
                        public void run() {
                            getCooldown().remove(p.getName());
                        }
                    }.runTaskLater(this.getInstance(), 100L);
                }
                else if (data.getKills() > 14 && !RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().hasCooldown(p.getUniqueId()) && s.getLine(1).contains("Kit") && s.getLine(2).equalsIgnoreCase("Scream")) {
                    this.giveScreamKit(p);
                    getCooldown().add(p.getName());
                    new BukkitRunnable() {
                        public void run() {
                            getCooldown().remove(p.getName());
                        }
                    }.runTaskLater(this.getInstance(), 100L);
                }
                else if (data.getKills() > 29 && !RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().hasCooldown(p.getUniqueId()) && s.getLine(1).contains("Kit") && s.getLine(2).equalsIgnoreCase("Lasher")) {
                    this.giveLasherKit(p);
                    getCooldown().add(p.getName());
                    new BukkitRunnable() {
                        public void run() {
                            getCooldown().remove(p.getName());
                        }
                    }.runTaskLater(this.getInstance(), 100L);
                }
                else if (data.getKills() > 49 && !RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().hasCooldown(p.getUniqueId()) && s.getLine(1).contains("Kit") && s.getLine(2).equalsIgnoreCase("Agony")) {
                    this.giveAgonyKit(p);
                    getCooldown().add(p.getName());
                    new BukkitRunnable() {
                        public void run() {
                            getCooldown().remove(p.getName());
                        }
                    }.runTaskLater(this.getInstance(), 100L);
                }
                else if (data.getKills() > 64 && !RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().hasCooldown(p.getUniqueId()) && s.getLine(1).contains("Kit") && s.getLine(2).equalsIgnoreCase("Toxin")) {
                    this.giveToxinKit(p);
                    getCooldown().add(p.getName());
                    new BukkitRunnable() {
                        public void run() {
                            getCooldown().remove(p.getName());
                        }
                    }.runTaskLater(this.getInstance(), 100L);
                }
            }
        }
    }*/
}

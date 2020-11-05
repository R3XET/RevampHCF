package eu.revamp.hcf.extra;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class Recipes extends Handler {

    public Recipes(RevampHCF instance) {
        super(instance);
        setupHeads();
        hrecipe();
        precipe();
        lrecipe();
        brecipe();
        mrecipe();
        gprecipe();
    }

    @SuppressWarnings("deprecation")
    private void setupHeads() {
        ItemStack goldenHead = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta gMeta = goldenHead.getItemMeta();
        gMeta.setDisplayName(CC.translate("&6&lGolden Head"));
        gMeta.setLore(Collections.singletonList(CC.translate("&7This will grant you better effects than a Gapple!")));
        goldenHead.setItemMeta(gMeta);
        ShapedRecipe goldenHeadRecipe = new ShapedRecipe(goldenHead);
        goldenHeadRecipe.shape("@@@", "@#@", "@@@");
        goldenHeadRecipe.setIngredient('@', Material.GOLD_INGOT);
        goldenHeadRecipe.setIngredient('#', Material.SKULL_ITEM, 3);
        this.getInstance().getServer().addRecipe(goldenHeadRecipe);
    }

    private void hrecipe() {
        ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET, 1);
        ItemMeta meta = helmet.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Chain Helmet");
        helmet.setItemMeta(meta);
        ShapedRecipe hrecipe = new ShapedRecipe(helmet);
        hrecipe.shape("@@@", "@ @");
        hrecipe.setIngredient('@', Material.IRON_FENCE);
        this.getInstance().getServer().addRecipe(hrecipe);
    }

    private void precipe() {
        ItemStack plate = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
        ItemMeta meta2 = plate.getItemMeta();
        meta2.setDisplayName(ChatColor.WHITE + "Chain Chestplate");
        plate.setItemMeta(meta2);
        ShapedRecipe precipe = new ShapedRecipe(plate);
        precipe.shape("@ @", "@@@", "@@@");
        precipe.setIngredient('@', Material.IRON_FENCE);
        this.getInstance().getServer().addRecipe(precipe);
    }

    private void lrecipe() {
        ItemStack leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
        ItemMeta meta3 = leggings.getItemMeta();
        meta3.setDisplayName(ChatColor.WHITE + "Chain Leggings");
        leggings.setItemMeta(meta3);
        ShapedRecipe lrecipe = new ShapedRecipe(leggings);
        lrecipe.shape("@@@", "@ @", "@ @");
        lrecipe.setIngredient('@', Material.IRON_FENCE);
        this.getInstance().getServer().addRecipe(lrecipe);
    }

    private void brecipe() {
        ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS, 1);
        ItemMeta meta4 = boots.getItemMeta();
        meta4.setDisplayName(ChatColor.WHITE + "Chain Leggings");
        boots.setItemMeta(meta4);
        ShapedRecipe lrecipe = new ShapedRecipe(boots);
        lrecipe.shape("   ", "@ @", "@ @");
        lrecipe.setIngredient('@', Material.IRON_FENCE);
        this.getInstance().getServer().addRecipe(lrecipe);
    }

    private void mrecipe() {
        ItemStack helmet = new ItemStack(Material.SPECKLED_MELON, 1);
        ShapedRecipe mrecipe = new ShapedRecipe(helmet);
        mrecipe.shape("@@@", "@ @");
        mrecipe.setIngredient('@', Material.SPECKLED_MELON);
        this.getInstance().getServer().addRecipe(mrecipe);
    }

    private void gprecipe() {
        ItemStack plate = new ItemStack(Material.SPECKLED_MELON, 1);
        ShapelessRecipe gprecipe = new ShapelessRecipe(plate);
        gprecipe.addIngredient(1, Material.MELON);
        gprecipe.addIngredient(1, Material.GOLD_NUGGET);
        this.getInstance().getServer().addRecipe(gprecipe);
    }
}

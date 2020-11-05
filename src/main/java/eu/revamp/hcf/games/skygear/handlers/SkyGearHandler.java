package eu.revamp.hcf.games.skygear.handlers;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.RevampSpigot;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SkyGearHandler {
  public void giveSpawnToken(Player player, String msg) {
    ItemStack spawntoken = new ItemStack(Material.PAPER);
    ItemMeta spawntokenMeta = spawntoken.getItemMeta();
    spawntokenMeta.setDisplayName(ChatColor.DARK_GREEN + "�" + ChatColor.GREEN + " Spawn Token " + ChatColor.DARK_GREEN + "�");
    spawntokenMeta.setLore(Arrays.asList(ChatColor.GREEN + "� Right-click to redeem this Token �"));
    spawntokenMeta.addEnchant(Enchantment.DURABILITY, 3, true);
    spawntoken.setItemMeta(spawntokenMeta);
    player.getInventory().addItem(spawntoken);

    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
  }

  public void giveBonusToken(Player player, String msg) {
    ItemStack bonustoken = new ItemStack(Material.PAPER);
    ItemMeta bonustokenMeta = bonustoken.getItemMeta();
    bonustokenMeta.setDisplayName(ChatColor.GOLD + "Bonus Token");
    bonustokenMeta.setLore(Arrays.asList(ChatColor.WHITE + "Right-Click to receive BonusXP (Only usable on SOTW)!"));
    bonustoken.setItemMeta(bonustokenMeta);
    player.getInventory().addItem(bonustoken);

    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
  }

  public void giveEndPortalToken(Player player, String msg) {
    ItemStack endportaltoken = new ItemStack(Material.PAPER);
    ItemMeta endportaltokenMeta = endportaltoken.getItemMeta();
    endportaltokenMeta.setDisplayName(ChatColor.DARK_AQUA + "�" + ChatColor.AQUA + " EndPortal Token " + ChatColor.DARK_AQUA + "�");
    endportaltokenMeta.setLore(Arrays.asList(ChatColor.DARK_AQUA + "� Right-click to redeem this Token �"));
    endportaltokenMeta.addEnchant(Enchantment.DURABILITY, 3, true);
    endportaltoken.setItemMeta(endportaltokenMeta);
    player.getInventory().addItem(endportaltoken);

    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
  }

  public void giveRank1Token(Player player, String msg) {
    ItemStack rank1token = new ItemStack(Material.PAPER);
    ItemMeta rank1tokenMeta = rank1token.getItemMeta();
    rank1tokenMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', RevampHCF.getInstance().getConfig().getString("skygear.rank2token.name")));
    rank1tokenMeta.setLore(Arrays.asList(ChatColor.DARK_AQUA + "� Right-click to redeem this Token �"));
    rank1tokenMeta.addEnchant(Enchantment.DURABILITY, 3, true);
    rank1token.setItemMeta(rank1tokenMeta);
    player.getInventory().addItem(rank1token);

    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
  }

  public void giveRank2Token(Player player, String msg) {
    ItemStack rank2token = new ItemStack(Material.PAPER);
    ItemMeta rank2tokenMeta = rank2token.getItemMeta();
    rank2tokenMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', RevampHCF.getInstance().getConfig().getString("skygear.rank1token.name")));
    rank2tokenMeta.setLore(Arrays.asList(ChatColor.DARK_AQUA + "� Right-click to redeem this Token �"));
    rank2tokenMeta.addEnchant(Enchantment.DURABILITY, 3, true);
    rank2token.setItemMeta(rank2tokenMeta);
    player.getInventory().addItem(rank2token);

    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
  }
}

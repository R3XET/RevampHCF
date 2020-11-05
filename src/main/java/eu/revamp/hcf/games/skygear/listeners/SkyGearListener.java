package eu.revamp.hcf.games.skygear.listeners;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.managers.CooldownManager;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/*
public class SkyGearListener
  implements Listener
{
    public static String format(int i) {
        int ms = i / 60;
        int ss = i % 60;
        String m = ((ms < 10) ? "0" : "") + ms;
        String s = ((ss < 10) ? "0" : "") + ss;
        String f = m + ":" + s;
        return f;
    }
    
  @EventHandler
  public void onSpawnTokenListener(PlayerInteractEvent event)
  {
    Player player = event.getPlayer();
    ItemStack item = event.getItem();
    Location location = player.getLocation();
    Faction factionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(location);
    if ((event.getAction() == Action.RIGHT_CLICK_AIR) && 
      (item != null) && 
      (item.getType() == Material.PAPER) && 
      (item.getItemMeta().getDisplayName().equals(ChatColor.DARK_GREEN + "�" + ChatColor.GREEN + " Spawn Token " + ChatColor.DARK_GREEN + "�") && item.containsEnchantment(Enchantment.DURABILITY))) {
      if (CooldownManager.isOnCooldown("SPAWNTOKEN_DELAY", player))
      {
        player.sendMessage(ChatColor.RED + "You must wait " + format(Cooldowns.getCooldownForPlayerInt("SPAWNTOKEN_DELAY", player)) + " before using Spawn Token again.");
      }
      else if (RevampHCF.getInstance().getTimerManager().getCombatTimer().getRemainingCooldown(player) > 0L) {
      	player.sendMessage(ChatColor.RED + "You cannot use Spawn Token while in Combat.");
      }
      else if (factionAt.isSafezone()) {
        	player.sendMessage(ChatColor.RED + "You cannot use Spawn Token at Spawn.");
        }
      else if (RevampHCF.getInstance().getFactionManager().getPlayerFaction(player) != factionAt && factionAt instanceof PlayerTeam) {
    	  player.sendMessage(ChatColor.RED + "You cannot use Spawn Token in enemy claim.");
      }
      else
      {
        sendLocation(player);
        ItemStack stack = player.getItemInHand();                    
        if (stack.getAmount() == 1) {
            player.setItemInHand(new ItemStack(Material.AIR, 1));
        } else {
            stack.setAmount(stack.getAmount() - 1);
        }
        CooldownManager.addCooldown("SPAWNTOKEN_DELAY", player, RevampHCF.getInstance().getConfig().getInt("skygear.spawntoken.cooldown"));
      }
    }
  }
  
  public boolean sendLocation(Player player)
  {
    if ((player.getLocation().getX() == 0.0D) && (player.getLocation().getBlockZ() == 0))
    {
      player.sendMessage(ChatColor.RED + "You are already at the spawn.");
      return true;
    }
    World world = Bukkit.getWorld("world");
    player.teleport(new Location(world, 0.0D, 75.0D, 0.0D));
    player.sendMessage("�3You have used one �bSpawn Token�3.");
    
    return false;
  }
  
  @EventHandler
  public void onEndPortalTokenListener(PlayerInteractEvent event)
  {
    Player player = event.getPlayer();
    ItemStack item = event.getItem();
    Location location = player.getLocation();
    Faction factionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(location);
    if ((event.getAction() == Action.RIGHT_CLICK_AIR) && 
      (item != null) && 
      (item.getType() == Material.PAPER) && 
      (item.getItemMeta().getDisplayName().equals(ChatColor.DARK_AQUA + "�" + ChatColor.AQUA + " EndPortal Token " + ChatColor.DARK_AQUA + "�") && item.containsEnchantment(Enchantment.DURABILITY))) {
      if (CooldownManager.isOnCooldown("endportaltoken", player))
      {
        player.sendMessage(ChatColor.RED + "You must wait " + format(CooldownManager.getCooldownForPlayerInt("endportaltoken", player)) + " before using EndPortal Token again.");
      }
      else if (RevampHCF.getInstance().getTimerManager().getCombatTimer().getRemainingCooldown(player) > 0L) {
      	player.sendMessage(ChatColor.RED + "You cannot use EndPortal Token while in Combat.");
      }
      else if (factionAt.isSafezone()) {
        	player.sendMessage(ChatColor.RED + "You cannot use EndPortal Token at Spawn.");
        }
      else if (RevampHCF.getInstance().getFactionManager().getPlayerFaction(player) != factionAt && factionAt instanceof PlayerTeam) {
    	  player.sendMessage(ChatColor.RED + "You cannot use EndPortal Token in enemy claim.");
      }
      else
      {
        sendEndPortalLocation(player);
        ItemStack stack = player.getItemInHand();                    
        if (stack.getAmount() == 1) {
            player.setItemInHand(new ItemStack(Material.AIR, 1));
        } else {
            stack.setAmount(stack.getAmount() - 1);
        }
        CooldownManager.addCooldown("endportaltoken", player, RevampHCF.getInstance().getConfig().getInt("skygear.endportaltoken.cooldown"));
      }
    }
  }
  
  public boolean sendEndPortalLocation(Player player)
  {
    if ((player.getLocation().getX() == 0.0D) && (player.getLocation().getBlockZ() == 0))
    {
      player.sendMessage(ChatColor.RED + "You are already at the spawn.");
      return true;
    }
    World world = Bukkit.getWorld("world");
    player.teleport(new Location(world, RevampHCF.getInstance().getConfig().getDouble("skygear.endportaltoken.coordinates.x"), RevampHCF.getInstance().getConfig().getDouble("skygear.endportaltoken.coordinates.y"), HardcoreFactionsPlugin.getInstance().getConfig().getDouble("skygear.endportaltoken.coordinates.z")));
    player.sendMessage("�3You have used one �bEndPortal Token�3.");
    
    return false;
  }
}
*/
package eu.revamp.hcf.handlers.player;

import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.Bukkit;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class KillStreakHandler extends Handler implements Listener
{
    public KillStreakHandler(RevampHCF plugin) {
        super(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity() instanceof Player && event.getEntity().getKiller() instanceof Player) {
            Player killed = event.getEntity();
            Player killer = killed.getKiller();
            event.setDroppedExp(0);
            killer.setLevel(killer.getLevel() + 1);
            killed.setLevel(0);
        }
    }
    
    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
    public void onPlayerLevelChange(PlayerLevelChangeEvent event) {
        Player player = event.getPlayer();
        switch (event.getNewLevel()) {
            case 3: {
                ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 3);
                player.getInventory().addItem(apple);
                player.sendMessage(CC.translate("&8[&b*&8]&c " + player.getName() + "&e got &63 Golden Apple &efrom their killstreak of &c3 &e."));
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "crate givekey " + player.getName() + " Streak 1");
                break;
            }
            case 6: {
                ItemStack slowness = new ItemStack(Material.POTION, 1, (short)16426);
                ItemStack poison = new ItemStack(Material.POTION, 1, (short)16388);
                player.getInventory().addItem(slowness, poison);
                Bukkit.getServer().broadcastMessage(CC.translate("&8[&b*&8]&c " + player.getName() + "&e got &61 Splash Slowness &e& &6Poison &efrom their killstreak of &c6 &e."));
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "crate givekey " + player.getName() + " Streak 2");
                break;
            }
            case 10: {
                ItemStack invisible = new ItemStack(Material.POTION, 1, (short)16430);
                player.getInventory().addItem(invisible);
                Bukkit.getServer().broadcastMessage(CC.translate("&8[&b*&8]&c " + player.getName() + "&e got &61 Splash Invisibility &efrom their killstreak of &c10 &e."));
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "crate givekey " + player.getName() + " Streak 3");
                break;
            }
            case 12: {
                ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 1, (short)1);
                player.getInventory().addItem(gapple);
                Bukkit.getServer().broadcastMessage(CC.translate("&8[&b*&8]&c " + player.getName() + "&e got &61 God Apple &efrom their killstreak of &c12 &e."));
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "crate givekey " + player.getName() + " Streak 5");
                break;
            }
            case 15: {
                ItemStack strength = new ItemStack(Material.POTION, 1, (short)16425);
                player.getInventory().addItem(strength);
                Bukkit.getServer().broadcastMessage(CC.translate("&8[&b*&8]&c " + player.getName() + "&e got &61 Splash Strength II &efrom their killstreak of &c15 &e."));
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "crate givekey " + player.getName() + " Streak 8");
                break;
            }
            case 30: {
                ItemStack sharpness = new ItemStack(Material.DIAMOND_SWORD, 1);
                sharpness.addEnchantment(Enchantment.DAMAGE_ALL, 2);
                ItemMeta meta = sharpness.getItemMeta();
                meta.setDisplayName(CC.translate("&8[&cKillstreak Sword&8]"));
                sharpness.setItemMeta(meta);
                player.getInventory().addItem(sharpness);
                Bukkit.getServer().broadcastMessage(CC.translate("&8[&b*&8]&c " + player.getName() + "&e got &6Sword sharpness2 fire1 &efrom their killstreak of &c30 &e."));
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "crate givekey " + player.getName() + " Streak 10");
                break;
            }
        }
    }
}

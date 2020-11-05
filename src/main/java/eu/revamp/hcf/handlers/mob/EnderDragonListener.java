package eu.revamp.hcf.handlers.mob;

import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.item.ItemBuilder;
import org.bukkit.event.EventHandler;
import eu.revamp.hcf.factions.type.PlayerFaction;
import org.bukkit.Sound;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.entity.EntityDeathEvent;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

import java.util.ArrayList;
import java.util.List;

public class EnderDragonListener extends Handler implements Listener
{
    public EnderDragonListener(RevampHCF plugin) {
        super(plugin);
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }

    @EventHandler @SuppressWarnings("deprecation")
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof EnderDragon && event.getEntity().getKiller() instanceof Player) {
            Player player = event.getEntity().getKiller();
            PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
            if (playerFaction != null) {
                String factionName = playerFaction.getName();
                List<String> dragonLore = new ArrayList<>();
                for (String lore : RevampHCF.getInstance().getConfig().getStringList("ENDER_DRAGON_EGG.LORE")) {
                    dragonLore.add(lore.replace("%player%", player.getName()).replace("%faction%", factionName));
                }
                ItemStack dragonEgg = new ItemBuilder(RevampHCF.getInstance().getConfig().getMaterial("ENDER_DRAGON_EGG.MATERIAL"))
                        .setName(RevampHCF.getInstance().getConfig().getString("ENDER_DRAGON_EGG.NAME"))
                        .setLore(dragonLore).toItemStack();
                event.setDroppedExp((int)Math.round(event.getDroppedExp() * 2.25));
                event.getDrops().clear();
                if (player.getInventory().firstEmpty() < 0) {
                    player.getWorld().dropItemNaturally(player.getLocation(), dragonEgg);
                    player.sendMessage(RevampHCF.getInstance().getConfig().getString("ENDER_DRAGON_EGG.DROPPED_MESSAGE"));
                }
                else {
                    player.getInventory().addItem(dragonEgg);
                    player.sendMessage(CC.translate(RevampHCF.getInstance().getConfig().getString("ENDER_DRAGON_EGG.ADDED_MESSAGE")));
                }
                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    online.playSound(online.getLocation(), Sound.ENDERDRAGON_DEATH, 1.0f, 1.0f);
                }
                Bukkit.broadcastMessage(CC.translate(RevampHCF.getInstance().getConfig().getString("ENDER_DRAGON_EGG.KILLED_MESSAGE")).replace("%faction%", factionName).replace("%player%", player.getName()));
            }
        }
    }
}

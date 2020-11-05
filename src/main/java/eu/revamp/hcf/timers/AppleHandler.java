package eu.revamp.hcf.timers;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.managers.CooldownManager;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.time.TimeUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class AppleHandler extends Handler implements Listener
{
    public AppleHandler(RevampHCF plugin) {
        super(plugin);
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        ItemStack goldenHead = event.getItem();
        if (item.getType().equals(Material.GOLDEN_APPLE) && item.getDurability() == 0 && !goldenHead.getItemMeta().hasLore()) {
            if (CooldownManager.isOnCooldown("APPLE_DELAY", player)) {
                event.setCancelled(true);
                for (String str : this.getInstance().getLanguage().getStringList("COOLDOWN.APPLE")) {
                    player.sendMessage(str.replace("%time%", TimeUtils.getRemaining((CooldownManager.getCooldownMillis("APPLE_DELAY", player)), true)));
                }
            }
            else {
                if (!CooldownManager.getCooldowns().containsKey("APPLE_DELAY")) {
                    CooldownManager.createCooldown("APPLE_DELAY");
                }
                CooldownManager.addCooldown("APPLE_DELAY", player, RevampHCF.getInstance().getConfig().getInt("COOLDOWNS.APPLE"));
                for (String str : RevampHCF.getInstance().getLanguage().getStringList("APPLE")) {
                    player.sendMessage(str.replace("%time%", TimeUtils.getRemaining(CooldownManager.getCooldownMillis("APPLE_DELAY", player), true)));
                }
            }
        }
    }
}

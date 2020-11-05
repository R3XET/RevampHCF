package eu.revamp.hcf.timers;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.managers.CooldownManager;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.spigot.utils.time.TimeUtils;
import net.mineaus.lunar.LunarClientAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class GappleHandler extends Handler implements Listener
{
    public GappleHandler(RevampHCF plugin) {
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
        if (item.getType().equals(Material.GOLDEN_APPLE) && item.getDurability() == 1) {
            if (CooldownManager.isOnCooldown("GAPPLE_DELAY", player)) {
                event.setCancelled(true);
                for (String str : this.getInstance().getLanguage().getStringList("COOLDOWN.GAPPLE")) {
                    player.sendMessage(str.replace("%time%", TimeUtils.getRemaining((CooldownManager.getCooldownMillis("GAPPLE_DELAY", player)), true)));
                }
            }
            else {
                if (!CooldownManager.getCooldowns().containsKey("GAPPLE_DELAY")) {
                    CooldownManager.createCooldown("GAPPLE_DELAY");
                }
                CooldownManager.addCooldown("GAPPLE_DELAY", player, RevampHCF.getInstance().getConfig().getInt("COOLDOWNS.GAPPLE"));
                if (LunarClientAPI.getInstance().isAuthenticated(player)) {
                    try {
                        LunarClientAPI.getInstance().sendCooldown(player, "Gapple", Material.GOLDEN_APPLE, RevampHCF.getInstance().getConfig().getInt("COOLDOWNS.GAPPLE"));
                    } catch (IOException ignored) { }
                }
                for (String str : RevampHCF.getInstance().getLanguage().getStringList("GAPPLE")) {
                    player.sendMessage(str.replace("%time%", TimeUtils.getRemaining(CooldownManager.getCooldownMillis("GAPPLE_DELAY", player), true)));
                }
            }
        }
    }
}

package eu.revamp.hcf.timers;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.managers.CooldownManager;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.packages.RevampPackages;
import eu.revamp.packages.utils.ConfigFile;
import eu.revamp.spigot.utils.time.TimeUtils;
import org.apache.commons.codec.language.bm.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HeadAppleHandler extends Handler implements Listener
{
    public HeadAppleHandler(RevampHCF plugin) {
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
        ItemMeta im = item.getItemMeta();
        String dustName = CC.translate("&6&lGolden Head");
        if (!item.hasItemMeta() || !im.hasDisplayName() || !im.getDisplayName().equals(dustName)) return;
        if (item.getType().equals(Material.GOLDEN_APPLE) && item.getDurability() == 0 && !goldenHead.getItemMeta().hasLore()) {
            if (CooldownManager.isOnCooldown("HEADAPPLE_DELAY", player)) {
                event.setCancelled(true);
                player.sendMessage(Language.COOLDOWN_HEAD_APPLE.toString().replace("%time%", TimeUtils.getRemaining(CooldownManager.getCooldownMillis("HEADAPPLE_DELAY", player), true)));
            }
            else {
                if (!CooldownManager.getCooldowns().containsKey("HEADAPPLE_DELAY")) {
                    CooldownManager.createCooldown("HEADAPPLE_DELAY");
                }
                CooldownManager.addCooldown("HEADAPPLE_DELAY", player, RevampHCF.getInstance().getConfig().getInt("COOLDOWNS.GOLDEN-HEAD"));
            }
        }
    }
}

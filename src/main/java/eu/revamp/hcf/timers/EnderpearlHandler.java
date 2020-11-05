package eu.revamp.hcf.timers;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.managers.CooldownManager;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.spigot.utils.time.TimeUtils;
import eu.revamp.system.api.player.PlayerData;
import eu.revamp.system.plugin.RevampSystem;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class EnderpearlHandler extends Handler implements Listener
{
    public EnderpearlHandler(RevampHCF plugin) {
        super(plugin);
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }

    public void quit(Player player) {
        if (CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player)) {
            CooldownManager.removeCooldown("ENDERPEARL_DELAY", player);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (player.getGameMode().equals(GameMode.CREATIVE)) return;
        PlayerData targetProfile = RevampSystem.getINSTANCE().getPlayerManagement().getPlayerData(player.getUniqueId());
        if (event.hasItem() && (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) && event.getItem().getType().equals(Material.ENDER_PEARL)) {
            if (CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player)) {
                event.setUseItemInHand(Event.Result.DENY);
                player.sendMessage(Language.COOLDOWN_ENDERPEARL.toString().replace("%time%", TimeUtils.getRemaining(CooldownManager.getCooldownMillis("ENDERPEARL_DELAY", player), true)));
            }
            else if (event.getItem().getType() == Material.ENDER_PEARL && event.getItem().getItemMeta().getDisplayName() != null && event.getItem().getItemMeta().getDisplayName().equals(this.getInstance().getConfig().getString("ABILITY.ULTIMATEPEARL.NAME")) && !targetProfile.isFrozen() || targetProfile.isGuiFrozen()) {
                if (!CooldownManager.getCooldowns().containsKey("ENDERPEARL_DELAY")) {
                    CooldownManager.createCooldown("ENDERPEARL_DELAY");
                }
                CooldownManager.addCooldown("ENDERPEARL_DELAY", player, this.getInstance().getConfig().getDouble("COOLDOWNS.ULTIMATE-PEARL"));
            }
            else {
                event.getItem().getType();
                if (!CooldownManager.getCooldowns().containsKey("ENDERPEARL_DELAY")) {
                    CooldownManager.createCooldown("ENDERPEARL_DELAY");
                }
                CooldownManager.addCooldown("ENDERPEARL_DELAY", player, this.getInstance().getConfig().getDouble("COOLDOWNS.ENDERPEARL"));
            }
        }
    }
}

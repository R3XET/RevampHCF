package eu.revamp.hcf.handlers.disguise;
/*
import de.robingrether.idisguise.api.DisguiseAPI;
import de.robingrether.idisguise.iDisguise;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageModule extends Handler implements Listener
{
    public DamageModule(RevampHCF instance) {
        super(instance);
    }
    @Override
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player)e.getEntity();
            DisguiseAPI disguiseAPI = iDisguise.getInstance().getAPI();
            if (disguiseAPI.isDisguised(player)) {
                disguiseAPI.undisguise((OfflinePlayer)player);
                player.sendMessage(CC.translate(RevampHCF.getInstance().getConfig().getString("DISGUISE_SETTINGS.DISGUISE_BROKEN")));
            }
        }
    }

    @EventHandler
    public void onInteractEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player player = (Player)e.getDamager();
            DisguiseAPI disguiseAPI = iDisguise.getInstance().getAPI();
            if (disguiseAPI.isDisguised(player)) {
                disguiseAPI.undisguise((OfflinePlayer)player);
                player.sendMessage(CC.translate(RevampHCF.getInstance().getConfig().getString("DISGUISE_SETTINGS.DISGUISE_BROKEN")));
            }
        }
    }
}
*/
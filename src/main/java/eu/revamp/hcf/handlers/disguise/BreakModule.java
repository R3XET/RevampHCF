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
import org.bukkit.event.block.BlockBreakEvent;

public class BreakModule extends Handler implements Listener
{
    public BreakModule(RevampHCF instance) {
        super(instance);
    }
    @Override
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        DisguiseAPI disguiseAPI = iDisguise.getInstance().getAPI();
        if (disguiseAPI.isDisguised(player)) {
            disguiseAPI.undisguise((OfflinePlayer)player);
            player.sendMessage(CC.translate(RevampHCF.getInstance().getConfig().getString("DISGUISE_SETTINGS.DISGUISE_BROKEN")));
        }
    }
}
*/
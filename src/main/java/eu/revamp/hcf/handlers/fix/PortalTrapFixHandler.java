package eu.revamp.hcf.handlers.fix;

import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class PortalTrapFixHandler extends Handler implements Listener
{
    public PortalTrapFixHandler(RevampHCF plugin) {
        super(plugin);
    }
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;
        if (RevampHCF.getInstance().getFactionManager().getFactionAt(e.getClickedBlock().getLocation()).isSafezone()) return;
        if (e.getClickedBlock().getType() == Material.PORTAL && e.getPlayer().getGameMode() == GameMode.CREATIVE) {
            e.getClickedBlock().setType(Material.AIR);
            e.getPlayer().sendMessage(CC.translate("&eYou have &cdisabled &ethis portal&e."));
        }
    }
}

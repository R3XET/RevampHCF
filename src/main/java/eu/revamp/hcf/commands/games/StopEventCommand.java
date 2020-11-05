package eu.revamp.hcf.commands.games;

import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.timers.GameHandler;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;

public class StopEventCommand extends BaseCommand
{
    public StopEventCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "stopevent";
        this.permission = "*";
        this.forPlayerUseOnly = false;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(CC.translate("&cPlease specify name."));
            return;
        }
        GameHandler eventTimer = RevampHCF.getInstance().getHandlerManager().getTimerManager().getGameHandler();
        Faction eventFaction = eventTimer.getEventFaction();
        if (!eventTimer.clearCooldown()) {
            sender.sendMessage(CC.translate("&cThere is not a running event."));
            return;
        }
        Bukkit.broadcastMessage(CC.translate("&f" + sender.getName() + " &ehas cancelled &f" + ((eventFaction == null) ? "the active event" : (eventFaction.getName() + "&e")) + "."));
    }
}

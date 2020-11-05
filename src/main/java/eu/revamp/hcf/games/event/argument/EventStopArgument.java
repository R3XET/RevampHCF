package eu.revamp.hcf.games.event.argument;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.timers.GameHandler;
import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class EventStopArgument extends CommandArgument {

    private final RevampHCF plugin;

    public EventStopArgument(RevampHCF plugin) {
        super("end", "Ends an event");
        this.plugin = plugin;
        this.aliases = new String[]{ "end" };
        this.permission = "event.command." + this.getName();
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <eventName>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(CC.translate("&cPlease specify name."));
            return false;
        }
        GameHandler eventTimer = this.plugin.getHandlerManager().getTimerManager().getGameHandler();
        Faction eventFaction = eventTimer.getEventFaction();
        if (!eventTimer.clearCooldown()) {
            sender.sendMessage(CC.translate("&cThere is not a running event."));
            return false;
        }
        Bukkit.broadcastMessage(CC.translate("&f" + sender.getName() + " &ehas cancelled &f" + ((eventFaction == null) ? "the active event" : (eventFaction.getName() + "&e")) + "."));
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}

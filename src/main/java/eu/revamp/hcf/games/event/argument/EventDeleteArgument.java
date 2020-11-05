package eu.revamp.hcf.games.event.argument;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.utils.games.EventFaction;
import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class EventDeleteArgument extends CommandArgument {

    private final RevampHCF plugin;

    public EventDeleteArgument(RevampHCF plugin) {
        super("delete", "Delete an event");
        this.plugin = plugin;
        this.aliases = new String[] { "remove" };
        this.permission = "event.command." + this.getName();
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <eventName>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            return false;
        }
        Faction faction2 = this.plugin.getFactionManager().getFaction(args[1]);
        if (!(faction2 instanceof EventFaction)) {
            sender.sendMessage(CC.translate("&cThere is already a faction named &l" + args[1]));
            return false;
        }
        if (this.plugin.getFactionManager().removeFaction(faction2, sender)) {
            sender.sendMessage(CC.translate("&esuccessfully deleted event faction &f" + faction2.getDisplayName(sender) + "&e."));
        }
        return false;
    }
}

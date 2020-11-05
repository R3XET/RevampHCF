package eu.revamp.hcf.games.event.argument;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.utils.games.EventFaction;
import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class EventRenameArgument extends CommandArgument {
    private final RevampHCF plugin;

    public EventRenameArgument(RevampHCF plugin) {
        super("rename", "Rename event");
        this.plugin = plugin;
        this.aliases = new String[]{ "tag" };
        this.permission = "event.command." + this.getName();
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <eventName>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(CC.translate("&cPlease specify the name."));
            return false;
        }
        if (args.length == 2) {
            sender.sendMessage(CC.translate("&cPlease specify the new name."));
            return false;
        }
        Faction faction2 = this.plugin.getFactionManager().getFaction(args[2]);
        if (faction2 != null) {
            sender.sendMessage(CC.translate("&cThere is already a faction named &l" + args[2]));
            return false;
        }
        faction2 = this.plugin.getFactionManager().getFaction(args[1]);
        if (!(faction2 instanceof EventFaction)) {
            sender.sendMessage(CC.translate("&cThere is not an event faction named &l" + args[1]));
            return false;
        }
        String oldName = faction2.getName();
        faction2.setName(args[2], sender);
        sender.sendMessage(CC.translate("&eYou have successfully renamed event &f" + oldName + " &fto &f" + faction2.getName()));
        return false;
    }
}

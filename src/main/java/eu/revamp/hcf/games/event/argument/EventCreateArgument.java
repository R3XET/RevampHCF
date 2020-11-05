package eu.revamp.hcf.games.event.argument;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.FactionType;
import eu.revamp.hcf.factions.utils.games.ConquestFaction;
import eu.revamp.hcf.factions.utils.games.KothFaction;
import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.spigot.utils.chat.color.CC;
import org.apache.commons.lang.WordUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class EventCreateArgument extends CommandArgument {

    private final RevampHCF plugin;
    
    public EventCreateArgument(RevampHCF plugin) {
        super("create", "Create an event");
        this.plugin = plugin;
        this.permission = "event.command." + this.getName();
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <eventName>";
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(CC.translate("&cPlease specify name."));
            return true;
        }
        if (args.length == 2) {
            sender.sendMessage(CC.translate("&cPlease specify type."));
            return true;
        }
        Faction faction2 = this.plugin.getFactionManager().getFaction(args[1]);
        if (faction2 != null) {
            sender.sendMessage(CC.translate("&cThere is already a faction named &l" + args[1]));
            return true;
        }
        gameType: {
            switch (args[2].toUpperCase()) {
                case "KOTH": {
                    faction2 = new KothFaction(args[1]);
                    break gameType;
                }
                case "CONQUEST": {
                    faction2 = new ConquestFaction(args[1]);
                    break gameType;
                }
                default:
                    break;
            }
            return true;
        }
        FactionType factionType = FactionType.valueOf(args[2]);
        String name = args[1];
        this.plugin.getFactionManager().createFaction(sender, name, factionType);
        sender.sendMessage(CC.translate("&eYou have successfully created event faction &f" + faction2.getDisplayName(sender) + " &ewith type &f" + WordUtils.capitalizeFully(args[2])));

        return false;
    }
}

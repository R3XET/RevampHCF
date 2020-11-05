package eu.revamp.hcf.games.event.argument;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.utils.games.KothFaction;
import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EventTeleportArgument extends CommandArgument {

    private final RevampHCF plugin;

    public EventTeleportArgument(RevampHCF plugin) {
        super("teleport", "Teleport to event");
        this.plugin = plugin;
        this.aliases = new String[]{ "tp" };
        this.permission = "event.command." + this.getName();
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <eventName>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(CC.translate("&cYou must put Event Name!"));
            return false;
        }
        Faction faction2 = this.plugin.getFactionManager().getFaction(args[1]);
        if (!(faction2 instanceof KothFaction)) {
            sender.sendMessage(CC.translate("&cThere is no event named &l" + args[1] + "&c!"));
            return false;
        }
        Location loc = ((KothFaction)faction2).getCaptureZone().getCuboid().getCenter();
        Player player = (Player) sender;
        player.teleport(loc);
        sender.sendMessage(CC.translate("&eYou have been teleported to &d" + args[1] + " &eevent!"));
        return false;
    }
}

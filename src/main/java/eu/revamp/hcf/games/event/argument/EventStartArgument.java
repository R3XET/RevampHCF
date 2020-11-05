package eu.revamp.hcf.games.event.argument;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.utils.games.ConquestFaction;
import eu.revamp.hcf.factions.utils.games.EventFaction;
import eu.revamp.hcf.factions.utils.games.KothFaction;
import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.time.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class EventStartArgument extends CommandArgument {

    private final RevampHCF plugin;

    public EventStartArgument(RevampHCF plugin) {
        super("start", "Starts an event");
        this.plugin = plugin;
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
        Faction faction = this.plugin.getFactionManager().getFaction(args[0]);
        if (!(faction instanceof EventFaction)) {
            sender.sendMessage(CC.translate("&cThere is no event named &l" + args[0]));
        }
        else if (this.plugin.getHandlerManager().getTimerManager().getGameHandler().tryContesting((EventFaction)faction, sender)) {
            sender.sendMessage(CC.translate("&eYou have Successfully started &f" + faction.getName()));
            if (faction instanceof KothFaction){
                for (String message : RevampHCF.getInstance().getLanguage().getStringList("KOTH")){
                    Bukkit.broadcastMessage(CC.translate(message).replace("%koth%", faction.getName()).replace("%time%", TimeUtils.getRemaining(RevampHCF.getInstance().getHandlerManager().getTimerManager().getGameHandler().getRemaining(), true)));
                }
            }
            else if (faction instanceof ConquestFaction){
                //TODO ADD MESSAGES
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}

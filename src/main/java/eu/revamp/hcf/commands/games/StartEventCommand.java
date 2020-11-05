package eu.revamp.hcf.commands.games;

import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.utils.games.EventFaction;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;

public class StartEventCommand extends BaseCommand
{
    public StartEventCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "startevent";
        this.permission = "*";
        this.forPlayerUseOnly = false;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(CC.translate("&cPlease specify name."));
            return;
        }
        Faction faction = RevampHCF.getInstance().getFactionManager().getFaction(args[0]);
        if (!(faction instanceof EventFaction)) {
            sender.sendMessage(CC.translate("&cThere is no event named &l" + args[0]));
            return;
        }
        if (RevampHCF.getInstance().getHandlerManager().getTimerManager().getGameHandler().tryContesting((EventFaction)faction, sender)) {
            sender.sendMessage(CC.translate("&eYou have Successfully started &f" + faction.getName()));
        }
    }
}

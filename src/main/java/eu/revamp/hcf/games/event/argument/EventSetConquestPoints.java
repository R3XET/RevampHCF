package eu.revamp.hcf.games.event.argument;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.utils.games.KothFaction;
import eu.revamp.hcf.factions.utils.zone.CaptureZone;
import eu.revamp.hcf.games.ConquestType;
import eu.revamp.hcf.games.GameType;
import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.generic.ConversionUtils;
import eu.revamp.spigot.utils.time.TimeUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class EventSetConquestPoints extends CommandArgument {
    private final RevampHCF plugin;

    public EventSetConquestPoints(RevampHCF plugin) {
        super("setconquestpoints", "Set conquest points");
        this.plugin = plugin;
        this.permission = "event.command." + this.getName();
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <eventName> <points>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            return false;
        }
        Faction faction2 = this.plugin.getFactionManager().getFaction(args[1]);
        if (!(faction2 instanceof PlayerFaction)) {
            sender.sendMessage(Language.FACTIONS_FACTION_NOT_FOUND.toString());
            return false;
        }
        if (!ConversionUtils.isInteger(args[2])){
            sender.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
            return false;
        }
        int amount = Integer.parseInt(args[2]);
        if (amount > 300) {
            sender.sendMessage(CC.translate("&cMaximum points for Conquest is 300."));
            return false;
        }
        PlayerFaction playerFaction = (PlayerFaction)faction2;
        ((ConquestType) GameType.CONQUEST.getEventTracker()).setPoints(playerFaction, amount);
        sender.sendMessage(CC.translate("&esuccessfully set the points of faction &f" + playerFaction.getName() + " &eto &f" + amount + "&e."));
        return false;
    }
}

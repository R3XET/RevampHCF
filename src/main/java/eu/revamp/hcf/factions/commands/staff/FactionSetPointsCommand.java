package eu.revamp.hcf.factions.commands.staff;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.chat.message.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionSetPointsCommand extends CommandArgument
{
    public FactionSetPointsCommand(RevampHCF plugin) {
        super("setpoints", "Sets the points of a faction", new String[] { "setfactionpoints" });
        this.permission = "*";
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <factionName> <points>.";
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
            return true;
        }
        int newPoints = Integer.parseInt(args[2]);
        if (newPoints < 0) {
            sender.sendMessage(RevampHCF.getInstance().getConfig().getString("PREFIX") + RevampHCF.getInstance().getConfig().getString("COMMANDS.INVALID_NUMBER"));
            return true;
        }
        Faction faction = RevampHCF.getInstance().getFactionManager().getContainingFaction(args[1]);
        if (faction == null) {
            sender.sendMessage(Language.FACTIONS_FACTION_NOT_FOUND.toString());
            return true;
        }
        if (!(faction instanceof PlayerFaction)) {
            sender.sendMessage(CC.translate("&cThis type of faction does not use points."));
            return true;
        }
        PlayerFaction playerFaction = (PlayerFaction)faction;
        int previousPoints = playerFaction.getPoints();
        playerFaction.setPoints(newPoints);
        MessageUtils.sendMessage(CC.translate("&a&l" + sender.getName() + "&asuccessfully set &l" + newPoints + " &afrom " + previousPoints + " &eto the faction &f" + faction.getName() + "&e."), "hcf.admin");
        return true;
    }
}

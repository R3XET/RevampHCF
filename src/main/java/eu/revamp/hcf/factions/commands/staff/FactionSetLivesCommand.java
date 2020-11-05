package eu.revamp.hcf.factions.commands.staff;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionSetLivesCommand extends CommandArgument
{
    public FactionSetLivesCommand(RevampHCF plugin) {
        super("setlives", "Sets the lives of a faction", new String[] { "setfactionlives" });
        this.permission = "*";
    }
    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <factionName> <lives>.";
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
            return true;
        }
        int newLives = Integer.parseInt(args[2]);
        if (newLives < 0) {
            sender.sendMessage(RevampHCF.getInstance().getConfig().getString("PREFIX") + RevampHCF.getInstance().getConfig().getString("COMMANDS.INVALID_NUMBER"));
            return true;
        }
        if (newLives > 300) {
            sender.sendMessage(CC.translate("&cYou can't set faction lives above 300."));
            return true;
        }
        Faction faction = RevampHCF.getInstance().getFactionManager().getContainingFaction(args[1]);
        if (faction == null) {
            sender.sendMessage(Language.FACTIONS_FACTION_NOT_FOUND.toString());
            return true;
        }
        if (!(faction instanceof PlayerFaction)) {
            sender.sendMessage(CC.translate("&cThis type of faction does not use lives."));
            return true;
        }
        PlayerFaction playerFaction = (PlayerFaction)faction;
        int previousLives = playerFaction.getLives();
        playerFaction.setLives(newLives);
        sender.sendMessage(CC.translate("&eYou have successfully set faction lives of &f" + faction.getName() + " &efrom &f" + previousLives + " &eto &f" + newLives + "&e."));
        return true;
    }
}

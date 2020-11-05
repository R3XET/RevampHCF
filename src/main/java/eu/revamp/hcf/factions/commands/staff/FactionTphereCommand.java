package eu.revamp.hcf.factions.commands.staff;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionTphereCommand extends CommandArgument
{
    private final RevampHCF plugin;
    
    public FactionTphereCommand(RevampHCF plugin) {
        super("tphere", "teleport a faction to you");
        this.plugin = plugin;
        this.permission = "faction.tphere";
        this.isPlayerOnly = true;
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <factionName>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final Player player = (Player)sender;
        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
            return true;
        }
        final Faction faction = RevampHCF.getInstance().getFactionManager().getContainingFaction(args[1]);
        if (faction == null) {
            sender.sendMessage(Language.FACTIONS_FACTION_NOT_FOUND.toString());
            return true;
        }
        if (!(faction instanceof PlayerFaction)) {
            sender.sendMessage(CC.translate("&cYou only can teleport player factions!"));
            return true;
        }
        for (Player online : ((PlayerFaction)faction).getOnlinePlayers()) {
            online.teleport(player.getLocation());
        }
        player.sendMessage(CC.translate("&eYou have teleported &d" + faction.getName() + " &eto yourself!"));
        return true;
    }
}

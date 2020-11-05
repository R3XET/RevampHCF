package eu.revamp.hcf.games.fury;

/*
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class FurySetpointsArgument
extends CommandArgument {
    private final HardcoreFactionsPlugin plugin;

    public FurySetpointsArgument(HardcoreFactionsPlugin plugin) {
        super("setpoints", "Sets the points of a faction in the Fury event", "hcf.command.fury.argument.setpoints");
        this.plugin = plugin;
    }

    @Override
	public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " <factionName> <amount>";
    }

    @Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ITeam faction = plugin.getFactionManager().getFaction(args[1]);
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        if (!(faction instanceof PlayerTeam)) {
            sender.sendMessage(ChatColor.RED + "Faction " + args[1] + " is either not found or is not a player faction.");
            return true;
        }
        Integer amount = JavaUtils.tryParseInt(args[2]);
        if (amount == null) {
            sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a number.");
            return true;
        }
        if (amount > this.plugin.getConfiguration().getConquestRequiredVictoryPoints()) {
            sender.sendMessage(ChatColor.RED + "Maximum points for Fury is " + this.plugin.getConfig().getInt("event-utilities.fury.required-points") + '.');
            return true;
        }
        PlayerTeam playerFaction = (PlayerTeam)faction;
        ((FuryTracker)EventType.FURY.getEventTracker()).setPoints(playerFaction, amount);
        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Set the points of faction " + playerFaction.getName() + " to " + amount + '.');
        return true;
    }
}
*/
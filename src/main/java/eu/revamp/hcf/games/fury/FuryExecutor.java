package eu.revamp.hcf.games.fury;

/*
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class FuryExecutor
extends CommandArgumentExecutor {
    public FuryExecutor(HardcoreFactionsPlugin plugin) {
        super("fury");
        this.addArgument(new FurySetpointsArgument(plugin));
    }

    @Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            CommandArgument argument = this.getArgument(args[0]);
            if (argument == null) {
                sender.sendMessage(HardcoreFactionsPlugin.getPlugin().getConfig().getString("Commands-Unknown-Subcommand").replace("{subCommand}", args[0]).replace("{commandLabel}", command.getName()));
                return true;
            }
            return super.onCommand(sender, command, label, args);
        }
        return super.onCommand(sender, command, label, args);
    }
}

*/
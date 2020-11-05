package eu.revamp.hcf.factions.commands.member;

import eu.revamp.hcf.commands.custom.DevCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionVersionCommand extends CommandArgument
{
    public FactionVersionCommand(RevampHCF plugin) {
        super("version", null);
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DevCommand.sendInfo(sender);
        return true;
    }
}

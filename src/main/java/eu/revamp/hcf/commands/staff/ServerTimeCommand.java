package eu.revamp.hcf.commands.staff;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.RevampHCF;
import java.util.TimeZone;
import org.apache.commons.lang.time.FastDateFormat;
import eu.revamp.hcf.commands.BaseCommand;

public class ServerTimeCommand extends BaseCommand
{
    private static FastDateFormat FORMAT = FastDateFormat.getInstance("E MMM dd h:mm:ssa z yyyy", TimeZone.getTimeZone("EST"));
    
    public ServerTimeCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "servertime";
        this.permission = "revamphcf.staff";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.YELLOW + "The server time is " + ChatColor.GOLD + ServerTimeCommand.FORMAT.format(System.currentTimeMillis()) + ChatColor.GRAY + " (EST)" + ChatColor.RED + '.');
    }
}

package eu.revamp.hcf.commands.staff;

import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;

public class StaffJoinCommand extends BaseCommand
{
    public StaffJoinCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "staffjoin";
        this.permission = "revamphcf.staff";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        final Player player = (Player)sender;
        if (args.length == 0) {
            this.sendUsage(sender);
            return;
        }
        this.getInstance().sendToServer(player, args[0]);
    }
    
    public void sendUsage(CommandSender sender) {
        sender.sendMessage(CC.translate("&cUsage: /staffjoin <serverName>"));
    }
}

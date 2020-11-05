package eu.revamp.hcf.commands.admin;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class SetReclaimCommand extends BaseCommand
{
    public SetReclaimCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "setreclaim";
        this.permission = "revamphcf.op";
        this.forPlayerUseOnly = false;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            this.sendUsage(sender);
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
            return;
        }
        HCFPlayerData user = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(target);
        if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("t")) {
            user.setReclaimed(true);
            sender.sendMessage(CC.translate("&eSuccessfully set &f" + target.getDisplayName() + " &eto &ftrue."));
        }
        else if (args[1].equalsIgnoreCase("false") || args[1].equalsIgnoreCase("f")) {
            user.setReclaimed(false);
            sender.sendMessage(CC.translate("&eSuccessfully set &f" + target.getDisplayName() + " &eto &ffalse."));
        }
        else {
            this.sendUsage(sender);
        }
    }
    
    public void sendUsage(CommandSender sender) {
        sender.sendMessage(CC.translate("&cCorrect Usage: /setreclaim <player> <true|false>."));
    }
}

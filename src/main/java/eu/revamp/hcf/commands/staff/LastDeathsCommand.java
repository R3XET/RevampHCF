package eu.revamp.hcf.commands.staff;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class LastDeathsCommand extends BaseCommand
{
    public LastDeathsCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "lastdeaths";
        this.permission = "revamphcf.staff";
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
            }
            else {
                HCFPlayerData targetData = this.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(target);
                if (targetData.getLastDeaths().isEmpty()) {
                    sender.sendMessage(CC.translate("&c&l" + target.getName() + " &chasn't died yet."));
                    return;
                }
                sender.sendMessage("");
                sender.sendMessage(CC.translate("&a&lLast &f10 &a&lDeaths for Player &f" + target.getName()));
                for (String str : targetData.getLastDeaths()) {
                    sender.sendMessage(CC.translate("&8* &e" + str));
                }
                sender.sendMessage("");
            }
        }
        else {
            this.sendUsage(sender);
        }
    }
    
    public void sendUsage(CommandSender sender) {
        sender.sendMessage(CC.translate("&cCorrect Usage: /lastdeaths <playerName>."));
    }
}

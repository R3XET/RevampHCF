package eu.revamp.hcf.factions.commands.member;

import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.factions.FactionExecutor;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionCommand extends CommandArgument
{
    public FactionCommand(FactionExecutor executor) {
        super("help", "View help on how to use factions.");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            this.showPage(sender);
            return true;
        }
        this.showPage(sender);
        return true;
    }
    
    private void showPage(CommandSender sender) {
        sender.sendMessage(CC.translate("&7&m---------------------------------------"));
        sender.sendMessage(CC.translate("&9&lFaction Help"));
        sender.sendMessage(CC.translate("&7&m---------------------------------------"));
        sender.sendMessage(CC.translate("&9General Commands:"));
        sender.sendMessage(CC.translate("&e/f create <factionName> &7- Create a new faction"));
        sender.sendMessage(CC.translate("&e/f accept <factionName> &7- Accept a pending invitation"));
        sender.sendMessage(CC.translate("&e/f leave &7- Leave your current faction"));
        sender.sendMessage(CC.translate("&e/f home &7- Teleport to your faction home"));
        sender.sendMessage(CC.translate("&e/f stuck &7- Teleport out of enemy territory"));
        sender.sendMessage(CC.translate("&e/f depositlives <amount&7|&eall> &7- Deposit lives into your faction lives"));
        sender.sendMessage(CC.translate("&e/f deposit <amount&7|&eall> &7- Deposit money into your faction balance"));
        sender.sendMessage("");
        sender.sendMessage(CC.translate("&9Information Commands:"));
        sender.sendMessage(CC.translate("&e/f who <player&7|&efactionName] &7- Display faction information"));
        sender.sendMessage(CC.translate("&e/f uninvite <player> &7- Revoke an invitation"));
        sender.sendMessage(CC.translate("&e/f invites &7- List all open invitations"));
        sender.sendMessage(CC.translate("&e/f kick <player> &7- Kick a player from your faction"));
        sender.sendMessage(CC.translate("&e/f claim &7- Start a claim for your faction"));
        sender.sendMessage(CC.translate("&e/f subclaim &7- Show the subclaim help page"));
        sender.sendMessage(CC.translate("&e/f sethome &7- Set your faction's home at your current location"));
        sender.sendMessage(CC.translate("&e/f withdraw <amoun> &7- Withdraw money from your faction's balance"));
        sender.sendMessage(CC.translate("&e/f revive <playerName> &7- Revive players with faction lives"));
        sender.sendMessage(CC.translate("&e/f announcement [message here] &7- Set your faction's announcement"));
        sender.sendMessage("");
        sender.sendMessage(CC.translate("&9Leader Commands:"));
        sender.sendMessage(CC.translate("&e/f coleader <player> &7- Add co-leader"));
        sender.sendMessage(CC.translate("&e/f promote <player> &7- Add or remove a captain"));
        sender.sendMessage(CC.translate("&e/f unclaim &7- Unclaim land"));
        sender.sendMessage(CC.translate("&e/f rename <newName> &7- Rename your faction"));
        sender.sendMessage(CC.translate("&e/f disband &7- Disband your faction"));
        if (sender.hasPermission("revamphcf.op")) {
            sender.sendMessage("");
            sender.sendMessage(CC.translate("&9Staff Commands:"));
            sender.sendMessage(CC.translate("&e/f forcedemote <player> &7- Force demote player"));
            sender.sendMessage(CC.translate("&e/f givebal [player&7|&efactionName] [balance] &7- Gives balance to a player's faction"));
            sender.sendMessage(CC.translate("&e/f setlives [player&7|&efactionName] [lives] &7- Gives lives to a player's faction"));
            sender.sendMessage(CC.translate("&e/f forcejoin [player&7|&efactionName] &7- Force joins player factions"));
            sender.sendMessage(CC.translate("&e/f forcekick <player> &7- Force kick player from faction"));
            sender.sendMessage(CC.translate("&e/f forcepromote <player> &7- Force promote player from faction"));
            sender.sendMessage(CC.translate("&e/f setdtr [player&7|&efactionName] &7- Set dtr to player faction"));
            sender.sendMessage(CC.translate("&e/f setdtrregen [player&7|&efactionName] &7- Set dtrregen to player faction"));
        }
        sender.sendMessage(CC.translate("&7&m---------------------------------------"));
    }
}

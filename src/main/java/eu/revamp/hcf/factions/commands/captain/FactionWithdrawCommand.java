package eu.revamp.hcf.factions.commands.captain;

import java.util.Collections;
import java.util.List;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.hcf.factions.utils.FactionMember;
import java.util.UUID;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.hcf.utils.chat.JavaUtils;
import eu.revamp.hcf.factions.utils.struction.Role;
import eu.revamp.spigot.utils.generic.ConversionUtils;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import com.google.common.collect.ImmutableList;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.command.CommandArgument;

@SuppressWarnings("unchecked")
public class FactionWithdrawCommand extends CommandArgument
{
    private final RevampHCF plugin;
    private static final ImmutableList<String> COMPLETIONS  = ImmutableList.of("all");

    public FactionWithdrawCommand(RevampHCF plugin) {
        super("withdraw", "Withdraws money from the faction balance.", new String[] { "w" });
        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <all|amount>";
    }
    
    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
            return true;
        }
        Player player = (Player)sender;
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
            return true;
        }
        UUID uuid = player.getUniqueId();
        FactionMember factionMember = playerFaction.getMember(uuid);
        if (factionMember.getRole() == Role.MEMBER) {
            sender.sendMessage(CC.translate("&cYour cannot do this with this role."));
            return true;
        }
        int factionBalance = playerFaction.getBalance();
        int amount;
        if (args[1].equalsIgnoreCase("all")) {
            amount = factionBalance;
        }
       else if (!ConversionUtils.isInteger(args[1])){
            sender.sendMessage(CC.translate("&cInvalid money."));
            return true;
        }
        amount = Integer.parseInt(args[1]);
        if (amount <= 0) {
            sender.sendMessage(CC.translate("&cAmount must be positive."));
            return true;
        }
        if (amount > factionBalance) {
            sender.sendMessage(CC.translate("&cThe faction doesn't have enough money to do this."));
            return true;
        }
        HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        data.addBalance(amount);
        playerFaction.setBalance(factionBalance - amount);
        playerFaction.broadcast(CC.translate("&d" + sender.getName() + " &ehas withdrew &d$" + JavaUtils.format(amount) + " &efrom the faction balance."));
        sender.sendMessage(CC.translate("&eYou have withdrawn &d$" + JavaUtils.format(amount) + " &efrom the faction balance."));
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return (List<String>)((args.length == 2) ? FactionWithdrawCommand.COMPLETIONS : Collections.emptyList());
    }
}

package eu.revamp.hcf.factions.commands.member;

import java.util.Collections;
import java.util.List;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.utils.struction.Relation;
import eu.revamp.hcf.utils.chat.JavaUtils;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.generic.ConversionUtils;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import com.google.common.collect.ImmutableList;
import eu.revamp.hcf.utils.command.CommandArgument;

@SuppressWarnings("unchecked")
public class FactionDepositCommand extends CommandArgument
{
    private final RevampHCF plugin;
    private static final ImmutableList<String> COMPLETIONS = ImmutableList.of("all");

    public FactionDepositCommand(RevampHCF plugin) {
        super("deposit", "Deposits money to the faction balance.", new String[] { "d" });
        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <all|amount>";
    }
    
    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (args.length <= 1) {
            sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
            return true;
        }
        if (playerFaction == null) {
            sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
            return true;
        }
        HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        int amount;
        if (args[1].equalsIgnoreCase("all")) {
            amount = data.getBalance();
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
        if (data.getBalance() < amount) {
            sender.sendMessage(CC.translate("&cYou need at least &l$" + JavaUtils.format(amount) + " &cto do this, you only have &l" + "$" + JavaUtils.format(data.getBalance())));
            return true;
        }
        data.setBalance(data.getBalance() - amount);
        playerFaction.setBalance(playerFaction.getBalance() + amount);
        playerFaction.broadcast(CC.translate(Relation.MEMBER.toChatColour() + playerFaction.getMember(player).getRole().getAstrix() + sender.getName() + " &ehas deposited &f" + "$" + JavaUtils.format(amount) + " &einto the faction balance."));
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return (List<String>)((args.length == 2) ? FactionDepositCommand.COMPLETIONS : Collections.emptyList());
    }
}

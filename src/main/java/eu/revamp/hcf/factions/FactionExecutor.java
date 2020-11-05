package eu.revamp.hcf.factions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.factions.commands.captain.FactionWithdrawCommand;
import eu.revamp.hcf.factions.commands.captain.FactionUninviteCommand;
import eu.revamp.hcf.factions.commands.captain.FactionUnallyCommand;
import eu.revamp.hcf.factions.commands.member.FactionUnclaimCommand;
import eu.revamp.hcf.factions.commands.member.FactionStuckCommand;
import eu.revamp.hcf.factions.commands.member.FactionShowCommand;
import eu.revamp.hcf.factions.commands.leader.FactionOpenCommand;
import eu.revamp.hcf.factions.commands.captain.FactionSetHQCommand;
import eu.revamp.hcf.factions.commands.staff.FactionSetDTRCommand;
import eu.revamp.hcf.factions.commands.captain.FactionReviveCommand;
import eu.revamp.hcf.factions.commands.captain.FactionPromoteCommand;
import eu.revamp.hcf.factions.commands.captain.FactionTagCommand;
import eu.revamp.hcf.factions.commands.staff.FactionRemoveCommand;
import eu.revamp.hcf.factions.commands.member.FactionMessageCommand;
import eu.revamp.hcf.factions.commands.member.FactionMapCommand;
import eu.revamp.hcf.factions.commands.member.FactionLivesDepositCommand;
import eu.revamp.hcf.factions.commands.member.FactionListCommand;
import eu.revamp.hcf.factions.commands.member.FactionLeaveCommand;
import eu.revamp.hcf.factions.commands.leader.FactionLeaderCommand;
import eu.revamp.hcf.factions.commands.captain.FactionKickCommand;
import eu.revamp.hcf.factions.commands.captain.FactionInvitesCommand;
import eu.revamp.hcf.factions.commands.captain.FactionInviteCommand;
import eu.revamp.hcf.factions.commands.member.FactionHQCommand;
import eu.revamp.hcf.factions.commands.member.FactionCommand;
import eu.revamp.hcf.factions.commands.staff.FactionForcePromoteCommand;
import eu.revamp.hcf.factions.commands.staff.FactionRemovePointsCommand;
import eu.revamp.hcf.factions.commands.staff.FactionAddPointsCommand;
import eu.revamp.hcf.factions.commands.staff.FactionSetPointsCommand;
import eu.revamp.hcf.factions.commands.staff.FactionSetLivesCommand;
import eu.revamp.hcf.factions.commands.staff.FactionSetBalanceCommand;
import eu.revamp.hcf.factions.commands.staff.FactionForceLeaderCommand;
import eu.revamp.hcf.factions.commands.staff.FactionForceKickCommand;
import eu.revamp.hcf.factions.commands.staff.FactionForceJoinCommand;
import eu.revamp.hcf.factions.commands.staff.FactionForceDemoteCommand;
import eu.revamp.hcf.factions.commands.staff.FactionSetDTRRegenCommand;
import eu.revamp.hcf.factions.commands.leader.FactionDisbandCommand;
import eu.revamp.hcf.factions.commands.member.FactionDepositCommand;
import eu.revamp.hcf.factions.commands.leader.FactionDemoteCommand;
import eu.revamp.hcf.factions.commands.member.FactionCreateCommand;
import eu.revamp.hcf.factions.commands.staff.FactionClearclaimsCommand;
import eu.revamp.hcf.factions.commands.captain.FactionClaimsCommand;
import eu.revamp.hcf.factions.commands.staff.FactionClaimforCommand;
import eu.revamp.hcf.factions.commands.captain.FactionClaimCommand;
import eu.revamp.hcf.factions.commands.leader.FactionCoLeaderArgument;
import eu.revamp.hcf.factions.commands.member.FactionChatCommand;
import eu.revamp.hcf.factions.commands.captain.FactionAnnouncementCommand;
import eu.revamp.hcf.factions.commands.captain.FactionAllyCommand;
import eu.revamp.hcf.factions.commands.member.FactionVersionCommand;
import eu.revamp.hcf.factions.commands.member.FactionAcceptCommand;
import eu.revamp.hcf.factions.commands.captain.FactionClaimChunkCommand;
import eu.revamp.hcf.factions.commands.staff.FactionSetMultiplierCommand;
import eu.revamp.hcf.factions.commands.staff.FactionTphereCommand;
import eu.revamp.hcf.factions.commands.staff.FactionUnfreezeCommand;
import eu.revamp.hcf.factions.commands.staff.FactionFreezeCommand;
import eu.revamp.hcf.factions.commands.staff.FactionTeleportCommand;
import eu.revamp.hcf.factions.commands.member.FactionPointsCommand;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.hcf.utils.command.ArgumentExecutor;

public class FactionExecutor extends ArgumentExecutor
{
    private final CommandArgument helpArgument;
    
    public FactionExecutor(RevampHCF plugin) {
        super("faction");
        this.addArgument(new FactionPointsCommand(plugin));
        this.addArgument(new FactionTeleportCommand(plugin));
        this.addArgument(new FactionFreezeCommand(plugin));
        this.addArgument(new FactionUnfreezeCommand(plugin));
        this.addArgument(new FactionTphereCommand(plugin));
        this.addArgument(new FactionSetMultiplierCommand(plugin));
        this.addArgument(new FactionClaimChunkCommand(plugin));
        this.addArgument(new FactionAcceptCommand(plugin));
        this.addArgument(new FactionVersionCommand(plugin));
        this.addArgument(new FactionAllyCommand(plugin));
        this.addArgument(new FactionAnnouncementCommand(plugin));
        this.addArgument(new FactionChatCommand(plugin));
        this.addArgument(new FactionCoLeaderArgument(plugin));
        this.addArgument(new FactionClaimCommand(plugin));
        this.addArgument(new FactionClaimforCommand(plugin));
        this.addArgument(new FactionClaimsCommand(plugin));
        this.addArgument(new FactionClearclaimsCommand(plugin));
        this.addArgument(new FactionCreateCommand(plugin));
        this.addArgument(new FactionDemoteCommand(plugin));
        this.addArgument(new FactionDepositCommand(plugin));
        this.addArgument(new FactionDisbandCommand(plugin));
        this.addArgument(new FactionSetDTRRegenCommand(plugin));
        this.addArgument(new FactionForceDemoteCommand(plugin));
        this.addArgument(new FactionForceJoinCommand(plugin));
        this.addArgument(new FactionForceKickCommand(plugin));
        this.addArgument(new FactionForceLeaderCommand(plugin));
        this.addArgument(new FactionSetBalanceCommand(plugin));
        this.addArgument(new FactionSetLivesCommand(plugin));
        this.addArgument(new FactionSetPointsCommand(plugin));
        this.addArgument(new FactionAddPointsCommand(plugin));
        this.addArgument(new FactionRemovePointsCommand(plugin));
        this.addArgument(new FactionForcePromoteCommand(plugin));
        this.addArgument(this.helpArgument = new FactionCommand(this));
        this.addArgument(new FactionHQCommand(this, plugin));
        this.addArgument(new FactionInviteCommand(plugin));
        this.addArgument(new FactionInvitesCommand(plugin));
        this.addArgument(new FactionKickCommand(plugin));
        this.addArgument(new FactionLeaderCommand(plugin));
        this.addArgument(new FactionLeaveCommand(plugin));
        this.addArgument(new FactionListCommand(plugin));
        this.addArgument(new FactionLivesDepositCommand(plugin));
        this.addArgument(new FactionMapCommand(plugin));
        this.addArgument(new FactionMessageCommand(plugin));
        this.addArgument(new FactionRemoveCommand(plugin));
        this.addArgument(new FactionTagCommand(plugin));
        this.addArgument(new FactionPromoteCommand(plugin));
        this.addArgument(new FactionReviveCommand(plugin));
        this.addArgument(new FactionSetDTRCommand(plugin));
        this.addArgument(new FactionSetHQCommand(plugin));
        this.addArgument(new FactionOpenCommand(plugin));
        this.addArgument(new FactionShowCommand(plugin));
        this.addArgument(new FactionStuckCommand(plugin));
        this.addArgument(new FactionUnclaimCommand(plugin));
        this.addArgument(new FactionUnallyCommand(plugin));
        this.addArgument(new FactionUninviteCommand(plugin));
        this.addArgument(new FactionWithdrawCommand(plugin));
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            this.helpArgument.onCommand(sender, command, label, args);
            return true;
        }
        CommandArgument argument = this.getArgument(args[0]);
        if (argument != null) {
            String permission = argument.getPermission();
            if (permission == null || sender.hasPermission(permission)) {
                argument.onCommand(sender, command, label, args);
                return true;
            }
        }
        this.helpArgument.onCommand(sender, command, label, args);
        return true;
    }
}

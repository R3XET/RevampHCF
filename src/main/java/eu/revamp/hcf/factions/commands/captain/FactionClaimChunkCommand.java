package eu.revamp.hcf.factions.commands.captain;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.Location;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import eu.revamp.hcf.factions.utils.struction.Role;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionClaimChunkCommand extends CommandArgument
{
    private RevampHCF plugin;
    
    public FactionClaimChunkCommand(RevampHCF plugin) {
        super("claimchunk", "Claim a chunk of land in the Wilderness.", new String[] { "chunkclaim" });
        this.plugin = plugin;
    }
    
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName();
    }
    
    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        Player player = (Player)sender;
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
            return true;
        }
        if (playerFaction.isRaidable()) {
            sender.sendMessage(ChatColor.RED + "You cannot claim land for your faction while raidable.");
            return true;
        }
        if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
            sender.sendMessage(ChatColor.RED + "You must be an officer to claim land.");
            return true;
        }
        Location location = player.getLocation();
        this.plugin.getHandlerManager().getClaimHandler().tryPurchasing(player, new ClaimZone(playerFaction, location.clone().add(7.0, 0.0, 7.0), location.clone().add(-7.0, 256.0, -7.0)));
        return true;
    }
}

package eu.revamp.hcf.factions.commands.captain;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import eu.revamp.hcf.factions.type.WildernessFaction;
import eu.revamp.hcf.handlers.claim.ClaimHandler;
import eu.revamp.hcf.factions.type.WarzoneFaction;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionClaimCommand extends CommandArgument
{
    public FactionClaimCommand(RevampHCF plugin) {
        super("claim", "Start a claim for your faction");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <wand:chunk>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        Location location = player.getLocation();
        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player.getUniqueId());
        if (!(RevampHCF.getInstance().getFactionManager().getFactionAt(location) instanceof WarzoneFaction)) {
            if (playerFaction == null) {
                sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
                return true;
            }
            if (playerFaction.isRaidable()) {
                sender.sendMessage(CC.translate("&cYou cannot claim land for your faction while raidable."));
                return true;
            }
            if (player.getInventory().contains(ClaimHandler.claimWand)) {
                sender.sendMessage(CC.translate("&cYou already have a claiming wand in your inventory."));
                return true;
            }
            if (RevampHCF.getInstance().getFactionManager().getFactionAt(location) instanceof WildernessFaction && !player.getInventory().addItem(new ItemStack[] { ClaimHandler.claimWand }).isEmpty()) {
                sender.sendMessage(CC.translate("&cYour inventory is full."));
                return true;
            }
        }
        else if (!(RevampHCF.getInstance().getFactionManager().getFactionAt(location) instanceof WildernessFaction)) {
            sender.sendMessage(CC.translate("&cYou are currently in unclaimable zone. The Warzone ends at " + RevampHCF.getInstance().getConfig().getString("FACTIONS-SETTINGS.WARZONE-RADIUS") + "."));
        }
        return true;
    }
}

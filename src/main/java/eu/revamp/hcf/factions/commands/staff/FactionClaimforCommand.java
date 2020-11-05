package eu.revamp.hcf.factions.commands.staff;

import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import org.bukkit.entity.Player;
import eu.revamp.hcf.factions.type.ClaimableFaction;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionClaimforCommand extends CommandArgument
{
    private final RevampHCF plugin;
    
    public FactionClaimforCommand(RevampHCF plugin) {
        super("claimfor", "Claims land for another faction.");
        this.plugin = plugin;
        this.permission = "*";
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <factioName> [shouldClearClaims]";
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
            return true;
        }
        Faction targetFaction = this.plugin.getFactionManager().getFaction(args[1]);
        if (!(targetFaction instanceof ClaimableFaction)) {
            sender.sendMessage(CC.translate("&cClaimable faction named " + args[1] + " not found."));
            return true;
        }
        Player player = (Player)sender;
        WorldEditPlugin worldEditPlugin = this.plugin.getWorldEdit();
        if (worldEditPlugin == null) {
            sender.sendMessage(CC.translate("&cYou must have WorldEdit to do this."));
            return true;
        }
        Selection selection = worldEditPlugin.getSelection(player);
        if (selection == null) {
            sender.sendMessage(CC.translate("&cYou must have WorldEdit to do this."));
            return true;
        }
        ClaimableFaction claimableFaction = (ClaimableFaction)targetFaction;
        if (claimableFaction.addClaim(new ClaimZone(claimableFaction, selection.getMinimumPoint(), selection.getMaximumPoint()), sender)) {
            sender.sendMessage(CC.translate("&eYou have successfully claimed this land for &f" + targetFaction.getName() + "&e."));
        }
        return true;
    }
}

package eu.revamp.hcf.factions.commands.staff;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.system.api.player.PlayerData;
import eu.revamp.system.plugin.RevampSystem;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionUnfreezeCommand extends CommandArgument
{
    private final RevampHCF plugin;
    
    public FactionUnfreezeCommand(RevampHCF plugin) {
        super("unfreeze", "unfreeze a faction");
        this.plugin = plugin;
        this.permission = "faction.unfreeze";
        this.isPlayerOnly = true;
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <factionName>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
            return true;
        }
        final Faction faction = RevampHCF.getInstance().getFactionManager().getContainingFaction(args[1]);
        if (faction == null) {
            player.sendMessage(Language.WARP_NO_WARPS.toString());
            return true;
        }
        if (!(faction instanceof PlayerFaction)) {
            sender.sendMessage(CC.translate("&cYou only can unfreeze player factions!"));
            return true;
        }
        for (Player online : ((PlayerFaction)faction).getOnlinePlayers()) {
            PlayerData targetProfile = RevampSystem.getINSTANCE().getPlayerManagement().getPlayerData(online.getUniqueId());
            targetProfile.setVanished(false);
        }
        player.sendMessage(CC.translate("&eYou have unfrozen &d" + faction.getName() + " &eFaction!"));
        return true;
    }
}

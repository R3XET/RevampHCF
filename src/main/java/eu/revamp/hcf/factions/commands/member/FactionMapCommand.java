package eu.revamp.hcf.factions.commands.member;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.hcf.factions.utils.LandMap;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.hcf.visualise.VisualType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class FactionMapCommand extends CommandArgument
{
    private final RevampHCF plugin;
    
    public FactionMapCommand(RevampHCF plugin) {
        super("map", "View all claims around your chunk.");
        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " [factionName]";
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        UUID uuid = player.getUniqueId();
        HCFPlayerData user = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(uuid);
        VisualType visualType;
        if (args.length <= 1) {
            visualType = VisualType.CLAIM_MAP;
        }
        else if ((visualType = Utils.getIfPresent(VisualType.class, args[1]).orElse(null)) == null) {
            player.sendMessage(Language.VISUALMAP_TYPE_NOT_FOUND.toString().replace("%type%", args[1]));
            return true;
        }
        boolean newShowingMap = !user.isShowClaimMap();
        if (newShowingMap) {
            if (!LandMap.updateMap(player, this.plugin, visualType, true)) {
                return true;
            }
        }
        else {
            this.plugin.getHandlerManager().getVisualiseHandler().clearVisualBlocks(player, visualType, null);
            sender.sendMessage(Language.VISUALMAP_HIDDEN.toString());
        }
        user.setShowClaimMap(newShowingMap);
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2 || !(sender instanceof Player)) {
            return Collections.emptyList();
        }
        VisualType[] values = VisualType.values();
        List<String> results = new ArrayList<>(values.length);
        VisualType[] array;
        for (int length = (array = values).length, i = 0; i < length; ++i) {
            VisualType visualType = array[i];
            results.add(visualType.name());
        }
        return results;
    }
}

package eu.revamp.hcf.commands.other;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.system.api.player.PlayerData;
import eu.revamp.system.plugin.RevampSystem;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ReclaimCommand extends BaseCommand
{
    public ReclaimCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "reclaim";
        this.permission = "revamphcf.reclaim";
        this.forPlayerUseOnly = true;
    }
    
    public List<String> getReclaim(String rank) {
        return RevampHCF.getInstance().getConfig().getStringList("Reclaims." + rank + ".commands");
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        HCFPlayerData user = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        PlayerData targetProfile = RevampSystem.getINSTANCE().getPlayerManagement().getPlayerData(player.getUniqueId());
        if (args.length != 0) return;
        if (user.isReclaimed()) {
            player.sendMessage(CC.translate("&cYou have already used reclaim this map!"));
            return;
        }
        String rank = targetProfile.getHighestRank().getDisplayName();
        user.setReclaimed(true);
        for (String command : this.getReclaim(rank)) {
            command = command.replace("%player%", player.getName()).replace("%rank%", rank);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            user.setReclaimed(true);
            player.sendMessage(CC.translate("&a&lYou have reclaimed your rank."));
        }
    }
}

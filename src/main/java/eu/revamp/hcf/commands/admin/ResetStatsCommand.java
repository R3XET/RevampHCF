package eu.revamp.hcf.commands.admin;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.spigot.utils.generic.Tasks;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class ResetStatsCommand extends BaseCommand
{
    public ResetStatsCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "resetstats";
        this.forPlayerUseOnly = false;
        this.permission = "*";
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Tasks.runAsync(this.getInstance(), () -> {
            if (args.length == 0) {
                sender.sendMessage(CC.translate("&cPlease specify a nickname!"));
            }
            else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
                    return;
                }
                if (!sender.hasPermission("*")) {
                    sender.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
                    return;
                }
                HCFPlayerData HCFPlayerData = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(target);
                HCFPlayerData.setKills(0);
                HCFPlayerData.setDeaths(0);
                if (this.getInstance().getConfiguration().isKitMap()) {
                    HCFPlayerData.setBalance(RevampHCF.getInstance().getConfig().getInt("STARTING_BALANCE.PLAYER"));
                }
                else {
                    HCFPlayerData.setBalance(RevampHCF.getInstance().getConfig().getInt("STARTING_BALANCE.PLAYER"));
                }
                HCFPlayerData.setEnderpearlCooldown(0L);
                HCFPlayerData.setGappleCooldown(0L);
                HCFPlayerData.setGoldenHeadCooldown(0L);
                if (this.getInstance().getConfiguration().isKitMap()) {
                    HCFPlayerData.setPvpTimerCooldown(0);
                }
                else {
                    HCFPlayerData.setPvpTimerCooldown(3600000);
                }
                HCFPlayerData.setSpawnTagCooldown(0L);
                HCFPlayerData.setShowClaimMap(Boolean.FALSE);
                HCFPlayerData.setShowLightning(true);
                HCFPlayerData.setReclaimed(Boolean.FALSE);
                HCFPlayerData.setCombatLogger(Boolean.FALSE);
                HCFPlayerData.setLastFactionLeaveMillis(0L);
                HCFPlayerData.setLastSeen(0L);
                HCFPlayerData.setLastDeaths(null);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kick " + HCFPlayerData.getName() + " -s &aYour statistics has been reset &7(Rejoin)");
                sender.sendMessage(CC.translate("&a" + HCFPlayerData.getName() + " &eStatistics has been restored!"));
            }
        });
    }
}

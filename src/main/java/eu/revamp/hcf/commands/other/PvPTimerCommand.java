package eu.revamp.hcf.commands.other;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.hcf.timers.PvPTimerHandler;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;

public class PvPTimerCommand extends BaseCommand
{
    public PvPTimerCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "pvptimer";
        this.permission = "revamphcf.command.pvptimer";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        PvPTimerHandler pvpTimer = RevampHCF.getInstance().getHandlerManager().getTimerManager().getPvpTimerHandler();
        if (args.length == 0 && !RevampHCF.getInstance().getConfiguration().isKitMap()) {
            this.sendUsage(sender);
            return;
        }
        if (RevampHCF.getInstance().getConfiguration().isKitMap()) {
            player.sendMessage(Language.COMMANDS_NO_KITMAP.toString());
        }
        else if (args[0].equalsIgnoreCase("enable")) {
            if (pvpTimer.getRemaining(player) <= 0L && data.getPvpTimerCooldown() <= 0) {
                sender.sendMessage(CC.translate("&cYour &a&lPvP Timer &cis currently not active."));
                return;
            }
            sender.sendMessage(CC.translate("&cYour &a&lPvP Timer &chas been disabled."));
            pvpTimer.clearCooldown(player);
            data.setPvpTimerCooldown(0);
        }
        else if (args[0].equalsIgnoreCase("grant")) {
            if (sender.hasPermission("revamphcf.op")) {
                if (args.length <= 1) {
                    sender.sendMessage(CC.translate("&cPlease specify name."));
                    return;
                }
                if (args[1] == null) {
                    sender.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
                    return;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (RevampHCF.getInstance().getFactionManager().getFactionAt(target.getLocation()).isSafezone()) {
                    RevampHCF.getInstance().getHandlerManager().getTimerManager().getPvpTimerHandler().setPaused(target.getUniqueId(), true);
                }

                HCFPlayerData tdata = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(target);
                tdata.setPvpTimerCooldown((int)this.getInstance().getHandlerManager().getTimerManager().getPvpTimerHandler().getTime());
                this.getInstance().getHandlerManager().getTimerManager().getPvpTimerHandler().setCooldown(target, target.getUniqueId(), this.getInstance().getHandlerManager().getTimerManager().getPvpTimerHandler().getTime(), true);
                sender.sendMessage(CC.translate("&aYou have successfully set &lPvP Timer&a to player &l" + target.getName() + "&a."));
                target.sendMessage(CC.translate("&a&l" + sender.getName() + " &ahas granted you &lPvP Timer&a."));
            }
            else {
                sender.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
            }
        }
        else if (args[0].equalsIgnoreCase("disable") || args[0].equalsIgnoreCase("off")) {
            if (sender.hasPermission("revamphcf.op")) {
                if (args.length <= 1) {
                    sender.sendMessage(CC.translate("&cPlease specify name."));
                    return;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
                    return;
                }
                HCFPlayerData tData = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(target);
                tData.setPvpTimerCooldown(0);
                RevampHCF.getInstance().getHandlerManager().getTimerManager().getPvpTimerHandler().clearCooldown(target);
                sender.sendMessage(CC.translate("&aYou have successfully disabled &lPvP Timer&a of player &l" + target.getName() + "&a."));
                target.sendMessage(CC.translate("&a&l" + sender.getName() + " &ahas disabled you &lPvP Timer&a."));
            }
            else {
                sender.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
            }
        }
    }
    
    private void sendUsage(CommandSender sender) {
        sender.sendMessage(CC.translate("&cPvP Protection - Help Commands"));
        sender.sendMessage(CC.translate("&c/pvp enable - Removes your pvp protection."));
        if (sender.hasPermission("revamphcf.op")) {
            sender.sendMessage("");
            sender.sendMessage(CC.translate("&cPvP Protection - Staff Commands"));
            sender.sendMessage(CC.translate("&c/pvp grant (playerName) - Gives player pvp protection."));
            sender.sendMessage(CC.translate("&c/pvp disable (playerName) - Disable pvp protection of a player."));
        }
    }
    
    private boolean checkNumber(String s) {
        try {
            Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}

package eu.revamp.hcf.factions.commands.member;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.timers.StuckHandler;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.World;
import eu.revamp.hcf.factions.type.SpawnFaction;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionStuckCommand extends CommandArgument
{
    public FactionStuckCommand(RevampHCF plugin) {
        super("stuck", "Teleport to a safe position.");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final Player player = (Player)sender;
        if (RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation()) instanceof SpawnFaction) {
            sender.sendMessage(CC.translate("&cYou cannot use this command at &aSpawn &cclaim."));
            return false;
        }
        if (player.getWorld().getEnvironment() != World.Environment.NORMAL) {
            sender.sendMessage(CC.translate("&cYou can only use this command from the overworld."));
            return true;
        }
        final StuckHandler stuckHandler = RevampHCF.getInstance().getHandlerManager().getTimerManager().getStuckHandler();
        if (stuckHandler.getRemaining(player) > 0L) {
            sender.sendMessage(CC.translate("&cYour Stuck timer is already active."));
            return true;
        }
        stuckHandler.setCooldown(player, player.getUniqueId());
        return true;
    }
}

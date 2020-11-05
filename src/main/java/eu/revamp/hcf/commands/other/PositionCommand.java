package eu.revamp.hcf.commands.other;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.managers.CooldownManager;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.hcf.utils.inventory.BukkitUtils;
import eu.revamp.spigot.utils.player.PlayerUtils;
import eu.revamp.spigot.utils.time.TimeUtils;
import eu.revamp.system.api.player.PlayerData;
import eu.revamp.system.plugin.RevampSystem;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class PositionCommand extends BaseCommand
{
    private static long posDelay = TimeUnit.MINUTES.toMillis(30L);

    public PositionCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "position";
        this.permission = "revamphcf.command.position";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        if (CooldownManager.isOnCooldown("POS_COMMAND_DELAY", player)) {
            player.sendMessage(ChatColor.RED + "You cannot use this command for another " + ChatColor.BOLD + TimeUtils.getRemaining(CooldownManager.getCooldownMillis("POS_COMMAND_DELAY", player), true) + ".");
            return;
        }
        if (args.length > 0) {
            Player target = PlayerUtils.playerWithNameOrUUID(args[0]);
            PlayerData targetProfile = RevampSystem.getINSTANCE().getPlayerManagement().getPlayerData(target.getUniqueId());
            if (!targetProfile.isVanished()) {
                Location pos = target.getLocation();
                sender.sendMessage(ChatColor.GOLD + "Player: " + target.getName());
                sender.sendMessage(ChatColor.YELLOW + "World: " + target.getWorld().getName());
                sender.sendMessage(ChatColor.YELLOW + String.format("Location: (%.3f, %.3f, %.3f)", pos.getX(), pos.getY(), pos.getZ()));
                sender.sendMessage(CC.translate(""));
                if (!CooldownManager.getCooldowns().containsKey("POS_COMMAND_DELAY")) {
                    CooldownManager.createCooldown("POS_COMMAND_DELAY");
                }
                CooldownManager.addCooldown("POS_COMMAND_DELAY", player, posDelay);
                return;
            }
        }
        sender.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
    }
}

package eu.revamp.hcf.commands.inventory;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.Bukkit;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class EnderchestCommand extends BaseCommand
{
    public EnderchestCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "enderchest";
        this.permission = "revamphcf.command.enderchest";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        if (args.length == 0) {
            if (!RevampHCF.getInstance().getConfig().getBoolean("ENDERCHEST_COMMAND_IN_COMBAT_ENABLED") && !RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().hasCooldown(player)) {
                player.openInventory(player.getEnderChest());
            }
            else if (!RevampHCF.getInstance().getConfig().getBoolean("ENDERCHEST_COMMAND_IN_COMBAT_ENABLED") && RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().hasCooldown(player)) {
                player.sendMessage(CC.translate("&cYou can't use enderchest under combat!"));
            }
            else if (RevampHCF.getInstance().getConfig().getBoolean("ENDERCHEST_COMMAND_IN_COMBAT_ENABLED")) {
                player.openInventory(player.getEnderChest());
            }
        }
        else {
            if (!player.hasPermission("revamphcf.op")) {
                player.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
                return;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString().replace("%player%", args[0]));
                return;
            }
            player.openInventory(target.getEnderChest());
        }
    }
}

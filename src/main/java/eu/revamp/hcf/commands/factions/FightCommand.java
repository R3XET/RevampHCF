package eu.revamp.hcf.commands.factions;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.managers.CooldownManager;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.hcf.factions.type.SpawnFaction;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.time.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class FightCommand extends BaseCommand
{
    private static long delay = TimeUnit.HOURS.toMillis(1L);

    public FightCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "fight";
        this.permission = "revamphcf.command.fight";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();
        if (RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation()) instanceof SpawnFaction) {
            sender.sendMessage(CC.translate("&cYou cannot use this command at &aSpawn &cclaim."));
            return;
        }
        if (player.getWorld().getEnvironment() != World.Environment.NORMAL) {
            sender.sendMessage(CC.translate("&cYou can only use this command from the overworld."));
            return;
        }
        if (RevampHCF.getInstance().getConfiguration().isKitMap()) {
            sender.sendMessage(Language.COMMANDS_NO_KITMAP.toString());
            return;
        }
        if (args.length == 0) {
            if (CooldownManager.isOnCooldown("FIGHT_COMMAND_DELAY", player)) {
                player.sendMessage(CC.translate(ChatColor.RED + "You cannot use this command for another " + TimeUtils.getRemaining(CooldownManager.getCooldownMillis("FIGHT_COMMAND_DELAY", player), true) + "."));
                return;
            }
            if (!CooldownManager.getCooldowns().containsKey("FIGHT_COMMAND_DELAY")) {
                CooldownManager.createCooldown("FIGHT_COMMAND_DELAY");
            }
            CooldownManager.addCooldown("FIGHT_COMMAND_DELAY", player, delay);
            Bukkit.broadcastMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "-----------------------------------");
            Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + sender.getName() + ChatColor.GRAY + " want fight at " + x + ", " + y + ", " + z);
            Bukkit.broadcastMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "-----------------------------------");
            player.sendMessage(ChatColor.GRAY + "You have announced that you are looking for a fight, you must wait 1 Hour before doing this again.");
        }
    }
}

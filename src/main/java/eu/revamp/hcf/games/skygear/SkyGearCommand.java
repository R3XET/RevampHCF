package eu.revamp.hcf.games.skygear;

import eu.revamp.hcf.games.skygear.handlers.SkyGearHandler;
import eu.revamp.hcf.utils.inventory.BukkitUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkyGearCommand implements CommandExecutor {
    SkyGearHandler scrollsHandler = new SkyGearHandler();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            return true;
        }

        Player player = (Player) sender;
        if ((cmd.getName().equalsIgnoreCase("skygear")) &&
                (args.length == 0) && sender.hasPermission("hcf.command.skygear")) {
            sender.sendMessage(ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
            sender.sendMessage("�3�lSkyGear �8- �7(Page 1/1)");
            sender.sendMessage(ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
            sender.sendMessage("�3/skygear <player> <type> �8- �7Give SkyGear to a player.");
            sender.sendMessage("�3/skygear list �8- �7List all SkyGears.");
            sender.sendMessage(ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
            return true;
        }
        if (args[0].equalsIgnoreCase("list") && sender.hasPermission("hcf.command.skygear.list")) {

            player.sendMessage(ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
            player.sendMessage("�3�lSkyGear List �8- �7(Page 1/1)");
            player.sendMessage(ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
            player.sendMessage((player.hasPermission("hcf.skygear.spawntoken") ? "�aSpawnToken" : "�cSpawnToken") + "�7, " + (player.hasPermission("hcf.skygear.endportaltoken") ? "�aEndPortalToken" : "�cEndPortalToken"));
            player.sendMessage(ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
            //+ "�7, " + (player.hasPermission("hcf.skygear.rank1token") ? ChatColor.GREEN + HardcoreFactions.getInstance().getConfig().getString("skygear.rank1token.name").replaceAll("�", "").replaceAll("�", "").replaceAll(" ", "") : ChatColor.RED + HardcoreFactions.getInstance().getConfig().getString("skygear.rank1token.name").replaceAll("�", "").replaceAll("�", "").replaceAll(" ", "")) + "�7, " + (player.hasPermission("hcf.skygear.rank2token") ? ChatColor.GREEN + HardcoreFactions.getInstance().getConfig().getString("skygear.rank2token.name").replaceAll("�", "").replaceAll("�", "").replaceAll(" ", "") : ChatColor.RED + HardcoreFactions.getInstance().getConfig().getString("skygear.rank2token.name").replaceAll("�", "").replaceAll("�", "").replaceAll(" ", ""))
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + args[0] + " is not online.");
            return true;
        }

        if (args[1].equalsIgnoreCase("spawntoken") && player.hasPermission("hcf.command.skygear.give")) {
            this.scrollsHandler.giveSpawnToken(target, "�3You have received one SkyGear �b(SpawnToken)�3.");
            return true;
        }
        if (args[1].equalsIgnoreCase("endportaltoken") && player.hasPermission("hcf.command.skygear.give")) {
            this.scrollsHandler.giveEndPortalToken(target, "�3You have received one SkyGear �b(EndPortalToken)�3.");
            return true;
        }
        return false;
    }
}

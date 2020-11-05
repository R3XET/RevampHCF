package eu.revamp.hcf.commands.games;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.hcf.utils.inventory.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class KingCommand extends BaseCommand {
	public static ArrayList<UUID> play = new ArrayList<>();
	public static Player player;
	public static String kingName = "";
	public static String kingPrize = "";

	public KingCommand(RevampHCF plugin) {
		super(plugin);
		this.command = "kingevent";
		this.permission = "*";
		this.forPlayerUseOnly = false;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0 || args.length > 3) {
			sender.sendMessage(CC.translate("&7&m--------------------------------"));
			sender.sendMessage(CC.translate("&4&lKing Event"));
			sender.sendMessage(CC.translate(""));
			sender.sendMessage(CC.translate("&4/kingevent start <player>&7 - &fStarts the King Event."));
			sender.sendMessage(CC.translate("&4/kingevent end &7- &fEnds the current king event."));
			sender.sendMessage(CC.translate("&4/kingevent prize <prize> &7- &fSets the prize for the current king event."));
			sender.sendMessage(CC.translate("&7&m--------------------------------"));
			return;
		}
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("end")) {
				KingCommand.player = null;
				return;
			}
			sender.sendMessage(ChatColor.RED + "Unknown sub-command!");
		} else {
			switch (args[0].toLowerCase()) {
				case "prize":
					KingCommand.kingPrize = args[1].replaceAll("_", " ");
					sender.sendMessage(ChatColor.GREEN + "You have successfully set the prize to " + KingCommand.kingPrize);
					return;
				case "start":
					sender.sendMessage(ChatColor.RED + "Unknown sub-command!");
					return;
			}
			if (Bukkit.getPlayer(args[1]) == null) {
				sender.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
				return;
			}
			Player p = Bukkit.getPlayer(args[1]);
			KingCommand.kingName = p.getName();
			Player player1 = Bukkit.getPlayer(KingCommand.kingName);
			sender.sendMessage(ChatColor.GREEN + "You have successfully started the king event!");
			Bukkit.broadcastMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + BukkitUtils.STRAIGHT_LINE_DEFAULT);
			Bukkit.broadcastMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH);
			Bukkit.broadcastMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "King Event");
			Bukkit.broadcastMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH);
			Bukkit.broadcastMessage(ChatColor.GOLD + " »&e King" + ChatColor.GRAY + ": " + ChatColor.WHITE + KingCommand.kingName);
			Bukkit.broadcastMessage(ChatColor.GOLD + " »&e Location" + ChatColor.GRAY + ": " + ChatColor.WHITE + "x" + player1.getLocation().getBlockX() + ", y" + player1.getLocation().getBlockY() + ", z" + player1.getLocation().getBlockZ());
			Bukkit.broadcastMessage(ChatColor.GOLD + " »&e Starting Health" + ChatColor.GRAY + ": " + ChatColor.WHITE + player1.getHealthScale());
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + BukkitUtils.STRAIGHT_LINE_DEFAULT);
			KingCommand.player = p;
		}
	}
}

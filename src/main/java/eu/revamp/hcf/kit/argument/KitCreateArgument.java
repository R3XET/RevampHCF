package eu.revamp.hcf.kit.argument;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.kit.Kit;
import eu.revamp.hcf.kit.event.KitCreateEvent;
import eu.revamp.hcf.utils.chat.JavaUtils;
import eu.revamp.hcf.utils.command.CommandArgument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class KitCreateArgument extends CommandArgument {
    private RevampHCF plugin;

    public KitCreateArgument(RevampHCF plugin) {
        super("create", "Creates a kit");
        this.plugin = plugin;
        this.aliases = new String[]{"make", "build"};
        this.permission = "manager.kit.argument." + this.getName();
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <kitName> [kitDescription]";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players may create kits.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        if (!JavaUtils.isAlphanumeric(args[1])) {
            sender.sendMessage(ChatColor.GRAY + "Kit names may only be alphanumeric.");
            return true;
        }
        Kit kit = this.plugin.getKitManager().getKit(args[1]);
        if (kit != null) {
            sender.sendMessage(ChatColor.RED + "There is already a kit named " + args[1] + '.');
            return true;
        }
        Player player = (Player) sender;
        kit = new Kit(args[1], (args.length >= 3) ? args[2] : null, player.getInventory());
        KitCreateEvent event = new KitCreateEvent(kit);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return true;
        }
        this.plugin.getKitManager().createKit(kit);
        sender.sendMessage(ChatColor.GRAY + "Created kit '" + kit.getDisplayName() + "'.");
        return true;
    }
}

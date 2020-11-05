package eu.revamp.hcf.commands.inventory;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.generic.Tasks;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class GiveCommand extends BaseCommand
{
    public GiveCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "give";
        this.permission = "revamphcf.command.give";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        /*
        Tasks.runAsync(this.getInstance(), () -> {
            Player player = (Player)sender;
            String amount = "";
            if (args.length == 0) {
                this.sendUsage(sender);
                return;
            }
            if (RevampHCF.getInstance().getItemDB().getItem(args[0]) == null) {
                sender.sendMessage(CC.translate("&cItem or ID not found."));
                return;
            }
            if (args.length == 1) {
                if (!player.getInventory().addItem(new ItemStack[] { RevampHCF.getInstance().getItemDB().getItem(args[0], RevampHCF.getInstance().getItemDB().getItem(args[0]).getMaxStackSize()) }).isEmpty()) {
                    player.sendMessage(CC.translate("&cYour inventory is full."));
                    return;
                }
                amount = String.valueOf(RevampHCF.getInstance().getItemDB().getItem(args[0]).getMaxStackSize());
            }
            if (args.length == 2) {
                if (!player.getInventory().addItem(new ItemStack[] { RevampHCF.getInstance().getItemDB().getItem(args[0], Integer.parseInt(args[1])) }).isEmpty()) {
                    player.sendMessage(CC.translate("&cYour inventory is full."));
                    return;
                }
                amount = args[1];
            }
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (online.hasPermission("revamphcf.op")) {
                    online.sendMessage(CC.translate("&8[&bGive Command&8] &f" + player.getName() + " &bgave himself &f" + amount + ", " + RevampHCF.getInstance().getItemDB().getName(RevampHCF.getInstance().getItemDB().getItem(args[0]))));
                }
            }
        });*/
        final Player p = (Player) sender;
        if (args.length == 0) {
            this.sendUsage(sender);
            return;
        }
        if (this.instance.getItemDB().getItem(args[0]) == null) {
            sender.sendMessage(ChatColor.GOLD + "Item named or with ID '" + ChatColor.RESET + args[0] + ChatColor.GOLD
                    + "' not found.");
            return;
        }
        if (args.length == 1) {
            if (!p.getInventory().addItem(new ItemStack[]{this.instance.getItemDB().getItem(args[0],
                    this.instance.getItemDB().getItem(args[0]).getMaxStackSize())}).isEmpty()) {
                p.sendMessage(ChatColor.RED + "Your inventory is full.");
                return;
            }
            for (final Player on : Bukkit.getOnlinePlayers()) {
                if (on.hasPermission("revamphcf.command.give")) {
                    if (on != p) {
                        on.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + p.getName() + ChatColor.YELLOW
                                + " has given himself " + ChatColor.GRAY
                                + this.instance.getItemDB().getItem(args[0]).getMaxStackSize() + ", "
                                + this.instance.getItemDB()
                                .getName(this.instance.getItemDB().getItem(args[0]))
                                + "]");
                    } else {
                        on.sendMessage(ChatColor.GOLD + "You gave yourself "
                                + this.instance.getItemDB().getItem(args[0]).getMaxStackSize() + ", "
                                + this.instance.getItemDB()
                                .getName(this.instance.getItemDB().getItem(args[0])));
                    }
                }
            }
        }
        if (args.length == 2) {
            if (!p.getInventory().addItem(
                    new ItemStack[]{this.instance.getItemDB().getItem(args[0], Integer.parseInt(args[1]))})
                    .isEmpty()) {
                p.sendMessage(ChatColor.RED + "Your inventory is full.");
                return;
            }
            for (final Player on : Bukkit.getOnlinePlayers()) {
                if (on.hasPermission("revamphcf.command.give")) {
                    if (on != p) {
                        on.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + p.getName() + ChatColor.YELLOW
                                + " has given himself" + ChatColor.GRAY + " " + args[1] + ", " + this.instance
                                .getItemDB().getName(this.instance.getItemDB().getItem(args[0]))
                                + " ]");
                    } else {
                        on.sendMessage(ChatColor.GOLD + "You gave yourself " + args[1] + ", " + this.instance
                                .getItemDB().getName(this.instance.getItemDB().getItem(args[0])));
                    }
                }
            }
        }
    }
    
    public void sendUsage(CommandSender sender) {
        sender.sendMessage(CC.translate("&cCorrect Usage: /give (item) (amount)."));
    }
}

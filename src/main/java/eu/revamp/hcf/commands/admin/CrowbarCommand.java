package eu.revamp.hcf.commands.admin;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.hcf.utils.inventory.Crowbar;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CrowbarCommand extends BaseCommand
{
    public CrowbarCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "crowbar";
        this.permission = "revamphcf.admin";
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        if (args.length == 0) {
            ItemStack stack = new Crowbar().getItemIfPresent();
            player.getInventory().addItem(stack);
            player.sendMessage(CC.translate("&eYou have received a crowbar!"));
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
            return;
        }
        ItemStack stack2 = new Crowbar().getItemIfPresent();
        target.getInventory().addItem(stack2);
        target.sendMessage(CC.translate("&eYou have received a crowbar!"));
        sender.sendMessage(CC.translate("&eYou have given &f" + target.getName() + " &ea crowbar!"));
    }
}

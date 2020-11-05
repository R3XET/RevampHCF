package eu.revamp.hcf.commands.inventory;

import eu.revamp.spigot.utils.generic.Tasks;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;

public class HatCommand extends BaseCommand
{
    public HatCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "hat";
        this.permission = "revamphcf.command.hat";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Tasks.runAsync(this.getInstance(), () -> {
            Player player = (Player)sender;
            ItemStack stack = player.getItemInHand();
            if (stack == null || stack.getType() == Material.AIR) {
                sender.sendMessage(ChatColor.RED + "You need to hold a block!");
                return;
            }
            if (stack.getType().getMaxDurability() != 0) {
                sender.sendMessage(ChatColor.RED + "The item you are holding cannot be used like a hat.");
                return;
            }
            PlayerInventory inventory = player.getInventory();
            ItemStack helmet = inventory.getHelmet();
            if (helmet != null && helmet.getType() != Material.AIR) {
                sender.sendMessage(ChatColor.RED + "You are already wearing something as hat.");
                return;
            }
            int amount = stack.getAmount();
            if (amount > 1) {
                --amount;
                stack.setAmount(amount);
            }
            else {
                player.setItemInHand(new ItemStack(Material.AIR, 1));
            }
            helmet = stack.clone();
            helmet.setAmount(1);
            inventory.setHelmet(helmet);
        });
    }
}

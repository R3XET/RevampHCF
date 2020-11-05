package eu.revamp.hcf.commands.admin;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.serialize.BukkitSerilization;
import org.bukkit.inventory.ItemStack;
import eu.revamp.spigot.utils.chat.color.CC;
import java.io.IOException;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class FirstJoinItemsCommand extends BaseCommand
{
    public FirstJoinItemsCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "firstjoinitems";
        this.permission = "*";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        if (args.length == 0) {
            this.sendUsage(player);
        }
        else if (args.length == 1) {
            if (args[0].equals("set")) {
                ItemStack[] items = player.getInventory().getContents();
                RevampHCF.getInstance().getHandlerManager().getConfigHandler().setFirstJoinItems(items);
                RevampHCF.getInstance().getUtilities().set("first-join-items", BukkitSerilization.itemStackArrayToBase64(items));
                try {
                    RevampHCF.getInstance().getUtilities().save(RevampHCF.getInstance().getUtilities().getFile());
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                player.getInventory().clear();
                player.sendMessage(CC.translate("&eYou have successfully set &fFirst Join &eitems."));
            }
            else if (args[0].equals("remove")) {
                RevampHCF.getInstance().getHandlerManager().getConfigHandler().setFirstJoinItems(new ItemStack[36]);
                RevampHCF.getInstance().getUtilities().set("first-join-items", "");
                try {
                    RevampHCF.getInstance().getUtilities().save(RevampHCF.getInstance().getUtilities().getFile());
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }
                player.sendMessage(CC.translate("&eYou have successfully removed &fFirst Join &eitems."));
            }
        }
        else {
            this.sendUsage(player);
        }
    }
    
    public void sendUsage(Player player) {
        player.sendMessage(CC.translate("&cFirstJoinItems - Help Commands"));
        player.sendMessage(CC.translate("&c/firstjoinitems set - Set First Join items."));
        player.sendMessage(CC.translate("&c/firstjoinitems remove - Remove First Join items."));
    }
}

package eu.revamp.hcf.commands.inventory;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MapKitCommand extends BaseCommand
{
    public MapKitCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "mapkit";
        this.permission = "revamphcf.command.mapkit";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        if (args.length == 0) {
            player.openInventory(RevampHCF.getInstance().getHandlerManager().getMapKitHandler().getMapKitInventory());
        }
        else if (args.length == 1 && args[0].equals("edit")) {
            if (player.hasPermission("*")) {
                RevampHCF.getInstance().getHandlerManager().getMapKitHandler().getEditingMapKit().add(player.getUniqueId());
                player.openInventory(RevampHCF.getInstance().getHandlerManager().getMapKitHandler().getMapKitInventory());
            }
            else {
                player.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
            }
        }
    }
}

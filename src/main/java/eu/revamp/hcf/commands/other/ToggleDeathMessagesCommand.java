package eu.revamp.hcf.commands.other;

import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;

public class ToggleDeathMessagesCommand extends BaseCommand
{
    public ToggleDeathMessagesCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "toggledeathmessages";
        this.permission = "revamphcf.command.toggledeathmessages";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        RevampHCF.getInstance().getHandlerManager().getDeathMessageHandler().toggleAlerts(player);
    }
}

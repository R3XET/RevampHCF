package eu.revamp.hcf.commands.other;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.spigot.utils.generic.Tasks;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class ToggleLightningCommand extends BaseCommand
{
    public ToggleLightningCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "togglelightning";
        this.permission = "revamphcf.command.togglelightning";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Tasks.runAsync(this.getInstance(), () -> {
            HCFPlayerData user = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer((Player)sender);
            boolean newShowLightning = !user.isShowLightning();
            user.setShowLightning(newShowLightning);
            sender.sendMessage(CC.translate("&eYou have " + (newShowLightning ? "&aEnabled" : "&cDisabled") + " &elightning strikes on death."));
        });
    }
}

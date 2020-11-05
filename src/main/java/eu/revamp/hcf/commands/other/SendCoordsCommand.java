package eu.revamp.hcf.commands.other;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.spigot.utils.generic.Tasks;
import eu.revamp.system.enums.ChatChannel;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SendCoordsCommand extends BaseCommand
{
    public SendCoordsCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "tl";
        this.permission = "revamphcf.command.teamlocation";
        this.forPlayerUseOnly = true;
    }
    
    @Override @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        Tasks.runAsync(this.getInstance(), () -> {
            Player player = (Player)sender;
            PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
            if (playerFaction == null) {
                player.sendMessage(Language.FACTIONS_NOFACTION.toString());
                return;
            }
            String format = String.format(ChatChannel.FACTION.getRawFormat(player), "", " [" + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ() + ']');
            for (Player target : playerFaction.getOnlinePlayers()) {
                target.sendMessage(format);
            }
        });
    }
}

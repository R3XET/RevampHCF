package eu.revamp.hcf.commands.other;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.spigot.utils.generic.Tasks;
import org.apache.commons.codec.language.bm.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GodCommand extends BaseCommand
{
    public GodCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "god";
        this.permission = "revamphcf.admin";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Tasks.runAsync(this.getInstance(), () -> {
            Player player = (Player)sender;
            if (args.length == 0) {
                if (RevampHCF.getInstance().getHandlerManager().getGodHandler().getPlayers().contains(player.getUniqueId())) {
                    RevampHCF.getInstance().getHandlerManager().getGodHandler().getPlayers().remove(player.getUniqueId());
                    player.sendMessage(Language.GOD_DISABLED.toString());
                }
                else {
                    RevampHCF.getInstance().getHandlerManager().getGodHandler().getPlayers().add(player.getUniqueId());
                    player.sendMessage(Language.GOD_ENABLED.toString());
                }
            }
            else {
                if (!player.hasPermission("revamphcf.op")) {
                    player.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
                    return;
                }
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
                    return;
                }
                if (RevampHCF.getInstance().getHandlerManager().getGodHandler().getPlayers().contains(target.getUniqueId())) {
                    RevampHCF.getInstance().getHandlerManager().getGodHandler().getPlayers().remove(target.getUniqueId());
                    player.sendMessage(Language.GOD_DISABLED_OTHER.toString().replace("%player%", target.getName()));
                }
                else {
                    RevampHCF.getInstance().getHandlerManager().getGodHandler().getPlayers().add(target.getUniqueId());
                    player.sendMessage(Language.GOD_ENABLED_OTHER.toString().replace("%player%", target.getName()));
                }
                target.sendMessage(Language.GOD_ENABLED_OTHER_TARGET.toString().replace("%player%", player.getName()));
            }
        });
    }
}

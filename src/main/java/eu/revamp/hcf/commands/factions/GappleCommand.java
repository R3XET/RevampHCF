package eu.revamp.hcf.commands.factions;

import eu.revamp.hcf.managers.CooldownManager;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.spigot.utils.time.TimeUtils;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;

public class GappleCommand extends BaseCommand
{
    public GappleCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "gapple";
        this.permission = "revamphcf.command.gapple";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        final Player player = (Player)sender;
        if (args.length == 0) {
            if (CooldownManager.isOnCooldown("GAPPLE_DELAY", player)) {
                sender.sendMessage(CC.translate("&cYour &6&lGolden Apple &ctimer is active for another &l" + TimeUtils.getRemaining(CooldownManager.getCooldownMillis("GAPPLE_DELAY", player), true, false) + "&c."));
            }
            else {
                sender.sendMessage(CC.translate("&cYour &6&lGolden Apple&c timer is currently not active."));
            }
        }
    }
}

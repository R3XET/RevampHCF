package eu.revamp.hcf.commands.factions;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.managers.CooldownManager;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.spigot.utils.time.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class LFFCommand extends BaseCommand
{
    private static long lffDelay = TimeUnit.MINUTES.toMillis(30L);

    public LFFCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "lff";
        this.permission = "revamphcf.command.lff";
        this.forPlayerUseOnly = true;
    }
    
    @Override @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        if (RevampHCF.getInstance().getFactionManager().getPlayerFaction(player) != null) {
            sender.sendMessage(Language.FACTIONS_ALREADY_IN_A_FACTION.toString());
            return;
        }
        if (args.length == 0) {
            if (CooldownManager.isOnCooldown("LFF_COMMAND_DELAY", player)) {
                player.sendMessage(Language.LFF_COOLDOWN.toString().replace("%time%", TimeUtils.getRemaining(CooldownManager.getCooldownMillis("LFF_COMMAND_DELAY", player), true)));
                return;
            }
            for (String lffmsg : this.getInstance().getLanguage().getStringList("LFF.MESSAGE")) {
                lffmsg = lffmsg.replace("%player%", player.getName());
                lffmsg = lffmsg.replace("&", "§");
                Bukkit.broadcastMessage(lffmsg);
            }
            if (!CooldownManager.getCooldowns().containsKey("LFF_COMMAND_DELAY")) {
                CooldownManager.createCooldown("LFF_COMMAND_DELAY");
            }
            CooldownManager.addCooldown("LFF_COMMAND_DELAY", player, lffDelay);
        }
    }
}
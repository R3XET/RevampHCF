package eu.revamp.hcf.factions.commands.member;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.FactionExecutor;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.utils.games.EventFaction;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.UUID;

public class FactionHQCommand extends CommandArgument
{
    private final FactionExecutor factionExecutor;
    private final RevampHCF plugin;
    
    public FactionHQCommand(FactionExecutor factionExecutor, RevampHCF plugin) {
        super("home", "Teleport to the faction home.", new String[] { "hq" });
        this.factionExecutor = factionExecutor;
        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName();
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        Player player = (Player)sender;
        if (args.length >= 2 && args[1].equalsIgnoreCase("set")) {
            this.factionExecutor.getArgument("sethome").onCommand(sender, command, label, args);
            return true;
        }
        UUID uuid = player.getUniqueId();
        HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        if (data.getSpawnTagCooldown() > 0L) {
            sender.sendMessage(CC.translate("&cYou can't warp whilst your &lSpawn Tag&c timer is active."));
            return true;
        }
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(uuid);
        if (playerFaction == null) {
            sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
            return true;
        }
        Location home = playerFaction.getHome();
        if (home == null) {
            sender.sendMessage(CC.translate("&cYour faction does not have a home set."));
            return true;
        }
        Faction factionAt = this.plugin.getFactionManager().getFactionAt(player.getLocation());
        if (factionAt instanceof EventFaction) {
            sender.sendMessage(CC.translate("&cYou cannot warp whilst in event zones."));
            return true;
        }
        if (factionAt != playerFaction && factionAt instanceof PlayerFaction) {
            RevampHCF.getInstance().getHandlerManager().getTimerManager().getHomeHandler().teleport(player, home, 40000L, PlayerTeleportEvent.TeleportCause.COMMAND);
        }
        long millis = 0L;
        if (factionAt.isSafezone()) {
            millis = 0L;
        }
        else {
            switch (player.getWorld().getEnvironment()) {
                case THE_END: {
                    sender.sendMessage(ChatColor.RED + "You cannot teleport to your faction home whilst in The End.");
                    return true;
                }
                case NETHER: {
                    millis = 30000L;
                    break;
                }
                default: {
                    millis = 10000L;
                    break;
                }
            }
        }
        if (factionAt != playerFaction && factionAt instanceof PlayerFaction) {
            millis *= 2L;
        }
        RevampHCF.getInstance().getHandlerManager().getTimerManager().getHomeHandler().teleport(player, home, millis, PlayerTeleportEvent.TeleportCause.COMMAND);
        return true;
    }
}

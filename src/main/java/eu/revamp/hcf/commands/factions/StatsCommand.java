package eu.revamp.hcf.commands.factions;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand extends BaseCommand {
    public StatsCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "stats";
        this.permission = "revamphcf.command.stats";
        this.forPlayerUseOnly = true;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        PlayerFaction faction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player.getUniqueId());
        HCFPlayerData user = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player.getUniqueId());
        String fac;
        if (faction != null) {
            fac = String.valueOf(faction);
        } else {
            fac = "None";
        }
        if (args.length == 0) {
            for (String stats : this.getInstance().getLanguage().getStringList("STATS")) {
                stats = stats.replace("%player%", player.getName());
                stats = stats.replace("%faction%", fac);
                stats = stats.replace("%balance%", String.valueOf(user.getBalance()));
                stats = stats.replace("%kills%", String.valueOf(user.getKills()));
                stats = stats.replace("%deaths%", String.valueOf(user.getDeaths()));
                stats = stats.replace("%emeralds%", String.valueOf(player.getStatistic(Statistic.MINE_BLOCK, Material.EMERALD_ORE)));
                stats = stats.replace("%diamonds%", String.valueOf(player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE)));
                stats = stats.replace("%gold%", String.valueOf(player.getStatistic(Statistic.MINE_BLOCK, Material.GOLD_ORE)));
                stats = stats.replace("%redstone%", String.valueOf(player.getStatistic(Statistic.MINE_BLOCK, Material.REDSTONE_ORE)));
                stats = stats.replace("%iron%", String.valueOf(player.getStatistic(Statistic.MINE_BLOCK, Material.IRON_ORE)));
                stats = stats.replace("%coal%", String.valueOf(player.getStatistic(Statistic.MINE_BLOCK, Material.COAL_ORE)));
                stats = stats.replace("%lapis%", String.valueOf(player.getStatistic(Statistic.MINE_BLOCK, Material.LAPIS_ORE)));
                stats = stats.replace("&", "§");
                player.sendMessage((stats));
            }
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
                return;
            }
            PlayerFaction targetFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(target.getUniqueId());
            HCFPlayerData targetUser = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(target.getUniqueId());
            String fac2;
            if (targetFaction != null) {
                fac2 = String.valueOf(targetFaction);
            } else {
                fac2 = "None";
            }
            for (String stats : this.getInstance().getLanguage().getStringList("STATS")) {
                stats = stats.replace("%player%", target.getName());
                stats = stats.replace("%faction%", fac2);
                stats = stats.replace("%balance%", String.valueOf(targetUser.getBalance()));
                stats = stats.replace("%kills%", String.valueOf(targetUser.getKills()));
                stats = stats.replace("%deaths%", String.valueOf(targetUser.getDeaths()));
                stats = stats.replace("%emeralds%", String.valueOf(target.getStatistic(Statistic.MINE_BLOCK, Material.EMERALD_ORE)));
                stats = stats.replace("%diamonds%", String.valueOf(target.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE)));
                stats = stats.replace("%gold%", String.valueOf(target.getStatistic(Statistic.MINE_BLOCK, Material.GOLD_ORE)));
                stats = stats.replace("%redstone%", String.valueOf(target.getStatistic(Statistic.MINE_BLOCK, Material.REDSTONE_ORE)));
                stats = stats.replace("%iron%", String.valueOf(target.getStatistic(Statistic.MINE_BLOCK, Material.IRON_ORE)));
                stats = stats.replace("%coal%", String.valueOf(target.getStatistic(Statistic.MINE_BLOCK, Material.COAL_ORE)));
                stats = stats.replace("%lapis%", String.valueOf(target.getStatistic(Statistic.MINE_BLOCK, Material.LAPIS_ORE)));
                stats = stats.replace("&", "§");
                player.sendMessage((stats));
            }
        }
    }
}

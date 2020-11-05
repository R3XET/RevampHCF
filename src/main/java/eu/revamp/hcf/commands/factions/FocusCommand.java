package eu.revamp.hcf.commands.factions;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.ChatColor;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Bukkit;
import eu.revamp.hcf.factions.utils.struction.Role;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.UUID;
import eu.revamp.hcf.factions.type.PlayerFaction;
import java.util.Map;
import eu.revamp.hcf.commands.BaseCommand;

public class FocusCommand extends BaseCommand
{
    public static Map<PlayerFaction, UUID> focus = new HashMap<>();
    
    public FocusCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "focus";
        this.permission = "revamphcf.command.focus";
        this.forPlayerUseOnly = true;
    }
    
    @Override @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        if (args.length == 0){
            player.sendMessage(Language.FOCUS_COMMAND_USAGE.toString());
            return;
        }
        if (args.length == 1) {
            PlayerFaction faction = this.getInstance().getFactionManager().getPlayerFaction(player);
            if (faction != null) {
                if (faction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
                    player.sendMessage(CC.translate("&cYour cannot do this with this role."));
                    return;
                }
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    if (FocusCommand.focus.containsKey(faction)) {
                        if (FocusCommand.focus.get(faction).equals(target.getUniqueId())) {
                            Player oldTarget = Bukkit.getPlayer(FocusCommand.focus.get(faction));
                            if (oldTarget != null) {
                                for (Player member : faction.getOnlinePlayers()) {
                                    this.getInstance().getHandlerManager().getScoreboardTagEvents().getScoreboardFor(member).addUpdate(oldTarget);
                                }
                            }
                            FocusCommand.focus.remove(faction);
                            this.sendUnFocusFactionMessage(faction, target);
                        }
                        else {
                            for (Player member2 : faction.getOnlinePlayers()) {
                                if (target != member2) {
                                    Player oldTarget2 = Bukkit.getPlayer(FocusCommand.focus.get(faction));
                                    if (oldTarget2 != null) {
                                        this.getInstance().getHandlerManager().getScoreboardTagEvents().getScoreboardFor(member2).addUpdate(oldTarget2);
                                    }
                                    FocusCommand.focus.put(faction, target.getUniqueId());
                                    this.sendFactionMessage(faction, target);
                                    this.broadcastFocus(faction);
                                }
                                else {
                                    player.sendMessage(Language.FOCUS_FACTION_MEMBERS.toString());
                                }
                            }
                        }
                    }
                    else {
                        for (Player member2 : faction.getOnlinePlayers()) {
                            if (target != member2) {
                                FocusCommand.focus.put(faction, target.getUniqueId());
                                this.sendFactionMessage(faction, target);
                                this.broadcastFocus(faction);
                            }
                            else {
                                player.sendMessage(Language.FOCUS_FACTION_MEMBERS.toString());
                            }
                        }
                    }
                }
                else {
                    player.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
                }
            }
        }
        else {
            player.sendMessage(Language.FACTIONS_NOFACTION.toString());
        }
    }
    
    public void broadcastFocus(PlayerFaction faction) {
        if (FocusCommand.focus.containsKey(faction)) {
            Player target = Bukkit.getPlayer(FocusCommand.focus.get(faction));
            if (target == null) return;
            for (Player others : faction.getOnlinePlayers()) {
                if (others.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard()) {
                    Scoreboard scoreboard = others.getScoreboard();
                    Team team = scoreboard.getTeam("faction-focus");
                    if (team != null) {
                        team.unregister();
                    }
                    team = scoreboard.registerNewTeam("faction-focus");
                    team.setPrefix(ChatColor.AQUA.toString() + ChatColor.BOLD.toString());
                    team.addEntry(target.getName());
                }
            }
        }
    }
    
    public void sendFactionMessage(PlayerFaction faction, Player target) {
        faction.broadcast(Language.FOCUS_ON.toString().replaceAll("%player%", target.getName()));
    }

    public void sendUnFocusFactionMessage(PlayerFaction faction, Player target) {
        faction.broadcast(Language.FOCUS_OFF.toString().replaceAll("%player%", target.getName()));
    }
}

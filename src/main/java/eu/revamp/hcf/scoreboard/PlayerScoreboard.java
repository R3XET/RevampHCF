package eu.revamp.hcf.scoreboard;

import com.google.common.base.Preconditions;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import lombok.Getter;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.classes.Archer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerScoreboard
{
    @Getter private Player player;
    @Getter private Scoreboard scoreboard;
    private Objective objective;
    private List<ScoreboardInput> inputs;
    private List<String> lastInputs;
    
    public PlayerScoreboard(Player player, Scoreboard scoreboard, String string) {
        this.player = player;
        this.scoreboard = scoreboard;
        Preconditions.checkState(string.length() < 32, "Max characters for Title is 32!");
        String title = CC.translate(string);
        (this.objective = this.getObjective(title)).setDisplaySlot(DisplaySlot.SIDEBAR);
        this.inputs = new ArrayList<>();
        this.lastInputs = new ArrayList<>();
    }
    
    public void add(String text, String cooldown) {
        if (this.inputs.size() >= 14) return;
        text = CC.translate(text);
        cooldown = CC.translate(cooldown);
        ScoreboardInput input;
        if (text.length() <= 16) {
            input = new ScoreboardInput("", text, cooldown, this.inputs.size());
        }
        else {
            String str1 = text.substring(text.length() - 16);
            String str2 = text.substring(0, text.length() - str1.length());
            input = new ScoreboardInput(str2, str1, cooldown, this.inputs.size());
        }
        this.inputs.add(input);
    }
    
    public void addLine(String line1, String line2, String line3) {
        line1 = CC.translate(line1);
        line2 = CC.translate(line2);
        line3 = CC.translate(line3);
        this.inputs.add(new ScoreboardInput(line1, line2, line3, this.inputs.size()));
    }
    
    public void update(Player player) {
        player.setScoreboard(this.scoreboard);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < this.inputs.size(); ++i) {
            ScoreboardInput object = this.inputs.get(i);
            Team team = this.getTeam(object.getName());
            if (!team.hasEntry(object.getName())) {
                team.addEntry(object.getName());
            }
            team.setPrefix(object.getPrefix());
            team.setSuffix(object.getSuffix());
            list.add(object.getName());
            this.objective.getScore(object.getName()).setScore(this.inputs.size() - object.getPosition());
        }
        this.lastInputs.removeAll(list);
        for (Object object : this.lastInputs) {
            Team team = this.getTeam((String) object);
            team.removeEntry((String) object);
            this.scoreboard.resetScores((String) object);
        }
        this.lastInputs = list;
    }
    
    public synchronized Team getTeam(String string) {
        Team team = this.scoreboard.getTeam(string);
        if (team == null) {
            team = this.scoreboard.registerNewTeam(string);
            team.addEntry(string);
        }
        return team;
    }
    
    public Objective getObjective(String string) {
        Objective objective = this.scoreboard.getObjective("uhc");
        if (objective == null) {
            objective = this.scoreboard.registerNewObjective("uhc", "dummy");
        }
        objective.setDisplayName(string);
        return objective;
    }

    public synchronized Team getEnemyTeam() {
        Team team = this.scoreboard.getTeam("4enemies");
        if (team == null) {
            team = this.scoreboard.registerNewTeam("4enemies");
            team.setPrefix(ChatColor.RED.toString());
        }
        return team;
    }
    
    public synchronized Team getMemberTeam() {
        Team team = this.scoreboard.getTeam("1members");
        if (team == null) {
            team = this.scoreboard.registerNewTeam("1members");
            team.setPrefix(ChatColor.GREEN.toString());
        }
        return team;
    }
    
    public synchronized Team getAllyTeam() {
        Team team = this.scoreboard.getTeam("2allies");
        if (team == null) {
            team = this.scoreboard.registerNewTeam("2allies");
            team.setPrefix(ChatColor.LIGHT_PURPLE.toString());
        }
        return team;
    }
    
    public synchronized Team getArcherTagTeam() {
        Team team = this.scoreboard.getTeam("3archers");
        if (team == null) {
            team = this.scoreboard.registerNewTeam("3archers");
            team.setPrefix(ChatColor.DARK_RED.toString());
        }
        return team;
    }
    
    public synchronized Team getFocusTeam() {
        Team team = this.scoreboard.getTeam("5focused");
        if (team == null) {
            team = this.scoreboard.registerNewTeam("5focused");
            team.setPrefix(ChatColor.AQUA + ChatColor.BOLD.toString());
        }
        return team;
    }
    
    public void addUpdate(Player target) {
        this.addUpdates(Collections.singleton(target));
    }
    @SuppressWarnings("deprecation")
    public void addUpdates(Iterable<? extends Player> updates) {
        this.player.setScoreboard(this.scoreboard);
        PlayerFaction playerFaction = null;
        boolean bol = false;
        for (Player update : updates) {
            if (this.player.equals(update)) {
                if (Archer.TAGGED.containsKey(update.getUniqueId())) {
                    this.getArcherTagTeam().addEntry(update.getName());
                }
                this.getMemberTeam().addEntry(update.getName());
            }
            else {
                if (!bol) {
                    playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(this.player);
                    bol = true;
                }
                if (Archer.TAGGED.containsKey(update.getUniqueId())) {
                    this.getArcherTagTeam().addEntry(update.getName());
                }
                else {
                    PlayerFaction targetFaction;
                    if (playerFaction == null || (targetFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(update)) == null) {
                        this.getEnemyTeam().addEntry(update.getName());
                    }
                    else if (playerFaction.equals(targetFaction)) {
                        this.getMemberTeam().addEntry(update.getName());
                    }
                    else if (playerFaction.getAllied().contains(targetFaction.getUniqueID())) {
                        this.getAllyTeam().addEntry(update.getName());
                    }
                    else {
                        this.getEnemyTeam().addEntry(update.getName());
                    }
                }
            }
        }
    }
    @SuppressWarnings("deprecation")
    public void addUpdates(Player[] updates) {
        this.player.setScoreboard(this.scoreboard);
        PlayerFaction playerFaction = null;
        boolean hasRun = false;
        for (Player update : updates) {
            if (this.player.equals(update)) {
                if (Archer.TAGGED.containsKey(update.getUniqueId())) {
                    this.getArcherTagTeam().addEntry(update.getName());
                }
                this.getMemberTeam().addEntry(update.getName());
            }
            else {
                if (!hasRun) {
                    playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(this.player);
                    hasRun = true;
                }
                if (Archer.TAGGED.containsKey(update.getUniqueId())) {
                    this.getArcherTagTeam().addEntry(update.getName());
                }
                else {
                    PlayerFaction targetFaction;
                    if (playerFaction == null || (targetFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(update)) == null) {
                        this.getEnemyTeam().addEntry(update.getName());
                    }
                    else if (playerFaction.equals(targetFaction)) {
                        this.getMemberTeam().addEntry(update.getName());
                    }
                    else if (playerFaction.getAllied().contains(targetFaction.getUniqueID())) {
                        this.getAllyTeam().addEntry(update.getName());
                    }
                    else {
                        this.getEnemyTeam().addEntry(update.getName());
                    }
                }
            }
        }
    }
    
    public void clear() {
        this.inputs.clear();
    }
}

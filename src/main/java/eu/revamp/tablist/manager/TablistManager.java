package eu.revamp.tablist.manager;

import eu.revamp.tablist.RevampTab;
import eu.revamp.tablist.element.TablistElement;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class TablistManager {

    private Scoreboard scoreboard;
    @Getter private Player player;
    private RevampTab revampTab;
    private Map<Integer, String> entries = new HashMap<>();
    @Getter private List<Object> fakePlayers = new ArrayList<>();
    private boolean enabled = true;

    public TablistManager(Scoreboard scoreboard, Player player, RevampTab revampTab) {
        int i;
        this.scoreboard = scoreboard;
        this.player = player;
        this.revampTab = revampTab;
        if (scoreboard.getTeam("z") == null) {
            scoreboard.registerNewTeam("z");
        }
        Team t = scoreboard.getTeam("z");
        int size = Bukkit.getOnlinePlayers().size();
        Player[] arr = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        Player online;
        for (i = 0; i < size; ++i) {
            Team team1;
            online = arr[i];
            if (!t.getEntries().contains(online.getName())) {
                t.addEntry(online.getName());
            }
            if (online.getScoreboard().getTeam("z") == null) {
                online.getScoreboard().registerNewTeam("z");
            }
            if ((team1 = online.getScoreboard().getTeam("z")).getEntries().contains(player.getName())) continue;
            team1.addEntry(player.getName());
        }
        size = this.revampTab.getTablistVersion().getSlots(this.player) + 1;
        for (i = 1; i < size; ++i) {
            String key = this.getNextBlank();
            this.entries.put(i, key);
            if (this.getByPos(i) == null) {
                scoreboard.registerNewTeam("a" + key);
            }
            Team team = scoreboard.getTeam("a" + key);
            team.addEntry(this.entries.get(i));
            this.fakePlayers.add(revampTab.getTablistVersion().createPlayer(player, key));
        }
        player.setScoreboard(scoreboard);
    }

    public void update() {
        this.revampTab.getTablistVersion().update(this.player);
        if (this.revampTab.getTablist().getTabElements(this.player) == null || this.revampTab.getTablist().getTabElements(this.player).isEmpty()) return;
        int size = this.revampTab.getTablistVersion().getSlots(this.player) + 1;
        for (int i = 1; i < size; ++i) {
            if (TablistElement.getByPosition(this.player, i) != null) {
                TablistElement tablistElement = TablistElement.getByPosition(this.player, i);
                if (tablistElement.getDisplay().length() > 16) {
                    String prefix = this.splitStrings(tablistElement.getDisplay())[0];
                    String suffix = this.splitStrings(tablistElement.getDisplay())[1];
                    this.getByPos(i).setPrefix(prefix);
                    this.getByPos(i).setSuffix(suffix);
                    continue;
                }
                this.getByPos(i).setPrefix(tablistElement.getDisplay() == null ? "" : tablistElement.getDisplay());
                this.getByPos(i).setSuffix("");
                continue;
            }
            this.getByPos(i).setSuffix("");
            this.getByPos(i).setPrefix("");
        }
    }

    public void enable() {
        if (this.enabled) {
            return;
        }
        for (Object fakePlayer : this.fakePlayers) {
            this.revampTab.getTablistVersion().addPlayerInfo(this.player, fakePlayer);
        }
        this.enabled = true;
    }

    public void disable() {
        if (!this.enabled) {
            return;
        }
        for (Object fakePlayer : this.fakePlayers) {
            this.revampTab.getTablistVersion().removePlayerInfo(this.player, fakePlayer);
        }
        this.enabled = false;
    }

    public void destroy() {
        this.revampTab.getTablists().remove(this);
        this.player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        for (String entry : this.scoreboard.getEntries()) {
            this.scoreboard.resetScores(entry);
        }
        for (Objective objective : this.scoreboard.getObjectives()) {
            objective.unregister();
        }
        for (Team team : this.scoreboard.getTeams()) {
            team.unregister();
        }
    }

    private Team getByPos(int slot) {
        return this.scoreboard.getTeam("a" + this.entries.get(slot));
    }

    private String repeat(String str, int repeat) {
        if (str == null) {
            return null;
        }
        if (repeat <= 0) {
            return "";
        }
        int inputLength = str.length();
        if (repeat == 1 || inputLength == 0) {
            return str;
        }
        if (inputLength == 1 && repeat <= 8192) {
            return this.padding(repeat, str.charAt(0));
        }
        int outputLength = inputLength * repeat;
        switch (inputLength) {
            case 1: {
                char ch = str.charAt(0);
                char[] output1 = new char[outputLength];
                for (int i = repeat - 1; i >= 0; --i) {
                    output1[i] = ch;
                }
                return new String(output1);
            }
            case 2: {
                char ch0 = str.charAt(0);
                char ch1 = str.charAt(1);
                char[] output2 = new char[outputLength];
                for (int i = repeat * 2 - 2; i >= 0; --i) {
                    output2[i] = ch0;
                    output2[i + 1] = ch1;
                    --i;
                }
                return new String(output2);
            }
        }
        StringBuilder buf = new StringBuilder(outputLength);
        for (int i = 0; i < repeat; ++i) {
            buf.append(str);
        }
        return buf.toString();
    }

    private String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
        if (repeat < 0) {
            throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
        }
        char[] buf = new char[repeat];
        Arrays.fill(buf, padChar);
        return new String(buf);
    }

    private String[] splitStrings(String text) {
        int lenght = text.length();
        if (lenght > 16) {
            String suffix;
            String prefix = text.substring(0, 16);
            if (prefix.charAt(15) == '\u00a7' || prefix.charAt(15) == '&') {
                prefix = prefix.substring(0, 15);
                suffix = text.substring(15, lenght);
            } else if (prefix.charAt(14) == '\u00a7' || prefix.charAt(14) == '&') {
                prefix = prefix.substring(0, 14);
                suffix = text.substring(14, lenght);
            } else {
                suffix = ChatColor.getLastColors(prefix.replace('&', '\u00a7')) + text.substring(16, lenght);
            }
            if (suffix.length() > 16) {
                suffix = suffix.substring(0, 16);
            }
            return new String[]{prefix, suffix};
        }
        return new String[]{text};
    }

    private String getNextBlank() {
        for (String blank : this.getBlanks()) {
            if (this.scoreboard.getTeam(blank) != null) continue;
            if (this.entries.values().stream().filter(blank::equalsIgnoreCase).findFirst().orElse(null) != null) continue;
            return blank;
        }
        return null;
    }

    private List<String> getBlanks() {
        ArrayList<String> toReturn = new ArrayList<>();
        for (ChatColor color : ChatColor.values()) {
            for (int i = 0; i < 4; ++i) {
                toReturn.add(this.repeat(String.valueOf(color), 4 - i) + ChatColor.RESET);
            }
        }
        return toReturn;
    }
}
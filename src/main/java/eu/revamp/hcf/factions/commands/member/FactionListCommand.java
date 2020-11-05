package eu.revamp.hcf.factions.commands.member;

import java.util.Collection;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.generic.ConversionUtils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import java.util.ArrayList;
import eu.revamp.hcf.utils.inventory.MapSorting;
import java.util.Comparator;
import net.md_5.bungee.api.chat.BaseComponent;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import eu.revamp.hcf.factions.type.PlayerFaction;
import org.bukkit.scheduler.BukkitRunnable;
import eu.revamp.hcf.utils.chat.JavaUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.HashMap;

import eu.revamp.hcf.factions.utils.FactionMember;
import java.util.UUID;
import java.util.Map;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionListCommand extends CommandArgument
{
    public double deathsUntilRaidable;
    public long regenCooldownTimestamp;
    public Map<UUID, FactionMember> members;
    private static final int MAX_FACTIONS_PER_PAGE = 10;
    private final RevampHCF plugin;
    
    public FactionListCommand(RevampHCF plugin) {
        super("list", "See a list of all factions.");
        this.deathsUntilRaidable = 1.0;
        this.members = new HashMap<>();
        this.plugin = plugin;
    }
    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName();
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int page;
        if (args.length < 2) {
            page = 1;
        } else if (!ConversionUtils.isInteger(args[1])) {
            sender.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
            return true;
        }
        page = Integer.parseInt(args[1]);
        int finalPage = page;
        new BukkitRunnable() {
            public void run() {
                FactionListCommand.this.showList(finalPage, label, sender);
            }
        }.runTaskAsynchronously(this.plugin);
        return true;
    }
    @SuppressWarnings("deprecation")
    private void showList(int pageNumber, String label, CommandSender sender) {
        if (pageNumber < 1) {
            sender.sendMessage(Language.F_LIST_INVALID_PAGE.toString());
            return;
        }

        // Store a map of factions to their online player count.
        Map<PlayerFaction, Integer> factionOnlineMap = new HashMap<>();
        Player senderPlayer = sender instanceof Player ? (Player) sender : null;
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (senderPlayer == null || senderPlayer.canSee(target)) {
                PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(target);
                if (playerFaction != null) {
                    factionOnlineMap.put(playerFaction, factionOnlineMap.getOrDefault(playerFaction, 0) + 1);
                }
            }
        }

        Map<Integer, List<BaseComponent[]>> pages = new HashMap<>();
        List<Map.Entry<PlayerFaction, Integer>> sortedMap = MapSorting.sortedValues(factionOnlineMap, Comparator.reverseOrder());

        for (Map.Entry<PlayerFaction, Integer> entry : sortedMap) {
            int currentPage = pages.size();

            List<BaseComponent[]> results = pages.get(currentPage);
            if (results == null || results.size() >= MAX_FACTIONS_PER_PAGE) {
                pages.put(++currentPage, results = new ArrayList<>(MAX_FACTIONS_PER_PAGE));
            }

            PlayerFaction playerFaction = entry.getKey();
            String displayName = playerFaction.getDisplayName(sender);
            
            int index = results.size() + (currentPage > 1 ? (currentPage - 1) * MAX_FACTIONS_PER_PAGE : 0) + 1;
            ComponentBuilder builder = new ComponentBuilder(index + ". ").color(net.md_5.bungee.api.ChatColor.GRAY);
            builder.append(ChatColor.BOLD + displayName).color(net.md_5.bungee.api.ChatColor.RED).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, '/' + label + " show " + playerFaction.getName())).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(net.md_5.bungee.api.ChatColor.YELLOW + "Click to view " + displayName + ChatColor.YELLOW + '.').create()));
            builder.append(" (" + entry.getValue() + '/' + playerFaction.getMembers().size() + ')', ComponentBuilder.FormatRetention.FORMATTING).color(net.md_5.bungee.api.ChatColor.GREEN);
            builder.append(" DTR: (").color(net.md_5.bungee.api.ChatColor.GRAY);
            builder.append(JavaUtils.format(playerFaction.getDeathsUntilRaidable())).color(fromBukkit(playerFaction.getDtrColour()));
            builder.append('/' + JavaUtils.format(playerFaction.getMaximumDeathsUntilRaidable()) + ")").color(net.md_5.bungee.api.ChatColor.GRAY);
            results.add(builder.create());
        }

        int maxPages = pages.size();

        if (pageNumber > maxPages) {
            sender.sendMessage(CC.translate("There " + (maxPages == 1 ? "is only " + maxPages + " page" : "are only " + maxPages + " pages") + "."));
            return;
        }

    	sender.sendMessage(CC.translate("&7&m---------------------------------------"));
    	sender.sendMessage(CC.translate("&a&lOnline Teams &7(Page " + pageNumber + "/" + maxPages + ")"));
    	sender.sendMessage(CC.translate("&7&m---------------------------------------"));

        Player player = sender instanceof Player ? (Player) sender : null;
        Collection<BaseComponent[]> components = pages.get(pageNumber);
        for (BaseComponent[] component : components) {
            if (component == null)
                continue;
            if (player != null) {
                player.spigot().sendMessage(component);
            } else {
                sender.sendMessage(TextComponent.toPlainText(component));
            }
        }

        sender.sendMessage(CC.translate("&eYou are currently on &aPage " + pageNumber + '/' + maxPages + "&7."));
        sender.sendMessage(CC.translate("&eTo view other pages, use &a/f list <page>&7."));
    	sender.sendMessage(CC.translate("&7&m---------------------------------------"));
    }
    
    private static ChatColor fromBukkit(org.bukkit.ChatColor chatColor) {
        return ChatColor.getByChar(chatColor.getChar());
    }
}

package eu.revamp.hcf.handlers.chat;
/*
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.events.FactionChatEvent;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.utils.struction.ChatChannel;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.system.plugin.RevampSystem;
import eu.revamp.system.plugin.RevampSystemAPI;
import lombok.Getter;
import lombok.Setter;
import net.mineaus.lunar.LunarClientAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;

public class ChatHandler extends Handler implements Listener
{
    @Getter @Setter private ArrayList<UUID> spy;
    
    public ChatHandler(RevampHCF plugin) {
        super(plugin);
        this.spy = new ArrayList<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @Override
    public void disable() {
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();
        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
        ChatChannel chatChannel = (playerFaction == null) ? ChatChannel.PUBLIC : playerFaction.getMember(player).getChatChannel();
        Set<Player> recipients = event.getRecipients();
        /*
        if (message.startsWith("!")){
            chatChannel = ChatChannel.PUBLIC;
            message = message.substring(1).trim();
        }
        else if (message.startsWith("@")){
            chatChannel = ChatChannel.FACTION;
            message = message.substring(1).trim();
        }
        */
/*
        //TODO CHECK IF CHANNEL WITH @ WORKS
        if (chatChannel == ChatChannel.FACTION || chatChannel == ChatChannel.ALLIANCE || chatChannel == ChatChannel.CAPTAIN) {
            if (!this.isGlobalChannel(message) || !this.isFactionChannel(message)) {
                Collection<Player> online = playerFaction.getOnlinePlayers();
                if (chatChannel == ChatChannel.ALLIANCE) {
                    Collection<PlayerFaction> allies = playerFaction.getAlliedFactions();
                    for (PlayerFaction ally : allies) {
                        online.addAll(ally.getOnlinePlayers());
                    }
                }
                recipients.retainAll(online);
                event.setFormat(chatChannel.getRawFormat(player));
                Bukkit.getPluginManager().callEvent(new FactionChatEvent(true, playerFaction, player, chatChannel, recipients, event.getMessage()));
                return;
            }
            message = message.substring(1).trim();
            event.setMessage(message);
        }
        event.setCancelled(true);
        Iterator<Player> iterator = event.getRecipients().iterator();
        while (iterator.hasNext()) {
            Player target = iterator.next();
            HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(target);
            if (data == null) {
                iterator.remove();
            }
            else if (data.getIgnoring().contains(player.getName())) {
                iterator.remove();
            }
        }
        for (Player recipient : event.getRecipients()) {
            recipient.sendMessage(this.getFormat(player, playerFaction, message, recipient));
        }
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(this.getFormat(player, playerFaction, message, console));
    }
    
    public boolean isLunarClient(Player player) {
        return LunarClientAPI.getInstance().isAuthenticated(player);
    }
    @SuppressWarnings("deprecation")
    private String getFormat(Player player, PlayerFaction playerFaction, String message, CommandSender viewer) {
        String fac = (playerFaction == null) ? (ChatColor.YELLOW + Faction.FACTIONLESS_PREFIX) : playerFaction.getDisplayName(viewer);
        eu.revamp.system.api.player.HCFPlayerData targetProfile = RevampSystem.getINSTANCE().getPlayerManagement().getHCFPlayerData(player.getUniqueId());
        String rank = CC.translate(CC.translate(targetProfile.getAllPrefixes().replace("_", " ")));
        PlayerFaction playersFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
        /*.replace("&", "")*/
/*
        if (playersFaction != null) {
            if (RevampHCF.getInstance().getConfig().getBoolean("USE-CHAT-PREFIXES")) {
                return RevampHCF.getInstance().getConfig().getString("CHAT.PUBLIC")
                        .replace("%fac%", fac)
                        .replace("%tag%", RevampSystemAPI.plugin.getTagManagement().getTagPrefix(player) != null ? RevampSystemAPI.plugin.getTagManagement().getTagPrefix(player) : "")
                        .replace("%rank%", rank)
                        .replace("%ign%", player.getName())
                        .replace("%message%", message)
                        .replace("&", player.hasPermission("revamphcf.chat.color") ? "§" : "")
                        .replace("%lunar%", this.isLunarClient(player) ? CC.translate("&9&l\\ud83c\\udf19 ") : "");
            }
        }
        return RevampHCF.getInstance().getConfig().getString("CHAT.PUBLIC").replace("%fac%", fac).replace("%tag%", targetProfile.getTag().getFormat().replace("%rank%", rank).replace("%ign%", player.getName()).replace("%message%", message/*.replace("&", "")*//*).replace("%lunar%", this.isLunarClient(player) ? CC.translate("&9&l\uff2c&b&l\uff23 ") : ""));
/*
    }

    private boolean isGlobalChannel(String input) {
        int length = input.length();
        if (length <= 1 || !input.startsWith("!")) {
            return false;
        }
        int i = 1;
        while (i < length) {
            char character = input.charAt(i);
            if (character == ' ') {
                ++i;
            }
            else {
                if (character == '/') {
                    return false;
                }
                break;
            }
        }
        return true;
    }
    private boolean isFactionChannel(String input) {
        int length = input.length();
        if (length <= 1 || !input.startsWith("@")) {
            return false;
        }
        int i = 1;
        while (i < length) {
            char character = input.charAt(i);
            if (character == ' ') {
                ++i;
            }
            else {
                if (character == '/') {
                    return false;
                }
                break;
            }
        }
        return true;
    }
}
*/
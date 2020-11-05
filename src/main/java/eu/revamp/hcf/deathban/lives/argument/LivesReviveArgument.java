package eu.revamp.hcf.deathban.lives.argument;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.deathban.Deathban;
import eu.revamp.hcf.factions.utils.FactionMember;
import eu.revamp.hcf.factions.utils.struction.Relation;
import eu.revamp.hcf.utils.command.CommandArgument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageRecipient;

import java.util.*;

/**
 * An {@link CommandArgument} used to revive {@link Deathban}ned {@link Player}s.
 *//*
public class LivesReviveArgument extends CommandArgument {

    private static final String REVIVE_BYPASS_PERMISSION = "hcf.revive.bypass";
    private static final String PROXY_CHANNEL_NAME = "BungeeCord";

    private final RevampHCF plugin;

    public LivesReviveArgument(RevampHCF plugin) {
        super("revive", "Revive a death-banned player");
        this.plugin = plugin;
        this.permission = "hcf.command.lives.argument." + getName();
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, PROXY_CHANNEL_NAME);
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <playerName>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]); //TODO: breaking

        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(ChatColor.GOLD + "Player '" + ChatColor.WHITE + args[1]
                    + ChatColor.GOLD + "' not found.");
            return true;
        }

        UUID targetUUID = target.getUniqueId();
        FactionMember factionTarget = plugin.getUserManager().getUser(targetUUID);
        Deathban deathban = factionTarget.getDeathban();

        if (deathban == null || !deathban.isActive()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', RevampHCF.getInstance().getConfig().getString("cmd-messages.lives-revive-not-deathbanned").replace("%target%", target.getName())));
            return true;
        }

        Relation relation = Relation.ENEMY;
        if (sender instanceof Player) {
            if (!sender.hasPermission(REVIVE_BYPASS_PERMISSION)) {
                if (plugin.getConfiguration().isKitMap()) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', RevampHCF.getInstance().getConfig().getString("cmd-messages.lives-revive-kitmap-on")));
                    return true;
                }

                if (plugin.getEotwHandler().isEndOfTheWorld()) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', RevampHCF.getInstance().getConfig().getString("cmd-messages.lives-revive-eotw-on")));
                    return true;
                }
            }

            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();
            int selfLives = plugin.getDeathbanManager().getLives(playerUUID);

            if (selfLives <= 0) {
                sender.sendMessage(ChatColor.RED + "You do not have any lives.");
                return true;
            }

            plugin.getDeathbanManager().setLives(playerUUID, selfLives - 1);
            PlayerTeam playerFaction = plugin.getFactionManager().getPlayerFaction(player);
            relation = playerFaction == null ? Relation.ENEMY : playerFaction.getFactionRelation(plugin.getFactionManager().getPlayerFaction(targetUUID));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', RevampHCF.getInstance().getConfig().getString("cmd-messages.lives-revive-enemy").replace("%target%", relation.toChatColour() + target.getName())));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', RevampHCF.getInstance().getConfig().getString("cmd-messages.lives-revive-teammate").replace("%target%", plugin.getConfiguration().getRelationColourTeammate() + target.getName())));
        }

        if (sender instanceof PluginMessageRecipient) {
            //NOTE: This server needs at least 1 player online.
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Message");
            out.writeUTF(args[1]);

            String serverDisplayName = ChatColor.GREEN + "HCF"; //TODO: Non hard-coded server display name.
            out.writeUTF(relation.toChatColour() + sender.getName() + ChatColor.GOLD + " has just revived you from "
                    + serverDisplayName + ChatColor.GOLD + '.');
            ((PluginMessageRecipient) sender).sendPluginMessage(plugin, PROXY_CHANNEL_NAME, out.toByteArray());
        }

        factionTarget.removeDeathban();
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return Collections.emptyList();
        }

        List<String> results = new ArrayList<>();
        Collection<FactionMember> factionUsers = plugin.getUserManager().getUsers().values();
        for (FactionMember factionUser : factionUsers) {
            Deathban deathban = factionUser.getDeathban();
            if (deathban == null || !deathban.isActive()) continue;

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(factionUser.getUniqueID());
            String offlineName = offlinePlayer.getName();
            if (offlineName != null) {
                results.add(offlinePlayer.getName());
            }
        }

        return results;
    }
}
*/
package eu.revamp.hcf.factions.commands.member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.utils.FactionMember;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.system.enums.ChatChannel;
import org.apache.commons.lang.StringUtils;
import java.util.Collection;
import eu.revamp.hcf.factions.type.PlayerFaction;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionChatCommand extends CommandArgument
{
    private final RevampHCF plugin;
    
    public FactionChatCommand(RevampHCF plugin) {
        super("chat", "Faction chats.", new String[] { "c" });
        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " [fac|public|ally] [message]";
    }

    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
            return true;
        }
        FactionMember member = playerFaction.getMember(player.getUniqueId());
        ChatChannel currentChannel = member.getChatChannel();
        ChatChannel parsed = (args.length >= 2) ? ChatChannel.parse(args[1], null) : currentChannel.getRotation();
        if (parsed == null && currentChannel != ChatChannel.PUBLIC) {
            Collection<Player> recipients = playerFaction.getOnlinePlayers();
            if (currentChannel == ChatChannel.ALLIANCE) {
                for (PlayerFaction ally : playerFaction.getAlliedFactions()) {
                    recipients.addAll(ally.getOnlinePlayers());
                }
            }
            String format = String.format(currentChannel.getRawFormat(player), "", StringUtils.join(args, ' ', 1, args.length));
            for (Player recipient : recipients) {
                recipient.sendMessage(format);
            }
            return true;
        }
        ChatChannel newChannel = (parsed == null) ? currentChannel.getRotation() : parsed;
        member.setChatChannel(newChannel);
        sender.sendMessage(CC.translate("&eYou are now in &f" + newChannel.getDisplayName().toLowerCase() + " &echat mode."));
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2 || !(sender instanceof Player)) {
            return Collections.emptyList();
        }
        ChatChannel[] values = ChatChannel.values();
        List<String> results = new ArrayList<>(values.length);
        ChatChannel[] array;
        for (int length = (array = values).length, i = 0; i < length; ++i) {
            ChatChannel type = array[i];
            results.add(type.getName());
        }
        return results;
    }
}

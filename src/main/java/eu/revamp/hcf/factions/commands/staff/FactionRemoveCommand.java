package eu.revamp.hcf.factions.commands.staff;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.message.MessageUtils;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;
import org.bukkit.entity.Player;
import java.util.List;
import eu.revamp.hcf.factions.Faction;
import org.bukkit.conversations.Conversable;
import org.bukkit.command.ConsoleCommandSender;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ConversationFactory;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionRemoveCommand extends CommandArgument
{
    private final ConversationFactory factory;
    private final RevampHCF plugin;
    
    public FactionRemoveCommand(RevampHCF plugin) {
        super("remove", "Remove a faction.");
        this.plugin = plugin;
        this.aliases = new String[] { "delete", "forcedisband", "forceremove" };
        this.permission = "hcf.admin";
        this.factory = new ConversationFactory(plugin).withFirstPrompt(new RemoveAllPrompt(plugin)).withEscapeSequence("/no").withTimeout(10).withModality(false).withLocalEcho(true);
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <all|factionName>";
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
            return true;
        }
        if (args[1].equalsIgnoreCase("all")) {
            if (!(sender instanceof ConsoleCommandSender)) {
                sender.sendMessage(CC.translate("&cNo console."));
                return true;
            }
            Conversable conversable = (Conversable)sender;
            conversable.beginConversation(this.factory.buildConversation(conversable));
        }
        else {
            Faction faction = this.plugin.getFactionManager().getContainingFaction(args[1]);
            if (faction == null) {
                sender.sendMessage(Language.FACTIONS_FACTION_NOT_FOUND.toString());
                return true;
            }
            if (this.plugin.getFactionManager().removeFaction(faction, sender)) {
                MessageUtils.sendMessage(CC.translate("&esuccessfully disbanded faction &f" + faction.getName()), "hcf.admin");
            }
        }
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2 || !(sender instanceof Player)) {
            return Collections.emptyList();
        }
        if (args[1].isEmpty()) {
            return null;
        }
        Player player = (Player)sender;
        List<String> results = new ArrayList<>(this.plugin.getFactionManager().getFactionNameMap().keySet());
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (player.canSee(target) && !results.contains(target.getName())) {
                results.add(target.getName());
            }
        }
        return results;
    }
    
    private static class RemoveAllPrompt extends StringPrompt
    {
        private final RevampHCF plugin;
        
        public RemoveAllPrompt(RevampHCF plugin) {
            this.plugin = plugin;
        }
        
        public String getPromptText(ConversationContext context) {
            return ChatColor.YELLOW + "Are you sure you want to do this? " + ChatColor.RED + ChatColor.BOLD + "All factions" + ChatColor.YELLOW + " will be cleared. " + "Type " + ChatColor.GREEN + "yes" + ChatColor.YELLOW + " to confirm or " + ChatColor.RED + "no" + ChatColor.YELLOW + " to deny.";
        }
        
        public Prompt acceptInput(ConversationContext context, String string) {
            switch (string.toLowerCase()) {
                case "no": {
                    context.getForWhom().sendRawMessage(ChatColor.BLUE + "Cancelled the process of disbanding all factions.");
                    return Prompt.END_OF_CONVERSATION;
                }
                case "yes": {
                    for (Faction faction : this.plugin.getFactionManager().getFactions()) {
                        this.plugin.getFactionManager().removeFaction(faction, Bukkit.getConsoleSender());
                    }
                    Conversable conversable = context.getForWhom();
                    Bukkit.broadcastMessage(String.valueOf(ChatColor.GOLD.toString()) + ChatColor.BOLD + "All factions have been disbanded" + ((conversable instanceof CommandSender) ? (" by " + ((CommandSender)conversable).getName()) : "") + '.');
                    return Prompt.END_OF_CONVERSATION;
                }
                default:
                    break;
            }
            context.getForWhom().sendRawMessage(ChatColor.RED + "Unrecognized response. Process of disbanding all factions cancelled.");
            return Prompt.END_OF_CONVERSATION;
        }
    }
}

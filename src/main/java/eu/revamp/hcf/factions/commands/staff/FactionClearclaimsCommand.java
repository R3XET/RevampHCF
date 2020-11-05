package eu.revamp.hcf.factions.commands.staff;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.Bukkit;
import java.util.ArrayList;
import java.util.Collections;
import org.bukkit.entity.Player;
import java.util.List;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.type.ClaimableFaction;
import org.bukkit.conversations.Conversable;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ConversationFactory;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionClearclaimsCommand extends CommandArgument
{
    private final ConversationFactory factory;
    private final RevampHCF plugin;
    
    public FactionClearclaimsCommand(RevampHCF plugin) {
        super("clearclaims", "Clears the claims of a faction.");
        this.plugin = plugin;
        this.permission = "revamphcf.op";
        this.factory = new ConversationFactory(plugin).withFirstPrompt(new ClaimClearAllPrompt(plugin)).withEscapeSequence("/no").withTimeout(10).withModality(false).withLocalEcho(true);
    }


    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <playerName|factionName|all>";
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
            return true;
        }
        else {
            Faction faction = this.plugin.getFactionManager().getContainingFaction(args[1]);
            if (faction == null) {
                sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
                return true;
            }
            if (faction instanceof ClaimableFaction) {
                ClaimableFaction claimableFaction = (ClaimableFaction)faction;
                claimableFaction.removeClaims(claimableFaction.getClaims(), sender);
                if (claimableFaction instanceof PlayerFaction) {
                    ((PlayerFaction)claimableFaction).broadcast(CC.translate("&eYour claims have been forcefully wiped by &f" + sender.getName()));
                }
            }
            sender.sendMessage(CC.translate("&eClaim belonging to &f" + faction.getName() + " &ehas been forcefully wiped."));
            return true;
        }
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
    
    private static class ClaimClearAllPrompt extends StringPrompt
    {
        private final RevampHCF plugin;
        
        public ClaimClearAllPrompt(RevampHCF plugin) {
            this.plugin = plugin;
        }
        
        public String getPromptText(ConversationContext context) {
            return ChatColor.YELLOW + "Are you sure you want to do this? " + ChatColor.RED + ChatColor.BOLD + "All claims" + ChatColor.YELLOW + " will be cleared. " + "Type " + ChatColor.GREEN + "yes" + ChatColor.YELLOW + " to confirm or " + ChatColor.RED + "no" + ChatColor.YELLOW + " to deny.";
        }
        
        public Prompt acceptInput(ConversationContext context, String string) {
            String lowerCase;
            switch (lowerCase = string.toLowerCase()) {
                case "no": {
                    context.getForWhom().sendRawMessage("Cancelled the process of clearing all faction claims.");
                    return Prompt.END_OF_CONVERSATION;
                }
                case "yes": {
                    for (Faction faction : this.plugin.getFactionManager().getFactions()) {
                        if (faction instanceof ClaimableFaction) {
                            ClaimableFaction claimableFaction = (ClaimableFaction)faction;
                            claimableFaction.removeClaims(claimableFaction.getClaims(), Bukkit.getConsoleSender());
                        }
                    }
                    Conversable conversable = context.getForWhom();
                    Bukkit.broadcastMessage(CC.translate("&eAll claims have been cleared &f" + ((conversable instanceof CommandSender) ? (" by " + ((CommandSender)conversable).getName()) : "")));
                    return Prompt.END_OF_CONVERSATION;
                }
                default:
                    break;
            }
            context.getForWhom().sendRawMessage("Unrecognized response. Process of clearing all faction claims cancelled.");
            return Prompt.END_OF_CONVERSATION;
        }
    }
}

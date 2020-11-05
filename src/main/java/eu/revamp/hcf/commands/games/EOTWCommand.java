package eu.revamp.hcf.commands.games;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;

import org.bukkit.command.Command;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.conversations.Conversable;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ConversationFactory;
import eu.revamp.hcf.commands.BaseCommand;

public class EOTWCommand extends BaseCommand
{
    private final ConversationFactory factory;
    
    public EOTWCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "eotw";
        this.forPlayerUseOnly = false;
        this.factory = new ConversationFactory(plugin).withFirstPrompt(new EotwPrompt()).withEscapeSequence("/no").withTimeout(10).withModality(false).withLocalEcho(true);
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
            return;
        }
        Conversable conversable = (Conversable)sender;
        conversable.beginConversation(this.factory.buildConversation(conversable));
    }
    
    private static class EotwPrompt extends StringPrompt
    {
        public String getPromptText(ConversationContext context) {
            return "Type Yes if you want to active EOTW Timer || Type No if you want to cancel this process.";
        }
        
        public Prompt acceptInput(ConversationContext context, String string) {
            if (string.equalsIgnoreCase("yes")) {
                boolean newStatus = !RevampHCF.getInstance().getHandlerManager().getEotwUtils().isEndOfTheWorld(false);
                Conversable conversable = context.getForWhom();
                if (conversable instanceof CommandSender) {
                    Command.broadcastCommandMessage((CommandSender)conversable, "has set EOTW mode to " + newStatus);
                }
                else {
                    conversable.sendRawMessage(CC.translate("&7has set EOTW mode to " + newStatus));
                }
                RevampHCF.getInstance().getHandlerManager().getEotwUtils().setEndOfTheWorld(newStatus);
            }
            else if (string.equalsIgnoreCase("no")) {
                context.getForWhom().sendRawMessage(CC.translate("&aSuccessfully cancelled EOTW Timer procces."));
            }
            else {
                context.getForWhom().sendRawMessage(CC.translate("&cEOTW Cancelled!"));
            }
            return Prompt.END_OF_CONVERSATION;
        }
    }
}

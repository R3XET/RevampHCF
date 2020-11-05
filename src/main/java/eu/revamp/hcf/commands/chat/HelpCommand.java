package eu.revamp.hcf.commands.chat;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class HelpCommand extends BaseCommand
{
    public HelpCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "help";
        this.permission = "revamphcf.command.help";
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        for (String str : RevampHCF.getInstance().getLanguage().getStringList("HELP")) {
            sender.sendMessage(CC.translate(str));
        }
    }
}

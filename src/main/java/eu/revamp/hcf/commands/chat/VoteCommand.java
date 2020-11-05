package eu.revamp.hcf.commands.chat;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.command.CommandSender;

public class VoteCommand extends BaseCommand
{
    public VoteCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "vote";
        this.permission = "revamphcf.command.vote";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        for (String str : RevampHCF.getInstance().getLanguage().getStringList("VOTE")) {
            sender.sendMessage(CC.translate(str));
        }
    }
}

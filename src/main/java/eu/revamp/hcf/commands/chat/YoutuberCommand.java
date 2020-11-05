package eu.revamp.hcf.commands.chat;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.command.CommandSender;

public class YoutuberCommand extends BaseCommand
{
    public YoutuberCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "youtuber";
        this.permission = "revamphcf.command.youtuber";
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        for (String str : RevampHCF.getInstance().getLanguage().getStringList("YOUTUBER")) {
            sender.sendMessage(CC.translate(str));
        }
    }
}

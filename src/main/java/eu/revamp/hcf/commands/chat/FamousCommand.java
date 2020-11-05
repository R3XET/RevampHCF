package eu.revamp.hcf.commands.chat;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.command.CommandSender;

public class FamousCommand extends BaseCommand
{
    public FamousCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "Famous";
        this.permission = "revamphcf.command.famous";
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        for (String str : RevampHCF.getInstance().getLanguage().getStringList("PARTNER")) {
            sender.sendMessage(CC.translate(str));
        }
    }
}

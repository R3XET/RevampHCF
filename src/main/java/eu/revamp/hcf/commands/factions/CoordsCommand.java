package eu.revamp.hcf.commands.factions;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class CoordsCommand extends BaseCommand
{
    public CoordsCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "coords";
        this.permission = "revamphcf.command.coords";
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        for (String str : RevampHCF.getInstance().getLanguage().getStringList("COORDS")) {
            sender.sendMessage(CC.translate(str).replace("|", "\u2503"));
        }
    }
}

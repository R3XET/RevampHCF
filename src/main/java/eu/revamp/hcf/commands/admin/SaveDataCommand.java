package eu.revamp.hcf.commands.admin;

import eu.revamp.hcf.Language;
import eu.revamp.spigot.utils.generic.Tasks;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;

public class SaveDataCommand extends BaseCommand
{
    public SaveDataCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "savedata";
        this.permission = "*";
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Language.SAVEDATA_SAVING.toString());
            Tasks.runAsync(this.getInstance(), () -> RevampHCF.getInstance().saveData());
            sender.sendMessage(Language.SAVEDATA_SAVED.toString());
        }
    }
}

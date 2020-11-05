package eu.revamp.hcf.commands;

import eu.revamp.hcf.RevampHCF;
import lombok.Getter;
import org.bukkit.command.CommandSender;

public abstract class BaseCommand
{
    @Getter public RevampHCF instance;
    public boolean forPlayerUseOnly;
    public String command;
    public String permission;
    
    public BaseCommand(RevampHCF plugin) {
        this.instance = plugin;
        this.command = "";
        this.permission = "";
        this.forPlayerUseOnly = false;
    }
    
    public abstract void execute(CommandSender sender, String[] args);
}

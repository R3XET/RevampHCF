package eu.revamp.hcf.commands.other;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.generic.Tasks;
import org.bukkit.entity.*;
import org.bukkit.World;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class KillallCommand extends BaseCommand
{
    public KillallCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "killall";
        this.permission = "revamphcf.op";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Tasks.runAsync(this.getInstance(), () -> {
            Player player = (Player)sender;
            if (args.length == 0) {
                player.sendMessage(Language.KILLALL_COMMAND_USAGE.toString());
            }
            else if (args[0].equalsIgnoreCase("all")) {
                for (World world : Bukkit.getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        if (entity instanceof Monster || entity instanceof Animals || entity instanceof Item || entity instanceof Slime || entity instanceof Flying) {
                            entity.remove();
                        }
                    }
                }
                player.sendMessage(Language.KILLALL_KILLED_ALL.toString());
            }
            else if (args[0].equalsIgnoreCase("mobs") || args[0].equalsIgnoreCase("mob")) {
                for (World world : Bukkit.getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        if (entity instanceof Monster) {
                            entity.remove();
                        }
                    }
                }
                player.sendMessage(Language.KILLALL_KILLED_ANIMALS.toString());
            }
            else if (args[0].equalsIgnoreCase("animals") || args[0].equalsIgnoreCase("animal")) {
                for (World world : Bukkit.getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        if (entity instanceof Animals) {
                            entity.remove();
                        }
                    }
                }
                player.sendMessage(Language.KILLALL_KILLED_ANIMALS.toString());
            }
            else if (args[0].equalsIgnoreCase("items") || args[0].equalsIgnoreCase("item")) {
                for (World world : Bukkit.getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        if (entity instanceof Item) {
                            entity.remove();
                        }
                    }
                }
                player.sendMessage(Language.KILLALL_KILLED_ITEMS.toString());
            }
        });
    }
}

package eu.revamp.hcf.commands.other;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.spigot.utils.generic.Tasks;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Set;

public class SpawnerCommand extends BaseCommand
{
    public SpawnerCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "spawner";
        this.permission = "revamphcf.admin";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Tasks.runAsync(this.getInstance(), () -> {
            Player player = (Player)sender;
            if (args.length == 0) {
                player.sendMessage(Language.SPAWNER_COMMAND_USAGE.toString());
            }
            else {
                try {
                    EntityType.valueOf(args[0].toUpperCase());
                }
                catch (Exception e) {
                    player.sendMessage(Language.SPAWNER_DOES_NOT_EXIST.toString().replace("%spawner%", args[0]));
                    return;
                }
                Block block = player.getTargetBlock((Set<Material>)null, 5);
                if (block == null || block.getType() != Material.MOB_SPAWNER) {
                    player.sendMessage(Language.SPAWNER_MUST_BE_LOOKING_AT_SPAWNER.toString());
                    return;
                }
                EntityType entityType = EntityType.valueOf(args[0].toUpperCase());
                CreatureSpawner spawner = (CreatureSpawner)block.getState();
                spawner.setSpawnedType(entityType);
                spawner.update();
                player.sendMessage(Language.SPAWNER_CHANGED.toString().replace("%type%", String.valueOf(entityType)));
            }
        });
    }
}

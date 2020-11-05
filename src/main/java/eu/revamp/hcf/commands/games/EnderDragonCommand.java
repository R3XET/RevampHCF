package eu.revamp.hcf.commands.games;

import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Sound;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;

public class EnderDragonCommand extends BaseCommand
{
    public EnderDragonCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "endevent";
        this.permission = "revamphcf.op";
        this.forPlayerUseOnly = true;
    }
    
    @Override @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        if (player.getWorld().getEnvironment() != World.Environment.THE_END) {
            player.sendMessage(CC.translate("&cYou must be in the end."));
            return;
        }
        PlayerFaction faction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
        player.getWorld().spawnEntity(player.getLocation().add(0.0, 1.0, 0.0), EntityType.ENDER_DRAGON);
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            online.sendMessage(CC.translate(CC.translate("&5[End Event] &dA Ender Dragon has spawned &7(End World)")).replace("%playername%", player.getName().replace("%faction%", faction.getName())));
        }
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            online.playSound(online.getLocation(), Sound.ENDERDRAGON_WINGS, 1.0f, 1.0f);
        }
        player.sendMessage(CC.translate("&5[End Event] &dA Ender Dragon has spawned &7(End World) &cNOTE: Go to kill the Dragon!").replace("%playername%", player.getName().replace("%faction%", faction.getName())));
    }
}

package eu.revamp.hcf.commands.chat;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.spigot.utils.generic.Tasks;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class NearCommand extends BaseCommand
{
    public NearCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "near";
        this.forPlayerUseOnly = true;
        this.permission = "revamphcf.command.near";
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Tasks.runAsync(this.getInstance(), () -> {
            Player p = (Player)sender;
            if (p.hasPermission("revamphcf.command.near.300")) {
                List<String> nearby = this.getNearbyEnemies300(p);
                p.sendMessage(CC.translate(" &9Players Nearby: &a(" + nearby.size() + ")"));
                p.sendMessage(CC.translate("&b&l➥ &f" + (nearby.isEmpty() ? "None" : nearby.toString().replace("[", "").replace("]", ""))));
                return;
            }
            if (p.hasPermission("revamphcf.command.near.100")) {
                List<String> nearby = this.getNearbyEnemies(p);
                p.sendMessage(CC.translate(" &9Players Nearby: &a(" + nearby.size() + ")"));
                p.sendMessage(CC.translate("&b&l➥ &f" + (nearby.isEmpty() ? "None" : nearby.toString().replace("[", "").replace("]", ""))));
            }
        });
    }
    
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return (args.length == 1) ? null : Collections.emptyList();
    }
    
    private List<String> getNearbyEnemies300(Player player) {
        List<String> players = new ArrayList<>();
        Collection<Entity> nearby = player.getNearbyEntities(300.0, 300.0, 300.0);
        for (Entity entity : nearby) {
            if (entity instanceof Player) {
                Player target = (Player)entity;
                if (!target.canSee(player)) {
                    continue;
                }
                if (!player.canSee(target)) {
                    continue;
                }
                if (target.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                    continue;
                }
                players.add(target.getName());
            }
        }
        return players;
    }
    
    private List<String> getNearbyEnemies(Player player) {
        List<String> players = new ArrayList<>();
        Collection<Entity> nearby = player.getNearbyEntities(100.0, 100.0, 100.0);
        for (Entity entity : nearby) {
            if (entity instanceof Player) {
                Player target = (Player)entity;
                if (!target.canSee(player)) {
                    continue;
                }
                if (!player.canSee(target)) {
                    continue;
                }
                if (target.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                    continue;
                }
                players.add(target.getName());
            }
        }
        return players;
    }
}

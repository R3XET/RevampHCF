package eu.revamp.hcf.commands.games;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.world.WorldUtils;
import eu.revamp.system.api.player.PlayerData;
import eu.revamp.system.plugin.RevampSystem;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.command.Command;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.Faction;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class EOTWFFACommand extends BaseCommand
{
    private static boolean eotwffa = false;
    
    public EOTWFFACommand(RevampHCF plugin) {
        super(plugin);
        this.command = "eotwffa";
        this.permission = "*";
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
            return;
        }
        if (args.length == 0) {
            this.sendUsage(sender);
            return;
        }
        if (args[0].equalsIgnoreCase("start")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerData targetProfile = RevampSystem.getINSTANCE().getPlayerManagement().getPlayerData(player.getUniqueId());
                if (targetProfile.isInStaffMode()) return;
                for (Faction faction : this.getInstance().getFactionManager().getFactions()) {
                    if (faction instanceof PlayerFaction) {
                        this.getInstance().getFactionManager().removeFaction(faction, sender);
                    }
                }
                Command.broadcastCommandMessage(Bukkit.getConsoleSender(), "All factions have been disbanded.");
                Bukkit.setWhitelist(true);
                player.setHealth(20.0);
                player.setFoodLevel(20);
                player.removePotionEffect(PotionEffectType.SPEED);
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
                player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
                setEOTWFFA(true);
                Command.broadcastCommandMessage(Bukkit.getConsoleSender(), "FFA players has recived potion effects.");
                Location loc = WorldUtils.destringifyLocation(RevampHCF.getInstance().getLocation().getString("World-Spawn.eotw-ffa"));
                player.teleport(loc);
            }
        }
    }
    
    public void sendUsage(CommandSender sender) {
        sender.sendMessage(CC.translate("&cCorrect Usage: /eotwffa start."));
    }
    
    public static boolean isEOTWFFA() {
        return EOTWFFACommand.eotwffa;
    }
    
    public static void setEOTWFFA(boolean eotwffa) {
        EOTWFFACommand.eotwffa = eotwffa;
    }
}

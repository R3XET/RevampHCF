package eu.revamp.hcf.factions.commands.leader;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.scheduler.BukkitRunnable;
import eu.revamp.hcf.factions.utils.struction.Role;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;

import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionDisbandCommand extends CommandArgument
{
    private final RevampHCF plugin;
    private final ArrayList<String> kuldown;
    
    public FactionDisbandCommand(RevampHCF plugin) {
        super("disband", "Disband your faction.");
        this.plugin = plugin;
        this.kuldown = new ArrayList<>();
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName();
    }


    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (this.kuldown.contains(player.getName())) {
            sender.sendMessage("§cYou are on cooldown!");
            return true;
        }
        if (playerFaction == null) {
            sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
            return true;
        }
        if (playerFaction.isRaidable() && !RevampHCF.getInstance().getConfiguration().isKitMap() && !this.plugin.getHandlerManager().getEotwUtils().isEndOfTheWorld()) {
            sender.sendMessage(CC.translate("&cYou cannot disband your faction while it is raidable."));
            return true;
        }
        if (playerFaction.getMember(player.getUniqueId()).getRole() != Role.LEADER) {
            sender.sendMessage(CC.translate("&cYour cannot do this with this role."));
            return true;
        }
        playerFaction.broadcast(CC.translate("&c&l" + sender.getName() + " has disbanded the faction."));
        this.plugin.getFactionManager().removeFaction(playerFaction, sender);
        if (!player.isOp()) {
            this.kuldown.add(player.getName());
            new BukkitRunnable() {
                public void run() {
                    FactionDisbandCommand.this.kuldown.remove(player.getName());
                }
            }.runTaskLater(RevampHCF.getInstance(), 600L);
        }
        return true;
    }
}

package eu.revamp.hcf.factions.commands.leader;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.ChatColor;
import eu.revamp.hcf.factions.utils.struction.Role;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionOpenCommand extends CommandArgument
{
    private final RevampHCF plugin;
    
    public FactionOpenCommand(RevampHCF plugin) {
        super("open", "Opens the faction to the public.");
        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName();
    }


    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate("&cNo console."));
            return true;
        }
        Player player = (Player)sender;
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
            return true;
        }
        if (playerFaction.getMember(player.getUniqueId()).getRole() != Role.LEADER) {
            sender.sendMessage(CC.translate("&cYour cannot do this with this role."));
            return true;
        }
        if (RevampHCF.getInstance().getConfig().getBoolean("F_OPEN_IN_COMBAT_ENABLED")) {
            boolean newOpen = !playerFaction.isOpen();
            playerFaction.setOpen(newOpen);
            playerFaction.broadcast(ChatColor.GOLD + "Team " + ChatColor.GRAY + "» " + ChatColor.YELLOW + sender.getName() + " has " + (newOpen ? (ChatColor.GREEN + "opened") : (ChatColor.RED + "closed")) + ChatColor.YELLOW + " the faction to public.");
        }
        else {
            sender.sendMessage(CC.translate("&cThis command is disabled."));
        }
        return true;
    }
}

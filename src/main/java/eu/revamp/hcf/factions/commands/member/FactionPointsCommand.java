package eu.revamp.hcf.factions.commands.member;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.hcf.utils.inventory.BukkitUtils;
import eu.revamp.spigot.utils.chat.Clickable;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FactionPointsCommand extends CommandArgument
{
    public static Comparator<PlayerFaction> POINTS_COMPARATOR = Comparator.comparingInt(PlayerFaction::getPoints);

    public FactionPointsCommand(RevampHCF plugin) {
        super("top", "See a list of all factions.");
        this.isPlayerOnly = true;
    }
    
    public String getUsage(String label) {
        return "/" + label + " " + this.getName();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<PlayerFaction> data = new ArrayList<>(RevampHCF.getInstance().getFactionManager().getFactions().stream().filter(x -> x instanceof PlayerFaction).map(x -> (PlayerFaction) x).filter(x -> x.getPoints() > 0).collect(Collectors.toSet()));
        data.sort(FactionPointsCommand.POINTS_COMPARATOR);
        Collections.reverse(data);
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(CC.translate("&a&lFaction Top Points"));
        for (int i = 0; i < 5 && i < data.size(); ++i) {
            PlayerFaction next = data.get(i);

            Clickable clickable = new Clickable();
            clickable.add("&7" + (i + 1) + ") &a" + next.getName() + "&7: &f" + next.getPoints(), CC.translate("&eClick view &l" + next.getName() + " faction."), "/f show " + next.getName());
            clickable.sendToPlayer((Player) sender);
        }
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        return true;
    }
}

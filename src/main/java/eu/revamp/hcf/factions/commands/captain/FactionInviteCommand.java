package eu.revamp.hcf.factions.commands.captain;

import java.util.Set;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.spigot.utils.chat.color.CC;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import eu.revamp.hcf.factions.utils.struction.Relation;
import org.bukkit.Bukkit;
import eu.revamp.hcf.factions.utils.struction.Role;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.regex.Pattern;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionInviteCommand extends CommandArgument
{
    private final Pattern nameRegex;
    
    public FactionInviteCommand(RevampHCF plugin) {
        super("invite", "Invite a player to the faction.", new String[] { "inv" });
        this.nameRegex = Pattern.compile("^[a-zA-Z0-9_]{2,16}$");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <playerName>";
    }

    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
            return true;
        }
        if (!this.nameRegex.matcher(args[1]).matches()) {
            sender.sendMessage(CC.translate("&c&l" + args[1] + " &cis an invalid username."));
            return true;
        }
        Player player = (Player)sender;
        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
            return true;
        }
        if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
            sender.sendMessage(CC.translate("&cYour cannot do this with this role."));
            return true;
        }
        Set<String> invitedPlayerNames = playerFaction.getInvitedPlayerNames();
        String name = args[1];
        if (playerFaction.getMember(name) != null) {
            sender.sendMessage(CC.translate("&c&l" + name + "&cis already in your faction."));
            return true;
        }
        if (!RevampHCF.getInstance().getConfiguration().isKitMap() && !RevampHCF.getInstance().getHandlerManager().getEotwUtils().isEndOfTheWorld() && playerFaction.isRaidable()) {
            sender.sendMessage(CC.translate("&cYou may not invite players whilst your faction is raidable."));
            return true;
        }
        if (!invitedPlayerNames.add(name)) {
            sender.sendMessage(CC.translate("&c&l" + name + " &chas already been invited."));
            return true;
        }
        Player target = Bukkit.getPlayer(name);
        if (target != null) {
            name = target.getName();
            target.sendMessage(new ComponentBuilder(Relation.ENEMY.toChatColour() + sender.getName() + ChatColor.YELLOW + " has invited you to join " + ChatColor.GREEN + playerFaction.getName()).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, '/' + label + " accept " + playerFaction.getName())).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.GREEN + "Click to join " + playerFaction.getDisplayName(target) + ChatColor.GREEN + " to accept this invitation."))).create());
        }
        playerFaction.broadcast(CC.translate(Relation.MEMBER.toChatColour() + sender.getName() + " &ehas invited &f" + name + " &einto the faction."));
        return true;
    }
}

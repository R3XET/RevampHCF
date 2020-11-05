package eu.revamp.hcf.factions.commands.captain;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.ChatColor;
import eu.revamp.hcf.utils.chat.JavaUtils;
import eu.revamp.hcf.factions.utils.struction.Role;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.apache.commons.lang.time.DurationFormatUtils;
import java.util.concurrent.TimeUnit;

import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionTagCommand extends CommandArgument
{
    private static final long FACTION_RENAME_DELAY_MILLIS = TimeUnit.SECONDS.toMillis(15L);
    private static final String FACTION_RENAME_DELAY_WORDS = DurationFormatUtils.formatDurationWords(FactionTagCommand.FACTION_RENAME_DELAY_MILLIS, true, true);
    private final RevampHCF plugin;

    public FactionTagCommand(RevampHCF plugin) {
        super("rename", "Change the name of your faction.");
        this.plugin = plugin;
        this.aliases = new String[] { "tag" };
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <newFactionName>";
    }

    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
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
        String newName = args[1];
        if (newName.length() < RevampHCF.getInstance().getConfiguration().getFactionNameMinCharacters()) {
            sender.sendMessage(CC.translate("&cFaction names must have at least &l" + RevampHCF.getInstance().getConfiguration().getFactionNameMinCharacters() + " &ccharacters."));
            return true;
        }
        if (newName.length() > RevampHCF.getInstance().getConfiguration().getFactionNameMaxCharacters()) {
            sender.sendMessage(CC.translate("&cFaction names cannot be longer than &l" + RevampHCF.getInstance().getConfiguration().getFactionNameMaxCharacters() + " &ccharacters."));
            return true;
        }
        if (!JavaUtils.isAlphanumeric(newName)) {
            sender.sendMessage(CC.translate("&cFaction names may only be alphanumeric."));
            return true;
        }
        if (!sender.hasPermission("revamphcf.op") && newName.equalsIgnoreCase("EOTW")) {
            sender.sendMessage(CC.translate("&cInvalid name."));
            return true;
        }
        if (this.plugin.getFactionManager().getFaction(newName) != null) {
            sender.sendMessage(CC.translate("&c&l" + newName + " &calready exists."));
            return true;
        }
        long difference = playerFaction.lastRenameMillis - System.currentTimeMillis() + FactionTagCommand.FACTION_RENAME_DELAY_MILLIS;
        if (!player.isOp() && difference > 0L) {
            player.sendMessage(ChatColor.RED + "There is a faction rename delay of " + FactionTagCommand.FACTION_RENAME_DELAY_WORDS + ". Therefore you need to wait another " + DurationFormatUtils.formatDurationWords(difference, true, true) + " to rename your faction.");
            return true;
        }
        playerFaction.setName(args[1], sender);
        return true;
    }
}

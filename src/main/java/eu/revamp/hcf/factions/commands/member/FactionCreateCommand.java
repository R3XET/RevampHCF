package eu.revamp.hcf.factions.commands.member;

import com.google.common.base.Joiner;
import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.FactionType;
import eu.revamp.hcf.utils.chat.JavaUtils;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionCreateCommand extends CommandArgument
{
    public FactionCreateCommand(RevampHCF plugin) {
        super("create", "Create a faction.", new String[] { "make", "define" });
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <factionName>";
    }
    
    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Language.COMMANDS_FOR_PLAYER_USE_ONLY.toString());
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
            return true;
        }
        final String name = args[1];
        int value = RevampHCF.getInstance().getConfiguration().getFactionNameMinCharacters();
        if (name.length() < value) {
            sender.sendMessage(CC.translate("&cFaction names must have at least &l" + value + " &ccharacters."));
            return true;
        }
        FactionType factionType;
        if (args.length == 2 || !sender.hasPermission("revamphcf.op")) {
            value = RevampHCF.getInstance().getConfiguration().getFactionNameMaxCharacters();
            if (name.length() > value) {
                sender.sendMessage(CC.translate("&cFaction names cannot be longer than &l" + value + " &ccharacters."));
                return true;
            }
            if (RevampHCF.getInstance().getConfiguration().getBlockedfacnames().contains(name.toLowerCase())) {
                sender.sendMessage(ChatColor.RED + "'" + name + "' is a blocked faction name.");
                return true;
            }
            if (!sender.hasPermission("revamphcf.op") && name.equalsIgnoreCase("EOTW")) {
                sender.sendMessage(CC.translate("&cInvalid name."));
                return true;
            }
            if (!JavaUtils.isAlphanumeric(name)) {
                sender.sendMessage(CC.translate("&cInvalid name."));
                return true;
            }
            if (RevampHCF.getInstance().getFactionManager().getFaction(name) != null) {
                sender.sendMessage(CC.translate("&cFaction &l" + name + " &calready exists."));
                return true;
            }
            if (RevampHCF.getInstance().getFactionManager().getPlayerFaction((Player)sender) != null) {
                sender.sendMessage(Language.FACTIONS_ALREADY_IN_A_FACTION.toString());
                return true;
            }
            factionType = FactionType.PLAYER;
        }
        else {
            try {
                factionType = FactionType.valueOf(args[2]);
            }
            catch (IllegalArgumentException ex) {
                sender.sendMessage(CC.translate("Invalid faction type '&l" + name + "&c'. Valid names are &l" + Joiner.on("&c, &l").join(FactionType.values())));
                return true;
            }
        }
        RevampHCF.getInstance().getFactionManager().createFaction(sender, name, factionType);
        if (factionType != FactionType.PLAYER) {
            sender.sendMessage(CC.translate("&aFaction of type &l" + factionType.name() + " &acreated with name &l" + name + "&a."));
        }
        return true;
    }
}

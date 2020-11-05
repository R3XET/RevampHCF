package eu.revamp.hcf.games.event.argument;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.utils.games.EventFaction;
import eu.revamp.hcf.games.cuboid.Cuboid;
import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EventSetAreaArgument extends CommandArgument {

    private final RevampHCF plugin;
    private final int MIN_EVENT_CLAIM_AREA;

    public EventSetAreaArgument(RevampHCF plugin) {
        super("setarea", "Set event area");
        this.plugin = plugin;
        this.permission = "event.command." + this.getName();
        this.MIN_EVENT_CLAIM_AREA = 8;
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <eventName>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(CC.translate("&cPlease specify name of area."));
            return false;
        }
        WorldEditPlugin worldEditPlugin = this.plugin.getWorldEdit();
        if (worldEditPlugin == null) {
            sender.sendMessage(CC.translate("&cWorldEdit must be installed to set event claim areas."));
            return false;
        }
        Player player = (Player) sender;
        Selection selection = worldEditPlugin.getSelection(player);
        if (selection == null) {
            sender.sendMessage(CC.translate("&cYou must make a WorldEdit selection to do this."));
            return false;
        }
        if (selection.getWidth() < this.MIN_EVENT_CLAIM_AREA || selection.getLength() < this.MIN_EVENT_CLAIM_AREA) {
            sender.sendMessage(CC.translate("&cEvent claim areas must be at least &l" + this.MIN_EVENT_CLAIM_AREA + "&cx&l" + this.MIN_EVENT_CLAIM_AREA));
            return false;
        }
        Faction faction3 = this.plugin.getFactionManager().getFaction(args[1]);
        if (!(faction3 instanceof EventFaction)) {
            sender.sendMessage(CC.translate("&cThere is not an event faction named &l" + args[1]));
            return false;
        }
        ((EventFaction)faction3).setClaim(new Cuboid(selection.getMinimumPoint(), selection.getMaximumPoint()), player);
        sender.sendMessage(CC.translate("&eYou have successfully updated the claim for event &f" + faction3.getName()));
        return false;
    }
}

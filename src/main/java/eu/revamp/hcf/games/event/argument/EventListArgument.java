package eu.revamp.hcf.games.event.argument;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.utils.games.KothFaction;
import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class EventListArgument extends CommandArgument {
    private final RevampHCF plugin;

    public EventListArgument(RevampHCF plugin) {
        super("list", "Display koth list");
        this.plugin = plugin;
        this.permission = "event.command." + this.getName();
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<String> all = new ArrayList<>();
        for (Faction faction : this.plugin.getFactionManager().getFactions()) {
            if (faction instanceof KothFaction) {
                all.add(faction.getName());
            }
        }
        sender.sendMessage(CC.translate("&eCurrent Events&7: &d" + all.toString().replace("[", "").replace("]", "").replace(",", "&e,&d")));
        return false;
    }
}

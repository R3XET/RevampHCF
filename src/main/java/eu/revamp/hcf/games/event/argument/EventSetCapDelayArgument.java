package eu.revamp.hcf.games.event.argument;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.utils.games.EventFaction;
import eu.revamp.hcf.factions.utils.games.KothFaction;
import eu.revamp.hcf.factions.utils.zone.CaptureZone;
import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.time.TimeUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class EventSetCapDelayArgument extends CommandArgument {
    private final RevampHCF plugin;

    public EventSetCapDelayArgument(RevampHCF plugin) {
        super("setcapdelay", "Rename event");
        this.plugin = plugin;
        this.permission = "event.command." + this.getName();
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <eventName> <delay>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(CC.translate("&cPlease specify name."));
            return false;
        }
        Faction faction2 = this.plugin.getFactionManager().getFaction(args[1]);
        if (faction2 == null || !(faction2 instanceof KothFaction)) {
            sender.sendMessage(CC.translate("&cThere is not a KOTH arena named &l" + args[1]));
            return false;
        }
        long duration = TimeUtils.parse(StringUtils.join(args, ' ', 2, args.length));
        if (duration == -1L) {
            sender.sendMessage(Language.COMMANDS_INVALID_DURATION.toString());
            return false;
        }
        KothFaction kothFaction = (KothFaction)faction2;
        CaptureZone captureZone2 = kothFaction.getCaptureZone();
        if (captureZone2 == null) {
            sender.sendMessage(CC.translate("&c&l" + kothFaction.getDisplayName(sender) + " &cdoes not have a capture zone."));
            return false;
        }
        if (captureZone2.isActive() && duration < captureZone2.getRemainingCaptureMillis()) {
            captureZone2.setRemainingCaptureMillis(duration);
        }
        captureZone2.setDefaultCaptureMillis(duration);
        sender.sendMessage(CC.translate("&eYou have successfully set the capture delay of KOTH arena &f" + kothFaction.getDisplayName(sender) + " &eto &f" + DurationFormatUtils.formatDurationWords(duration, true, true)));

        return false;
    }
}

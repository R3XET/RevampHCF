package eu.revamp.hcf.games.event;

import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class EventCommand extends CommandArgument {
    public EventCommand(EventExecutor executor) {
        super("help", "View help on how to use events.");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("event.command"))
        this.showPage(sender);
        return true;
    }

    private void showPage(CommandSender sender) {
        sender.sendMessage(CC.translate("&7&m---------------------------------------"));
        sender.sendMessage(CC.translate("&6&lKoTH &7- &eHelp Commands"));
        sender.sendMessage(CC.translate("  &7/koth create (kothName) (type) &f- Create KoTH."));
        sender.sendMessage(CC.translate("  &7/koth delete (kothName) (type) &f- Delete KoTH."));
        sender.sendMessage(CC.translate("  &7/koth setarea (kothName) &f- Set KoTH area."));
        sender.sendMessage(CC.translate("  &7/koth setcapzone (kothName) &f- Set KoTH capzone."));
        sender.sendMessage(CC.translate("  &7/koth start (kothName) &f- Start KoTH."));
        sender.sendMessage(CC.translate("  &7/koth setcapdelay (kothName) (delay) &f- SetCapDelay of KoTH."));
        sender.sendMessage(CC.translate("  &7/koth stop (kothName) &f- Stop KoTH."));
        sender.sendMessage(CC.translate("  &7/koth rename (kothName) (newKothName) &f- Rename KoTH."));
        sender.sendMessage(CC.translate("  &7/koth setconquestpoints (factionName) (points) &f- Set faction points."));
        sender.sendMessage(CC.translate("  &7/koth schedule create (kothName) (day) (time) (capTime) &f- Create Koth Schedule."));
        sender.sendMessage(CC.translate("&7&m---------------------------------------"));
    }
}

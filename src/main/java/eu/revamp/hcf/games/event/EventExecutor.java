package eu.revamp.hcf.games.event;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.games.event.argument.*;
import eu.revamp.hcf.utils.command.ArgumentExecutor;
import eu.revamp.hcf.utils.command.CommandArgument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class EventExecutor extends ArgumentExecutor {
    private final CommandArgument helpArgument;

    public EventExecutor(RevampHCF plugin) {
        super("event");
        this.addArgument(new EventCreateArgument(plugin));
        this.addArgument(new EventDeleteArgument(plugin));
        this.addArgument(new EventListArgument(plugin));
        this.addArgument(new EventRenameArgument(plugin));
        this.addArgument(new EventScheduleArgument(plugin));
        this.addArgument(new EventSetAreaArgument(plugin));
        this.addArgument(new EventSetCapDelayArgument(plugin));
        this.addArgument(new EventSetCapzoneArgument(plugin));
        this.addArgument(new EventSetConquestPoints(plugin));
        this.addArgument(new EventStartArgument(plugin));
        this.addArgument(new EventStopArgument(plugin));
        this.addArgument(new EventTeleportArgument(plugin));

        this.addArgument(this.helpArgument = new EventCommand(this));

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            this.helpArgument.onCommand(sender, command, label, args);
            return true;
        }
        CommandArgument argument = this.getArgument(args[0]);
        if (argument != null) {
            String permission = argument.getPermission();
            if (permission == null || sender.hasPermission(permission)) {
                argument.onCommand(sender, command, label, args);
                return true;
            }
        }
        this.helpArgument.onCommand(sender, command, label, args);
        return true;
    }
}

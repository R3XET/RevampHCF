package eu.revamp.hcf.games.event.argument;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.games.schedule.ScheduleData;
import eu.revamp.hcf.games.schedule.ScheduleType;
import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.generic.ConversionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EventScheduleArgument extends CommandArgument {
    private final RevampHCF plugin;

    public EventScheduleArgument(RevampHCF plugin) {
        super("schedule", "Set conquest points");
        this.plugin = plugin;
        this.permission = "event.command." + this.getName();
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " create <kothName> <day> <time> <capTime>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("revamphcf.op")) {
            Player player = (Player) sender;
            this.asPlayer(player, args);
        }
        else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                this.plugin.getHandlerManager().getScheduleHandler().sendAdminKothScheduleList(sender);
            }
            else if (args[0].equalsIgnoreCase("clear")) {
                this.plugin.getHandlerManager().getScheduleHandler().clearSchedule(sender);
            }
            else if (args[0].equalsIgnoreCase("create")) {
                sender.sendMessage(CC.translate("&cCorrect Usage: /koth schedule create <kothName>"));
            }
            else if (args[0].equalsIgnoreCase("remove")) {
                sender.sendMessage(CC.translate("&cCorrect Usage: /koth schedule remove <kothName>"));
            }
        }
        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")) {
                String string = String.valueOf(args[1]);
                if (!ConversionUtils.isInteger(string)) {
                    sender.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
                    return false;
                }
                int n = Integer.parseInt(string);
                ScheduleData scheduleData = this.plugin.getHandlerManager().getScheduleHandler().getScheduleData(ScheduleType.KOTH, n);
                if (scheduleData == null) {
                    sender.sendMessage(CC.translate("&cThat schedule does not exists."));
                }
                else {
                    this.plugin.getHandlerManager().getScheduleHandler().removeScheduleData(ScheduleType.KOTH, n);
                    sender.sendMessage(CC.translate("&eYou have successfully removed Schedule ID &f" + n));
                }
            }
        }
        else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("create")) {
                this.checkAndCreateSchedule(sender, args, String.valueOf(15));
            }
        }
        else if (args.length == 5) {
            if (args[0].equalsIgnoreCase("create")) {
                this.checkAndCreateSchedule(sender, args, args[4]);
            }
        }
        return false;
    }

    private void asPlayer(Player player, String[] args) {
        this.plugin.getHandlerManager().getScheduleHandler().sendPlayerScheduleList(ScheduleType.KOTH, player);
    }

    private void checkAndCreateSchedule(CommandSender sender, String[] args, String string) {
        String eventName = args[1];
        String day = StringUtils.capitalize(args[2]);
        String time = args[3];
        Faction faction = RevampHCF.getInstance().getFactionManager().getFaction(args[1]);
        if (faction == null) {
            sender.sendMessage(CC.translate("&cThere is not a KOTH arena named &l" + eventName));
        }
        else if (!this.isDayValid(day) && !day.equalsIgnoreCase("all")) {
            sender.sendMessage(CC.translate("&c&lDay Format&c incorrect. Use like: &lMonday, Tuesday &cetc."));
        }
        else if (!this.isTimeValid(time)) {
            sender.sendMessage(CC.translate("&cTime format incorrect."));
        }
        else if (!ConversionUtils.isInteger(string)) {
            sender.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
        }
        else {
            int n = Integer.parseInt(string);
            if (day.equalsIgnoreCase("all") || day.equalsIgnoreCase("daily")) {
                String[] days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
                for (String string5 : days) {
                    int n4 = this.plugin.getHandlerManager().getScheduleHandler().getSchedules().size() + 1;
                    this.plugin.getHandlerManager().getScheduleHandler().addScheduleData(n4, faction.getName(), ScheduleType.KOTH, string5, this.getCorrectTime(time), n);
                    sender.sendMessage(CC.translate("&eYou have successfully created schedule with id &f" + n4 + "&b."));
                }
            }
            else {
                int n5 = this.plugin.getHandlerManager().getScheduleHandler().getSchedules().size() + 1;
                this.plugin.getHandlerManager().getScheduleHandler().addScheduleData(n5, faction.getName(), ScheduleType.KOTH, day, this.getCorrectTime(time), n);
                sender.sendMessage(CC.translate("&eYou have successfully created schedule with id &f" + n5 + "&b."));
            }
        }
    }

    private boolean isDayValid(String string) {
        return string.equalsIgnoreCase("Monday") || string.equalsIgnoreCase("Tuesday") || string.equalsIgnoreCase("Wednesday") || string.equalsIgnoreCase("Thursday") || string.equalsIgnoreCase("Friday") || string.equalsIgnoreCase("Saturday") || string.equalsIgnoreCase("Sunday");
    }

    private boolean isTimeValid(String string) {
        if (string.contains(":")) {
            String[] args = string.split(":");
            if (ConversionUtils.isInteger(args[0]) && ConversionUtils.isInteger(args[0])) {
                int n2 = Integer.parseInt(args[0]);
                int n3 = Integer.parseInt(args[1]);
                return n2 >= 0 && n2 < 24 && n3 >= 0 && n3 < 60;
            }
        }
        else {
            int n4;
            return ConversionUtils.isInteger(string) && (n4 = Integer.parseInt(string)) >= 0 && n4 < 24;
        }
        return false;
    }

    private String getCorrectTime(String string) {
        String string2;
        string2 = (string.contains(":") ? string : (string + ":00"));
        return string2;
    }
}


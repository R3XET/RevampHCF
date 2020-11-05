package eu.revamp.hcf.commands.games;

import com.google.common.collect.ImmutableList;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.FactionManager;
import eu.revamp.hcf.factions.FactionType;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.utils.games.CapturableFaction;
import eu.revamp.hcf.factions.utils.games.ConquestFaction;
import eu.revamp.hcf.factions.utils.games.EventFaction;
import eu.revamp.hcf.factions.utils.games.KothFaction;
import eu.revamp.hcf.factions.utils.zone.CaptureZone;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import eu.revamp.hcf.games.ConquestType;
import eu.revamp.hcf.games.GameType;
import eu.revamp.hcf.games.KothType;
import eu.revamp.hcf.games.cuboid.Cuboid;
import eu.revamp.hcf.games.schedule.ScheduleData;
import eu.revamp.hcf.games.schedule.ScheduleType;
import eu.revamp.hcf.timers.GameHandler;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.generic.ConversionUtils;
import eu.revamp.spigot.utils.time.TimeUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class KoTHCommand extends BaseCommand {
    private static final int MIN_EVENT_CLAIM_AREA = 8;
    public DateTimeFormatter HHMMA;

    public KoTHCommand(RevampHCF plugin) {
        super(plugin);
        this.HHMMA = DateTimeFormatter.ofPattern("h:mma");
        this.command = "koth";
    }

    @Override @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        if (sender.hasPermission("*")) {
            if (args.length == 0) {
                this.sendUsage(sender);
                return;
            }
            if (args[0].equalsIgnoreCase("create")) {
                if (args.length == 1) {
                    sender.sendMessage(CC.translate("&cPlease specify name."));
                    return;
                }
                if (args.length == 2) {
                    sender.sendMessage(CC.translate("&cPlease specify type."));
                    return;
                }
                Faction faction2 = RevampHCF.getInstance().getFactionManager().getFaction(args[1]);
                if (faction2 != null) {
                    sender.sendMessage(CC.translate("&cThere is already a faction named &l" + args[1]));
                    return;
                }
                gameType: {
                    switch (args[2].toUpperCase()) {
                        case "KOTH": {
                            faction2 = new KothFaction(args[1]);
                            break gameType;
                        }
                        case "CONQUEST": {
                            faction2 = new ConquestFaction(args[1]);
                            break gameType;
                        }
                        default:
                            break;
                    }
                    this.sendUsage(sender);
                    return;
                }
                FactionType factionType = FactionType.valueOf(args[2]);
                String name = args[1];
                RevampHCF.getInstance().getFactionManager().createFaction(sender, name, factionType);
                sender.sendMessage(CC.translate("&eYou have successfully created event faction &f" + faction2.getDisplayName(sender) + " &ewith type &f" + WordUtils.capitalizeFully(args[2])));
            }
            else if (args[0].equalsIgnoreCase("setarea")) {
                if (args.length == 1) {
                    sender.sendMessage(CC.translate("&cPlease specify name of area."));
                    return;
                }
                WorldEditPlugin worldEditPlugin = RevampHCF.getInstance().getWorldEdit();
                if (worldEditPlugin == null) {
                    sender.sendMessage(CC.translate("&cWorldEdit must be installed to set event claim areas."));
                    return;
                }
                Selection selection = worldEditPlugin.getSelection(player);
                if (selection == null) {
                    sender.sendMessage(CC.translate("&cYou must make a WorldEdit selection to do this."));
                    return;
                }
                if (selection.getWidth() < KoTHCommand.MIN_EVENT_CLAIM_AREA || selection.getLength() < KoTHCommand.MIN_EVENT_CLAIM_AREA) {
                    sender.sendMessage(CC.translate("&cEvent claim areas must be at least &l" + KoTHCommand.MIN_EVENT_CLAIM_AREA + "&cx&l" + KoTHCommand.MIN_EVENT_CLAIM_AREA));
                    return;
                }
                Faction faction3 = RevampHCF.getInstance().getFactionManager().getFaction(args[1]);
                if (!(faction3 instanceof EventFaction)) {
                    sender.sendMessage(CC.translate("&cThere is not an event faction named &l" + args[1]));
                    return;
                }
                ((EventFaction)faction3).setClaim(new Cuboid(selection.getMinimumPoint(), selection.getMaximumPoint()), player);
                sender.sendMessage(CC.translate("&eYou have successfully updated the claim for event &f" + faction3.getName()));
            }
            else if (args[0].equalsIgnoreCase("setcapzone")) {
                if (args.length == 1) {
                    sender.sendMessage(CC.translate("&cPlease specify name of capzone."));
                    return;
                }
                WorldEditPlugin worldEdit = RevampHCF.getInstance().getWorldEdit();
                if (worldEdit == null) {
                    sender.sendMessage(CC.translate("&cWorldEdit must be installed to set event capture zone."));
                    return;
                }
                Selection selection = worldEdit.getSelection((Player)sender);
                if (selection == null) {
                    sender.sendMessage(CC.translate("&cYou must make a WorldEdit selection to do this."));
                    return;
                }
                if (selection.getWidth() < 2 || selection.getLength() < 2) {
                    sender.sendMessage(CC.translate("&cCapzones must be at least &l" + KoTHCommand.MIN_EVENT_CLAIM_AREA + "&cx&l" + KoTHCommand.MIN_EVENT_CLAIM_AREA));
                    return;
                }
                Faction faction3 = RevampHCF.getInstance().getFactionManager().getFaction(args[1]);
                if (!(faction3 instanceof CapturableFaction)) {
                    sender.sendMessage(CC.translate("&cThere is not a capturable faction named &l" + args[1]));
                    return;
                }
                CapturableFaction capturableFaction = (CapturableFaction)faction3;
                Collection<ClaimZone> claims = capturableFaction.getClaims();
                if (claims.isEmpty()) {
                    sender.sendMessage(CC.translate("&cCapture zones can only be inside the event claim."));
                    return;
                }
                ClaimZone claim = new ClaimZone(faction3, selection.getMinimumPoint(), selection.getMaximumPoint());
                World world = claim.getWorld();
                int minimumX = claim.getMinimumX();
                int maximumX = claim.getMaximumX();
                int minimumZ = claim.getMinimumZ();
                int maximumZ = claim.getMaximumZ();
                FactionManager factionManager = RevampHCF.getInstance().getFactionManager();
                for (int x = minimumX; x <= maximumX; ++x) {
                    for (int z = minimumZ; z <= maximumZ; ++z) {
                        Faction factionAt = factionManager.getFactionAt(world, x, z);
                        if (factionAt != capturableFaction) {
                            sender.sendMessage(CC.translate("&cCapture zones can only be inside the event claim."));
                            return;
                        }
                    }
                }
                CaptureZone captureZone;
                if (capturableFaction instanceof ConquestFaction) {
                    if (args.length < 3) {
                        this.sendUsage(sender);
                        return;
                    }
                    ConquestFaction conquestFaction = (ConquestFaction)capturableFaction;
                    ConquestFaction.ConquestZone conquestZone = ConquestFaction.ConquestZone.getByName(args[2]);
                    if (conquestZone == null) {
                        sender.sendMessage(CC.translate("&cThere is no conquest zone named &l" + args[2]));
                        sender.sendMessage(CC.translate("&cDid you mean?: &l" + StringUtils.join(ConquestFaction.ConquestZone.getNames(), ", ")));
                        return;
                    }
                    captureZone = new CaptureZone(conquestZone.getName(), conquestZone.getColor().toString(), claim, ConquestType.DEFAULT_CAP_MILLIS);
                    conquestFaction.setZone(conquestZone, captureZone);
                }
                else {
                    if (!(capturableFaction instanceof KothFaction)) {
                        sender.sendMessage(CC.translate("&cCan only set capture zones for Conquest or KOTH factions."));
                        return;
                    }
                    ((KothFaction)capturableFaction).setCaptureZone(captureZone = new CaptureZone(capturableFaction.getName(), claim, KothType.DEFAULT_CAP_MILLIS));
                }
                sender.sendMessage(CC.translate("&eYou have successfully set capture zone &f" + captureZone.getDisplayName() + " &efor faction &f" + faction3.getName()));
            }
            else if (player.hasPermission("revamphcf.op") && args[0].equalsIgnoreCase("start")) {
                if (args.length == 1) {
                    sender.sendMessage(CC.translate("&cPlease specify name."));
                    return;
                }
                Faction faction2 = RevampHCF.getInstance().getFactionManager().getFaction(args[1]);
                if (!(faction2 instanceof EventFaction)) {
                    sender.sendMessage(CC.translate("&cThere is no event named &l" + args[1]));
                    return;
                }
                if (RevampHCF.getInstance().getHandlerManager().getTimerManager().getGameHandler().tryContesting((EventFaction)faction2, sender)) {
                    sender.sendMessage(CC.translate("&eYou have Successfully started &f" + faction2.getName()));
                    for (String message : RevampHCF.getInstance().getLanguage().getStringList("KOTH")){
                        Bukkit.broadcastMessage(CC.translate(message).replace("%koth%", faction2.getName()).replace("%time%", TimeUtils.getRemaining(RevampHCF.getInstance().getHandlerManager().getTimerManager().getGameHandler().getRemaining(), true)));
                    }
                }
            }
            else if ((player.hasPermission("revamphcf.op") && args[0].equalsIgnoreCase("stop")) || (player.hasPermission("revamphcf.op") && args[0].equalsIgnoreCase("end"))) {
                GameHandler eventTimer = RevampHCF.getInstance().getHandlerManager().getTimerManager().getGameHandler();
                Faction eventFaction = eventTimer.getEventFaction();
                if (!eventTimer.clearCooldown()) {
                    sender.sendMessage(CC.translate("&cThere is not a running event."));
                    return;
                }
                Bukkit.broadcastMessage(CC.translate("&f" + sender.getName() + " &ehas cancelled &f" + ((eventFaction == null) ? "the active event" : (eventFaction.getName() + "&e")) + "."));
            }
            else if (args[0].equalsIgnoreCase("tp")) {
                if (args.length == 1) {
                    sender.sendMessage(CC.translate("&cYou must put Event Name!"));
                    return;
                }
                Faction faction2 = RevampHCF.getInstance().getFactionManager().getFaction(args[1]);
                if (!(faction2 instanceof KothFaction)) {
                    sender.sendMessage(CC.translate("&cThere is no event named &l" + args[1] + "&c!"));
                    return;
                }
                Location loc = ((KothFaction)faction2).getCaptureZone().getCuboid().getCenter();
                player.teleport(loc);
                sender.sendMessage(CC.translate("&eYou have been teleported to &d" + args[1] + " &eevent!"));
            }
            else if (args[0].equalsIgnoreCase("list")) {
                List<String> all = new ArrayList<>();
                for (Faction faction4 : RevampHCF.getInstance().getFactionManager().getFactions()) {
                    if (faction4 instanceof KothFaction) {
                        all.add(faction4.getName());
                    }
                }
                sender.sendMessage(CC.translate("&eCurrent Events&7: &d" + all.toString().replace("[", "").replace("]", "").replace(",", "&e,&d")));
            }
            else if (args[0].equalsIgnoreCase("rename") || args[0].equalsIgnoreCase("tag")) {
                if (args.length == 1) {
                    sender.sendMessage(CC.translate("&cPlease specify name."));
                    return;
                }
                if (args.length == 2) {
                    sender.sendMessage(CC.translate("&cPlease specify second name."));
                    return;
                }
                Faction faction2 = RevampHCF.getInstance().getFactionManager().getFaction(args[2]);
                if (faction2 != null) {
                    sender.sendMessage(CC.translate("&cThere is already a faction named &l" + args[2]));
                    return;
                }
                faction2 = RevampHCF.getInstance().getFactionManager().getFaction(args[1]);
                if (!(faction2 instanceof EventFaction)) {
                    sender.sendMessage(CC.translate("&cThere is not an event faction named &l" + args[1]));
                    return;
                }
                String oldName = faction2.getName();
                faction2.setName(args[2], sender);
                sender.sendMessage(CC.translate("&eYou have successfully renamed event &f" + oldName + " &fto &f" + faction2.getName()));
            }
            else if (args[0].equalsIgnoreCase("setcapdelay")) {
                if (args.length == 1) {
                    sender.sendMessage(CC.translate("&cPlease specify name."));
                    return;
                }
                Faction faction2 = RevampHCF.getInstance().getFactionManager().getFaction(args[1]);
                if (faction2 == null || !(faction2 instanceof KothFaction)) {
                    sender.sendMessage(CC.translate("&cThere is not a KOTH arena named &l" + args[1]));
                    return;
                }
                long duration = TimeUtils.parse(StringUtils.join(args, ' ', 2, args.length));
                if (duration == -1L) {
                    sender.sendMessage(Language.COMMANDS_INVALID_DURATION.toString());
                    return;
                }
                KothFaction kothFaction = (KothFaction)faction2;
                CaptureZone captureZone2 = kothFaction.getCaptureZone();
                if (captureZone2 == null) {
                    sender.sendMessage(CC.translate("&c&l" + kothFaction.getDisplayName(sender) + " &cdoes not have a capture zone."));
                    return;
                }
                if (captureZone2.isActive() && duration < captureZone2.getRemainingCaptureMillis()) {
                    captureZone2.setRemainingCaptureMillis(duration);
                }
                captureZone2.setDefaultCaptureMillis(duration);
                sender.sendMessage(CC.translate("&eYou have successfully set the capture delay of KOTH arena &f" + kothFaction.getDisplayName(sender) + " &eto &f" + DurationFormatUtils.formatDurationWords(duration, true, true)));
            }
            else if (args[0].equalsIgnoreCase("setconquestpoints")) {
                if (args.length < 3) {
                    this.sendUsage(sender);
                    return;
                }
                Faction faction2 = RevampHCF.getInstance().getFactionManager().getFaction(args[1]);
                if (!(faction2 instanceof PlayerFaction)) {
                    sender.sendMessage(Language.FACTIONS_FACTION_NOT_FOUND.toString());
                    return;
                }
                if (!ConversionUtils.isInteger(args[2])){
                    sender.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
                    return;
                }
                int amount = Integer.parseInt(args[2]);
                if (amount > 300) {
                    sender.sendMessage(CC.translate("&cMaximum points for Conquest is 300."));
                    return;
                }
                PlayerFaction playerFaction = (PlayerFaction)faction2;
                ((ConquestType)GameType.CONQUEST.getEventTracker()).setPoints(playerFaction, amount);
                sender.sendMessage(CC.translate("&esuccessfully set the points of faction &f" + playerFaction.getName() + " &eto &f" + amount + "&e."));
            }
            else if (args[0].equalsIgnoreCase("delete")) {
                if (args.length < 2) {
                    this.sendUsage(sender);
                    return;
                }
                Faction faction2 = RevampHCF.getInstance().getFactionManager().getFaction(args[1]);
                if (!(faction2 instanceof EventFaction)) {
                    sender.sendMessage(CC.translate("&cThere is already a faction named &l" + args[1]));
                    return;
                }
                if (RevampHCF.getInstance().getFactionManager().removeFaction(faction2, sender)) {
                    sender.sendMessage(CC.translate("&esuccessfully deleted event faction &f" + faction2.getDisplayName(sender) + "&e."));
                }
            }
            else if (args[0].equalsIgnoreCase("list")) {
                ImmutableList<Faction> facs = RevampHCF.getInstance().getFactionManager().getFactions();
                Faction fac = this.getInstance().getFactionManager().getPlayerFaction((Player)sender);
                if (fac instanceof EventFaction) {
                    facs.stream().sorted(Comparator.comparing(Faction::getName)).forEach(koth -> sender.sendMessage(String.valueOf(koth)));
                }
                facs.stream().filter(faction -> faction instanceof EventFaction).map(Faction::getName).collect((Collector<? super Object, ?, List<Object>>)Collectors.toList());
            }
            else if (args[0].equalsIgnoreCase("schedule")) {
                if (!sender.hasPermission("revamphcf.op")) {
                    this.asPlayer(player, args);
                }
                else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("list")) {
                        this.getInstance().getHandlerManager().getScheduleHandler().sendAdminKothScheduleList(sender);
                    }
                    else if (args[0].equalsIgnoreCase("clear")) {
                        this.getInstance().getHandlerManager().getScheduleHandler().clearSchedule(sender);
                    }
                    else if (args[0].equalsIgnoreCase("create")) {
                        sender.sendMessage(CC.translate("&cCorrect Usage: /koth schedule create <kothName>"));
                    }
                    else if (args[0].equalsIgnoreCase("remove")) {
                        sender.sendMessage(CC.translate("&cCorrect Usage: /koth schedule remove <kothName>"));
                    }
                    else {
                        this.sendUsage(sender);
                    }
                }
                else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")) {
                        String string = String.valueOf(args[1]);
                        if (!ConversionUtils.isInteger(string)) {
                            sender.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
                            return;
                        }
                        int n = Integer.parseInt(string);
                        ScheduleData scheduleData = this.getInstance().getHandlerManager().getScheduleHandler().getScheduleData(ScheduleType.KOTH, n);
                        if (scheduleData == null) {
                            sender.sendMessage(CC.translate("&cThat schedule does not exists."));
                        }
                        else {
                            this.getInstance().getHandlerManager().getScheduleHandler().removeScheduleData(ScheduleType.KOTH, n);
                            sender.sendMessage(CC.translate("&eYou have successfully removed Schedule ID &f" + n));
                        }
                    }
                    else {
                        this.sendUsage(sender);
                    }
                }
                else if (args.length == 4) {
                    if (args[0].equalsIgnoreCase("create")) {
                        this.checkAndCreateSchedule(sender, args, String.valueOf(15));
                    }
                    else {
                        sender.sendMessage("Koth Usage 392 Line");
                    }
                }
                else if (args.length == 5) {
                    if (args[0].equalsIgnoreCase("create")) {
                        this.checkAndCreateSchedule(sender, args, args[4]);
                    }
                    else {
                        sender.sendMessage("Koth Usage 398 Line");
                    }
                }
                else {
                    sender.sendMessage("Koth Usage 401 Line");
                }
            }
        }
        else {
            RevampHCF.getInstance().getHandlerManager().getScheduleHandler().sendPlayerScheduleList(ScheduleType.KOTH, sender);
        }
    }
    
    public void sendUsage(CommandSender sender) {
        sender.sendMessage(CC.translate("&6&lKoTH &7- &eHelp Commands"));
        sender.sendMessage(CC.translate("  &7/koth create (kothName) (type) &f- Create KoTH."));
        sender.sendMessage(CC.translate("  &7/koth setarea (kothName) &f- Set KoTH area."));
        sender.sendMessage(CC.translate("  &7/koth setcapzone (kothName) &f- Set KoTH capzone."));
        sender.sendMessage(CC.translate("  &7/koth start (kothName) &f- Start KoTH."));
        sender.sendMessage(CC.translate("  &7/koth setcapdelay (kothName) &f- SetCapDelay of KoTH."));
        sender.sendMessage(CC.translate("  &7/koth stop (kothName) &f- Stop KoTH."));
        sender.sendMessage(CC.translate("  &7/koth rename (kothName) (newKothName) &f- Rename KoTH."));
        sender.sendMessage(CC.translate("  &7/koth setconquestpoints (factionName) (points) &f- Set faction points."));
        sender.sendMessage(CC.translate("  &7/koth schedule create (kothName) (day) (time) (capTime) &f- Create Koth Schedule."));
    }
    
    public void asPlayer(Player player, String[] args) {
        this.getInstance().getHandlerManager().getScheduleHandler().sendPlayerScheduleList(ScheduleType.KOTH, player);
    }
    
    public void checkAndCreateSchedule(CommandSender sender, String[] args, String string) {
        String string2 = args[1];
        String string3 = StringUtils.capitalize(args[2]);
        String string4 = args[3];
        Faction faction = RevampHCF.getInstance().getFactionManager().getFaction(args[1]);
        if (faction == null) {
            sender.sendMessage(CC.translate("&cThere is not a KOTH arena named &l" + string2));
        }
        else if (!this.isDayValid(string3) && !string3.equalsIgnoreCase("all")) {
            sender.sendMessage(CC.translate("&c&lDay Format&c incorrect. Use like: &lMonday, Tuesday &cetc."));
        }
        else if (!this.isTimeValid(string4)) {
            sender.sendMessage(CC.translate("&cTime format incorrect."));
        }
        else if (!ConversionUtils.isInteger(string)) {
            sender.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
        }
        else {
            int n = Integer.parseInt(string);
            if (string3.equalsIgnoreCase("all") || string3.equalsIgnoreCase("daily")) {
                String[] args3 = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
                for (String string5 : args3) {
                    int n4 = this.getInstance().getHandlerManager().getScheduleHandler().getSchedules().size() + 1;
                    this.getInstance().getHandlerManager().getScheduleHandler().addScheduleData(n4, faction.getName(), ScheduleType.KOTH, string5, this.getCorrectTime(string4), n);
                    sender.sendMessage(CC.translate("&eYou have successfully created schedule with id &f" + n4 + "&b."));
                }
            }
            else {
                int n5 = this.getInstance().getHandlerManager().getScheduleHandler().getSchedules().size() + 1;
                this.getInstance().getHandlerManager().getScheduleHandler().addScheduleData(n5, faction.getName(), ScheduleType.KOTH, string3, this.getCorrectTime(string4), n);
                sender.sendMessage(CC.translate("&eYou have successfully created schedule with id &f" + n5 + "&b."));
            }
        }
    }
    
    public boolean isDayValid(String string) {
        return string.equalsIgnoreCase("Monday") || string.equalsIgnoreCase("Tuesday") || string.equalsIgnoreCase("Wednesday") || string.equalsIgnoreCase("Thursday") || string.equalsIgnoreCase("Friday") || string.equalsIgnoreCase("Saturday") || string.equalsIgnoreCase("Sunday");
    }
    
    public boolean isTimeValid(String string) {
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
    
    public String getCorrectTime(String string) {
        String string2;
        string2 = (string.contains(":") ? string : (string + ":00"));
        return string2;
    }
}

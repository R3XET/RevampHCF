package eu.revamp.hcf.games.schedule;

import java.time.format.TextStyle;
import java.util.EnumSet;
import java.time.DayOfWeek;

import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.message.MessageUtils;
import lombok.Getter;

import java.util.Comparator;

import lombok.Setter;
import org.apache.commons.lang.time.FastDateFormat;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import java.util.HashSet;
import java.util.Set;
import eu.revamp.hcf.utils.Handler;

public class ScheduleHandler extends Handler
{
    @Getter @Setter private Set<ScheduleData> schedules;
    @Getter @Setter private ScheduleTask scheduleTask;
    
    public ScheduleHandler(RevampHCF plugin) {
        super(plugin);
        setSchedules(new HashSet<>());
        this.loadSchedules();
        setScheduleTask(new ScheduleTask());
    }
    
    @Override
    public void disable() {
        this.saveSchedules();
        getScheduleTask().cancel();
        setScheduleTask(null);
        getSchedules().clear();
    }
    
    public void loadSchedules() {
        ConfigurationSection configurationSection = this.getInstance().getSchedules().getConfigurationSection("koth");
        for (String s : configurationSection.getKeys(false)) {
            ScheduleData scheduleData = new ScheduleData();
            scheduleData.setId(Integer.parseInt(s));
            scheduleData.setName(configurationSection.getString(s + ".name"));
            scheduleData.setDay(configurationSection.getString(s + ".day"));
            scheduleData.setTime(configurationSection.getString(s + ".time"));
            scheduleData.setCapTime(configurationSection.getInt(s + ".capTime"));
            scheduleData.setType(ScheduleType.KOTH);
            getSchedules().add(scheduleData);
        }
    }
    
    public void saveSchedules() {
        ConfigurationSection configurationSection = this.getInstance().getSchedules().getConfigurationSection("koth");
        for (ScheduleData scheduleData : getSchedules()) {
            if (scheduleData.getType() != ScheduleType.KOTH) {
                continue;
            }
            ConfigurationSection section = configurationSection.createSection(String.valueOf(scheduleData.getId()));
            section.set("name", scheduleData.getName());
            section.set("day", scheduleData.getDay());
            section.set("time", scheduleData.getTime());
            section.set("capTime", scheduleData.getCapTime());
        }
        RevampHCF.getInstance().getSchedules().save();
    }

    public void addScheduleData(int id, String name, ScheduleType type, String day, String time, int capTime) {
        ScheduleData scheduleData = new ScheduleData();
        scheduleData.setId(id);
        scheduleData.setName(name);
        scheduleData.setDay(day);
        scheduleData.setTime(time);
        scheduleData.setCapTime(capTime);
        scheduleData.setType(type);
        getSchedules().add(scheduleData);
    }
    
    public boolean containsScheduleType(ScheduleType scheduleType) {
        for (ScheduleData schedule : getSchedules()) {
            if (schedule.getType() == scheduleType) {
                return true;
            }
        }
        return false;
    }
    
    public boolean containsScheduleTypeAndDay(ScheduleType scheduleType, String s) {
        for (ScheduleData scheduleData : getSchedules()) {
            if (scheduleData.getType() == scheduleType && scheduleData.getDay().equals(s)) {
                return true;
            }
        }
        return false;
    }
    
    public void clearSchedule(CommandSender sender) {
        if (getSchedules().isEmpty()) {
            sender.sendMessage("Event schedule is empty at the moment!");
            return;
        }
        for (ScheduleData scheduleData : getSchedules()) {
            ScheduleType[] values;
            for (int length = (values = ScheduleType.values()).length, i = 0; i < length; ++i) {
                this.getInstance().getSchedules().set(values[i].name().toLowerCase() + "." + scheduleData.getId(), null);
            }
        }
        RevampHCF.getInstance().getSchedules().save();
        MessageUtils.sendMessage("&bEvent &6schedule &bhas been cleared by &c" + sender.getName() + "&b!", "hcf.staff");
        getSchedules().clear();
    }
    
    public void removeScheduleData(ScheduleType scheduleType, int n) {
        getSchedules().remove(this.getScheduleData(scheduleType, n));
        this.getInstance().getSchedules().set(scheduleType.name().toLowerCase() + "." + n, null);
        RevampHCF.getInstance().getSchedules().save();
        for (ScheduleData scheduleData : this.getSchedules()) {
            if (scheduleData.getType() != ScheduleType.KOTH) {
                continue;
            }
            int id = scheduleData.getId();
            if (id <= n) {
                continue;
            }
            scheduleData.setId(id - 1);
        }
    }
    
    public void removeScheduleData(ScheduleType scheduleType, String s) {
        ScheduleData scheduleData = this.getScheduleData(scheduleType, s);
        this.getInstance().getSchedules().set(scheduleType.name().toLowerCase() + "." + scheduleData.getId(), null);
        RevampHCF.getInstance().getSchedules().save();
        for (ScheduleData scheduleData2 : this.getSchedules()) {
            if (scheduleData2.getType() != ScheduleType.KOTH) {
                continue;
            }
            int id = scheduleData2.getId();
            if (id <= scheduleData.getId()) {
                continue;
            }
            scheduleData2.setId(id - 1);
        }
    }
    
    public ScheduleData getScheduleData(ScheduleType scheduleType, int n) {
        for (ScheduleData scheduleData : getSchedules()) {
            if (scheduleData.getId() == n && scheduleData.getType() == scheduleType) {
                return scheduleData;
            }
        }
        return null;
    }
    
    public ScheduleData getScheduleData(ScheduleType scheduleType, String s) {
        for (ScheduleData scheduleData : getSchedules()) {
            if (scheduleData.getName().equalsIgnoreCase(s) && scheduleData.getType() == scheduleType) {
                return scheduleData;
            }
        }
        return null;
    }
    
    public String getDayName(Date date) {
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
    }
    
    public void sendAdminKothScheduleList(CommandSender sender) {
        FastDateFormat instance = FastDateFormat.getInstance(RevampHCF.getInstance().getConfig().getString("DATE_FORMAT"), TimeZone.getTimeZone(RevampHCF.getInstance().getConfig().getString("TIMEZONE")), Locale.ENGLISH);
        Date date = new Date();
        String format = instance.format(date);
        sender.sendMessage("Koth Schedule List");
        sender.sendMessage("Current Time is: " + this.getDayName(date) + " " + format);
        sender.sendMessage("");
        if (getSchedules().isEmpty()) {
            sender.sendMessage("There are no schedules yet!");
        }
        else {
            getSchedules().stream().sorted(Comparator.comparing(ScheduleData::getId)).forEach(scheduleData -> sender.sendMessage(CC.translate(" &7- &3(#" + scheduleData.getId() + ") &3(&c" + scheduleData.getDay() + "&3) &3&l" + scheduleData.getName() + " &b" + scheduleData.getTime() + " &7CapTime: " + scheduleData.getCapTime())));
        }
    }
    
    public void sendPlayerScheduleList(ScheduleType scheduleType, CommandSender sender) {
        FastDateFormat instance = FastDateFormat.getInstance(RevampHCF.getInstance().getConfig().getString("DATE_FORMAT"), TimeZone.getTimeZone(RevampHCF.getInstance().getConfig().getString("TIMEZONE")), Locale.ENGLISH);
        Date date = new Date();
        String format = instance.format(date);
        sender.sendMessage(CC.translate("&7&m-------------------------------"));
        sender.sendMessage(CC.translate("&6&lKoth Schedule List&7:"));
        sender.sendMessage(CC.translate("&eCurrent Time is: &f") + this.getDayName(date) + " " + format);
        sender.sendMessage("");
        if (getSchedules().isEmpty() || !this.containsScheduleType(scheduleType)) {
            sender.sendMessage(CC.translate("&cThere are no schedules yet!"));
        }
        else {
            EnumSet<DayOfWeek> enumSet = EnumSet.allOf(DayOfWeek.class);
            for (DayOfWeek dayOfWeek : enumSet) {
                String string2 = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                sender.sendMessage(CC.translate("&6&lDay&7: &f" + string2));
                if (!this.containsScheduleTypeAndDay(scheduleType, string2)) {
                    sender.sendMessage(CC.translate(" &c&l* &cNone"));
                    sender.sendMessage("");
                }
                else {
                    getSchedules().stream().filter(scheduleData -> scheduleData.getType() == scheduleType).filter(scheduleData -> scheduleData.getDay().equals(string2)).sorted(Comparator.comparing(ScheduleData::getHours).thenComparing(ScheduleData::getMinutes)).forEach(scheduleData -> sender.sendMessage(CC.translate(" &a&l* &9&l" + scheduleData.getName() + " &eTime&7: &f" + scheduleData.getTime() + " &eCaptime&7: &f" + scheduleData.getCapTime() + " min")));
                    sender.sendMessage(CC.translate("&7&m-------------------------------"));
                }
            }
        }
    }
}

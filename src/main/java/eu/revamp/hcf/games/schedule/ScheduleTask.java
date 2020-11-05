package eu.revamp.hcf.games.schedule;

import eu.revamp.hcf.RevampHCF;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ScheduleTask extends BukkitRunnable
{
    public ScheduleTask() {
        this.runTaskTimerAsynchronously(RevampHCF.getInstance(), 20L, 20L);
    }
    
    public String getDayName(Date date) {
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
    }
    @SuppressWarnings("deprecation")
    public void run() {
        TimeZone.setDefault(TimeZone.getTimeZone(RevampHCF.getInstance().getConfig().getString("TIMEZONE")));
        final Date date = new Date();
        for (ScheduleData scheduleData : RevampHCF.getInstance().getHandlerManager().getScheduleHandler().getSchedules()) {
            if (!scheduleData.getDay().equalsIgnoreCase(this.getDayName(date))) {
                continue;
            }
            if (scheduleData.getHours() != date.getHours()) {
                continue;
            }
            if (scheduleData.getMinutes() != date.getMinutes()) {
                continue;
            }
            if (date.getSeconds() != 0) {
                continue;
            }
            switch (scheduleData.getType()) {
                case KOTH: {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "koth start " + scheduleData.getName());
                }
                default: {
                    continue;
                }
            }
        }
    }
}

package eu.revamp.hcf.games.schedule;

import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;

public class ScheduleData
{
    @Getter @Setter private int id;
    @Getter @Setter private String name;
    @Getter @Setter private ScheduleType type;
    @Getter @Setter private String day;
    @Getter @Setter private int dayID;
    @Getter @Setter private String time;
    @Getter @Setter private int capTime;
    
    public ScheduleData() {
    }
    
    public ScheduleData(int id, ScheduleType type, String name, String day, String time, int capTime) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.day = day;
        this.dayID = DayOfWeek.valueOf(day).getValue();
        this.time = time;
        this.capTime = capTime;
    }

    public int getHours() {
        return Integer.parseInt(this.time.split(":")[0]);
    }
    
    public int getMinutes() {
        return Integer.parseInt(this.time.split(":")[1]);
    }
}

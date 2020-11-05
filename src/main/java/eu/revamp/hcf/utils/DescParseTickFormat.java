package eu.revamp.hcf.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Locale;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.Map;

public class DescParseTickFormat
{
    //TODO ELIMINARE E RIMPIAZZARE
    public static Map<String, Integer> nameToTicks = new LinkedHashMap<>();
    public static Map<Integer, String> ticksToName = new LinkedHashMap<>();
    public static Set<String> resetAliases = new HashSet<>();
    public static int ticksAtMidnight = 18000;
    public static int ticksPerDay = 24000;
    public static int ticksPerHour = 1000;
    public static double ticksPerMinute = 16.666666666666668;
    public static double ticksPerSecond = 0.2777777777777778;
    private static final SimpleDateFormat SDFTwentyFour = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    private static final SimpleDateFormat SDFTwelve = new SimpleDateFormat("h:mm aa", Locale.ENGLISH);
    
    static {
        DescParseTickFormat.SDFTwentyFour.setTimeZone(TimeZone.getTimeZone("GMT"));
        DescParseTickFormat.SDFTwelve.setTimeZone(TimeZone.getTimeZone("GMT"));
        DescParseTickFormat.nameToTicks.put("sunrise", 23000);
        DescParseTickFormat.nameToTicks.put("dawn", 23000);
        DescParseTickFormat.nameToTicks.put("daystart", 0);
        DescParseTickFormat.nameToTicks.put("day", 0);
        DescParseTickFormat.nameToTicks.put("morning", 1000);
        DescParseTickFormat.nameToTicks.put("midday", 6000);
        DescParseTickFormat.nameToTicks.put("noon", 6000);
        DescParseTickFormat.nameToTicks.put("afternoon", 9000);
        DescParseTickFormat.nameToTicks.put("sunset", 12000);
        DescParseTickFormat.nameToTicks.put("dusk", 12000);
        DescParseTickFormat.nameToTicks.put("sundown", 12000);
        DescParseTickFormat.nameToTicks.put("nightfall", 12000);
        DescParseTickFormat.nameToTicks.put("nightstart", 14000);
        DescParseTickFormat.nameToTicks.put("night", 14000);
        DescParseTickFormat.nameToTicks.put("midnight", 18000);
        DescParseTickFormat.ticksToName.put(23000, "dawn");
        DescParseTickFormat.ticksToName.put(0, "day");
        DescParseTickFormat.ticksToName.put(14000, "night");
        DescParseTickFormat.ticksToName.put(18000, "midnight");
        DescParseTickFormat.ticksToName.put(6000, "noon");
        DescParseTickFormat.ticksToName.put(9000, "sunset");
        DescParseTickFormat.resetAliases.add("reset");
        DescParseTickFormat.resetAliases.add("normal");
        DescParseTickFormat.resetAliases.add("default");
    }
    
    public static long parse(String desc) throws NumberFormatException {
        desc = desc.toLowerCase(Locale.ENGLISH).replaceAll("[^A-Za-z0-9:]", "");
        try {
            return parseTicks(desc);
        }
        catch (NumberFormatException e1) {
            try {
                return parse24(desc);
            }
            catch (NumberFormatException e2) {
                try {
                    return parse12(desc);
                }
                catch (NumberFormatException e3) {
                    try {
                        return parseAlias(desc);
                    }
                    catch (NumberFormatException e4) {
                        throw new NumberFormatException();
                    }
                }
            }
        }
    }
    
    public static long parseTicks(String desc) throws NumberFormatException {
        if (!desc.matches("^[0-9]+ti?c?k?s?$")) {
            throw new NumberFormatException();
        }
        desc = desc.replaceAll("[^0-9]", "");
        return Long.parseLong(desc) % 24000L;
    }
    
    public static long parse24(String desc) throws NumberFormatException {
        if (!desc.matches("^[0-9]{2}[^0-9]?[0-9]{2}$")) {
            throw new NumberFormatException();
        }
        desc = desc.toLowerCase(Locale.ENGLISH).replaceAll("[^0-9]", "");
        if (desc.length() != 4) {
            throw new NumberFormatException();
        }
        int hours = Integer.parseInt(desc.substring(0, 2));
        int minutes = Integer.parseInt(desc.substring(2, 4));
        return hoursMinutesToTicks(hours, minutes);
    }
    
    public static long parse12(String desc) throws NumberFormatException {
        if (!desc.matches("^[0-9]{1,2}([^0-9]?[0-9]{2})?(pm|am)$")) {
            throw new NumberFormatException();
        }
        int hours = 0;
        int minutes = 0;
        desc = desc.toLowerCase(Locale.ENGLISH);
        String parsetime = desc.replaceAll("[^0-9]", "");
        if (parsetime.length() > 4) {
            throw new NumberFormatException();
        }
        if (parsetime.length() == 4) {
            hours += Integer.parseInt(parsetime.substring(0, 2));
            minutes += Integer.parseInt(parsetime.substring(2, 4));
        }
        else if (parsetime.length() == 3) {
            hours += Integer.parseInt(parsetime.substring(0, 1));
            minutes += Integer.parseInt(parsetime.substring(1, 3));
        }
        else if (parsetime.length() == 2) {
            hours += Integer.parseInt(parsetime.substring(0, 2));
        }
        else {
            if (parsetime.length() != 1) {
                throw new NumberFormatException();
            }
            hours += Integer.parseInt(parsetime.substring(0, 1));
        }
        if (desc.endsWith("pm") && hours != 12) {
            hours += 12;
        }
        if (desc.endsWith("am") && hours == 12) {
            hours -= 12;
        }
        return hoursMinutesToTicks(hours, minutes);
    }
    
    public static long hoursMinutesToTicks(int hours, int minutes) {
        long ret = 18000L;
        ret += hours * 1000;
        ret += (long)(minutes / 60.0 * 1000.0);
        ret %= 24000L;
        return ret;
    }
    
    public static long parseAlias(String desc) throws NumberFormatException {
        Integer ret = DescParseTickFormat.nameToTicks.get(desc);
        if (ret == null) {
            throw new NumberFormatException();
        }
        return ret;
    }
    
    public static boolean meansReset(String desc) {
        return DescParseTickFormat.resetAliases.contains(desc);
    }
    
    public static String formatTicks(long ticks) {
        return ticks % 24000L + "ticks";
    }
    
    public static String format24(long ticks) {
        synchronized (DescParseTickFormat.SDFTwentyFour) {
            // monitorexit(DescParseTickFormat.SDFTwentyFour)
            return formatDateFormat(ticks, DescParseTickFormat.SDFTwentyFour);
        }
    }
    
    public static String format12(long ticks) {
        synchronized (DescParseTickFormat.SDFTwelve) {
            // monitorexit(DescParseTickFormat.SDFTwelve)
            return formatDateFormat(ticks, DescParseTickFormat.SDFTwelve);
        }
    }
    
    public static String formatDateFormat(long ticks, SimpleDateFormat format) {
        String name = DescParseTickFormat.ticksToName.get((int)ticks);
        if (name != null) {
            return name;
        }
        Date date = ticksToDate(ticks);
        return format.format(date);
    }
    
    public static Date ticksToDate(long ticks) {
        ticks = ticks - 18000L + 24000L;
        long days = ticks / 24000L;
        ticks -= days * 24000L;
        long hours = ticks / 1000L;
        ticks -= hours * 1000L;
        long minutes = (long)Math.floor(ticks / 16.666666666666668);
        double dticks = ticks - minutes * 16.666666666666668;
        long seconds = (long)Math.floor(dticks / 0.2777777777777778);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.ENGLISH);
        cal.setLenient(true);
        cal.set(0, Calendar.JANUARY, 1, 0, 0, 0);
        cal.add(Calendar.DAY_OF_YEAR, (int)days);
        cal.add(Calendar.HOUR_OF_DAY, (int)hours);
        cal.add(Calendar.MINUTE, (int)minutes);
        cal.add(Calendar.SECOND, (int)seconds + 1);
        return cal.getTime();
    }
}

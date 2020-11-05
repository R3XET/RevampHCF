package eu.revamp.hcf.managers;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {

    @Getter public static HashMap<String, HashMap<UUID, Long>> cooldowns = new HashMap<>();


/*
if (!CooldownManager.getCooldowns().containsKey("blockeggdefender_delay")) {
    CooldownManager.createCooldown("blockeggdefender_delay");
    }
    CooldownManager.addCooldown("blockeggdefender_delay", eggdamagedplayer, RevampPackages.getInstance().getConfig().getDouble("blockegg.Cooldown-Break-Place"));
 */
/*
TimeUtils.getRemaining(CooldownManager.getCooldownMillis("PYROBALL_DELAY", player), true)
 */


    public static void deleteCooldowns() {
        CooldownManager.getCooldowns().clear();
    }

    public static void createCooldown(String cooldown) {
        if (CooldownManager.getCooldowns().containsKey(cooldown)) {
            throw new IllegalArgumentException("Sorry, but cooldown doesn't exists.");
        }
        CooldownManager.getCooldowns().put(cooldown, new HashMap<>());
    }
    public static HashMap<UUID, Long> getCooldownMap(String cooldown) {
        if (CooldownManager.getCooldowns().containsKey(cooldown)) {
            return CooldownManager.getCooldowns().get(cooldown);
        }
        return null;
    }
    public static void addCooldown(String cooldown, Player p, double seconds) {
        if (!CooldownManager.getCooldowns().containsKey(cooldown)) {
            throw new IllegalArgumentException(cooldown + " doesn't exists.");
        }
        long next = (long) (System.currentTimeMillis() + seconds * 1000L);
        CooldownManager.getCooldowns().get(cooldown).put(p.getUniqueId(), next);
    }
    public static boolean isOnCooldown(String cooldown, Player p) {
        return CooldownManager.getCooldowns().containsKey(cooldown) && CooldownManager.getCooldowns().get(cooldown).containsKey(p.getUniqueId()) && System.currentTimeMillis() <= CooldownManager.getCooldowns().get(cooldown).get(p.getUniqueId());
    }
    public static int getCooldownMillis(String cooldown, Player p) {
        return (int)(CooldownManager.getCooldowns().get(cooldown).get(p.getUniqueId()) - System.currentTimeMillis());
    }
    public static int getCooldownInt(String cooldown, Player p) {
        return (int)((CooldownManager.getCooldowns().get(cooldown).get(p.getUniqueId()) - System.currentTimeMillis()) / 1000L);
    }
    public static double getCooldownDouble(String cooldown, Player p) {
        return (double) (CooldownManager.getCooldowns().get(cooldown).get(p.getUniqueId()) - System.currentTimeMillis()) / 1000L;
    }
    public static long getCooldownLong(String cooldown, Player p) {
        return CooldownManager.getCooldowns().get(cooldown).get(p.getUniqueId()) - System.currentTimeMillis();
    }

    public static void removeCooldown(String k, Player p) {
        if (!CooldownManager.getCooldowns().containsKey(k)) {
            throw new IllegalArgumentException(k + " doesn't exists.");
        }
        CooldownManager.getCooldowns().get(k).remove(p.getUniqueId());
    }
}

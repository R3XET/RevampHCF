package eu.revamp.hcf.handlers.games;

import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.time.TimeUtils;
import lombok.Getter;
import org.bukkit.Sound;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.entity.Player;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.Faction;
import org.bukkit.command.Command;
import org.bukkit.Bukkit;
import eu.revamp.hcf.factions.utils.games.KothFaction;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;
import eu.revamp.hcf.utils.Handler;

public class EOTWUtilsHandler extends Handler
{
    public static long EOTW_WARMUP_WAIT_MILLIS;
    public static int EOTW_WARMUP_WAIT_SECONDS;
    private static long EOTW_CAPPABLE_WAIT;
    @Getter private EotwRunnable runnable;
    
    static {
        EOTWUtilsHandler.EOTW_WARMUP_WAIT_MILLIS = TimeUnit.SECONDS.toMillis(15L);
        EOTWUtilsHandler.EOTW_WARMUP_WAIT_SECONDS = (int) TimeUnit.MILLISECONDS.toSeconds(EOTWUtilsHandler.EOTW_WARMUP_WAIT_MILLIS);
        EOTWUtilsHandler.EOTW_CAPPABLE_WAIT = TimeUnit.SECONDS.toMillis(30L);
    }
    
    public EOTWUtilsHandler(RevampHCF plugin) {
        super(plugin);
    }

    public boolean isEndOfTheWorld() {
        return this.isEndOfTheWorld(true);
    }
    
    public boolean isEndOfTheWorld(boolean ignoreWarmup) {
        return this.runnable != null && (!ignoreWarmup || this.runnable.getElapsedMilliseconds() > 0L);
    }
    
    public void setEndOfTheWorld(boolean yes) {
        if (yes == this.isEndOfTheWorld(false)) return;
        if (yes) {
            (this.runnable = new EotwRunnable()).runTaskTimer(this.getInstance(), 20L, 20L);
        }
        else if (this.runnable != null) {
            this.runnable.cancel();
            this.runnable = null;
        }
    }
    
    public static class EotwRunnable extends BukkitRunnable
    {
        private boolean hasInformedStarted;
        private boolean hasInformedCapable;
        private long startStamp;
        private KothFaction kothFaction;
        
        public EotwRunnable() {
            this.startStamp = System.currentTimeMillis() + EOTWUtilsHandler.EOTW_WARMUP_WAIT_MILLIS;
        }
        
        public long getTimeUntilStarting() {
            long difference = System.currentTimeMillis() - this.startStamp;
            return (difference > 0L) ? 0L : Math.abs(difference);
        }
        
        public long getTimeUntilStarting(long now) {
            long difference = now - this.startStamp;
            return (difference > 0L) ? 0L : Math.abs(difference);
        }
        
        public long getTimeUntilCappable() {
            return EOTWUtilsHandler.EOTW_CAPPABLE_WAIT - this.getElapsedMilliseconds();
        }
        
        public long getElapsedMilliseconds() {
            return System.currentTimeMillis() - this.startStamp;
        }
        
        public void run() {
            long elapsedMillis = this.getElapsedMilliseconds();
            int elapsedSeconds = (int)Math.round(elapsedMillis / 1000.0);
            if (!this.hasInformedStarted && elapsedSeconds >= 0) {
                Faction eotwFaction = RevampHCF.getInstance().getFactionManager().getFaction("EOTW");
                if (eotwFaction == null) {
                    eotwFaction = new KothFaction("EOTW");
                }
                else if (!(eotwFaction instanceof KothFaction)) {
                    RevampHCF.getInstance().getFactionManager().removeFaction(eotwFaction, Bukkit.getConsoleSender());
                    eotwFaction = new KothFaction("EOTW");
                }
                Command.broadcastCommandMessage(Bukkit.getConsoleSender(), "Created EOTW faction");
                this.kothFaction = (KothFaction)eotwFaction;
                for (Faction faction : RevampHCF.getInstance().getFactionManager().getFactions()) {
                    if (faction instanceof PlayerFaction) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "f setdtr all -999");
                    }
                }
                Command.broadcastCommandMessage(Bukkit.getConsoleSender(), "All factions have been set raidable");
                for (Player player : Bukkit.getOnlinePlayers()) {
                    HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
                    data.setPvpTimerCooldown(0);
                    RevampHCF.getInstance().getHandlerManager().getTimerManager().getPvpTimerHandler().clearCooldown(player);
                }
                Command.broadcastCommandMessage(Bukkit.getConsoleSender(), "All pvp protected users have been removed.");
                this.hasInformedStarted = true;
                Bukkit.broadcastMessage(CC.translate("&8[&4&lEOTW&8] &4&lEOTW &chas begun."));
            }
            else if (!this.hasInformedCapable && elapsedMillis >= EOTWUtilsHandler.EOTW_CAPPABLE_WAIT) {
                if (this.kothFaction != null) {
                    RevampHCF.getInstance().getHandlerManager().getTimerManager().getGameHandler().tryContesting(this.kothFaction, Bukkit.getConsoleSender());
                }
                this.hasInformedCapable = true;
            }
            if (elapsedMillis < 0L && elapsedMillis >= -EOTWUtilsHandler.EOTW_WARMUP_WAIT_MILLIS) {
                if (elapsedSeconds < 0) return;
                Bukkit.broadcastMessage(CC.translate("&8[&4&lEOTW&8] &4&lEOTW &cis starting in &4&l" + TimeUtils.getRemaining(Math.abs(elapsedSeconds) * 1000L, true, false) + '.'));
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.playSound(players.getLocation(), Sound.NOTE_BASS_DRUM, 1.0f, 1.0f);
                }
            }
        }
    }
}

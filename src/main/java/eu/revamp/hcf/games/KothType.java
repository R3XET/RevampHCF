package eu.revamp.hcf.games;

import eu.revamp.hcf.factions.utils.games.EventFaction;
import eu.revamp.hcf.factions.utils.games.KothFaction;
import eu.revamp.hcf.factions.utils.zone.CaptureZone;
import eu.revamp.hcf.timers.GameHandler;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.date.DateTimeFormats;
import eu.revamp.system.api.player.PlayerData;
import eu.revamp.system.plugin.RevampSystem;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;
import eu.revamp.hcf.RevampHCF;

public class KothType implements EventGameType {
    private static final long MINIMUM_CONTROL_TIME_ANNOUNCE = TimeUnit.SECONDS.toMillis(25L);
    public static long DEFAULT_CAP_MILLIS = TimeUnit.MINUTES.toMillis(15L);
    private final RevampHCF plugin;

    public KothType(RevampHCF plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public GameType getEventType() {
        return GameType.KOTH;
    }
    
    @Override
    public void tick(GameHandler eventTimer, EventFaction eventFaction) {
        CaptureZone captureZone = ((KothFaction)eventFaction).getCaptureZone();
        captureZone.updateScoreboardRemaining();
        long remainingMillis = captureZone.getRemainingCaptureMillis();
        if (remainingMillis <= 0L) {
            this.plugin.getHandlerManager().getTimerManager().getGameHandler().handleWinner(captureZone.getCappingPlayer());
            eventTimer.clearCooldown();
            return;
        }
        if (remainingMillis == captureZone.getDefaultCaptureMillis()) return;
        int remainingSeconds = (int)(remainingMillis / 1000L);
        if (remainingSeconds > 0 && remainingSeconds % 30 == 0) {
            Bukkit.broadcastMessage(CC.translate("&6[KingOfTheHill] Someone is controlling &e" + captureZone.getDisplayName() + ". &c(" + DateTimeFormats.KOTH_FORMAT.format(remainingMillis) + "&c)"));
        }
    }
    
    @Override
    public void onContest(EventFaction eventFaction, GameHandler eventTimer) {
        Bukkit.broadcastMessage(CC.translate("&6[KingOfTheHill] &e" + eventFaction.getName() + " &6can now be contested. &c(" + DateTimeFormats.KOTH_FORMAT.format(eventTimer.getRemaining()) + "&c)"));
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            online.playSound(online.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0f, 1.0f);
        }
    }
    
    @Override
    public boolean onControlTake(Player player, CaptureZone captureZone) {
        PlayerData targetProfile = RevampSystem.getINSTANCE().getPlayerManagement().getPlayerData(player.getUniqueId());
        if (!player.getGameMode().equals(GameMode.CREATIVE) && !targetProfile.isInStaffMode() && !targetProfile.isVanished()) {
            player.sendMessage(CC.translate("&6You are now in control of &e" + captureZone.getDisplayName() + '.'));
            player.playSound(player.getLocation(), Sound.CLICK, 1.0f, 1.0f);
        }
        return true;
    }
    
    @Override
    public void onControlLoss(Player player, CaptureZone captureZone, EventFaction eventFaction) {
        player.sendMessage(CC.translate("&cYou are no longer in control of &e" + captureZone.getDisplayName() + '.'));
        player.playSound(player.getLocation(), Sound.ARROW_HIT, 1.0f, 1.0f);
        long remainingMillis = captureZone.getRemainingCaptureMillis();
        if (remainingMillis > 0L && captureZone.getDefaultCaptureMillis() - remainingMillis > KothType.MINIMUM_CONTROL_TIME_ANNOUNCE) {
            Bukkit.broadcastMessage(CC.translate("&6[KingOfTheHill] &e" + player.getName() + " &6has lost control of &e" + captureZone.getDisplayName() + ". &c(" + captureZone.getScoreboardRemaining() + "&c)"));
            player.playSound(player.getLocation(), Sound.ARROW_HIT, 1.0f, 1.0f);
        }
    }
    
    @Override
    public void stopTiming() {
    }
}

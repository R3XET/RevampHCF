package eu.revamp.hcf.utils.timer;

import java.util.LinkedHashSet;

import eu.revamp.hcf.RevampHCF;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import eu.revamp.hcf.timers.StuckHandler;
import eu.revamp.hcf.timers.ClassWarmupHandler;
import eu.revamp.hcf.timers.SOTWHandler;
import eu.revamp.hcf.timers.PvPTimerHandler;
import eu.revamp.hcf.timers.GameHandler;
import eu.revamp.hcf.timers.HomeHandler;
import eu.revamp.hcf.timers.ArcherHandler;
import eu.revamp.hcf.timers.SpawnTagHandler;
import org.bukkit.event.Listener;

@Getter
public class TimerManager implements Listener
{
    private SpawnTagHandler spawnTagHandler;
    private ArcherHandler archerHandler;
    private HomeHandler homeHandler;
    private GameHandler gameHandler;
    private PvPTimerHandler pvpTimerHandler;
    private SOTWHandler sotwHandler;
    private ClassWarmupHandler classWarmupHandler;
    private StuckHandler stuckHandler;
    @Setter private Set<Timer> timers;
    
    public TimerManager(RevampHCF plugin) {
        RevampHCF.getInstance().getServer().getPluginManager().registerEvents(this, plugin);
        this.timers = new LinkedHashSet<>();
        this.registerTimer(this.stuckHandler = new StuckHandler());
        if (!RevampHCF.getInstance().getConfiguration().isKitMap()) {
            this.registerTimer(this.pvpTimerHandler = new PvPTimerHandler());
        }
        this.registerTimer(this.spawnTagHandler = new SpawnTagHandler(plugin));
        this.registerTimer(this.homeHandler = new HomeHandler(plugin));
        this.registerTimer(this.gameHandler = new GameHandler(plugin));
        this.registerTimer(this.classWarmupHandler = new ClassWarmupHandler(plugin));
        this.registerTimer(this.archerHandler = new ArcherHandler());
    }
    
    public void registerTimer(Timer timer) {
        this.timers.add(timer);
        if (timer instanceof Listener) {
            RevampHCF.getInstance().getServer().getPluginManager().registerEvents((Listener)timer, RevampHCF.getInstance());
        }
    }
    
    public void unregisterTimer(Timer timer) {
        this.timers.remove(timer);
    }
}

package eu.revamp.tablist;

import eu.revamp.hcf.integration.implement.tinyprotocol.Reflection;
import eu.revamp.hcf.integration.implement.tinyprotocol.TinyProtocol;
import eu.revamp.tablist.interfaces.PlayerVersionInterface;
import eu.revamp.tablist.interfaces.TabVersionInterface;
import eu.revamp.tablist.interfaces.TablistInterface;
import eu.revamp.tablist.listener.TabListener;
import eu.revamp.tablist.manager.TablistManager;
import eu.revamp.tablist.playerversion.*;
import eu.revamp.tablist.tabversion.TabVersion1_7;
import eu.revamp.tablist.tabversion.TabVersionTinyProtocol;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter @Setter
public class RevampTab {
    @Getter @Setter  private static RevampTab instance;
    private TabVersionInterface tablistVersion;
    private PlayerVersionInterface playerVersion;
    private TablistInterface tablist;
    private List<TablistManager> tablists;
    private TinyProtocol tinyProtocol;
    private JavaPlugin javaPlugin;
    private long startTime;
    private long ticks;
    @Getter private ScheduledExecutorService executorService;


    public RevampTab(JavaPlugin javaPlugin, TablistInterface tablist) {
        this(javaPlugin, tablist, 10L);
    }

    public RevampTab(JavaPlugin javaPlugin, TablistInterface tablist, long ticks) {
        setTablists(new ArrayList<>());
        setJavaPlugin(javaPlugin);
        setTablist(tablist);
        setStartTime(System.currentTimeMillis());
        setTicks(ticks);
        this.onEnable();
    }

    private void onEnable() {
        setInstance(this);
        if (Reflection.VERSION.contains("v1_7")) {
            setTablistVersion(new TabVersion1_7(this));
        }
        else {
            setTinyProtocol(new TinyProtocol(getJavaPlugin()) {});
            setTablistVersion(new TabVersionTinyProtocol(this));
        }
        PluginManager pm = Bukkit.getServer().getPluginManager();
        if (getTablistVersion().getClass() == TabVersion1_7.class) {
            setPlayerVersion(new PlayerVersion1_7());
        }
        else if (pm.getPlugin("ProtocolSupport") != null) {
            setPlayerVersion(new PlayerVersionProtocolSupport());
        }
        else if (pm.getPlugin("ViaVersion") != null) {
            setPlayerVersion(new PlayerVersionViaVersion());
        }
        else if (pm.getPlugin("ProtocolLib") != null) {
            setPlayerVersion(new PlayerVersionProtocolLib());
        }
        else {
            setPlayerVersion(new PlayerVersionTinyProtocol());
        }
        pm.registerEvents(new TabListener(this), getJavaPlugin());


        (this.executorService = Executors.newSingleThreadScheduledExecutor()).scheduleAtFixedRate(() -> {
            try {
                this.getTablists().forEach(TablistManager::update);
            }
            catch (Exception ignored) {
            }
        }, 0L, this.getTicks() * 50L, TimeUnit.MILLISECONDS);


        /*
        new BukkitRunnable() {
            public void run() {
                for (int size = RevampTab.this.tablists.size(), i = 0; i < size; ++i) {
                    try {
                        RevampTab.this.tablists.get(i).update();
                    } catch (Exception ignored) {
                    }
                }
            }
        }.runTaskTimer(this.javaPlugin, 60L, this.ticks);
        */
    }

    public void onDisable(){
        getExecutorService().shutdownNow();
        setInstance(null);
    }
}

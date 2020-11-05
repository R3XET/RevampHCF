package eu.revamp.hcf.playerdata;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.spigot.utils.generic.Tasks;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class HCFPlayerDataHandler extends Handler implements Listener {
    private final Map<UUID, HCFPlayerData> playerData;
    private final File playerDataDirectory;

    public HCFPlayerDataHandler(RevampHCF plugin) {
        super(plugin);
        this.playerData = new HashMap<>();
        this.playerDataDirectory = new File(this.getInstance().getDataFolder(), "playerdata");
    }
    
    @Override
    public void enable() {
        Bukkit.getOnlinePlayers().forEach(Player::loadData);
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @Override
    public void disable() {
        Bukkit.getOnlinePlayers().forEach(Player::saveData);
        Bukkit.getServer().savePlayers();
    }

    @Deprecated
    public HCFPlayerData getPlayer(Player player) {
        return this.playerData.get(player.getUniqueId());
    }
    
    public HCFPlayerData getPlayer(UUID uuid) {
        return this.playerData.get(uuid);
    }

    public HCFPlayerData getPlayerData(UUID uuid) {
        return this.playerData.get(uuid);
    }
    
    public void addPlayerData(Player player, HCFPlayerData HCFPlayerData) {
        this.playerData.put(player.getUniqueId(), HCFPlayerData);
    }

    public HCFPlayerData createData(UUID uuid, String name) {
        if (this.playerData.containsKey(uuid)) return getPlayerData(uuid);
        this.playerData.put(uuid, new HCFPlayerData(uuid, name));
        return getPlayerData(uuid);
    }

    public void deleteData(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) return;
        this.playerData.remove(uuid);
    }
    
    @EventHandler
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        String name = event.getName();
        this.createData(uuid, name);
        HCFPlayerData playerData = this.getPlayerData(uuid);
        playerData.loadData();
        this.playerData.put(uuid, playerData);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String name = player.getName();
        this.createData(uuid, name);
        HCFPlayerData playerData = this.getPlayerData(uuid);
        playerData.saveData();
    }


    // MongoDB Section

    public HCFPlayerData loadMongoData(UUID uuid) {
        Document document = this.getInstance().getMongoManagerHCF().getDocumentation().find(Filters.eq("uuid", uuid.toString())).first();

        if (document == null) {
            return null;
        }
        this.createData(uuid, document.getString("name"));
        return this.getPlayerData(uuid);
    }

    @SuppressWarnings("deprecation")
    public void saveMongoData(UUID uniqueId, String value, Object key) {
        Tasks.runAsync(this.getInstance(), () -> {
            Document document = this.getInstance().getMongoManagerHCF().getDocumentation().find(Filters.eq("uuid", uniqueId.toString())).first();

            if (document != null && document.containsKey(value)) {
                document.put(value, key);

                this.getInstance().getMongoManagerHCF().getDocumentation().replaceOne(Filters.eq("uuid", uniqueId.toString()), document, new UpdateOptions().upsert(true));
            }
        });
    }

    public boolean hasMongoData(UUID uuid) {
        Document document = this.getInstance().getMongoManagerHCF().getDocumentation().find(Filters.eq("uuid", uuid.toString())).first();
        return document != null;
    }


    // YAML Section

    public HCFPlayerData loadYamlData(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        File dataFile = new File(this.playerDataDirectory, player.getUniqueId().toString() + ".yml");
        if (!dataFile.exists()) {
            return null;
        }
        this.createData(uuid, player.getName());
        return this.getPlayerData(uuid);
    }

    public void saveYamlData(UUID uniqueId, String path, Object value) {
        Tasks.runAsync(this.getInstance(), () -> {

            File dataFile = new File(this.playerDataDirectory, uniqueId.toString() + ".yml");

            if (dataFile.exists()) {
                YamlConfiguration configuration = YamlConfiguration.loadConfiguration(dataFile);
                if (configuration.contains(path)){
                    configuration.set(path, value);
                    try {
                        configuration.save(dataFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public boolean hasYamlData(UUID uuid) {
        File dataFile = new File(this.playerDataDirectory, uuid.toString() + ".yml");
        return dataFile.exists();
    }
}



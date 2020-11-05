package eu.revamp.hcf.managers;
/*
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.spigot.utils.generic.Tasks;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class PlayerManagementHCF extends Manager {
    private Map<UUID, HCFPlayerData> playerData = new HashMap<>();

    public PlayerManagementHCF(RevampHCF plugin) {
        super(plugin);
    }

    public HCFPlayerData createPlayerData(UUID uuid, String name) {
        if (this.playerData.containsKey(uuid)) return getPlayerData(uuid);
        this.playerData.put(uuid, new HCFPlayerData(uuid, name));
        return getPlayerData(uuid);
    }

    public HCFPlayerData getPlayerData(UUID uuid) {
        return this.playerData.get(uuid);
    }

    public void deleteData(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) return;
        this.playerData.remove(uuid);
    }

    public HCFPlayerData loadData(UUID uuid) {
        Document document = plugin.getMongoManagerHCF().getDocumentation().find(Filters.eq("uuid", uuid.toString())).first();

        if (document == null) {
            return null;
        }
        this.createPlayerData(uuid, document.getString("name"));
        return this.getPlayerData(uuid);
    }

    public String getFixedName(String name) {
        Document document = plugin.getMongoManagerHCF().getDocumentation().find(Filters.eq("lowerCaseName", name.toLowerCase())).first();
        if (document == null) return name;
        return document.getString("name");
    }

    @SuppressWarnings("deprecation")
    public void saveYamlData(UUID uniqueId, String value, Object key) {
        Tasks.runAsync(plugin, () -> {
            Document document = plugin.getMongoManagerHCF().getDocumentation().find(Filters.eq("uuid", uniqueId.toString())).first();

            if (document != null && document.containsKey(value)) {
                document.put(value, key);

                plugin.getMongoManagerHCF().getDocumentation().replaceOne(Filters.eq("uuid", uniqueId.toString()), document, new UpdateOptions().upsert(true));
            }
        });
    }

    public boolean hasData(UUID uuid) {
        Document document = plugin.getMongoManagerHCF().getDocumentation().find(Filters.eq("uuid", uuid.toString())).first();

        return document != null;
    }
}
*/
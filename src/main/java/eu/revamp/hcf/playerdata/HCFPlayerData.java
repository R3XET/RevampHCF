package eu.revamp.hcf.playerdata;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.system.api.player.PlayerData;
import eu.revamp.system.plugin.RevampSystem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Getter @Setter
public class HCFPlayerData {

    private final UUID uuid;
    private final String playerName;

    private int kills;
    private int deaths;
    private int balance;
    private boolean showClaimMap = false;
    private boolean showLightning = true;
    private boolean reclaimed = false;
    private boolean combatLogger;
    private List<String> lastDeaths;
    private long lastFactionLeaveMillis;
    private long lastSeen;
    private long enderpearlCooldown;
    private long gappleCooldown;
    private long goldenHeadCooldown;
    private int pvpTimerCooldown;
    private long spawnTagCooldown;
    private String rank;
    private String name;
    boolean hasStarter;

    public void addKill() {
        ++this.kills;
    }

    public void addDeath() {
        ++this.deaths;
    }

    public void addBalance(int amount) {
        this.balance += amount;
    }


    public void saveData(){
        switch (RevampHCF.getInstance().getDatabaseType()){
            case MONGO: {
                this.saveMongoData();
            }
            case JSON: {
                //TODO ADD JSON
            }
            case YAML: {
                this.saveYamlData();
            }
        }
    }

    public void loadData(){
        switch (RevampHCF.getInstance().getDatabaseType()){
            case MONGO: {
                this.loadMongoData();
            }
            case JSON: {
                //TODO ADD JSON
            }
            case YAML: {
                this.loadYamlData();
            }
        }
    }

    private void loadYamlData() {
        File playerDataDirectory = new File(RevampHCF.getInstance().getDataFolder(), "playerdata");

        File dataFile = new File(playerDataDirectory, uuid.toString() + ".yml");

        if (!dataFile.exists()) {
            this.createYamlData();
            return;
        }

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(dataFile);
        this.kills = configuration.getInt("kills");
        this.deaths = configuration.getInt("deaths");
        this.balance = configuration.getInt("balance");
        this.enderpearlCooldown = configuration.getLong("enderpearlCooldown");
        this.gappleCooldown = configuration.getLong("gappleCooldown");
        this.goldenHeadCooldown = configuration.getLong("goldenHeadCooldown");
        this.pvpTimerCooldown = configuration.getInt("pvpTimerCooldown");
        this.spawnTagCooldown = configuration.getLong("spawnTagCooldown");
        this.showClaimMap = configuration.getBoolean("showClaimMap");
        this.showLightning = configuration.getBoolean("showLightning");
        this.reclaimed = configuration.getBoolean("reclaimed");
        this.combatLogger = configuration.getBoolean("combatLogger");
        this.lastFactionLeaveMillis = configuration.getLong("lastFactionLeaveMillis");
        this.rank = configuration.getString("rank");
        this.name = configuration.getString("name");
        this.lastSeen = configuration.getLong("lastSeen");
        this.lastDeaths = configuration.getStringList("lastDeaths");
    }

    private void loadMongoData() {
        Document document = RevampHCF.getInstance().getMongoManagerHCF().getDocumentation().find(Filters.eq("uuid", this.uuid.toString())).first();
        if (document == null) {
            this.createMongoData();
            return;
        }
        this.kills = document.getInteger("kills");
        this.deaths = document.getInteger("deaths");
        this.balance = document.getInteger("balance");
        this.enderpearlCooldown = document.getLong("enderpearlCooldown");
        this.gappleCooldown = document.getLong("gappleCooldown");
        this.goldenHeadCooldown = document.getLong("goldenHeadCooldown");
        this.pvpTimerCooldown = document.getInteger("pvpTimerCooldown");
        this.spawnTagCooldown = document.getLong("spawnTagCooldown");
        this.showClaimMap = document.getBoolean("showClaimMap");
        this.showLightning = document.getBoolean("showLightning");
        this.reclaimed = document.getBoolean("reclaimed");
        this.combatLogger = document.getBoolean("combatLogger");
        this.lastFactionLeaveMillis = document.getLong("lastFactionLeaveMillis");
        this.rank = document.getString("rank");
        this.name = document.getString("name");
        this.lastSeen = document.getLong("lastSeen");
        this.lastDeaths = (List<String>) document.get("lastDeaths");
    }

    private void createYamlData() {
        File playerDataDirectory = new File(RevampHCF.getInstance().getDataFolder(), "playerdata");

        File dataFile = new File(playerDataDirectory, this.uuid.toString() + ".yml");
        PlayerData targetProfile = RevampSystem.getINSTANCE().getPlayerManagement().getPlayerData(this.uuid);
        try {
            dataFile.createNewFile();
            this.kills = 0;
            this.deaths = 0;
            this.balance = RevampHCF.getInstance().getConfig().getInt("STARTING_BALANCE.PLAYER");
            this.enderpearlCooldown = 0L;
            this.gappleCooldown = 0L;
            this.goldenHeadCooldown = 0L;
            this.hasStarter = false;
            this.spawnTagCooldown = 0L;
            this.showClaimMap = true;
            this.showLightning = true;
            this.reclaimed = false;
            this.combatLogger = false;
            this.lastFactionLeaveMillis = 0L;
            this.rank = targetProfile.getHighestRank().getDisplayName();
            this.lastSeen = 0L;
            this.lastDeaths = null;
            if (RevampHCF.getInstance().getConfiguration().isKitMap()) {
                this.pvpTimerCooldown = 0;
            } else {
                this.pvpTimerCooldown = 3600000;
            }
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(dataFile);
            configuration.set("kills", this.kills);
            configuration.set("deaths", this.deaths);
            configuration.set("balance", this.balance);
            configuration.set("enderpearlCooldown", this.enderpearlCooldown);
            configuration.set("gappleCooldown", this.gappleCooldown);
            configuration.set("goldenHeadCooldown", this.goldenHeadCooldown);
            configuration.set("pvpTimerCooldown", this.pvpTimerCooldown);
            configuration.set("spawnTagCooldown", this.spawnTagCooldown);
            configuration.set("showClaimMap", this.showClaimMap);
            configuration.set("showLightning", this.showLightning);
            configuration.set("reclaimed", this.reclaimed);
            configuration.set("combatLogger", this.combatLogger);
            configuration.set("lastFactionLeaveMillis", this.lastFactionLeaveMillis);
            configuration.set("rank", this.rank);
            configuration.set("name", this.name);
            configuration.set("lastSeen", this.lastSeen);
            configuration.set("lastDeaths", this.lastDeaths);
            try {
                configuration.save(dataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }


    private void createMongoData() {

        PlayerData targetProfile = RevampSystem.getINSTANCE().getPlayerManagement().getPlayerData(this.uuid);
        this.kills = 0;
        this.deaths = 0;
        this.balance = RevampHCF.getInstance().getConfig().getInt("STARTING_BALANCE.PLAYER");
        this.enderpearlCooldown = 0L;
        this.gappleCooldown = 0L;
        this.goldenHeadCooldown = 0L;
        this.hasStarter = false;
        this.spawnTagCooldown = 0L;
        this.showClaimMap = true;
        this.showLightning = true;
        this.reclaimed = false;
        this.combatLogger = false;
        this.lastFactionLeaveMillis = 0L;
        this.rank = targetProfile.getHighestRank().getDisplayName();
        this.lastSeen = 0L;
        this.lastDeaths = null;
        if (RevampHCF.getInstance().getConfiguration().isKitMap()) {
            this.pvpTimerCooldown = 0;
        } else {
            this.pvpTimerCooldown = 3600000;
        }
        Document document = new Document();
        document.put("kills", this.kills);
        document.put("deaths", this.deaths);
        document.put("balance", this.balance);
        document.put("enderpearlCooldown", this.enderpearlCooldown);
        document.put("gappleCooldown", this.gappleCooldown);
        document.put("goldenHeadCooldown", this.goldenHeadCooldown);
        document.put("pvpTimerCooldown", this.pvpTimerCooldown);
        document.put("spawnTagCooldown", this.spawnTagCooldown);
        document.put("showClaimMap", this.showClaimMap);
        document.put("showLightning", this.showLightning);
        document.put("reclaimed", this.reclaimed);
        document.put("combatLogger", this.combatLogger);
        document.put("lastFactionLeaveMillis", this.lastFactionLeaveMillis);
        document.put("rank", this.rank);
        document.put("name", this.name);
        document.put("lastSeen", this.lastSeen);
        document.put("lastDeaths", this.lastDeaths);
        RevampHCF.getInstance().getMongoManagerHCF().getDocumentation().replaceOne(Filters.eq("uuid", this.uuid.toString()), document, new UpdateOptions().upsert(true));
    }

    // Saves yaml data

    private void saveYamlData() {
        File playerDataDirectory = new File(RevampHCF.getInstance().getDataFolder(), "playerdata");
        File dataFile = new File(playerDataDirectory, uuid.toString() + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(dataFile);
        configuration.set("kills", this.kills);
        configuration.set("deaths", this.deaths);
        configuration.set("balance", this.balance);
        configuration.set("enderpearlCooldown", this.enderpearlCooldown);
        configuration.set("gappleCooldown", this.gappleCooldown);
        configuration.set("goldenHeadCooldown", this.goldenHeadCooldown);
        configuration.set("pvpTimerCooldown", this.pvpTimerCooldown);
        configuration.set("spawnTagCooldown", this.spawnTagCooldown);
        configuration.set("showClaimMap", this.showClaimMap);
        configuration.set("showLightning", this.showLightning);
        configuration.set("reclaimed", this.reclaimed);
        configuration.set("combatLogger", this.combatLogger);
        configuration.set("lastFactionLeaveMillis", this.lastFactionLeaveMillis);
        configuration.set("rank", this.rank);
        configuration.set("name", this.name);
        configuration.set("lastSeen", this.lastSeen);
        configuration.set("lastDeaths", this.lastDeaths);
        try {
            configuration.save(dataFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Saves mongo data

    private void saveMongoData(){
        Document document = new Document();
        document.put("kills", this.kills);
        document.put("deaths", this.deaths);
        document.put("balance", this.balance);
        document.put("enderpearlCooldown", this.enderpearlCooldown);
        document.put("gappleCooldown", this.gappleCooldown);
        document.put("goldenHeadCooldown", this.goldenHeadCooldown);
        document.put("pvpTimerCooldown", this.pvpTimerCooldown);
        document.put("spawnTagCooldown", this.spawnTagCooldown);
        document.put("showClaimMap", this.showClaimMap);
        document.put("showLightning", this.showLightning);
        document.put("reclaimed", this.reclaimed);
        document.put("combatLogger", this.combatLogger);
        document.put("lastFactionLeaveMillis", this.lastFactionLeaveMillis);
        document.put("rank", this.rank);
        document.put("name", this.name);
        document.put("lastSeen", this.lastSeen);
        document.put("lastDeaths", this.lastDeaths);
        RevampHCF.getInstance().getMongoManagerHCF().getDocumentation().replaceOne(Filters.eq("uuid", this.uuid.toString()), document, new UpdateOptions().upsert(true));
    }
}

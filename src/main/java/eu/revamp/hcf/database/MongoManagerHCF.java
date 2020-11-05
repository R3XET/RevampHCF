package eu.revamp.hcf.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.file.ConfigFile;
import eu.revamp.hcf.managers.Manager;
import eu.revamp.spigot.utils.chat.color.CC;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class MongoManagerHCF extends Manager {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> documentation/*,
            rank,
            name,
            kills,
            deaths,
            balance,
            showClaimMap,
            showLightning,
            reclaimed,
            combatLogger,
            lastDeaths,
            lastFactionLeaveMillis,
            lastSeen,
            enderpearlCooldown,
            gappleCooldown,
            headappleCooldown,
            pvpTimerCooldown,
            spawnTagCooldown,
            hasStarter*/;

    private final ConfigFile configFile = plugin.getDataBase();

    public MongoManagerHCF(RevampHCF plugin) {
        super(plugin);
    }

    @SuppressWarnings("deprecation")
    public boolean connect() {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        try {
            if (this.configFile.getBoolean("MONGODB.AUTHENTICATION.ENABLED")) {
                MongoCredential credential = MongoCredential.createCredential(
                        this.configFile.getString("MONGODB.AUTHENTICATION.USERNAME"),
                        this.configFile.getString("MONGODB.AUTHENTICATION.DATABASE"),
                        this.configFile.getString("MONGODB.AUTHENTICATION.PASSWORD").toCharArray()
                );

                mongoClient = new MongoClient(new ServerAddress(this.configFile.getString("MONGODB.ADDRESS"),
                        this.configFile.getInt("MONGODB.PORT")), Collections.singletonList(credential));
            } else {
                mongoClient = new MongoClient(this.configFile.getString("MONGODB.ADDRESS"),
                        this.configFile.getInt("MONGODB.PORT"));
            }
            mongoDatabase = mongoClient.getDatabase(this.configFile.getString("MONGODB.DATABASE"));
            documentation = mongoDatabase.getCollection("RevampHCF-Documentation");/*
            rank = mongoDatabase.getCollection("RevampHCF-Rank");
            name = mongoDatabase.getCollection("RevampHCF-Name");
            kills = mongoDatabase.getCollection("RevampHCF-Kills");
            lastFactionLeaveMillis = mongoDatabase.getCollection("RevampHCF-LastFactionLeaveMillis");
            deaths = mongoDatabase.getCollection("RevampHCF-Deaths");
            balance = mongoDatabase.getCollection("RevampHCF-Balance");
            showClaimMap = mongoDatabase.getCollection("RevampHCF-ShowClaimMap");
            showLightning = mongoDatabase.getCollection("RevampHCF-ShowLightning");
            reclaimed = mongoDatabase.getCollection("RevampHCF-Reclaimed");
            combatLogger = mongoDatabase.getCollection("RevampHCF-CombatLogger");
            lastDeaths = mongoDatabase.getCollection("RevampHCF-LastDeaths");
            lastSeen = mongoDatabase.getCollection("RevampHCF-LastSeen");
            enderpearlCooldown = mongoDatabase.getCollection("RevampHCF-EnderpearlCooldown");
            gappleCooldown = mongoDatabase.getCollection("RevampHCF-GappleCooldown");
            headappleCooldown = mongoDatabase.getCollection("RevampHCF-HeadappleCooldown");
            pvpTimerCooldown = mongoDatabase.getCollection("RevampHCF-PvpTimerCooldown");
            spawnTagCooldown = mongoDatabase.getCollection("RevampHCF-SpawnTagCooldown");
            hasStarter = mongoDatabase.getCollection("RevampHCF-HasStarter");*/
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(CC.translate(Language.PREFIX + "&cDisabling RevampHCF due to issues with mongo database."));
            Bukkit.getServer().getPluginManager().disablePlugin(this.plugin);
            return false;
        }
    }
}

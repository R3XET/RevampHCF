package eu.revamp.hcf;

import java.util.ArrayList;
import java.util.EnumMap;

import eu.revamp.hcf.file.ConfigFile;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.World;
import java.util.Map;
import java.util.List;
import org.bukkit.ChatColor;
import eu.revamp.hcf.utils.Handler;

@Getter @Setter
public class Configuration extends Handler {
    private int subclaimNameMinCharacters;
    private int subclaimNameMaxCharacters;
    public int factionNameMinCharacters;
    private int factionNameMaxCharacters;
    private int maxMembers;
    private int roadMinHeight;
    private int roadMaxHeight;
    private int maxAllysPerFaction;
    private int maxClaimsPerFaction;
    private int conquestDeathLoss;
    private int conquestWinPoints;
    private int warzoneRadius;
    private ChatColor teammateColor;
    private ChatColor allyColor;
    private ChatColor captainColor;
    private ChatColor enemyColor;
    private ChatColor spawnColor;
    private ChatColor roadColor;
    private ChatColor warzoneColor;
    private ChatColor wildernessColor;
    private boolean kitMap;
    private boolean obsidianGeneratorsEnabled;
    private boolean allowClaimingOnRoads;
    private double dtrIncrementBetweenUpdate;
    private double maxDtr;
    private List<String> blockedfacnames;
    private String dtrWordsBetweenUpdate;
    private int endPortalRadius;
    private double lootingXPMultiplier;
    private double fortuneXPMultiplier;
    private double globalXPMultiplier;
    private double fishingXPMultiplier;
    private double luckXPMultiplier;
    private double smeltXPMultiplier;
    private int endPortalCenter;
    private String timeZone;
    private String dateFormat;
    private int startLives;

    private boolean securityEnabled;

    private long dtrUpdate;
    public Map<World.Environment, Integer> borderSizes;

    public Configuration(RevampHCF instance) {
        super(instance);
        ConfigFile config = RevampHCF.getInstance().getConfig();
        this.blockedfacnames = new ArrayList<>();
        this.borderSizes = new EnumMap<>(World.Environment.class);
        this.kitMap = config.getBoolean("KITMAP_MODE_ENABLED");
        this.blockedfacnames = config.getStringList("BLOCKED-FACTIONS-NAMES");
        this.dateFormat = config.getString("DATE_FORMAT");
        this.timeZone = config.getString("TIMEZONE");
        this.securityEnabled = config.getBoolean("SECURITY_ENABLED");

        this.subclaimNameMinCharacters = config.getInt("SUBCLAIMS.MIN-NAME");
        this.subclaimNameMaxCharacters = config.getInt("SUBCLAIMS.MAX-NAME");
        this.factionNameMinCharacters = config.getInt("FACTIONS-SETTINGS.MIN-NAME");
        this.factionNameMaxCharacters = config.getInt("FACTIONS-SETTINGS.MAX-NAME");
        this.roadMinHeight = config.getInt("FACTIONS-SETTINGS.ROAD-MIN-HEIGHT");
        this.roadMaxHeight = config.getInt("FACTIONS-SETTINGS.ROAD-MAX-HEIGHT");
        this.conquestDeathLoss = config.getInt("CONQUEST.DEATH-LOSS");
        this.conquestWinPoints = config.getInt("CONQUEST.WIN-POINTS");
        this.maxAllysPerFaction = config.getInt("FACTIONS-SETTINGS.MAX-ALLIES");

        this.teammateColor = ChatColor.valueOf(config.getString("TEAMMATE_COLOR"));
        this.allyColor = ChatColor.valueOf(config.getString("ALLY_COLOR"));
        this.captainColor = ChatColor.valueOf(config.getString("CAPTAIN_COLOR"));
        this.enemyColor = ChatColor.valueOf(config.getString("ENEMY_COLOR"));
        this.spawnColor = ChatColor.valueOf(config.getString("SPAWN_COLOR"));
        this.roadColor = ChatColor.valueOf(config.getString("ROAD_COLOR"));
        this.warzoneColor = ChatColor.valueOf(config.getString("WARZONE_COLOR"));
        this.wildernessColor = ChatColor.valueOf(config.getString("WILDERNESS_COLOR"));
        this.allowClaimingOnRoads = config.getBoolean("CLAIMING_ON_ROADS_ENABLED");
        this.obsidianGeneratorsEnabled = config.getBoolean("OBSIDIAN_GENERATORS_ENABLED");
        this.startLives = config.getInt("DEFAULT_LIVES");
        if (isKitMap()) {
            this.borderSizes.put(World.Environment.NORMAL, config.getInt("BORDER.OverWorld"));
            this.borderSizes.put(World.Environment.NETHER, config.getInt("BORDER.Nether"));
            this.borderSizes.put(World.Environment.THE_END, config.getInt("BORDER.End"));
            this.warzoneRadius = config.getInt("FACTIONS-SETTINGS.WARZONE-RADIUS");
            this.maxMembers = config.getInt("FACTIONS-SETTINGS.MAX-MEMBERS");
            this.maxClaimsPerFaction = config.getInt("FACTIONS-SETTINGS.MAX-CLAIMS-PER-FAC");
            this.dtrWordsBetweenUpdate = config.getString("FACTIONS-SETTINGS.dtrwords-update");
            this.dtrUpdate = config.getLong("FACTIONS-SETTINGS.DTR-UPDATE");
            this.dtrIncrementBetweenUpdate = config.getDouble("FACTIONS-SETTINGS.DTR-INCREMENT");
            this.maxDtr = config.getDouble("FACTIONS-SETTINGS.MAX-DTR");
        }
        else {
            this.borderSizes.put(World.Environment.NORMAL, config.getInt("BORDER.OverWorld"));
            this.borderSizes.put(World.Environment.NETHER, config.getInt("BORDER.Nether"));
            this.borderSizes.put(World.Environment.THE_END, config.getInt("BORDER.End"));
            this.warzoneRadius = config.getInt("FACTIONS-SETTINGS.WARZONE-RADIUS");
            this.maxMembers = config.getInt("FACTIONS-SETTINGS.MAX-MEMBERS");
            this.maxClaimsPerFaction = config.getInt("FACTIONS-SETTINGS.MAX-CLAIMS-PER-FAC");
            this.dtrWordsBetweenUpdate = config.getString("FACTIONS-SETTINGS.dtrwords-update");
            this.endPortalCenter = 1000;
            this.endPortalRadius = 25;
            this.lootingXPMultiplier = config.getDouble("EXP.LOOTING");
            this.fortuneXPMultiplier = config.getDouble("EXP.FORTUNE");
            this.globalXPMultiplier = config.getDouble("EXP.GLOBAL");
            this.fishingXPMultiplier = config.getDouble("EXP.FISHING");
            this.luckXPMultiplier = config.getDouble("EXP.LUCK");
            this.smeltXPMultiplier = config.getDouble("EXP.SMELTING");
            this.dtrUpdate = config.getLong("FACTIONS-SETTINGS.DTR-UPDATE");
            this.dtrIncrementBetweenUpdate = config.getDouble("FACTIONS-SETTINGS.DTR-INCREMENT");
            this.maxDtr = config.getDouble("FACTIONS-SETTINGS.MAX-DTR");
        }
    }
}

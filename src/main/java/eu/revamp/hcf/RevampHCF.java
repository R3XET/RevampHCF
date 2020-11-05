package eu.revamp.hcf;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import eu.revamp.hcf.classes.Archer;
import eu.revamp.hcf.classes.Bard;
import eu.revamp.hcf.classes.Miner;
import eu.revamp.hcf.classes.Rogue;
import eu.revamp.hcf.classes.utils.bard.EffectRestorer;
import eu.revamp.hcf.database.DataType;
import eu.revamp.hcf.database.MongoManagerHCF;
//import eu.revamp.hcf.deathban.managers.DeathbanFile; //TODO UNCOMMENT
import eu.revamp.hcf.deathban.managers.DeathbanManager;
import eu.revamp.hcf.extra.Biomes;
import eu.revamp.hcf.extra.Recipes;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.FactionExecutor;
import eu.revamp.hcf.factions.FactionFileManager;
import eu.revamp.hcf.factions.FactionManager;
import eu.revamp.hcf.factions.type.*;
import eu.revamp.hcf.factions.utils.FactionMember;
import eu.revamp.hcf.factions.utils.games.CapturableFaction;
import eu.revamp.hcf.factions.utils.games.ConquestFaction;
import eu.revamp.hcf.factions.utils.games.KothFaction;
import eu.revamp.hcf.factions.utils.zone.CaptureZone;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import eu.revamp.hcf.factions.utils.zone.SubclaimZone;
import eu.revamp.hcf.file.ConfigFile;
import eu.revamp.hcf.file.FileManager;
import eu.revamp.hcf.games.cuboid.Cuboid;
import eu.revamp.hcf.games.cuboid.NamedCuboid;
import eu.revamp.hcf.games.cuboid.PersistableLocation;
import eu.revamp.hcf.games.event.EventExecutor;
import eu.revamp.hcf.handlers.HandlerManager;
import eu.revamp.hcf.handlers.settings.FactionHandler;
import eu.revamp.hcf.integration.PermissionManager;
import eu.revamp.hcf.kit.FlatFileKitManager;
import eu.revamp.hcf.kit.Kit;
import eu.revamp.hcf.kit.KitExecutor;
import eu.revamp.hcf.kit.KitManager;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.hcf.tablist.TabAdapter;
import eu.revamp.hcf.utils.inventory.item.ItemDB;
import eu.revamp.hcf.utils.inventory.item.SimpleItemDB;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.generic.Tasks;
import eu.revamp.spigot.utils.player.PlayerUtils;
import lombok.Getter;
import lombok.Setter;
import me.allen.ziggurat.Ziggurat;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Getter @Setter
public class RevampHCF extends JavaPlugin {

    @Getter @Setter public static RevampHCF instance;
    private HandlerManager handlerManager;
    private Configuration configuration;
    private Archer archer;
    private Bard bard;
    private Miner miner;
    private Rogue rogue;
    private PlayerFaction playerfaction;
    private EffectRestorer effectRestorer;
    private WorldEditPlugin worldEdit;
    private ItemDB itemDB;
    private HCFPlayerData hcfPlayerData;

    private FactionHandler factionHandler;

    private FactionManager factionManager;
    private Recipes recipes;
    private PermissionManager permissionManager;
    private FileManager fileManager;

    private KitManager kitManager;


    private ConfigFile language, dataBase, config, factions, limiters, schedules, tablist, scoreboard, utilities, location, kits;


    private MongoManagerHCF mongoManagerHCF;
    private DeathbanManager deathbanManager;

    private DataType databaseType = DataType.YAML;


    @Override
    public void onEnable() {
        Biomes.setupBiomes();


        instance = this;
        this.registerConfiguration();

        this.loadLanguages();

        this.language = new ConfigFile(this, "language.yml");
        this.dataBase = new ConfigFile(this, "database.yml");
        this.config = new ConfigFile(this, "config.yml");
        this.factions = new ConfigFile(this, "factions.yml");
        this.limiters = new ConfigFile(this, "limiters.yml");
        this.location = new ConfigFile(this, "location.yml");
        this.schedules = new ConfigFile(this, "schedules.yml");
        this.scoreboard = new ConfigFile(this, "scoreboard.yml");
        this.tablist = new ConfigFile(this, "tablist.yml");
        this.utilities = new ConfigFile(this, "utilities.yml");
        this.kits = new ConfigFile(this, "kits.yml");


        this.configuration = new Configuration(this);

        this.permissionManager = new PermissionManager(this);
        this.handlerManager = new HandlerManager(this);

        this.handlerManager.setupHandlers();
        this.handlerManager.setupBooleans();
        this.handlerManager.enableHandlers();

        this.factionHandler = new FactionHandler(this);

        this.factionManager = new FactionFileManager(this);


        this.kitManager = new FlatFileKitManager(this);


        this.fileManager = new FileManager(this);
        this.fileManager.setupDirectories();


        this.permissionManager.setupPermissionSystem();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");


        this.effectRestorer = new EffectRestorer(this);
        this.itemDB = new SimpleItemDB(this);
        this.recipes = new Recipes(this);
        //getServer().getPluginManager().registerEvents(new ToolsEnchants(), this);

        //this.databaseType = config.getString("DATA_TYPE");

        if (databaseType.equals(DataType.MONGO)) {

        this.mongoManagerHCF = new MongoManagerHCF(this);
        if (!this.mongoManagerHCF.connect()) return;

        }

        //this.deathbanManager = new DeathbanFile(this); //TODO UNCOMMENT

        this.setupTablist();


        this.getCommand("faction").setExecutor(new FactionExecutor(this));
        this.getCommand("event").setExecutor(new EventExecutor(this));
        this.getCommand("kit").setExecutor(new KitExecutor(this));

        this.setWorldEdit((WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit"));

        Tasks.runLater(this, ()-> {
            for (String command : RevampHCF.getInstance().getConfig().getStringList("STARTUP_COMMANDS")) {
                getServer().dispatchCommand(getServer().getConsoleSender(), command);
            }
        }, 300L);

        Tasks.runAsyncTimer(this, () -> {
            Bukkit.broadcastMessage(RevampHCF.getInstance().getConfig().getString("SAVE.SAVING"));
            RevampHCF.this.saveData();
            Bukkit.broadcastMessage(RevampHCF.getInstance().getConfig().getString("SAVE.SUCCESSFULLY"));
        }, 100L, TimeUnit.MINUTES.toMillis(RevampHCF.getInstance().getConfig().getInt("SAVE.INTERVAL") * 20L));

        //new BootsEnchants().runTaskTimer(this, 120L, 65L);
        //new ChestPlateEnchants().runTaskTimer(this, 120L, 65L);
        //new LeggingsEnchants().runTaskTimer(this, 120L, 65L);
        //new LeggingsEnchants().runTaskTimer(this, 120L, 65L);
        //new ToolsEnchants().runTaskTimer(this, 120L, 65L);


        this.log("&c======&7===========================&c=====");
        this.log("&bRevampHCF has been successfully loaded.");
        this.log("");
        this.log("&bVersion&7: &3v" + this.getDescription().getVersion());
        this.log("&bName&7: &3" + this.getDescription().getName());
        this.log(" ");
        this.log("&bDatabase&7: &a" + this.databaseType.toString());
        this.log("&c======&7===========================&c=====");
    }

    private void log(String message) {
        Bukkit.getConsoleSender().sendMessage(CC.translate(message));
    }

    public void reloadFiles() {
        this.loadLanguages();

        this.language = new ConfigFile(this, "language.yml");
        this.dataBase = new ConfigFile(this, "database.yml");
        this.config = new ConfigFile(this, "config.yml");
        this.factions = new ConfigFile(this, "factions.yml");
        this.limiters = new ConfigFile(this, "limiters.yml");
        this.location = new ConfigFile(this, "location.yml");
        this.schedules = new ConfigFile(this, "schedules.yml");
        this.scoreboard = new ConfigFile(this, "scoreboard.yml");
        this.tablist = new ConfigFile(this, "tablist.yml");
        this.utilities = new ConfigFile(this, "utilities.yml");
        this.kits = new ConfigFile(this, "kits.yml");
    }

    private void registerListeners() {
        Bukkit.getConsoleSender().sendMessage(CC.translate("&bLoading listeners..."));

        //ListenerHandler.loadListenersFromPackage(this, "eu.revamp.hcf.handlers.basics");
        //ListenerHandler.loadListenersFromPackage(this, "eu.revamp.hcf.handlers.blocks");
        //ListenerHandler.loadListenersFromPackage(this, "eu.revamp.hcf.handlers.chat");


        Bukkit.getConsoleSender().sendMessage(CC.translate("&bLoaded all listeners."));
    }

    @Override
    public void onDisable() {
        Ziggurat.setInstance(null);
        //RevampTab.getInstance().onDisable();

        for (Player online : PlayerUtils.getOnlinePlayers()) {
            HCFPlayerData playerData = this.getHandlerManager().getHCFPlayerDataHandler().getPlayerData(online.getUniqueId());
            playerData.saveData();
        }
        PlayerUtils.getOnlinePlayers().forEach(player -> {
            HCFPlayerData playerData = this.getHandlerManager().getHCFPlayerDataHandler().getPlayerData(player.getUniqueId());
            if (playerData != null) {
                playerData.saveData();
            }
        });


        if (this.mongoManagerHCF != null && this.mongoManagerHCF.getMongoClient() != null) {
            this.mongoManagerHCF.getMongoClient().close();
        }



        this.utilities.save();
        this.location.save();
        this.schedules.save();
        this.factions.save();
        this.saveData();
        this.handlerManager.disableHandlers();
    }

    public void setupTablist() {
        if (this.tablist.getBoolean("enabled")) {
            if (this.getServer().getPluginManager().getPlugin("ProtocolSupport") != null || this.getServer().getPluginManager().getPlugin("ViaVersion") != null || this.getServer().getPluginManager().getPlugin("ProtocolLib") != null) {
                try {
                    //new TabListHandler(this);
                    new Ziggurat(this, new TabAdapter(this));
                } catch (Exception e) {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Log-1] &eIt seems like that the TabAPI doesn't support your version of server. &7(&b" + Bukkit.getServer().getClass().getPackage().getName().substring(23) + "&7) &7(&c" + e.getMessage() + "&7)"));
                }
            } else {
                Bukkit.getConsoleSender().sendMessage("&c[Log-1] &eYou need ProtocolSupport, ViaVersion or ProtocolLib for TabAPI to work properly, disabling for now.&7(&b" + Bukkit.getServer().getClass().getPackage().getName().substring(23) + "&7)");
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&c[Log-1] &eCustom tab feature is disabled for now .&7(&b" + Bukkit.getServer().getClass().getPackage().getName().substring(23) + "&7)"));
        }
    }

    public void saveData() {
        this.factionManager.saveFactionData();
        this.getServer().getOnlinePlayers().forEach(Player::saveData);
        this.getServer().savePlayers();
        try {
            for (World world : Bukkit.getWorlds()) {
                world.save();
            }
        } catch (Exception ignored) {
        }
        Bukkit.savePlayers();
        this.kitManager.saveKitData();
        //this.deathbanManager.saveDeathbanData();
    }

    public void registerConfiguration() {
        ConfigurationSerialization.registerClass(PersistableLocation.class);
        ConfigurationSerialization.registerClass(Cuboid.class);
        ConfigurationSerialization.registerClass(NamedCuboid.class);
        ConfigurationSerialization.registerClass(CaptureZone.class);
        ConfigurationSerialization.registerClass(ClaimZone.class);
        ConfigurationSerialization.registerClass(SubclaimZone.class);
        ConfigurationSerialization.registerClass(ClaimableFaction.class);
        ConfigurationSerialization.registerClass(ConquestFaction.class);
        ConfigurationSerialization.registerClass(CapturableFaction.class);
        ConfigurationSerialization.registerClass(KothFaction.class);
        ConfigurationSerialization.registerClass(EndPortalFaction.class);
        ConfigurationSerialization.registerClass(Faction.class);
        ConfigurationSerialization.registerClass(FactionMember.class);
        ConfigurationSerialization.registerClass(PlayerFaction.class);
        ConfigurationSerialization.registerClass(RoadFaction.class);
        ConfigurationSerialization.registerClass(SpawnFaction.class);
        ConfigurationSerialization.registerClass(RoadFaction.NorthRoadFaction.class);
        ConfigurationSerialization.registerClass(RoadFaction.EastRoadFaction.class);
        ConfigurationSerialization.registerClass(RoadFaction.SouthRoadFaction.class);
        ConfigurationSerialization.registerClass(RoadFaction.WestRoadFaction.class);
        ConfigurationSerialization.registerClass(GlowstoneFaction.class);
        ConfigurationSerialization.registerClass(TournamentFaction.class);


        ConfigurationSerialization.registerClass(Kit.class);
    }

    public void sendToServer(Player player, String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(this, "BungeeCord", b.toByteArray());
    }

    private void loadLanguages() {
        if (this.language == null) {
            return;
        }
        Arrays.stream(Language.values()).forEach(language -> {
            if (this.language.getString(language.getPath()) == null) {
                if (language.getValue() != null) {
                    this.language.set(language.getPath(), language.getValue());
                } else if (language.getListValue() != null && this.language.getStringList(language.getPath()) == null) {
                    this.language.set(language.getPath(), language.getListValue());
                }
            }
        });
        this.language.save();
    }
}

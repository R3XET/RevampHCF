package eu.revamp.hcf.handlers;

import eu.revamp.hcf.Configuration;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.classes.utils.ArmorClassManager;
import eu.revamp.hcf.commands.EssentialsCommandHandler;
import eu.revamp.hcf.deathban.DeathBanHandler;
import eu.revamp.hcf.factions.type.GlowstoneFaction;
import eu.revamp.hcf.file.ConfigHandler;
import eu.revamp.hcf.games.schedule.ScheduleHandler;
import eu.revamp.hcf.handlers.basics.SecurityHandler;
import eu.revamp.hcf.handlers.basics.StaffHandler;
import eu.revamp.hcf.handlers.basics.WorldEditCrashFixHandler;
import eu.revamp.hcf.handlers.blocks.AutoSmeltOreHandler;
import eu.revamp.hcf.handlers.blocks.BlockPickupHandler;
import eu.revamp.hcf.handlers.blocks.FoundDiamondsHandler;
import eu.revamp.hcf.handlers.blocks.FurnaceSpeedHandler;
import eu.revamp.hcf.handlers.chat.DeathMessageHandler;
import eu.revamp.hcf.handlers.claim.BorderHandler;
import eu.revamp.hcf.handlers.claim.ClaimHandler;
import eu.revamp.hcf.handlers.claim.ClaimWandHandler;
import eu.revamp.hcf.handlers.combat.CombatLoggerHandler;
import eu.revamp.hcf.handlers.custom.CustomEnchantHandler;
/*import eu.revamp.hcf.handlers.disguise.BreakModule;
import eu.revamp.hcf.handlers.disguise.ClickModule;
import eu.revamp.hcf.handlers.disguise.DamageModule;
import eu.revamp.hcf.handlers.disguise.PlaceModule;*/
import eu.revamp.hcf.handlers.elevators.MinecartElevatorHandler;
import eu.revamp.hcf.handlers.elevators.SignElevatorHandler;
import eu.revamp.hcf.handlers.enchant.EnchantmentLimiterHandler;
import eu.revamp.hcf.handlers.essentials.BackHandler;
import eu.revamp.hcf.handlers.essentials.GodHandler;
import eu.revamp.hcf.handlers.essentials.HCFHandler;
import eu.revamp.hcf.handlers.essentials.WarpHandler;
import eu.revamp.hcf.handlers.fix.*;
import eu.revamp.hcf.handlers.fix.bed.AntiBedBombingHandler;
import eu.revamp.hcf.handlers.fix.bed.AntiBedHandler;
import eu.revamp.hcf.handlers.fix.glitch.*;
import eu.revamp.hcf.handlers.fix.security.AntiCreativeBypassHandler;
import eu.revamp.hcf.handlers.fix.security.ColonCommandFixListener;
import eu.revamp.hcf.handlers.games.EOTWHandler;
import eu.revamp.hcf.handlers.games.EOTWUtilsHandler;
import eu.revamp.hcf.handlers.games.EndPortalHandler;
import eu.revamp.hcf.handlers.games.GlowstoneMountainHandler;
import eu.revamp.hcf.handlers.hcf.BookDisenchantHandler;
import eu.revamp.hcf.handlers.hcf.EnderchestHandler;
import eu.revamp.hcf.handlers.hcf.EnderpearlEventsHandler;
import eu.revamp.hcf.handlers.hcf.ItemPickupPvPTimerHandler;
import eu.revamp.hcf.handlers.hcf.blocks.BlockBreakInCombatHandler;
import eu.revamp.hcf.handlers.hcf.blocks.BlockPlaceInCombatHandler;
import eu.revamp.hcf.handlers.hcf.spawners.end.AntiSpawnersBreakEndHandler;
import eu.revamp.hcf.handlers.hcf.spawners.end.AntiSpawnersPlaceEndHandler;
import eu.revamp.hcf.handlers.hcf.spawners.nether.AntiSpawnersBreakNetherHandler;
import eu.revamp.hcf.handlers.hcf.spawners.nether.AntiSpawnersPlaceNetherHandler;
import eu.revamp.hcf.handlers.items.CrowbarHandler;
import eu.revamp.hcf.handlers.items.ItemStatTrackingHandler;
import eu.revamp.hcf.handlers.mob.EnderDragonListener;
import eu.revamp.hcf.handlers.mob.MobStackHandler;
import eu.revamp.hcf.handlers.other.GoldenHeadHandler;
import eu.revamp.hcf.handlers.other.RankReviveHandler;
import eu.revamp.hcf.handlers.player.*;
import eu.revamp.hcf.handlers.portal.PortalHandler;
import eu.revamp.hcf.handlers.potion.BrewingSpeedHandler;
import eu.revamp.hcf.handlers.potion.PotionLimitHandler;
import eu.revamp.hcf.handlers.settings.*;
import eu.revamp.hcf.handlers.shop.ItemShopHandler;
import eu.revamp.hcf.handlers.shop.ShopHandler;
import eu.revamp.hcf.handlers.shop.ShopSignHandler;
import eu.revamp.hcf.handlers.shop.SpawnerShopHandler;
import eu.revamp.hcf.handlers.signs.EventSignHandler;
import eu.revamp.hcf.handlers.signs.KitSignHandler;
import eu.revamp.hcf.handlers.signs.SignHandler;
import eu.revamp.hcf.handlers.signs.SignSubclaimHandler;
//import eu.revamp.hcf.kit.FlatFileKitManager;
//import eu.revamp.hcf.kit.KitListener;
//import eu.revamp.hcf.kit.KitManager;
import eu.revamp.hcf.playerdata.HCFPlayerDataHandler;
import eu.revamp.hcf.scoreboard.ScoreboardHandler;
import eu.revamp.hcf.scoreboard.ScoreboardTagEvents;
import eu.revamp.hcf.timers.*;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.hcf.utils.timer.TimerManager;
import eu.revamp.hcf.visualise.VisualiseHandler;
import eu.revamp.hcf.visualise.WallBorderHandler;
import eu.revamp.spigot.utils.time.TimeUtils;
import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public class HandlerManager extends Handler {

    private final Configuration configuration;
    private SecurityHandler securityHandler;
    private StaffHandler staffHandler;
    private WorldEditCrashFixHandler worldEditCrashFixHandler;
    private BackHandler backHandler;
    private GodHandler godHandler;
    private HCFHandler hcfHandler;
    private WarpHandler warpHandler;
    private ArmorFixHandler armorfixHandler;
    private AntiBlockHitHandler antiBlockHitHandler;
    private AntiBlockJumpHandler antiBlockJumpHandler;
    private BoatGlitchFixListener boatglitchHandler;
    private ColonCommandFixListener coloncommandHandler;
    private AntiCreativeBypassHandler antiCreativeBypassHandler;
    private MonsterSpawnHandler monsterSpawnHandler;
    //private HitDetectionListener hitdetectionHandler;
    private IllegalEnchantHandler illegalenchantHandler;
    private InfinityArrowFixListener infinityarrowHandler;
    private PortalTrapFixHandler portalTrapFixHandler;
    private AutoSmeltOreHandler smeltoreHandler;
    private NoPotLagHandler nopotlagHandler;
    private MobFarmListener mobfarmHandler;
    private AntiPhaseShovelHandler antiPhaseShovelHandler;
    private BlockPickupHandler blockPickupHandler;
    private SignElevatorHandler signElevatorHandler;
    private MinecartElevatorHandler minecartelevatorHandler;
    private KillStreakHandler killStreakHandler;
    private KitSignHandler kitSignHandler;
    private ClaimWandHandler claimWandHandler;
    private SignSubclaimHandler signSubclaimHandler;
    private ServerFullHandler serverfullHandler;
    private EnderDragonListener enderdragonHandler;
    private EnchantmentLimiterHandler enchantmentLimiterHandler;
    private PotionLimitHandler potionLimitHandler;
    private BorderHandler borderHandler;
    private BrewingSpeedHandler brewingSpeedHandler;
    private ClaimHandler claimHandler;
    private CombatLoggerHandler combatLoggerHandler;
    private CrowbarHandler crowbarHandler;
    private DeathMessageHandler deathMessageHandler;
    private DeathSignHandler deathSignHandler;
    private EOTWHandler eotwHandler;
    private EventSignHandler eventSignHandler;
    private ExpHandler expHandler;
    private ShopHandler shopHandler;
    private ItemShopHandler itemshopHandler;
    private FoundDiamondsHandler foundDiamondsHandler;
    private GlowstoneMountainHandler glowstoneMountainHandler;
    private GlowstoneFaction glowstonefaction;
    private ItemStatTrackingHandler itemStatTrackingHandler;
    private KitMapHandler kitMapHandler;
    private MapKitHandler mapKitHandler;
    private PortalHandler portalHandler;
    private ShopSignHandler shopSignHandler;
    private SpawnerShopHandler spawnerShopHandler;
    private SignHandler signHandler;
    private DynamicPlayerHandler dynamicPlayerHandler;
    private FurnaceSpeedHandler furnaceSpeedHandler;
    private MobStackHandler mobStackHandler;
    private ProtectionHandler protectionHandler;
    private RankReviveHandler rankReviveHandler;
    private XPMultiplierHandler xpHandler;
    private WhitelistHandler wlHandler;
    private VisualiseHandler visualiseHandler;
    private ScheduleHandler scheduleHandler;
    private DeathBanHandler deathBanHandler;
    private ArmorClassManager armorClassManager;
    private TimerManager timerManager;
    private EOTWUtilsHandler eotwUtils;
    private EndPortalHandler endPortalHandler;
    private ClassWarmupHandler classLoadTimer;
    private AppleHandler appleHandler;
    private HeadAppleHandler headappleHandler;
    private EnderpearlHandler enderpearlHandler;
    private PvPTimerHandler pvpTimerHandler;
    private GappleHandler gappleHandler;
    private LogoutHandler logoutHandler;
    private SOTWHandler sotwHandler;
    private SaleOFFHandler saleoffHandler;
    private KeySaleHandler keysaleHandler;
    private GlowstoneHandler glowstoneHandler;
    private RebootHandler rebootHandler;
    private CustomEnchantHandler customEnchantHandler;
    //private DragEventEnchants dragEventEnchants;
    private EssentialsCommandHandler essentialsCommandHandler;
    private WallBorderHandler wallBorderHandler;
    //private TabListHandler tabListHandler;
    private AntiBedHandler antiBedHandler;
    private AntiBedBombingHandler antiBedBombingHandler;
    private EnderchestHandler enderchestHandler;
    private BookDisenchantHandler bookDisenchantHandler;
    private EnderpearlEventsHandler enderpearlEventsHandler;
    private ItemPickupPvPTimerHandler itemPickupPvPTimerHandler;
    private BlockPlaceInCombatHandler blockPlaceInCombatHandler;
    private BlockBreakInCombatHandler blockBreakInCombatHandler;
    private HCFPlayerDataHandler HCFPlayerDataHandler;
    private GoldenHeadHandler goldenheadHandler;
    private ScoreboardHandler scoreboardHandler;
    private ScoreboardTagEvents scoreboardTagEvents;
    private AntiSpawnersBreakEndHandler antiSpawnersBreakEndHandler;
    private AntiSpawnersBreakNetherHandler antiSpawnersBreakNetherHandler;
    private AntiSpawnersPlaceEndHandler antiSpawnersPlaceEndHandler;
    private AntiSpawnersPlaceNetherHandler antiSpawnersPlaceNetherHandler;
    /*private BreakModule breakModule;
    private ClickModule clickModule;
    private DamageModule damageModule;
    private PlaceModule placeModule;*/
    //private DisguiseCommand disguiseCommand;
    private ConfigHandler configHandler;
   // private KitManager kitManager;
    //private KitListener kitListener;
    private SugarCaneFixHandler sugarCaneFixHandler;
    private DupeGlitchHandler dupeGlitchHandler;
    private FixInvisListener fixInvisListener;

    public HandlerManager(RevampHCF instance) {
        super(instance);
        this.configuration = new Configuration(this.getInstance());
    }

    public void setupHandlers(){
        this.essentialsCommandHandler = new EssentialsCommandHandler(this.getInstance());
        this.configHandler = new ConfigHandler(this.getInstance());
        this.visualiseHandler = new VisualiseHandler();

        this.HCFPlayerDataHandler = new HCFPlayerDataHandler(this.getInstance());
        this.scheduleHandler = new ScheduleHandler(this.getInstance());
        this.claimHandler = new ClaimHandler(this.getInstance());

        this.appleHandler = new AppleHandler(this.getInstance());
        this.wallBorderHandler = new WallBorderHandler(this.getInstance());
        this.headappleHandler = new HeadAppleHandler(this.getInstance());
        this.enderpearlHandler = new EnderpearlHandler(this.getInstance());
        this.eventSignHandler = new EventSignHandler(this.getInstance());
        this.gappleHandler = new GappleHandler(this.getInstance());
        this.logoutHandler = new LogoutHandler(this.getInstance());
        this.sotwHandler = new SOTWHandler(this.getInstance());
        this.saleoffHandler = new SaleOFFHandler(this.getInstance());
        this.keysaleHandler = new KeySaleHandler(this.getInstance());
        this.glowstoneHandler = new GlowstoneHandler(this.getInstance());
        this.rebootHandler = new RebootHandler(this.getInstance());
        this.mobStackHandler = new MobStackHandler(this.getInstance());
        this.shopHandler = new ShopHandler(this.getInstance());
        this.itemshopHandler = new ItemShopHandler(this.getInstance());
        this.brewingSpeedHandler = new BrewingSpeedHandler(this.getInstance());
        this.boatglitchHandler = new BoatGlitchFixListener(this.getInstance());
        this.coloncommandHandler = new ColonCommandFixListener(this.getInstance());
        this.smeltoreHandler = new AutoSmeltOreHandler(this.getInstance());
        this.mobfarmHandler = new MobFarmListener(this.getInstance());
        this.deathMessageHandler = new DeathMessageHandler(this.getInstance());
        this.blockPickupHandler = new BlockPickupHandler(this.getInstance());
        this.signElevatorHandler = new SignElevatorHandler(this.getInstance());
        this.minecartelevatorHandler = new MinecartElevatorHandler(this.getInstance());
        this.crowbarHandler = new CrowbarHandler(this.getInstance());
        this.itemStatTrackingHandler = new ItemStatTrackingHandler(this.getInstance());
        this.endPortalHandler = new EndPortalHandler(this.getInstance());
        this.portalHandler = new PortalHandler(this.getInstance());
        this.furnaceSpeedHandler = new FurnaceSpeedHandler(this.getInstance());
        this.protectionHandler = new ProtectionHandler(this.getInstance());
        this.shopSignHandler = new ShopSignHandler(this.getInstance());
        this.signSubclaimHandler = new SignSubclaimHandler(this.getInstance());
        this.serverfullHandler = new ServerFullHandler(this.getInstance());
        this.enderdragonHandler = new EnderDragonListener(this.getInstance());
        this.eotwHandler = new EOTWHandler(this.getInstance());
        this.claimWandHandler = new ClaimWandHandler(this.getInstance());
        this.borderHandler = new BorderHandler(this.getInstance());
        this.spawnerShopHandler = new SpawnerShopHandler(this.getInstance());
        this.signHandler = new SignHandler(this.getInstance());
        this.foundDiamondsHandler = new FoundDiamondsHandler(this.getInstance());
        this.mapKitHandler = new MapKitHandler(this.getInstance());
        this.dynamicPlayerHandler = new DynamicPlayerHandler(this.getInstance());
        this.xpHandler= new XPMultiplierHandler(this.getInstance());
        this.wlHandler = new WhitelistHandler(this.getInstance());
        this.expHandler = new ExpHandler(this.getInstance());
        this.armorClassManager = new ArmorClassManager(this.getInstance());
        this.eotwUtils = new EOTWUtilsHandler(this.getInstance());
        this.securityHandler = new SecurityHandler(this.getInstance());
        this.staffHandler = new StaffHandler(this.getInstance());
        this.worldEditCrashFixHandler = new WorldEditCrashFixHandler(this.getInstance());
        this.backHandler = new BackHandler(this.getInstance());
        this.godHandler = new GodHandler(this.getInstance());
        this.hcfHandler = new HCFHandler(this.getInstance());
        this.warpHandler = new WarpHandler(this.getInstance());
        //setHitdetectionHandler(new HitDetectionListener(this.getInstance());
        this.infinityarrowHandler = new InfinityArrowFixListener(this.getInstance());
        this.customEnchantHandler = new CustomEnchantHandler(this.getInstance());
        //setDragEventEnchants(new DragEventEnchants(this.getInstance());
        //setDoubleJumpHandler(new DoubleJumpHandler(this.getInstance());
        /*this.breakModule = new BreakModule(this.getInstance());
        this.clickModule = new ClickModule(this.getInstance());
        this.damageModule = new DamageModule(this.getInstance());
        this.placeModule = new PlaceModule(this.getInstance());*/
        //setDisguiseCommand(new DisguiseCommand(this.getInstance());

        this.combatLoggerHandler = new CombatLoggerHandler(this.getInstance());
        this.goldenheadHandler = new GoldenHeadHandler(this.getInstance());
        //setKitManager(new FlatFileKitManager(this.getInstance());
        //setKitListener(new KitListener(this.getInstance());
        this.dupeGlitchHandler = new DupeGlitchHandler(this.getInstance());
        this.sugarCaneFixHandler = new SugarCaneFixHandler(this.getInstance());
        this.fixInvisListener = new FixInvisListener(this.getInstance());

        if (!this.configuration.isKitMap()) {
            this.rankReviveHandler = new RankReviveHandler(this.getInstance());
            this.deathBanHandler = new DeathBanHandler(this.getInstance());
            this.glowstoneMountainHandler = new GlowstoneMountainHandler(this.getInstance());
            this.potionLimitHandler = new PotionLimitHandler(this.getInstance());
            this.enchantmentLimiterHandler = new EnchantmentLimiterHandler(this.getInstance());
            this.glowstoneHandler.cancel();
            this.glowstoneHandler.start(TimeUtils.convert(RevampHCF.getInstance().getConfig().getInt("GLOWSTONE_MOUNTAIN_RESET_TIME"), 'm'));

        }
        if (configuration.isKitMap()) {
            this.kitSignHandler = new KitSignHandler(this.getInstance());
            this.killStreakHandler = new KillStreakHandler(this.getInstance());
            this.kitMapHandler = new KitMapHandler(this.getInstance());
        }
        this.timerManager = new TimerManager(this.getInstance());

        this.scoreboardTagEvents = new ScoreboardTagEvents(this.getInstance());


        this.scoreboardHandler = new ScoreboardHandler(this.getInstance());
        this.scoreboardHandler.enable();
        this.scoreboardHandler.setupHCFScoreboard();
    }

    public void enableHandlers(){
        this.wallBorderHandler.enable();
        this.appleHandler.enable();
        this.headappleHandler.enable();
        this.itemStatTrackingHandler.enable();
        this.scoreboardTagEvents.enable();
        this.HCFPlayerDataHandler.enable();
        this.gappleHandler.enable();
        this.enderpearlHandler.enable();
        this.scheduleHandler.enable();
        this.logoutHandler.enable();
        this.eventSignHandler.enable();
        this.sotwHandler.enable();
        this.saleoffHandler.enable();
        this.keysaleHandler.enable();
        this.glowstoneHandler.enable();
        this.rebootHandler.enable();
        this.blockPickupHandler.enable();
        this.spawnerShopHandler.enable();
        this.combatLoggerHandler.enable();
        this.signHandler.enable();
        this.boatglitchHandler.enable();
        this.coloncommandHandler.enable();
        //getHitdetectionHandler.enable();
        this.infinityarrowHandler.enable();
        this.smeltoreHandler.enable();
        this.mobfarmHandler.enable();
        this.mobStackHandler.enable();
        this.foundDiamondsHandler.enable();
        this.mapKitHandler.enable();
        this.dynamicPlayerHandler.enable();
        this.securityHandler.enable();
        this.serverfullHandler.enable();
        this.enderdragonHandler.enable();
        this.staffHandler.enable();
        this.worldEditCrashFixHandler.enable();
        this.godHandler.enable();
        this.hcfHandler.enable();
        this.backHandler.enable();
        this.warpHandler.enable();
        this.customEnchantHandler.enable();
        //getDragEventEnchants.enable();
        /*this.placeModule.enable();
        this.clickModule.enable();
        this.damageModule.enable();
        this.breakModule.enable();*/
        //getDisguiseCommand.enable();
        this.goldenheadHandler.enable();
        this.protectionHandler.enable();
        this.fixInvisListener.enable();
        //getKitListener.enable();
        if (configuration.isKitMap()) {
            this.shopHandler.enable();
            this.itemshopHandler.enable();
            this.kitSignHandler.enable();
            this.mapKitHandler.enable();
        }
        if (!configuration.isKitMap()) {
            this.deathBanHandler.enable();
            this.enchantmentLimiterHandler.enable();
            this.potionLimitHandler.enable();
            this.rankReviveHandler.enable();
        }
    }
    public void disableHandlers(){
        this.logoutHandler.disable();
        this.scheduleHandler.disable();
        this.combatLoggerHandler.disable();
        this.signHandler.disable();
        this.mobStackHandler.disable();
        this.mapKitHandler.disable();
        this.armorClassManager.onDisable();
        this.backHandler.disable();
        this.godHandler.disable();
        this.warpHandler.disable();
        //getTabListHandler.disable();
        //getHitdetectionHandler.disable();
        this.HCFPlayerDataHandler.disable();
        this.scoreboardHandler.disable();
        this.scoreboardTagEvents.disable();
       // getKitListener.disable();
        this.classLoadTimer.disable();
        if (!this.configuration.isKitMap()) {
            this.deathBanHandler.disable();
            this.rankReviveHandler.disable();
            this.potionLimitHandler.disable();
            this.enchantmentLimiterHandler.disable();
        }
        if (this.configuration.isKitMap()) {
            this.kitSignHandler.disable();
        }
        Bukkit.getScheduler().cancelTasks(this.getInstance());
        RevampHCF.setInstance(null);
    }

    public void setupBooleans(){
        this.antiBedHandler = new AntiBedHandler(this.getInstance());
        this.antiBedBombingHandler = new AntiBedBombingHandler(this.getInstance());
        this.enderchestHandler = new EnderchestHandler(this.getInstance());
        this.bookDisenchantHandler = new BookDisenchantHandler(this.getInstance());
        this.itemPickupPvPTimerHandler = new ItemPickupPvPTimerHandler(this.getInstance());
        this.enderpearlEventsHandler = new EnderpearlEventsHandler(this.getInstance());
        this.blockPlaceInCombatHandler = new BlockPlaceInCombatHandler(this.getInstance());
        this.blockBreakInCombatHandler = new BlockBreakInCombatHandler(this.getInstance());
        this.deathSignHandler = new DeathSignHandler(this.getInstance());
        this.antiPhaseShovelHandler = new AntiPhaseShovelHandler(this.getInstance());
        this.illegalenchantHandler = new IllegalEnchantHandler(this.getInstance());
        this.nopotlagHandler = new NoPotLagHandler(this.getInstance());
        this.armorfixHandler = new ArmorFixHandler(this.getInstance());
        this.monsterSpawnHandler = new MonsterSpawnHandler(this.getInstance());
        this.portalTrapFixHandler = new PortalTrapFixHandler(this.getInstance());
        this.antiCreativeBypassHandler = new AntiCreativeBypassHandler(this.getInstance());
        this.antiBlockJumpHandler = new AntiBlockJumpHandler(this.getInstance());
        this.antiBlockHitHandler = new AntiBlockHitHandler(this.getInstance());
        this.antiSpawnersBreakEndHandler = new AntiSpawnersBreakEndHandler(this.getInstance());
        this.antiSpawnersBreakNetherHandler = new AntiSpawnersBreakNetherHandler(this.getInstance());
        this.antiSpawnersPlaceEndHandler = new AntiSpawnersPlaceEndHandler(this.getInstance());
        this.antiSpawnersPlaceNetherHandler = new AntiSpawnersPlaceNetherHandler(this.getInstance());

        if (this.getInstance().getConfig().getBoolean("BEDS_ENABLED")) {
            this.antiBedHandler.enable();
        }
        if (!this.getInstance().getConfig().getBoolean("BED_BOMBING_ENABLED")) {
            this.antiBedBombingHandler.enable();
        }
        if (!this.getInstance().getConfig().getBoolean("ENDERCHEST_HCF_ENABLED")){
            this.enderchestHandler.enable();
        }
        if (this.getInstance().getConfig().getBoolean("BOOK_DISENCHANT_ENABLED")){
            this.bookDisenchantHandler.enable();
        }
        if (!this.getInstance().getConfig().getBoolean("PEARLS_IN_EVENT_ENABLED")){
            this.itemPickupPvPTimerHandler.enable();
        }
        if (!this.getInstance().getConfig().getBoolean("ALLOW_ITEM_PICKUP_PVPTIMER")){
            this.enderpearlEventsHandler.enable();
        }
        if (!this.getInstance().getConfig().getBoolean("PLACE_BLOCKS_IN_COMBAT_ENABLED")){
            this.blockPlaceInCombatHandler.enable();
        }
        if (!this.getInstance().getConfig().getBoolean("BREAK_BLOCK_IN_COMBAT_ENABLED")){
            this.blockBreakInCombatHandler.enable();
        }
        if (this.getInstance().getConfig().getBoolean("DEATH_SIGNS_ENABLED")){
            this.deathSignHandler.enable();
        }
        if (this.getInstance().getConfig().getBoolean("ANTI_PHASE_SHOVEL_ENABLED")){
            this.antiPhaseShovelHandler.enable();
        }
        if (this.getInstance().getConfig().getBoolean("ILLEGAL_ENCHANT_CHECKER_ENABLED")){
            this.illegalenchantHandler.enable();
        }
        if (this.getInstance().getConfig().getBoolean("ANTI_POTLAG_ENABLED")){
            this.nopotlagHandler.enable();
        }
        if (this.getInstance().getConfig().getBoolean("ARMOR_DURABILITY_FIX_ENABLED")){
            this.armorfixHandler.enable();
        }
        if (!this.getInstance().getConfig().getBoolean("MONSTER_SPAWN_ENABLED")){
            this.monsterSpawnHandler.enable();
        }
        if (this.getInstance().getConfig().getBoolean("ANTI_PORTAL_TRAP_ENABLED")){
            this.portalTrapFixHandler.enable();
        }
        if (this.getInstance().getConfig().getBoolean("ANTI_CREATIVE_BYPASS_ENABLED")){
            this.antiCreativeBypassHandler.enable();
        }
        if (this.getInstance().getConfig().getBoolean("ANTI_BLOCK_JUMP_ENABLED")){
            this.antiBlockJumpHandler.enable();
        }
        if (this.getInstance().getConfig().getBoolean("ANTI_BLOCK_HIT_ENABLED")){
            this.antiBlockHitHandler.enable();
        }
        if (!this.getInstance().getConfig().getBoolean("SPAWNER_BREAK_ENABLED.IN_END")){
            this.antiSpawnersBreakEndHandler.enable();
        }
        if (!this.getInstance().getConfig().getBoolean("SPAWNER_BREAK_ENABLED.IN_NETHER")){
            this.antiSpawnersBreakNetherHandler.enable();
        }
        if (!this.getInstance().getConfig().getBoolean("SPAWNER_PLACE_ENABLED.IN_END")){
            this.antiSpawnersPlaceEndHandler.enable();
        }
        if (!this.getInstance().getConfig().getBoolean("SPAWNER_PLACE_ENABLED.IN_NETHER")){
            this.antiSpawnersPlaceNetherHandler.enable();
        }
    }
}

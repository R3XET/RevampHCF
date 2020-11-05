package eu.revamp.hcf.scoreboard;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.classes.*;
import eu.revamp.hcf.classes.utils.ArmorClass;
import eu.revamp.hcf.commands.games.KingCommand;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.type.SpawnFaction;
import eu.revamp.hcf.factions.utils.games.ConquestFaction;
import eu.revamp.hcf.factions.utils.games.EventFaction;
import eu.revamp.hcf.factions.utils.games.KothFaction;
import eu.revamp.hcf.games.ConquestType;
import eu.revamp.hcf.handlers.games.EOTWUtilsHandler;
import eu.revamp.hcf.managers.CooldownManager;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.hcf.timers.*;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.hcf.utils.timer.GlobalTimer;
import eu.revamp.hcf.utils.timer.PlayerTimer;
import eu.revamp.hcf.utils.timer.Timer;
import eu.revamp.packages.RevampPackages;
import eu.revamp.spigot.utils.player.PlayerUtils;
import eu.revamp.spigot.utils.time.TimeUtils;
import eu.revamp.system.api.player.PlayerData;
import eu.revamp.system.plugin.RevampSystem;
import net.mineaus.lunar.LunarClientAPI;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Map;

public class ScoreboardHandler extends Handler implements Listener {
    public ScoreboardHandler(RevampHCF plugin) {
        super(plugin);
    }
    @Override
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }

    @Override
    public void disable() {
        for (Player player: PlayerUtils.getOnlinePlayers()){
            PlayerScoreboard board = RevampHCF.getInstance().getHandlerManager().getScoreboardTagEvents().getScoreboardFor(player);
            board.clear();
        }
    }

    public boolean timer(Player player) {
        Collection<Timer> timers = RevampHCF.getInstance().getHandlerManager().getTimerManager().getTimers();
        for (Timer timer : timers) {
            if (timer instanceof PlayerTimer) {
                PlayerTimer playerTimer = (PlayerTimer)timer;
                long remaining3 = playerTimer.getRemaining(player);
                if (remaining3 > 0L) {
                    return true;
                }
            }
            else {
                if (!(timer instanceof GlobalTimer)) {
                    continue;
                }
                GlobalTimer globalTimer = (GlobalTimer)timer;
                long remaining3 = globalTimer.getRemaining();
                if (remaining3 <= 0L) {
                    continue;
                }
                String timerName = globalTimer.getName();
                if (timerName.length() <= 14) {
                    continue;
                }
                timerName = timerName;
            }
        }
        return false;
    }
    public void setupHCFScoreboard() {
        new BukkitRunnable() {
            public void run() {
                for (Player player : PlayerUtils.getOnlinePlayers()) {
                    PlayerData targetProfile = RevampSystem.getINSTANCE().getPlayerManagement().getPlayerData(player.getUniqueId());
                    PlayerScoreboard board = RevampHCF.getInstance().getHandlerManager().getScoreboardTagEvents().getScoreboardFor(player);
                    EOTWUtilsHandler.EotwRunnable eotwRunnable = RevampHCF.getInstance().getHandlerManager().getEotwUtils().getRunnable();
                    EventFaction eventFaction = RevampHCF.getInstance().getHandlerManager().getTimerManager().getGameHandler().getEventFaction();
                    SOTWHandler.SotwRunnable sotwRunnable = RevampHCF.getInstance().getHandlerManager().getSotwHandler().getSotwRunnable();
                    ArmorClass pvpClass = RevampHCF.getInstance().getHandlerManager().getArmorClassManager().getEquippedClass(player);
                    RebootHandler.rebootRunnable rebootRunnable = RevampHCF.getInstance().getHandlerManager().getRebootHandler().getRebootRunnable();
                    SaleOFFHandler.saleoffRunnable saleRunnable = RevampHCF.getInstance().getHandlerManager().getSaleoffHandler().getSaleoffRunnable();
                    GlowstoneHandler.glowstoneRunnable glowstoneRunnable = RevampHCF.getInstance().getHandlerManager().getGlowstoneHandler().getGlowstoneRunnable();
                    KeySaleHandler.keysaleRunnable keysaleRunnable = RevampHCF.getInstance().getHandlerManager().getKeysaleHandler().getKeysaleRunnable();
                    Faction factionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation());
                    HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
                    double tps = Bukkit.spigot().getTPS()[0];
                    DecimalFormat dc = new DecimalFormat("##.##");
                    if (board != null) {
                        board.clear();
                    }
                    assert board != null;
                    if (KingCommand.player != null) {
                        Player player2 = Bukkit.getPlayer(KingCommand.kingName);
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.KILLTHEKING.TITLE"), "");
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.KILLTHEKING.KING"), player2.getName());
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.KILLTHEKING.LOCATION"), player2.getLocation().getBlockX() + ", " + player2.getLocation().getBlockY() + ", " + player2.getLocation().getBlockZ());
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.KILLTHEKING.PRIZE"), KingCommand.kingPrize);
                    }
                if (targetProfile.isFrozen() || targetProfile.isGuiFrozen() || RevampHCF.getInstance().getHandlerManager().getRebootHandler().getRebootRunnable() != null || RevampHCF.getInstance().getHandlerManager().getSotwHandler().getSotwRunnable() != null || RevampHCF.getInstance().getHandlerManager().getKeysaleHandler().getKeysaleRunnable() != null || RevampHCF.getInstance().getHandlerManager().getSaleoffHandler().getSaleoffRunnable() != null || ScoreboardHandler.this.timer(player) || player.getWorld().getEnvironment().equals(World.Environment.NETHER) || targetProfile.isInStaffMode() || CooldownManager.isOnCooldown("GAPPLE_DELAY", player) || CooldownManager.isOnCooldown("APPLE_DELAY", player) || CooldownManager.isOnCooldown("HEADAPPLE_DELAY", player) || CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("MALIGNANTEGG_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("ROTATIONSTICK_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("ULTIMATEPEARL_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("THROWWEB_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("SNOWBALL_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("SECONDCHANCE_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("DISARMERAXE_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("PUMPKINSWAPPER_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("GRAPPLINGHOOK_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("INVISDUST_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("NINJA_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("FREEZEGUN_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("ROCKET_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("BLOCKEGGSHOOTER_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("BLOCKEGGDEFENDER_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("SWITCHEREGG_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("COCAINE_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("COOLDOWNBOW_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("LUMBERJACK_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("PORTABLESTRENGTH_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("PYROBALL_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("RANDOMIZER_DELAY", player) || RevampHCF.getInstance().getHandlerManager().getLogoutHandler().getTeleporting().containsKey(player) || RevampHCF.getInstance().getConfiguration().isKitMap() || eventFaction instanceof KothFaction || factionAt instanceof SpawnFaction || eventFaction instanceof ConquestFaction || RevampHCF.getInstance().getHandlerManager().getArmorClassManager().getEquippedClass(player) != null || eotwRunnable != null) {
                        board.addLine("&7&m-------", "&0&7&m--------", "&7&m-------");
                    }
                    if (targetProfile.isFrozen() || targetProfile.isGuiFrozen()) {
                        for (String message: RevampHCF.getInstance().getScoreboard().getStringList("SCOREBOARD.FROZEN")) {
                            board.add(message, "");
                        }
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.FROZEN-TEAMSPEAK"), "");
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && targetProfile.isInStaffMode()) {
                        board.add( RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.STAFFMODE") + ChatColor.GRAY + " (" + Bukkit.getOnlinePlayers().size() + ")", "");
                        if (RevampSystem.getINSTANCE().getChatManagement().isMuted()) {
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.CHAT-LOCKED"), "");
                        }
                        else if (!RevampSystem.getINSTANCE().getChatManagement().isMuted()) {
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.CHAT-UNLOCKED"), "");
                        }
                        else if (targetProfile.getChatCooldown() != null && !targetProfile.getChatCooldown().hasExpired()
                                && targetProfile.getChatCooldown().getSecondsLeft() <= RevampSystem.getINSTANCE().getChatManagement().getDelay()) {
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.CHAT-SLOWED"), "");
                        }
                        if (targetProfile.isVanished()) {
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.VANISH") + ChatColor.GREEN, " Enabled");
                        }
                        else {
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.VANISH") + ChatColor.RED, " Disabled");
                        }
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.TPS") + dc.format(tps) + ChatColor.GRAY, "");
                        if (!RevampHCF.getInstance().getConfiguration().isKitMap()) {
                            board.addLine("&7&m-------", "&1&7&m--------", "&7&m-------");
                        }
                    }
                    if (LunarClientAPI.getInstance().isAuthenticated(player)){
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.LUNAR_CLIENT"), "");
                    }
                    if (!targetProfile.isInStaffMode() && RevampHCF.getInstance().getConfiguration().isKitMap()) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.STATS"), "");
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.KILLS"), String.valueOf(data.getKills()));
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.DEATHS"), String.valueOf(data.getDeaths()));
                        if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && factionAt instanceof SpawnFaction && player.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.BALANCE"), String.valueOf(data.getBalance()));
                        }
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation()).equals(RevampHCF.getInstance().getFactionManager().getPlayerFaction(player.getUniqueId()))) {
                        board.addLine("&7&m-------", "&1&7&m--------", "&7&m-------");
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.TEAM-INFO"), "");
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.DTR"), String.valueOf(RevampHCF.getInstance().getFactionManager().getPlayerFaction(player.getUniqueId()).getRegenStatus().getSymbol()) + RevampHCF.getInstance().getFactionManager().getPlayerFaction(player.getUniqueId()).getDtrColour() + RevampHCF.getInstance().getFactionManager().getPlayerFaction(player.getUniqueId()).getDeathsUntilRaidable());
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.BALANCE"), String.valueOf(RevampHCF.getInstance().getFactionManager().getPlayerFaction(player.getUniqueId()).getBalance()));
                        if (RevampHCF.getInstance().getFactionManager().getPlayerFaction(player.getUniqueId()).lives > 0) {
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.LIVES"), String.valueOf(RevampHCF.getInstance().getFactionManager().getPlayerFaction(player.getUniqueId()).getLives()));
                        }
                        if (RevampHCF.getInstance().getFactionManager().getPlayerFaction(player.getUniqueId()).getRemainingRegenerationTime() > 0L) {
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.REGEN"), String.valueOf(TimeUtils.getRemaining(RevampHCF.getInstance().getFactionManager().getPlayerFaction(player.getUniqueId()).getRemainingRegenerationTime(), false)));
                        }
                        if (!RevampHCF.getInstance().getConfiguration().isKitMap()) {
                            board.addLine("&7&m-------", "&2&7&m--------", "&7&m-------");
                        }
                    }
                    if (RevampHCF.getInstance().getHandlerManager().getSotwHandler().getSotwRunnable() != null && !RevampHCF.getInstance().getHandlerManager().getSotwHandler().isSotwEnabled(player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.SOTW-PROTECTION"), TimeUtils.getRemaining(sotwRunnable.getRemaining(), true));
                    }
                    if (RevampHCF.getInstance().getHandlerManager().getSotwHandler().getSotwRunnable() != null && RevampHCF.getInstance().getHandlerManager().getSotwHandler().isSotwEnabled(player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.SOTW-ENABLED"), TimeUtils.getRemaining(sotwRunnable.getRemaining(), true));
                    }
                    if (RevampHCF.getInstance().getHandlerManager().getRebootHandler().getRebootRunnable() != null) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.REBOOT"), TimeUtils.getRemaining(rebootRunnable.getRemaining(), true));
                    }
                    if (RevampHCF.getInstance().getHandlerManager().getKeysaleHandler().getKeysaleRunnable() != null) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.KEYSALE"), TimeUtils.getRemaining(keysaleRunnable.getRemaining(), true));
                    }
                    if ((!targetProfile.isFrozen() || !targetProfile.isGuiFrozen()) && RevampHCF.getInstance().getHandlerManager().getSaleoffHandler().getSaleoffRunnable() != null) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.SALE"), TimeUtils.getRemaining(saleRunnable.getRemaining(), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && CooldownManager.isOnCooldown("GAPPLE_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.GAPPLE"), TimeUtils.getRemaining(CooldownManager.getCooldownMillis("GAPPLE_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampHCF.getInstance().getHandlerManager().getGlowstoneHandler().getGlowstoneRunnable() != null && player.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.GLOWSTONE"), TimeUtils.getRemaining(glowstoneRunnable.getRemaining(), true));
                    }
                    /*
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && player.getWorld().getEnvironment().equals(World.Environment.THE_END)) {
                        if (RevampHCF.getInstance().getUtilities().getConfigurationSection("warps") != null && RevampHCF.getInstance().getUtilities().getConfigurationSection("warps").contains("crates")){
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.END_EXIT"), (int) RevampHCF.getInstance().getUtilities().getDouble("warps.crates.x") + ", " + (int) RevampHCF.getInstance().getUtilities().getDouble("warps.crates.z"));
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.END_EXIT_DISTANCE"), String.valueOf(player.getLocation().distance(WorldUtils.destringifyLocation(RevampHCF.getInstance().getUtilities().getString("warps.crates")))));
                        }
                    }
                    */
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && !RevampHCF.getInstance().getConfiguration().isKitMap() && factionAt instanceof SpawnFaction && player.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.YOUR_BALANCE"), String.valueOf(data.getBalance()));
                        if (RevampHCF.getInstance().getUtilities().getConfigurationSection("warps") != null && RevampHCF.getInstance().getUtilities().getConfigurationSection("warps").contains("crates")){
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.CRATES_LOCATION"), (int) RevampHCF.getInstance().getUtilities().getDouble("warps.crates.x") + ", " + (int) RevampHCF.getInstance().getUtilities().getDouble("warps.crates.z"));
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.CRATES_DISTANCE"), String.valueOf((int) player.getLocation().distance(Utils.getLocation("crates"))));
                        }
                        if (RevampHCF.getInstance().getUtilities().getConfigurationSection("warps") != null && RevampHCF.getInstance().getUtilities().getConfigurationSection("warps").contains("cows")){
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.COW_LOCATION"), (int) RevampHCF.getInstance().getUtilities().getDouble("warps.cows.x") + ", " + (int) RevampHCF.getInstance().getUtilities().getDouble("warps.cows.z"));
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.COW_DISTANCE"), String.valueOf((int) player.getLocation().distance(Utils.getLocation("cows"))));
                        }
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.ENDERPEARL"), TimeUtils.getRemaining(CooldownManager.getCooldownMillis("ENDERPEARL_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && CooldownManager.isOnCooldown("APPLE_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.CRAPPLE"), TimeUtils.getRemaining(CooldownManager.getCooldownMillis("APPLE_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && CooldownManager.isOnCooldown("HEADAPPLE_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.GOLDEN-HEAD"), TimeUtils.getRemaining(CooldownManager.getCooldownMillis("HEADAPPLE_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampHCF.getInstance().getHandlerManager().getLogoutHandler().getTeleporting().containsKey(player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.LOGOUT"), TimeUtils.getRemaining(RevampHCF.getInstance().getHandlerManager().getLogoutHandler().getMillisecondsLeft(player), true));
                    }
                    /*
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("MALIGNANTEGG_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.MALIGNANTEGG"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("MALIGNANTEGG_DELAY", player), true));
                    }*/
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("THROWWEB_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.COBWEB"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("THROWWEB_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("GRAPPLINGHOOK_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.GRAPPLINGHOOK"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("GRAPPLINGHOOK_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("DISARMERAXE_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.DISARMER-AXE"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("DISARMERAXE_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("SNOWBALL_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.SNOWBALL"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("SNOWBALL_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("NINJA_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.NINJA"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("NINJA_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("INVISDUST_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.INVISIBILITY"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("INVISDUST_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("PUMPKINSWAPPER_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.PUMPKIN-SWAPPER"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("PUMPKINSWAPPER_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("FREEZEGUN_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.FREEZEGUN"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("FREEZEGUN_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("SWITCHEREGG_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.SWITCHEREGG"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("SWITCHEREGG_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("ROCKET_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.ROCKET"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("ROCKET_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("BLOCKEGGSHOOTER_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.BLOCKEGG"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("BLOCKEGGSHOOTER_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("BLOCKEGGDEFENDER_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.BLOCKEGG"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("BLOCKEGGDEFENDER_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("PYROBALL_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.PYRO"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("PYROBALL_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("SECONDCHANCE_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.SECOND_CHANCE"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("SECONDCHANCE_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("COCAINE_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.COCAINE"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("COCAINE_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("COOLDOWNBOW_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.COOLDOWNBOW"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("COOLDOWNBOW_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("LUMBERJACK_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.LUMBERJACK"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("LUMBERJACK_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("PORTABLESTRENGTH_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.PORTABLESTRENGTH"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("PORTABLESTRENGTH_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("RANDOMIZER_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.RANDOMIZER"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("RANDOMIZER_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("ROTATIONSTICK_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.ROTATIONSTICK"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("ROTATIONSTICK_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("ULTIMATEPEARL_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.ULTIMATEPEARL"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("ULTIMATEPEARL_DELAY", player), true));
                    }



                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("GUARDIAN_ANGEL_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.GUARDIAN_ANGEL"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("GUARDIAN_ANGEL_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("GUARDIAN_ANGEL_USE_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.GUARDIAN_ANGEL_HEALTH"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("GUARDIAN_ANGEL_USE_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("ICARO_FEATHER_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.ICARO_FEATHER"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("ICARO_FEATHER_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("ICARO_FEATHER_USE_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.ICARO_FEATHER_FALL"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("ICARO_FEATHER_USE_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("BEDBOMB_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.BEDBOMB"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("BEDBOMB_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("BEDBOMB_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.BEDBOMB"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("BEDBOMB_DELAY", player), true));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampPackages.getInstance().getCooldownManager().isOnCooldown("POCKET_BARD_DELAY", player)) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.POCKET_BARD"), TimeUtils.getRemaining(RevampPackages.getInstance().getCooldownManager().getCooldownMillis("POCKET_BARD_DELAY", player), true));
                    }


                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && eotwRunnable != null) {
                        long remaining = eotwRunnable.getTimeUntilStarting();
                        if (remaining > 0L) {
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.EOTW-STARTS"), TimeUtils.getRemaining(remaining, true));
                        }
                        else if ((remaining = eotwRunnable.getTimeUntilCappable()) > 0L) {
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.EOTW-CAP"), TimeUtils.getRemaining(remaining, true));
                        }
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && pvpClass instanceof Bard) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.BARD"), "");
                        Bard bardClass = (Bard)pvpClass;
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.BARD-ENERGY"), Utils.handleBardFormat(bardClass.getEnergyMillis(player), true, true));
                        long remaining2 = bardClass.getRemainingBuffDelay(player);
                        if (remaining2 > 0L) {
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.BARD-EFFECT"), TimeUtils.getRemaining(remaining2, true));
                        }
                    }
                    else if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && pvpClass instanceof Archer) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.ARCHER"), "");
                        if (CooldownManager.isOnCooldown("ARCHER_SPEED_DELAY", player)) {
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.ARCHER-SPEED"), TimeUtils.getRemaining(CooldownManager.getCooldownMillis("ARCHER_SPEED_DELAY", player), true));
                        }
                        if (CooldownManager.isOnCooldown("ARCHER_JUMP_DELAY", player)) {
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.ARCHER-JUMP"), TimeUtils.getRemaining(CooldownManager.getCooldownMillis("ARCHER_JUMP_DELAY", player), true));
                        }
                    }
                    else if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && pvpClass instanceof Rogue) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.ROGUE"), "");
                        if (CooldownManager.isOnCooldown("ROGUE_SPEED_DELAY", player)) {
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.ROGUE-SPEED"), TimeUtils.getRemaining(CooldownManager.getCooldownMillis("ROGUE_SPEED_DELAY", player), true));
                        }
                        if (CooldownManager.isOnCooldown("ROGUE_JUMP_DELAY", player)) {
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.ROGUE-JUMP"), TimeUtils.getRemaining(CooldownManager.getCooldownMillis("ROGUE_JUMP_DELAY", player), true));
                        }
                    }
                    else if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && pvpClass instanceof Ranger) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.RANGER"), "");
                        if (CooldownManager.isOnCooldown("RANGER_STRENGTH_DELAY", player)) {
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.RANGER-STRENGTH"), TimeUtils.getRemaining(CooldownManager.getCooldownMillis("RANGER_STRENGTH_DELAY", player), true));
                        }
                        if (CooldownManager.isOnCooldown("RANGER_FIRE_DELAY", player)) {
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.RANGER-FIRE"), TimeUtils.getRemaining(CooldownManager.getCooldownMillis("RANGER_FIRE_DELAY", player), true));
                        }
                        if (CooldownManager.isOnCooldown("RANGER_QUARTZ_DELAY", player)) {
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.RANGER-DEMON"), TimeUtils.getRemaining(CooldownManager.getCooldownMillis("RANGER_QUARTZ_DELAY", player), true));
                        }
                    }
                    else if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && pvpClass instanceof Miner) {
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.MINER"), "");
                        board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.MINER-DIAMONDS"), String.valueOf(player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE)));
                    }
                    if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && RevampHCF.getInstance().getHandlerManager().getTimerManager().getGameHandler() != null) {
                        if (eventFaction instanceof KothFaction) {
                            if (eventFaction.getName().equalsIgnoreCase("eotw")) {
                                board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.EOTW"), "");
                                board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.EOTW-KOTH") + TimeUtils.getRemaining(RevampHCF.getInstance().getHandlerManager().getTimerManager().getGameHandler().getRemaining(), true), "");
                            } else {
                                if (Long.parseLong(TimeUtils.getRemaining(RevampHCF.getInstance().getHandlerManager().getTimerManager().getGameHandler().getRemaining(), true)) >= 0L) {
                                    board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.KOTH").replace("%koth%", eventFaction.getScoreboardName()), TimeUtils.getRemaining(RevampHCF.getInstance().getHandlerManager().getTimerManager().getGameHandler().getRemaining(), true));
                                }
                            }
                        }
                        else if (eventFaction instanceof ConquestFaction) {
                            ConquestFaction conquestFaction = (ConquestFaction)eventFaction;
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.CONQUEST"), "");
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.CONQUEST-RED") + conquestFaction.getRed().getScoreboardRemaining() + " " + RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.CONQUEST-YELLOW")  + conquestFaction.getYellow().getScoreboardRemaining(), "");
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.CONQUEST-GREEN") + conquestFaction.getGreen().getScoreboardRemaining() + " " + RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.CONQUEST-BLUE")  + conquestFaction.getBlue().getScoreboardRemaining(), "");
                            ConquestType conquestTracker = (ConquestType)conquestFaction.getEventType().getEventTracker();
                            int count = 0;
                            for (Map.Entry<PlayerFaction, Integer> entry : conquestTracker.getFactionPointsMap().entrySet()) {
                                String factionName = entry.getKey().getName();
                                if (factionName.length() > 14) {
                                    factionName = factionName.substring(0, 14);
                                }
                                board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.CONQUEST-POINTS") .replace("%fac%", factionName), String.valueOf(entry.getValue()));
                                if (++count == 3) break;
                            }
                        }
                    }
                    Collection<Timer> timers = RevampHCF.getInstance().getHandlerManager().getTimerManager().getTimers();
                    for (Timer timer : timers) {
                        if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && timer instanceof PlayerTimer) {
                            PlayerTimer playerTimer = (PlayerTimer)timer;
                            long remaining7 = playerTimer.getRemaining(player);
                            if (remaining7 <= 0L) {
                                continue;
                            }
                            String timerName = playerTimer.getName();
                            if (timerName.length() > 14) {
                                timerName = timerName;
                            }
                            if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && playerTimer instanceof ArcherHandler) {
                                board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.ARCHER-MARK"), TimeUtils.getRemaining(remaining7, true));
                            }
                            if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && playerTimer instanceof ClassWarmupHandler) {
                                board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.CLASS-WARMUP"), TimeUtils.getRemaining(remaining7, true));
                            }
                            if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && playerTimer instanceof HomeHandler) {
                                board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.HOME"), TimeUtils.getRemaining(remaining7, true));
                            }
                            if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && playerTimer instanceof SpawnTagHandler) {
                                board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.COMBAT-TAG"), TimeUtils.getRemaining(remaining7, true));
                            }
                            if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && playerTimer instanceof PvPTimerHandler) {
                                board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.PVP-TIMER"), TimeUtils.getRemaining(remaining7, true));
                            }
                            if ((!targetProfile.isFrozen() || targetProfile.isGuiFrozen()) && !(playerTimer instanceof StuckHandler)) {
                                continue;
                            }
                            board.add(RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.STUCK"), TimeUtils.getRemaining(remaining7, true));
                        }
                    }
                    if (targetProfile.isFrozen() || targetProfile.isGuiFrozen() || RevampHCF.getInstance().getHandlerManager().getRebootHandler().getRebootRunnable() != null || RevampHCF.getInstance().getHandlerManager().getSotwHandler().getSotwRunnable() != null || RevampHCF.getInstance().getHandlerManager().getKeysaleHandler().getKeysaleRunnable() != null || RevampHCF.getInstance().getHandlerManager().getSaleoffHandler().getSaleoffRunnable() != null || ScoreboardHandler.this.timer(player) || player.getWorld().getEnvironment().equals(World.Environment.NETHER) || CooldownManager.isOnCooldown("GAPPLE_DELAY", player) || CooldownManager.isOnCooldown("APPLE_DELAY", player) || CooldownManager.isOnCooldown("ENDERPEARL_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("MALIGNANTEGG_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("ROTATIONSTICK_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("ULTIMATEPEARL_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("THROWWEB_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("SNOWBALL_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("SECONDCHANCE_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("DISARMERAXE_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("PUMPKINSWAPPER_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("GRAPPLINGHOOK_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("INVISDUST_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("NINJA_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("FREEZEGUN_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("ROCKET_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("BLOCKEGGSHOOTER_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("BLOCKEGGDEFENDER_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("SWITCHEREGG_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("COCAINE_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("COOLDOWNBOW_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("LUMBERJACK_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("PORTABLESTRENGTH_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("PYROBALL_DELAY", player) || RevampPackages.getInstance().getCooldownManager().isOnCooldown("RANDOMIZER_DELAY", player) || RevampHCF.getInstance().getHandlerManager().getLogoutHandler().getTeleporting().containsKey(player) || eventFaction instanceof KothFaction || factionAt instanceof SpawnFaction || RevampHCF.getInstance().getConfiguration().isKitMap() || eventFaction instanceof ConquestFaction || RevampHCF.getInstance().getHandlerManager().getArmorClassManager().getEquippedClass(player) != null || eotwRunnable != null) {
                        board.addLine("&7&m-------", "&3&7&m--------", "&7&m-------");
                    }
                    board.update(player);
                }
            }
        }.runTaskTimerAsynchronously(this.getInstance(), 40L, 2L /*RevampHCF.getInstance().getScoreboard().getLong("UPDATE_TICKS")*/);
    }
}

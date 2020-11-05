package eu.revamp.hcf.deathban;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.deathban.commands.DeathBanCommand;
import eu.revamp.hcf.file.ConfigFile;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.date.DateUtils;
import eu.revamp.spigot.utils.serialize.BukkitSerilization;
import eu.revamp.spigot.utils.world.WorldUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class DeathBanHandler extends Handler implements Listener {
    @Getter private File deathbansFolder;
    private File inventoriesFolder;
    @Getter private File livesFolder;
    private final List<UUID> playerConfirmation;

    public DeathBanHandler(RevampHCF plugin) {
        super(plugin);
        this.playerConfirmation = new ArrayList<>();
    }
    
    @Override
    public void enable() {
        File deathbanFolder = new File(RevampHCF.getInstance().getDataFolder(), "deathban");
        this.deathbansFolder = new File(deathbanFolder, "deathbans");
        this.inventoriesFolder = new File(deathbanFolder, "inventories");
        this.livesFolder = new File(deathbanFolder, "lives");
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
        this.getInstance().getServer().getPluginManager().registerEvents(new DeathBanCommand(RevampHCF.getInstance()), this.getInstance());
    }
    
    @Override
    public void disable() {
        this.playerConfirmation.clear();
    }
    
    public void banPlayer(Player player, PlayerDeathEvent event) {
        File file = new File(this.deathbansFolder, player.getUniqueId().toString() + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        Location location = event.getEntity().getLocation();
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        HCFPlayerData HCFPlayerData = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        ConfigFile config = RevampHCF.getInstance().getConfig();
        AtomicBoolean success = new AtomicBoolean(false);
        config.getConfigurationSection("DEATHBAN.BAN_TIMES").getKeys(false).forEach(s -> {
            if (HCFPlayerData.getRank().equalsIgnoreCase("DEATHBAN.BAN_TIMES." + s + ".RANK_NAME")){
                configuration.set("ban_until", System.currentTimeMillis() + DateUtils.parseTime("DEATHBAN.BAN_TIMES." + s + ".TIME"));
                success.set(true);
            }
        });
        if (!success.get()){
            configuration.set("ban_until", System.currentTimeMillis() + DateUtils.parseTime("DEATHBAN.DEFAULT_BAN_TIME"));
        }

        /*
        if (HCFPlayerData.getRank().equalsIgnoreCase("Default")) {
            configuration.set("ban_until", System.currentTimeMillis() + 3600000L);
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Hydra")) {
            configuration.set("ban_until", System.currentTimeMillis() + 3000000L);
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Kepler")) {
            configuration.set("ban_until", System.currentTimeMillis() + 2700000L);
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Rainer")) {
            configuration.set("ban_until", System.currentTimeMillis() + 2100000L);
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Osiris")) {
            configuration.set("ban_until", System.currentTimeMillis() + 1200000L);
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Ultra")) {
            configuration.set("ban_until", System.currentTimeMillis() + 600000L);
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Master")) {
            configuration.set("ban_until", System.currentTimeMillis() + 300000L);
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Ultimate")) {
            configuration.set("ban_until", System.currentTimeMillis());
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Revamp")) {
            configuration.set("ban_until", System.currentTimeMillis());
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Youtuber")) {
            configuration.set("ban_until", System.currentTimeMillis() + 600000L);
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Famous")) {
            configuration.set("ban_until", System.currentTimeMillis() + 300000L);
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Partner")) {
            configuration.set("ban_until", System.currentTimeMillis());
        }*/

        configuration.set("death_message", event.getDeathMessage());
        configuration.set("coords", WorldUtils.getWorldName(location) + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
        try {
            configuration.save(file);
        }
        catch (IOException ex2) {
            ex2.printStackTrace();
        }
        File invFile = new File(this.inventoriesFolder, player.getUniqueId() + ".yml");
        if (!invFile.exists()) {
            try {
                invFile.createNewFile();
            }
            catch (IOException ex3) {
                ex3.printStackTrace();
            }
        }
        YamlConfiguration configurationInv = YamlConfiguration.loadConfiguration(invFile);
        configurationInv.set("inventory", BukkitSerilization.itemStackArrayToBase64(player.getInventory().getContents()));
        configurationInv.set("armor", BukkitSerilization.itemStackArrayToBase64(player.getInventory().getArmorContents()));
        configurationInv.set("used", false);
        try {
            configurationInv.save(invFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void banVillager(Player player, Villager villager, EntityDeathEvent event) {
        File file = new File(this.deathbansFolder, player.getUniqueId().toString() + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        Location location = event.getEntity().getLocation();
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        HCFPlayerData HCFPlayerData = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        /*
        if (HCFPlayerData.getRank().equalsIgnoreCase("Default")) {
            configuration.set("ban_until", System.currentTimeMillis() + 4500000L);
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Hydra")) {
            configuration.set("ban_until", System.currentTimeMillis() + 3600000L);
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Kepler")) {
            configuration.set("ban_until", System.currentTimeMillis() + 3600000L);
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Rainer")) {
            configuration.set("ban_until", System.currentTimeMillis() + 3000000L);
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Osiris")) {
            configuration.set("ban_until", System.currentTimeMillis() + 2400000L);
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Ultra")) {
            configuration.set("ban_until", System.currentTimeMillis() + 1800000L);
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Master")) {
            configuration.set("ban_until", System.currentTimeMillis() + 1200000L);
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Ultimate")) {
            configuration.set("ban_until", System.currentTimeMillis() + 600000L);
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Venun")) {
            configuration.set("ban_until", System.currentTimeMillis() + 300000L);
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Youtuber")) {
            configuration.set("ban_until", System.currentTimeMillis() + 600000L);
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Famous")) {
            configuration.set("ban_until", System.currentTimeMillis() + 300000L);
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Partner")) {
            configuration.set("ban_until", System.currentTimeMillis());
        }*/

        ConfigFile config = RevampHCF.getInstance().getConfig();
        AtomicBoolean success = new AtomicBoolean(false);
        config.getConfigurationSection("DEATHBAN.BAN_TIMES").getKeys(false).forEach(s -> {
            if (HCFPlayerData.getRank().equalsIgnoreCase("DEATHBAN.BAN_TIMES." + s + ".RANK_NAME")){
                configuration.set("ban_until", System.currentTimeMillis() + DateUtils.parseTime("DEATHBAN.BAN_TIMES." + s + ".TIME"));
                success.set(true);
            }
        });
        if (!success.get()){
            configuration.set("ban_until", System.currentTimeMillis() + DateUtils.parseTime("DEATHBAN.DEFAULT_BAN_TIME"));
        }


        configuration.set("death_message", "CombaatLogger ");
        configuration.set("coords", WorldUtils.getWorldName(location) + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
        try {
            configuration.save(file);
        }
        catch (IOException ex2) {
            ex2.printStackTrace();
        }
        File invFile = new File(this.inventoriesFolder, player.getUniqueId() + ".yml");
        if (!invFile.exists()) {
            try {
                invFile.createNewFile();
            }
            catch (IOException ex3) {
                ex3.printStackTrace();
            }
        }
        ItemStack[] contents = (ItemStack[])villager.getMetadata("Contents").get(0).value();
        ItemStack[] armor = (ItemStack[])villager.getMetadata("Armor").get(0).value();
        YamlConfiguration configurationInv = YamlConfiguration.loadConfiguration(invFile);
        configurationInv.set("inventory", BukkitSerilization.itemStackArrayToBase64(contents));
        configurationInv.set("armor", BukkitSerilization.itemStackArrayToBase64(armor));
        configurationInv.set("used", false);
        try {
            configurationInv.save(invFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void kickPlayer(Player player, PlayerDeathEvent event) {
        HCFPlayerData HCFPlayerData = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        ConfigFile config = RevampHCF.getInstance().getConfig();
        AtomicBoolean success = new AtomicBoolean(false);
        config.getConfigurationSection("DEATHBAN.BAN_TIMES").getKeys(false).forEach(s -> {
            if (HCFPlayerData.getRank().equalsIgnoreCase("DEATHBAN.BAN_TIMES." + s + ".RANK_NAME")){
                long time = DateUtils.parseTime("DEATHBAN.BAN_TIMES." + s + ".TIME");
                player.kickPlayer(CC.translate("&cYou are deathbanned for another &l" + DurationFormatUtils.formatDurationWords(time/* * 30L * 1000L*/, true, true)) + "\n " + "\n" + event.getDeathMessage());
                success.set(true);
            }
        });
        if (!success.get()){
            long time = DateUtils.parseTime("DEATHBAN.DEFAULT_BAN_TIME");
            player.kickPlayer(CC.translate("&cYou are deathbanned for another &l" + DurationFormatUtils.formatDurationWords(time/* * 30L * 1000L*/, true, true)) + "\n " + "\n" + event.getDeathMessage());
        }

        /*
        if (HCFPlayerData.getRank().equalsIgnoreCase("Default")) {
            int banTimeUser = 60;
            player.kickPlayer(CC.translate("&cYou are deathbanned for another &l" + DurationFormatUtils.formatDurationWords(banTimeUser * 60L * 1000L, true, true)) + "\n " + "\n" + event.getDeathMessage());
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Hydra")) {
            int banTimeHydra = 50;
            player.kickPlayer(CC.translate("&cYou are deathbanned for another &l" + DurationFormatUtils.formatDurationWords(banTimeHydra * 60L * 1000L, true, true)) + "\n " + "\n" + event.getDeathMessage());
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Kepler")) {
            int banTimeKepler = 45;
            player.kickPlayer(CC.translate("&cYou are deathbanned for another &l" + DurationFormatUtils.formatDurationWords(banTimeKepler * 60L * 1000L, true, true)) + "\n " + "\n" + event.getDeathMessage());
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Rainer")) {
            int banTimeRainer = 35;
            player.kickPlayer(CC.translate("&cYou are deathbanned for another &l" + DurationFormatUtils.formatDurationWords(banTimeRainer * 60L * 1000L, true, true)) + "\n " + "\n" + event.getDeathMessage());
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Osiris")) {
            int banTimeOsiris = 20;
            player.kickPlayer(CC.translate("&cYou are deathbanned for another &l" + DurationFormatUtils.formatDurationWords(banTimeOsiris * 60L * 1000L, true, true)) + "\n " + "\n" + event.getDeathMessage());
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Ultra")) {
            int banTimeUltra = 10;
            player.kickPlayer(CC.translate("&cYou are deathbanned for another &l" + DurationFormatUtils.formatDurationWords(banTimeUltra * 60L * 1000L, true, true)) + "\n " + "\n" + event.getDeathMessage());
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Master")) {
            int banTimeMaster = 5;
            player.kickPlayer(CC.translate("&cYou are deathbanned for another &l" + DurationFormatUtils.formatDurationWords(banTimeMaster * 60L * 1000L, true, true)) + "\n " + "\n" + event.getDeathMessage());
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Ultimate")) {
            int banTimeUltimate = 0;
            player.kickPlayer(CC.translate("&a&lYou can join now because you have Ultimate Rank &l" + DurationFormatUtils.formatDurationWords(banTimeUltimate * 60L * 1000L, true, true)) + "\n " + "\n" + event.getDeathMessage());
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Venun")) {
            int banTimeVenun = 0;
            player.kickPlayer(CC.translate("&a&lYou can join now because you have Venun Rank &l" + DurationFormatUtils.formatDurationWords(banTimeVenun * 60L * 1000L, true, true)) + "\n " + "\n" + event.getDeathMessage());
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Youtuber")) {
            int banTimeYoutuber = 10;
            player.kickPlayer(CC.translate("&cYou are deathbanned for another &l" + DurationFormatUtils.formatDurationWords(banTimeYoutuber * 60L * 1000L, true, true)) + "\n " + "\n" + event.getDeathMessage());
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Famous")) {
            int banTimeFamous = 5;
            player.kickPlayer(CC.translate("&cYou are deathbanned for another &l" + DurationFormatUtils.formatDurationWords(banTimeFamous * 60L * 1000L, true, true)) + "\n " + "\n" + event.getDeathMessage());
        }
        else if (HCFPlayerData.getRank().equalsIgnoreCase("Partner")) {
            int banTimePartner = 0;
            player.kickPlayer(CC.translate("&a&lYou can join now because you have Partner Rank &l" + DurationFormatUtils.formatDurationWords(banTimePartner * 60L * 1000L, true, true)) + "\n " + "\n" + event.getDeathMessage());
        }*/
    }
    
    public int getLives(Player player) {
        File file = new File(this.livesFolder, player.getUniqueId() + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        return configuration.getInt("lives");
    }
    
    public void setLives(Player player, int amount) {
        File file = new File(this.livesFolder, player.getUniqueId() + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        int lives = configuration.getInt("lives");
        configuration.set("lives", lives - Math.abs(amount));
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        this.banPlayer(player, event);
        new BukkitRunnable() {
            public void run() {
                DeathBanHandler.this.kickPlayer(player, event);
            }
        }.runTaskLater(RevampHCF.getInstance(), 10L);
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        File file = new File(this.livesFolder, player.getUniqueId() + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            configuration.set("lives", RevampHCF.getInstance().getConfiguration().getStartLives());
            try {
                configuration.save(file);
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
    
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        File file = new File(this.deathbansFolder, player.getUniqueId() + ".yml");
        if (file.exists()) {
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            long banTime = configuration.getLong("ban_until");
            String deathMsg = configuration.getString("death_message");
            if (System.currentTimeMillis() < banTime) {
                if (this.getLives(player) <= 0) {
                    event.setKickMessage(CC.translate("&cYou are deathbanned for another &l" + DurationFormatUtils.formatDurationWords(banTime - System.currentTimeMillis(), true, true)) + "\n" + "\n" + deathMsg);
                    event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                }
                else if (RevampHCF.getInstance().getHandlerManager().getEotwUtils().getRunnable() != null) {
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, CC.translate("&4&lEOTW &cis running, therefore you cannot use lives!"));
                }
                else if (this.playerConfirmation.contains(player.getUniqueId())) {
                    File newFile = new File(this.livesFolder, player.getUniqueId() + ".yml");
                    YamlConfiguration newConfiguration = YamlConfiguration.loadConfiguration(newFile);
                    int lives = newConfiguration.getInt("lives");
                    newConfiguration.set("lives", lives - 1);
                    try {
                        newConfiguration.save(newFile);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    file.delete();
                    this.playerConfirmation.remove(player.getUniqueId());
                }
                else {
                    event.setKickMessage(CC.translate("&cYou are deathbanned for another &l" + DurationFormatUtils.formatDurationWords(banTime - System.currentTimeMillis(), true, true)) + "\n" + "\n" + "You have " + this.getLives(player) + ", Please join again to use a Live");
                    event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                    this.playerConfirmation.add(player.getUniqueId());
                }
            }
            else {
                file.delete();
                this.playerConfirmation.remove(player.getUniqueId());
            }
        }
    }

    @Getter @Setter
    public static class DeathBanTime {
        private String group;
        private int time;
    }
}

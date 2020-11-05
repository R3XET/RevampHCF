package eu.revamp.hcf.handlers.disguise;
/*
import de.robingrether.idisguise.api.DisguiseAPI;
import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.iDisguise;
import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.file.ConfigFile;
import eu.revamp.hcf.file.ConfigFileOld;
import eu.revamp.hcf.utils.Handler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class ClickModule extends Handler implements Listener
{
    private HashMap<UUID, Long> cooldown;

    public ClickModule(RevampHCF plugin) {
        super(plugin);
        this.cooldown = new HashMap<>();
    }

    @Override
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null) return;
        Player player = (Player)e.getWhoClicked();
        UUID uuid = player.getUniqueId();
        ItemStack item = e.getCurrentItem();
        String itemName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        String openInv = ChatColor.stripColor(player.getOpenInventory().getTitle());
        int cooldownTime = RevampHCF.getInstance().getConfig().getInt("DISGUISE_SETTINGS.COOLDOWN");
        if (openInv.equalsIgnoreCase("disguises")) {
            e.setCancelled(true);
            if (this.cooldown.containsKey(uuid)) {
                float time = (float)((System.currentTimeMillis() - this.cooldown.get(uuid)) / 1000L);
                if (time < cooldownTime) {
                    e.setCancelled(true);
                    player.sendMessage(RevampHCF.getInstance().getConfig().getString("DISGUISE_SETTINGS.COOLDOWN_MESSAGE").replaceAll("%time%", String.valueOf(Math.abs(time - cooldownTime))));
                }
                else {
                    this.cooldown.put(uuid, System.currentTimeMillis());
                    this.disguise(player, RevampHCF.getInstance().getConfig(), item, itemName);
                }
            }
            else {
                this.cooldown.put(uuid, System.currentTimeMillis());
                this.disguise(player, RevampHCF.getInstance().getConfig(), item, itemName);
            }
        }
    }

    private void disguise(Player player, ConfigFile configFile, ItemStack item, String itemName) {
        for (String str : configFile.getConfigurationSection("disguises").getKeys(false)) {
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase(configFile.getString("disguises." + str + ".name"))) {
                if (player.hasPermission(configFile.getString("disguises." + str + ".perm"))) {
                    DisguiseAPI disguiseAPI = iDisguise.getInstance().getAPI();
                    if (disguiseAPI.isDisguised(player)) {
                        disguiseAPI.undisguise((OfflinePlayer)player);
                    }
                    disguiseAPI.disguise((OfflinePlayer)player, Disguise.fromString(itemName));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(RevampHCF.getInstance(), () -> {
                        if (disguiseAPI.isDisguised(player)) {
                            disguiseAPI.undisguise((OfflinePlayer)player);
                        }
                    }, configFile.getInt("DISGUISE_SETTINGS.BLOW-TIME") * 20);
                    player.closeInventory();
                    player.sendMessage(configFile.getString("DISGUISE_SETTINGS.DISGUISE_MESSAGE").replace("%disguise%", itemName));
                }
                else {
                    player.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
                }
            }
        }
    }
}
*/
package eu.revamp.hcf.commands.custom;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.file.ConfigFile;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.spigot.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DisguiseCommand extends Handler implements Listener {
    public DisguiseCommand(RevampHCF instance) {
        super(instance);
    }

    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase("/disguise") || e.getMessage().equalsIgnoreCase("/d")) {
            e.setCancelled(true);
            Player player = e.getPlayer();
            ConfigFile configFile = RevampHCF.getInstance().getConfig();
            List<ItemStack> unlocked = new ArrayList<>();
            List<ItemStack> locked = new ArrayList<>();
            for (String str : configFile.getConfigurationSection("disguises").getKeys(false)) {
                String path = "disguises." + str + ".";
                if (player.hasPermission(configFile.getString(path + "perm"))) {
                    unlocked.add(new ItemBuilder(Material.SKULL_ITEM).setSkullOwner(configFile.getString(path + "owner")).setName(configFile.getString(path + "name")).setLore(configFile.getString(path + "lore") + "\n&aUnlocked").toItemStack());
                } else {
                    locked.add(new ItemBuilder(Material.SKULL_ITEM).setSkullOwner(configFile.getString(path + "owner")).setName(configFile.getString(path + "name")).setLore(configFile.getString(path + "lore") + "\n&4Locked").toItemStack());
                }
            }
            Inventory disguises = Bukkit.createInventory(null, (unlocked.size() + locked.size() + 8) / 9 * 9, "Disguises");
            if (!unlocked.isEmpty()) {
                for (ItemStack item : unlocked) {
                    disguises.addItem(item);
                }
            }
            if (!locked.isEmpty()) {
                for (ItemStack item : locked) {
                    disguises.addItem(item);
                }
            }
            player.openInventory(disguises);
        }
    }
}

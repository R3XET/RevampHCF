package eu.revamp.hcf.classes.utils;

import eu.revamp.hcf.RevampHCF;
import lombok.Getter;
import eu.revamp.hcf.classes.utils.events.ArmorClassEquipEvent;
import eu.revamp.hcf.classes.utils.events.ArmorClassUnequipEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.Bukkit;
import eu.revamp.hcf.classes.Ranger;
import eu.revamp.hcf.classes.Rogue;
import eu.revamp.hcf.classes.Miner;
import eu.revamp.hcf.classes.Bard;
import eu.revamp.hcf.classes.Archer;

import java.util.*;

import org.bukkit.event.Listener;

public class ArmorClassManager implements Listener
{
    private final Map<UUID, ArmorClass> equippedClassMap;
    private List<ArmorClass> pvpClasses;

    public ArmorClassManager(RevampHCF plugin) {
        this.equippedClassMap = new HashMap<>();
        this.pvpClasses = new ArrayList<>();
        if (RevampHCF.getInstance().getConfig().getBoolean("ARCHER.ENABLED")) {
            this.pvpClasses.add(new Archer(plugin));
        }
        if (RevampHCF.getInstance().getConfig().getBoolean("BARD.ENABLED")) {
            this.pvpClasses.add(new Bard(plugin));
        }
        if (RevampHCF.getInstance().getConfig().getBoolean("MINER.ENABLED")) {
            this.pvpClasses.add(new Miner(plugin));
        }
        if (RevampHCF.getInstance().getConfig().getBoolean("ROGUE.ENABLED")) {
            this.pvpClasses.add(new Rogue(plugin));
        }
        if (RevampHCF.getInstance().getConfig().getBoolean("RANGER.ENABLED")) {
            this.pvpClasses.add(new Ranger(plugin));
        }
        Bukkit.getPluginManager().registerEvents(this, plugin);
        for (ArmorClass pvpClass : this.pvpClasses) {
            if (pvpClass instanceof Listener) {
                plugin.getServer().getPluginManager().registerEvents((Listener)pvpClass, plugin);
            }
        }
    }

    public void onDisable() {
        for (Map.Entry<UUID, ArmorClass> entry : new HashMap<>(this.equippedClassMap).entrySet()) {
            this.setEquippedClass(Bukkit.getPlayer(entry.getKey()), null);
        }
        this.pvpClasses.clear();
        this.equippedClassMap.clear();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        this.setEquippedClass(event.getEntity(), null);
    }

    public Collection<ArmorClass> getPvpClasses() {
        return this.pvpClasses;
    }

    public ArmorClass getEquippedClass(Player player) {
        synchronized (this.equippedClassMap) {
            // monitorexit(this.equippedClassMap)
            return this.equippedClassMap.get(player.getUniqueId());
        }
    }

    public boolean hasClassEquipped(Player player, ArmorClass pvpClass) {
        return this.getEquippedClass(player) == pvpClass;
    }

    public void setEquippedClass(Player player, ArmorClass pvpClass) {
        if (pvpClass == null) {
            ArmorClass equipped = this.equippedClassMap.remove(player.getUniqueId());
            if (equipped != null) {
                equipped.onUnequip(player);
                Bukkit.getPluginManager().callEvent(new ArmorClassUnequipEvent(player, equipped));
            }
        }
        else if (pvpClass.onEquip(player) && pvpClass != this.getEquippedClass(player)) {
            this.equippedClassMap.put(player.getUniqueId(), pvpClass);
            Bukkit.getPluginManager().callEvent(new ArmorClassEquipEvent(player, pvpClass));
        }
    }
}

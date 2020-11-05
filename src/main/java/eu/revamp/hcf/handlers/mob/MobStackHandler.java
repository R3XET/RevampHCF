package eu.revamp.hcf.handlers.mob;

import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.entity.Cow;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.Ageable;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Monster;
import org.bukkit.entity.LivingEntity;
import org.bukkit.World;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.entity.EntityType;
import java.util.List;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class MobStackHandler extends Handler implements Listener
{
    private List<EntityType> mobList;
    
    public MobStackHandler(RevampHCF plugin) {
        super(plugin);
        this.mobList = new ArrayList<>();
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
        this.loadEntityList();
        this.startStackTask();
    }
    
    @Override
    public void disable() {
        this.mobList.clear();
        for (World world : Bukkit.getWorlds()) {
            for (LivingEntity entity : world.getLivingEntities()) {
                if (entity instanceof Monster && entity.isCustomNameVisible()) {
                    entity.remove();
                }
            }
        }
    }
    
    public void loadEntityList() {
        if (!this.mobList.isEmpty()) {
            this.mobList.clear();
        }
        for (String entityName : this.getInstance().getHandlerManager().getConfigHandler().getMobStackingEntity()) {
            EntityType entityType = EntityType.valueOf(entityName.toUpperCase());
            this.mobList.add(entityType);
        }
    }
    
    public void startStackTask() {
        new BukkitRunnable() {
            public void run() {
                int radius = 5;
                List<EntityType> entityTypes = MobStackHandler.this.mobList;
                for (World world : Bukkit.getServer().getWorlds()) {
                    for (LivingEntity entity : world.getLivingEntities()) {
                        if (entityTypes.contains(entity.getType()) && entity.isValid()) {
                            for (Entity nearby : entity.getNearbyEntities(radius, radius, radius)) {
                                if (nearby instanceof LivingEntity && nearby.isValid() && entityTypes.contains(nearby.getType())) {
                                    MobStackHandler.this.stackOne(entity, (LivingEntity)nearby, ChatColor.RED);
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(this.getInstance(), 20L, 100L);
    }
    
    public void unstackOne(LivingEntity livingEntity, ChatColor color) {
        String displayName = livingEntity.getCustomName();
        int stackSize = this.getAmount(displayName, color);
        if (stackSize <= 1) return;
        --stackSize;
        String newDisplayName = color + "x" + stackSize;
        LivingEntity newEntity = (LivingEntity)livingEntity.getWorld().spawnEntity(livingEntity.getLocation(), livingEntity.getType());
        newEntity.setCustomName(newDisplayName);
        newEntity.setCustomNameVisible(false);
        livingEntity.setHealth(0.0);
        if (newEntity instanceof Ageable) {
            ((Ageable)newEntity).setAdult();
        }
        if (newEntity instanceof Zombie) {
            ((Zombie)newEntity).setBaby(false);
        }
    }
    
    public void stackOne(LivingEntity target, LivingEntity stackee, ChatColor color) {
        if (target.getType() != stackee.getType()) return;
        String displayName = target.getCustomName();
        int oldAmount = this.getAmount(displayName, color);
        int newAmount = 1;
        if (this.isStacked(stackee, color)) {
            newAmount = this.getAmount(stackee.getCustomName(), color);
        }
        if (newAmount >= 300) return;
        stackee.remove();
        if (oldAmount == 0) {
            int amount = newAmount + 1;
            String newDisplayName = color + "x" + amount;
            target.setCustomName(newDisplayName);
            target.setCustomNameVisible(true);
        }
        else {
            int amount = oldAmount + newAmount;
            String newDisplayName = color + "x" + amount;
            target.setCustomName(newDisplayName);
        }
    }
    
    public int getAmount(String displayName, ChatColor color) {
        if (displayName == null) return 0;
        String nameColor = ChatColor.getLastColors(displayName);
        if (nameColor.equals(String.valueOf('§' + color.getChar()))) return 0;
        String name1 = displayName.replace("x", "");
        String name2 = ChatColor.stripColor(name1.replace("§f", ""));
        name2 = ChatColor.stripColor(name2);
        if (!name2.matches("[0-9]+")) return 0;
        if (name2.length() > 4) return 0;
        return Integer.parseInt(name2);
    }
    
    public boolean isStacked(LivingEntity entity, ChatColor color) {
        return this.getAmount(entity.getCustomName(), color) != 0;
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = event.getEntity();
            if (entity.getType() != EntityType.PLAYER && entity.getType() != EntityType.VILLAGER) {
                this.unstackOne(entity, ChatColor.RED);
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) return;
        if (entity instanceof Cow) {
            Cow cow = (Cow)entity;
            if (item.getType() == Material.WHEAT && cow.canBreed() && cow.isCustomNameVisible()) {
                cow.setBreed(false);
                Cow baby = (Cow)cow.getWorld().spawnEntity(cow.getLocation(), EntityType.COW);
                baby.setBaby();
            }
        }
    }
}

package eu.revamp.hcf.handlers.chat;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.hcf.RevampHCF;
import lombok.Getter;
import org.apache.commons.lang.WordUtils;
import com.google.common.base.Preconditions;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class DeathMessageHandler extends Handler implements Listener
{
    @Getter private final List<UUID> players;
    
    public DeathMessageHandler(RevampHCF plugin) {
        super(plugin);
        this.players = new ArrayList<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @Override
    public void disable() {
    }
    
    public void toggleAlerts(Player player) {
        if (this.players.contains(player.getUniqueId())) {
            this.players.remove(player.getUniqueId());
            player.sendMessage(Language.DEATHMESSAGES_DISABLED.toString());
        }
        else {
            this.players.add(player.getUniqueId());
            player.sendMessage(Language.DEATHMESSAGES_ENABLED.toString());
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        String message = event.getDeathMessage();
        if (message == null || message.isEmpty()) return;
        if (this.players.contains(event.getEntity().getUniqueId())) {
            event.setDeathMessage(null);
        }
        else {
            event.setDeathMessage(this.getDeathMessage(message, event.getEntity(), this.getKiller(event)));
        }
    }
    
    private CraftEntity getKiller(PlayerDeathEvent event) {
        EntityLiving lastAttacker = ((CraftPlayer)event.getEntity()).getHandle().getLastDamager();
        return (lastAttacker == null) ? null : lastAttacker.getBukkitEntity();
    }
    
    private String getDeathMessage(String input, Entity entity, Entity killer) {
        input = input.replaceFirst("\\[", ChatColor.GRAY + "[" + ChatColor.WHITE);
        input = replaceLast(input, "]", ChatColor.GRAY + "]" + ChatColor.WHITE);
        if (entity != null) {
            input = input.replaceFirst("(?i)" + this.getEntityName(entity), ChatColor.RED + this.getPlayerDisplayName(entity) + ChatColor.YELLOW);
        }
        if (killer != null && (entity == null || !killer.equals(entity))) {
            input = input.replaceFirst("(?i)" + this.getEntityName(killer), ChatColor.RED + this.getKillerDisplayName(killer) + ChatColor.YELLOW);
        }
        return input;
    }
    
    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ')', replacement);
    }
    
    private String getEntityName(Entity entity) {
        Preconditions.checkNotNull((Object)entity, "Entity cannot be null");
        return (entity instanceof Player) ? entity.getName() : ((CraftEntity)entity).getHandle().getName();
    }
    
    private String getPlayerDisplayName(Entity entity) {
        Preconditions.checkNotNull((Object)entity, "Entity cannot be null");
        if (entity instanceof Player) {
            Player player = (Player)entity;
            HCFPlayerData user = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
            return String.valueOf(player.getName()) + ChatColor.DARK_RED + '[' + user.getKills() + ']';
        }
        return WordUtils.capitalizeFully(entity.getType().name().replace('_', ' '));
    }
    
    private String getKillerDisplayName(Entity entity) {
        Preconditions.checkNotNull((Object)entity, "Entity cannot be null");
        if (entity instanceof Player) {
            Player player = (Player)entity;
            HCFPlayerData user = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
            int kills = user.getKills() + 1;
            return String.valueOf(player.getName()) + ChatColor.DARK_RED + '[' + kills + ']';
        }
        return WordUtils.capitalizeFully(entity.getType().name().replace('_', ' '));
    }
}

package eu.revamp.hcf.factions;

import com.google.common.base.Preconditions;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import lombok.Getter;
import lombok.Setter;
import eu.revamp.hcf.utils.inventory.BukkitUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import eu.revamp.hcf.factions.utils.struction.Relation;
import eu.revamp.hcf.factions.events.FactionRenameEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.Bukkit;
import eu.revamp.hcf.factions.utils.games.KothFaction;
import eu.revamp.hcf.factions.utils.games.ConquestFaction;
import eu.revamp.hcf.factions.type.PlayerFaction;
import java.util.LinkedHashMap;
import java.util.Map;

import java.util.UUID;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

@Getter @Setter
public abstract class Faction implements ConfigurationSerializable {
    public static String FACTIONLESS_PREFIX = RevampHCF.getInstance().getConfig().getString("CHAT.FACTIONLESS");
    protected UUID uniqueID;
    protected String name;
    protected long creationMillis;
    public long lastRenameMillis;
    protected double dtrLossMultiplier;
    protected double deathbanMultiplier;
    protected boolean safezone;

    public Faction(String name) {
        this.creationMillis = System.currentTimeMillis();
        this.dtrLossMultiplier = RevampHCF.getInstance().getConfig().getDouble("FACTIONS-SETTINGS.DTRLOSSMULTIPLIER");
        this.deathbanMultiplier = RevampHCF.getInstance().getConfig().getDouble("FACTIONS-SETTINGS.DEATHBANMULTIPLIER");
        this.uniqueID = UUID.randomUUID();
        this.name = name;
    }

    public Faction(Map<String, Object> map) {
        this.creationMillis = System.currentTimeMillis();
        this.dtrLossMultiplier = RevampHCF.getInstance().getConfig().getDouble("FACTIONS-SETTINGS.DTRLOSSMULTIPLIER");
        this.deathbanMultiplier = RevampHCF.getInstance().getConfig().getDouble("FACTIONS-SETTINGS.DEATHBANMULTIPLIER");
        this.uniqueID = UUID.fromString((String) map.get("uniqueID"));
        this.name = (String) map.get("name");
        this.creationMillis = Long.parseLong((String) map.get("creationMillis"));
        this.lastRenameMillis = Long.parseLong((String) map.get("lastRenameMillis"));
        this.deathbanMultiplier = (double) map.get("deathbanMultiplier");
        this.safezone = (boolean) map.get("safezone");
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("uniqueID", this.uniqueID.toString());
        map.put("name", this.name);
        map.put("creationMillis", Long.toString(this.creationMillis));
        map.put("lastRenameMillis", Long.toString(this.lastRenameMillis));
        map.put("deathbanMultiplier", this.deathbanMultiplier);
        map.put("safezone", this.safezone);
        return map;
    }

    public static Faction from(String name, FactionType factionType) {
        if (FactionType.PLAYER == factionType) {
            return new PlayerFaction(name);
        }
        if (FactionType.CONQUEST == factionType) {
            return new ConquestFaction(name);
        }
        if (FactionType.KOTH == factionType) {
            return new KothFaction(name);
        }
        throw new IllegalArgumentException("Invalid faction type \"" + factionType + "\".");
    }

    public boolean setName(String name) {
        return this.setName(name, Bukkit.getConsoleSender());
    }

    public boolean setName(String name, CommandSender sender) {
        if (this.name.equals(name)) {
            return false;
        }
        FactionRenameEvent event = new FactionRenameEvent(this, sender, this.name, name);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return false;
        }
        this.lastRenameMillis = System.currentTimeMillis();
        this.name = name;
        return true;
    }

    public Relation getFactionRelation(Faction faction) {
        if (faction instanceof PlayerFaction) {
            PlayerFaction playerFaction = (PlayerFaction) faction;
            if (playerFaction == this) {
                return Relation.MEMBER;
            }
            if (playerFaction.getAllied().contains(this.uniqueID)) {
                return Relation.ALLY;
            }
        }
        return Relation.ENEMY;
    }

    @SuppressWarnings("deprecation")
    public Relation getRelation(CommandSender sender) {
        return (sender instanceof Player) ? this.getFactionRelation(RevampHCF.getInstance().getFactionManager().getPlayerFaction((Player) sender)) : Relation.ENEMY;
    }

    public String getDisplayName(CommandSender sender) {
        return (this.safezone ? RevampHCF.getInstance().getConfiguration().getSpawnColor() : this.getRelation(sender).toChatColour()) + this.name;
    }

    public String getTabDisplayName(CommandSender sender) {
        return (this.safezone ? RevampHCF.getInstance().getConfiguration().getSpawnColor() : ChatColor.GRAY) + this.name;
    }

    public String getDisplayName(Faction other) {
        return this.getFactionRelation(other).toChatColour() + this.name;
    }

    public void printDetails(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(CC.translate(this.getDisplayName(sender)));
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
    }

    public boolean isDeathban() {
        return !this.safezone && this.deathbanMultiplier > 0.0;
    }

    public void setDeathban(boolean deathban) {
        if (deathban != this.isDeathban()) {
            this.deathbanMultiplier = (deathban ? 1.0 : 0.0);
        }
    }

    public void setDeathbanMultiplier(double deathbanMultiplier) {
        Preconditions.checkArgument(deathbanMultiplier >= 0.0, "Deathban multiplier may not be negative");
        this.deathbanMultiplier = deathbanMultiplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Faction faction = (Faction) o;
        if (this.creationMillis != faction.creationMillis) {
            return false;
        }
        if (this.lastRenameMillis != faction.lastRenameMillis) {
            return false;
        }
        if (Double.compare(faction.dtrLossMultiplier, this.dtrLossMultiplier) != 0) {
            return false;
        }
        if (Double.compare(faction.deathbanMultiplier, this.deathbanMultiplier) != 0) {
            return false;
        }
        if (this.safezone != faction.safezone) {
            return false;
        }
        if (this.uniqueID != null) {
            if (this.uniqueID.equals(faction.uniqueID)) {
                return !((this.name != null) ? (!this.name.equals(faction.name)) : (faction.name != null));
            }
        } else if (faction.uniqueID == null) {
            return !((this.name != null) ? (!this.name.equals(faction.name)) : (faction.name != null));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = (this.uniqueID != null) ? this.uniqueID.hashCode() : 0;
        result = 31 * result + ((this.name != null) ? this.name.hashCode() : 0);
        result = 31 * result + (int) (this.creationMillis ^ this.creationMillis >>> 32);
        result = 31 * result + (int) (this.lastRenameMillis ^ this.lastRenameMillis >>> 32);
        long temp = Double.doubleToLongBits(this.dtrLossMultiplier);
        result = 31 * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.deathbanMultiplier);
        result = 31 * result + (int) (temp ^ temp >>> 32);
        result = 31 * result + (this.safezone ? 1 : 0);
        return result;
    }
}

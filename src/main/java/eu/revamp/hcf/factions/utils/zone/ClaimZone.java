package eu.revamp.hcf.factions.utils.zone;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.games.cuboid.Cuboid;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.Location;
import eu.revamp.hcf.factions.Faction;
import java.util.Collection;
import java.util.ArrayList;

import eu.revamp.hcf.utils.inventory.GenericUtils;
import org.spigotmc.CaseInsensitiveMap;
import eu.revamp.hcf.factions.type.ClaimableFaction;
import java.util.UUID;
import java.util.Map;
import java.util.Random;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import eu.revamp.hcf.games.cuboid.NamedCuboid;

public class ClaimZone extends NamedCuboid implements Cloneable, ConfigurationSerializable
{
    private static Random RANDOM = new Random();
    private Map<String, SubclaimZone> SubclaimZones;
    @Getter private UUID claimUniqueID;
    private UUID factionUUID;
    private ClaimableFaction faction;
    private boolean loaded;

    public ClaimZone(Map<String, Object> map) {
        super(map);
        this.SubclaimZones = new CaseInsensitiveMap<>();
        this.loaded = false;
        this.name = (String) map.get("name");
        this.claimUniqueID = UUID.fromString((String) map.get("claimUUID"));
        this.factionUUID = UUID.fromString((String) map.get("factionUUID"));
        for (SubclaimZone SubclaimZone : GenericUtils.createList(map.get("SubclaimZones"), SubclaimZone.class)) {
            this.SubclaimZones.put(SubclaimZone.getName(), SubclaimZone);
        }
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("name", this.name);
        map.put("claimUUID", this.claimUniqueID.toString());
        map.put("factionUUID", this.factionUUID.toString());
        map.put("SubclaimZones", new ArrayList<>(this.SubclaimZones.values()));
        return map;
    }
    
    public ClaimZone(Faction faction, Location location) {
        super(location, location);
        this.SubclaimZones = new CaseInsensitiveMap<>();
        this.loaded = false;
        this.name = this.generateName();
        this.factionUUID = faction.getUniqueID();
        this.claimUniqueID = UUID.randomUUID();
    }
    
    public ClaimZone(Faction faction, Location location1, Location location2) {
        super(location1, location2);
        this.SubclaimZones = new CaseInsensitiveMap<>();
        this.loaded = false;
        this.name = this.generateName();
        this.factionUUID = faction.getUniqueID();
        this.claimUniqueID = UUID.randomUUID();
    }
    
    public ClaimZone(Faction faction, World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        super(world, x1, y1, z1, x2, y2, z2);
        this.SubclaimZones = new CaseInsensitiveMap<>();
        this.loaded = false;
        this.name = this.generateName();
        this.factionUUID = faction.getUniqueID();
        this.claimUniqueID = UUID.randomUUID();
    }
    
    public ClaimZone(Faction faction, Cuboid cuboid) {
        super(cuboid);
        this.SubclaimZones = new CaseInsensitiveMap<>();
        this.loaded = false;
        this.name = this.generateName();
        this.factionUUID = faction.getUniqueID();
        this.claimUniqueID = UUID.randomUUID();
    }
    
    private String generateName() {
        return String.valueOf(ClaimZone.RANDOM.nextInt(899) + 100);
    }

    public ClaimableFaction getFaction() {
        if (!this.loaded) {
            Faction faction = RevampHCF.getInstance().getFactionManager().getFaction(this.factionUUID);
            if (faction instanceof ClaimableFaction) {
                this.faction = (ClaimableFaction)faction;
            }
            this.loaded = true;
        }
        return this.faction;
    }
    
    public Collection<SubclaimZone> getSubclaimZones() {
        return this.SubclaimZones.values();
    }
    
    public SubclaimZone getSubclaimZone(String name) {
        return this.SubclaimZones.get(name);
    }
    
    public String getFormattedName() {
        return this.getName() + ": (" + this.worldName + ", " + this.x1 + ", " + this.y1 + ", " + this.z1 + ") - " + "(" + this.worldName + ", " + this.x2 + ", " + this.y2 + ", " + this.z2 + ')';
    }
}

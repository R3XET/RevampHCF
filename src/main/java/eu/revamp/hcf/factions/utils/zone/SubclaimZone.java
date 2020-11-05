package eu.revamp.hcf.factions.utils.zone;

import eu.revamp.hcf.games.cuboid.Cuboid;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.Location;
import eu.revamp.hcf.factions.Faction;

import java.util.stream.Collectors;

import eu.revamp.hcf.utils.inventory.GenericUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.Set;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class SubclaimZone extends ClaimZone implements Cloneable, ConfigurationSerializable
{
    @Getter private Set<UUID> accessibleMembers;
    
    public SubclaimZone(Map<String, Object> map) {
        super(map);
        (this.accessibleMembers = new HashSet<>()).addAll(GenericUtils.createList(map.get("accessibleMembers"), String.class).stream().map(UUID::fromString).collect(Collectors.toList()));
    }
    
    public SubclaimZone(Faction faction, Location location) {
        super(faction, location, location);
        this.accessibleMembers = new HashSet<>();
    }
    
    public SubclaimZone(Faction faction, Location location1, Location location2) {
        super(faction, location1, location2);
        this.accessibleMembers = new HashSet<>();
    }
    
    public SubclaimZone(Faction faction, World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        super(faction, world, x1, y1, z1, x2, y2, z2);
        this.accessibleMembers = new HashSet<>();
    }
    
    public SubclaimZone(Faction faction, Cuboid cuboid) {
        super(faction, cuboid);
        this.accessibleMembers = new HashSet<>();
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.remove("SUBCLAIMS");
        map.put("accessibleMembers", this.accessibleMembers.stream().map(UUID::toString).collect(Collectors.toList()));
        return map;
    }

    public SubclaimZone clone() {
        return (SubclaimZone)super.clone();
    }
}

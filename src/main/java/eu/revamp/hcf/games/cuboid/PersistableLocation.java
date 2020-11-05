package eu.revamp.hcf.games.cuboid;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import java.util.LinkedHashMap;
import java.util.Map;
import com.google.common.base.Preconditions;
import java.util.UUID;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class PersistableLocation implements ConfigurationSerializable, Cloneable
{
    private Location location;
    private World world;
    @Getter @Setter private String worldName;
    @Getter @Setter private UUID worldUID;
    @Getter @Setter private double x;
    @Getter @Setter private double y;
    @Getter @Setter private double z;
    @Getter @Setter private float yaw;
    @Getter @Setter private float pitch;
    
    public PersistableLocation(Location location) {
        Preconditions.checkNotNull((Object)location, "Location cannot be null");
        Preconditions.checkNotNull((Object)location.getWorld(), "Locations' world cannot be null");
        this.world = location.getWorld();
        this.worldName = this.world.getName();
        this.worldUID = this.world.getUID();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }
    
    public PersistableLocation(World world, double x, double y, double z) {
        this.worldName = world.getName();
        this.x = x;
        this.y = y;
        this.z = z;
        float n = 0.0f;
        this.yaw = n;
        this.pitch = n;
    }
    
    public PersistableLocation(String worldName, double x, double y, double z) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        float n = 0.0f;
        this.yaw = n;
        this.pitch = n;
    }
    
    public PersistableLocation(Map<String, String> map) {
        this.worldName = map.get("worldName");
        this.worldUID = UUID.fromString(map.get("worldUID"));
        Object o = map.get("x");
        if (o instanceof String) {
            this.x = Double.parseDouble((String)o);
        }
        else {
            this.x = (double)o;
        }
        o = map.get("y");
        if (o instanceof String) {
            this.y = Double.parseDouble((String)o);
        }
        else {
            this.y = (double)o;
        }
        o = map.get("z");
        if (o instanceof String) {
            this.z = Double.parseDouble((String)o);
        }
        else {
            this.z = (double)o;
        }
        this.yaw = Float.parseFloat(map.get("yaw"));
        this.pitch = Float.parseFloat(map.get("pitch"));
    }
    
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("worldName", this.worldName);
        map.put("worldUID", this.worldUID.toString());
        map.put("x", this.x);
        map.put("y", this.y);
        map.put("z", this.z);
        map.put("yaw", Float.toString(this.yaw));
        map.put("pitch", Float.toString(this.pitch));
        return map;
    }
    
    public World getWorld() {
        Preconditions.checkNotNull((Object)this.worldUID, "World UUID cannot be null");
        Preconditions.checkNotNull((Object)this.worldName, "World name cannot be null");
        if (this.world == null) {
            this.world = Bukkit.getWorld(this.worldUID);
        }
        return this.world;
    }
    
    public void setWorld(World world) {
        this.worldName = world.getName();
        this.worldUID = world.getUID();
        this.world = world;
    }
    
    public Location getLocation() {
        if (this.location == null) {
            this.location = new Location(this.getWorld(), this.x, this.y, this.z, this.yaw, this.pitch);
        }
        return this.location;
    }
    
    public PersistableLocation clone() throws CloneNotSupportedException {
        try {
            return (PersistableLocation)super.clone();
        }
        catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
            throw new RuntimeException();
        }
    }
    
    @Override
    public String toString() {
        return "PersistableLocation [worldName=" + this.worldName + ", worldUID=" + this.worldUID + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", yaw=" + this.yaw + ", pitch=" + this.pitch + ']';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersistableLocation)) {
            return false;
        }
        PersistableLocation that = (PersistableLocation)o;
        if (Double.compare(that.x, this.x) != 0) {
            return false;
        }
        if (Double.compare(that.y, this.y) != 0) {
            return false;
        }
        if (Double.compare(that.z, this.z) != 0) {
            return false;
        }
        if (Float.compare(that.yaw, this.yaw) != 0) {
            return false;
        }
        if (Float.compare(that.pitch, this.pitch) != 0) {
            return false;
        }
        Label_0137: {
            if (this.world != null) {
                if (this.world.equals(that.world)) {
                    break Label_0137;
                }
            }
            else if (that.world == null) {
                break Label_0137;
            }
            return false;
        }
        Label_0173: {
            if (this.worldName != null) {
                if (this.worldName.equals(that.worldName)) {
                    break Label_0173;
                }
            }
            else if (that.worldName == null) {
                break Label_0173;
            }
            return false;
        }
        if (this.worldUID != null) {
            return this.worldUID.equals(that.worldUID);
        }
        else return that.worldUID == null;
    }
    
    @Override
    public int hashCode() {
        int result = (this.world != null) ? this.world.hashCode() : 0;
        result = 31 * result + ((this.worldName != null) ? this.worldName.hashCode() : 0);
        result = 31 * result + ((this.worldUID != null) ? this.worldUID.hashCode() : 0);
        long temp = Double.doubleToLongBits(this.x);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.y);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.z);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        result = 31 * result + ((this.yaw != 0.0f) ? Float.floatToIntBits(this.yaw) : 0);
        result = 31 * result + ((this.pitch != 0.0f) ? Float.floatToIntBits(this.pitch) : 0);
        return result;
    }
}

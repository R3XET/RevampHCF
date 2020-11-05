package eu.revamp.hcf.factions.utils;

import lombok.Getter;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.games.cuboid.Cuboid;
import eu.revamp.hcf.RevampHCF;
import com.google.common.base.Preconditions;
import eu.revamp.hcf.factions.type.PlayerFaction;
import org.bukkit.Location;
import org.bukkit.World;
import java.util.UUID;

public class ClaimSelection implements Cloneable
{
    @Getter private UUID uuid;
    @Getter private World world;
    @Getter private long lastUpdateMillis;
    @Getter private Location pos1;
    @Getter private Location pos2;
    
    public ClaimSelection(World world) {
        this.uuid = UUID.randomUUID();
        this.world = world;
    }
    
    public ClaimSelection(World world, Location pos1, Location pos2) {
        this.uuid = UUID.randomUUID();
        this.world = world;
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public int getPrice(PlayerFaction playerFaction, boolean selling) {
        Preconditions.checkNotNull((Object)playerFaction, "Player faction cannot be null");
        return (this.pos1 == null || this.pos2 == null) ? 0 : RevampHCF.getInstance().getHandlerManager().getClaimHandler().calculatePrice(new Cuboid(this.pos1, this.pos2), playerFaction.getClaims().size(), selling);
    }
    
    public ClaimZone toClaim(Faction faction) {
        Preconditions.checkNotNull((Object)faction, "Faction cannot be null");
        return (this.pos1 == null || this.pos2 == null) ? null : new ClaimZone(faction, this.pos1, this.pos2);
    }

    public void setPos1(Location location) {
        Preconditions.checkNotNull((Object)location, "The location cannot be null");
        this.pos1 = location;
        this.lastUpdateMillis = System.currentTimeMillis();
    }

    public void setPos2(Location location) {
        Preconditions.checkNotNull((Object)location, "The location is null");
        this.pos2 = location;
        this.lastUpdateMillis = System.currentTimeMillis();
    }
    
    public boolean hasBothPositionsSet() {
        return this.pos1 != null && this.pos2 != null;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClaimSelection)) {
            return false;
        }
        ClaimSelection that = (ClaimSelection)o;
        Label_0054: {
            if (this.uuid != null) {
                if (this.uuid.equals(that.uuid)) {
                    break Label_0054;
                }
            }
            else if (that.uuid == null) {
                break Label_0054;
            }
            return false;
        }
        Label_0087: {
            if (this.world != null) {
                if (this.world.equals(that.world)) {
                    break Label_0087;
                }
            }
            else if (that.world == null) {
                break Label_0087;
            }
            return false;
        }
        if (this.pos1 != null) {
            if (this.pos1.equals(that.pos1)) {
                return !((this.pos2 != null) ? (!this.pos2.equals(that.pos2)) : (that.pos2 != null));
            }
        }
        else if (that.pos1 == null) {
            return !((this.pos2 != null) ? (!this.pos2.equals(that.pos2)) : (that.pos2 != null));
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.uuid != null) ? this.uuid.hashCode() : 0;
        result = 31 * result + ((this.world != null) ? this.world.hashCode() : 0);
        result = 31 * result + ((this.pos1 != null) ? this.pos1.hashCode() : 0);
        result = 31 * result + ((this.pos2 != null) ? this.pos2.hashCode() : 0);
        return result;
    }
    
    public ClaimSelection clone() {
        try {
            return (ClaimSelection)super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }
}

package eu.revamp.hcf.factions.type;

import java.util.Map;

import lombok.Getter;
import eu.revamp.hcf.games.cuboid.Cuboid;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class TournamentFaction extends ClaimableFaction implements ConfigurationSerializable
{
    @Getter private Cuboid tournamentArea;
    
    public TournamentFaction() {
        super("Tournament");
        this.tournamentArea = null;
    }
    
    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> map = super.serialize();
        map.put("glowstoneArea", this.tournamentArea);
        return map;
    }
    
    public TournamentFaction(Map<String, Object> map) {
        super(map);
        this.setDeathban(true);
        this.tournamentArea = (Cuboid) map.get("glowstoneArea");
    }
    public void setTournamentArea(Cuboid glowstoneArea) {
        this.tournamentArea = glowstoneArea;
    }
}

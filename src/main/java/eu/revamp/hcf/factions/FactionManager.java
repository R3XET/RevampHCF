package eu.revamp.hcf.factions;

import org.bukkit.command.CommandSender;
import javax.annotation.Nullable;
import org.bukkit.entity.Player;
import eu.revamp.hcf.factions.type.PlayerFaction;
import java.util.UUID;
import org.bukkit.block.Block;
import org.bukkit.World;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import org.bukkit.Location;
import com.google.common.collect.ImmutableList;
import java.util.Map;
import org.apache.commons.lang.time.DurationFormatUtils;
import eu.revamp.hcf.RevampHCF;
import java.util.concurrent.TimeUnit;

public interface FactionManager
{
    long MAX_DTR_REGEN_MILLIS = TimeUnit.HOURS.toMillis(RevampHCF.getInstance().getConfig().getInt("FACTIONS-SETTINGS.MAX-DTR-REGEN"));
    String MAX_DTR_REGEN_WORDS = DurationFormatUtils.formatDurationWords(FactionManager.MAX_DTR_REGEN_MILLIS, true, true);
    
    Map<String, ?> getFactionNameMap();
    
    ImmutableList<Faction> getFactions();
    
    ClaimZone getClaimAt(Location p0);
    
    ClaimZone getClaimAt(World p0, int p1, int p2);
    
    Faction getFactionAt(Location p0);
    
    Faction getFactionAt(Block p0);
    
    Faction getFactionAt(World p0, int p1, int p2);
    
    Faction getFaction(String p0);
    
    Faction getFaction(UUID p0);
    
    PlayerFaction getContainingPlayerFaction(String p0);
    
    @Deprecated
    PlayerFaction getPlayerFaction(Player p0);
    
    PlayerFaction getPlayerFaction(UUID p0);
    
    Faction getContainingFaction(String p0);
    
    boolean containsFaction(Faction p0);
    
    @Nullable
    Faction createFaction(String p0, FactionType p1);
    
    @Nullable
    Faction createFaction(CommandSender p0, String p1, FactionType p2);
    
    boolean removeFaction(Faction p0, CommandSender p1);
    
    void reloadFactionData();
    
    void saveFactionData();
}

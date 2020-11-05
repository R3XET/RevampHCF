package eu.revamp.hcf.factions.type;

import eu.revamp.hcf.factions.enums.ClaimChangeEnum;
import eu.revamp.hcf.factions.events.FactionClaimChangeEvent;
import eu.revamp.hcf.factions.events.FactionClaimChangedEvent;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Bukkit;
import java.util.Optional;
import javax.annotation.Nullable;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import java.util.Collection;
import eu.revamp.hcf.utils.inventory.GenericUtils;
import java.util.Map;
import java.util.ArrayList;
import org.bukkit.World;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import eu.revamp.hcf.factions.Faction;

public abstract class ClaimableFaction extends Faction implements ConfigurationSerializable
{
    protected List<ClaimZone> claims;
    protected static ImmutableMap<World.Environment, String> ENVIRONMENT_MAPPINGS = ImmutableMap.of(World.Environment.NETHER, "Nether", World.Environment.NORMAL, "Overworld", World.Environment.THE_END, "The End");

    public ClaimableFaction(String name) {
        super(name);
        this.claims = new ArrayList<>();
    }
    
    public ClaimableFaction(Map<String, Object> map) {
        super(map);
        (this.claims = new ArrayList<>()).addAll(GenericUtils.createList(map.get("claims"), ClaimZone.class));
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("claims", new ArrayList<>(this.claims));
        return map;
    }
    
    @Override
    public void printDetails(CommandSender sender) {
        sender.sendMessage(' ' + this.getDisplayName(sender));
        for (ClaimZone claim : this.claims) {
            Location location = claim.getCenter();
            sender.sendMessage(ChatColor.YELLOW + "  Location: " + ChatColor.RED + '(' + ClaimableFaction.ENVIRONMENT_MAPPINGS.get(location.getWorld().getEnvironment()) + ", " + location.getBlockX() + " " + CC.STICK + " " + location.getBlockZ() + ')');
        }
    }
    
    public List<ClaimZone> getClaims() {
        return ImmutableList.copyOf(this.claims);
    }
    
    public Collection<ClaimZone> getClaims(World world) {
        return Collections2.filter(new ArrayList<>(this.claims), claim -> claim != null && world.equals(claim.getWorld()));
    }
    
    public boolean addClaim(ClaimZone claim, @Nullable CommandSender optionalSender) {
        return this.addClaims(ImmutableList.of(claim), optionalSender);
    }
    
    public boolean addClaims(Collection<ClaimZone> claims, @Nullable CommandSender optionalSender) {
        CommandSender sender = Optional.ofNullable(optionalSender).orElse(Bukkit.getConsoleSender());
        FactionClaimChangeEvent event = new FactionClaimChangeEvent(sender, ClaimChangeEnum.CLAIM, claims, this);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled() || !this.claims.addAll(claims)) {
            return false;
        }
        Bukkit.getPluginManager().callEvent(new FactionClaimChangedEvent(sender, ClaimChangeEnum.CLAIM, claims));
        return true;
    }
    
    public boolean removeClaim(ClaimZone claim, @Nullable CommandSender optionalSender) {
        return this.removeClaims(ImmutableList.of(claim), optionalSender);
    }
    
    public boolean removeClaims(Collection<ClaimZone> claims, @Nullable CommandSender optionalSender) {
        if (optionalSender == null) {
            optionalSender = Bukkit.getConsoleSender();
        }
        CommandSender sender = optionalSender;
        FactionClaimChangeEvent event = new FactionClaimChangeEvent(sender, ClaimChangeEnum.UNCLAIM, claims, this);
        Bukkit.getPluginManager().callEvent(event);
        List<ClaimZone> copy = new ArrayList<>(this.claims);
        if (event.isCancelled()) {
            return false;
        }
        if (!copy.removeAll(claims)) {
            return false;
        }
        this.claims.clear();
        this.claims.addAll(copy);
        if (this instanceof PlayerFaction) {
            PlayerFaction playerFaction = (PlayerFaction)this;
            Location home = playerFaction.getHome();
            RevampHCF plugin = RevampHCF.getInstance();
            int refund = 0;
            for (ClaimZone claim : claims) {
                refund += plugin.getHandlerManager().getClaimHandler().calculatePrice(claim, claims.size(), true);
                if (home != null && claim.contains(home)) {
                    playerFaction.setHome(null);
                    playerFaction.broadcast(String.valueOf(ChatColor.RED.toString()) + ChatColor.BOLD + "Your factions' home " + "was unset as its residing claim was removed.");
                    break;
                }
            }
            HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(playerFaction.getLeader().getUniqueID());
            data.addBalance(refund);
            playerFaction.broadcast(ChatColor.YELLOW + "Faction leader was refunded " + ChatColor.GREEN + "$" + refund + ChatColor.YELLOW + " due to a land unclaim.");
        }
        Bukkit.getPluginManager().callEvent(new FactionClaimChangedEvent(sender, ClaimChangeEnum.UNCLAIM, claims));
        return true;
    }
}

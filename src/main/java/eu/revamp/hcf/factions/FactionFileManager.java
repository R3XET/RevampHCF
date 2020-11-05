package eu.revamp.hcf.factions;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.enums.ClaimChangeEnum;
import eu.revamp.hcf.factions.events.*;
import eu.revamp.hcf.factions.type.*;
import eu.revamp.hcf.factions.utils.FactionMember;
import eu.revamp.hcf.factions.utils.struction.Role;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.generic.JavaUtils;
import eu.revamp.system.enums.ChatChannel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.craftbukkit.v1_8_R3.util.LongHash;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.spigotmc.CaseInsensitiveMap;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FactionFileManager implements Listener, FactionManager
{
    private WarzoneFaction warzone;
    private WildernessFaction wilderness;
    private Table<String, Long, ClaimZone> claimPositionMap;
    private ConcurrentMap<UUID, UUID> factionPlayerUuidMap;
    private ConcurrentMap<UUID, Faction> factionUUIDMap;
    private Map<String, UUID> factionNameMap;
    private RevampHCF plugin;
    
    public FactionFileManager(RevampHCF plugin) {
        this.claimPositionMap = HashBasedTable.create();
        this.factionPlayerUuidMap = new ConcurrentHashMap<>();
        this.factionUUIDMap = new ConcurrentHashMap<>();
        this.factionNameMap = new CaseInsensitiveMap<>();
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.warzone = new WarzoneFaction();
        this.wilderness = new WildernessFaction();
        this.reloadFactionData();
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerJoinedFaction(FactionPlayerJoinedEvent event) {
        this.factionPlayerUuidMap.put(event.getPlayerUUID(), event.getFaction().getUniqueID());
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerLeftFaction(FactionPlayerLeftEvent event) {
        this.factionPlayerUuidMap.remove(event.getUniqueID());
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onFactionRename(FactionRenameEvent event) {
        this.factionNameMap.remove(event.getOriginalName());
        this.factionNameMap.put(event.getNewName(), event.getFaction().getUniqueID());
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onFactionClaim(FactionClaimChangedEvent event) {
        for (ClaimZone claim : event.getAffectedClaims()) {
            this.cacheClaim(claim, event.getCause());
        }
    }
    
    @Deprecated
    public Map<String, UUID> getFactionNameMap() {
        return this.factionNameMap;
    }
    
    public ImmutableList<Faction> getFactions() {
        return ImmutableList.copyOf(this.factionUUIDMap.values());
    }
    
    public ClaimZone getClaimAt(World world, int x, int z) {
        return this.claimPositionMap.get(world.getName(), LongHash.toLong(x, z));
    }
    
    public ClaimZone getClaimAt(Location location) {
        return this.getClaimAt(location.getWorld(), location.getBlockX(), location.getBlockZ());
    }
    
    public Faction getFactionAt(World world, int x, int z) {
        World.Environment environment = world.getEnvironment();
        ClaimZone claim = this.getClaimAt(world, x, z);
        if (claim != null) {
            return claim.getFaction();
        }
        if (environment == World.Environment.THE_END) {
            return this.warzone;
        }
        int warzoneRadius = this.plugin.getConfiguration().getWarzoneRadius();
        return (Math.abs(x) > warzoneRadius || Math.abs(z) > warzoneRadius) ? this.wilderness : this.warzone;
    }
    
    public Faction getFactionAt(Location location) {
        return this.getFactionAt(location.getWorld(), location.getBlockX(), location.getBlockZ());
    }
    
    public Faction getFactionAt(Block block) {
        return this.getFactionAt(block.getLocation());
    }
    
    public Faction getFaction(String factionName) {
        UUID uuid = this.factionNameMap.get(factionName);
        return (uuid != null) ? this.factionUUIDMap.get(uuid) : null;
    }
    
    public Faction getFaction(UUID factionUUID) {
        return this.factionUUIDMap.get(factionUUID);
    }
    
    public PlayerFaction getPlayerFaction(UUID playerUUID) {
        UUID uuid = this.factionPlayerUuidMap.get(playerUUID);
        Faction faction = (uuid != null) ? this.factionUUIDMap.get(uuid) : null;
        return (faction instanceof PlayerFaction) ? ((PlayerFaction)faction) : null;
    }
    @Deprecated public PlayerFaction getPlayerFaction(Player player) {
        return this.getPlayerFaction(player.getUniqueId());
    }
    @SuppressWarnings("deprecation")
    public PlayerFaction getContainingPlayerFaction(String search) {
        OfflinePlayer target = JavaUtils.isUUID(search) ? Bukkit.getOfflinePlayer(UUID.fromString(search)) : Bukkit.getOfflinePlayer(search);
        return (target.hasPlayedBefore() || target.isOnline()) ? this.getPlayerFaction(target.getUniqueId()) : null;
    }
    @SuppressWarnings("deprecation")
    public Faction getContainingFaction(String search) {
        Faction faction = this.getFaction(search);
        if (faction != null) {
            return faction;
        }
        UUID playerUUID = Bukkit.getOfflinePlayer(search).getUniqueId();
        if (playerUUID != null) {
            return this.getPlayerFaction(playerUUID);
        }
        return null;
    }
    
    public boolean containsFaction(Faction faction) {
        return this.factionNameMap.containsKey(faction.getName());
    }
    
    public Faction createFaction(String name, FactionType factionType) {
        return this.createFaction(Bukkit.getConsoleSender(), name, factionType);
    }
    
    public Faction createFaction(CommandSender sender, String name, FactionType factionType) {
        Objects.requireNonNull(sender);
        Objects.requireNonNull(name);
        Objects.requireNonNull(factionType);
        Faction faction = Faction.from(name, factionType);
        if (FactionType.PLAYER == factionType) {
            PlayerFaction playerFaction = (PlayerFaction)faction;
            if (sender instanceof Player) {
                Player player = (Player)sender;
                FactionMember member = new FactionMember(player, ChatChannel.PUBLIC, Role.LEADER);
                if (!playerFaction.addMember(sender, player, player.getUniqueId(), member)) {
                    return null;
                }
            }
        }
        if (this.factionUUIDMap.putIfAbsent(faction.getUniqueID(), faction) != null) {
            return null;
        }
        this.factionNameMap.put(faction.getName(), faction.getUniqueID());
        FactionCreateEvent createEvent = new FactionCreateEvent(faction, sender);
        Bukkit.getPluginManager().callEvent(createEvent);
        return createEvent.isCancelled() ? null : faction;
    }
    
    public boolean removeFaction(Faction faction, CommandSender sender) {
        Objects.requireNonNull(faction, "Faction cannot be null");
        if (!this.factionUUIDMap.containsKey(faction.getUniqueID())) {
            return false;
        }
        FactionRemoveEvent removeEvent = new FactionRemoveEvent(faction, sender);
        Bukkit.getPluginManager().callEvent(removeEvent);
        if (removeEvent.isCancelled()) {
            return false;
        }
        this.factionUUIDMap.remove(faction.getUniqueID());
        this.factionNameMap.remove(faction.getName());
        if (faction instanceof ClaimableFaction) {
            Bukkit.getPluginManager().callEvent(new FactionClaimChangedEvent(sender, ClaimChangeEnum.UNCLAIM, ((ClaimableFaction)faction).getClaims()));
        }
        if (faction instanceof PlayerFaction) {
            PlayerFaction playerFaction = (PlayerFaction)faction;
            for (PlayerFaction ally : playerFaction.getAlliedFactions()) {
                ally.getRelations().remove(faction.getUniqueID());
            }
            for (UUID uuid : playerFaction.getMembers().keySet()) {
                playerFaction.removeMember(sender, null, uuid, true, true);
            }
        }
        return true;
    }
    
    private void cacheClaim(ClaimZone claim, ClaimChangeEnum cause) {
        Objects.requireNonNull(claim, "Claim cannot be null");
        Objects.requireNonNull(cause, "Cause cannot be null");
        Preconditions.checkArgument(cause != ClaimChangeEnum.RESIZE, "Cannot cache claims of resize type");
        World world = claim.getWorld();
        if (world == null) return;
        int minX = Math.min(claim.getX1(), claim.getX2());
        int maxX = Math.max(claim.getX1(), claim.getX2());
        int minZ = Math.min(claim.getZ1(), claim.getZ2());
        int maxZ = Math.max(claim.getZ1(), claim.getZ2());
        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                if (cause == ClaimChangeEnum.CLAIM) {
                    this.claimPositionMap.put(world.getName(), LongHash.toLong(x, z), claim);
                }
                else if (cause == ClaimChangeEnum.UNCLAIM) {
                    this.claimPositionMap.remove(world.getName(), LongHash.toLong(x, z));
                }
            }
        }
    }
    
    private void cacheFaction(Faction faction) {
        Objects.requireNonNull(faction, "Faction cannot be null");
        this.factionNameMap.put(faction.getName(), faction.getUniqueID());
        this.factionUUIDMap.put(faction.getUniqueID(), faction);
        if (faction instanceof ClaimableFaction) {
            for (ClaimZone claim : ((ClaimableFaction)faction).getClaims()) {
                this.cacheClaim(claim, ClaimChangeEnum.CLAIM);
            }
        }
        if (faction instanceof PlayerFaction) {
            for (FactionMember factionMember : ((PlayerFaction)faction).getMembers().values()) {
                this.factionPlayerUuidMap.put(factionMember.getUniqueID(), faction.getUniqueID());
            }
        }
    }
    
    public void reloadFactionData() {
        this.factionNameMap.clear();
        Object object = RevampHCF.getInstance().getFactions().get("factions");
        if (object instanceof MemorySection) {
            MemorySection section = (MemorySection)object;
            for (String factionName : section.getKeys(false)) {
                Object next = RevampHCF.getInstance().getFactions().get(String.valueOf(section.getCurrentPath()) + '.' + factionName);
                if (next instanceof Faction) {
                    this.cacheFaction((Faction)next);
                }
            }
        }
        else if (object instanceof List) {
            List<?> list = (List<?>)object;
            for (Object next2 : list) {
                if (next2 instanceof Faction) {
                    this.cacheFaction((Faction)next2);
                }
            }
        }
        Set<Faction> adding = new HashSet<>();
        if (!this.factionNameMap.containsKey("NorthRoad")) {
            adding.add(new RoadFaction.NorthRoadFaction());
            adding.add(new RoadFaction.EastRoadFaction());
            adding.add(new RoadFaction.SouthRoadFaction());
            adding.add(new RoadFaction.WestRoadFaction());
        }
        if (!this.factionNameMap.containsKey("Spawn")) {
            adding.add(new SpawnFaction());
        }
        if (!this.factionNameMap.containsKey("EndPortal")) {
            adding.add(new EndPortalFaction());
        }
        if (!this.factionNameMap.containsKey("Glowstone")) {
            adding.add(new GlowstoneFaction());
        }
        if (!this.factionNameMap.containsKey("Tournament")) {
            adding.add(new TournamentFaction());
        }
        for (Faction added : adding) {
            this.cacheFaction(added);
            Bukkit.getConsoleSender().sendMessage(CC.translate("&c&lFaction " + added.getName() + " successfully created."));
        }
    }
    
    public void saveFactionData() {
        RevampHCF.getInstance().getFactions().set("factions", new ArrayList<>(this.factionUUIDMap.values()));
        RevampHCF.getInstance().getFactions().save();
    }
}

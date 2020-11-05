package eu.revamp.hcf.factions.type;

import com.google.common.collect.Sets;
import java.util.Collections;
import com.google.common.base.Preconditions;
import eu.revamp.hcf.factions.enums.FactionLeaveEnum;
import eu.revamp.hcf.factions.utils.FactionMember;
import eu.revamp.hcf.factions.utils.struction.Raidable;
import eu.revamp.hcf.factions.utils.struction.RegenStatus;
import eu.revamp.hcf.factions.utils.struction.Relation;
import eu.revamp.hcf.factions.utils.struction.Role;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.spigot.utils.chat.color.CC;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang.time.DurationFormatUtils;
import eu.revamp.hcf.utils.chat.JavaUtils;
import org.apache.commons.lang.StringUtils;
import eu.revamp.hcf.utils.inventory.BukkitUtils;
import java.io.File;

import eu.revamp.hcf.factions.events.FactionDTRChangeEvent;
import eu.revamp.hcf.timers.HomeHandler;
import org.bukkit.ChatColor;
import com.google.common.base.Objects;
import org.bukkit.Location;

import java.util.HashSet;
import com.google.common.collect.ImmutableMap;
import eu.revamp.hcf.factions.Faction;

import java.util.List;
import com.google.common.collect.Maps;
import eu.revamp.hcf.factions.events.FactionPlayerLeftEvent;
import eu.revamp.hcf.factions.events.FactionPlayerLeaveEvent;
import eu.revamp.hcf.factions.events.FactionPlayerJoinedEvent;
import org.bukkit.Bukkit;
import eu.revamp.hcf.factions.events.FactionPlayerJoinEvent;
import javax.annotation.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Collection;
import eu.revamp.hcf.utils.inventory.GenericUtils;

import java.util.TreeSet;
import java.util.HashMap;
import eu.revamp.hcf.games.cuboid.PersistableLocation;
import java.util.Set;
import java.util.UUID;
import java.util.Map;

@Getter @Setter
public class PlayerFaction extends ClaimableFaction implements Raidable
{
    public Map<UUID, Relation> requestedRelations;
    public Map<UUID, Relation> relations;
    public Map<UUID, FactionMember> members;
    public Set<String> invitedPlayerNames;
    public PersistableLocation home;
    @NonNull public String announcement;
    public boolean open;
    public int balance;
    public int lives;
    private int kothCaptures;
    private int conquestCaptures;
    private int points;
    public double deathsUntilRaidable;
    public long regenCooldownTimestamp;
    private transient UUID focused;
    private long lastDtrUpdateTimestamp;
    private static UUID[] EMPTY_UUID_ARRAY = new UUID[0];

    public PlayerFaction(String name) {
        super(name);
        this.requestedRelations = new HashMap<>();
        this.relations = new HashMap<>();
        this.members = new HashMap<>();
        this.invitedPlayerNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        this.deathsUntilRaidable = 1.0;
    }
    
    public PlayerFaction(Map<String, Object> map) {
        super(map);
        this.requestedRelations = new HashMap<>();
        this.relations = new HashMap<>();
        this.members = new HashMap<>();
        this.invitedPlayerNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        this.deathsUntilRaidable = 1.0;
        for (Map.Entry<String, FactionMember> entry : GenericUtils.castMap(map.get("members"), String.class, FactionMember.class).entrySet()) {
            if (entry.getValue() != null) {
                this.members.put(UUID.fromString(entry.getKey()), entry.getValue());
            }
        }
        this.invitedPlayerNames.addAll(GenericUtils.createList(map.get("invitedPlayerNames"), String.class));
        Object object = map.get("home");
        if (object != null) {
            this.home = (PersistableLocation)object;
        }
        object = map.get("announcement");
        if (object != null) {
            this.announcement = (String)object;
        }
        for (Map.Entry<String, String> entry2 : GenericUtils.castMap(map.get("relations"), String.class, String.class).entrySet()) {
            this.relations.put(UUID.fromString(entry2.getKey()), Relation.valueOf(entry2.getValue()));
        }
        for (Map.Entry<String, String> entry2 : GenericUtils.castMap(map.get("requestedRelations"), String.class, String.class).entrySet()) {
            this.requestedRelations.put(UUID.fromString(entry2.getKey()), Relation.valueOf(entry2.getValue()));
        }
        this.open = (boolean) map.get("open");
        this.balance = (int) map.get("balance");
        this.lives = (int) map.get("lives");
        this.kothCaptures = (int) map.get("kothCaptures");
        this.conquestCaptures = (int) map.get("conquestCaptures");
        this.points = (int) map.get("points");
        this.deathsUntilRaidable = (double) map.get("deathsUntilRaidable");
        this.regenCooldownTimestamp = Long.parseLong((String) map.get("regenCooldownTimestamp"));
        this.lastDtrUpdateTimestamp = Long.parseLong((String) map.get("lastDtrUpdateTimestamp"));
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        Map<String, String> relationSaveMap = new HashMap<>(this.relations.size());
        for (Map.Entry<UUID, Relation> entry : this.relations.entrySet()) {
            relationSaveMap.put(entry.getKey().toString(), entry.getValue().name());
        }
        map.put("relations", relationSaveMap);
        Map<String, String> requestedRelationsSaveMap = new HashMap<>(this.requestedRelations.size());
        for (Map.Entry<UUID, Relation> entry2 : this.requestedRelations.entrySet()) {
            requestedRelationsSaveMap.put(entry2.getKey().toString(), entry2.getValue().name());
        }
        map.put("requestedRelations", requestedRelationsSaveMap);
        Set<Map.Entry<UUID, FactionMember>> entrySet = this.members.entrySet();
        Map<String, FactionMember> saveMap = new LinkedHashMap<>(this.members.size());
        for (Map.Entry<UUID, FactionMember> entry3 : entrySet) {
            saveMap.put(entry3.getKey().toString(), entry3.getValue());
        }
        map.put("members", saveMap);
        map.put("invitedPlayerNames", new ArrayList<>(this.invitedPlayerNames));
        if (this.home != null) {
            map.put("home", this.home);
        }
        if (this.announcement != null) {
            map.put("announcement", this.announcement);
        }
        map.put("open", this.open);
        map.put("balance", this.balance);
        map.put("lives", this.lives);
        map.put("kothCaptures", this.kothCaptures);
        map.put("conquestCaptures", this.conquestCaptures);
        map.put("points", this.points);
        map.put("deathsUntilRaidable", this.deathsUntilRaidable);
        map.put("regenCooldownTimestamp", Long.toString(this.regenCooldownTimestamp));
        map.put("lastDtrUpdateTimestamp", Long.toString(this.lastDtrUpdateTimestamp));
        return map;
    }
    
    public boolean addMember(CommandSender sender, @Nullable Player player, UUID playerUUID, FactionMember factionMember) {
        if (this.members.containsKey(playerUUID)) {
            return false;
        }
        FactionPlayerJoinEvent eventPre = new FactionPlayerJoinEvent(sender, player, playerUUID, this);
        Bukkit.getPluginManager().callEvent(eventPre);
        if (eventPre.isCancelled()) {
            return false;
        }
        this.lastDtrUpdateTimestamp = System.currentTimeMillis();
        this.invitedPlayerNames.remove(factionMember.getName());
        this.members.put(playerUUID, factionMember);
        Bukkit.getPluginManager().callEvent(new FactionPlayerJoinedEvent(sender, player, playerUUID, this));
        return true;
    }
    
    public boolean removeMember(CommandSender sender, @Nullable Player player, UUID playerUUID, boolean kick, boolean force) {
        if (!this.members.containsKey(playerUUID)) {
            return true;
        }
        FactionPlayerLeaveEvent preEvent = new FactionPlayerLeaveEvent(sender, player, playerUUID, this, FactionLeaveEnum.LEAVE, kick, false);
        Bukkit.getPluginManager().callEvent(preEvent);
        if (preEvent.isCancelled()) {
            return false;
        }
        this.members.remove(playerUUID);
        this.setDeathsUntilRaidable(Math.min(this.deathsUntilRaidable, this.getMaximumDeathsUntilRaidable()));
        FactionPlayerLeftEvent event = new FactionPlayerLeftEvent(sender, player, playerUUID, this, FactionLeaveEnum.LEAVE, kick, false);
        Bukkit.getPluginManager().callEvent(event);
        return true;
    }
    
    public Collection<UUID> getAllied() {
        return Maps.filterValues(this.relations, relation -> relation == Relation.ALLY).keySet();
    }
    
    public List<PlayerFaction> getAlliedFactions() {
        Collection<UUID> allied = this.getAllied();
        Iterator<UUID> iterator = allied.iterator();
        List<PlayerFaction> results = new ArrayList<>(allied.size());
        while (iterator.hasNext()) {
            Faction faction = RevampHCF.getInstance().getFactionManager().getFaction(iterator.next());
            if (faction instanceof PlayerFaction) {
                results.add((PlayerFaction)faction);
            }
            else {
                iterator.remove();
            }
        }
        return results;
    }

    public Map<UUID, FactionMember> getMembers() {
        return ImmutableMap.copyOf(this.members);
    }
    
    public Set<Player> getOnlinePlayers() {
        return this.getOnlinePlayers(null);
    }
    
    public int getOnlinePlayersAmount() {
        return this.getOnlinePlayers().size();
    }

    @SuppressWarnings("rawtypes")
	public Set<Player> getOnlinePlayers(CommandSender sender) {
        Set<?> entrySet = this.getOnlineMembers(sender).entrySet();
        HashSet<Player> results = new HashSet<>(entrySet.size());

        for (Object o : entrySet) {
            Map.Entry entry = (Map.Entry) o;
            results.add(Bukkit.getPlayer((UUID) entry.getKey()));
        }

        return results;
    }
    
    public Map<UUID, FactionMember> getOnlineMembers() {
        return this.getOnlineMembers(null);
    }

    public Map<UUID, FactionMember> getOnlineMembers(CommandSender sender) {
        Player senderPlayer = sender instanceof Player ? ((Player) sender) : null;
        Map<UUID, FactionMember> results = new HashMap<>();
        for (Map.Entry<UUID, FactionMember> entry : members.entrySet()) {
            Player target = Bukkit.getPlayer(entry.getKey());
            if (target == null || (senderPlayer != null && !senderPlayer.canSee(target))) {
                continue;
            }

            results.put(entry.getKey(), entry.getValue());
        }

        return results;
    }
    
    public FactionMember getLeader() {
        Map<UUID, FactionMember> members = this.members;
        for (Map.Entry<UUID, FactionMember> entry : members.entrySet()) {
            if (entry.getValue().getRole() == Role.LEADER) {
                return entry.getValue();
            }
        }
        return null;
    }
    
    @Deprecated
    public FactionMember getMember(String memberName) {
        UUID uuid = Bukkit.getOfflinePlayer(memberName).getUniqueId();
        if (uuid == null) {
            return null;
        }
        return this.members.get(uuid);
    }
    
    public FactionMember getMember(Player player) {
        return this.getMember(player.getUniqueId());
    }
    
    public FactionMember getMember(UUID memberUUID) {
        return this.members.get(memberUUID);
    }
    
    public Location getHome() {
        return (this.home == null) ? null : this.home.getLocation();
    }
    
    public void setHome(@Nullable Location home) {
        if (home == null && this.home != null) {
            HomeHandler timer = RevampHCF.getInstance().getHandlerManager().getTimerManager().getHomeHandler();
            for (Player player : this.getOnlinePlayers()) {
                Location destination = timer.getDestination(player);
                if (Objects.equal(destination, this.home.getLocation())) {
                    timer.clearCooldown(player);
                    player.sendMessage(CC.RED + "Your home was unset, so your Home timer has been cancelled");
                }
            }
        }
        this.home = ((home == null) ? null : new PersistableLocation(home));
    }

    @Override
    public boolean isRaidable() {
        return this.deathsUntilRaidable <= 0.0;
    }
    
    @Override
    public double getDeathsUntilRaidable() {
        return this.getDeathsUntilRaidable(true);
    }
    
    @Override
    public double getMaximumDeathsUntilRaidable() {
        if (this.members.size() == 1) {
            return 1.1;
        }
        return Math.min(RevampHCF.getInstance().getConfiguration().getMaxDtr(), this.members.size() * 0.9);
    }
    
    public double getDeathsUntilRaidable(boolean updateLastCheck) {
        if (updateLastCheck) {
            this.updateDeathsUntilRaidable();
        }
        return this.deathsUntilRaidable;
    }
    
    public ChatColor getDtrColour() {
        this.updateDeathsUntilRaidable();
        if (this.deathsUntilRaidable < 0.0) {
            return ChatColor.RED;
        }
        if (this.deathsUntilRaidable < 1.0) {
            return ChatColor.YELLOW;
        }
        return ChatColor.GREEN;
    }
    
    public ChatColor getTabDtrColour() {
        this.updateDeathsUntilRaidable();
        if (this.deathsUntilRaidable < 0.0) {
            return ChatColor.RED;
        }
        if (this.deathsUntilRaidable < 1.0) {
            return ChatColor.YELLOW;
        }
        return ChatColor.GRAY;
    }
    
    private void updateDeathsUntilRaidable() {
        if (this.getRegenStatus() == RegenStatus.REGENERATING) {
            long now = System.currentTimeMillis();
            long millisPassed = now - this.lastDtrUpdateTimestamp;
            if (millisPassed >= RevampHCF.getInstance().getConfiguration().getDtrIncrementBetweenUpdate()) {
                long remainder = millisPassed % RevampHCF.getInstance().getConfiguration().getDtrUpdate();
                int multiplier = (int)((millisPassed + remainder) / RevampHCF.getInstance().getConfiguration().getDtrUpdate());
                double increase = multiplier * RevampHCF.getInstance().getConfiguration().getDtrIncrementBetweenUpdate();
                this.lastDtrUpdateTimestamp = now - remainder;
                this.setDeathsUntilRaidable(this.deathsUntilRaidable + increase);
            }
        }
    }
    
    @Override
    public double setDeathsUntilRaidable(double deathsUntilRaidable) {
        return this.setDeathsUntilRaidable(deathsUntilRaidable, true);
    }
    
    private double setDeathsUntilRaidable(double deathsUntilRaidable, boolean limit) {
        deathsUntilRaidable = Math.round(deathsUntilRaidable * 100.0) / 100.0;
        if (limit) {
            deathsUntilRaidable = Math.min(deathsUntilRaidable, this.getMaximumDeathsUntilRaidable());
        }
        if (Math.abs(deathsUntilRaidable - this.deathsUntilRaidable) != 0.0) {
            FactionDTRChangeEvent event = new FactionDTRChangeEvent(FactionDTRChangeEvent.DtrUpdateCause.REGENERATION, this, this.deathsUntilRaidable, deathsUntilRaidable);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                deathsUntilRaidable = Math.round(event.getNewDtr() * 100.0) / 100.0;
                if (deathsUntilRaidable > 0.0 && this.deathsUntilRaidable <= 0.0) {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&cFaction &l" + this.getName() + "&c is now raidable."));
                }
                this.lastDtrUpdateTimestamp = System.currentTimeMillis();
                return this.deathsUntilRaidable = deathsUntilRaidable;
            }
        }
        return this.deathsUntilRaidable;
    }

    @Override
    public long getRemainingRegenerationTime() {
        return (this.regenCooldownTimestamp == 0L) ? 0L : (this.regenCooldownTimestamp - System.currentTimeMillis());
    }
    
    @Override
    public void setRemainingRegenerationTime(long millis) {
        long systemMillis = System.currentTimeMillis();
        this.regenCooldownTimestamp = systemMillis + millis;
        this.lastDtrUpdateTimestamp = systemMillis + RevampHCF.getInstance().getConfiguration().getDtrUpdate() * 2L;
    }
    
    @Override
    public RegenStatus getRegenStatus() {
        if (this.getRemainingRegenerationTime() > 0L) {
            return RegenStatus.PAUSED;
        }
        if (this.getMaximumDeathsUntilRaidable() > this.deathsUntilRaidable) {
            return RegenStatus.REGENERATING;
        }
        return RegenStatus.FULL;
    }
    
    @Override
    public void printDetails(CommandSender sender) {
        String leaderName = null;
        Set<String> allyNames = new HashSet<>(RevampHCF.getInstance().getConfiguration().getMaxAllysPerFaction());
        for (Map.Entry<UUID, Relation> entry : this.relations.entrySet()) {
            Faction faction = RevampHCF.getInstance().getFactionManager().getFaction(entry.getKey());
            if (faction instanceof PlayerFaction) {
                PlayerFaction ally = (PlayerFaction)faction;
                allyNames.add(ally.getDisplayName(sender) + CC.GOLD + '[' + CC.WHITE + ally.getOnlinePlayers(sender).size() + CC.GRAY + '/' + CC.WHITE + ally.members.size() + CC.GOLD + ']');
            }
        }
        HashSet<String> memberNames = new HashSet<>();
        HashSet<String> captainNames = new HashSet<>();
        HashSet<String> coleaderNames = new HashSet<>();
        for (Map.Entry<UUID, FactionMember> entry2 : this.members.entrySet()) {
            FactionMember factionMember = entry2.getValue();
            Player target = factionMember.toOnlinePlayer();
            HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(entry2.getKey());
            int kills;
            String color;
            if (target == null) {
                kills = 0;
                color = CC.GRAY;
            }
            else {
                if (data == null) return;
                kills = data.getKills();
                color = CC.GREEN;
            }
            if (!RevampHCF.getInstance().getConfiguration().isKitMap()) {
                File targetFile = new File(RevampHCF.getInstance().getHandlerManager().getDeathBanHandler().getDeathbansFolder(), factionMember.getUniqueID().toString() + ".yml");
                if (targetFile.exists()) {
                    color = CC.RED;
                }
            }
            String memberName = color + factionMember.getName() + CC.AQUA + '[' + CC.GREEN + kills + CC.AQUA + ']';
            switch (factionMember.getRole()) {
                default: {
                    continue;
                }
                case LEADER: {
                    leaderName = memberName;
                    continue;
                }
                case COLEADER: {
                    coleaderNames.add(memberName);
                    continue;
                }
                case CAPTAIN: {
                    captainNames.add(memberName);
                    continue;
                }
                case MEMBER: {
                    memberNames.add(memberName);
                }
            }
        }
        sender.sendMessage(CC.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(CC.translate("&c" + this.getDisplayName(sender) + " &7(" + this.getOnlinePlayers(sender).size() + "/" + this.members.size() + ") &7" + CC.STICK + " &bHQ: " + ((this.home == null) ? "&7None" : ("&c" + this.home.getLocation().getBlockX() + "&c, &c" + this.home.getLocation().getBlockZ()))));
        if (!allyNames.isEmpty()) {
            sender.sendMessage(CC.translate("&bAllies: &c" + StringUtils.join(allyNames, CC.GRAY + ", ")));
        }
        if (leaderName != null) {
            sender.sendMessage(CC.translate("&bLeader: &c" + leaderName));
        }
        if (!coleaderNames.isEmpty()) {
            sender.sendMessage(CC.translate("&bCo-Leaders: &c" + StringUtils.join(coleaderNames, CC.GRAY + ", ")));
        }
        if (!captainNames.isEmpty()) {
            sender.sendMessage(CC.translate("&bCaptains: &c" + StringUtils.join(captainNames, CC.GRAY + ", ")));
        }
        if (!memberNames.isEmpty()) {
            sender.sendMessage(CC.translate("&bMembers: &c" + StringUtils.join(memberNames, CC.GRAY + ", ")));
        }
        if (sender instanceof Player) {
            if (this.announcement != null) {
                sender.sendMessage(CC.translate("&bAnnouncement: &d" + this.announcement));
            }
            else {
                sender.sendMessage(CC.translate("&bAnnouncement: &7None"));
            }
        }
        sender.sendMessage(CC.translate("&bBalance: &a$" + this.balance));
        if (RevampHCF.getInstance().getConfig().getBoolean("F_OPEN_IN_COMBAT_ENABLED") && !RevampHCF.getInstance().getConfiguration().isKitMap()) {
            sender.sendMessage(CC.translate("&bLives: &c\u2764 " + this.lives + " &7 " + CC.STICK + " &bStatus:" + (this.isOpen() ? " &a(Open)" : " &c(Closed)")));
        }
        else if (!RevampHCF.getInstance().getConfig().getBoolean("F_OPEN_IN_COMBAT_ENABLED") && !RevampHCF.getInstance().getConfiguration().isKitMap()) {
            sender.sendMessage(CC.translate("&bLives: &c\u2764 " + this.lives + " &7 "));
        }
        else if (RevampHCF.getInstance().getConfiguration().isKitMap()) {
            sender.sendMessage(CC.translate("&bStatus:" + (this.isOpen() ? " &a(Open)" : " &c(Closed)")));
        }
        sender.sendMessage(CC.translate("&bPoints: &c" + this.points));
        if (this.kothCaptures > 0) {
            sender.sendMessage(CC.translate("&bKOTH Captures: &c" + this.getKothCaptures()));
        }
        if (this.conquestCaptures > 0) {
            sender.sendMessage(CC.translate("&bConquest Captures: &c" + this.getConquestCaptures()));
        }
        sender.sendMessage(CC.translate("&bDeaths until Raidable: " + this.getRegenStatus().getSymbol() + this.getDtrColour() + JavaUtils.format(this.getDeathsUntilRaidable())));
        long dtrRegenRemaining = this.getRemainingRegenerationTime();
        if (dtrRegenRemaining > 0L) {
            sender.sendMessage(CC.translate("&bTime Until Regen: &9" + DurationFormatUtils.formatDurationWords(dtrRegenRemaining, true, true)));
        }
        sender.sendMessage(CC.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
    }
    
    public void broadcast(String message) {
        this.broadcast(CC.translate(message), PlayerFaction.EMPTY_UUID_ARRAY);
    }
    
    public void broadcast(String[] messages) {
        this.broadcast(messages, PlayerFaction.EMPTY_UUID_ARRAY);
    }
    
    public void broadcast(String message, @Nullable UUID... ignore) {
        this.broadcast(new String[] { message }, ignore);
    }
    
    public void broadcast(String[] messages, UUID... ignore) {
        Preconditions.checkNotNull((Object)messages, "Messages cannot be null");
        Preconditions.checkArgument(messages.length > 0, "Message array cannot be empty");
        Collection<Player> players = this.getOnlinePlayers();
        Collection<UUID> ignores = ((ignore.length == 0) ? Collections.emptySet() : Sets.newHashSet(ignore));
        for (Player player : players) {
            if (!ignores.contains(player.getUniqueId())) {
                player.sendMessage(messages);
            }
        }
    }
}

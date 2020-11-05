package eu.revamp.hcf.factions.utils;

import com.google.common.base.Preconditions;
import eu.revamp.hcf.factions.utils.struction.Role;
import eu.revamp.hcf.kit.Kit;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.hcf.utils.inventory.GenericUtils;
import eu.revamp.system.enums.ChatChannel;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.TObjectLongMap;
import lombok.Getter;
import lombok.Setter;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.map.hash.TObjectLongHashMap;
import gnu.trove.procedure.TObjectIntProcedure;
import gnu.trove.procedure.TObjectLongProcedure;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
@Getter @Setter
public class FactionMember implements ConfigurationSerializable {
    private UUID uniqueID;
    private ChatChannel chatChannel;
    private Role role;
    private TObjectLongMap<UUID> kitCooldownMap;
    private TObjectIntMap<UUID> kitUseMap;
    
    public FactionMember(Player player, ChatChannel chatChannel, Role role) {
        this.uniqueID = player.getUniqueId();
        this.chatChannel = chatChannel;
        this.role = role;
        this.kitUseMap = new TObjectIntHashMap<>();
        this.kitCooldownMap = new TObjectLongHashMap<>();
    }
    
    public FactionMember(Map<String, Object> map) {
        this.uniqueID = UUID.fromString((String) map.get("uniqueID"));
        this.chatChannel = Utils.getIfPresent(ChatChannel.class, (String) map.get("chatChannel")).orElse(ChatChannel.PUBLIC);
        this.role = Utils.getIfPresent(Role.class, (String) map.get("role")).orElse(Role.MEMBER);
        this.kitUseMap = new TObjectIntHashMap<>();
        this.kitCooldownMap = new TObjectLongHashMap<>();
        for (Map.Entry<String, Integer> entry : GenericUtils.castMap(map.get("kit-use-map"), String.class, Integer.class).entrySet()) {
            this.kitUseMap.put(UUID.fromString(entry.getKey()), entry.getValue());
        }
        for (Map.Entry<String, String> entry2 : GenericUtils.castMap(map.get("kit-cooldown-map"), String.class, String.class).entrySet()) {
            this.kitCooldownMap.put(UUID.fromString(entry2.getKey()), Long.parseLong(entry2.getValue()));
        }
    }
    
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("uniqueID", this.uniqueID.toString());
        map.put("chatChannel", this.chatChannel.name());
        map.put("role", this.role.name());

        Map<String, Integer> kitUseSaveMap = new HashMap<>(this.kitUseMap.size());
        this.kitUseMap.forEachEntry((uuid, value) -> {
            kitUseSaveMap.put(uuid.toString(), value);
            return true;
        });
        new TObjectIntProcedure<UUID>() {
            public boolean execute(UUID uuid, int value) {
                kitUseSaveMap.put(uuid.toString(), value);
                return true;
            }
        };
        Map<String, String> kitCooldownSaveMap = new HashMap<>(this.kitCooldownMap.size());
        this.kitCooldownMap.forEachEntry((uuid, value) -> {
            kitCooldownSaveMap.put(uuid.toString(), Long.toString(value));
            return true;
        });
        new TObjectLongProcedure<UUID>() {
            public boolean execute(UUID uuid, long value) {
                kitCooldownSaveMap.put(uuid.toString(), Long.toString(value));
                return true;
            }
        };
        map.put("kit-use-map", kitUseSaveMap);
        map.put("kit-cooldown-map", kitCooldownSaveMap);
        return map;
    }
    
    public String getName() {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(this.uniqueID);
        return (offlinePlayer.hasPlayedBefore() || offlinePlayer.isOnline()) ? offlinePlayer.getName() : null;
    }
    
    public void setChatChannel(ChatChannel chatChannel) {
        Preconditions.checkNotNull(chatChannel, "ChatChannel cannot be null");
        this.chatChannel = chatChannel;
    }
    public Player toOnlinePlayer() {
        return Bukkit.getPlayer(this.uniqueID);
    }
    
    public OfflinePlayer toOfflinePlayer() {
        return Bukkit.getOfflinePlayer(this.uniqueID);
    }

    public long getRemainingKitCooldown(Kit kit) {
        long remaining = this.kitCooldownMap.get(kit.getUniqueID());
        if (remaining == this.kitCooldownMap.getNoEntryValue()) {
            return 0L;
        }
        return remaining - System.currentTimeMillis();
    }

    public void updateKitCooldown(Kit kit) {
        this.kitCooldownMap.put(kit.getUniqueID(), System.currentTimeMillis() + kit.getDelayMillis());
    }

    public int getKitUses(Kit kit) {
        int result = this.kitUseMap.get(kit.getUniqueID());
        return (result == this.kitUseMap.getNoEntryValue()) ? 0 : result;
    }
    public int incrementKitUses(Kit kit) {
        return this.kitUseMap.adjustOrPutValue(kit.getUniqueID(), 1, 1);
    }
}

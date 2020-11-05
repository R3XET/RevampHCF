package eu.revamp.hcf.factions.utils.games;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.utils.zone.CaptureZone;
import eu.revamp.spigot.utils.chat.color.CC;
import lombok.Getter;
import lombok.Setter;
import eu.revamp.hcf.factions.type.PlayerFaction;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.apache.commons.lang.time.DurationFormatUtils;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import eu.revamp.hcf.utils.inventory.BukkitUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.games.GameType;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
@SuppressWarnings("unchecked")
public class KothFaction extends CapturableFaction implements ConfigurationSerializable {
    @Getter @Setter private CaptureZone captureZone;
    
    public KothFaction(String name) {
        super(name);
    }
    
    public KothFaction(Map<String, Object> map) {
        super(map);
        this.captureZone = (CaptureZone) map.get("captureZone");
    }
    
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("captureZone", this.captureZone);
        return map;
    }
    
    public List<CaptureZone> getCaptureZones() {
        return (List<CaptureZone>)((this.captureZone == null) ? ImmutableList.of() : ImmutableList.of(this.captureZone));
    }
    
    public GameType getEventType() {
        return GameType.KOTH;
    }
    @SuppressWarnings("deprecation")
    public void printDetails(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(CC.translate(this.getDisplayName(sender)));
        for (ClaimZone claim : this.claims) {
            Location location = claim.getCenter();
            sender.sendMessage(ChatColor.YELLOW + "Location: " + ChatColor.RESET + '(' + ENVIRONMENT_MAPPINGS.get(location.getWorld().getEnvironment()) + ", " + location.getBlockX() + " " + CC.STICK + " " + location.getBlockZ() + ')');
        }
        if (this.captureZone != null) {
            long remainingCaptureMillis = this.captureZone.getRemainingCaptureMillis();
            long defaultCaptureMillis = this.captureZone.getDefaultCaptureMillis();
            if (remainingCaptureMillis > 0L && remainingCaptureMillis != defaultCaptureMillis) {
                sender.sendMessage(ChatColor.YELLOW + "Remaining Time: " + ChatColor.RESET + DurationFormatUtils.formatDurationWords(remainingCaptureMillis, true, true));
            }
            sender.sendMessage(ChatColor.YELLOW + "Capture Delay: " + ChatColor.RESET + this.captureZone.getDefaultCaptureWords());
            if (this.captureZone.getCappingPlayer() != null && sender.hasPermission("hcf.staff")) {
                Player capping = this.captureZone.getCappingPlayer();
                PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(capping);
                String factionTag = "[" + ((playerFaction == null) ? "*" : playerFaction.getName()) + "]";
                sender.sendMessage(ChatColor.YELLOW + "Current Capper: " + ChatColor.RESET + capping.getName() + ChatColor.RESET + factionTag);
            }
        }
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
    }
}

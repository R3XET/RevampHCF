package eu.revamp.hcf.handlers.hcf;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.spigot.utils.chat.color.CC;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.UUID;

public class ItemPickupPvPTimerHandler extends Handler implements Listener {

    @Getter @Setter private String item_pickup_message_meta_key;
    @Getter @Setter private long item_pickup_message_delay;

    public ItemPickupPvPTimerHandler(RevampHCF instance) {
        super(instance);
        setItem_pickup_message_meta_key("pickupMessageDelay");
        setItem_pickup_message_delay(1250L);
    }
    @Override
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onItemPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        long remaining = this.getInstance().getHandlerManager().getPvpTimerHandler().getRemaining(player);
        HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        if (remaining > 0L && data.getPvpTimerCooldown() > 0) {
            if (RevampHCF.getInstance().getConfig().getBoolean("ALLOW_ITEM_PICKUP_PVPTIMER")){
                return;
            }
            UUID itemUUID = event.getItem().getUniqueId();
            Long delay = this.getInstance().getHandlerManager().getPvpTimerHandler().getItem_pickup_delays().get(itemUUID);
            if (delay == null) return;
            long millis = System.currentTimeMillis();
            if (delay - millis > 0L) {
                event.setCancelled(true);
                List<MetadataValue> value = player.getMetadata(getItem_pickup_message_meta_key());
                if (value != null && !value.isEmpty() && value.get(0).asLong() - millis <= 0L) {
                    player.setMetadata(getItem_pickup_message_meta_key(), new FixedMetadataValue(RevampHCF.getInstance(), millis + getItem_pickup_message_delay()));
                    player.sendMessage(CC.translate("&cYou cannot pick this item up for another &l" + DurationFormatUtils.formatDurationWords(remaining, true, true) + " &cas your &a&lPvP Timer&c is active."));
                }
            }
            else {
                this.getInstance().getHandlerManager().getPvpTimerHandler().getItem_pickup_delays().remove(itemUUID);
            }
        }
    }
}

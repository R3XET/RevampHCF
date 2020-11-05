package eu.revamp.hcf.handlers.blocks;

import eu.revamp.packages.utils.items.ItemUtils;
import lombok.Getter;
import org.bukkit.event.player.PlayerQuitEvent;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import eu.revamp.hcf.RevampHCF;
import java.util.UUID;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class FoundDiamondsHandler extends Handler implements Listener
{
    @Getter private final List<UUID> players;

    public FoundDiamondsHandler(RevampHCF plugin) {
        super(plugin);
        this.players = new ArrayList<>();
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, RevampHCF.getInstance());
    }
    
    public void toggleAlerts(Player player) {
        if (this.players.contains(player.getUniqueId())) {
            this.players.remove(player.getUniqueId());
            player.sendMessage(CC.translate("&efoundOre alerts recieveing &aEnabled&e."));
        }
        else {
            this.players.add(player.getUniqueId());
            player.sendMessage(CC.translate("&efoundOre alerts recieveing &cDisabled&e."));
        }
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block.getType().equals(Material.DIAMOND_ORE)) {
            block.setMetadata("foundOre", new FixedMetadataValue(RevampHCF.getInstance(), true));
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (player.getGameMode() == GameMode.CREATIVE)
            return;
        if (block.getType().equals(Material.DIAMOND_ORE) && !block.hasMetadata("foundOre")) {
            int count = 0;
            for (int x = -3; x < 4; x++) {
                for (int y = -3; y < 4; y++) {
                    for (int z = -3; z < 4; z++) {
                        Block newBlock = block.getRelative(x, y, z);
                        if (newBlock.getType().equals(Material.DIAMOND_ORE) && !newBlock.hasMetadata("foundOre")) {
                            count++;
                            newBlock.setMetadata("foundOre", new FixedMetadataValue(RevampHCF.getInstance(), Boolean.TRUE));
                        }
                    }
                }
            }
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (!this.players.contains(online.getUniqueId()))
                    online.sendMessage(CC.translate("&f[FD] &b" + player.getName() + " has found " + count + " diamond" + ((count == 1) ? "" : "s") + "."));
            }
        }
    }

        /*
    private void countDiamonds(Block blockm, AtomicInteger count){
        for (int[] face: ItemUtils.BLOCK_RELATIVES){
            if (count.get() >= 50) return;

            Block newBlock = blockm.getRelative(face[0], face[1], face[3]);
            if (newBlock.getType() != Material.DIAMOND_ORE || newBlock.hasMetadata("foundOre")) continue;

            count.getAndIncrement();
            newBlock.setMetadata("foundOre", new FixedMetadataValue(RevampHCF.getInstance(), true));
            this.countDiamonds(newBlock, count);
        }
    }*/

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.players.remove(player.getUniqueId());
    }
}

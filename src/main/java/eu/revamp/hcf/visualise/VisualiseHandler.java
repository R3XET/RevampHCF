package eu.revamp.hcf.visualise;

import java.util.Collections;
import javax.annotation.Nullable;

import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import org.bukkit.Chunk;
import org.bukkit.World;
import java.util.ArrayList;
import org.bukkit.Material;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import eu.revamp.hcf.games.cuboid.Cuboid;
import com.google.common.collect.Maps;
import com.google.common.base.Predicate;
import java.util.HashMap;
import java.util.Map;
import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import com.google.common.collect.HashBasedTable;
import org.bukkit.Location;
import java.util.UUID;
import com.google.common.collect.Table;

public class VisualiseHandler
{
    @Getter private final Table<UUID, Location, VisualBlock> storedVisualises;
    
    public VisualiseHandler() {
        this.storedVisualises = HashBasedTable.create();
    }

    @Deprecated
    public VisualBlock getVisualBlockAt(Player player, int x, int y, int z) throws NullPointerException {
        return this.getVisualBlockAt(player, new Location(player.getWorld(), x, y, z));
    }
    
    public VisualBlock getVisualBlockAt(Player player, Location location) throws NullPointerException {
        Preconditions.checkNotNull(player, "Player cannot be null");
        Preconditions.checkNotNull(location, "Location cannot be null");
        synchronized (this.storedVisualises) {
            // monitorexit(this.storedVisualises)
            return this.storedVisualises.get(player.getUniqueId(), location);
        }
    }
    
    public Map<Location, VisualBlock> getVisualBlocks(Player player) {
        synchronized (this.storedVisualises) {
            // monitorexit(this.storedVisualises)
            return new HashMap<>(this.storedVisualises.row(player.getUniqueId()));
        }
    }

    public Map<Location, VisualBlock> getVisualBlocks(Player player, VisualType visualType) {
        return Maps.filterValues(this.getVisualBlocks(player), visualBlock -> visualType ==  visualBlock.getVisualType());
    }
    
    public LinkedHashMap<Location, VisualBlockData> generate(Player player, Cuboid cuboid, VisualType visualType, boolean canOverwrite) {
        Collection<Location> locations = new HashSet<>(cuboid.getSizeX() * cuboid.getSizeY() * cuboid.getSizeZ());
        Iterator<Location> iterator = cuboid.locationIterator();
        while (iterator.hasNext()) {
            locations.add(iterator.next());
        }
        return this.generate(player, locations, visualType, canOverwrite);
    }
    
    public LinkedHashMap<Location, VisualBlockData> generateAsync(Player player, Cuboid cuboid, VisualType visualType, boolean canOverwrite) {
        Collection<Location> locations = new HashSet<>(cuboid.getSizeX() * cuboid.getSizeY() * cuboid.getSizeZ());
        Iterator<Location> iterator = cuboid.locationIterator();
        while (iterator.hasNext()) {
            locations.add(iterator.next());
        }
        return this.generateAsync(player, locations, visualType, canOverwrite);
    }
    @SuppressWarnings("deprecation")
    public LinkedHashMap<Location, VisualBlockData> generate(Player player, Iterable<Location> locations, VisualType visualType, boolean canOverwrite) {
        synchronized (this.storedVisualises) {
            LinkedHashMap<Location, VisualBlockData> results = new LinkedHashMap<>();
            ArrayList<VisualBlockData> filled = visualType.blockFiller().bulkGenerate(player, locations);
            if (filled != null) {
                int count = 0;
                for (Location location : locations) {
                    if (!canOverwrite && this.storedVisualises.contains(player.getUniqueId(), location)) {
                        continue;
                    }
                    Material previousType = location.getBlock().getType();
                    if (previousType.isSolid()) {
                        continue;
                    }
                    if (previousType != Material.AIR) {
                        continue;
                    }
                    VisualBlockData visualBlockData = filled.get(count++);
                    results.put(location, visualBlockData);
                    player.sendBlockChange(location, visualBlockData.getBlockType(), visualBlockData.getData());
                    this.storedVisualises.put(player.getUniqueId(), location, new VisualBlock(visualType, visualBlockData, location));
                }
            }
            // monitorexit(this.storedVisualises)
            return results;
        }
    }
    @SuppressWarnings("deprecation")
    public LinkedHashMap<Location, VisualBlockData> generateAsync(Player player, Iterable<Location> locations, VisualType visualType, boolean canOverwrite) {
        synchronized (this.storedVisualises) {
            LinkedHashMap<Location, VisualBlockData> results = new LinkedHashMap<>();
            ArrayList<VisualBlockData> filled = visualType.blockFiller().bulkGenerate(player, locations);
            if (filled != null) {
                for (Location location : locations) {
                    if (!canOverwrite && this.storedVisualises.contains(player.getUniqueId(), location)) {
                        continue;
                    }
                    location.getWorld().getChunkAtAsync(location, new World.ChunkLoadCallback() {
                        int count = 0;
                        
                        public void onLoad(Chunk chunk) {
                            Material previousType = CraftMagicNumbers.getMaterial(((CraftChunk)chunk).getHandle().getType((BlockPosition)location.getBlock().getChunk()));
                            if (previousType.isSolid() || previousType != Material.AIR) return;
                            VisualBlockData visualBlockData = filled.get(this.count++);
                            results.put(location, visualBlockData);
                            player.sendBlockChange(location, visualBlockData.getBlockType(), visualBlockData.getData());
                            VisualiseHandler.this.storedVisualises.put(player.getUniqueId(), location, new VisualBlock(visualType, visualBlockData, location));
                        }
                    });
                }
            }
            // monitorexit(this.storedVisualises)
            return results;
        }
    }
    
    public boolean clearVisualBlock(Player player, Location location) {
        return this.clearVisualBlock(player, location, true);
    }
    @SuppressWarnings("deprecation")
    public boolean clearVisualBlock(Player player, Location location, boolean sendRemovalPacket) {
        synchronized (this.storedVisualises) {
            VisualBlock visualBlock = this.storedVisualises.remove(player.getUniqueId(), location);
            if (sendRemovalPacket && visualBlock != null) {
                Block block = location.getBlock();
                VisualBlockData visualBlockData = visualBlock.getBlockData();
                if (visualBlockData.getBlockType() != block.getType() || visualBlockData.getData() != block.getData()) {
                    player.sendBlockChange(location, block.getType(), block.getData());
                }
                // monitorexit(this.storedVisualises)
                return true;
            }
        }
        // monitorexit(this.storedVisualises)
        return false;
    }
    
    public Map<Location, VisualBlock> clearVisualBlocks(Player player) {
        return this.clearVisualBlocks(player, null, null);
    }
    
    public Map<Location, VisualBlock> clearVisualBlocks(Player player, @Nullable VisualType visualType, @Nullable Predicate<VisualBlock> predicate) {
        return this.clearVisualBlocks(player, visualType, predicate, true);
    }

    public Map<Location, VisualBlock> clearVisualBlocks(Player player, @Nullable VisualType visualType, @Nullable Predicate<VisualBlock> predicate, boolean sendRemovalPackets) {
        synchronized (this.storedVisualises) {
            if (!this.storedVisualises.containsRow(player.getUniqueId())) {
                // monitorexit(this.storedVisualises)
                return Collections.emptyMap();
            }
            Map<Location, VisualBlock> results = new HashMap<>(this.storedVisualises.row(player.getUniqueId()));
            Map<Location, VisualBlock> removed = new HashMap<>();
            for (Map.Entry<Location, VisualBlock> entry : results.entrySet()) {
                VisualBlock visualBlock = entry.getValue();
                if ((predicate == null || predicate.apply(visualBlock)) && (visualType == null || visualBlock.getVisualType() == visualType)) {
                    Location location = entry.getKey();
                    if (removed.put(location, visualBlock) != null) {
                        continue;
                    }
                    this.clearVisualBlock(player, location, sendRemovalPackets);
                }
            }
            // monitorexit(this.storedVisualises)
            return removed;
        }
    }
}

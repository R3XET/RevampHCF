package eu.revamp.hcf.extra;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Handler;
import net.minecraft.server.v1_8_R3.BiomeBase;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;

public class Biomes extends Handler {
    public Biomes(RevampHCF instance) {
        super(instance);
    }
    public static void setupBiomes(){
        Bukkit.getConsoleSender().sendMessage("§7§m----------------------------------");
        Field biomesField;
        try {
            biomesField = BiomeBase.class.getDeclaredField("biomes");
            biomesField.setAccessible(true);
            if (biomesField.get(null) instanceof BiomeBase[]) {
                BiomeBase[] biomes = (BiomeBase[])biomesField.get(null);
                biomes[BiomeBase.DEEP_OCEAN.id] = BiomeBase.PLAINS;
                biomes[BiomeBase.OCEAN.id] = BiomeBase.FOREST;
                biomes[BiomeBase.JUNGLE.id] = BiomeBase.FOREST;
                biomes[BiomeBase.ICE_PLAINS.id] = BiomeBase.DESERT;
                biomes[BiomeBase.JUNGLE_EDGE.id] = BiomeBase.FOREST;
                biomes[BiomeBase.JUNGLE_HILLS.id] = BiomeBase.FOREST;
                biomes[BiomeBase.COLD_TAIGA.id] = BiomeBase.PLAINS;
                biomes[BiomeBase.ICE_MOUNTAINS.id] = BiomeBase.PLAINS;
                biomes[BiomeBase.FROZEN_RIVER.id] = BiomeBase.FOREST;
                biomes[BiomeBase.RIVER.id] = BiomeBase.SMALL_MOUNTAINS;
                biomes[BiomeBase.BEACH.id] = BiomeBase.FOREST;
                biomes[BiomeBase.BIRCH_FOREST.id] = BiomeBase.FOREST;
                biomes[BiomeBase.MESA.id] = BiomeBase.SMALL_MOUNTAINS;
                biomes[BiomeBase.MESA_PLATEAU.id] = BiomeBase.FOREST;
                biomes[BiomeBase.MESA_PLATEAU_F.id] = BiomeBase.FOREST;
                biomes[BiomeBase.MUSHROOM_ISLAND.id] = BiomeBase.FOREST;
                biomes[BiomeBase.MUSHROOM_SHORE.id] = BiomeBase.DESERT;
                biomes[BiomeBase.TAIGA.id] = BiomeBase.SMALL_MOUNTAINS;
                biomes[BiomeBase.TAIGA_HILLS.id] = BiomeBase.PLAINS;
                biomes[BiomeBase.EXTREME_HILLS.id] = BiomeBase.FOREST;
                biomes[BiomeBase.EXTREME_HILLS_PLUS.id] = BiomeBase.DESERT;
                biomes[BiomeBase.BIRCH_FOREST_HILLS.id] = BiomeBase.PLAINS;
                biomes[BiomeBase.COLD_TAIGA_HILLS.id] = BiomeBase.PLAINS;
                biomes[BiomeBase.DESERT_HILLS.id] = BiomeBase.DESERT;
                biomes[BiomeBase.FOREST_HILLS.id] = BiomeBase.FOREST;
                biomes[BiomeBase.MEGA_TAIGA_HILLS.id] = BiomeBase.PLAINS;
                biomes[BiomeBase.ROOFED_FOREST.id] = BiomeBase.DESERT;
                biomes[BiomeBase.SMALL_MOUNTAINS.id] = BiomeBase.FOREST;
                biomes[BiomeBase.SWAMPLAND.id] = BiomeBase.DESERT;
                biomes[BiomeBase.STONE_BEACH.id] = BiomeBase.PLAINS;
                biomes[BiomeBase.SAVANNA.id] = BiomeBase.PLAINS;
                biomes[BiomeBase.SAVANNA_PLATEAU.id] = BiomeBase.FOREST;
                biomesField.set(null, biomes);
            }
        }
        catch (Exception ignored) {}
        Bukkit.getConsoleSender().sendMessage("§bBiomes has been generated correctly");
        Bukkit.getConsoleSender().sendMessage("§7§m----------------------------------");
    }
}

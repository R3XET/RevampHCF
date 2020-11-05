package eu.revamp.hcf.visualise;

import java.util.ArrayList;

import eu.revamp.hcf.RevampHCF;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.utils.struction.Relation;

public enum VisualType {

    //TODO: Figure out a better way for filling blocks than this

    /**
     * Represents the wall approaching claims when Spawn Tagged.
     */
    SPAWN_BORDER() {
        private BlockFiller blockFiller = new BlockFiller() {

			@Override @SuppressWarnings("deprecation")
            VisualBlockData generate(Player player, Location location) {
                return new VisualBlockData(Material.STAINED_GLASS, DyeColor.RED.getData());
            }
        };

        @Override
        BlockFiller blockFiller() {
            return blockFiller;
        }
    },
    /**
     * Represents the wall approaching claims when PVP Protected.
     */
    CLAIM_BORDER() {
        private BlockFiller blockFiller = new BlockFiller() {

			@Override @SuppressWarnings("deprecation")
            VisualBlockData generate(Player player, Location location) {
                return new VisualBlockData(Material.STAINED_GLASS, DyeColor.GREEN.getData());
            }
        };

        @Override
        BlockFiller blockFiller() {
            return blockFiller;
        }
    },
    /**
     * Represents claims shown using /faction map.
     */
    SUBCLAIM_MAP() {
        private BlockFiller blockFiller = new BlockFiller() {
            @Override
            VisualBlockData generate(Player player, Location location) {
                return new VisualBlockData(Material.LOG, (byte) 1);
            }
        };

        @Override
        BlockFiller blockFiller() {
            return blockFiller;
        }
    },
    /**
     * Represents claims shown using /faction map.
     */
    CLAIM_MAP() {
        private BlockFiller blockFiller = new BlockFiller() {
            private Material[] types = new Material[]{
                    Material.SNOW_BLOCK,
                    Material.SANDSTONE,
                    Material.FURNACE,
                    Material.NETHERRACK,
                    Material.BOOKSHELF,
                    Material.LAPIS_BLOCK,
                    Material.NETHER_BRICK,
                    Material.DIAMOND_ORE,
                    Material.COAL_ORE,
                    Material.IRON_ORE,
                    Material.GOLD_ORE,
                    Material.LAPIS_ORE,
                    Material.REDSTONE_ORE,
                    Material.DIAMOND_BLOCK,
                    Material.GOLD_BLOCK,
                    Material.IRON_BLOCK,
                    Material.REDSTONE_BLOCK,
                    Material.EMERALD_BLOCK,
                    Material.COAL_BLOCK
            };

            private int materialCounter = 0;

            private int startPos, position = -1;
            private boolean flag = false;


			@Override @SuppressWarnings("deprecation")
            VisualBlockData generate(Player player, Location location) {
                if (flag) {
                    flag = false;
                    startPos = position;
                }

                int y = location.getBlockY();

                if ((startPos == y) || y % 3 == 0) {
                    return new VisualBlockData(types[materialCounter]);
                }

                Faction faction = RevampHCF.getInstance().getFactionManager().getFactionAt(location);
                return new VisualBlockData(Material.STAINED_GLASS, (faction != null ? faction.getRelation(player) : Relation.ENEMY).toDyeColour().getData());
            }

            @Override
            ArrayList<VisualBlockData> bulkGenerate(Player player, Iterable<Location> locations) {
                flag = true;
                ArrayList<VisualBlockData> result = super.bulkGenerate(player, locations);
                if (++materialCounter == types.length) {
                    materialCounter = 0;
                }

                startPos = -1;
                position = -1;
                return result;
            }
        };

        @Override
        BlockFiller blockFiller() {
            return blockFiller;
        }
    },

    CREATE_CLAIM_SELECTION() {
        private BlockFiller blockFiller = new BlockFiller() {
            @Override
            VisualBlockData generate(Player player, Location location) {
                return new VisualBlockData(location.getBlockY() % 3 != 0 ? Material.GLASS : Material.REDSTONE_BLOCK);
            }
        };

        @Override
        BlockFiller blockFiller() {
            return blockFiller;
        }
    },;

    /**
     * Gets the {@link BlockFiller} instance.
     *
     * @return the filler
     */
    abstract BlockFiller blockFiller();
}

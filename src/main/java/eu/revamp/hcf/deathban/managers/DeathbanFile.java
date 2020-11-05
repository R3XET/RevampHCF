package eu.revamp.hcf.deathban.managers;
/*
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.deathban.Deathban;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.file.ConfigFile;
import eu.revamp.hcf.games.cuboid.PersistableLocation;
import eu.revamp.spigot.utils.date.DateUtils;
import net.minecraft.util.gnu.trove.impl.Constants;
import net.minecraft.util.gnu.trove.map.TObjectIntMap;
import net.minecraft.util.gnu.trove.map.hash.TObjectIntHashMap;
import net.minecraft.util.gnu.trove.procedure.TObjectIntProcedure;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class DeathbanFile implements DeathbanManager {

	private static final int MAX_DEATHBAN_MULTIPLIER = 300;

	private final RevampHCF plugin;

	private TObjectIntMap<UUID> livesMap;
	private ConfigFile livesConfig;
    private final List<DeathbanConfigManager> deathbanConfigManager = new ArrayList<>();

	public DeathbanFile(RevampHCF plugin) {
		this.plugin = plugin;
		reloadDeathbanData();
	}

	@Override
	public TObjectIntMap<UUID> getLivesMap() {
		return livesMap;
	}

	@Override
	public int getLives(UUID uuid) {
		return livesMap.get(uuid);
	}

	@Override
	public int setLives(UUID uuid, int lives) {
		livesMap.put(uuid, lives);
		return lives;
	}

	@Override
	public int addLives(UUID uuid, int amount) {
		return livesMap.adjustOrPutValue(uuid, amount, amount);
	}

	@Override
	public int takeLives(UUID uuid, int amount) {
		return setLives(uuid, getLives(uuid) - amount);
	}

	@Override
	public double getDeathBanMultiplier(Player player) {
		for (int i = 5; i < MAX_DEATHBAN_MULTIPLIER; i++) {
			if (player.hasPermission("hcf.deathban.multiplier." + i)) {
				return (i) / 100.0;
			}
		}

		return 1.0D;
	}

	@Override
	public Deathban applyDeathBan(Player player, String reason) {
		Location location = player.getLocation();
		Faction factionAt = plugin.getFactionManager().getFactionAt(location);
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("deathban");

		long duration;
		if (plugin.getEotwHandler().isEndOfTheWorld()) {
			return applyDeathBan(player.getUniqueId(), new Deathban(reason, Integer.MAX_VALUE, new PersistableLocation(location), true));
		}
		duration = TimeUnit.MINUTES.toMillis(plugin.getConfiguration().getDeathbanBaseDurationMinutes());
		if (!factionAt.isDeathban()) {
			duration /= 2; // non-deathban factions should be 50% quicker
		} else if (section != null) {
	            for (String key : section.getKeys(false)) {
	                ConfigurationSection s = section.getConfigurationSection(key);
	                deathbanConfigManager.add(new DeathbanConfigManager(key, s.getString("deathban." + key + ".time")));

	                if (player.hasPermission(RevampHCF.getInstance().getConfig().getString("deathban." + key + ".permission"))) {
	                duration = DateUtils.parseTime(RevampHCF.getInstance().getConfig().getString("deathban." + key + ".time"));
	                }
	            }
	        }



		duration *= getDeathBanMultiplier(player);
		duration *= factionAt.getDeathbanMultiplier();

		return applyDeathBan(player.getUniqueId(), new Deathban(reason, Math.min(MAX_DEATHBAN_TIME, duration), new PersistableLocation(location), plugin.getEotwHandler().isEndOfTheWorld()));
	}

	@Override
	public Deathban applyDeathBan(UUID uuid, Deathban deathban) {
		plugin.getUserManager().getUser(uuid).setDeathban(deathban);
		return deathban;
	}

	@Override
	public void reloadDeathbanData() {
		livesConfig = new ConfigFile(plugin, "lives");
		Object object = livesConfig.get("lives");
		if (object instanceof MemorySection) {
			MemorySection section = (MemorySection) object;
			Set<String> keys = section.getKeys(false);
			livesMap = new TObjectIntHashMap<>(keys.size(), Constants.DEFAULT_LOAD_FACTOR, 0);
			for (String id : keys) {
				livesMap.put(UUID.fromString(id), livesConfig.getInt(section.getCurrentPath() + "." + id));
			}
		} else {
			livesMap = new TObjectIntHashMap<>(Constants.DEFAULT_CAPACITY, Constants.DEFAULT_LOAD_FACTOR, 0);
		}
	}

	@Override
	public void saveDeathbanData() {
		Map<String, Integer> saveMap = new LinkedHashMap<>(livesMap.size());
		livesMap.forEachEntry((uuid, i) -> {
			saveMap.put(uuid.toString(), i);
			return true;
		});

		livesConfig.set("lives", saveMap);
		livesConfig.save();
	}
}
*/
package eu.revamp.hcf.tablist;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.deathban.DeathBanHandler;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.spigot.utils.chat.color.Replacement;
import eu.revamp.spigot.utils.direction.DirectionUtils;
import me.allen.ziggurat.ZigguratAdapter;
import me.allen.ziggurat.objects.BufferedTabObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;


public class TabAdapter extends Handler implements ZigguratAdapter {

    public TabAdapter(RevampHCF plugin) {
        super(plugin);
    }

    public Set<BufferedTabObject> getSlots(Player player) {
        Set<BufferedTabObject> tabObjects = new HashSet<>();
        RevampHCF.getInstance().getTablist().getConfigurationSection("LEFT").getKeys(false).forEach(key -> tabObjects.add(new BufferedTabObject().text(this.translatePlaceHolder(RevampHCF.getInstance().getTablist().getString("LEFT." + key), player)).slot(Integer.parseInt(key))));
        RevampHCF.getInstance().getTablist().getConfigurationSection("MIDDLE").getKeys(false).forEach(key -> tabObjects.add(new BufferedTabObject().text(this.translatePlaceHolder(RevampHCF.getInstance().getTablist().getString("MIDDLE." + key), player)).slot(Integer.parseInt(key) + 20)));
        RevampHCF.getInstance().getTablist().getConfigurationSection("RIGHT").getKeys(false).forEach(key -> tabObjects.add(new BufferedTabObject().text(this.translatePlaceHolder(RevampHCF.getInstance().getTablist().getString("RIGHT." + key), player)).slot(Integer.parseInt(key) + 40)));
        RevampHCF.getInstance().getTablist().getConfigurationSection("FAR_RIGHT").getKeys(false).forEach(key -> tabObjects.add(new BufferedTabObject().text(this.translatePlaceHolder(RevampHCF.getInstance().getTablist().getString("FAR_RIGHT." + key), player)).slot(Integer.parseInt(key) + 60)));
        return tabObjects;
    }

    @Override
    public String getFooter() {
        return this.getInstance().getTablist().getString("FOOTER");
    }

    @Override
    public String getHeader() {
        return this.getInstance().getTablist().getString("HEADER");
    }

    @SuppressWarnings("deprecation")
    private String translatePlaceHolder(String source, Player player) {
        DeathBanHandler deathban = RevampHCF.getInstance().getHandlerManager().getDeathBanHandler();
        PlayerFaction playerFaction = this.getInstance().getFactionManager().getPlayerFaction(player);
        Replacement replacement = new Replacement(source);
        //Tasks.run(this.getInstance(), () -> {
            replacement.add("%player%", player.getName());
            replacement.add("%rank%", this.getInstance().getPermissionManager().getPermissionSystem().getName(player));
            replacement.add("%ping%", Utils.getPing(player));
            replacement.add("%online%", Bukkit.getOnlinePlayers().size());
            replacement.add("%max%", Bukkit.getMaxPlayers());
            replacement.add("%balance%", this.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player.getUniqueId()).getBalance());
            replacement.add("%kills%", this.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player.getUniqueId()).getKills());
            replacement.add("%deaths%", this.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player.getUniqueId()).getDeaths());
            replacement.add("%lives%", deathban.getLives(player));
            if (playerFaction == null) {
                replacement.add("%faction%", this.getInstance().getConfig().getString("WILDERNESS"));
            } else {
                replacement.add("%faction%", this.getInstance().getFactionManager().getPlayerFaction(player).getName());
            }
            replacement.add("%location%", (int) player.getLocation().getX() + ", " + (int) player.getLocation().getZ());
            replacement.add("%direction%", DirectionUtils.getCardinalDirection(player));
        //});
        return replacement.toString();
    }
}

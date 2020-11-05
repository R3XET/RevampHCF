package eu.revamp.hcf.tablist;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.Replacement;
import eu.revamp.spigot.utils.direction.DirectionUtils;
import eu.revamp.tablist.interfaces.TablistInterface;
import eu.revamp.tablist.RevampTab;
import eu.revamp.tablist.element.TablistElement;
import eu.revamp.hcf.deathban.DeathBanHandler;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.hcf.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabListHandler extends Handler implements TablistInterface {

    public TabListHandler(RevampHCF plugin) {
        super(plugin);
        new RevampTab(plugin, this, RevampHCF.getInstance().getTablist().getLong("UPDATE_TICKS"));
    }

    @Override
    public List<TablistElement> getTabElements(Player player) {
        ArrayList<TablistElement> tablistElements = new ArrayList<>();
        this.getInstance().getTablist().getConfigurationSection("LEFT").getKeys(false).forEach(key -> tablistElements.add(new TablistElement(this.translatePlaceHolder(this.getInstance().getTablist().getString("LEFT." + key), player), Integer.parseInt(key))));
        this.getInstance().getTablist().getConfigurationSection("MIDDLE").getKeys(false).forEach(key -> tablistElements.add(new TablistElement(this.translatePlaceHolder(this.getInstance().getTablist().getString("MIDDLE." + key), player), Integer.parseInt(key) + 20)));
        this.getInstance().getTablist().getConfigurationSection("RIGHT").getKeys(false).forEach(key -> tablistElements.add(new TablistElement(this.translatePlaceHolder(this.getInstance().getTablist().getString("RIGHT." + key), player), Integer.parseInt(key) + 40)));
        this.getInstance().getTablist().getConfigurationSection("FAR_RIGHT").getKeys(false).forEach(key -> tablistElements.add(new TablistElement(this.translatePlaceHolder(this.getInstance().getTablist().getString("FAR_RIGHT." + key), player), Integer.parseInt(key) + 60)));
        return tablistElements;
    }

    @Override
    public String getTabFooter(Player player) {
        return this.getInstance().getTablist().getString("FOOTER");
    }

    @Override
    public String getTabHeader(Player player) {
        return this.getInstance().getTablist().getString("HEADER");
    }

    @SuppressWarnings("deprecation")
    private String translatePlaceHolder(String source, Player player) {
        DeathBanHandler deathban = RevampHCF.getInstance().getHandlerManager().getDeathBanHandler();
        PlayerFaction playerFaction = this.getInstance().getFactionManager().getPlayerFaction(player);
        Replacement replacement = new Replacement(source);
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
        
        return replacement.toString();
    }

    /*
    @SuppressWarnings("deprecation")
    private String translateLowPlaceHolder(String source, Player player){
        DeathBanHandler deathban = RevampHCF.getInstance().getHandlerManager().getDeathBanHandler();
        PlayerFaction playerFaction = this.getInstance().getFactionManager().getPlayerFaction(player);
        Replacement replacement = new Replacement(source);
        replacement.add("%player%", player.getName());
        replacement.add("%rank%", this.getInstance().getPermissionManager().getPermissionSystem().getName(player));
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
        return replacement.toString(false);
    }


    private String translateHighPlaceHolder(String source, Player player) {
        Replacement replacement = new Replacement(source);
        replacement.add("%ping%", Utils.getPing(player));
        replacement.add("%location%", (int) player.getLocation().getX() + ", " + (int) player.getLocation().getZ());
        replacement.add("%direction%", BukkitUtils.getCardinalDirection(player));
        return replacement.toString();
    }
     */
}

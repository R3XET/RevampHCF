package eu.revamp.tablist.tabversion;

import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.tablist.manager.TablistManager;
import eu.revamp.tablist.RevampTab;
import eu.revamp.tablist.interfaces.TabVersionInterface;
import eu.revamp.tablist.util.TabUtil;
import eu.revamp.hcf.integration.implement.tinyprotocol.Reflection;
import eu.revamp.hcf.integration.implement.tinyprotocol.TinyProtocol;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.UUID;

public class TabVersionTinyProtocol implements TabVersionInterface
{
    private Class<?> playerInfo;
    private Class<?> playerInfoEnum;
    private Class<?> headerFooter;
    private Class<?> chatComponentText;
    private Class<?> entityPlayerClass;
    private RevampTab revampTab;
    private Object server;
    private Object world;
    private Object interactManager;

    public TabVersionTinyProtocol(RevampTab revampTab) {
        this.playerInfo = Reflection.getMinecraftClass("PacketPlayOutPlayerInfo");
        this.playerInfoEnum = Reflection.getMinecraftClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
        this.headerFooter = Reflection.getMinecraftClass("PacketPlayOutPlayerListHeaderFooter");
        this.chatComponentText = Reflection.getMinecraftClass("ChatComponentText");
        this.entityPlayerClass = Reflection.getMinecraftClass("EntityPlayer");
        this.revampTab = revampTab;
        try {
            this.server = Reflection.getCraftBukkitClass("CraftServer").getMethod("getServer", new Class[0]).invoke(Bukkit.getServer());
            this.world = Reflection.getCraftBukkitClass("CraftWorld").getMethod("getHandle", new Class[0]).invoke(Bukkit.getServer().getWorlds().get(0));
            this.interactManager = Reflection.getMinecraftClass("PlayerInteractManager").getDeclaredConstructors()[0].newInstance(this.world);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setup(Player player) {
        if (this.revampTab.getTablist().getTabElements(player) == null || this.revampTab.getTablist().getTabElements(player).isEmpty()) {
            return;
        }
        Scoreboard scoreboard = (player.getScoreboard() == Bukkit.getScoreboardManager().getMainScoreboard()) ? Bukkit.getScoreboardManager().getNewScoreboard() : player.getScoreboard();
        this.revampTab.getTablists().add(new TablistManager(scoreboard, player, this.revampTab));
    }

    @Override
    public void addPlayerInfo(Player player, Object ep) {
        try {
            Constructor<?> packetConstructor = this.playerInfo.getConstructors()[1];
            Object addPlayerEnum = this.playerInfoEnum.getField("ADD_PLAYER").get(null);
            Object array = Array.newInstance(this.entityPlayerClass, 1);
            Array.set(array, 0, ep);
            Object playerInfoPacket = packetConstructor.newInstance(addPlayerEnum, array);
            this.revampTab.getTinyProtocol().sendPacket(player, playerInfoPacket);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object createPlayer(Player player, String name) {
        try {
            OfflinePlayer testPlayer = TabUtil.createNewFakePlayer(UUID.randomUUID(), name);
            GameProfile gameProfileTest = new GameProfile(testPlayer.getUniqueId(), testPlayer.getName());
            Constructor<?> epConstructor = this.entityPlayerClass.getDeclaredConstructors()[0];
            Object epTest = epConstructor.newInstance(this.server, this.world, gameProfileTest, this.interactManager);
            gameProfileTest.getProperties().put("textures", new Property("textures", "eyJ0aW1lc3RhbXAiOjE1NDczMzg3OTQwODcsInByb2ZpbGVJZCI6IjFjZjQ0OTMwODY1MTQ4NGE5ZmZjODI5YjlmNDg3NGE2IiwicHJvZmlsZU5hbWUiOiJJbmdBbmciLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2Y1MGVlOGEwNDNlNzEwZTIyOGM5MjQ2NGQ4ZDJkYzgxZmE0MWJkZjRhZDczNThmNTBjYjk1MjQ3MzAzOTczZjUifX19", "mW3mZThLJuHHqMZjk8BO3WEXtqqh17/M4Px61qD8cdcs08QAN1y5dbGoqXubfZidtrTMBJPMuTCTLNKPpN9FPN7j7Y89NQKtPACwoQykugU53n32kqAklZQpQ7/3ehEiAYirnxk7hTS/nNrghpkYHpBWit/YdCEYpGkf2jsILqWCq8caHkW8frxOjuaoWvF2HJ8ocXQcnqjhg4BjRLNFfyrKneowAnFkES9O4l6psX2M7Pb1Gd59cIU3C1O6JhSmkW1mHN/Vpk4pGBZVkA8F6gn6m84KpxpdHeGTGIUh1VqRuyMwMXKPo/zVyFU/+AyWqTRYi/i3/Q6mDY9LV7HvxkGjPey5gSysuZvVzF3goaloa/pUPdXKiBKjuJzBujR4grlrsCWUL76dCj/j1eidYT+9SSMFjh2d1ttH4Y2fV+LW1JNKqhAPzQmUDdMKvaBkMj2WPjoiHdqhSCvMfGd0+rmH/KzJx/nYhhuif+DQy71FlROUDL0rc2aTA3EwPkxyXvLP66CXACdyPwgCcMxcGYjdWa58oiKXm5KRw7iCVxPfOw9mcmVphEqRbBsIU4ikJ3ow7/GaQBkBqloZR79L4YeSUWWy8QAH3xpSxMQkri1vaSL+k42TUbWvDJYG6TWRMFJ4xoUyWJQwPtvcXoWd02lCHQP0d+INYt6PPY8nAto="));
            this.addPlayerInfo(player, epTest);
            return epTest;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void removePlayerInfo(Player player, Object ep) {
        try {
            Constructor<?> packetConstructor = this.playerInfo.getConstructor(this.playerInfoEnum, Class.forName("[Lnet.minecraft.server." + Reflection.VERSION + ".EntityPlayer;"));
            Object addPlayerEnum = this.playerInfoEnum.getField("REMOVE_PLAYER").get(null);
            Object array = Array.newInstance(this.entityPlayerClass, 1);
            Array.set(array, 0, ep);
            Object playerInfoPacket = packetConstructor.newInstance(addPlayerEnum, array);
            this.revampTab.getTinyProtocol().sendPacket(player, playerInfoPacket);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removePlayerInfoForEveryone(Player player) {
        for (Player player2 : Bukkit.getOnlinePlayers()) {
            //this.revampTab.getTinyProtocol();
            this.removePlayerInfo(player2, TinyProtocol.getPlayerHandle.invoke(player));
        }
    }

    @Override
    public void update(Player player) {
        if (this.revampTab.getPlayerVersion().getProtocolVersion(player) < 47) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                //this.revampTab.getTinyProtocol();
                this.addPlayerInfo(player, TinyProtocol.getPlayerHandle.invoke(online));
            }
            new BukkitRunnable() {
                public void run() {
                    TabVersionTinyProtocol.this.removeAllOnlinePlayers(player);
                }
            }.runTaskLater(this.revampTab.getJavaPlugin(), 1L);
        }
        if (RevampTab.getInstance().getTablist().getTabElements(player) == null || RevampTab.getInstance().getTablist().getTabElements(player).isEmpty()) {
            Objects.requireNonNull(RevampTab.getInstance().getTablists().stream().filter(tablist -> tablist.getPlayer() == player).findFirst().orElse(null)).disable();
        }
        else {
            Objects.requireNonNull(RevampTab.getInstance().getTablists().stream().filter(tablist -> tablist.getPlayer() == player).findFirst().orElse(null)).enable();
        }
        if (this.revampTab.getTablist().getTabHeader(player) != null && this.revampTab.getTablist().getTabFooter(player) != null) {
            this.setHeaderAndFooter(player);
        }
    }

    @Override
    public void setHeaderAndFooter(Player player) {
        if (this.getSlots(player) == 80) {
            try {
                /*
                if (Reflection.VERSION.contains("v1_13") || Reflection.VERSION.contains("v1_14")) {
                    player.getClass().getMethod("setPlayerListHeader", String.class).invoke(player, CC.translate(this.revampTab.getTablist().getTabHeader(player)));
                    player.getClass().getMethod("setPlayerListFooter", String.class).invoke(player, CC.translate(this.revampTab.getTablist().getTabFooter(player)));
                    return;
                }
                */
                Object headerFooterPacket = this.headerFooter.newInstance();
                Field a = headerFooterPacket.getClass().getDeclaredField("a");
                Field b = headerFooterPacket.getClass().getDeclaredField("b");
                a.setAccessible(true);
                b.setAccessible(true);
                Object chatComponentHeader = this.chatComponentText.getConstructors()[0].newInstance(CC.translate(this.revampTab.getTablist().getTabHeader(player)));
                Object chatComponentFooter = this.chatComponentText.getConstructors()[0].newInstance(CC.translate(this.revampTab.getTablist().getTabFooter(player)));
                a.set(headerFooterPacket, chatComponentHeader);
                b.set(headerFooterPacket, chatComponentFooter);
                this.revampTab.getTinyProtocol().sendPacket(player, headerFooterPacket);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addAllOnlinePlayers(Player player) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            //this.revampTab.getTinyProtocol();
            this.addPlayerInfo(player, TinyProtocol.getPlayerHandle.invoke(online));
        }
    }

    @Override
    public void removeAllOnlinePlayers(Player player) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            //this.revampTab.getTinyProtocol();
            this.removePlayerInfo(player, TinyProtocol.getPlayerHandle.invoke(online));
        }
    }

    @Override
    public int getSlots(Player player) {
        return (this.revampTab.getPlayerVersion().getProtocolVersion(player) >= 47) ? 80 : 60;
    }
}

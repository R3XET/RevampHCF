package eu.revamp.tablist.tabversion;

import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.tablist.RevampTab;
import eu.revamp.tablist.manager.TablistManager;
import eu.revamp.tablist.interfaces.TabVersionInterface;
import eu.revamp.tablist.util.TabUtil;
import net.minecraft.server.v1_7_R4.*;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.spigotmc.ProtocolInjector;

import java.util.Objects;
import java.util.UUID;

public class TabVersion1_7 implements TabVersionInterface
{
    private MinecraftServer minecraftServer;
    private WorldServer worldServer;
    private PlayerInteractManager playerInteractManager;
    private RevampTab revampTab;

    public TabVersion1_7(RevampTab revampTab) {
        this.minecraftServer = MinecraftServer.getServer();
        this.worldServer = this.minecraftServer.getWorldServer(0);
        this.playerInteractManager = new PlayerInteractManager(this.worldServer);
        this.revampTab = revampTab;
    }

    @Override
    public void setup(Player player) {
        this.removePlayerInfoForEveryone(player);
        for (Player online : Bukkit.getOnlinePlayers()) {
            this.removePlayerInfo(player, ((CraftPlayer)online).getHandle());
        }
        if (this.revampTab.getTablist().getTabElements(player) == null || this.revampTab.getTablist().getTabElements(player).isEmpty()) return;
        Scoreboard scoreboard = (player.getScoreboard() == Bukkit.getScoreboardManager().getMainScoreboard()) ? Bukkit.getScoreboardManager().getNewScoreboard() : player.getScoreboard();
        for (Player online2 : Bukkit.getOnlinePlayers()) {
            this.removePlayerInfo(player, ((CraftPlayer)online2).getHandle());
        }
        this.removePlayerInfoForEveryone(player);
        this.revampTab.getTablists().add(new TablistManager(scoreboard, player, this.revampTab));
    }

    @Override
    public Object createPlayer(Player player, String name) {
        OfflinePlayer testPlayer = TabUtil.createNewFakePlayer(UUID.randomUUID(), name);
        GameProfile gameProfileTest = new GameProfile(testPlayer.getUniqueId(), testPlayer.getName());
        EntityPlayer epTest = new EntityPlayer(this.minecraftServer, this.worldServer, gameProfileTest, this.playerInteractManager);
        gameProfileTest.getProperties().put("textures", new Property("textures", "eyJ0aW1lc3RhbXAiOjE0MTEyNjg3OTI3NjUsInByb2ZpbGVJZCI6IjNmYmVjN2RkMGE1ZjQwYmY5ZDExODg1YTU0NTA3MTEyIiwicHJvZmlsZU5hbWUiOiJsYXN0X3VzZXJuYW1lIiwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzg0N2I1Mjc5OTg0NjUxNTRhZDZjMjM4YTFlM2MyZGQzZTMyOTY1MzUyZTNhNjRmMzZlMTZhOTQwNWFiOCJ9fX0=", "u8sG8tlbmiekrfAdQjy4nXIcCfNdnUZzXSx9BE1X5K27NiUvE1dDNIeBBSPdZzQG1kHGijuokuHPdNi/KXHZkQM7OJ4aCu5JiUoOY28uz3wZhW4D+KG3dH4ei5ww2KwvjcqVL7LFKfr/ONU5Hvi7MIIty1eKpoGDYpWj3WjnbN4ye5Zo88I2ZEkP1wBw2eDDN4P3YEDYTumQndcbXFPuRRTntoGdZq3N5EBKfDZxlw4L3pgkcSLU5rWkd5UH4ZUOHAP/VaJ04mpFLsFXzzdU4xNZ5fthCwxwVBNLtHRWO26k/qcVBzvEXtKGFJmxfLGCzXScET/OjUBak/JEkkRG2m+kpmBMgFRNtjyZgQ1w08U6HHnLTiAiio3JswPlW5v56pGWRHQT5XWSkfnrXDalxtSmPnB5LmacpIImKgL8V9wLnWvBzI7SHjlyQbbgd+kUOkLlu7+717ySDEJwsFJekfuR6N/rpcYgNZYrxDwe4w57uDPlwNL6cJPfNUHV7WEbIU1pMgxsxaXe8WSvV87qLsR7H06xocl2C0JFfe2jZR4Zh3k9xzEnfCeFKBgGb4lrOWBu1eDWYgtKV67M2Y+B3W5pjuAjwAxn0waODtEn/3jKPbc/sxbPvljUCw65X+ok0UUN1eOwXV5l2EGzn05t3Yhwq19/GxARg63ISGE8CKw="));
        epTest.ping = 1;
        this.addPlayerInfo(player, epTest);
        return epTest;
    }

    @Override
    public void addPlayerInfo(Player player, Object ep) {
        EntityPlayer entityPlayer = (EntityPlayer)ep;
        new PacketPlayOutPlayerInfo();
        PacketPlayOutPlayerInfo packet = PacketPlayOutPlayerInfo.addPlayer(entityPlayer);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public void removePlayerInfo(Player player, Object ep) {
        EntityPlayer entityPlayer = (EntityPlayer)ep;
        new PacketPlayOutPlayerInfo();
        PacketPlayOutPlayerInfo packet = PacketPlayOutPlayerInfo.removePlayer(entityPlayer);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public void removePlayerInfoForEveryone(Player player) {
        EntityPlayer ep = ((CraftPlayer)player).getHandle();
        for (Player online : Bukkit.getOnlinePlayers()) {
            this.removePlayerInfo(online, ep);
        }
    }

    @Override
    public void update(Player player) {
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
            IChatBaseComponent componentHeader = ChatSerializer.a("{\"text\": \"" + CC.translate(this.revampTab.getTablist().getTabHeader(player) + "\"}"));
            IChatBaseComponent componentFooter = ChatSerializer.a("{\"text\": \"" + CC.translate(this.revampTab.getTablist().getTabFooter(player) + "\"}"));
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTabHeader(componentHeader, componentFooter));
        }
    }

    @Override
    public void addAllOnlinePlayers(Player player) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            this.addPlayerInfo(player, ((CraftPlayer)online).getHandle());
        }
    }

    @Override
    public void removeAllOnlinePlayers(Player player) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            this.removePlayerInfo(player, ((CraftPlayer)online).getHandle());
        }
    }

    @Override
    public int getSlots(Player player) {
        return (this.revampTab.getPlayerVersion().getProtocolVersion(player) >= 47) ? 80 : 60;
    }
}

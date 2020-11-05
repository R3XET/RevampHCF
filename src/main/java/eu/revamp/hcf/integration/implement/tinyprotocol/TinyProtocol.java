package eu.revamp.hcf.integration.implement.tinyprotocol;

import eu.revamp.tablist.RevampTab;
import eu.revamp.tablist.playerversion.PlayerVersionTinyProtocol;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.mojang.authlib.GameProfile;
import io.netty.channel.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public abstract class TinyProtocol
{
    private static AtomicInteger ID = new AtomicInteger(0);
    public static Reflection.MethodInvoker getPlayerHandle = Reflection.getMethod("{obc}.entity.CraftPlayer", "getHandle");
    public static Reflection.MethodInvoker getProfile = Reflection.getMethod("{obc}.entity.CraftPlayer", "getProfile");
    private static Reflection.FieldAccessor<Object> getConnection = Reflection.getField("{nms}.EntityPlayer", "playerConnection", Object.class);
    private static Reflection.FieldAccessor<Object> getManager = Reflection.getField("{nms}.PlayerConnection", "networkManager", Object.class);
    private static Reflection.FieldAccessor<Channel> getChannel = Reflection.getField("{nms}.NetworkManager", Channel.class, 0);
    private static Class<Object> minecraftServerClass = Reflection.getUntypedClass("{nms}.MinecraftServer");
    private static Class<Object> serverConnectionClass = Reflection.getUntypedClass("{nms}.ServerConnection");
    private static Reflection.FieldAccessor<Object> getMinecraftServer = Reflection.getField("{obc}.CraftServer", TinyProtocol.minecraftServerClass, 0);
    private static Reflection.FieldAccessor<Object> getServerConnection = Reflection.getField(TinyProtocol.minecraftServerClass, TinyProtocol.serverConnectionClass, 0);
    private static Reflection.MethodInvoker getNetworkMarkers = Reflection.getTypedMethod(TinyProtocol.serverConnectionClass, null, List.class, TinyProtocol.serverConnectionClass);
    private static Class<?> PACKET_SET_PROTOCOL = Reflection.getMinecraftClass("PacketHandshakingInSetProtocol");
    private static Class<?> PACKET_LOGIN_IN_START = Reflection.getMinecraftClass("PacketLoginInStart");
    private static Reflection.FieldAccessor<GameProfile> getGameProfile = Reflection.getField(TinyProtocol.PACKET_LOGIN_IN_START, GameProfile.class, 0);
    private static Reflection.FieldAccessor<Integer> protocolId = Reflection.getField(TinyProtocol.PACKET_SET_PROTOCOL, Integer.TYPE, 0);
    private Map<String, Channel> channelLookup;
    private Map<Channel, Integer> protocolLookup;
    private Listener listener;
    private Set<Channel> uninjectedChannels;
    private List<Object> networkManagers;
    private List<Channel> serverChannels;
    private ChannelInboundHandlerAdapter serverChannelHandler;
    private ChannelInitializer<Channel> beginInitProtocol;
    private ChannelInitializer<Channel> endInitProtocol;
    private static Reflection.FieldAccessor<Enum> protocolType = Reflection.getField(TinyProtocol.PACKET_SET_PROTOCOL, Enum.class, 0);
    private String handlerName;
    protected volatile boolean closed;
    protected Plugin plugin;
    
    public TinyProtocol(Plugin plugin) {
        this.channelLookup = new MapMaker().weakValues().makeMap();
        this.protocolLookup = new WeakHashMap<>();
        this.uninjectedChannels = Collections.newSetFromMap(new MapMaker().weakKeys().makeMap());
        this.serverChannels = Lists.newArrayList();
        this.plugin = plugin;
        this.handlerName = this.getHandlerName();
        this.registerBukkitEvents();
        try {
            this.registerChannelHandler();
            this.registerPlayers(plugin);
        }
        catch (IllegalArgumentException ex) {
            plugin.getLogger().info("[v1_7TinyProtocol] Delaying server channel injection due to late bind.");
            new BukkitRunnable() {
                public void run() {
                    registerChannelHandler();
                    registerPlayers(plugin);
                    plugin.getLogger().info("[v1_7TinyProtocol] Late bind injection successful.");
                }
            }.runTask(plugin);
        }
    }
    
    private void createServerChannelHandler() {
        this.endInitProtocol = new ChannelInitializer<Channel>() {
            protected void initChannel(Channel channel) {
                try {
                    synchronized (networkManagers) {
                        if (!closed) {
                            channel.eventLoop().submit(() -> injectChannelInternal(channel));
                        }
                    }
                }
                catch (Exception e) {
                    plugin.getLogger().log(Level.SEVERE, "Cannot inject incomming channel " + channel, e);
                }
            }
        };
        this.beginInitProtocol = new ChannelInitializer<Channel>() {
            protected void initChannel(Channel channel) {
                channel.pipeline().addLast(endInitProtocol);
            }
        };
        this.serverChannelHandler = new ChannelInboundHandlerAdapter() {
            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                Channel channel = (Channel)msg;
                channel.pipeline().addFirst(beginInitProtocol);
                ctx.fireChannelRead(msg);
            }
        };
    }
    
    private void registerBukkitEvents() {
        this.listener = new Listener() {
            @EventHandler(priority = EventPriority.LOWEST)
            public void onPlayerLogin(PlayerLoginEvent e) {
                if (closed) return;
                Channel channel = getChannel(e.getPlayer());
                if (!uninjectedChannels.contains(channel)) {
                    injectPlayer(e.getPlayer());
                }
            }
            
            @EventHandler
            public void onPluginDisable(PluginDisableEvent e) {
                if (e.getPlugin().equals(plugin)) {
                    close();
                }
            }
        };
        this.plugin.getServer().getPluginManager().registerEvents(this.listener, this.plugin);
    }
    
    private void registerChannelHandler() {
        Object mcServer = TinyProtocol.getMinecraftServer.get(Bukkit.getServer());
        Object serverConnection = TinyProtocol.getServerConnection.get(mcServer);
        boolean looking = true;
        this.networkManagers = (List<Object>)TinyProtocol.getNetworkMarkers.invoke(null, serverConnection);
        this.createServerChannelHandler();
        int i = 0;
        while (looking) {
            List<Object> list = (List<Object>)Reflection.getField(serverConnection.getClass(), List.class, i).get(serverConnection);
            for (Object item : list) {
                if (!(item instanceof ChannelFuture)) {
                    break;
                }
                Channel serverChannel = ((ChannelFuture)item).channel();
                this.serverChannels.add(serverChannel);
                serverChannel.pipeline().addFirst(this.serverChannelHandler);
                looking = false;
            }
            ++i;
        }
    }
    
    private void unregisterChannelHandler() {
        if (this.serverChannelHandler == null) return;
        for (Channel serverChannel : this.serverChannels) {
            ChannelPipeline pipeline = serverChannel.pipeline();
            serverChannel.eventLoop().execute(() -> {
                try {
                    pipeline.remove(serverChannelHandler);
                }
                catch (NoSuchElementException ignored) {}
            });
        }
    }
    
    private void registerPlayers(Plugin plugin) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            this.injectPlayer(player);
        }
    }
    
    public int getProtocolVersion(Player player) {
        Channel channel = this.channelLookup.get(player.getName());
        if (channel == null) {
            Object connection = TinyProtocol.getConnection.get(TinyProtocol.getPlayerHandle.invoke(player));
            Object manager = TinyProtocol.getManager.get(connection);
            this.channelLookup.put(player.getName(), channel = TinyProtocol.getChannel.get(manager));
        }
        return this.protocolLookup.get(channel);
    }
    
    public Object onPacketOutAsync(Player receiver, Channel channel, Object packet) {
        return packet;
    }
    
    public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
        return packet;
    }
    
    public void sendPacket(Player player, Object packet) {
        this.sendPacket(this.getChannel(player), packet);
    }
    
    public void sendPacket(Channel channel, Object packet) {
        channel.pipeline().writeAndFlush(packet);
    }
    
    public void receivePacket(Player player, Object packet) {
        this.receivePacket(this.getChannel(player), packet);
    }
    
    public void receivePacket(Channel channel, Object packet) {
        channel.pipeline().context("encoder").fireChannelRead(packet);
    }
    
    protected String getHandlerName() {
        return "tiny-" + this.plugin.getName() + "-" + TinyProtocol.ID.incrementAndGet();
    }
    
    public void injectPlayer(Player player) {
        this.injectChannelInternal(this.getChannel(player)).player = player;
    }
    
    public void injectChannel(Channel channel) {
        this.injectChannelInternal(channel);
    }
    
    private PacketInterceptor injectChannelInternal(Channel channel) {
        try {
            PacketInterceptor interceptor = (PacketInterceptor)channel.pipeline().get(this.handlerName);
            if (interceptor == null) {
                interceptor = new PacketInterceptor();
                channel.pipeline().addBefore("packet_handler", this.handlerName, interceptor);
                this.uninjectedChannels.remove(channel);
            }
            return interceptor;
        }
        catch (IllegalArgumentException e) {
            return (PacketInterceptor)channel.pipeline().get(this.handlerName);
        }
    }
    
    public Channel getChannel(Player player) {
        Channel channel = this.channelLookup.get(player.getName());
        if (channel == null) {
            Object connection = TinyProtocol.getConnection.get(TinyProtocol.getPlayerHandle.invoke(player));
            Object manager = TinyProtocol.getManager.get(connection);
            this.channelLookup.put(player.getName(), channel = TinyProtocol.getChannel.get(manager));
        }
        return channel;
    }
    
    public void uninjectPlayer(Player player) {
        this.uninjectChannel(this.getChannel(player));
    }
    
    public void uninjectChannel(Channel channel) {
        if (!this.closed) {
            this.uninjectedChannels.add(channel);
        }
        channel.eventLoop().execute(() -> channel.pipeline().remove(handlerName));
    }
    
    public boolean hasInjected(Player player) {
        return this.hasInjected(this.getChannel(player));
    }
    
    public boolean hasInjected(Channel channel) {
        return channel.pipeline().get(this.handlerName) != null;
    }
    
    public void close() {
        if (!this.closed) {
            this.closed = true;
            for (Player player : this.plugin.getServer().getOnlinePlayers()) {
                this.uninjectPlayer(player);
            }
            HandlerList.unregisterAll(this.listener);
            this.unregisterChannelHandler();
        }
    }

    private class PacketInterceptor extends ChannelDuplexHandler
    {
        public volatile Player player;
        
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Channel channel = ctx.channel();
            try {
                this.handleLoginStart(channel, msg);
                msg = onPacketInAsync(this.player, channel, msg);
            }
            catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error in onPacketInAsync().", e);
            }
            if (msg != null) {
                super.channelRead(ctx, msg);
            }
        }
        
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            try {
                msg = onPacketOutAsync(this.player, ctx.channel(), msg);
            }
            catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error in onPacketOutAsync().", e);
            }
            if (msg != null) {
                super.write(ctx, msg, promise);
            }
        }

        private void handleLoginStart(Channel channel, Object packet) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            if (TinyProtocol.PACKET_LOGIN_IN_START.isInstance(packet)) {
                GameProfile profile = TinyProtocol.getGameProfile.get(packet);
                channelLookup.put(profile.getName(), channel);
            }
            else if (RevampTab.getInstance().getPlayerVersion().getClass() == PlayerVersionTinyProtocol.class && TinyProtocol.PACKET_SET_PROTOCOL.isInstance(packet)) {
                Method method = packet.getClass().getMethod("b");
                protocolLookup.put(channel, (int)method.invoke(packet, new Object[0]));
            }
        }
    }
}

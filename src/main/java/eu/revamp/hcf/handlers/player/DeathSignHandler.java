package eu.revamp.hcf.handlers.player;

import eu.revamp.hcf.RevampHCF;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.block.BlockState;
import org.bukkit.block.Block;
import org.bukkit.GameMode;
import java.util.Arrays;
import org.bukkit.block.Sign;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import com.google.common.collect.Lists;
import org.apache.commons.lang.time.FastDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.HandlerList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class DeathSignHandler extends Handler implements Listener
{
    private String deathSignItemName = ChatColor.GOLD + "Death Sign";
    
    public DeathSignHandler(RevampHCF plugin) {
        super(plugin);
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }

    private ItemStack getDeathSign(String playerName, String killerName) {
        ItemStack stack = new ItemStack(Material.SIGN, 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(this.deathSignItemName);
        FastDateFormat date = FastDateFormat.getInstance(RevampHCF.getInstance().getConfig().getString("DATE_FORMAT"), TimeZone.getTimeZone(RevampHCF.getInstance().getConfig().getString("TIMEZONE")), Locale.ENGLISH);
        meta.setLore(Lists.newArrayList(ChatColor.GREEN + playerName, ChatColor.WHITE + "slain by", ChatColor.GREEN + killerName, date.format(System.currentTimeMillis())));
        stack.setItemMeta(meta);
        return stack;
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onSignChange(SignChangeEvent event) {
        if (this.isDeathSign(event.getBlock())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (this.isDeathSign(block)) {
            BlockState state = block.getState();
            Sign sign = (Sign)state;
            ItemStack stack = new ItemStack(Material.SIGN, 1);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(this.deathSignItemName);
            meta.setLore(Arrays.asList(sign.getLines()));
            stack.setItemMeta(meta);
            Player player = event.getPlayer();
            World world = player.getWorld();
            if (player.getGameMode() != GameMode.CREATIVE && world.isGameRule("doTileDrops")) {
                world.dropItemNaturally(block.getLocation(), stack);
            }
            event.setCancelled(true);
            block.setType(Material.AIR);
            state.update();
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack stack = event.getItemInHand();
        BlockState state = event.getBlock().getState();
        if (state instanceof Sign && stack.hasItemMeta()) {
            ItemMeta meta = stack.getItemMeta();
            if (meta.hasDisplayName() && meta.getDisplayName().equals(this.deathSignItemName)) {
                Sign sign = (Sign)state;
                List<String> lore = meta.getLore();
                int count = 0;
                for (String loreLine : lore) {
                    sign.setLine(count++, loreLine);
                    if (count == 4) {
                        break;
                    }
                }
                sign.update();
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        if (killer != null && (!killer.equals(player) & !RevampHCF.getInstance().getConfiguration().isKitMap())) {
            event.getDrops().add(getDeathSign(player.getName(), killer.getName()));
        }
    }
    
    private boolean isDeathSign(Block block) {
        BlockState state = block.getState();
        if (state instanceof Sign) {
            String[] lines = ((Sign)state).getLines();
            return lines.length > 0 && lines[1] != null && lines[1].equals(ChatColor.WHITE + "slain by");
        }
        return false;
    }
}

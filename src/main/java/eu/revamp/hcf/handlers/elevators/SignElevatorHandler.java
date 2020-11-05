package eu.revamp.hcf.handlers.elevators;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.apache.commons.lang.StringUtils;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class SignElevatorHandler extends Handler implements Listener
{
    private String prefix;
    private String signTitle;
    
    public SignElevatorHandler(RevampHCF plugin) {
        super(plugin);
        this.prefix = ChatColor.DARK_RED + ChatColor.BOLD.toString() + "[Elevators] " + ChatColor.RED;
        this.signTitle = ChatColor.DARK_RED + ChatColor.BOLD.toString() + "[Elevator]";
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onSignUpdate(SignChangeEvent e) {
        if (StringUtils.containsIgnoreCase(e.getLine(0), "Elevator")) {
            boolean up;
            if (StringUtils.containsIgnoreCase(e.getLine(1), "Up")) {
                up = true;
            }
            else {
                if (!StringUtils.containsIgnoreCase(e.getLine(1), "Down")) {
                    e.getPlayer().sendMessage(this.prefix + "Invalid sign! Needs to be Up or Down");
                    this.fail(e);
                    return;
                }
                up = false;
            }
            e.setLine(0, this.signTitle);
            e.setLine(1, up ? "Up" : "Down");
            e.setLine(2, "");
            e.setLine(3, "");
        }
    }
    
    public void fail(SignChangeEvent e) {
        e.setLine(0, this.signTitle);
        e.setLine(1, ChatColor.RED + "Error");
        e.setLine(2, "");
        e.setLine(3, "");
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock() != null) {
            Block block = e.getClickedBlock();
            Long lastInteractTime = RevampHCF.getInstance().getHandlerManager().getAntiBlockHitHandler().lastInteractTimes.get(e.getPlayer().getUniqueId());
            if (block.getState() instanceof Sign) {
                Sign sign = (Sign)block.getState();
                String[] lines = sign.getLines();
                if (lines[0].equals(this.signTitle)) {
                    boolean up;
                    if (lines[1].equalsIgnoreCase("Up")) {
                        up = true;
                    }
                    else {
                        if (!lines[1].equalsIgnoreCase("Down")) return;
                        up = false;
                    }
                    if (lastInteractTime - System.currentTimeMillis() <= 0L) {
                        this.signClick(e.getPlayer(), sign.getLocation(), up);
                    }
                }
            }
        }
    }
    
    public boolean signClick(Player player, Location signLocation, boolean up) {
        Block block = signLocation.getBlock();
        do {
            block = block.getRelative(up ? BlockFace.UP : BlockFace.DOWN);
            if (block.getY() > block.getWorld().getMaxHeight() || block.getY() <= 1) {
                player.sendMessage(this.prefix + "Could not locate the sign " + (up ? "above" : "below"));
                return false;
            }
        } while (!this.isSign(block));
        boolean underSafe = this.isSafe(block.getRelative(BlockFace.DOWN));
        boolean overSafe = this.isSafe(block.getRelative(BlockFace.UP));
        if (!underSafe && !overSafe) {
            player.sendMessage(this.prefix + "Could not find a place to teleport by the sign " + (up ? "above" : "below"));
            return false;
        }
        Location location = player.getLocation().clone();
        location.setX(block.getX() + 0.5);
        location.setY(block.getY() + (underSafe ? -1 : 0));
        location.setZ(block.getZ() + 0.5);
        location.setPitch(0.0f);
        player.teleport(location);
        return true;
    }
    
    public boolean isSign(Block block) {
        if (block.getState() instanceof Sign) {
            Sign sign = (Sign)block.getState();
            String[] lines = sign.getLines();
            return lines[0].equals(this.signTitle) && (lines[1].equalsIgnoreCase("Up") || lines[1].equalsIgnoreCase("Down"));
        }
        return false;
    }
    
    public boolean isSafe(Block block) {
        return block != null && !block.getType().isSolid() && block.getType() != Material.GLASS && block.getType() != Material.STAINED_GLASS;
    }
}

package eu.revamp.hcf.handlers.signs;

import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.Inventory;
import java.util.Set;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.block.Chest;
import java.util.HashSet;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.BlockState;
import org.bukkit.GameMode;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.type.PlayerFaction;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import java.util.stream.Collectors;
import eu.revamp.hcf.factions.utils.struction.Role;
import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;
import org.bukkit.event.block.SignChangeEvent;

import java.util.Collection;
import org.bukkit.block.Sign;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.Bukkit;
import eu.revamp.hcf.RevampHCF;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;
import java.util.regex.Pattern;
import java.util.List;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class SignSubclaimHandler extends Handler implements Listener
{
    private static int MAX_SIGN_LINE_CHARS = 16;
    private static String SUBCLAIM_CONVERSION_PREFIX = String.valueOf(ChatColor.DARK_RED.toString()) + ChatColor.BOLD + "[Subclaim]";
    private static List<String> SUBCLAIM_ALIASES = Arrays.asList("SUBCLAIM", "PRIVATE");
    private static Pattern SQUARE_PATTERN_REPLACER= Pattern.compile("\\[|\\]");
    private static BlockFace[] SIGN_FACES = new BlockFace[] { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP };
    
    public SignSubclaimHandler(RevampHCF plugin) {
        super(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @Override
    public void disable() {
    }
    
    private boolean isSubclaimable(Block block) {
        Material type = block.getType();
        return type == Material.FENCE_GATE || type == Material.TRAP_DOOR || block.getState() instanceof InventoryHolder;
    }
    
    private boolean isSubclaimed(Block block) {
        if (this.isSubclaimable(block)) {
            Collection<Sign> attachedSigns = this.getAttachedSigns(block);
            for (Sign attachedSign : attachedSigns) {
                if (attachedSign.getLine(0).equals(SignSubclaimHandler.SUBCLAIM_CONVERSION_PREFIX)) {
                    return false;
                }
            }
        }
        return false;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH) @SuppressWarnings("deprecation")
    public void onSignChange(SignChangeEvent event) {
        String[] lines = event.getLines();
        if (!SignSubclaimHandler.SUBCLAIM_ALIASES.contains(SignSubclaimHandler.SQUARE_PATTERN_REPLACER.matcher(lines[0].toUpperCase()).replaceAll(""))) return;
        Block block = event.getBlock();
        MaterialData materialData = block.getState().getData();
        if (materialData instanceof org.bukkit.material.Sign) {
            org.bukkit.material.Sign sign = (org.bukkit.material.Sign)materialData;
            Block attachedBlock = block.getRelative(sign.getAttachedFace());
            if (this.isSubclaimable(attachedBlock)) {
                Player player = event.getPlayer();
                PlayerFaction playerFaction = this.getInstance().getFactionManager().getPlayerFaction(player);
                if (playerFaction == null) return;
                Faction factionAt = this.getInstance().getFactionManager().getFactionAt(block.getLocation());
                if (playerFaction == factionAt) {
                    if (this.isSubclaimed(attachedBlock)) {
                        player.sendMessage(ChatColor.RED + "There is already a subclaim sign on this " + attachedBlock.getType().toString() + '.');
                        return;
                    }
                    List<String> memberList = new ArrayList<>(3);
                    for (int i = 1; i < lines.length; ++i) {
                        String line = lines[i];
                        if (StringUtils.isNotBlank(line)) {
                            memberList.add(line);
                        }
                    }
                    if (memberList.isEmpty()) {
                        player.sendMessage(ChatColor.RED + "Subclaim signs need to have at least 1 player name inserted.");
                        return;
                    }
                    boolean leaderChest = lines[1].equals(Role.LEADER.getAstrix()) || lines[1].equalsIgnoreCase("LEADER");
                    if (leaderChest) {
                        if (playerFaction.getMember(player).getRole() != Role.LEADER) {
                            player.sendMessage(ChatColor.RED + "Only faction leaders can create leader subclaimed objects.");
                            return;
                        }
                        event.setLine(2, null);
                        event.setLine(3, null);
                    }
                    event.setLine(0, SignSubclaimHandler.SUBCLAIM_CONVERSION_PREFIX);
                    List<String> actualMembers = memberList.stream().filter(member -> playerFaction.getMember(member) != null).collect(Collectors.toList());
                    playerFaction.broadcast("&2" + player.getName() + " &ehas created a subclaim on block type &f" + attachedBlock.getType().toString() + " &eat &f" + '(' + attachedBlock.getX() + ", " + attachedBlock.getZ() + ')' + ChatColor.YELLOW + " for " + (leaderChest ? "leaders" : (actualMembers.isEmpty() ? "captains" : ("members " + ChatColor.RED + '[' + StringUtils.join(actualMembers, ", ") + ']'))));
                }
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        if (this.getInstance().getHandlerManager().getEotwUtils().isEndOfTheWorld()) return;
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE && player.hasPermission("revamphcf.op")) return;
        Block block = event.getBlock();
        BlockState state = block.getState();
        Block subclaimObjectBlock = null;
        if (!(state instanceof Sign)) {
            subclaimObjectBlock = block;
        }
        else {
            Sign sign = (Sign)state;
            MaterialData signData = sign.getData();
            if (signData instanceof org.bukkit.material.Sign) {
                org.bukkit.material.Sign materialSign = (org.bukkit.material.Sign)signData;
                subclaimObjectBlock = block.getRelative(materialSign.getAttachedFace());
            }
        }
        if (subclaimObjectBlock != null && !this.checkSubclaimIntegrity(player, subclaimObjectBlock)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot break this subclaimed " + subclaimObjectBlock.getType().toString() + '.');
        }
    }
    
    private String getShortenedName(String originalName) {
        if (originalName.length() >= MAX_SIGN_LINE_CHARS) {
            originalName = originalName.substring(0, MAX_SIGN_LINE_CHARS);
        }
        return originalName;
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE && player.hasPermission("revamphcf.op")) return;
        if (this.getInstance().getHandlerManager().getEotwUtils().isEndOfTheWorld()) return;
        Block block = event.getClickedBlock();
        if (!this.isSubclaimable(block)) return;
        if (!this.checkSubclaimIntegrity(player, block)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You do not have access to this subclaimed " + block.getType().toString() + '.');
        }
    }
    @SuppressWarnings("deprecation")
    private boolean checkSubclaimIntegrity(Player player, Block subclaimObject) {
        if (!this.isSubclaimable(subclaimObject)) return true;
        PlayerFaction playerFaction = this.getInstance().getFactionManager().getPlayerFaction(player);
        if (playerFaction == null || playerFaction.isRaidable()) return true;
        Role role = playerFaction.getMember(player).getRole();
        if (role == Role.LEADER) return true;
        if (playerFaction != this.getInstance().getFactionManager().getFactionAt(subclaimObject)) return true;
        Collection<Sign> attachedSigns = this.getAttachedSigns(subclaimObject);
        if (attachedSigns.isEmpty()) return true;
        boolean hasLooped = false;
        String search = this.getShortenedName(player.getName());
        for (Sign attachedSign : attachedSigns) {
            String[] lines = attachedSign.getLines();
            if (!lines[0].equals(SignSubclaimHandler.SUBCLAIM_CONVERSION_PREFIX)) continue;
            hasLooped = true;
            if (Role.LEADER.getAstrix().equals(lines[1])) continue;
            if (Role.COLEADER.getAstrix().equals(lines[1])) continue;
            if (Role.CAPTAIN.getAstrix().equals(lines[1])) continue;
            if (Role.LEADER.getName().equals(lines[1])) continue;
            if (Role.COLEADER.getName().equals(lines[1])) continue;
            if (Role.CAPTAIN.getName().equals(lines[1])) continue;
            if (role == Role.CAPTAIN) return true;
            if (role == Role.COLEADER) return true;
            for (int i = 1; i < lines.length; ++i) {
                if (lines[i].toLowerCase().contains(search.toLowerCase())) {
                    return true;
                }
            }
        }
        return !hasLooped;
    }
    
    public Collection<Sign> getAttachedSigns(Block block) {
        Set<Sign> results = new HashSet<>();
        this.getSignsAround(block, results);
        BlockState state = block.getState();
        if (state instanceof Chest) {
            Inventory chestInventory = ((Chest)state).getInventory();
            if (chestInventory instanceof DoubleChestInventory) {
                DoubleChest doubleChest = ((DoubleChestInventory)chestInventory).getHolder();
                Block left = ((Chest)doubleChest.getLeftSide()).getBlock();
                Block right = ((Chest)doubleChest.getRightSide()).getBlock();
                this.getSignsAround(left.equals(block) ? right : left, results);
            }
        }
        return results;
    }
    
    private Set<Sign> getSignsAround(Block block, Set<Sign> results) {
        BlockFace[] sign_FACES;
        for (int length = (sign_FACES = SignSubclaimHandler.SIGN_FACES).length, i = 0; i < length; ++i) {
            BlockFace face = sign_FACES[i];
            Block relative = block.getRelative(face);
            BlockState relativeState = relative.getState();
            if (relativeState instanceof Sign) {
                org.bukkit.material.Sign materialSign = (org.bukkit.material.Sign)relativeState.getData();
                if (relative.getRelative(materialSign.getAttachedFace()).equals(block)) {
                    results.add((Sign)relative.getState());
                }
            }
        }
        return results;
    }
}

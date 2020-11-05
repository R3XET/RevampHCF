package eu.revamp.hcf.handlers.shop;

import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.inventory.InventoryUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.Location;
import org.bukkit.World;
import eu.revamp.hcf.factions.Faction;
import org.bukkit.entity.Player;
import org.bukkit.block.BlockState;
import org.bukkit.block.Block;
import java.util.Map;

import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;
import eu.revamp.hcf.factions.type.SpawnFaction;
import java.util.Arrays;
import eu.revamp.hcf.utils.inventory.Crowbar;
import org.bukkit.block.Sign;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Bukkit;

import java.util.regex.Pattern;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class ShopSignHandler extends Handler implements Listener
{
    private static long SIGN_TEXT_REVERT_TICKS = 100L;
    private static Pattern ALPHANUMERIC_REMOVER = Pattern.compile("[^A-Za-z0-9]");

    public ShopSignHandler(RevampHCF plugin) {
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

    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGH) @SuppressWarnings("deprecation")
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            BlockState state = block.getState();
            if (state instanceof Sign) {
                Sign sign = (Sign) state;
                String[] lines = sign.getLines();

                boolean parsed = true;
                Integer quantity = null, price = null;
                try {
                    quantity = Integer.parseInt(lines[2]);
                    price = Integer.parseInt(ALPHANUMERIC_REMOVER.matcher(lines[3]).replaceAll(""));
                } catch (IllegalArgumentException ex) {
                    parsed = false;
                }

                if (parsed) {
                    ItemStack stack;
                    if (lines[1].equalsIgnoreCase("Crowbar")) {
                        stack = new Crowbar().getItemIfPresent();
                    } else if ((stack = this.getInstance().getItemDB().getItem(ALPHANUMERIC_REMOVER.matcher(lines[1]).replaceAll(""), quantity)) == null) {
                        return;
                    }

                    // Final handling of shop.
                    Player player = event.getPlayer();
                    String[] fakeLines = Arrays.copyOf(sign.getLines(), 4);
                    Faction factionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation());
                    Faction FactionAtSIGN = RevampHCF.getInstance().getFactionManager().getFactionAt(sign.getLocation());
                    if (factionAt instanceof SpawnFaction && lines[0].contains("Sell") && lines[0].contains(ChatColor.RED.toString()) && FactionAtSIGN instanceof SpawnFaction) {
                        int sellQuantity = Math.min(quantity, InventoryUtils.countAmount(player.getInventory(),
                                stack.getType(), stack.getDurability()));
                        if (sellQuantity <= 0) {
                            fakeLines[0] = ChatColor.RED + "Not carrying any";
                            fakeLines[2] = ChatColor.RED + "on you.";
                            fakeLines[3] = "";
                        } else {
                            // Recalculate the price.
                            HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
                            int newPrice = (int) (((double) price / (double) quantity) * sellQuantity);
                            fakeLines[0] = ChatColor.GREEN + "Sold " + sellQuantity;
                            fakeLines[3] = ChatColor.GREEN + "for " + ChatColor.RESET + "$" + newPrice;
                            data.addBalance(newPrice);
                            InventoryUtils.removeItem(player.getInventory(), stack.getType(), stack.getData().getData(), sellQuantity);
                            player.updateInventory();
                        }
                    } else if (factionAt instanceof SpawnFaction && lines[0].contains("Buy") && lines[0].contains(ChatColor.GREEN.toString()) && FactionAtSIGN instanceof SpawnFaction) {
                        HCFPlayerData data2 = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
                        if (price > data2.getBalance()) {
                            fakeLines[0] = ChatColor.RED + "Cannot afford";
                        } else {
                            fakeLines[0] = ChatColor.GREEN + "Item bought";
                            fakeLines[3] = ChatColor.GREEN + "for " + ChatColor.RESET + "$" + price;
                            data2.setBalance(data2.getBalance() - price);
                            World world = player.getWorld();
                            Location location = player.getLocation();
                            Map<Integer, ItemStack> excess = player.getInventory().addItem(stack);
                            for (Map.Entry<Integer, ItemStack> excessItemStack : excess.entrySet()) {
                                world.dropItemNaturally(location, excessItemStack.getValue());
                            }
                            player.setItemInHand(player.getItemInHand());
                            player.updateInventory();
                        }
                    } else {
                        return;
                    }
                    event.setCancelled(true);
                    this.getInstance().getHandlerManager().getSignHandler().showLines(player, sign, fakeLines, SIGN_TEXT_REVERT_TICKS, true);
                }
            }
        }
    }

    //TODO OLD CODE
    /*
    @EventHandler @SuppressWarnings("deprecation")
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            BlockState state = block.getState();
            if (state instanceof Sign) {
                Sign sign = (Sign)state;
                String[] lines = sign.getLines();
                if (!ConversionUtils.isInteger(lines[2])) return;
                int quantity = Integer.parseInt(lines[2]);
                if (!ConversionUtils.isInteger(ShopSignHandler.ALPHANUMERIC_REMOVER.matcher(lines[3]).replaceAll(""))) return;
                int price = Integer.parseInt(lines[2]);
                ItemStack stack;
                if (lines[1].equalsIgnoreCase("Crowbar")) {
                    stack = new Crowbar().getItemIfPresent();
                }
                else if ((stack = RevampHCF.getInstance().getItemDB().getItem(ShopSignHandler.ALPHANUMERIC_REMOVER.matcher(lines[1]).replaceAll(""), quantity)) == null) return;
                Player player = event.getPlayer();
                Faction factionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation());
                Faction FactionAtSIGN = RevampHCF.getInstance().getFactionManager().getFactionAt(sign.getLocation());
                String[] fakeLines = Arrays.copyOf(sign.getLines(), 4);
                if (factionAt instanceof SpawnFaction && lines[0].contains("Sell") && FactionAtSIGN instanceof SpawnFaction) {
                    int sellQuantity = Math.min(quantity, InventoryUtils.countAmount(player.getInventory(), stack.getType(), stack.getDurability()));
                    if (sellQuantity <= 0) {
                        fakeLines[0] = ChatColor.RED + "Not carrying any";
                        fakeLines[2] = ChatColor.RED + "on you.";
                        fakeLines[3] = "";
                    }
                    else {
                        HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
                        int newPrice = price / quantity * sellQuantity;
                        fakeLines[0] = ChatColor.RED + "Sold " + ChatColor.RESET + sellQuantity;
                        fakeLines[1] = ChatColor.RED + "for " + ChatColor.RESET + "$" + newPrice;
                        fakeLines[2] = ChatColor.RED + "New balance";
                        fakeLines[3] = ChatColor.RESET + "$" + data.getBalance();
                        data.addBalance(newPrice);
                        InventoryUtils.removeItem(player.getInventory(), stack.getType(), stack.getData().getData(), sellQuantity);
                        player.updateInventory();
                    }
                }
                else {
                    if (!(factionAt instanceof SpawnFaction) || !lines[0].contains("Buy") || !(FactionAtSIGN instanceof SpawnFaction)) return;
                    HCFPlayerData data2 = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
                    if (price > data2.getBalance()) {
                        fakeLines[0] = ChatColor.RED + "Cannot afford";
                    }
                    else {
                        fakeLines[0] = ChatColor.GREEN + "Item bought";
                        fakeLines[1] = ChatColor.GREEN + "for " + ChatColor.RESET + "$" + price;
                        fakeLines[2] = ChatColor.GREEN + "New balance";
                        fakeLines[3] = ChatColor.RESET + "$" + data2.getBalance();
                        data2.setBalance(data2.getBalance() - price);
                        World world = player.getWorld();
                        Location location = player.getLocation();
                        Map<Integer, ItemStack> excess = player.getInventory().addItem(stack);
                        for (Map.Entry<Integer, ItemStack> excessItemStack : excess.entrySet()) {
                            world.dropItemNaturally(location, excessItemStack.getValue());
                        }
                        player.setItemInHand(player.getItemInHand());
                        player.updateInventory();
                    }
                }
                event.setCancelled(true);
                RevampHCF.getInstance().getHandlerManager().getSignHandler().showLines(player, sign, fakeLines, ShopSignHandler.SIGN_TEXT_REVERT_TICKS, true);
            }
        }
    }*/
    //TODO OLD CODE
}

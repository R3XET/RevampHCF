package eu.revamp.hcf.utils.inventory;

import java.util.Map;

import eu.revamp.hcf.RevampHCF;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;

@Getter @Setter
public class Menu
{
    private String title;
    private int rows;
    private HashMap<Integer, ItemStack> content;
    private HashMap<Integer, ItemAction> commands;
    private Inventory inventory;
    private ItemAction globalAction;
    private boolean runempty;
    
    public Menu(String title, int rows, ItemStack[] contents) {
        this(title, rows);
        this.setContents(contents);
    }
    
    public Menu(String title, int rows) {
        this.title = "";
        this.rows = 3;
        this.content = new HashMap<>();
        this.commands = new HashMap<>();
        this.runempty = false;
        if (rows < 1 || rows > 6) {
            throw new IndexOutOfBoundsException("Menu can only have between 1 and 6 rows.");
        }
        this.title = title;
        this.rows = rows;
        this.setListener(RevampHCF.getInstance());
    }
    
    private void setListener(JavaPlugin pl) {
        pl.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onInvClick(InventoryClickEvent event) {
                Player player = (Player)event.getWhoClicked();
                Inventory inv = event.getInventory();
                ItemStack item = event.getCurrentItem();
                int slot = event.getRawSlot();
                InventoryAction a = event.getAction();
                if ((item == null || item.getType() == Material.AIR) && !Menu.this.runempty) return;
                if (inv.getName().equals(Menu.this.title) && inv.equals(Menu.this.inventory) && slot <= Menu.this.rows * 9 - 1) {
                    event.setCancelled(true);
                    if (Menu.this.hasAction(slot)) {
                        Menu.this.commands.get(slot).run(player, inv, item, slot, a);
                    }
                    if (Menu.this.globalAction != null) {
                        Menu.this.globalAction.run(player, inv, item, slot, a);
                    }
                }
            }
        }, pl);
    }
    

    public boolean hasAction(int slot) {
        return this.commands.containsKey(slot);
    }
    
    @Deprecated
    public void setAction(int slot, ItemAction action) {
        this.commands.put(slot, action);
    }

    public void removeGlobalAction() {
        this.globalAction = null;
    }
    
    @Deprecated
    public void removeAction(int slot) {
        this.commands.remove(slot);
    }
    
    public void runWhenEmpty(boolean state) {
        this.runempty = state;
    }
    
    public int nextOpenSlot() {
        int h = 0;
        for (Integer i : this.content.keySet()) {
            if (i > h) {
                h = i;
            }
        }
        for (int j = 0; j <= h; ++j) {
            if (!this.content.containsKey(j)) {
                return j;
            }
        }
        return h + 1;
    }
    
    public void setContents(ItemStack[] contents) throws ArrayIndexOutOfBoundsException {
        if (contents.length > this.rows * 9) {
            throw new ArrayIndexOutOfBoundsException("setContents() : Contents are larger than inventory.");
        }
        this.content.clear();
        for (int i = 0; i < contents.length; ++i) {
            if (contents[i] != null && contents[i].getType() != Material.AIR) {
                this.content.put(i, contents[i]);
            }
        }
    }
    
    public void addItem(ItemStack item) {
        if (this.nextOpenSlot() > this.rows * 9 - 1) {
            RevampHCF.getInstance().getLogger().info("addItem() : Inventory is full.");
            return;
        }
        this.setItem(this.nextOpenSlot(), item);
    }
    
    public void setItem(int slot, ItemStack item) throws IndexOutOfBoundsException {
        if (slot < 0 || slot > this.rows * 9 - 1) {
            throw new IndexOutOfBoundsException("setItem() : Slot is outside inventory.");
        }
        if (item == null || item.getType() == Material.AIR) {
            this.removeItem(slot);
            return;
        }
        this.content.put(slot, item);
    }
    
    public void fill(ItemStack item) {
        for (int i = 0; i < this.rows * 9; ++i) {
            this.content.put(i, item);
        }
    }
    
    public void fillRange(int s, int e, ItemStack item) throws IndexOutOfBoundsException {
        if (e <= s) {
            throw new IndexOutOfBoundsException("fillRange() : Ending index must be less than starting index.");
        }
        if (s < 0 || s > this.rows * 9 - 1) {
            throw new IndexOutOfBoundsException("fillRange() : Starting index is outside inventory.");
        }
        if (e < 0 || e > this.rows * 9 - 1) {
            throw new IndexOutOfBoundsException("fillRange() : Ending index is outside inventory.");
        }
        for (int i = s; i <= e; ++i) {
            this.content.put(i, item);
        }
    }
    
    public void removeItem(int slot) {
        this.content.remove(slot);
    }
    
    public ItemStack getItem(int slot) {
        if (this.content.containsKey(slot)) {
            return this.content.get(slot);
        }
        return null;
    }
    
    public void replaceItem(Integer slot, ItemStack itemStack) {
        this.inventory.setItem(slot, itemStack);
    }

    public void build() {
        (this.inventory = Bukkit.createInventory(null, this.rows * 9, this.title)).clear();
        for (Map.Entry<Integer, ItemStack> entry : this.content.entrySet()) {
            this.inventory.setItem(entry.getKey(), entry.getValue());
        }
    }
    
    public Inventory getMenu() {
        this.build();
        return this.inventory;
    }
    
    public void showMenu(Player player) {
        player.openInventory(this.getMenu());
    }
    
    public ItemStack[] getContents() {
        return this.getMenu().getContents();
    }
    
    public interface ItemAction
    {
        void run(Player p0, Inventory p1, ItemStack p2, int p3, InventoryAction p4);
    }
}

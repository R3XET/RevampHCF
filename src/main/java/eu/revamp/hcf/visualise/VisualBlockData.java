package eu.revamp.hcf.visualise;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public class VisualBlockData extends MaterialData
{
    public VisualBlockData(Material type) {
        super(type);
    }
    @SuppressWarnings("deprecation")
    public VisualBlockData(Material type, final byte data) {
        super(type, data);
    }
    
    public Material getBlockType() {
        return this.getItemType();
    }
    
    public Material getItemType() {
        return super.getItemType();
    }
    
    @Deprecated
    public ItemStack toItemStack() {
        throw new UnsupportedOperationException("This is a VisualBlock data");
    }
    
    @Deprecated
    public ItemStack toItemStack(int amount) {
        throw new UnsupportedOperationException("This is a VisualBlock data");
    }
}

package eu.revamp.hcf.classes.utils.bard;

import com.google.common.base.Preconditions;
import eu.revamp.hcf.RevampHCF;
import lombok.Getter;
import org.bukkit.scheduler.BukkitTask;

public class BardData
{
    public static double ENERGY_PER_MILLISECOND = 1.0;
    public static double MIN_ENERGY = 0.0;
    public static double MAX_ENERGY = RevampHCF.getInstance().getConfig().getInt("BARD.MAX-ENERGY") + 0.0;
    public long energyStart;
    @Getter public long buffCooldown;
    public static long MAX_ENERGY_MILLIS = (long)(BardData.MAX_ENERGY * 1000.0);
    @Getter public BukkitTask heldTask;

    public void setBuffCooldown(long millis) {
        this.buffCooldown = System.currentTimeMillis() + millis;
    }
    
    public long getRemainingBuffDelay() {
        return this.buffCooldown - System.currentTimeMillis();
    }
    
    public void startEnergyTracking() {
        this.setEnergy(0.0);
    }
    
    public long getEnergyMillis() {
        if (this.energyStart == 0L) {
            return 0L;
        }
        return Math.min(BardData.MAX_ENERGY_MILLIS, (long)(BardData.ENERGY_PER_MILLISECOND * (System.currentTimeMillis() - this.energyStart)));
    }
    
    public double getEnergy() {
        return Math.round(this.getEnergyMillis() / 100.0) / 10.0;
    }
    
    public void setEnergy(double energy) {
        Preconditions.checkArgument(energy >= BardData.MIN_ENERGY, "Energy cannot be less than " + BardData.MIN_ENERGY);
        Preconditions.checkArgument(energy <= BardData.MAX_ENERGY, "Energy cannot be more than " + BardData.MAX_ENERGY);
        this.energyStart = (long)(System.currentTimeMillis() - 1000.0 * energy);
    }
}

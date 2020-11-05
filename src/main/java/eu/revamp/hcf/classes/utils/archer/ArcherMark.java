package eu.revamp.hcf.classes.utils.archer;

import org.bukkit.scheduler.BukkitTask;

public class ArcherMark implements Comparable<ArcherMark>
{
    public BukkitTask decrementTask;
    public int currentLevel;
    
    public void reset() {
        if (this.decrementTask != null) {
            this.decrementTask.cancel();
            this.decrementTask = null;
        }
        this.currentLevel = 0;
    }
    
    public int incrementMark() {
        return ++this.currentLevel;
    }
    
    public int decrementMark() {
        return --this.currentLevel;
    }
    
    @Override
    public int compareTo(ArcherMark archerMark) {
        return Integer.compare(this.currentLevel, archerMark.currentLevel);
    }
}

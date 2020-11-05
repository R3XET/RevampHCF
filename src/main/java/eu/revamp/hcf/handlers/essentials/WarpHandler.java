package eu.revamp.hcf.handlers.essentials;

import eu.revamp.hcf.RevampHCF;
import lombok.Getter;

import java.util.ArrayList;

import lombok.Setter;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;
@Getter @Setter
public class WarpHandler extends Handler implements Listener
{
    private ArrayList<String> warps;
    
    public WarpHandler(RevampHCF instance) {
        super(instance);
        setWarps(new ArrayList<>());
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @Override
    public void disable() {
        getWarps().clear();
    }
}

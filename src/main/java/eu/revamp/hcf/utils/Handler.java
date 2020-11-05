package eu.revamp.hcf.utils;

import eu.revamp.hcf.RevampHCF;
import lombok.Getter;

public class Handler
{
    @Getter private final RevampHCF instance;
    
    public Handler(RevampHCF instance) {
        this.instance = instance;
    }
    
    public void enable() {
    }
    
    public void disable() {
    }
}

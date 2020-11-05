package eu.revamp.hcf.utils.timer;

import lombok.Getter;

public abstract class Timer
{
    @Getter protected String name;
    protected long defaultCooldown;
    
    public Timer(String name, final long defaultCooldown) {
        this.name = name;
        this.defaultCooldown = defaultCooldown;
    }
}

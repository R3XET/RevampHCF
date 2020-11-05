package eu.revamp.hcf.factions.utils.struction;

import eu.revamp.spigot.utils.chat.color.CC;
import lombok.Getter;
import eu.revamp.hcf.utils.inventory.BukkitUtils;
import org.bukkit.DyeColor;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.ChatColor;

public enum Relation
{
    MEMBER("MEMBER", 0, 3), 
    ALLY("ALLY", 1, 2), 
    ENEMY("ENEMY", 2, 1);
    
    @Getter private int value;
    
    Relation(String s, int n, int value) {
        this.value = value;
    }

    public boolean isAtLeast(Relation relation) {
        return this.value >= relation.value;
    }
    
    public boolean isAtMost(Relation relation) {
        return this.value <= relation.value;
    }
    
    public boolean isMember() {
        return this == Relation.MEMBER;
    }
    
    public boolean isAlly() {
        return this == Relation.ALLY;
    }
    
    public boolean isEnemy() {
        return this == Relation.ENEMY;
    }
    
    public String getDisplayName() {
        switch (this) {
            case ALLY: {
                return this.toChatColour() + "alliance";
            }
            default: {
                return this.toChatColour() + this.name().toLowerCase();
            }
        }
    }
    
    public ChatColor toChatColour() {
        switch (this) {
            case MEMBER: {
                return RevampHCF.getInstance().getConfiguration().getTeammateColor();
            }
            case ALLY: {
                return RevampHCF.getInstance().getConfiguration().getAllyColor();
            }
            default: {
                return ChatColor.YELLOW;
            }
        }
    }
    
    public DyeColor toDyeColour() {
        return CC.toDyeColor(this.toChatColour());
    }
}

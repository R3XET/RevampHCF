package eu.revamp.hcf.file;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.spigot.utils.serialize.BukkitSerilization;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter @Setter
public class ConfigHandler extends Handler
{
    private ItemStack[] firstJoinItems;
    private List<String> freezeDisabledCommands;
    private List<String> freezeCommandMessage; //TODO UNUSED
    private List<String> mobStackingEntity;

    public ConfigHandler(RevampHCF plugin) {
        super(plugin);
        if (this.getInstance().getUtilities().getString("first-join-items").equals("")) {
            setFirstJoinItems(new ItemStack[36]);
        }
        else {
            setFirstJoinItems(BukkitSerilization.itemStackArrayFromBase64(this.getInstance().getUtilities().getString("first-join-items")));
        }
        setFreezeDisabledCommands(this.getInstance().getConfig().getStringList("FREEZE-DISABLE-COMMANDS"));
        setMobStackingEntity(this.getInstance().getConfig().getStringList("STACKING-ENTITY"));
    }

    @Override
    public void enable() {
    }

    @Override
    public void disable() {
    }
}

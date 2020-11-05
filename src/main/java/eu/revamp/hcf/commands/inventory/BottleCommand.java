package eu.revamp.hcf.commands.inventory;

import eu.revamp.spigot.utils.generic.Tasks;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import eu.revamp.hcf.handlers.player.ExpHandler;
import java.util.ArrayList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;

public class BottleCommand extends BaseCommand
{
    public BottleCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "bottle";
        this.permission = "revamphcf.command.bottle";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Tasks.runAsync(this.getInstance(), () -> {
            Player player = (Player)sender;
            if (args.length == 0) {
                if (player.getExp() <= 0.0) {
                    player.sendMessage(RevampHCF.getInstance().getConfig().getString("XP_BOTTLE.NO_XP_ERROR_MESSAGE"));
                    return;
                }
                ItemStack experiencePotion = new ItemStack(Material.EXP_BOTTLE);
                ItemMeta experienceMeta = experiencePotion.getItemMeta();
                int xpLevel = ExpHandler.levelToExp(player.getLevel());
                experienceMeta.setDisplayName(RevampHCF.getInstance().getConfig().getString("XP_BOTTLE.NAME"));
                List<String> stringList = new ArrayList<>();
                for (String lore : RevampHCF.getInstance().getConfig().getStringList("XP_BOTTLE.LORE")){
                    stringList.add(lore.replace("%levels%", String.valueOf(player.getLevel())).replace("%points%", String.valueOf(xpLevel)));
                }
                experienceMeta.setLore(stringList);
                experiencePotion.setItemMeta(experienceMeta);
                player.setExp(0.0f);
                player.setLevel(0);
                player.getInventory().addItem(experiencePotion);
            }
        });
    }
}

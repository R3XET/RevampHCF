package eu.revamp.hcf.commands.admin;

import eu.revamp.hcf.RevampHCF;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class EndPortalCommand extends BaseCommand
{
    public EndPortalCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "endportal";
        this.permission = "*";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        if (player.getInventory().firstEmpty() == -1) return;
        ItemStack portalMaker = new ItemStack(Material.BLAZE_ROD);
        ItemMeta itemMeta = portalMaker.getItemMeta();
        itemMeta.setDisplayName("§cEndPortal Maker");
        portalMaker.setItemMeta(itemMeta);
        player.getInventory().addItem(portalMaker);
    }
}

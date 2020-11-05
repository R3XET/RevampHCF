package eu.revamp.hcf.commands.custom;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.hcf.handlers.custom.CustomEnchantHandler;
import eu.revamp.spigot.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CustomEnchantCommand extends BaseCommand {

    public CustomEnchantCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "customenchant";
        this.permission = "revamphcf.command.customenchant";
        this.forPlayerUseOnly = true;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        ItemStack speed = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("ENCHANTS.SPEED.NAME")).setLore(this.getInstance().getConfig().getStringList("ENCHANTS.SPEED.LORE")).toItemStack();
        ItemStack fire = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("ENCHANTS.FIRE.NAME")).setLore(this.getInstance().getConfig().getStringList("ENCHANTS.FIRE.LORE")).toItemStack();
        ItemStack str = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("ENCHANTS.STRENGTH.NAME")).setLore(this.getInstance().getConfig().getStringList("ENCHANTS.STRENGTH.LORE")).toItemStack();
        ItemStack jump = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("ENCHANTS.JUMP.NAME")).setLore(this.getInstance().getConfig().getStringList("ENCHANTS.JUMP.LORE")).toItemStack();
        ItemStack haste = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("ENCHANTS.HASTE.NAME")).setLore(this.getInstance().getConfig().getStringList("ENCHANTS.HASTE.LORE")).toItemStack();
        ItemStack water = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("ENCHANTS.WATERBREATHING.NAME")).setLore(this.getInstance().getConfig().getStringList("ENCHANTS.WATERBREATHING.LORE")).toItemStack();
        ItemStack vision = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("ENCHANTS.NIGHTVISION.NAME")).setLore(this.getInstance().getConfig().getStringList("ENCHANTS.NIGHTVISION.LORE")).toItemStack();
        ItemStack feast = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("ENCHANTS.IMPLANTS.NAME")).setLore(this.getInstance().getConfig().getStringList("ENCHANTS.IMPLANTS.LORE")).toItemStack();
        ItemStack smelt = new ItemBuilder(Material.ENCHANTED_BOOK).setName(this.getInstance().getConfig().getString("ENCHANTS.SMELT.NAME")).setLore(this.getInstance().getConfig().getStringList("ENCHANTS.SMELT.LORE")).toItemStack();

        ItemStack blue = new ItemBuilder(Material.STAINED_GLASS_PANE).setName(this.getInstance().getConfig().getString("ENCHANTS.GLASS.NAME")).setLore(this.getInstance().getConfig().getStringList("ENCHANTS.GLASS.LORE")).toItemStack();
        ItemStack red = new ItemBuilder(Material.STAINED_GLASS_PANE).setName(this.getInstance().getConfig().getString("ENCHANTS.GLASS.NAME")).setLore(this.getInstance().getConfig().getStringList("ENCHANTS.GLASS.LORE")).toItemStack();

        for (int x = 0; x < CustomEnchantHandler.gui.getSize(); x++) {
            if (CustomEnchantHandler.gui.getItem(x) == null) {
                if (this.getInstance().getConfig().getString("ENCHANT-GUI-GLASS-COLOR").equalsIgnoreCase("BLUE")) {
                    CustomEnchantHandler.gui.setItem(x, blue);
                }
                else if (this.getInstance().getConfig().getString("ENCHANT-GUI-GLASS-COLOR").equalsIgnoreCase("RED")) {
                    CustomEnchantHandler.gui.setItem(x, red);
                }
            }
        }
        CustomEnchantHandler.gui.setItem(18, speed);
        CustomEnchantHandler.gui.setItem(19, fire);
        CustomEnchantHandler.gui.setItem(20, str);
        CustomEnchantHandler.gui.setItem(21, jump);
        CustomEnchantHandler.gui.setItem(22, haste);
        CustomEnchantHandler.gui.setItem(23, water);
        CustomEnchantHandler.gui.setItem(24, vision);
        CustomEnchantHandler.gui.setItem(25, feast);
        CustomEnchantHandler.gui.setItem(26, smelt);
        player.openInventory(CustomEnchantHandler.gui);
    }
}

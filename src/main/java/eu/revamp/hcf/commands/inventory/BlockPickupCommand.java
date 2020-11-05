package eu.revamp.hcf.commands.inventory;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class BlockPickupCommand extends BaseCommand
{
    public BlockPickupCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "block";
        this.permission = "revamphcf.command.block";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        String list = RevampHCF.getInstance().getHandlerManager().getBlockPickupHandler().getListMaterial().get(player.getUniqueId()).toString().replace("[", "§f").replace("]", "").replaceAll(",", "§7,§f");
        int count = RevampHCF.getInstance().getHandlerManager().getBlockPickupHandler().getListMaterial().get(player.getUniqueId()).size();
        if (args.length == 0) {
            for (String str : RevampHCF.getInstance().getLanguage().getStringList("BLOCK.USAGE")) {
                player.sendMessage(CC.translate(str));
            }
            return;
        }
        if (args[0].equalsIgnoreCase("add")) {
            if (args.length != 2) {
                for (String str : RevampHCF.getInstance().getLanguage().getStringList("BLOCK.USAGE")) {
                    player.sendMessage(CC.translate(str));
                }
                return;
            }
            String mat = args[1].toUpperCase();
            if (Material.getMaterial(mat) == null) {
                player.sendMessage(Language.COMMANDS_INVALID_MATERIAL.toString());
                return;
            }
            if (!RevampHCF.getInstance().getHandlerManager().getBlockPickupHandler().getListMaterial().containsKey(player.getUniqueId())) {
                RevampHCF.getInstance().getHandlerManager().getBlockPickupHandler().getListMaterial().put(player.getUniqueId(), new ArrayList<>());
                RevampHCF.getInstance().getHandlerManager().getBlockPickupHandler().getListMaterial().get(player.getUniqueId()).add(Material.valueOf(mat));
                player.sendMessage(CC.translate("&bYou have successfully added &b&l" + mat + " &bblock to your block list."));
                return;
            }
            if (RevampHCF.getInstance().getHandlerManager().getBlockPickupHandler().getListMaterial().get(player.getUniqueId()).size() == 10) {
                player.sendMessage(CC.translate("&cYou have reached limit of 10 blocks."));
                return;
            }
            RevampHCF.getInstance().getHandlerManager().getBlockPickupHandler().getListMaterial().get(player.getUniqueId()).add(Material.valueOf(mat));
            player.sendMessage(CC.translate("&bYou have successfully added &b&l" + mat + " &bblock to your block list."));
        }
        else {
            if (!args[0].equalsIgnoreCase("remove")) {
                if (args[0].equalsIgnoreCase("clear")) {
                    if (!RevampHCF.getInstance().getHandlerManager().getBlockPickupHandler().getListMaterial().containsKey(player.getUniqueId())) {
                        player.sendMessage(CC.translate("&cYour list is clear. Type /block add (material) To add materials."));
                        return;
                    }
                    RevampHCF.getInstance().getHandlerManager().getBlockPickupHandler().getListMaterial().remove(player.getUniqueId());
                    player.sendMessage(CC.translate("&bYou have successfully cleared your block list."));
                }
                if (args[0].equalsIgnoreCase("list")) {
                    if (!RevampHCF.getInstance().getHandlerManager().getBlockPickupHandler().getListMaterial().containsKey(player.getUniqueId())) {
                        player.sendMessage(CC.translate("&cYour list is clear. Type /block add <material> To add materials."));
                        return;
                    }
                    for (String str : RevampHCF.getInstance().getLanguage().getStringList("BLOCK.COUNT")) {
                        player.sendMessage(CC.translate(str).replace("%list%", list).replace("%count%", String.valueOf(count)));
                    }
                }
                return;
            }
            if (args.length != 2) {
                for (String str : RevampHCF.getInstance().getLanguage().getStringList("BLOCK.USAGE")) {
                    player.sendMessage(CC.translate(str));
                }
                return;
            }
            String mat = args[1].toUpperCase();
            if (Material.getMaterial(mat) == null) {
                player.sendMessage(Language.COMMANDS_INVALID_MATERIAL.toString());
                return;
            }
            if (!RevampHCF.getInstance().getHandlerManager().getBlockPickupHandler().getListMaterial().containsKey(player.getUniqueId())) {
                player.sendMessage(CC.translate("&cYour list is clear. Type /block add (material) To add materials."));
                return;
            }
            if (RevampHCF.getInstance().getHandlerManager().getBlockPickupHandler().getListMaterial().get(player.getUniqueId()).contains(Material.valueOf(mat))) {
                RevampHCF.getInstance().getHandlerManager().getBlockPickupHandler().getListMaterial().get(player.getUniqueId()).remove(Material.valueOf(mat));
                player.sendMessage(CC.translate("&bYou have successfully removed &b&l" + mat + " &bblock from your block list."));
                return;
            }
            player.sendMessage(CC.translate("&cYou don't have &l" + mat + " &con your block list."));
        }
    }
}

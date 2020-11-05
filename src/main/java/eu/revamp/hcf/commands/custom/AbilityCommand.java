package eu.revamp.hcf.commands.custom;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AbilityCommand extends BaseCommand {

    public AbilityCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "ability";
        this.permission = "hcf.ability";
        this.forPlayerUseOnly = false;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0){
            for (String str : this.getInstance().getConfig().getStringList("ABILITY.MESSAGE")) {
                sender.sendMessage(CC.translate(str));
            }
        }
        else if (args[0].equalsIgnoreCase("list") || (args[0].equalsIgnoreCase("items"))){
            for (String str : this.getInstance().getConfig().getStringList("ABILITY.LIST")) {
                sender.sendMessage(CC.translate(str));
            }
        }
        else if (args.length == 4 && args[0].equalsIgnoreCase("give") && Bukkit.getServer().getPlayer(args[1]) != null) {
            Player player = Bukkit.getPlayer(args[1]);
            switch (args[2].toLowerCase()) {
                case "malignantegg":
                    ItemStack megg = new ItemStack(Material.EGG, Integer.parseInt(args[3]));
                    ItemMeta metamegg = megg.getItemMeta();
                    metamegg.setDisplayName(this.getInstance().getConfig().getString("ABILITY.MALIGNANTEGG.NAME"));
                    List<String> loremegg = new ArrayList<>();
                    for (String lores : this.getInstance().getConfig().getStringList("ABILITY.MALIGNANTEGG.LORE")) {
                        loremegg.add(CC.translate(lores).replace("%time%", this.getInstance().getConfig().getString("COOLDOWNS.MALIGNANTEGG")));
                    }
                    metamegg.setLore(loremegg);
                    megg.setItemMeta(metamegg);
                    player.getInventory().addItem(megg);
                    player.sendMessage(CC.translate("&eYou have received" + " " + this.getInstance().getConfig().getString("ABILITY.MALIGNANTEGG.NAME")));
                    sender.sendMessage(CC.translate("&eYou have given &f" + player.getName() + " " + this.getInstance().getConfig().getString("ABILITY.MALIGNANTEGG.NAME")));
                    break;
                case "snowball":
                    ItemStack snowball = new ItemStack(Material.SNOW_BALL, Integer.parseInt(args[3]));
                    ItemMeta metasn = snowball.getItemMeta();
                    metasn.setDisplayName(this.getInstance().getConfig().getString("ABILITY.SNOWBALL.NAME"));
                    List<String> loresn = new ArrayList<>();
                    for (String lores : this.getInstance().getConfig().getStringList("ABILITY.SNOWBALL.LORE")) {
                        loresn.add(CC.translate(lores).replace("%time%", this.getInstance().getConfig().getString("COOLDOWNS.SNOWBALL")));
                    }
                    metasn.setLore(loresn);
                    snowball.setItemMeta(metasn);
                    player.getInventory().addItem(snowball);
                    player.sendMessage(CC.translate("&eYou have received" + " " + this.getInstance().getConfig().getString("ABILITY.SNOWBALL.NAME")));
                    sender.sendMessage(CC.translate("&eYou have given &f" + player.getName() + " " + this.getInstance().getConfig().getString("ABILITY.SNOWBALL.NAME")));
                    break;
                case "blockegg":
                    ItemStack egg = new ItemStack(Material.EGG, Integer.parseInt(args[3]));
                    ItemMeta metabe = egg.getItemMeta();
                    metabe.setDisplayName(this.getInstance().getConfig().getString("ABILITY.BLOCKEGG.NAME"));
                    List<String> lorebe = new ArrayList<>();
                    for (String lores : this.getInstance().getConfig().getStringList("ABILITY.BLOCKEGG.LORE")) {
                        lorebe.add(CC.translate(lores).replace("%time%", this.getInstance().getConfig().getString("COOLDOWNS.BLOCKEGG")));
                    }
                    metabe.setLore(lorebe);
                    egg.setItemMeta(metabe);
                    player.getInventory().addItem(egg);
                    player.sendMessage(CC.translate("&eYou have received" + " " + this.getInstance().getConfig().getString("ABILITY.BLOCKEGG.NAME")));
                    sender.sendMessage(CC.translate("&eYou have given &f" + player.getName() + " " + this.getInstance().getConfig().getString("ABILITY.BLOCKEGG.NAME")));
                    break;
                case "switcheregg":
                    ItemStack switcheregg = new ItemStack(Material.EGG, Integer.parseInt(args[3]));
                    ItemMeta metase = switcheregg.getItemMeta();
                    metase.setDisplayName(this.getInstance().getConfig().getString("ABILITY.SWITCHEREGG.NAME"));
                    List<String> lorese = new ArrayList<>();
                    for (String lores : this.getInstance().getConfig().getStringList("ABILITY.SWITCHEREGG.LORE")) {
                        lorese.add(CC.translate(lores).replace("%time%", this.getInstance().getConfig().getString("COOLDOWNS.SWITCHEREGG")));
                    }
                    metase.setLore(lorese);
                    switcheregg.setItemMeta(metase);
                    player.getInventory().addItem(switcheregg);
                    player.sendMessage(CC.translate("&eYou have received" + " " + this.getInstance().getConfig().getString("ABILITY.SWITCHEREGG.NAME")));
                    sender.sendMessage(CC.translate("&eYou have given &f" + player.getName() + " " + this.getInstance().getConfig().getString("ABILITY.SWITCHEREGG.NAME")));
                    break;
                case "grapplinghook":
                    ItemStack ghook = new ItemStack(Material.FISHING_ROD, Integer.parseInt(args[3]));
                    ItemMeta metagh = ghook.getItemMeta();
                    metagh.setDisplayName(this.getInstance().getConfig().getString("ABILITY.GRAPPLINGHOOK.NAME"));
                    List<String> loregh = new ArrayList<>();
                    for (String lores : this.getInstance().getConfig().getStringList("ABILITY.GRAPPLINGHOOK.LORE")) {
                        loregh.add(CC.translate(lores).replace("%time%", this.getInstance().getConfig().getString("COOLDOWNS.GRAPPLINGHOOK")));
                    }
                    metagh.setLore(loregh);
                    ghook.setItemMeta(metagh);
                    player.getInventory().addItem(ghook);
                    player.sendMessage(CC.translate("&eYou have received" + " " + this.getInstance().getConfig().getString("ABILITY.GRAPPLINGHOOK.NAME")));
                    sender.sendMessage(CC.translate("&eYou have given &f" + player.getName() + " " + this.getInstance().getConfig().getString("ABILITY.GRAPPLINGHOOK.NAME")));
                    break;
                case "throwweb":
                    ItemStack throwweb = new ItemStack(Material.WEB, Integer.parseInt(args[3]));
                    ItemMeta metath = throwweb.getItemMeta();
                    metath.setDisplayName(this.getInstance().getConfig().getString("ABILITY.THROWEB.NAME"));
                    List<String> loreth = new ArrayList<>();
                    for (String lores : this.getInstance().getConfig().getStringList("ABILITY.THROWEB.LORE")) {
                        loreth.add(CC.translate(lores).replace("%time%", this.getInstance().getConfig().getString("COOLDOWNS.THROWABLECOBWEB")));
                    }
                    metath.setLore(loreth);
                    throwweb.setItemMeta(metath);
                    player.getInventory().addItem(throwweb);
                    player.sendMessage(CC.translate("&eYou have received" + " " + this.getInstance().getConfig().getString("ABILITY.THROWEB.NAME")));
                    sender.sendMessage(CC.translate("&eYou have given &f" + player.getName() + " " + this.getInstance().getConfig().getString("ABILITY.THROWEB.NAME")));
                    break;
                case "disarmeraxe":
                    ItemStack disarmeraxe = new ItemStack(Material.GOLD_AXE, Integer.parseInt(args[3]));
                    ItemMeta metada = disarmeraxe.getItemMeta();
                    metada.setDisplayName(this.getInstance().getConfig().getString("ABILITY.DISARMERAXE.NAME"));
                    List<String> loreda = new ArrayList<>();
                    for (String lores : this.getInstance().getConfig().getStringList("ABILITY.DISARMERAXE.LORE")) {
                        loreda.add(CC.translate(lores).replace("%time%", this.getInstance().getConfig().getString("COOLDOWNS.DISARMERAXE")));
                    }
                    metada.setLore(loreda);
                    disarmeraxe.setItemMeta(metada);
                    player.getInventory().addItem(disarmeraxe);
                    player.sendMessage(CC.translate("&eYou have received" + " " + this.getInstance().getConfig().getString("ABILITY.DISARMERAXE.NAME")));
                    sender.sendMessage(CC.translate("&eYou have given &f" + player.getName() + " " + this.getInstance().getConfig().getString("ABILITY.DISARMERAXE.NAME")));
                    break;
                case "ninja":
                    ItemStack nstar = new ItemStack(Material.NETHER_STAR, Integer.parseInt(args[3]));
                    ItemMeta metans = nstar.getItemMeta();
                    metans.setDisplayName(this.getInstance().getConfig().getString("ABILITY.NINJA.NAME"));
                    List<String> lorens = new ArrayList<>();
                    for (String lores : this.getInstance().getConfig().getStringList("ABILITY.NINJA.LORE")) {
                        lorens.add(CC.translate(lores).replace("%time%", this.getInstance().getConfig().getString("COOLDOWNS.NINJA")));
                    }
                    metans.setLore(lorens);
                    nstar.setItemMeta(metans);
                    player.getInventory().addItem(nstar);
                    player.sendMessage(CC.translate("&eYou have received" + " " + this.getInstance().getConfig().getString("ABILITY.NINJA.NAME")));
                    sender.sendMessage(CC.translate("&eYou have given &f" + player.getName() + " " + this.getInstance().getConfig().getString("ABILITY.NINJA.NAME")));
                    break;
                case "freezegun":
                    ItemStack ghoe = new ItemStack(Material.GOLD_HOE, Integer.parseInt(args[3]));
                    ItemMeta metafg = ghoe.getItemMeta();
                    metafg.setDisplayName(this.getInstance().getConfig().getString("ABILITY.FREEZEGUN.NAME"));
                    List<String> lorefg = new ArrayList<>();
                    for (String lores : this.getInstance().getConfig().getStringList("ABILITY.FREEZEGUN.LORE")) {
                        lorefg.add(CC.translate(lores).replace("%time%", this.getInstance().getConfig().getString("COOLDOWNS.FREEZEGUN")));
                    }
                    metafg.setLore(lorefg);
                    ghoe.setItemMeta(metafg);
                    player.getInventory().addItem(ghoe);
                    player.sendMessage(CC.translate("&eYou have received" + " " + this.getInstance().getConfig().getString("ABILITY.FREEZEGUN.NAME")));
                    sender.sendMessage(CC.translate("&eYou have given &f" + player.getName() + " " + this.getInstance().getConfig().getString("ABILITY.FREEZEGUN.NAME")));
                    break;
                case "invisdust":
                    ItemStack invdust = new ItemStack(Material.INK_SACK, Integer.parseInt(args[3]));
                    ItemMeta metaid = invdust.getItemMeta();
                    metaid.setDisplayName(this.getInstance().getConfig().getString("ABILITY.INVISDUST.NAME"));
                    List<String> loreid = new ArrayList<>();
                    for (String lores : this.getInstance().getConfig().getStringList("ABILITY.INVISDUST.LORE")) {
                        loreid.add(CC.translate(lores).replace("%time%", this.getInstance().getConfig().getString("COOLDOWNS.INVISDUST")));
                    }
                    metaid.setLore(loreid);
                    invdust.setItemMeta(metaid);
                    player.getInventory().addItem(invdust);
                    player.sendMessage(CC.translate("&eYou have received" + " " + this.getInstance().getConfig().getString("ABILITY.INVISDUST.NAME")));
                    sender.sendMessage(CC.translate("&eYou have given &f" + player.getName() + " " + this.getInstance().getConfig().getString("ABILITY.INVISDUST.NAME")));
                    break;
                case "secondchance":
                    ItemStack secondchance = new ItemStack(Material.FEATHER, Integer.parseInt(args[3]));
                    ItemMeta metass = secondchance.getItemMeta();
                    metass.setDisplayName(this.getInstance().getConfig().getString("ABILITY.SECONDCHANCE.NAME"));
                    List<String> loress = new ArrayList<>();
                    for (String lores : this.getInstance().getConfig().getStringList("ABILITY.SECONDCHANCE.LORE")) {
                        loress.add(CC.translate(lores).replace("%time%", this.getInstance().getConfig().getString("COOLDOWNS.SECONDCHANCE")));
                    }
                    metass.setLore(loress);
                    secondchance.setItemMeta(metass);
                    player.getInventory().addItem(secondchance);
                    player.sendMessage(CC.translate("&eYou have received" + " " + this.getInstance().getConfig().getString("ABILITY.SECONDCHANCE.NAME")));
                    sender.sendMessage(CC.translate("&eYou have given &f" + player.getName() + " " + this.getInstance().getConfig().getString("ABILITY.SECONDCHANCE.NAME")));
                    break;
                case "backpack":
                    ItemStack backpack = new ItemStack(Material.CHEST, Integer.parseInt(args[3]));
                    ItemMeta metabp = backpack.getItemMeta();
                    metabp.setDisplayName(this.getInstance().getConfig().getString("ABILITY.BACKPACK.NAME"));
                    List<String> lorebp = new ArrayList<>();
                    for (String lores : this.getInstance().getConfig().getStringList("ABILITY.BACKPACK.LORE")) {
                        lorebp.add(CC.translate(lores).replace("%time%", this.getInstance().getConfig().getString("COOLDOWNS.BACKPACK")));
                    }
                    metabp.setLore(lorebp);
                    backpack.setItemMeta(metabp);
                    player.getInventory().addItem(backpack);
                    player.sendMessage(CC.translate("&eYou have received" + " " + this.getInstance().getConfig().getString("ABILITY.BACKPACK.NAME")));
                    sender.sendMessage(CC.translate("&eYou have given &f" + player.getName() + " " + this.getInstance().getConfig().getString("ABILITY.BACKPACK.NAME")));
                    break;
                case "rocket":
                    ItemStack rocket = new ItemStack(Material.FIREWORK, Integer.parseInt(args[3]));
                    ItemMeta metark = rocket.getItemMeta();
                    metark.setDisplayName(this.getInstance().getConfig().getString("ABILITY.ROCKET.NAME"));
                    List<String> lorerk = new ArrayList<>();
                    for (String lores : this.getInstance().getConfig().getStringList("ABILITY.ROCKET.LORE")) {
                        lorerk.add(CC.translate(lores).replace("%time%", this.getInstance().getConfig().getString("COOLDOWNS.ROCKET")));
                    }
                    metark.setLore(lorerk);
                    rocket.setItemMeta(metark);
                    player.getInventory().addItem(rocket);
                    player.sendMessage(CC.translate("&eYou have received" + " " + this.getInstance().getConfig().getString("ABILITY.ROCKET.NAME")));
                    sender.sendMessage(CC.translate("&eYou have given &f" + player.getName() + " " + this.getInstance().getConfig().getString("ABILITY.ROCKET.NAME")));
                    break;
                case "pumpkinswapper":
                    ItemStack psword = new ItemStack(Material.IRON_SWORD, Integer.parseInt(args[3]));
                    ItemMeta metaps = psword.getItemMeta();
                    metaps.setDisplayName(this.getInstance().getConfig().getString("ABILITY.PUMPKINSWAPPER.NAME"));
                    List<String> loreps = new ArrayList<>();
                    for (String lores : this.getInstance().getConfig().getStringList("ABILITY.PUMPKINSWAPPER.LORE")) {
                        loreps.add(CC.translate(lores).replace("%time%", this.getInstance().getConfig().getString("COOLDOWNS.PUMPKINSWAPPER")));
                    }
                    metaps.setLore(loreps);
                    psword.setItemMeta(metaps);
                    player.getInventory().addItem(psword);
                    player.sendMessage(CC.translate("&eYou have received" + " " + this.getInstance().getConfig().getString("ABILITY.PUMPKINSWAPPER.NAME")));
                    sender.sendMessage(CC.translate("&eYou have given &f" + player.getName() + " " + this.getInstance().getConfig().getString("ABILITY.PUMPKINSWAPPER.NAME")));
                    break;
                case "pyroball":
                    ItemStack pball = new ItemStack(Material.SNOW_BALL, Integer.parseInt(args[3]));
                    ItemMeta metapb = pball.getItemMeta();
                    metapb.setDisplayName(this.getInstance().getConfig().getString("ABILITY.PYROBALL.NAME"));
                    List<String> lorepb = new ArrayList<>();
                    for (String lores : this.getInstance().getConfig().getStringList("ABILITY.PYROBALL.LORE")) {
                        lorepb.add(CC.translate(lores).replace("%time%", this.getInstance().getConfig().getString("COOLDOWNS.PYROBALL")));
                    }
                    metapb.setLore(lorepb);
                    pball.setItemMeta(metapb);
                    player.getInventory().addItem(pball);
                    player.sendMessage(CC.translate("&eYou have received" + " " + this.getInstance().getConfig().getString("ABILITY.PYROBALL.NAME")));
                    sender.sendMessage(CC.translate("&eYou have given &f" + player.getName() + " " + this.getInstance().getConfig().getString("ABILITY.PYROBALL.NAME")));
                    break;
                case "ultimatepearl":
                    ItemStack upearl = new ItemStack(Material.ENDER_PEARL, Integer.parseInt(args[3]));
                    ItemMeta metaup = upearl.getItemMeta();
                    metaup.setDisplayName(this.getInstance().getConfig().getString("ABILITY.ULTIMATEPEARL.NAME"));
                    List<String> loreup = new ArrayList<>();
                    for (String lores : this.getInstance().getConfig().getStringList("ABILITY.ULTIMATEPEARL.LORE")) {
                        loreup.add(CC.translate(lores).replace("%time%", this.getInstance().getConfig().getString("COOLDOWNS.ULTIMATE-PEARL")));
                    }
                    metaup.setLore(loreup);
                    upearl.setItemMeta(metaup);
                    player.getInventory().addItem(upearl);
                    player.sendMessage(CC.translate("&eYou have received" + " " + this.getInstance().getConfig().getString("ABILITY.ULTIMATEPEARL.NAME")));
                    sender.sendMessage(CC.translate("&eYou have given &f" + player.getName() + " " + this.getInstance().getConfig().getString("ABILITY.ULTIMATEPEARL.NAME")));
                    break;
            }
        } else {
            for (String str : this.getInstance().getConfig().getStringList("ABILITY.MESSAGE")) {
                sender.sendMessage(CC.translate(str));
            }
        }
    }
}
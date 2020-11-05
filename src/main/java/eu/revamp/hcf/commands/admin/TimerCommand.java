package eu.revamp.hcf.commands.admin;

import eu.revamp.hcf.managers.CooldownManager;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.packages.RevampPackages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimerCommand extends BaseCommand {

    public TimerCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "timer";
        this.permission = "revamphcf.admin";
        this.forPlayerUseOnly = false;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0){
            for (String str : this.getInstance().getLanguage().getStringList("TIMER.MESSAGE")) {
                sender.sendMessage(CC.translate(str));
            }
        }
        else if (args[0].equalsIgnoreCase("list")) {
            for (String str : this.getInstance().getLanguage().getStringList("TIMER.LIST")) {
                sender.sendMessage(CC.translate(str));
            }
        }
        else if (args.length == 4 && args[0].equalsIgnoreCase("set") && Bukkit.getServer().getPlayer(args[1]) != null) {
            Player player = Bukkit.getPlayer(args[1]);
            switch (args[2].toLowerCase()) {
                case "malignantegg":
                    if (!CooldownManager.getCooldowns().containsKey("MALIGNANTEGG_DELAY")) {
                        CooldownManager.createCooldown("MALIGNANTEGG_DELAY");
                    }
                    CooldownManager.addCooldown("MALIGNANTEGG_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "snowball":
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("SNOWBALL_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("SNOWBALL_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("SNOWBALL_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "blockeggdefender":
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("BLOCKEGGDEFENDER_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("BLOCKEGGDEFENDER_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("BLOCKEGGDEFENDER_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "blockeggshooter":
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("BLOCKEGGSHOOTER_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("BLOCKEGGSHOOTER_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("BLOCKEGGSHOOTER_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "switcheregg":
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("SWITCHEREGG_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("SWITCHEREGG_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("SWITCHEREGG_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "grapplinghook":
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("GRAPPLINGHOOK_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("GRAPPLINGHOOK_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("GRAPPLINGHOOK_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "throwweb":
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("THROWWEB_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("THROWWEB_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("THROWWEB_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "disarmeraxe":
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("DISARMERAXE_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("DISARMERAXE_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("DISARMERAXE_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "ninja":
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("NINJA_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("NINJA_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("NINJA_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "freezegun":
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("FREEZEGUN_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("FREEZEGUN_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("FREEZEGUN_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "invisdust":
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("INVISDUST_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("INVISDUST_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("INVISDUST_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "secondchance":
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("SECONDCHANCE_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("SECONDCHANCE_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("SECONDCHANCE_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "rocket":
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("ROCKET_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("ROCKET_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("ROCKET_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "pumpkinswapper":
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("PUMPKINSWAPPER_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("PUMPKINSWAPPER_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("PUMPKINSWAPPER_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "pyroball":
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("PYROBALL_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("PYROBALL_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("PYROBALL_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "PORTABLESTRENGTH":
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("PORTABLESTRENGTH_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("PORTABLESTRENGTH_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("PORTABLESTRENGTH_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "randomizer":
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("RANDOMIZER_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("RANDOMIZER_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("RANDOMIZER_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "lumberjack":
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("LUMBERJACK_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("LUMBERJACK_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("LUMBERJACK_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "cooldownbow":
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("COOLDOWNBOW_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("COOLDOWNBOW_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("COOLDOWNBOW_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "cocaine":
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("COCAINE_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("COCAINE_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("COCAINE_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "gapple":
                    if (!CooldownManager.getCooldowns().containsKey("GAPPLE_DELAY")) {
                        CooldownManager.createCooldown("GAPPLE_DELAY");
                    }
                    CooldownManager.addCooldown("GAPPLE_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "enderpearl":
                    if (!CooldownManager.getCooldowns().containsKey("ENDERPEARL_DELAY")) {
                        CooldownManager.createCooldown("ENDERPEARL_DELAY");
                    }
                    CooldownManager.addCooldown("ENDERPEARL_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "goldenhead":
                    if (!CooldownManager.getCooldowns().containsKey("HEADAPPLE_DELAY")) {
                        CooldownManager.createCooldown("HEADAPPLE_DELAY");
                    }
                    CooldownManager.addCooldown("HEADAPPLE_DELAY", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "crapple":
                    if (!CooldownManager.getCooldowns().containsKey("CRAPPLE")) {
                        CooldownManager.createCooldown("CRAPPLE");
                    }
                    CooldownManager.addCooldown("CRAPPLE", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set the " + args[2] + " timer of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
                    break;
                case "all":
                    if (!CooldownManager.getCooldowns().containsKey("PYROBALL_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("PYROBALL_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("PYROBALL_DELAY", player, Double.parseDouble(args[3]));
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("PUMPKINSWAPPER_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("PUMPKINSWAPPER_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("PUMPKINSWAPPER_DELAY", player, Double.parseDouble(args[3]));
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("ROCKET_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("ROCKET_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("ROCKET_DELAY", player, Double.parseDouble(args[3]));
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("SECONDCHANCE_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("SECONDCHANCE_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("SECONDCHANCE_DELAY", player, Double.parseDouble(args[3]));
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("INVISDUST_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("INVISDUST_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("INVISDUST_DELAY", player, Double.parseDouble(args[3]));
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("FREEZEGUN_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("FREEZEGUN_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("FREEZEGUN_DELAY", player, Double.parseDouble(args[3]));
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("NINJA_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("NINJA_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("NINJA_DELAY", player, Double.parseDouble(args[3]));
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("DISARMERAXE_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("DISARMERAXE_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("DISARMERAXE_DELAY", player, Double.parseDouble(args[3]));
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("THROWWEB_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("THROWWEB_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("THROWWEB_DELAY", player, Double.parseDouble(args[3]));
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("GRAPPLINGHOOK_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("GRAPPLINGHOOK_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("GRAPPLINGHOOK_DELAY", player, Double.parseDouble(args[3]));
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("SWITCHEREGG_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("SWITCHEREGG_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("SWITCHEREGG_DELAY", player, Double.parseDouble(args[3]));
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("BLOCKEGGSHOOTER_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("BLOCKEGGSHOOTER_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("BLOCKEGGSHOOTER_DELAY", player, Double.parseDouble(args[3]));
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("BLOCKEGGDEFENDER_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("BLOCKEGGDEFENDER_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("BLOCKEGGDEFENDER_DELAY", player, Double.parseDouble(args[3]));
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("SNOWBALL_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("SNOWBALL_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("SNOWBALL_DELAY", player, Double.parseDouble(args[3]));
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("PORTABLESTRENGTH_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("PORTABLESTRENGTH_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("PORTABLESTRENGTH_DELAY", player, Double.parseDouble(args[3]));
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("RANDOMIZER_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("RANDOMIZER_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("RANDOMIZER_DELAY", player, Double.parseDouble(args[3]));
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("LUMBERJACK_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("LUMBERJACK_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("LUMBERJACK_DELAY", player, Double.parseDouble(args[3]));
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("COOLDOWNBOW_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("COOLDOWNBOW_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("COOLDOWNBOW_DELAY", player, Double.parseDouble(args[3]));
                    if (!eu.revamp.packages.utils.CooldownManager.getCooldowns().containsKey("COCAINE_DELAY")) {
                        RevampPackages.getInstance().getCooldownManager().createCooldown("COCAINE_DELAY");
                    }
                    RevampPackages.getInstance().getCooldownManager().addCooldown("COCAINE_DELAY", player, Double.parseDouble(args[3]));
                    if (!CooldownManager.getCooldowns().containsKey("MALIGNANTEGG_DELAY")) {
                        CooldownManager.createCooldown("MALIGNANTEGG_DELAY");
                    }
                    CooldownManager.addCooldown("MALIGNANTEGG_DELAY", player, Double.parseDouble(args[3]));
                    if (!CooldownManager.getCooldowns().containsKey("ENDERPEARL_DELAY")) {
                        CooldownManager.createCooldown("ENDERPEARL_DELAY");
                    }
                    CooldownManager.addCooldown("ENDERPEARL_DELAY", player, Double.parseDouble(args[3]));
                    if (!CooldownManager.getCooldowns().containsKey("GAPPLE_DELAY")) {
                        CooldownManager.createCooldown("GAPPLE_DELAY");
                    }
                    CooldownManager.addCooldown("GAPPLE_DELAY", player, Double.parseDouble(args[3]));
                    if (!CooldownManager.getCooldowns().containsKey("HEADAPPLE_DELAY")) {
                        CooldownManager.createCooldown("HEADAPPLE_DELAY");
                    }
                    CooldownManager.addCooldown("HEADAPPLE_DELAY", player, Double.parseDouble(args[3]));
                    if (!CooldownManager.getCooldowns().containsKey("CRAPPLE")) {
                        CooldownManager.createCooldown("CRAPPLE");
                    }
                    CooldownManager.addCooldown("CRAPPLE", player, Double.parseDouble(args[3]));
                    player.sendMessage(CC.translate("&eYou have set all timers of " + player.getName() + " to " + Double.parseDouble(args[3]) + "s."));
            }
        } else {
            for (String str : this.getInstance().getLanguage().getStringList("TIMER.MESSAGE")) {
                sender.sendMessage(CC.translate(str));
            }
        }
    }
}

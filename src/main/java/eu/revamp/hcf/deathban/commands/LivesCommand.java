package eu.revamp.hcf.deathban.commands;

import java.io.IOException;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.generic.ConversionUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;

import java.io.File;
import eu.revamp.hcf.commands.BaseCommand;

public class LivesCommand extends BaseCommand
{
    private File deathbanFolder;
    private File deathbansFolder;
    private File livesFolder;
    
    public LivesCommand(RevampHCF plugin) {
        super(plugin);
        this.deathbanFolder = new File(RevampHCF.getInstance().getDataFolder(), "deathban");
        this.deathbansFolder = new File(this.deathbanFolder, "deathbans");
        this.livesFolder = new File(this.deathbanFolder, "lives");
        this.command = "lives";
        this.permission = "revamphcf.command.lives";
    }
    
    @Override @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length == 0) {
                this.sendUsage(player);
            }
            else if (args.length == 1) {
                switch (args[0]) {
                    case "check":
                        player.sendMessage(Language.LIVES_CHECK.toString().replace("%lives%", String.valueOf(this.getLives(player))));
                        break;
                    case "revive":
                        player.sendMessage(Language.LIVES_REVIVE_USAGE.toString());
                        break;
                    case "send":
                        player.sendMessage(Language.LIVES_SEND_USAGE.toString());
                        break;
                    default:
                        this.sendUsage(player);
                        break;
                }
            }
            else if (args.length == 2) {
                switch (args[0]) {
                    case "check":
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null) {
                            player.sendMessage(Language.LIVES_CHECK_OTHER.toString().replace("%lives%", String.valueOf( this.getLives(target))).replace("%player%", target.getName()));
                        } else {
                            player.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
                        }
                        break;
                    case "revive":
                        OfflinePlayer target2 = Bukkit.getOfflinePlayer(args[1]);
                        this.revivePlayer(player, target2);
                        break;
                    case "send":
                        player.sendMessage(Language.LIVES_COMMAND_USAGE.toString());
                        break;
                    default:
                        this.sendUsage(player);
                        break;
                }
            }
            else if (args.length == 3) {
                Player target = Bukkit.getPlayer(args[1]);
                switch (args[0]) {
                    case "send":
                        if (target != null) {
                            if (target != player) {
                                if (ConversionUtils.isInteger(args[2])) {
                                    int amount = Integer.parseInt(args[2]);
                                    this.sendLives(player, target, amount);
                                } else {
                                    player.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
                                }
                            } else {
                                player.sendMessage(Language.LIVES_SEND_YOURSELF.toString());
                            }
                        } else {
                            player.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
                        }
                        break;
                    case "set":
                        if (player.hasPermission("revamphcf.op")) {
                            if (target != null) {
                                if (ConversionUtils.isInteger(args[2])) {
                                    int amount = Integer.parseInt(args[2]);
                                    this.setLives(player, target, amount);
                                } else {
                                    player.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
                                }
                            } else if (ConversionUtils.isInteger(args[2])) {
                                OfflinePlayer offline = Bukkit.getOfflinePlayer(args[1]);
                                int amount2 = Integer.parseInt(args[2]);
                                this.setLives(player, offline, amount2);
                            } else {
                                player.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
                            }
                        } else {
                            player.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
                        }
                        break;
                    case "add":
                        if (player.hasPermission("revamphcf.op")) {
                            if (target != null) {
                                if (ConversionUtils.isInteger(args[2])) {
                                    int amount = Integer.parseInt(args[2]);
                                    this.addLives(player, target, amount);
                                } else {
                                    player.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
                                }
                            } else if (ConversionUtils.isInteger(args[2])) {
                                OfflinePlayer offline = Bukkit.getOfflinePlayer(args[1]);
                                int amount2 = Integer.parseInt(args[2]);
                                this.addLives(player, offline, amount2);
                            } else {
                                player.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
                            }
                        } else {
                            player.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
                        }
                        break;
                    default:
                        this.sendUsage(player);
                        break;
                }
            }
            else {
                this.sendUsage(player);
            }
        }
        else if (sender instanceof ConsoleCommandSender) {
            if (args.length == 0) {
                this.sendUsage(sender);
            }
            else if (args.length == 1) {
                switch (args[0]) {
                    case "check":
                    case "revive":
                    case "send":
                        sender.sendMessage(CC.translate("&cNo console."));
                        break;
                    default:
                        this.sendUsage(sender);
                        break;
                }
            }
            else if (args.length == 2) {
                switch (args[0]) {
                    case "check":
                        Player target3 = Bukkit.getPlayer(args[1]);
                        if (target3 != null) {
                            sender.sendMessage(Language.LIVES_CHECK_OTHER.toString().replace("%lives%", String.valueOf( this.getLives(target3))).replace("%player%", target3.getName()));
                        } else {
                            sender.sendMessage(Language.COMMANDS_PLAYER_NOT_FOUND.toString());
                        }
                        break;
                    case "revive":
                    case "send":
                        sender.sendMessage(CC.translate("&cNo console."));
                        break;
                    default:
                        this.sendUsage(sender);
                        break;
                }
            }
            else if (args.length == 3) {
                switch (args[0]) {
                    case "send":
                        sender.sendMessage(CC.translate("&cNo console."));
                        break;
                    case "set": {
                        Player target3 = Bukkit.getPlayer(args[1]);
                        if (target3 != null) {
                            if (ConversionUtils.isInteger(args[2])) {
                                int amount3 = Integer.parseInt(args[2]);
                                this.setLives(sender, target3, amount3);
                            } else {
                                sender.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
                            }
                        } else if (ConversionUtils.isInteger(args[2])) {
                            OfflinePlayer offline2 = Bukkit.getOfflinePlayer(args[1]);
                            int amount = Integer.parseInt(args[2]);
                            this.setLives(sender, offline2, amount);
                        } else {
                            sender.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
                        }
                        break;
                    }
                    case "add": {
                        Player target3 = Bukkit.getPlayer(args[1]);
                        if (target3 != null) {
                            if (ConversionUtils.isInteger(args[2])) {
                                int amount3 = Integer.parseInt(args[2]);
                                this.addLives(sender, target3, amount3);
                            } else {
                                sender.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
                            }
                        } else if (ConversionUtils.isInteger(args[2])) {
                            OfflinePlayer offline2 = Bukkit.getOfflinePlayer(args[1]);
                            int amount = Integer.parseInt(args[2]);
                            this.addLives(sender, offline2, amount);
                        } else {
                            sender.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
                        }
                        break;
                    }
                    default:
                        this.sendUsage(sender);
                        break;
                }
            }
            else {
                this.sendUsage(sender);
            }
        }
    }
    
    public int getLives(Player player) {
        File file = new File(this.livesFolder, player.getUniqueId().toString() + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        int lives = configuration.getInt("lives");
        return lives;
    }
    
    public void revivePlayer(Player player, OfflinePlayer target) {
        File file = new File(this.livesFolder, player.getUniqueId() + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        int lives = configuration.getInt("lives");
        if (lives > 0) {
            File targetFile = new File(this.deathbansFolder, target.getUniqueId().toString() + ".yml");
            if (targetFile.exists()) {
                targetFile.delete();
                configuration.set("lives", lives - 1);
                try {
                    configuration.save(file);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                player.sendMessage(CC.translate("&eYou have used a life and successfully revived &l" + target.getName()));
            }
            else {
                player.sendMessage(CC.translate("&c&l" + target.getName() + " &cis not death-banned."));
            }
        }
        else {
            player.sendMessage(Language.LIVES_NO_LIVES.toString());
        }
    }
    
    public void sendLives(Player player, Player target, int amount) {
        File file = new File(this.livesFolder, player.getUniqueId() + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        int lives = configuration.getInt("lives");
        if (lives >= Math.abs(amount)) {
            File targetFile = new File(this.livesFolder, target.getUniqueId() + ".yml");
            if (targetFile.exists()) {
                configuration.set("lives", lives - Math.abs(amount));
                YamlConfiguration targetConfiguration = YamlConfiguration.loadConfiguration(targetFile);
                targetConfiguration.set("lives", targetConfiguration.getInt("lives") + Math.abs(amount));
                player.sendMessage(CC.translate("&eYou have successfully sent &f" + Math.abs(amount) + " &elives to &f" + target.getName() + "&e."));
                try {
                    configuration.save(file);
                    targetConfiguration.save(targetFile);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                player.sendMessage(Language.LIVES_NOT_IN_DB.toString().replace("%player%", target.getName()));
            }
        }
        else {
            player.sendMessage(Language.LIVES_INSUFFICIENT_LIVES.toString().replace("%lives%", String.valueOf(Math.abs(amount))));
        }
    }
    
    public void setLives(CommandSender sender, Player target, int amount) {
        File targetFile = new File(this.livesFolder, target.getUniqueId().toString() + ".yml");
        if (targetFile.exists()) {
            YamlConfiguration targetConfiguration = YamlConfiguration.loadConfiguration(targetFile);
            targetConfiguration.set("lives", amount);
            sender.sendMessage(CC.translate("&aYou have successfully set &l" + target.getName() + " &alives to &l" + amount + "&a."));
            target.sendMessage(CC.translate("&eYour lives were set to &f" + amount + " &eby &f" + sender.getName() + "&e."));
            try {
                targetConfiguration.save(targetFile);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            sender.sendMessage(Language.LIVES_NOT_IN_DB.toString().replace("%player%", target.getName()));
        }
    }
    
    public void setLives(CommandSender sender, OfflinePlayer target, int amount) {
        File targetFile = new File(this.livesFolder, target.getUniqueId() + ".yml");
        if (targetFile.exists()) {
            YamlConfiguration targetConfiguration = YamlConfiguration.loadConfiguration(targetFile);
            targetConfiguration.set("lives", amount);
            sender.sendMessage(CC.translate("&aYou have successfully set &l" + target.getName() + " &alives to &l" + amount + "&a."));
            try {
                targetConfiguration.save(targetFile);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            sender.sendMessage(Language.LIVES_NOT_IN_DB.toString().replace("%player%", target.getName()));
        }
    }
    
    public void addLives(CommandSender sender, Player target, int amount) {
        File targetFile = new File(this.livesFolder, target.getUniqueId().toString() + ".yml");
        if (targetFile.exists()) {
            YamlConfiguration targetConfiguration = YamlConfiguration.loadConfiguration(targetFile);
            targetConfiguration.set("lives", targetConfiguration.getInt("lives") + amount);
            sender.sendMessage(CC.translate("&aYou have successfully added &l" + amount + " &alives to &l" + target.getName() + "&a."));
            target.sendMessage(CC.translate("&f" + sender.getName() + " &ehas added &f" + amount + " &elives to your account."));
            try {
                targetConfiguration.save(targetFile);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            sender.sendMessage(Language.LIVES_NOT_IN_DB.toString().replace("%player%", target.getName()));
        }
    }
    
    public void addLives(CommandSender sender, OfflinePlayer target, int amount) {
        File targetFile = new File(this.livesFolder, target.getUniqueId() + ".yml");
        if (targetFile.exists()) {
            YamlConfiguration targetConfiguration = YamlConfiguration.loadConfiguration(targetFile);
            targetConfiguration.set("lives", targetConfiguration.getInt("lives") + amount);
            sender.sendMessage(CC.translate("&aYou have successfully added &l" + amount + " &alives to &l" + target.getName() + "&a."));
            try {
                targetConfiguration.save(targetFile);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            sender.sendMessage(Language.LIVES_NOT_IN_DB.toString().replace("%player%", target.getName()));
        }
    }
    
    public void sendUsage(CommandSender sender) {
        sender.sendMessage(Language.LIVES_COMMAND_USAGE.toString());
    }
}

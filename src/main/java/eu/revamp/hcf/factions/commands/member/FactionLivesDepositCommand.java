package eu.revamp.hcf.factions.commands.member;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.deathban.DeathBanHandler;
import eu.revamp.hcf.factions.type.PlayerFaction;
import java.io.IOException;

import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;

import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.utils.command.CommandArgument;

public class FactionLivesDepositCommand extends CommandArgument
{
    public FactionLivesDepositCommand(RevampHCF plugin) {
        super("depositlives", "Deposits lives into your faction lives.", new String[] { "dlives", "depositl" });
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <amount>.";
    }

    public boolean isInteger(String s) {
        return this.isInteger(s, 10);
    }
    
    public boolean isInteger(String s, int radix) {
        if (s.isEmpty()) {
            return false;
        }
        for (int i = 0; i < s.length(); ++i) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) {
                    return false;
                }
            }
            else if (Character.digit(s.charAt(i), radix) < 0) {
                return false;
            }
        }
        return true;
    }
    
    @Override @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.translate("&cNo console."));
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cCorrect Usage: " + this.getUsage(label)));
            return true;
        }
        if (!this.isInteger(args[1])) {
            sender.sendMessage(Language.COMMANDS_INVALID_NUMBER.toString());
            return true;
        }
        int amount = Integer.parseInt(args[1]);
        Player player = (Player)sender;
        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
            return true;
        }
        if (playerFaction.isRaidable() && !RevampHCF.getInstance().getHandlerManager().getEotwUtils().isEndOfTheWorld()) {
            sender.sendMessage(Language.DEPOSIT_LIVES_RAIDABLE.toString());
            return true;
        }
        DeathBanHandler deathban = RevampHCF.getInstance().getHandlerManager().getDeathBanHandler();
        int currentLives = deathban.getLives(player);
        if (currentLives <= 0) {
            sender.sendMessage(Language.DEPOSIT_LIVES_INVALID_LIVES.toString());
            return true;
        }
        if (currentLives - amount < 0) {
            sender.sendMessage(Language.DEPOSIT_LIVES_NOT_ENOUGH_LIVES.toString());
            return true;
        }
        playerFaction.setLives(playerFaction.getLives() + amount);
        File file = new File(RevampHCF.getInstance().getHandlerManager().getDeathBanHandler().getLivesFolder(), player.getUniqueId() + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        int lives = configuration.getInt("lives");
        configuration.set("lives", lives - amount);
        try {
            configuration.save(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        sender.sendMessage(Language.DEPOSIT_LIVES_DEPOSITED.toString().replace("%lives%", String.valueOf(amount)));
        playerFaction.broadcast(Language.DEPOSIT_LIVES_DEPOSITED_OTHER.toString().replace("%lives%", String.valueOf(amount)).replace("%player%", sender.getName()));
        return true;
    }
}

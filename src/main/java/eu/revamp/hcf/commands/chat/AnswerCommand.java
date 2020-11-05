package eu.revamp.hcf.commands.chat;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AnswerCommand extends BaseCommand {

    public AnswerCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "answer";
        this.permission = "revamphcf.command.answer";
        this.forPlayerUseOnly = true;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        for (String str : RevampHCF.getInstance().getConfig().getConfigurationSection("FAQ.ANSWERS.PREFIX").getKeys(false)) {
            player.sendMessage(CC.translate(RevampHCF.getInstance().getConfig().getString("FAQ.ANSWERS.PREFIX." + str)));
        }
        player.sendMessage(CC.translate(RevampHCF.getInstance().getConfig().getString("FAQ.ANSWERS.ANSWERS." + args[0])));
        for (String str : RevampHCF.getInstance().getConfig().getConfigurationSection("FAQ.ANSWERS.SUFFIX").getKeys(false)) {
            player.sendMessage(CC.translate(RevampHCF.getInstance().getConfig().getString("FAQ.ANSWERS.SUFFIX." + str)));
        }
    }
}
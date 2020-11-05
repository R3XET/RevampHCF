package eu.revamp.hcf.commands.chat;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.spigot.utils.chat.Clickable;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.chat.message.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FaqCommand extends BaseCommand {

    public FaqCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "faq";
        this.permission = "revamphcf.command.faq";
        this.forPlayerUseOnly = true;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        for (String str : RevampHCF.getInstance().getConfig().getConfigurationSection("FAQ.QUESTIONS.PREFIX").getKeys(false)) {
            player.sendMessage(CC.translate(RevampHCF.getInstance().getConfig().getString("FAQ.QUESTIONS.PREFIX." + str)));
        }
        for (String str : RevampHCF.getInstance().getConfig().getConfigurationSection("FAQ.QUESTIONS.QUESTIONS").getKeys(false)) {
            String questions = CC.translate(RevampHCF.getInstance().getConfig().getString("FAQ.QUESTIONS.QUESTIONS." + str));
            String hover = CC.translate(RevampHCF.getInstance().getConfig().getString("FAQ.QUESTIONS.HOVER." + str));
            Clickable clickable = new Clickable();
            clickable.add(questions , hover, "/answer " + str);
            clickable.sendToPlayer((Player) sender);
        }
        for (String str : RevampHCF.getInstance().getConfig().getConfigurationSection("FAQ.QUESTIONS.SUFFIX").getKeys(false)) {
            player.sendMessage(CC.translate(RevampHCF.getInstance().getConfig().getString("FAQ.QUESTIONS.SUFFIX." + str)));
        }
    }
}

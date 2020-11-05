package eu.revamp.hcf.commands.admin;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.world.WorldUtils;
import org.apache.commons.codec.language.bm.Lang;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class SetCommand extends BaseCommand
{
    public SetCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "set";
        this.permission = "*";
        this.forPlayerUseOnly = true;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            for (String str : RevampHCF.getInstance().getLanguage().getStringList("SET.COMMAND_USAGE")) {
                sender.sendMessage(CC.translate(str));
            }
            return;
        }
        switch (args[0].toLowerCase()) {
            case "endexit":
                RevampHCF.getInstance().getLocation().set("World-Spawn.end-exit", WorldUtils.stringifyLocation(player.getLocation()));
                RevampHCF.getInstance().getLocation().save();
                sender.sendMessage(Language.SET_END_EXIT.toString());
                break;
            case "endspawn":
                RevampHCF.getInstance().getLocation().set("World-Spawn.end-spawn", WorldUtils.stringifyLocation(player.getLocation()));
                RevampHCF.getInstance().getLocation().save();
                sender.sendMessage(Language.SET_END_SPAWN.toString());
                break;
            case "spawn":
                RevampHCF.getInstance().getLocation().set("World-Spawn.world-spawn", WorldUtils.stringifyLocation(player.getLocation()));
                RevampHCF.getInstance().getLocation().save();
                sender.sendMessage(Language.SET_SPAWN.toString());
                break;
            case "netherspawn":
                RevampHCF.getInstance().getLocation().set("World-Spawn.nether-spawn", WorldUtils.stringifyLocation(player.getLocation()));
                RevampHCF.getInstance().getLocation().save();
                sender.sendMessage(Language.SET_NETHER_SPAWN.toString());
                break;
            case "portalfreeze":
                RevampHCF.getInstance().getLocation().set("World-Spawn.portal-freeze", WorldUtils.stringifyLocation(player.getLocation()));
                RevampHCF.getInstance().getLocation().save();
                sender.sendMessage(Language.SET_PORTAL_FREEZE.toString());
                break;
            case "eotwffa":
                RevampHCF.getInstance().getLocation().set("World-Spawn.eotw-ffa", WorldUtils.stringifyLocation(player.getLocation()));
                RevampHCF.getInstance().getLocation().save();
                sender.sendMessage(Language.SET_EOTW_FFA.toString());
                break;
        }
    }
}

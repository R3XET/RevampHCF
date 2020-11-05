package eu.revamp.hcf.commands;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.admin.*;
import eu.revamp.hcf.commands.chat.*;
import eu.revamp.hcf.commands.chat.AnswerCommand;
import eu.revamp.hcf.commands.chat.FaqCommand;
import eu.revamp.hcf.commands.custom.*;
import eu.revamp.hcf.commands.factions.*;
import eu.revamp.hcf.commands.factions.StatsCommand;
import eu.revamp.hcf.commands.games.*;
import eu.revamp.hcf.commands.inventory.*;
import eu.revamp.hcf.commands.other.*;
import eu.revamp.hcf.commands.revive.RevampCommand;
import eu.revamp.hcf.commands.staff.*;
import eu.revamp.hcf.commands.teleport.*;
import eu.revamp.hcf.deathban.commands.*;
import eu.revamp.hcf.utils.Handler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.ArrayList;
import java.util.List;

public class EssentialsCommandHandler extends Handler implements CommandExecutor
{
    private final List<BaseCommand> commands;
    
    public EssentialsCommandHandler(RevampHCF plugin) {
        super(plugin);
        this.commands = new ArrayList<>();
        this.commands.add(new FaqCommand(plugin));
        this.commands.add(new AnswerCommand(plugin));
        this.commands.add(new ServerTimeCommand(plugin));
        this.commands.add(new HatCommand(plugin));
        this.commands.add(new KingCommand(plugin));
        this.commands.add(new PositionCommand(plugin));
        this.commands.add(new LFFCommand(plugin));
        this.commands.add(new PlayerTimeCommand(plugin));
        this.commands.add(new FightCommand(plugin));
        this.commands.add(new NearCommand(plugin));
        this.commands.add(new LastDeathsCommand(plugin));
        this.commands.add(new ResetStatsCommand(plugin));
        //this.commands.add(new StopEventCommand(plugin));  //TODO OLD
        //this.commands.add(new StartEventCommand(plugin)); //TODO OLD
        this.commands.add(new EOTWFFACommand(plugin));
        this.commands.add(new StaffReviveCommand(plugin));
        this.commands.add(new invCommand(plugin));
        this.commands.add(new CheckCommand(plugin));
        this.commands.add(new YoutuberCommand(plugin));
        this.commands.add(new FamousCommand(plugin));
        this.commands.add(new PartnerCommand(plugin));
        this.commands.add(new CoordsCommand(plugin));
        this.commands.add(new RebootCommand(plugin));
        this.commands.add(new SaleOFFCommand(plugin));
        this.commands.add(new KeySaleCommand(plugin));
        this.commands.add(new EnderDragonCommand(plugin));
        this.commands.add(new SendCoordsCommand(plugin));
        this.commands.add(new PlayerdistanceCommand(plugin));
        this.commands.add(new ViewdistanceCommand(plugin));
        this.commands.add(new HelpCommand(plugin));
        this.commands.add(new StaffJoinCommand(plugin));
        this.commands.add(new EnderchestCommand(plugin));
        this.commands.add(new GiveCommand(plugin));
        this.commands.add(new ExpCommand(plugin));
        this.commands.add(new GodCommand(plugin));
        this.commands.add(new KickallCommand(plugin));
        this.commands.add(new KillallCommand(plugin));
        this.commands.add(new KillCommand(plugin));
        this.commands.add(new PlaytimeCommand(plugin));
        this.commands.add(new SpawnerCommand(plugin));
        this.commands.add(new DelwarpCommand(plugin));
        this.commands.add(new SetwarpCommand(plugin));
        this.commands.add(new TeleportallCommand(plugin));
        this.commands.add(new WarpCommand(plugin));
        this.commands.add(new RevampCommand(plugin));
        this.commands.add(new CrowbarCommand(plugin));
        this.commands.add(new BlockPickupCommand(plugin));
        this.commands.add(new DeathBanCommand(plugin));
        this.commands.add(new EndPortalCommand(plugin));
        this.commands.add(new EOTWCommand(plugin));
        this.commands.add(new FirstJoinItemsCommand(plugin));
        //this.commands.add(new KoTHCommand(plugin)); //TODO OLD
        this.commands.add(new SaveDataCommand(plugin));
        this.commands.add(new SeenCommand(plugin));
        this.commands.add(new SetCommand(plugin));
        this.commands.add(new SetReclaimCommand(plugin));
        this.commands.add(new SOTWCommand(plugin));
        this.commands.add(new VoteCommand(plugin));
        this.commands.add(new GlowstoneCommand(plugin));
        this.commands.add(new BalanceCommand(plugin));
        this.commands.add(new BottleCommand(plugin));
        this.commands.add(new FocusCommand(plugin));
        this.commands.add(new GappleCommand(plugin));
        this.commands.add(new LivesCommand(plugin));
        this.commands.add(new LogoutCommand(plugin));
        this.commands.add(new MapKitCommand(plugin));
        this.commands.add(new PayCommand(plugin));
        this.commands.add(new PvPTimerCommand(plugin));
        this.commands.add(new ReclaimCommand(plugin));
        this.commands.add(new RegenCommand(plugin));
        this.commands.add(new SpawnCommand(plugin));
        this.commands.add(new StatsCommand(plugin));
        this.commands.add(new ToggleDeathMessagesCommand(plugin));
        this.commands.add(new ToggleLightningCommand(plugin));
        this.commands.add(new DevCommand(plugin));
        this.commands.add(new NewVideoCommand(plugin));
        this.commands.add(new RecordingCommand(plugin));
        this.commands.add(new TimerCommand(plugin));
        //this.commands.add(new CustomEnchantCommand(plugin));
        //this.commands.add(new KitCommand(plugin));
        for (BaseCommand baseCommand : this.commands) {
            this.getInstance().getCommand(baseCommand.command).setExecutor(this);
        }
    }
    
    @Override
    public void disable() {
        this.commands.clear();
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        for (BaseCommand baseCommand : this.commands) {
            if (cmd.getName().equalsIgnoreCase(baseCommand.command)) {
                if (sender instanceof ConsoleCommandSender && baseCommand.forPlayerUseOnly) {
                    sender.sendMessage(Language.COMMANDS_FOR_PLAYER_USE_ONLY.toString());
                    return true;
                }
                if (!sender.hasPermission(baseCommand.permission) && !baseCommand.permission.equals("")) {
                    sender.sendMessage(Language.COMMANDS_NO_PERMISSION_MESSAGE.toString());
                    return true;
                }
                baseCommand.execute(sender, args);
                return true;
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] strings) {
        return null;
    }
}

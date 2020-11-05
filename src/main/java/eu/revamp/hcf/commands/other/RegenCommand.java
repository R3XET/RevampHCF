package eu.revamp.hcf.commands.other;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.BaseCommand;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.utils.struction.RegenStatus;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegenCommand extends BaseCommand
{
    public RegenCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "regen";
        this.permission = "revamphcf.command.regen";
        this.forPlayerUseOnly = true;
    }
    
    @Override @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            sender.sendMessage(Language.FACTIONS_NOFACTION.toString());
            return;
        }
        RegenStatus regenStatus = playerFaction.getRegenStatus();
        switch (regenStatus) {
            case FULL: {
                sender.sendMessage(Language.DTR_FULL.toString());
            }
            case PAUSED: {
                sender.sendMessage(Language.DTR_PAUSED.toString().replace("%time%", DurationFormatUtils.formatDurationWords(playerFaction.getRemainingRegenerationTime(), true, true)));
            }
            case REGENERATING: {
                sender.sendMessage(Language.DTR_REGENERATING.toString().replace("%dtr%", regenStatus.getSymbol() + ' ' + playerFaction.getDeathsUntilRaidable()).replace("%dtr2%", String.valueOf(RevampHCF.getInstance().getConfiguration().getDtrIncrementBetweenUpdate())).replace("%time%", RevampHCF.getInstance().getConfiguration().getDtrWordsBetweenUpdate()).replace("%maxdtr%", DurationFormatUtils.formatDurationWords(this.getRemainingRegenMillis(playerFaction), true, true)));
            }
            default: {
                sender.sendMessage(Language.DTR_ERROR.toString());
            }
        }
    }
    
    public long getRemainingRegenMillis(PlayerFaction faction) {
        long millisPassedSinceLastUpdate = System.currentTimeMillis() - faction.getLastDtrUpdateTimestamp();
        double dtrRequired = faction.getMaximumDeathsUntilRaidable() - faction.getDeathsUntilRaidable();
        return (long)(RevampHCF.getInstance().getConfiguration().getDtrUpdate() / RevampHCF.getInstance().getConfiguration().getDtrIncrementBetweenUpdate() * dtrRequired) - millisPassedSinceLastUpdate;
    }
}

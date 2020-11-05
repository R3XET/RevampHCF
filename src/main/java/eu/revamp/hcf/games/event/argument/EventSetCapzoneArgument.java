package eu.revamp.hcf.games.event.argument;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.FactionManager;
import eu.revamp.hcf.factions.utils.games.CapturableFaction;
import eu.revamp.hcf.factions.utils.games.ConquestFaction;
import eu.revamp.hcf.factions.utils.games.KothFaction;
import eu.revamp.hcf.factions.utils.zone.CaptureZone;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import eu.revamp.hcf.games.ConquestType;
import eu.revamp.hcf.games.KothType;
import eu.revamp.hcf.utils.command.CommandArgument;
import eu.revamp.spigot.utils.chat.color.CC;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

public class EventSetCapzoneArgument extends CommandArgument {

    private final RevampHCF plugin;
    private final int MIN_EVENT_CLAIM_AREA;

    public EventSetCapzoneArgument(RevampHCF plugin) {
        super("setcapzone", "Set capzone area");
        this.plugin = plugin;
        this.permission = "event.command." + this.getName();
        this.MIN_EVENT_CLAIM_AREA = 8;
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <eventName>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(CC.translate("&cPlease specify name of capzone."));
            return false;
        }
        WorldEditPlugin worldEdit = this.plugin.getWorldEdit();
        if (worldEdit == null) {
            sender.sendMessage(CC.translate("&cWorldEdit must be installed to set event capture zone."));
            return false;
        }
        Selection selection = worldEdit.getSelection((Player)sender);
        if (selection == null) {
            sender.sendMessage(CC.translate("&cYou must make a WorldEdit selection to do this."));
            return false;
        }
        if (selection.getWidth() < 2 || selection.getLength() < 2) {
            sender.sendMessage(CC.translate("&cCapzones must be at least &l" + this.MIN_EVENT_CLAIM_AREA + "&cx&l" + this.MIN_EVENT_CLAIM_AREA));
            return false;
        }
        Faction faction3 = this.plugin.getFactionManager().getFaction(args[1]);
        if (!(faction3 instanceof CapturableFaction)) {
            sender.sendMessage(CC.translate("&cThere is not a capturable faction named &l" + args[1]));
            return false;
        }
        CapturableFaction capturableFaction = (CapturableFaction)faction3;
        Collection<ClaimZone> claims = capturableFaction.getClaims();
        if (claims.isEmpty()) {
            sender.sendMessage(CC.translate("&cCapture zones can only be inside the event claim."));
            return false;
        }
        ClaimZone claim = new ClaimZone(faction3, selection.getMinimumPoint(), selection.getMaximumPoint());
        World world = claim.getWorld();
        int minimumX = claim.getMinimumX();
        int maximumX = claim.getMaximumX();
        int minimumZ = claim.getMinimumZ();
        int maximumZ = claim.getMaximumZ();
        FactionManager factionManager = this.plugin.getFactionManager();
        for (int x = minimumX; x <= maximumX; ++x) {
            for (int z = minimumZ; z <= maximumZ; ++z) {
                Faction factionAt = factionManager.getFactionAt(world, x, z);
                if (factionAt != capturableFaction) {
                    sender.sendMessage(CC.translate("&cCapture zones can only be inside the event claim."));
                    return false;
                }
            }
        }
        CaptureZone captureZone;
        if (capturableFaction instanceof ConquestFaction) {
            if (args.length < 3) {
                return false;
            }
            ConquestFaction conquestFaction = (ConquestFaction)capturableFaction;
            ConquestFaction.ConquestZone conquestZone = ConquestFaction.ConquestZone.getByName(args[2]);
            if (conquestZone == null) {
                sender.sendMessage(CC.translate("&cThere is no conquest zone named &l" + args[2]));
                sender.sendMessage(CC.translate("&cDid you mean?: &l" + StringUtils.join(ConquestFaction.ConquestZone.getNames(), ", ")));
                return false;
            }
            captureZone = new CaptureZone(conquestZone.getName(), conquestZone.getColor().toString(), claim, ConquestType.DEFAULT_CAP_MILLIS);
            conquestFaction.setZone(conquestZone, captureZone);
        }
        else {
            if (!(capturableFaction instanceof KothFaction)) {
                sender.sendMessage(CC.translate("&cCan only set capture zones for Conquest or KOTH factions."));
                return false;
            }
            ((KothFaction)capturableFaction).setCaptureZone(captureZone = new CaptureZone(capturableFaction.getName(), claim, KothType.DEFAULT_CAP_MILLIS));
        }
        sender.sendMessage(CC.translate("&eYou have successfully set capture zone &f" + captureZone.getDisplayName() + " &efor faction &f" + faction3.getName()));
        return false;
    }
}

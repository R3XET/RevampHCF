package eu.revamp.hcf.commands.admin;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.FactionManager;
import eu.revamp.spigot.utils.world.WorldUtils;
import org.bukkit.World;
import java.util.Collection;
import eu.revamp.hcf.factions.Faction;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Location;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import eu.revamp.hcf.factions.type.GlowstoneFaction;
import org.bukkit.ChatColor;
import java.io.IOException;
import com.sk89q.worldedit.world.DataException;
import com.sk89q.worldedit.MaxChangedBlocksException;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.spigot.utils.chat.color.CC;
import java.io.File;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.commands.BaseCommand;

public class GlowstoneCommand extends BaseCommand
{
    public GlowstoneCommand(RevampHCF plugin) {
        super(plugin);
        this.command = "glowstone";
        this.permission = "*";
        this.forPlayerUseOnly = true;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        if (args.length == 0) {
            this.sendUsage(sender);
            return;
        }
        if (args[0].equalsIgnoreCase("set")) {
            File file = new File(this.getInstance().getDataFolder(), "glowstone-mountain.schematic");
            if (!file.exists()) {
                player.sendMessage(CC.translate("&cThere is no glowstone-mountain schematic!"));
                return;
            }
            RevampHCF.getInstance().getLocation().set("glowstone-location", WorldUtils.stringifyLocation(player.getLocation()));
            RevampHCF.getInstance().getLocation().save();
            sender.sendMessage(CC.translate("&aYou have successfully set Glowstone location."));
        }
        else if (args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("force")) {
            File file = new File(this.getInstance().getDataFolder(), "glowstone-mountain.schematic");
            if (!file.exists()) {
                player.sendMessage(CC.translate("&cThere is no glowstone-mountain schematic!"));
                return;
            }
            Location loc = WorldUtils.destringifyLocation(RevampHCF.getInstance().getLocation().getString("glowstone-location"));
            if (loc == null) {
                System.out.println("§cGLOWSTONE MOUNTAIN IS NOT SET!");
            }
            else {
                try {
                    this.getInstance().getHandlerManager().getGlowstoneMountainHandler().loadGlowstone(loc);
                    player.sendMessage(CC.translate("&cYou have reset glowstone mountain!"));
                }
                catch (MaxChangedBlocksException | DataException | IOException ex2) {
                    ex2.printStackTrace();
                }
            }
        }
        else {
            if (!args[0].equalsIgnoreCase("setarea")) return;
            WorldEditPlugin worldEdit = RevampHCF.getInstance().getWorldEdit();
            if (worldEdit == null) {
                sender.sendMessage(CC.translate("&cYou must have WorldEdit to do this."));
                return;
            }
            Selection selection = worldEdit.getSelection((Player)sender);
            if (selection == null) {
                sender.sendMessage(CC.translate("&cYou must have WorldEdit to do this."));
                return;
            }
            if (selection.getWidth() < 2 || selection.getLength() < 2) {
                sender.sendMessage(ChatColor.RED + "Capture zones must be at least " + 2 + 'x' + 2 + '.');
                return;
            }
            Faction faction = RevampHCF.getInstance().getFactionManager().getFaction("Glowstone");
            if (!(faction instanceof GlowstoneFaction)) {
                sender.sendMessage(ChatColor.RED + "There is not a capturable faction named '" + args[1] + "'.");
                return;
            }
            GlowstoneFaction capturableFaction = (GlowstoneFaction)faction;
            Collection<ClaimZone> claims = capturableFaction.getClaims();
            if (claims.isEmpty()) {
                sender.sendMessage(ChatColor.RED + "Capture zones can only be inside the event claim.");
                return;
            }
            ClaimZone claim = new ClaimZone(faction, selection.getMinimumPoint(), selection.getMaximumPoint());
            World world = claim.getWorld();
            int minimumX = claim.getMinimumX();
            int maximumX = claim.getMaximumX();
            int minimumZ = claim.getMinimumZ();
            int maximumZ = claim.getMaximumZ();
            FactionManager factionManager = RevampHCF.getInstance().getFactionManager();
            for (int x = minimumX; x <= maximumX; ++x) {
                for (int z = minimumZ; z <= maximumZ; ++z) {
                    Faction factionAt = factionManager.getFactionAt(world, x, z);
                    if (!factionAt.equals(capturableFaction)) {
                        sender.sendMessage(ChatColor.RED + "Capture zones can only be inside the event claim.");
                        return;
                    }
                }
            }
            capturableFaction.setGlowstoneArea(claim);
            sender.sendMessage("You set the glowstone area for glowstone palace");
        }
    }
    
    public void sendUsage(CommandSender sender) {
        sender.sendMessage(CC.translate("&cGlowstone Mountain - Help Commands"));
        sender.sendMessage(CC.translate("&c/glowstone set - Set Glowstone area."));
        sender.sendMessage(CC.translate("&c/glowstone reset - Reset Glowstone."));
        sender.sendMessage(CC.translate("&c/glowstone setarea - Set Capzone Glowstone Area."));
    }
}

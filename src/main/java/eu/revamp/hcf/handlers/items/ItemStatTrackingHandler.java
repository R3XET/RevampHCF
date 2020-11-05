package eu.revamp.hcf.handlers.items;

import eu.revamp.spigot.utils.chat.color.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.EventHandler;
import org.bukkit.Material;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.apache.commons.lang.time.FastDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import java.util.Arrays;
import org.bukkit.entity.Player;
import java.util.regex.Matcher;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import eu.revamp.hcf.RevampHCF;
import java.util.regex.Pattern;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class ItemStatTrackingHandler extends Handler implements Listener
{
    @Getter @Setter private Pattern pattern;
    
    public ItemStatTrackingHandler(RevampHCF plugin) {
        super(plugin);
        setPattern(Pattern.compile("\\d+"));
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }

    public boolean containsStatTrakLore(ItemStack stack) {
        String line = CC.translate("&6&lKills&7: &f");
        for (String lore : stack.getItemMeta().getLore()) {
            if (lore.startsWith(line)) {
                return true;
            }
        }
        return false;
    }
    
    public int getKillCount(ItemStack stack) {
        String line = CC.translate("&6&lKills&7: &f");
        for (String lore : stack.getItemMeta().getLore()) {
            if (lore.startsWith(line)) {
                Matcher matc = getPattern().matcher(ChatColor.stripColor(lore));
                if (matc.find()) {
                    return Integer.parseInt(matc.group());
                }
            }
        }
        return 0;
    }
    
    public void setNewStatTrakLore(Player player, Player target, ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        meta.setLore(Arrays.asList("", CC.translate("&6&lKills&7: &f1"), "", this.getKillLine(player, target)));
        stack.setItemMeta(meta);
    }
    
    public void setNewStatTrakWithLore(Player player, Player target, ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = meta.getLore();
        lore.add("");
        lore.add(CC.translate("&6&lKills&7: &f1"));
        lore.add("");
        lore.add(this.getKillLine(player, target));
        meta.setLore(lore);
        stack.setItemMeta(meta);
    }
    
    public String getKillLine(Player player, Player target) {
        FastDateFormat date = FastDateFormat.getInstance(RevampHCF.getInstance().getConfig().getString("DATE_FORMAT"), TimeZone.getTimeZone(RevampHCF.getInstance().getConfig().getString("TIMEZONE")), Locale.ENGLISH);
        return CC.translate("&c" + player.getName() + " &ewas slain by &c" + target.getName() + " &9" + date.format(System.currentTimeMillis()));
    }
    
    public void updateStatTrakLore(Player player, Player target, ItemStack stack) {
        if (stack.getItemMeta().hasLore()) {
            if (this.containsStatTrakLore(stack)) {
                ItemMeta meta = stack.getItemMeta();
                int i = meta.getLore().indexOf(CC.translate("&6&lKills&7: &f" + this.getKillCount(stack)));
                List<String> lore = meta.getLore();
                switch (meta.getLore().size() - (i + 1)) {
                    case 2: {
                        lore.set(i, CC.translate("&6&lKills&7: &f" + (this.getKillCount(stack) + 1)));
                        lore.add(lore.size() - 1, this.getKillLine(player, target));
                        break;
                    }
                    case 3: {
                        lore.set(i, CC.translate("&6&lKills&7: &f" + (this.getKillCount(stack) + 1)));
                        lore.add(lore.size() - 2, this.getKillLine(player, target));
                        break;
                    }
                    case 4: {
                        lore.set(i, CC.translate("&6&lKills&7: &f" + (this.getKillCount(stack) + 1)));
                        lore.add(lore.size() - 3, this.getKillLine(player, target));
                        lore.remove(lore.size() - 1);
                        break;
                    }
                }
                meta.setLore(lore);
                stack.setItemMeta(meta);
            }
            else {
                this.setNewStatTrakWithLore(player, target, stack);
            }
        }
        else {
            this.setNewStatTrakLore(player, target, stack);
        }
    }
    
    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        if (victim == killer || killer == null) return;
        ItemStack stack = killer.getItemInHand();
        if (stack == null || stack.getType() == Material.AIR) return;
        this.updateStatTrakLore(victim, killer, stack);
    }
}

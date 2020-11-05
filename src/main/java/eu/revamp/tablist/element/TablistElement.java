package eu.revamp.tablist.element;

import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.tablist.RevampTab;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.List;
@Getter @Setter
public class TablistElement {

    private String display;
    private int slot;

    public TablistElement(String display, int slot) {
        setDisplay( CC.translate(display));
        setSlot(slot);
    }

    public static TablistElement getByPosition(Player player, int slot) {
        TablistElement returnElement = null;
        List<TablistElement> elements;
        int size;
        if (RevampTab.getInstance().getTablistVersion().getSlots(player) == 80) {
            elements = RevampTab.getInstance().getTablist().getTabElements(player);
            size = elements.size();
            for (int i = 0; i < size; ++i) {
                if (elements.get(i).getSlot() != slot) continue;
                returnElement = elements.get(i);
                break;
            }
        } else {
            int algorithm = ((slot % 3 == 0 ? 3 : slot % 3) - 1) * 20 + (int)Math.ceil((float)slot / 3.0f);
            elements = RevampTab.getInstance().getTablist().getTabElements(player);
            size = elements.size();
            for (int i = 0; i < size; ++i) {
                if (elements.get(i).getSlot() != algorithm) continue;
                returnElement = elements.get(i);
                break;
            }
        }
        return returnElement;
    }
}

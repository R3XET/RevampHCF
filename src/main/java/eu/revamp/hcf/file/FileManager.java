package eu.revamp.hcf.file;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Handler;

import java.io.File;

public class FileManager extends Handler {
    public FileManager(RevampHCF instance) {
        super(instance);
    }
    public void setupDirectories() {
        if (!this.getInstance().getDataFolder().exists()) {
            this.getInstance().getDataFolder().mkdir();
        }
        File playerdata = new File(this.getInstance().getDataFolder(), "playerdata");
        if (!playerdata.exists()) {
            playerdata.mkdir();
        }
        File deathban = new File(this.getInstance().getDataFolder(), "deathban");
        if (!deathban.exists()) {
            deathban.mkdir();
        }
        File deathbans = new File(deathban, "deathbans");
        if (!deathbans.exists()) {
            deathbans.mkdir();
        }
        File inventories = new File(deathban, "inventories");
        if (!inventories.exists()) {
            inventories.mkdir();
        }
        File lives = new File(deathban, "lives");
        if (!lives.exists()) {
            lives.mkdir();
        }
        File staff = new File(this.getInstance().getDataFolder(), "staff");
        if (!staff.exists()) {
            staff.mkdir();
        }
        File staffCommand = new File(staff, "command");
        if (!staffCommand.exists()) {
            staffCommand.mkdir();
        }
        File staffBlock = new File(staff, "block");
        if (!staffBlock.exists()) {
            staffBlock.mkdir();
        }
        File staffDrop = new File(staff, "drop");
        if (!staffDrop.exists()) {
            staffDrop.mkdir();
        }
        File staffChat = new File(staff, "chat");
        if (!staffChat.exists()) {
            staffChat.mkdir();
        }
        File staffJL = new File(staff, "joinleave");
        if (!staffJL.exists()) {
            staffJL.mkdir();
        }
    }
}

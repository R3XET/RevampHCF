package eu.revamp.hcf.file;

import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.hcf.RevampHCF;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigFileOld {

    @Getter private File file;
    @Getter private YamlConfiguration configuration;
    
    public ConfigFileOld() {
        this.load();
    }
    
    public void load() {
        this.file = new File(RevampHCF.getInstance().getDataFolder(), "config.yml");
        if (!this.file.exists()) {
            RevampHCF.getInstance().saveResource("config.yml", false);
        }
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }
    
    public void save() {
        try {
            this.configuration.save(this.file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void set(String path, Object value){
        try{
            this.configuration.set(path, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ConfigurationSection getConfigurationSection(String path){
        return this.configuration.getConfigurationSection(path);
    }
    public double getDouble(String path) {
        if (this.configuration.contains(path)) {
            return this.configuration.getDouble(path);
        }
        return 0.0;
    }
    
    public int getInt(String path) {
        if (this.configuration.contains(path)) {
            return this.configuration.getInt(path);
        }
        return 0;
    }
    
    public boolean getBoolean(String path) {
        return this.configuration.contains(path) && this.configuration.getBoolean(path);
    }

    public String getString(String path) {
        if (this.configuration.contains(path)) {
            return CC.translate(this.configuration.getString(path));
        }
        return "String at path: " + path + " not found!";
    }
    
    public long getLong(String path) {
        if (this.configuration.contains(path)) {
            return this.configuration.getLong(path);
        }
        return 0L;
    }
    
    public List<String> getStringList(String path) {
        if (this.configuration.contains(path)) {
            ArrayList<String> strings = new ArrayList<>();
            for (String string : this.configuration.getStringList(path)) {
                strings.add(CC.translate(string));
            }
            return strings;
        }
        return Arrays.asList("String List at path: " + path + " not found!");
    }

    public ChatColor getColor(String path) {
        if (this.configuration.contains(path)) {
            return ChatColor.valueOf(this.configuration.getString(path));
        }
        return ChatColor.RED;
    }

    public Material getMaterial(String path){
        if (this.configuration.contains(path)) {
            return Material.valueOf(this.configuration.getString(path));
        }
        return null;
    }

    public boolean isList(String path) {
        return this.configuration.isList(path);
    }
}

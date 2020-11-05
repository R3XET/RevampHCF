package eu.revamp.hcf.utils.inventory.item;

import java.io.BufferedReader;
import java.util.Collections;

import lombok.Getter;
import eu.revamp.hcf.RevampHCF;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.math.BigInteger;
import java.io.OutputStreamWriter;
import java.security.DigestOutputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public class ManagedFile
{
    private static int BUFFER_SIZE = 8192;
    @Getter private transient File file;
    
    public ManagedFile(String filename, JavaPlugin plugin) {
        this.file = new File(plugin.getDataFolder(), filename);
        if (!this.file.exists()) {
            try {
                copyResourceAscii('/' + filename, this.file);
            }
            catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "items.csv has not been loaded", ex);
            }
        }
    }
    
    public static void copyResourceAscii(String resourceName, File file) throws IOException {
        Throwable t = null;
        try {
            try (InputStreamReader reader = new InputStreamReader(ManagedFile.class.getResourceAsStream(resourceName), StandardCharsets.UTF_8)) {
                MessageDigest digest = getDigest();
                Throwable t2 = null;
                {
                    try (DigestOutputStream digestStream = new DigestOutputStream(new FileOutputStream(file), digest)) {
                        try (OutputStreamWriter writer = new OutputStreamWriter(digestStream, StandardCharsets.UTF_8)) {
                            char[] buffer = new char[8192];
                            int length;
                            while ((length = reader.read(buffer)) >= 0) {
                                writer.write(buffer, 0, length);
                            }
                            writer.write("\n");
                            writer.flush();
                            digestStream.on(false);
                            digestStream.write(35);
                            digestStream.write(new BigInteger(1, digest.digest()).toString(16).getBytes(StandardCharsets.UTF_8));
                        }
                        if (digestStream != null) {
                            digestStream.close();
                        }
                    } finally {
                        if (t2 == null) {
                            Throwable t3 = null;
                            t2 = t3;
                        } else {
                            Throwable t3 = null;
                            if (t2 != t3) {
                                t2.addSuppressed(t3);
                            }
                        }
                    }
                }
            }
        }
        finally {
            if (t == null) {
                Throwable t4 = null;
                t = t4;
            }
            else {
                Throwable t4 = null;
                if (t != t4) {
                    t.addSuppressed(t4);
                }
            }
        }
    }
    
    public static MessageDigest getDigest() throws IOException {
        try {
            return MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException ex) {
            throw new IOException(ex);
        }
    }

    public List<String> getLines() {
        try {
            Throwable t = null;
            try {
                try (BufferedReader reader = Files.newBufferedReader(Paths.get(this.file.getPath()), StandardCharsets.UTF_8)) {
                    List<String> lines = new ArrayList<>();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        lines.add(line);
                    }
                    return lines;
                }
            }
            finally {
                if (t == null) {
                    Throwable t2 = null;
                    t = t2;
                }
                else {
                    Throwable t2 = null;
                    if (t != t2) {
                        t.addSuppressed(t2);
                    }
                }
            }
        }
        catch (IOException ex) {
            RevampHCF.getInstance().getLogger().log(Level.SEVERE, ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }
    
    public static int getBufferSize() {
        return ManagedFile.BUFFER_SIZE;
    }
}

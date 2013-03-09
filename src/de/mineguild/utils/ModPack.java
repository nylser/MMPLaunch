package de.mineguild.utils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * User: korbinian
 * Date: 04.03.13
 * Time: 18:30
 */

@XmlRootElement(name = "modpack")
public class ModPack {

    private String version;
    private List<Mod> modList = new ArrayList<Mod>();

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @XmlElement(name = "mod")
    public List<Mod> getMods() {
        return modList;
    }

    public void setMods(List<Mod> modList) {
        this.modList = modList;
    }

    public boolean contains(List<Mod> mods) {
        if (modList.containsAll(mods)) {
            return true;
        }
        return false;
    }

    public boolean isEmpty() {
        if (modList.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean addMod(Mod mod) {
        for (Mod cmod : modList) {
            if (cmod.getMd5().equals(mod.getMd5())) {
                return false;
            }
        }
        System.out.println("Added: " + mod.getFilename());
        modList.add(mod);
        return true;
    }

    public boolean removeMod(Mod mod) {
        for (Mod cmod : modList) {
            if (cmod.getMd5().equals(mod.getMd5())) {
                modList.remove(cmod);
                return true;
            }
        }
        System.out.println("Doesn't contain!");
        return false;
    }

    public List<Mod> getModsByType(ModType type) {
        List<Mod> reList = new ArrayList<Mod>();

        if (modList.isEmpty()) {
            return reList;
        }

        for (Mod mod : modList) {
            if (mod.getType() == type) {
                reList.add(mod);
            }
        }
        return reList;
    }

    public enum ModType {
        INSTMOD, COREMOD, MOD
    }

    public static class Mod {
        private URL url;
        private String filename;
        private ModType type;
        private Boolean installed;
        private String md5;

        public Mod(URL url, String filename, ModType type, Boolean installed) {
            this.url = url;
            this.filename = filename;
            this.type = type;
            this.installed = installed;
        }

        public Mod(String filename, ModType type, Boolean installed, String md5) {
            this.filename = filename;
            this.type = type;
            this.installed = installed;
            this.md5 = md5;
        }

        public Mod() {

        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public URL getUrl() {
            return url;
        }

        public void setUrl(URL url) {
            this.url = url;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public ModType getType() {
            return type;
        }

        public void setType(ModType type) {
            this.type = type;
        }

        public Boolean getInstalled() {
            return installed;
        }

        public void setInstalled(Boolean installed) {
            this.installed = installed;
        }

    }

}

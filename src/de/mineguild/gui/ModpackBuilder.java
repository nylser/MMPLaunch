package de.mineguild.gui;

import de.mineguild.utils.DownloadUtils;
import de.mineguild.utils.ModPack;

import javax.swing.*;
import javax.xml.bind.JAXB;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static de.mineguild.utils.ModPack.Mod;
import static de.mineguild.utils.ModPack.ModType;
import static de.mineguild.utils.ModPack.ModType.*;

public class ModpackBuilder extends JFrame {

    private JPanel mainPanel;
    private JPanel centerPanel;
    private JScrollPane modScrollPane;
    private JTable modTable;
    private JList modList;
    private JLabel label = new JLabel("Test");
    private File instPath = new File("KMP");
    private File source = new File("modpack.xml");
    private ModPack modPack;


    public ModpackBuilder() {
        setSize(300, 300);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();


    }

    public static void main(String[] args) {

        new ModpackBuilder();

    }

    public void initComponents() {

        if (source.exists() && source.isFile()) {
            modPack = JAXB.unmarshal(source, ModPack.class);
        } else {
            modPack = new ModPack();
        }

        if (instPath.exists() && instPath.isDirectory()) {

            for (File mod : new File(instPath, "/instMods").listFiles()) {
                if (mod.isFile()) {
                    modPack.addMod(new Mod(mod.getName(), INSTMOD, true, DownloadUtils.getMD5(mod)));
                }
            }
            for (File mod : new File(instPath, "/minecraft/coremods").listFiles()) {
                if (mod.isFile())
                    modPack.addMod(new Mod(mod.getName(), COREMOD, true, DownloadUtils.getMD5(mod)));
            }
            for (File mod : new File("test").listFiles()) {
                if (mod.isFile())
                    modPack.addMod(new Mod(mod.getName(), MOD, true, DownloadUtils.getMD5(mod)));
            }

            cleanUp(INSTMOD);
            cleanUp(COREMOD);
            cleanUp(MOD);

        }

        HashMap<File, URL> links = new HashMap<File, URL>();
        for (Mod mod : modPack.getModsByType(MOD)) {
            if (mod.getUrl() != null) {
                links.put(new File("test/", mod.getFilename()), DownloadUtils.validateUrl(mod.getUrl(), mod.getFilename()));
            }
        }

        fLoop:
        for (File file : new File("test").listFiles()) {
            if (file.isFile()) {
                String md5 = DownloadUtils.getMD5(file);
                for (Mod mod : modPack.getModsByType(MOD)) {
                    if (mod.getMd5().equals(md5)) {
                        break fLoop;
                    } else if (mod.getFilename().equals(file.getName())) {

                    }
                }
                System.out.println(file.getName() + " is invalid!");
            }
        }
        //Download.show(links);

        //getData();

        JAXB.marshal(modPack, source);

    }

    public void cleanUp(ModType type) {
        File path;
        List<String> fileNames = new ArrayList<String>();
        switch (type) {
            case INSTMOD:
                path = new File(instPath, "/instMods");
                break;
            case COREMOD:
                path = new File(instPath, "/minecraft/coremods");
                break;
            default:
                path = new File("test");
                break;
        }

        for (File f : path.listFiles()) {
            if (f.isFile()) {
                fileNames.add(DownloadUtils.getMD5(f));
            }
        }

        for (Mod mod : modPack.getModsByType(type)) {
            if (!fileNames.contains(mod.getMd5())) {
                modPack.removeMod(mod);
                System.out.println("Cleaned up: " + mod.getFilename());
            }
        }

    }

    public void getData() {
        List<Mod> mods = modPack.getMods();
        for (Mod mod : mods) {
            if (mod.getUrl() == null) {
                try {
                    mod.setUrl(new URL(JOptionPane.showInputDialog(this, mod.getFilename() + "\nUrl:")));
                } catch (MalformedURLException e) {
                    mod.setUrl(null);
                }
            }
            JAXB.marshal(modPack, source);
        }
    }


}

package de.mineguild.mclauncher;

import de.mineguild.Main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;


public class LauncherRelauncher implements Runnable {

    private String originalAppData;

    public LauncherRelauncher(String originalAppData) {
        this.originalAppData = originalAppData;
    }

    @Override
    public void run() {
        String launcherPath;
        try {
            launcherPath = Main.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI().getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException("The path to the launcher could not be discovered.", e);
        }

        ArrayList<String> params = new ArrayList<String>();
        params.add("javaw");
        params.add("-classpath");
        params.add(launcherPath);
        params.add(Main.class.getCanonicalName());
        ProcessBuilder procBuilder = new ProcessBuilder(params);
        if (originalAppData != null) {
            procBuilder.environment().put("APPDATA", originalAppData);
        }
        try {
            procBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException("The launcher could not be started: " + e.getMessage(), e);
        }
    }

}
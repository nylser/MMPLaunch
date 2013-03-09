package de.mineguild;

import de.mineguild.gui.dialoges.Download;
import de.mineguild.gui.dialoges.Login;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: korbinian
 * Date: 23.02.13
 * Time: 21:47
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) throws Exception {


        JPanel panel = new JPanel();
        panel.setVisible(true);

        //Setting Look and Feel to Nimbus
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

        Login dialog = new Login();
        dialog.pack();
        dialog.setVisible(true);

        System.out.println("Logged in and ready!");

        HashMap<File, URL> links = new HashMap<File, URL>();

        try {
            links.put(new File("miscperipherals-3.1c-ccbeta.jar"), new URL("https://dl.dropbox.com/u/861751/Mods/miscperipherals/miscperipherals-3.1c-ccbeta.jar"));
            links.put(new File("Weisse_Rose_Radiobeitrag_9E-FLG+inout_mixdown.mp3"), new URL("http://mineguild.de/archive/wrose/Weisse_Rose_Radiobeitrag_9E-FLG+inout_mixdown.mp3"));
            Download.show(links);
        } catch (Exception ignored) {

        }


    }

}

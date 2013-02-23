package de.mineguild;

import de.mineguild.gui.dialoges.Login;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: korbinian
 * Date: 23.02.13
 * Time: 21:47
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) {

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


    }

}

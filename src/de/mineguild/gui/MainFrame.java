package de.mineguild.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: korbinian
 * Date: 23.02.13
 * Time: 22:29
 * To change this template use File | Settings | File Templates.
 */
public class MainFrame extends JFrame {

    private JTabbedPane tabbedPane;
    private JPanel panel1;
    private JTextPane newsPane;
    private JPanel newsPanel;
    private JPanel hiddenPanel1;
    private JPasswordField passwordField;
    private JButton createModpackFromDirectoryButton;
    private JButton updateModpackFromDirectoryButton;
    private JPanel devPanel;

    //Temporary main method.
    public static void main(String[] args) {

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

        new MainFrame();
    }


    public MainFrame() {
        setSize(300, 300);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();


    }

    private void initComponents() {
        tabbedPane.setFocusable(false);
        tabbedPane.setSelectedComponent(devPanel);
        hiddenPanel1.setVisible(false);
        add(tabbedPane);
        try {
            newsPane.setPage("http://mineguild.de");
            newsPane.setEditable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (new String(passwordField.getPassword()).equals("secret")) {
                    System.out.println("TRUE");
                    hiddenPanel1.setVisible(true);
                }
            }
        });


    }

}

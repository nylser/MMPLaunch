package de.mineguild.gui.dialoges;

import de.mineguild.utils.LoginUtils;

import javax.swing.*;
import java.awt.event.*;

public class Login extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public Login() {
        setContentPane(contentPane);
        setModal(true);

        setSize(10, 10);
        setTitle("Login");
        setLocationRelativeTo(null);

        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        int response = LoginUtils.webLogin(usernameField.getText(), new String(passwordField.getPassword()));
        if (response == LoginUtils.VALID) {
            dispose();
        } else if (response == LoginUtils.VALID_NOT_ACTIVE) {
            JOptionPane.showMessageDialog(this, "This account is not active!", "Account not active", JOptionPane.WARNING_MESSAGE);
        } else if (response == LoginUtils.INVALID) {
            JOptionPane.showMessageDialog(this, "Username and/or password is wrong!", "Incorrect credentials", JOptionPane.WARNING_MESSAGE);
        } else if (response == LoginUtils.ERROR_OCCURRED) {
            JOptionPane.showMessageDialog(this, "An error occurred during the login! Please check your internet connection!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "UNKNOWN RESPONSE! (THIS SHOULDN'T HAPPEN!) ");
            dispose();
        }

    }

    private void onCancel() {
// add your code here if necessary
        System.exit(0);
    }


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

        Login dialog = new Login();
        dialog.pack();
        dialog.setVisible(true);
    }
}

package de.mineguild.utils;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * UI utility methods.
 *
 * @author sk89q
 */
public class UIUtil {

    private static String[] monospaceFontNames = {
            "Consolas", "DejaVu Sans Mono", "Bitstream Vera Sans Mono", "Lucida Console"};

    private UIUtil() {
    }

    /**
     * Browse to a folder.
     *
     * @param file
     * @param component
     */
    public static void browse(File file, Component component) {
        try {
            Desktop.getDesktop().browse(new URL("file://" + file.getAbsolutePath()).toURI());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(component, "Unable to open '" +
                    file.getAbsolutePath() + "'. Maybe it doesn't exist?",
                    "Open failed", JOptionPane.ERROR_MESSAGE);
        } catch (URISyntaxException e) {
        }
    }

    /**
     * Opens a URL.
     *
     * @param url
     * @param component
     */
    public static void openURL(String url, Component component) {
        try {
            openURL(new URL(url), component);
        } catch (MalformedURLException e) {
        }
    }

    /**
     * Opens a URL.
     *
     * @param url
     * @param component
     */
    public static void openURL(URL url, Component component) {
        try {
            Desktop.getDesktop().browse(url.toURI());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(component, "Unable to open '" +
                    url + "'",
                    "Open failed", JOptionPane.ERROR_MESSAGE);
        } catch (URISyntaxException e) {
        }
    }

    /**
     * Shows an error dialog.
     *
     * @param component component
     * @param title     title
     * @param message   message
     */
    public static void showError(Component component, String title, String message) {
        JOptionPane.showMessageDialog(component, message, title, JOptionPane.ERROR_MESSAGE);
        ;
    }

    /**
     * Equalize the width of the given components.
     *
     * @param component component
     */
    public static void equalWidth(Component... component) {
        double widest = 0;
        for (Component comp : component) {
            Dimension dim = comp.getPreferredSize();
            if (dim.getWidth() > widest) {
                widest = dim.getWidth();
            }
        }

        for (Component comp : component) {
            Dimension dim = comp.getPreferredSize();
            comp.setPreferredSize(new Dimension((int) widest, (int) dim.getHeight()));
        }
    }

    /**
     * Remove all the opaqueness of the given components and child components.
     *
     * @param components list of components
     */
    public static void removeOpaqueness(Component... components) {
        for (Component component : components) {
            if (component instanceof JComponent) {
                JComponent jComponent = (JComponent) component;
                jComponent.setOpaque(false);
                removeOpaqueness(jComponent.getComponents());
            }
        }
    }

    /**
     * Get a supported monospace font.
     *
     * @return font
     */
    public static Font getMonospaceFont() {
        for (String fontName : monospaceFontNames) {
            Font font = Font.decode(fontName + "-11");
            if (!font.getFamily().equalsIgnoreCase("Dialog"))
                return font;
        }
        return new Font("Monospace", Font.PLAIN, 11);
    }

}
package de.mineguild.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * PhpPostConnect.java This class can represent a connection to an PHP-Site to send the side data via "POST" and get
 * Data from the side which will be written by side with "echo"
 *
 * @author sparrow
 */
class PhpPostConnect {

    /**
     * Contains the URL to the PHP-Script
     */
    private URL sitepath;

    /**
     * The Connection to the URL
     */
    private URLConnection con;

    /**
     * Empty construct, you must set the URL of the target before you start to send and read data
     */
    public PhpPostConnect() {

    }

    /**
     * Construct which also define the targed URL
     *
     * @param sitepath The URL to the target PHP-Script
     */
    public PhpPostConnect(URL sitepath) {
        this.sitepath = sitepath;
    }

    /**
     * Set the URL to the target PHP-Script
     *
     * @param sitepath The URL to the target PHP-Script
     */
    public void setSitePath(URL sitepath) {
        this.sitepath = sitepath;
    }

    /**
     * To get the target-URL
     *
     * @return The URL to the target PHP-Script
     */
    public URL getSitePath() {
        return this.sitepath;
    }

    /**
     * Sending data to the target-URL
     *
     * @param data The data which should be send
     * @throws java.io.IOException
     */
    public void send(String data)
            throws IOException {
        if (con == null) {
            con = sitepath.openConnection();
        }
        if (!con.getDoOutput()) {
            con.setDoOutput(true);
        }
        OutputStream out = con.getOutputStream();
        out.write(data.getBytes());
        out.flush();
    }

    /**
     * Reading incoming data from the target-URL
     *
     * @return The incoming data
     * @throws java.io.IOException
     */
    public String read()
            throws IOException {
        if (con == null) {
            con = sitepath.openConnection();
        }
        InputStream in = con.getInputStream();
        int c = 0;
        StringBuilder incoming = new StringBuilder();
        while (c >= 0) {
            c = in.read();
            incoming.append((char) c);
        }
        return incoming.toString();
    }

}

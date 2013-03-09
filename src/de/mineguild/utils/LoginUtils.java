package de.mineguild.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: korbinian
 * Date: 23.02.13
 * Time: 18:16
 * To change this template use File | Settings | File Templates.
 */


public class LoginUtils {

    public static final int VALID_NOT_ACTIVE = 1;
    public static final int VALID = 0;
    public static final int INVALID = 2;
    public static final int ERROR_OCCURRED = 3;

    public static String getHash(String src) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(src.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString(anArray & 0xFF | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int webLogin(String username, String password) {
        username = getHash(username);
        password = getHash(password);
        try {
            URL url = new URL("http://update.mineguild.de/request.php");
            PhpPostConnect con = new PhpPostConnect(url);
            try {
                con.send("UsrId=" + username + "&UsrPwd=" + password);
                String output = con.read();
                if (output.startsWith("active")) {
                    return VALID;
                } else if (output.startsWith("inactive")) {
                    return VALID_NOT_ACTIVE;
                } else {
                    return INVALID;
                }
            } catch (IOException e) {
                System.err.println("Couldn't connect to file " + url.toString());
                return ERROR_OCCURRED;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ERROR_OCCURRED;
    }

}

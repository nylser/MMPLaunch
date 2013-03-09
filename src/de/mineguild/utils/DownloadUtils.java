package de.mineguild.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DownloadUtils {


    public static URL getMediafire(URL source, String filename) {
        String urlPattern = ".*(?i)(http://([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5]).+?/" + filename + ").*";
        Pattern pattern;
        Matcher matcher;
        String url;
        StringBuilder doc = new StringBuilder();
        pattern = Pattern.compile(urlPattern);
        File tmpFile = new File(".tmp");
        try {
            FileUtils.copyURLToFile(source, tmpFile);
            List<String> lines = FileUtils.readLines(tmpFile);
            for (String line : lines) {
                doc.append(line);
            }
        } catch (IOException ignored) {
            return null;
        }
        matcher = pattern.matcher(doc);
        if (matcher.matches()) {
            url = matcher.group(1);
        } else {
            System.out.println("Empty!");
            return null;
        }
        tmpFile.delete();
        try {
            return new URL(url);
        } catch (MalformedURLException ignored) {
            System.err.println("Not a valid url!");
            return null;
        }

    }

    public static URL getAdfly(URL source, String filename) {
        String urlPattern = ".*(?i)(https?://.*/" + filename + ").*";
        String mediafirePattern = ".*(?i)(https?://mediafire.com/.*).*";
        Pattern pattern = Pattern.compile(urlPattern);
        Matcher matcher;
        String url;
        StringBuilder doc = new StringBuilder();
        File tmpFile = new File(".tmp");
        try {
            FileUtils.copyURLToFile(source, tmpFile);
            List<String> lines = FileUtils.readLines(tmpFile);
            for (String line : lines) {
                doc.append(line);
            }
        } catch (IOException ignored) {
            return null;
        }
        matcher = pattern.matcher(doc);
        if (matcher.matches()) {
            url = matcher.group(1);
        } else {
            pattern = Pattern.compile(mediafirePattern);
            matcher = pattern.matcher(doc);
            if (matcher.matches()) {
                url = matcher.group(1);
            } else {
                System.out.println("Empty!");
                return null;
            }

        }
        tmpFile.delete();
        try {
            return new URL(url);
        } catch (MalformedURLException ignored) {
            return null;
        }

    }

    public static URL validateUrl(URL u, String filename) {
        try {
            filename = java.net.URLEncoder.encode(filename, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (u.toString().contains("adf.ly")) {
            System.out.println("Validating: " + filename);
            return getAdfly(u, filename);
        } else if (u.toString().contains("mediafire.com")) {
            System.out.println("Validating: " + filename);
            return getMediafire(u, filename);
        } else {
            return u;
        }
    }

    public static String getMD5(File source) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream is = new FileInputStream(source);
            byte[] dataBytes = new byte[1024];
            int nread = 0;
            while ((nread = is.read(dataBytes)) != -1)
                md.update(dataBytes, 0, nread);
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace(); //TODO
        }
        return null;
    }


    public static void main(String[] args) throws Exception {
        System.out.println(getMD5(new File("m.xml")));
    }


}

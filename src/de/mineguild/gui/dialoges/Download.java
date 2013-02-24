package de.mineguild.gui.dialoges;

import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Download extends JDialog {
    private static Map<File, URL> dList;
    private JPanel contentPane;
    private JButton buttonCancel;
    private JProgressBar downloadProgress;
    private JProgressBar totalDownloadProgress;
    private JTextArea downloadPInfo;
    private Thread t;
    private static int status = 0;

    //Status declared here.
    final static int DOWNLOAD_SUCCESSFULL = 0;
    final static int ERROR_OCCURRED = 1;
    final static int FILES_MISSING = 2;


    public Download() {
        setContentPane(contentPane);
        setModal(true);
        setLocationRelativeTo(null);
        setSize(500, 500);
        setTitle("Starting download...");
        setResizable(false);

        getRootPane().setDefaultButton(buttonCancel);

        try {
            download(dList);
        } catch (Exception ignored) {
        }
        t.start();

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

    public static int show(Map<File, URL> list) {
        dList = list;
        Download dialog = new Download();
        dialog.pack();
        dialog.setVisible(true);
        return status;
    }

    private void onCancel() {
        t.interrupt();
        close();
    }

    void close() {
        dispose();
    }

    void download(final Map<File, URL> list) {

        if (list.size() != 0) {
        } else {
            System.out.println("The linklist is empty!");
            status = ERROR_OCCURRED;
            close();
        }

        t = new Thread() {

            public void run() {
                float totalProgress;
                float totalSize = 0;
                float overallRead = 0;


                for (Map.Entry<File, URL> entry : list.entrySet()) {
                    URL curUrl = entry.getValue();
                    try {
                        HttpURLConnection connection = (HttpURLConnection) curUrl.openConnection();
                        totalSize += connection.getContentLength();
                    } catch (Exception e) {
                        e.printStackTrace();
                        status = ERROR_OCCURRED;
                        close();
                        Thread.currentThread().interrupt();
                    }

                }

                int c = 0;
                for (Map.Entry<File, URL> entry : list.entrySet()) {
                    c++;
                    File curFile = entry.getKey();
                    URL curUrl = entry.getValue();
                    if (curFile.exists() && !curFile.delete()) {
                        status = FILES_MISSING;
                        break;
                    }

                    try {

                        downloadPInfo.setText("File " + c + "/" + list.entrySet().size() + " : \n" + curFile.getName());
                        HttpURLConnection connection = (HttpURLConnection) curUrl.openConnection();
                        int fileSize = connection.getContentLength();
                        float totalDataRead = 0;

                        BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
                        FileOutputStream fos = new FileOutputStream(curFile);
                        BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
                        byte[] data = new byte[1024];
                        int i;

                        while ((i = in.read(data, 0, 1024)) >= 0) {
                            if (Thread.interrupted()) {
                                bout.flush();
                                bout.close();
                                curFile.delete();
                                return;
                            }
                            totalDataRead = totalDataRead + i;
                            overallRead = overallRead + i;
                            bout.write(data, 0, i);
                            float Percent = (totalDataRead * 100) / fileSize;
                            totalProgress = (overallRead * 100) / totalSize;
                            setTitle("Download: " + (int) totalProgress + "% done!");
                            if (overallRead + .1f == totalSize) totalProgress = 100;
                            downloadProgress.setValue((int) Percent);
                            totalDownloadProgress.setValue((int) totalProgress);
                        }
                        bout.flush();
                        bout.close();


                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
                buttonCancel.setFocusable(false);
                close();
            }
        };

    }

// --Commented out by Inspection START (23.02.13 23:14):
//    private boolean validate(File file, byte[] md5) throws Exception {
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        InputStream is = new FileInputStream(file);
//        try {
//            is = new DigestInputStream(is, md);
//        } finally {
//            is.close();
//        }
//        byte[] digest = md.digest();
//
//        return digest == md5;
//    }
// --Commented out by Inspection STOP (23.02.13 23:14)

}

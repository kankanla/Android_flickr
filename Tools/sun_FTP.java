package com.example.e560.m1126a.ToolsClass;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by E560 on 2016/12/04.
 */

public class sun_FTP {

    private String ftp_url, ftp_user, ftp_password;
    private boolean ftp_open;
    private String TAG = "sun_FTP";

    public sun_FTP(String ftp_url, String ftp_user, String ftp_password) {
        this.ftp_url = ftp_url;
        this.ftp_user = ftp_user;
        this.ftp_password = ftp_password;
        this.ftp_open = true;
    }

    public void Ftp_connect(String file_mode, File file) throws IOException {
        InetAddress inetAddress = InetAddress.getByName(ftp_url);
        Socket socket21 = new Socket(inetAddress.getHostAddress(), 21);
        BufferedReader ftpcmd_input = new BufferedReader(new InputStreamReader(socket21.getInputStream()));
        BufferedWriter ftpcmd_output = new BufferedWriter(new OutputStreamWriter(socket21.getOutputStream()));
        sendFTPcommand(ftpcmd_output, "USER " + ftp_user);
        sendFTPcommand(ftpcmd_output, "PASS " + ftp_password);
        sendFTPcommand(ftpcmd_output, "PASV ");

        switch (file_mode) {
            case "upFile":
                while (ftp_open) {
                    String temp = ftpcmd_input.readLine();
                    Log.i(TAG, temp);
                    String code = temp.substring(0, 3);
                    //进入被动模式（IP 地址、ID 端口）
                    if (code.equals("227")) {
                        String[] temp2 = get227(temp);
                        sendFTPcommand(ftpcmd_output, "TYPE " + "I");
                        sendFTPcommand(ftpcmd_output, "STOR " + file.getName());
                        upFile(temp2, file);
                        sendFTPcommand(ftpcmd_output, "QUIT");
                    }
                }
                break;
            case "downFile":

                break;
            case "Del_file":

                break;
        }
    }

    private void upFile(String[] urltemp, File file) throws IOException {
        Socket dataSocket = new Socket(urltemp[0], Integer.parseInt(urltemp[1]));
        OutputStream outputStream = dataSocket.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buff = new byte[20480];
        int len = 0;
        while ((len = fileInputStream.read(buff)) != -1) {
            outputStream.write(buff, 0, len);
            outputStream.flush();
        }
        outputStream.close();
        fileInputStream.close();
        dataSocket.close();
        ftp_open = false;
    }

    protected void sendFTPcommand(BufferedWriter ftpcmd_output, String command) throws IOException {
        ftpcmd_output.write(command + "\r\n");
        ftpcmd_output.flush();
        if (command.equals("QUIT")) {
            ftpcmd_output.close();
        }
    }

    protected Socket ftpSocket() throws IOException {
        Socket socket = new Socket(ftp_url, 21);

        return null;
    }

    protected String[] get227(String string) {
        //227 Entering Passive Mode (208,71,106,43,237,147).
        int start = string.indexOf("(") + 1;
        int end = string.indexOf(")");
        String substring = string.substring(start, end);
        Log.i(TAG, substring);
        String[] temp = substring.split(",");
        String ip = temp[0] + "." + temp[1] + "." + temp[2] + "." + temp[3];
        int port = Integer.parseInt(temp[4]) * 256 + Integer.parseInt(temp[5]);
        String sport = String.valueOf(port);
        Log.i(TAG, "POST " + port);
        String[] res = {ip, sport};
        return res;
    }
}

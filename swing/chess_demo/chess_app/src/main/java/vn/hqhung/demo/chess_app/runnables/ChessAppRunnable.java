package vn.hqhung.demo.chess_app.runnables;


import lombok.extern.log4j.Log4j;
import vn.hqhung.demo.chess_app.components.ChessAppFrame;
import vn.hqhung.demo.chess_app.components.chess_game.ChessTablePanel;
import vn.hqhung.demo.common.runnables.RunnableImpl;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @System: demo
 * @Title: Chess Game Runnable
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
@Log4j
public class ChessAppRunnable implements RunnableImpl {
    private InetAddress ip;
    private int port;
    private ObjectOutputStream output;
    private final ChessAppFrame chessApp;

    public ChessAppRunnable(ChessAppFrame chessApp) {
        this.chessApp = chessApp;

        try (BufferedReader br = new BufferedReader(new FileReader("./config/server.txt"))) {
            ip = InetAddress.getByName(br.readLine());
            port = Integer.parseInt(br.readLine());
            if (port < 0 || port > 0xFFFF) {
                JOptionPane.showMessageDialog(null, "Port is invalid!");
                System.exit(0);
            }
        } catch (FileNotFoundException ex) {
            log.error("Can't find file config", ex);
            JOptionPane.showMessageDialog(null, "Can't find file config !!!");
            System.exit(0);
        } catch (IOException ex) {
            log.error("Can't read file config", ex);
            JOptionPane.showMessageDialog(null, "Can't read file config !!!");
            System.exit(0);
        }
    }

    @Override
    public Closeable getCloseable() {
        return socket;
    }

    private Socket socket;

    public void run() {
        try {
            socket = new Socket(ip, port);
            output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            output.writeUTF("#connect");
            output.flush();

            while (true) {
                String in = input.readUTF();
                if (in.equals("#list")) {
                    int n = input.readInt();
                    List<ChessTablePanel> table = new ArrayList<>();
                    for (int i = 0; i < n; i++) {
                        table.add(new ChessTablePanel(chessApp, input.readInt(), input.readUTF(), input.readUTF(), input.readBoolean(), input.readInt()));
                    }
                    chessApp.reloadListTable(table);
                }
            }
        } catch (IOException ioe) {
            log.error("Error connect to server", ioe);
            JOptionPane.showMessageDialog(null, "Error connect to server!!!");
            System.exit(0);
        }
    }
}

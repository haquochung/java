package vn.hqhung.demo.chess_server.runnables;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import vn.hqhung.demo.chess_server.models.ChessTable;
import vn.hqhung.demo.common.runnables.RunnableImpl;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @System: demo
 * @Title: Server Runnable
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/15
 */
@Log4j
public class ServerRunnable implements RunnableImpl {

    public static final int DEFAULT_PORT = 1234;

    private static final List<Client> CLIENTS = new ArrayList<>();
    private int port;
    @Getter
    private InetAddress ip;

    public ServerRunnable() {
        try {
            ip = InetAddress.getLocalHost();
            port = DEFAULT_PORT;
        } catch (UnknownHostException ex) {
            log.error("Error can't get Server IP!!! ", ex);
        }
    }

    public void update(int port) {
        this.port = port;
    }

    ServerSocket socketServer = null;

    @Override
    public Closeable getCloseable() {
        return socketServer;
    }

    @Override
    public void run() {
        try {
            CLIENTS.clear();
            socketServer = new ServerSocket(port, 0, ip);
            Socket s;
            ObjectInputStream input;
            ObjectOutputStream output;
            String in;
            while (!socketServer.isClosed()) {
                try {
                    s = socketServer.accept();
                    log.info("New connection from IP: " + s.getInetAddress().getHostAddress());
                    input = new ObjectInputStream(s.getInputStream());
                    output = new ObjectOutputStream(s.getOutputStream());

                    in = input.readUTF();
                    if (in.equals("#connect")) {
                        CLIENTS.add(new Client(s, input, output));
                        sendListTable(output);
                    }
                } catch (IOException ex) {
                    if (!socketServer.isClosed()) {
                        log.error("Error while check connect", ex);
                    }
                }
            }
        } catch (Throwable th) {
            log.error("Error on run Server", th);
        } finally {
            if (socketServer != null) {
                try {
                    socketServer.close();
                } catch (IOException ignored) {}
            }
        }
    }

    public static void sendListTable() {
        List<Client> listDisconnect = new ArrayList<>();
        for (Client client : CLIENTS) {
            try {
                sendListTable(client.output);
            } catch (IOException ex) {
                log.info(client.socket.getInetAddress().getHostAddress() + " have disconnected!");
                listDisconnect.add(client);
            }
        }
        for (Client client : listDisconnect) {
            CLIENTS.remove(client);
        }
    }

    private static void sendListTable(ObjectOutputStream output) throws IOException {
        ChessTable chessTable;
        output.writeUTF("#list");
        int size = ConnectRunnable.CHESS_TABLES.size();
        output.writeInt(size);
        for (int i : ConnectRunnable.CHESS_TABLES.keySet()) {
            chessTable = ConnectRunnable.CHESS_TABLES.get(i);
            output.writeInt(chessTable.tableId);

            if (chessTable.whitePlayerName == null) {
                output.writeUTF("");
            } else {
                output.writeUTF(chessTable.whitePlayerName);
            }

            if (chessTable.blackPlayerName == null) {
                output.writeUTF("");
            } else {
                output.writeUTF(chessTable.blackPlayerName);
            }

            output.writeBoolean(chessTable.state);
            output.writeInt(chessTable.time);
        }
        output.flush();
    }

    /**
     *
     */
    static class Client {
        ObjectInputStream input;
        ObjectOutputStream output;
        Socket socket;

        Client(Socket socket, ObjectInputStream input, ObjectOutputStream output) {
            this.socket = socket;
            this.input = input;
            this.output = output;
        }
    }
}

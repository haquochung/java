package vn.hqhung.demo.chess_server.runnables;

import lombok.extern.log4j.Log4j;
import vn.hqhung.demo.chess_server.ChessServer;
import vn.hqhung.demo.chess_server.models.ChessTable;
import vn.hqhung.demo.chess_server.models.ChessGame;
import vn.hqhung.demo.common.models.ConnectionInfo;
import vn.hqhung.demo.common.runnables.RunnableImpl;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

/**
 * @System: demo
 * @Title: Connect Runnable
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/15
 */
@Log4j
public class ConnectRunnable implements RunnableImpl {

    public static final Map<Integer, ChessGame> GAME_MAP = new HashMap<>();
    public static final Map<Integer, ChessTable> CHESS_TABLES = new HashMap<>();
    public static final Map<Integer, Boolean> GAME_STATUS = new HashMap<>();
    public InetAddress ip;
    public int port;

    public ConnectRunnable() {
        try {
            ip = InetAddress.getLocalHost();
            port = ServerRunnable.DEFAULT_PORT + 1;
        } catch (UnknownHostException ex) {
            log.error("Error can't get Server IP!!! ", ex);
        }
    }

    public void update(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port + 1;
    }

    private ServerSocket socketServer = null;

    @Override
    public Closeable getCloseable() {
        return socketServer;
    }

    public void run() {
        try {
            GAME_MAP.clear();
            CHESS_TABLES.clear();
            socketServer = new ServerSocket(port, 0, ip);
            while (!socketServer.isClosed()) {
                Socket s;
                ObjectInputStream input;
                ObjectOutputStream output;

                int idTable;
                String playerName;
                String color;
                ChessGame game;
                try {
                    s = socketServer.accept();

                    input = new ObjectInputStream(s.getInputStream());
                    output = new ObjectOutputStream(s.getOutputStream());

                    String in = input.readUTF();
                    switch (in) {
                        case "#create":
                            playerName = input.readUTF();
                            color = input.readUTF();
                            int time = input.readInt();

                            if (GAME_MAP.size() >= 10)  {
                                output.writeInt(ConnectionInfo.ERR_TABLE_ID.getValue());
                                output.flush();
                                break;
                            }

                            idTable = generateId();
                            if (idTable == 0) {
                                output.writeInt(ConnectionInfo.ERR_TABLE_ID.getValue());
                                output.flush();
                                break;
                            }

                            log.info("New ChessGame <" + idTable + "> - Creator: " + playerName + " Color Piece: " + color);

                            createNewTable(idTable);
                            ConnectRunnable.GAME_STATUS.put(idTable, false);

                            if (color.equals("White")) {
                                CHESS_TABLES.put(idTable, new ChessTable(idTable, playerName, null, false, time));
                            } else {
                                CHESS_TABLES.put(idTable, new ChessTable(idTable, null, playerName, false, time));
                            }

                            game = GAME_MAP.get(idTable);
                            GameRunnable player = new GameRunnable(input, output, playerName, color, game);
                            game.addPlayer(player);
                            ChessServer.getExecutorService().execute(player);
                            output.writeInt(ConnectionInfo.TABLE_IS_OK.getValue());
                            output.flush();

                            ServerRunnable.sendListTable();
                            break;

                        case "#join":
                            playerName = input.readUTF();
                            color = input.readUTF();
                            idTable = input.readInt();

                            if (idTable < 1 || idTable > 10) {
                                output.writeInt(ConnectionInfo.ERR_TABLE_ID.getValue());
                                output.flush();
                                break;
                            }

                            game = GAME_MAP.get(idTable);
                            if (game == null || game.isAllPlayers()) {
                                log.info("User: " + playerName + " join to ChessGame <" + idTable + "> to play but it's full");
                                output.writeInt(ConnectionInfo.ERR_TABLE_FULL.getValue());
                                output.flush();
                                break;
                            }

                            log.info("'" + playerName + "' join to ChessGame : " + idTable + " to play");
                            if (color.equals("White")) {
                                CHESS_TABLES.get(idTable).whitePlayerName = playerName;
                            } else {
                                CHESS_TABLES.get(idTable).blackPlayerName = playerName;
                            }

                            game.sendMessageToAll("** '" + playerName + "' JOINED!! **");

                            GameRunnable client = new GameRunnable(input, output, playerName, color, game);
                            game.addPlayer(client);
                            ChessServer.getExecutorService().execute(client);
                            output.writeInt(ConnectionInfo.TABLE_IS_OK.getValue());
                            output.flush();

                            game.sendJoinToOtherPlayer(client);

                            if (game.clientPlayer1.status) {
                                game.clientPlayer2.output.writeUTF("#ready");
                                game.clientPlayer2.output.flush();
                            } else if (game.clientPlayer2.status) {
                                game.clientPlayer1.output.writeUTF("#ready");
                                game.clientPlayer1.output.flush();
                            }

                            ServerRunnable.sendListTable();
                            break;

                        case "#view":
                            playerName = input.readUTF();
                            idTable = input.readInt();

                            game = GAME_MAP.get(idTable);
                            if (idTable < 1 || idTable > 10 || game == null) {
                                output.writeInt(ConnectionInfo.ERR_TABLE_ID.getValue());
                                output.flush();
                                break;
                            }

                            log.info("User: " + playerName + " join to ChessGame <" + idTable + "> to view");
                            game.sendMessageToAll("** '" + playerName + "' joined!**");

                            GameRunnable viewer = new GameRunnable(input, output, playerName, null, game);
                            game.addViewer(viewer);
                            ChessServer.getExecutorService().execute(viewer);
                            output.writeInt(ConnectionInfo.TABLE_IS_OK.getValue());
                            output.flush();

                            if (game.clientPlayer1 != null && game.clientPlayer1.status) {
                                game.clientViewer.get(game.clientViewer.size() - 1).output.writeUTF("#ready");
                                game.clientViewer.get(game.clientViewer.size() - 1).output.writeUTF("White");
                                game.clientViewer.get(game.clientViewer.size() - 1).output.flush();
                            }

                            if (game.clientPlayer2 != null && game.clientPlayer2.status) {
                                game.clientViewer.get(game.clientViewer.size() - 1).output.writeUTF("#ready");
                                game.clientViewer.get(game.clientViewer.size() - 1).output.writeUTF("Black");
                                game.clientViewer.get(game.clientViewer.size() - 1).output.flush();
                            }

                            if (game.isAllReady()) {
                                game.sendSettingsAndMovesToNewObserver();
                            }

                            break;
                    }
                } catch (IOException ex) {
                    if (!socketServer.isClosed()) {
                        log.error("Error while check connect", ex);
                    }
                }
            }
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            if (socketServer != null) {
                try {
                    socketServer.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public void createNewTable(int idTable) {
        GAME_MAP.put(idTable, new ChessGame(idTable));
    }

    int id = 0;

    private int generateId() {
        return ++id;
    }
}

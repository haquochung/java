package vn.hqhung.demo.chess_server.runnables;

import lombok.extern.log4j.Log4j;
import vn.hqhung.demo.chess_server.ChessServer;
import vn.hqhung.demo.chess_server.models.ChessGame;
import vn.hqhung.demo.common.runnables.RunnableImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @System: demo
 * @Title: Game Runnable
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
@Log4j
public class GameRunnable implements RunnableImpl {
    public ObjectInputStream input;
    public ObjectOutputStream output;
    public String name;
    public String color;
    public boolean status, isOK;
    public ChessGame game;

    public GameRunnable(ObjectInputStream input, ObjectOutputStream output, String name, String color, ChessGame game) {
        this.input = input;
        this.output = output;
        this.name = name;
        this.color = color;
        this.status = false;
        this.game = game;
    }

    public void run() {
        isOK = true;
        String in = "";
        while (isOK) {
            try {
                try {
                    in = input.readUTF();
                } catch (Exception e) {
                    game.sendToAll(this, in);
                    game.clockRunnable.canRun = false;
                    game.clientPlayer1.status = false;
                    game.clientPlayer2.status = false;
                    ConnectRunnable.CHESS_TABLES.get(game.idTable).state = false;
                    ServerRunnable.sendListTable();
                    break;
                }
                switch (in) {
                    case "#ready":
                        status = true;
                        game.sendReadyToOther(this);
                        if (game.isAllReady()) {
                            log.info("Table <" + game.idTable + "> is start play");
                            ConnectRunnable.CHESS_TABLES.get(game.idTable).state = true;
                            ServerRunnable.sendListTable();

                            game.generateSettings();
                            game.sendSettingsToAll();
                            game.clockRunnable.canRun = true;
                            ChessServer.getExecutorService().execute(game.clockRunnable);
                        }
                        break;
                    case "#move":
                        int fromX = input.readInt();
                        int fromY = input.readInt();
                        int toX = input.readInt();
                        int toY = input.readInt();
                        String chessPieceName = input.readUTF();
                        game.sendMoveToOther(this, fromX, fromY, toX, toY, chessPieceName);
                        break;
                    case "#message":
                        String str = input.readUTF();
                        game.sendMessageToAll(name + ": " + str);
                        break;
                    case "#leave":
                        log.info("'" + name + "' left ChessGame <" + game.idTable + ">");
                        output.writeUTF("OK");
                        output.flush();
                        game.sendLeave(this);
                        isOK = false;
                        break;
                    case "#kick":
                        game.sendKick(this);
                        break;
                    case "#drawAsk":
                    case "#drawNegative":
                    case "#undoAsk":
                    case "#undoNegative":
                        game.sendToAll(this, in);
                        break;
                    case "#drawPossible":
                    case "#lose":
                    case "#endGame":
                        game.sendToAll(this, in);
                        game.clockRunnable.canRun = false;
                        game.clientPlayer1.status = false;
                        game.clientPlayer2.status = false;
                        ConnectRunnable.CHESS_TABLES.get(game.idTable).state = false;
                        game.reset();
                        ServerRunnable.sendListTable();
                        break;
                    case "#undoPossible":
                        game.sendUndoToAll(this, in);
                        game.clockRunnable.switchClock();
                        break;
                }
            } catch (IOException ex) {
                log.error(ex.getMessage(), ex);
                isOK = false;
                try {
                    log.info("Error connection with user: " + name);
                    game.sendErrorConnectionToOther(this);
                } catch (IOException ex1) {
                    log.error(ex1.getMessage(), ex1);
                }
            }
        }
    }
}

package vn.hqhung.demo.chess_server.models;

import lombok.extern.log4j.Log4j;
import vn.hqhung.demo.chess_server.runnables.ClockRunnable;
import vn.hqhung.demo.chess_server.runnables.ConnectRunnable;
import vn.hqhung.demo.chess_server.runnables.GameRunnable;
import vn.hqhung.demo.chess_server.runnables.ServerRunnable;
import vn.hqhung.demo.common.models.GameSetting;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @System: demo
 * @Title: Chess Game
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/17
 */
@Log4j
public class ChessGame {

    public GameRunnable clientPlayer1;
    public GameRunnable clientPlayer2;
    public ArrayList<GameRunnable> clientViewer;
    public GameSetting whiteSet;
    public GameSetting blackSet;
    public GameSetting viewerSet;
    public ClockRunnable clockRunnable;
    public int idTable;
    private final ArrayList<Move> movesList;

    public ChessGame(int idTable) {
        this.idTable = idTable;
        clientViewer = new ArrayList<>();
        movesList = new ArrayList<>();
    }

    public void reset() {
        clientViewer.clear();
        movesList.clear();
    }

    public void generateSettings() {
        whiteSet = new GameSetting();
        whiteSet.whitePlayer.setName(clientPlayer1.name);
        whiteSet.blackPlayer.setName(clientPlayer2.name);
        whiteSet.whitePlayer.setCanMove(true);
        whiteSet.blackPlayer.setCanMove(false);
        whiteSet.upSideDown = false;


        blackSet = new GameSetting();
        blackSet.whitePlayer.setName(clientPlayer1.name);
        blackSet.blackPlayer.setName(clientPlayer2.name);
        blackSet.whitePlayer.setCanMove(false);
        blackSet.blackPlayer.setCanMove(true);
        blackSet.upSideDown = true;


        viewerSet = new GameSetting();
        viewerSet.whitePlayer.setName(clientPlayer1.name);
        viewerSet.blackPlayer.setName(clientPlayer2.name);
        viewerSet.whitePlayer.setCanMove(false);
        viewerSet.blackPlayer.setCanMove(false);
        viewerSet.upSideDown = false;

        clockRunnable = new ClockRunnable(this);
        clockRunnable.setTime(ConnectRunnable.CHESS_TABLES.get(idTable).time * 60);
    }

    public void sendSettingsToAll() throws IOException {
        clientPlayer1.output.writeUTF("#settings");
        clientPlayer1.output.writeObject(whiteSet);
        clientPlayer1.output.flush();

        clientPlayer2.output.writeUTF("#settings");
        clientPlayer2.output.writeObject(blackSet);
        clientPlayer2.output.flush();

        for (GameRunnable viewer : clientViewer) {
            viewer.output.writeUTF("#settings");
            viewer.output.writeObject(viewerSet);
            viewer.output.flush();
        }
    }

    public void sendSettingsAndMovesToNewObserver() throws IOException {
        GameRunnable viewer = clientViewer.get(clientViewer.size() - 1);

        viewer.output.writeUTF("#settings");
        viewer.output.writeObject(viewerSet);
        viewer.output.flush();

        for (Move m : movesList) {
            viewer.output.writeUTF("#move");
            if (m.white) {
                viewer.output.writeInt(7 - m.bX);
                viewer.output.writeInt(7 - m.bY);
                viewer.output.writeInt(7 - m.eX);
                viewer.output.writeInt(7 - m.eY);
            } else {
                viewer.output.writeInt(m.bX);
                viewer.output.writeInt(m.bY);
                viewer.output.writeInt(m.eX);
                viewer.output.writeInt(m.eY);
            }
            viewer.output.writeUTF(m.piece);
        }
        viewer.output.flush();
    }

    public void sendMoveToOther(GameRunnable sender, int beginX, int beginY, int endX, int endY, String piece) throws IOException {
        if (sender == clientPlayer1 || sender == clientPlayer2) {
            GameRunnable receiver = (clientPlayer1 == sender) ? clientPlayer2 : clientPlayer1;
            receiver.output.writeUTF("#move");
            receiver.output.writeInt(beginX);
            receiver.output.writeInt(beginY);
            receiver.output.writeInt(endX);
            receiver.output.writeInt(endY);
            receiver.output.writeUTF(piece);
            receiver.output.flush();

            for (GameRunnable viewer : clientViewer) {
                viewer.output.writeUTF("#move");

                if (sender == clientPlayer1) {
                    viewer.output.writeInt(7 - beginX);
                    viewer.output.writeInt(7 - beginY);
                    viewer.output.writeInt(7 - endX);
                    viewer.output.writeInt(7 - endY);
                } else {
                    viewer.output.writeInt(beginX);
                    viewer.output.writeInt(beginY);
                    viewer.output.writeInt(endX);
                    viewer.output.writeInt(endY);
                }

                viewer.output.writeUTF(piece);
                viewer.output.flush();
            }

            if (sender == clientPlayer1) {
                movesList.add(new Move(beginX, beginY, endX, endY, piece, true));
            } else {
                movesList.add(new Move(beginX, beginY, endX, endY, piece, false));
            }

            clockRunnable.switchClock();
        }
    }

    public void sendJoinToOtherPlayer(GameRunnable sender) throws IOException {
        if (sender == clientPlayer1 || sender == clientPlayer2) {
            GameRunnable receiver = (clientPlayer1 == sender) ? clientPlayer2 : clientPlayer1;
            receiver.output.writeUTF("#join");
            receiver.output.writeUTF(sender.name);
            receiver.output.flush();

            for (GameRunnable viewer : clientViewer) {
                viewer.output.writeUTF("#join");
                viewer.output.writeUTF(sender.name);
                viewer.output.flush();
            }
        }
    }

    public void sendErrorConnectionToOther(GameRunnable sender) throws IOException {
        if (sender == clientPlayer1 || sender == clientPlayer2) {
            if (clientPlayer1 != sender && clientPlayer1 != null) {
                clientPlayer1.output.writeUTF("#errorConnection");
                clientPlayer1.output.flush();
                clientPlayer2 = null;
                ConnectRunnable.CHESS_TABLES.get(idTable).blackPlayerName = "";
            } else if (clientPlayer2 != sender && clientPlayer2 != null) {
                clientPlayer2.output.writeUTF("#errorConnection");
                clientPlayer2.output.flush();
                clientPlayer1 = null;
                ConnectRunnable.CHESS_TABLES.get(idTable).whitePlayerName = "";
            }

            ConnectRunnable.CHESS_TABLES.get(idTable).state = false;

            if (clientPlayer1 != null || clientPlayer2 != null) {
                for (GameRunnable viewer : clientViewer) {
                    viewer.output.writeUTF("#errorConnection");
                    viewer.output.writeUTF(sender.name);
                    viewer.output.flush();
                }
            } else {
                for (GameRunnable viewer : clientViewer) {
                    viewer.output.writeUTF("#close");
                    viewer.output.flush();
                    viewer.isOK = false;
                }

                ConnectRunnable.GAME_STATUS.remove(idTable);
                ConnectRunnable.GAME_MAP.remove(idTable);
                ConnectRunnable.CHESS_TABLES.remove(idTable);
            }

            if (clockRunnable != null) {
                clockRunnable.canRun = false;
            }

            ServerRunnable.sendListTable();
        } else {
            clientViewer.remove(sender);
            sendMessageToAll("** '" + sender.name + "' was disconnected to server **");
        }
    }

    public void sendMessageToAll(String str) throws IOException {
        if (clientPlayer1 != null) {
            clientPlayer1.output.writeUTF("#message");
            clientPlayer1.output.writeUTF(str);
            clientPlayer1.output.flush();
        }

        if (clientPlayer2 != null) {
            clientPlayer2.output.writeUTF("#message");
            clientPlayer2.output.writeUTF(str);
            clientPlayer2.output.flush();
        }

        for (GameRunnable viewer : clientViewer) {
            viewer.output.writeUTF("#message");
            viewer.output.writeUTF(str);
            viewer.output.flush();
        }
    }

    public void sendToAll(GameRunnable sender, String msg) throws IOException {
        if (sender == clientPlayer1 || sender == clientPlayer2) {
            GameRunnable receiver = (clientPlayer1 == sender) ? clientPlayer2 : clientPlayer1;
            receiver.output.writeUTF(msg);
            receiver.output.flush();

            for (GameRunnable viewer : clientViewer) {
                viewer.output.writeUTF(msg);
                viewer.output.writeUTF(sender.name);
                viewer.output.flush();
            }
        }
    }

    public void sendUndoToAll(GameRunnable sender, String msg) throws IOException {
        if (sender == clientPlayer1 || sender == clientPlayer2) {
            GameRunnable receiver;
            if (clientPlayer1 != sender) {
                if (movesList.size() % 2 == 0) {
                    movesList.remove(movesList.size() - 1);
                    clockRunnable.switchClock();
                }

                receiver = clientPlayer1;
            } else {
                if (movesList.size() > 1 && movesList.size() % 2 != 0) {
                    movesList.remove(movesList.size() - 1);
                    clockRunnable.switchClock();
                }

                receiver = clientPlayer2;
            }

            receiver.output.writeUTF(msg);
            receiver.output.flush();

            movesList.remove(movesList.size() - 1);

            for (GameRunnable viewer : clientViewer) {
                viewer.output.writeUTF(msg);
                viewer.output.writeUTF(sender.name);
                viewer.output.flush();
            }
        }
    }

    public void sendLeave(GameRunnable sender) throws IOException {
        if (sender == clientPlayer1 || sender == clientPlayer2) {
            GameRunnable receiver = null;
            if (sender == clientPlayer1) {
                ConnectRunnable.CHESS_TABLES.get(idTable).whitePlayerName = "";
                ConnectRunnable.CHESS_TABLES.get(idTable).state = false;
                clientPlayer1 = null;

                if (clientPlayer2 != null) {
                    clientPlayer2.status = false;
                    receiver = clientPlayer2;
                }
            } else {
                ConnectRunnable.CHESS_TABLES.get(idTable).blackPlayerName = "";
                ConnectRunnable.CHESS_TABLES.get(idTable).state = false;
                clientPlayer2 = null;

                if (clientPlayer1 != null) {
                    clientPlayer1.status = false;
                    receiver = clientPlayer1;
                }
            }

            if (receiver != null) {
                receiver.output.writeUTF("#leave");
                receiver.output.writeUTF(sender.name);
                receiver.output.flush();

                for (GameRunnable viewer : clientViewer) {
                    viewer.output.writeUTF("#leave");
                    viewer.output.writeUTF(sender.name);
                    viewer.output.flush();
                }

                ServerRunnable.sendListTable();
            } else {
                for (GameRunnable viewer : clientViewer) {
                    viewer.output.writeUTF("#close");
                    viewer.output.flush();
                }

                clientViewer.clear();

                ConnectRunnable.GAME_STATUS.remove(idTable);
                ConnectRunnable.GAME_MAP.remove(idTable);
                ConnectRunnable.CHESS_TABLES.remove(idTable);

                ServerRunnable.sendListTable();
            }

            if (clockRunnable != null) {
                clockRunnable.canRun = false;
            }
        } else {
            clientViewer.remove(sender);
            sendMessageToAll("** '" + sender.name + "' LEFT! **");
        }
    }

    public void sendKick(GameRunnable sender) throws IOException {
        String nick;

        if (sender == clientPlayer1) {
            clientPlayer2.output.writeUTF("#kick");
            clientPlayer2.output.flush();

            nick = clientPlayer2.name;
            clientPlayer2.isOK = false;
            clientPlayer2 = null;
            ConnectRunnable.CHESS_TABLES.get(idTable).blackPlayerName = "";
        } else {
            clientPlayer1.output.writeUTF("#kick");
            clientPlayer1.output.flush();

            nick = clientPlayer1.name;
            clientPlayer1.isOK = false;
            clientPlayer1 = null;
            ConnectRunnable.CHESS_TABLES.get(idTable).whitePlayerName = "";
        }

        ServerRunnable.sendListTable();
        sendMessageToAll("** '" + nick + "' was KICK by host! **");
    }

    public boolean isAllPlayers() {
        return clientPlayer1 != null && clientPlayer2 != null;
    }

    public void sendReadyToOther(GameRunnable sender) throws IOException {
        GameRunnable receiver = (clientPlayer1 == sender) ? clientPlayer2 : clientPlayer1;
        if (receiver != null) {
            receiver.output.writeUTF("#ready");
            receiver.output.flush();
        }

        for (GameRunnable viewer : clientViewer) {
            viewer.output.writeUTF("#ready");
            viewer.output.writeUTF(sender.color);
            viewer.output.flush();
        }
    }

    public boolean isAllReady() {
        if (isAllPlayers()) {
            return clientPlayer1.status && clientPlayer2.status;
        }
        return false;
    }

    public void addPlayer(GameRunnable client) {
        if (client.color.equals("White")) {
            clientPlayer1 = client;
        } else {
            clientPlayer2 = client;
        }
    }

    public void addViewer(GameRunnable client) {
        clientViewer.add(client);
    }

    public void timeOver(String color) {
        try {
            clientPlayer1.output.writeUTF("#end");
            clientPlayer1.output.writeUTF(color);
            clientPlayer1.output.flush();

            clientPlayer2.output.writeUTF("#end");
            clientPlayer2.output.writeUTF(color);
            clientPlayer2.output.flush();

            for (GameRunnable viewer : clientViewer) {
                viewer.output.writeUTF("#end");
                viewer.output.writeUTF(color);
                viewer.output.flush();
            }

            clientPlayer1.status = false;
            clientPlayer2.status = false;

            ConnectRunnable.CHESS_TABLES.get(idTable).state = false;
            ServerRunnable.sendListTable();
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public void sendTime(String t1, String t2) {
        try {
            clientPlayer1.output.writeUTF("#time");
            clientPlayer1.output.writeUTF(t1);
            clientPlayer1.output.writeUTF(t2);
            clientPlayer1.output.flush();

            clientPlayer2.output.writeUTF("#time");
            clientPlayer2.output.writeUTF(t1);
            clientPlayer2.output.writeUTF(t2);
            clientPlayer2.output.flush();

            for (GameRunnable viewer : clientViewer) {
                viewer.output.writeUTF("#time");
                viewer.output.writeUTF(t1);
                viewer.output.writeUTF(t2);
                viewer.output.flush();
            }
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    private static class Move {
        int bX;
        int bY;
        int eX;
        int eY;
        String piece;
        boolean white;

        Move(int bX, int bY, int eX, int eY, String piece, boolean color) {
            this.bX = bX;
            this.bY = bY;
            this.eX = eX;
            this.eY = eY;
            this.piece = piece;
            this.white = color;
        }
    }
}

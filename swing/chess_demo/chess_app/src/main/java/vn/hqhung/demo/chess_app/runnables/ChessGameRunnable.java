package vn.hqhung.demo.chess_app.runnables;

import lombok.extern.log4j.Log4j;
import vn.hqhung.demo.chess_app.components.chess_game.*;
import vn.hqhung.demo.chess_app.components.ChessAppFrame;
import vn.hqhung.demo.chess_app.components.ChessGameFrame;
import vn.hqhung.demo.common.models.GamePlayer;
import vn.hqhung.demo.common.models.GameSetting;
import vn.hqhung.demo.common.models.ConnectionInfo;
import vn.hqhung.demo.common.runnables.RunnableImpl;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @System: demo
 * @Title: Chess App Runnable
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
@Log4j
public class ChessGameRunnable implements RunnableImpl {

    Socket socket;
    ObjectOutputStream output;
    ObjectInputStream input;
    InetAddress ip;
    int port;
    public ChessGamePanel game;
    GameSetting gameSetting;
    public String name;
    private boolean isOK;
    private final ChessAppFrame appLayout;

    @Override
    public Closeable getCloseable() {
        return socket;
    }

    public ChessGameRunnable(ChessAppFrame appLayout) {
        this.appLayout = appLayout;

        try (BufferedReader br = new BufferedReader(new FileReader("./config/server.txt"))) {
            ip = InetAddress.getByName(br.readLine());
            port = Integer.parseInt(br.readLine()) + 1;
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

    public boolean create(String nick, boolean isWhite, int time) throws Error {
        try {
            socket = new Socket(ip, port);

            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            output.writeUTF("#create");
            output.writeUTF(nick);
            output.writeUTF(isWhite ? "White" : "Black");
            output.writeInt(time);
            output.flush();

            int code = input.readInt();
            return code == ConnectionInfo.TABLE_IS_OK.getValue();

        } catch (IOException ex) {
            log.error("Error when read input stream", ex);
            return false;
        }
    }

    public boolean join(String nick, boolean isWhite, int tableID) throws Error {
        try {
            socket = new Socket(ip, port);

            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            output.writeUTF("#join");
            output.writeUTF(nick);
            output.writeUTF(isWhite ? "White" : "Black");
            output.writeInt(tableID);
            output.flush();

            int code = input.readInt();
            return code == ConnectionInfo.TABLE_IS_OK.getValue();

        } catch (IOException ex) {
            log.error("Error when read input stream", ex);
            return false;
        }
    }

    public boolean view(String nick, int tableID) throws Error {
        try {
            socket = new Socket(ip, port);

            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            output.writeUTF("#view");
            output.writeUTF(nick);
            output.writeInt(tableID);
            output.flush();

            int code = input.readInt();
            return code == ConnectionInfo.TABLE_IS_OK.getValue();

        } catch (Exception ex) {
            log.error("Error when read input stream", ex);
            return false;
        }
    }

    public void run() {
        isOK = true;

        while (isOK) {
            try {
                String in = input.readUTF();
                switch (in) {
                    case "#join":
                        name = input.readUTF();

                        if (!game.viewer) {
                            if (appLayout.player.isWhite()) {
                                game.getGamePlayerPanel().setOtherPlayer(new GamePlayer(name, false));
                            } else {
                                game.getGamePlayerPanel().setOtherPlayer(new GamePlayer(name, true));
                            }
                        } else {
                            if (game.getGamePlayerPanel().getCurrentPlayer().getName().isEmpty()) {
                                game.getGamePlayerPanel().setCurrentPlayer(new GamePlayer(name, true));
                            } else {
                                game.getGamePlayerPanel().setOtherPlayer(new GamePlayer(name, false));
                            }
                        }
                        break;
                    case "#time":
                        if (game.getGamePlayerPanel().getCurrentPlayer().isWhite() || game.viewer) {
                            game.getGamePlayerPanel().lblCurrentPlayerTime.setText(input.readUTF());
                            game.getGamePlayerPanel().lblOtherPlayerTime.setText(input.readUTF());
                        } else {
                            game.getGamePlayerPanel().lblOtherPlayerTime.setText(input.readUTF());
                            game.getGamePlayerPanel().lblCurrentPlayerTime.setText(input.readUTF());
                        }
                        break;

                    case "#end":
                        in = input.readUTF();
                        boolean isWhite = "White".equals(in);
                        if (game.getGamePlayerPanel().getCurrentPlayer().isWhite() == isWhite) {
                            game.endGame("Timeout! '" + game.getGamePlayerPanel().getOtherPlayer().getName() + "' WIN!");
                        } else {
                            game.endGame("Time of '" + game.getGamePlayerPanel().getOtherPlayer().getName() + "' is run out! You WIN!");
                        }
                        if (game.getGamePlayerPanel().isOwner) {
                            game.getGamePlayerPanel().btnKick.setEnabled(false);
                        }

                        break;

                    case "#ready":
                        if (!game.viewer) {
                            game.getGamePlayerPanel().lblImageOtherPlayer.setText("Ready");
                        } else {
                            in = input.readUTF();
                            if (in.equals("Black")) {
                                game.getGamePlayerPanel().lblImageOtherPlayer.setText("Ready");
                            } else {
                                game.getGamePlayerPanel().lblImageCurrentPlayer.setText("Ready");
                            }
                        }
                        break;

                    case "#leave":
                        name = input.readUTF();
                        game.chat.addMessage("** '" + name + "' LEFT! **");
                        if (!game.viewer) {
                            if (name.equals(game.getGamePlayerPanel().getOtherPlayer().getName())) {
                                game.getGamePlayerPanel().lblOtherPlayerName.setText("");
                                game.getGamePlayerPanel().lblImageOtherPlayer.setText("");

                                if (game.getGamePlayerPanel().isOwner) {
                                    game.getGamePlayerPanel().btnKick.setEnabled(false);
                                } else {
                                    game.getGamePlayerPanel().isOwner = true;
                                    game.getGamePlayerPanel().btnKick.setEnabled(false);
                                    game.getGamePlayerPanel().btnKick.setVisible(true);
                                }

                                if (game.isPlay) {
                                    game.endGame("You WIN!!! '" + game.getGamePlayerPanel().getOtherPlayer().getName() + "' was LEFT!");
                                }
                            }
                        } else {
                            if (name.equals(game.getGamePlayerPanel().getCurrentPlayer().getName())) {
                                game.getGamePlayerPanel().lblCurrentPlayerName.setText("");
                                game.getGamePlayerPanel().lblImageCurrentPlayer.setText("");
                                if (game.isPlay) {
                                    game.endGame("'" + game.getGamePlayerPanel().getOtherPlayer().getName() + "' WIN!!! '" + name + "' was LEFT!");
                                }
                            } else {
                                game.getGamePlayerPanel().lblOtherPlayerName.setText("");
                                game.getGamePlayerPanel().lblImageOtherPlayer.setText("");
                                if (game.isPlay) {
                                    game.endGame("'" + game.getGamePlayerPanel().getCurrentPlayer().getName() + "' WIN!!! '" + name + "' was LEFT!");
                                }
                            }
                        }
                        break;

                    case "#kick":
                        if (!game.viewer) {
                            output.writeUTF("#OK");
                            output.flush();
                            JOptionPane.showMessageDialog(game, "You was KICK by host!!!");
                            isOK = false;
                            ChessGameFrame playGame = (ChessGameFrame) game.getRootPane().getParent();
                            playGame.error = true;
                            playGame.close();
                        } else {
                            name = input.readUTF();
                            if (name.equals(game.getGamePlayerPanel().getCurrentPlayer().getName())) {
                                game.getGamePlayerPanel().lblCurrentPlayerName.setText("");
                                game.getGamePlayerPanel().lblImageCurrentPlayer.setText("");
                            } else {
                                game.getGamePlayerPanel().lblOtherPlayerName.setText("");
                                game.getGamePlayerPanel().lblImageOtherPlayer.setText("");
                            }
                        }
                        break;

                    case "#move":
                        int beginX = input.readInt();
                        int beginY = input.readInt();
                        int endX = input.readInt();
                        int endY = input.readInt();
                        String piece = input.readUTF();
                        ChessPiece chessPiece = null;
                        switch (piece.toUpperCase()) {
                            case "ROOK":
                                chessPiece = new Rook(game.getChessBoardPanel(), game.getActivePlayer());
                                break;
                            case "KNIGHT":
                                chessPiece = new Knight(game.getChessBoardPanel(), game.getActivePlayer());
                                break;
                            case "BISHOP":
                                chessPiece = new Bishop(game.getChessBoardPanel(), game.getActivePlayer());
                                break;
                            case "QUEEN":
                                chessPiece = new Queen(game.getChessBoardPanel(), game.getActivePlayer());
                                break;
                        }
                        game.simulateMove(beginX, beginY, endX, endY, chessPiece);
                        break;

                    case "#message":
                        String str = input.readUTF();
                        game.chat.addMessage(str);
                        break;

                    case "#settings":
                        try {
                            gameSetting = (GameSetting) input.readObject();
                        } catch (ClassNotFoundException ex) {
                            log.error(ex.getMessage(), ex);
                        }

                        game.gameSetting = gameSetting;
                        game.getChessBoardPanel().gameSetting = gameSetting;
                        game.newGame();
                        game.getChessBoardPanel().repaint();
                        game.getChessBoardPanel().draw();
                        game.isPlay = true;
                        break;

                    case "#errorConnection":
                        if (!game.viewer) {
                            game.chat.addMessage("** Can't connect to '" + game.getGamePlayerPanel().getOtherPlayer().getName() + "' **");
                            game.getGamePlayerPanel().lblOtherPlayerName.setText("");
                            game.getGamePlayerPanel().lblImageOtherPlayer.setText("");

                            if (game.getGamePlayerPanel().isOwner) {
                                game.getGamePlayerPanel().btnKick.setEnabled(false);
                            } else {
                                game.getGamePlayerPanel().isOwner = true;
                                game.getGamePlayerPanel().btnKick.setEnabled(false);
                                game.getGamePlayerPanel().btnKick.setVisible(true);
                            }

                            if (game.isPlay) {
                                game.endGame("You WIN!!! '" + game.getGamePlayerPanel().getOtherPlayer().getName() + "' was DISCONNECTED");
                            }
                        } else {
                            name = input.readUTF();
                            if (name.equals(game.getGamePlayerPanel().getCurrentPlayer().getName())) {
                                game.getGamePlayerPanel().lblCurrentPlayerName.setText("");
                                game.getGamePlayerPanel().lblImageCurrentPlayer.setText("");
                                if (game.isPlay) {
                                    game.endGame("'" + game.getGamePlayerPanel().getOtherPlayer().getName() + "' WIN!!! '" + name + "'  was DISCONNECTED!");
                                }
                            } else {
                                game.getGamePlayerPanel().lblOtherPlayerName.setText("");
                                game.getGamePlayerPanel().lblImageOtherPlayer.setText("");
                                if (game.isPlay) {
                                    game.endGame("'" + game.getGamePlayerPanel().getCurrentPlayer().getName() + "' WIN!!! '" + name + "' was DISCONNECTED!");
                                }
                            }
                        }
                        break;

                    case "#close":
                        output.writeUTF("#OK");
                        output.flush();
                        JOptionPane.showMessageDialog(game, "Game will end because all player has left!");
                        isOK = false;
                        ChessGameFrame playGame = (ChessGameFrame) game.getRootPane().getParent();
                        playGame.error = true;
                        playGame.close();
                        break;

                    case "#drawAsk":
                        if (!game.viewer)
                            if (JOptionPane.showConfirmDialog(game, "Draw?", "Draw", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                output.writeUTF("#drawPossible");
                                output.flush();
                                game.endGame("Draw!");
                            } else {
                                output.writeUTF("#drawNegative");
                                output.flush();
                            }
                        else {
                            name = input.readUTF();
                            game.chat.addMessage("** '" + name + "' want DRAW **");
                        }
                        break;

                    case "#drawPossible":
                        game.endGame("Draw!");
                        break;

                    case "#drawNegative":
                        if (!game.viewer) JOptionPane.showMessageDialog(game, "No Draw!!!");
                        else {
                            name = input.readUTF();
                            game.chat.addMessage("** '" + name + "' don't want to DRAW! **");
                        }
                        break;

                    case "#undoAsk":
                        if (!game.viewer) {
                            if (JOptionPane.showConfirmDialog(game, "Undo?", "Undo", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                output.writeUTF("#undoPossible");
                                output.flush();
                                game.undo(false);
                            } else {
                                output.writeUTF("#undoNegative");
                                output.flush();
                            }
                        } else {
                            name = input.readUTF();
                            game.chat.addMessage("** '" + name + "' want to undo **");
                        }
                        break;

                    case "#undoPossible":
                        if (!game.viewer) game.chat.addMessage("** UNDO **");

                        else {
                            name = input.readUTF();
                            game.chat.addMessage("** '" + name + "' agree to UNDO! **");
                        }
                        game.undo(true);
                        break;

                    case "#undoNegative":
                        if (!game.viewer) JOptionPane.showMessageDialog(game, " NO UNDO!");
                        else {
                            name = input.readUTF();
                            game.chat.addMessage("** '" + name + "' don't agree to UNDO! **");
                        }
                        break;

                    case "#lose":
                        if (!game.viewer) {
                            game.endGame("YOU WIN!!!");
                        } else {
                            name = input.readUTF();
                            game.endGame("'" + name + "' was LOSE!!!");
                        }
                        break;
                }
            } catch (IOException ex) {
                isOK = false;
                JOptionPane.showMessageDialog(game, "Error connect to server! App will close!!!");
                ChessGameFrame parent = (ChessGameFrame) game.getRootPane().getParent();
                parent.error = true;
                parent.close();
                log.error(ex.getMessage(), ex);
            }
        }
    }

    public void ready() {
        try {
            output.writeUTF("#ready");
            output.flush();
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public void kick() {
        try {
            output.writeUTF("#kick");
            output.flush();
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public void leave() {
        try {
            output.writeUTF("#leave");
            output.flush();
            isOK = false;
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public void draw() {
        try {
            output.writeUTF("#drawAsk");
            output.flush();
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public void undo() {
        try {
            output.writeUTF("#undoAsk");
            output.flush();
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public void lose() {
        try {
            output.writeUTF("#lose");
            output.flush();
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public void endGame() {
        try {
            output.writeUTF("#endGame");
            output.flush();
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    //sending new move to server
    public void sendMove(int beginX, int beginY, int endX, int endY, ChessPiece piece) {
        try {
            output.writeUTF("#move");
            output.writeInt(7 - beginX);
            output.writeInt(7 - beginY);
            output.writeInt(7 - endX);
            output.writeInt(7 - endY);
            output.writeUTF(piece.getChessPieceType().name());
            output.flush();
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public void sendMassage(String str) {
        try {
            output.writeUTF("#message");
            output.writeUTF(str);
            output.flush();
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}

package vn.hqhung.demo.chess_app.components.chess_game;

import vn.hqhung.demo.chess_app.ChessApp;
import vn.hqhung.demo.chess_app.components.ChessAppFrame;
import vn.hqhung.demo.chess_app.components.ChessGameFrame;
import vn.hqhung.demo.chess_app.helpers.ResourceHelper;
import vn.hqhung.demo.common.models.GamePlayer;
import vn.hqhung.demo.chess_app.runnables.ChessGameRunnable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @System: demo
 * @Title: Chess Table Layout
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
public class ChessTablePanel extends JPanel {

    private final int idTable;
    private final int time;
    private final String whiteName;
    private final String blackName;
    private final boolean state;
    private final ChessAppFrame chessAppFrame;

    public ChessTablePanel(ChessAppFrame chessAppFrame, int idTable, String whiteName, String blackName, boolean state, int time) {
        this.chessAppFrame = chessAppFrame;
        this.idTable = idTable;
        this.whiteName = whiteName;
        this.blackName = blackName;
        this.state = state;
        this.time = time;
        initComponents();
    }

    private void initComponents() {

        setDoubleBuffered(true); // use to draw
        JLabel lbIDTable = new JLabel();
        JLabel lbStateTable = new JLabel();

        setMinimumSize(new Dimension(120, 120));
        setMaximumSize(new Dimension(120, 120));
        setPreferredSize(new Dimension(120, 120));
        setBackground(Color.WHITE);
        setLayout(null);

        if (whiteName != null && !whiteName.equals("")) {
            JLabel lbWhite = new JLabel();
            lbWhite.setIcon(new ImageIcon(ResourceHelper.loadImage("Player.png")));
            lbWhite.setBounds(5, 48, 25, 25);
            add(lbWhite);
        } else {
            btnWhite = new JButton("...");
            btnWhite.setBounds(5, 48, 25, 25);
            btnWhite.addActionListener(this::joinGame);
            add(btnWhite);
        }

        if (blackName != null && !blackName.equals("")) {
            JLabel lbBlack = new JLabel();
            lbBlack.setIcon(new ImageIcon(ResourceHelper.loadImage("Player.png")));
            lbBlack.setBounds(91, 48, 25, 25);
            add(lbBlack);
        } else {
            JButton btnBlack = new JButton("...");
            btnBlack.setBounds(91, 48, 25, 25);
            btnBlack.addActionListener(this::joinGame);
            add(btnBlack);
        }

        lbIDTable.setFont(new Font("Arial", Font.BOLD, 14));
        lbIDTable.setForeground(Color.BLACK);
        lbIDTable.setText(String.valueOf(idTable));
        lbIDTable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbIDTable.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lbIDTable.setBounds(33, 5, 55, 25);
        add(lbIDTable);

        if (state) {
            lbStateTable.setIcon(new ImageIcon(ResourceHelper.loadImage("Table_Disable.png")));
        } else {
            lbStateTable.setIcon(new ImageIcon(ResourceHelper.loadImage("Table_Enable.png")));
        }
        lbStateTable.setBounds(35, 35, 55, 55);
        add(lbStateTable);

        JButton btnView = new JButton("View");
        btnView.setBounds(35, 95, 55, 25);
        btnView.addActionListener(e -> viewGame());
        add(btnView);
    }

    private JButton btnWhite;

    private void viewGame() {
        String msg = "<html><body width='300'><h1>Game info</h1>";
        msg += "White Piece: <h3>" + whiteName + "</h3>";
        msg += "Black Piece: <h3>" + blackName + "</h3>";

        msg += "Time of game: <h3>" + time + " minutes</h3>";
        msg += "<h3>Want to view? Please choose 'Yes'/'No'</h3>";

        int choose = JOptionPane.showConfirmDialog(this, msg, "Game info", JOptionPane.YES_NO_OPTION);
        if (choose == JOptionPane.YES_OPTION) {
            chessAppFrame.gameRunnable = new ChessGameRunnable(chessAppFrame);
            boolean isView = chessAppFrame.gameRunnable.view(chessAppFrame.playerName, idTable);

            if (isView) {
                GamePlayer player = new GamePlayer(whiteName, true);
                GamePlayer otherPlayer = new GamePlayer(blackName, false);
                ChessGamePanel newGame = new ChessGamePanel(true, chessAppFrame);
                ChessGameFrame game = new ChessGameFrame(newGame, chessAppFrame);
                chessAppFrame.gameRunnable.game = newGame;

                if (player.getName() != null) {
                    newGame.getGamePlayerPanel().setCurrentPlayer(player);
                }
                if (otherPlayer.getName() != null) {
                    newGame.getGamePlayerPanel().setOtherPlayer(otherPlayer);
                }

                newGame.getChessBoardPanel().draw();
                game.setVisible(true);

                ChessApp.getExecutorService().execute(chessAppFrame.gameRunnable);

                chessAppFrame.setVisible(false);
            }
        }
    }

    private void joinGame(ActionEvent e) {
        String msg = "<html><body width='300'><h1>Game Info</h1>";
        boolean isWhite = true;
        String name;
        if (e.getSource() == btnWhite) {
            msg += "Black Piece: <h3>" + blackName;
            name = blackName;
        } else {
            msg += "White Piece: <h3>" + whiteName;
            isWhite = false;
            name = whiteName;
        }

        msg += "</h3>Time of game: <h3>" + time + " minutes</h3>";
        msg += "<h3>Want to play? Please choose 'Yes'/'No'</h3>";

        int choose = JOptionPane.showConfirmDialog(this, msg, "Game Info", JOptionPane.YES_NO_OPTION);

        if (choose == JOptionPane.YES_OPTION) {
            chessAppFrame.gameRunnable = new ChessGameRunnable(chessAppFrame);
            boolean isJoin = chessAppFrame.gameRunnable.join(chessAppFrame.playerName, isWhite, idTable);
            if (isJoin) {
                chessAppFrame.player = new GamePlayer(chessAppFrame.playerName, isWhite);
                GamePlayer otherPlayer = new GamePlayer(name, !isWhite);

                ChessGamePanel newGame = new ChessGamePanel(false, chessAppFrame);
                ChessGameFrame game = new ChessGameFrame(newGame, chessAppFrame);
                chessAppFrame.gameRunnable.game = newGame;
                newGame.getGamePlayerPanel().btnReady.setVisible(true);
                newGame.getGamePlayerPanel().setCurrentPlayer(chessAppFrame.player);
                newGame.getGamePlayerPanel().setOtherPlayer(otherPlayer);
                newGame.getChessBoardPanel().draw();
                game.setVisible(true);

                ChessApp.getExecutorService().execute(chessAppFrame.gameRunnable);

                chessAppFrame.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "Player in game is FULL!");
            }
        }
    }
}

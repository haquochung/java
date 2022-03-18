package vn.hqhung.demo.chess_app.components;

import vn.hqhung.demo.chess_app.ChessApp;
import vn.hqhung.demo.chess_app.components.chess_game.ChessGamePanel;
import vn.hqhung.demo.chess_app.components.chess_game.ChessTablePanel;
import vn.hqhung.demo.chess_app.helpers.ResourceHelper;
import vn.hqhung.demo.common.models.GamePlayer;
import vn.hqhung.demo.chess_app.runnables.ChessGameRunnable;
import vn.hqhung.demo.chess_app.runnables.ChessAppRunnable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

/**
 * @System: demo
 * @Title: Chess App Layout
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
public class ChessAppFrame extends JFrame {

    public GamePlayer player;
    public String playerName;
    public ChessGameRunnable gameRunnable;
    private JPanel pnListTable;

    /**
     * Creates new form GUI
     */
    public ChessAppFrame() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setPreferredSize(new Dimension(420, 470));
        setTitle("Chess - Demo");
        setIconImage(ResourceHelper.loadImage("icon.png"));
        getContentPane().setLayout(null);

        initComponents();
        setLocationRelativeTo(null); // center screen

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                if (playerName == null) {
                    playerName = inputPlayerName();
                    if (playerName == null) {
                        System.exit(0);
                    }
                }
                super.componentShown(e);
            }
        });

        ChessAppRunnable appRunnable = new ChessAppRunnable(this);
        ChessApp.getExecutorService().execute(appRunnable);
    }

    private String inputPlayerName() {
        NewPlayerDialog getName = new NewPlayerDialog(this);
        getName.setLocationRelativeTo(this);
        getName.setModal(true);
        getName.setVisible(true);
        return getName.name;
    }

    public void reloadListTable(List<ChessTablePanel> list) {
        pnListTable.removeAll();
        pnListTable.validate();

        int i = 0;

        for (ChessTablePanel table : list) {
            pnListTable.add(table);

            if (i % 2 == 0) {
                pnListTable.setPreferredSize(new Dimension(260, 126 * (i / 2 + 1)));
            }

            i++;
            pnListTable.validate();
            pnListTable.repaint();
        }

        repaint();
    }

    private void createNewTable() {
        NewGameDialog newGameDialog = new NewGameDialog(this);
        newGameDialog.setLocationRelativeTo(this);
        newGameDialog.setModal(true);
        newGameDialog.setVisible(true);

        if (newGameDialog.clicked) {
            gameRunnable = new ChessGameRunnable(this);
            boolean isCreate = gameRunnable.create(playerName, newGameDialog.isWhite, newGameDialog.time);

            if (isCreate) {
                player = new GamePlayer(playerName, newGameDialog.isWhite);
                ChessGamePanel newGame = new ChessGamePanel(false, this);
                ChessGameFrame game = new ChessGameFrame(newGame, this);
                gameRunnable.game = newGame;
                newGame.getGamePlayerPanel().isOwner = true;
                newGame.getGamePlayerPanel().setCurrentPlayer(player);
                newGame.getChessBoardPanel().draw();
                ChessApp.getExecutorService().execute(gameRunnable);
                game.setVisible(true);
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "Game is FULL! Can't create more");
            }
        }
    }

    private void initComponents() {
        JLabel lbListTable = new JLabel("List Game:");
        lbListTable.setBounds(10, 10, 141, 20);
        lbListTable.setFont(new Font("Arial", Font.ITALIC, 16));
        getContentPane().add(lbListTable);

        pnListTable = new JPanel(new FlowLayout(FlowLayout.LEFT), true);
        pnListTable.setPreferredSize(new Dimension(260, 385));

        JScrollPane scroll = new JScrollPane();
        scroll.setBounds(10, 35, 281, 385);
        getContentPane().add(scroll);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setAutoscrolls(true);
        scroll.setViewportView(pnListTable);

        JButton btnNewGame = new JButton("New Game");
        btnNewGame.setBounds(295, 30, 100, 30);
        btnNewGame.addActionListener(evt -> createNewTable());
        getContentPane().add(btnNewGame);

        JButton btnExit = new JButton("Exit");
        btnExit.setBounds(295, 390, 100, 30);
        btnExit.addActionListener(evt -> System.exit(0));
        getContentPane().add(btnExit);

        pack();
    }
}

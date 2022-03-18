package vn.hqhung.demo.chess_app.components;

import vn.hqhung.demo.chess_app.components.chess_game.ChessGamePanel;
import vn.hqhung.demo.chess_app.helpers.ResourceHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @System: demo
 * @Title: Game Layout
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
public class ChessGameFrame extends JFrame {

    private final ChessAppFrame chessAppFrame;
    public boolean error = false;
    private final ChessGamePanel chessGamePanel;

    public ChessGameFrame(ChessGamePanel game, ChessAppFrame chessAppFrame) {
        this.chessAppFrame = chessAppFrame;
        this.chessGamePanel = game;

        setTitle("Chess - Demo");
        setIconImage(ResourceHelper.loadImage("icon.png"));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setBounds(new Rectangle(0, 0, 0, 0));
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                formWindowClosing();
            }
        });
        setPreferredSize(new Dimension(940, 680));
        getContentPane().setLayout(null);
        setLayout(new BorderLayout());

        initComponents();

        setLocationRelativeTo(null); // center screen
    }

    public void close() {
        dispose();
        if (!error) {
            chessAppFrame.gameRunnable.leave();
        }
        chessAppFrame.setVisible(true);
    }

    private void initComponents() {

        add(chessGamePanel);
        pack();
    }

    private void formWindowClosing() {
        if (!error) {
            chessAppFrame.gameRunnable.leave();
        }
        chessAppFrame.setVisible(true);
    }
}

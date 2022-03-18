package vn.hqhung.demo.chess_app.components.chess_game;

import lombok.Getter;
import vn.hqhung.demo.chess_app.components.ChessAppFrame;
import vn.hqhung.demo.chess_app.helpers.ResourceHelper;
import vn.hqhung.demo.common.models.GamePlayer;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

/**
 * @System: demo
 * @Title: Game Player
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
public class GamePlayerPanel extends JPanel {

    @Getter
    private GamePlayer currentPlayer;

    public void setCurrentPlayer(GamePlayer currentPlayer) {
        this.currentPlayer = currentPlayer;
        lblCurrentPlayerName.setText(currentPlayer.getName());
        if (isOwner) {
            btnReady.setVisible(true);
            btnKick.setVisible(true);
            btnKick.setEnabled(false);
        }
    }

    @Getter
    private GamePlayer otherPlayer;

    public void setOtherPlayer(GamePlayer otherPlayer) {
        this.otherPlayer = otherPlayer;
        lblOtherPlayerName.setText(otherPlayer.getName());
        btnKick.setEnabled(true);
    }

    public boolean isOwner = false;
    private final ChessAppFrame chessAppFrame;

    public GamePlayerPanel(ChessAppFrame chessAppFrame) {
        super();
        this.chessAppFrame = chessAppFrame;
        initComponents();
    }

    public JLabel lblImageCurrentPlayer;
    public JLabel lblCurrentPlayerName;
    public JLabel lblCurrentPlayerTime;
    public JLabel lblImageOtherPlayer;
    public JLabel lblOtherPlayerName;
    public JLabel lblOtherPlayerTime;
    private JPanel capturedPiecesOfCurrentPlayer, capturedPiecesOfOtherPlayer;
    public JButton btnKick, btnReady;

    public void loadListCapturePiece(Stack<ChessPiece> capturedPieces) {
        capturedPiecesOfCurrentPlayer.removeAll();
        capturedPiecesOfCurrentPlayer.validate();
        capturedPiecesOfCurrentPlayer.repaint();

        capturedPiecesOfOtherPlayer.removeAll();
        capturedPiecesOfOtherPlayer.validate();
        capturedPiecesOfOtherPlayer.repaint();

        if (capturedPieces != null) {
            Stack<ChessPiece> stk = new Stack<>();

            for (ChessPiece piece : capturedPieces)
                stk.push(piece);

            for (ChessPiece chessPiece : stk) {
                if (chessPiece.owner.isWhite()) {
                    JLabel imgOtherPiece = new JLabel(new ImageIcon(chessPiece.orgImage.getScaledInstance(25, 25, 0)));
                    imgOtherPiece.setPreferredSize(new Dimension(25, 25));
                    capturedPiecesOfOtherPlayer.add(imgOtherPiece);
                    capturedPiecesOfOtherPlayer.validate();
                    capturedPiecesOfOtherPlayer.repaint();
                } else {
                    JLabel imgPlayerPiece = new JLabel(new ImageIcon(chessPiece.orgImage.getScaledInstance(25, 25, 0)));
                    imgPlayerPiece.setPreferredSize(new Dimension(25, 25));
                    capturedPiecesOfCurrentPlayer.add(imgPlayerPiece);
                    capturedPiecesOfCurrentPlayer.validate();
                    capturedPiecesOfCurrentPlayer.repaint();
                }
            }
        }
    }

    private void initComponents() {
        setLayout(null);
        setDoubleBuffered(true); // use to draw
        lblImageCurrentPlayer = new JLabel();
        lblImageOtherPlayer = new JLabel();
        lblCurrentPlayerName = new JLabel();
        lblOtherPlayerName = new JLabel();
        lblOtherPlayerTime = new JLabel();
        lblCurrentPlayerTime = new JLabel();
        capturedPiecesOfCurrentPlayer = new JPanel(true);
        capturedPiecesOfOtherPlayer = new JPanel(true);
        btnKick = new JButton("Kick");
        btnReady = new JButton("OK");

        lblOtherPlayerTime.setHorizontalAlignment(SwingConstants.CENTER);
        lblOtherPlayerTime.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
        lblOtherPlayerTime.setFont(new Font("Arial", Font.BOLD, 20));
        lblOtherPlayerTime.setBounds(10, 10, 100, 40);
        add(lblOtherPlayerTime);

        lblCurrentPlayerTime.setHorizontalAlignment(SwingConstants.CENTER);
        lblCurrentPlayerTime.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
        lblCurrentPlayerTime.setFont(new Font("Arial", Font.BOLD, 20));
        lblCurrentPlayerTime.setBounds(10, 345, 100, 40);
        add(lblCurrentPlayerTime);

        capturedPiecesOfOtherPlayer.setBackground(Color.WHITE);
        capturedPiecesOfOtherPlayer.setBounds(120, 10, 50, 220);
        capturedPiecesOfOtherPlayer.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        add(capturedPiecesOfOtherPlayer);

        capturedPiecesOfCurrentPlayer.setBackground(Color.WHITE);
        capturedPiecesOfCurrentPlayer.setBounds(120, 345, 50, 220);
        capturedPiecesOfCurrentPlayer.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        add(capturedPiecesOfCurrentPlayer);

        lblImageOtherPlayer.setIcon(new ImageIcon(ResourceHelper.loadImage("profilePlayer.png")));
        lblImageOtherPlayer.setBounds(10, 60, 100, 170);
        lblImageOtherPlayer.setHorizontalAlignment(SwingConstants.CENTER);
        lblImageOtherPlayer.setHorizontalTextPosition(SwingConstants.CENTER);
        lblImageOtherPlayer.setForeground(new Color(255, 200, 0));
        lblImageOtherPlayer.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 18));
        lblImageOtherPlayer.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
        add(lblImageOtherPlayer);

        lblOtherPlayerName.setBounds(10, 240, 100, 40);
        lblOtherPlayerName.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 16));
        lblOtherPlayerName.setHorizontalAlignment(SwingConstants.CENTER);
        lblOtherPlayerName.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
        add(lblOtherPlayerName);

        btnKick.setBounds(30, 300, 60, 30);
        btnKick.setFont(new Font("Arial", Font.BOLD, 14));
        btnKick.addActionListener(e -> {
            btnKick.setEnabled(false);
            chessAppFrame.gameRunnable.kick();
            lblOtherPlayerName.setText("");
            lblImageOtherPlayer.setText("");
        });
        btnKick.setVisible(false);
        add(btnKick);

        lblImageCurrentPlayer.setIcon(new ImageIcon(ResourceHelper.loadImage("profilePlayer.png")));
        lblImageCurrentPlayer.setBounds(10, 395, 100, 170);
        lblImageCurrentPlayer.setHorizontalAlignment(SwingConstants.CENTER);
        lblImageCurrentPlayer.setHorizontalTextPosition(SwingConstants.CENTER);
        lblImageCurrentPlayer.setForeground(new Color(255, 200, 0));
        lblImageCurrentPlayer.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 18));
        lblImageCurrentPlayer.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
        add(lblImageCurrentPlayer);

        lblCurrentPlayerName.setBounds(10, 575, 100, 40);
        lblCurrentPlayerName.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 16));
        lblCurrentPlayerName.setHorizontalAlignment(SwingConstants.CENTER);
        lblCurrentPlayerName.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
        add(lblCurrentPlayerName);

        btnReady.setBounds(120, 575, 50, 40);
        btnReady.addActionListener(e -> {
            lblImageCurrentPlayer.setText("Ready");
            btnReady.setEnabled(false);
            chessAppFrame.gameRunnable.ready();
        });
        btnReady.setVisible(false);
        add(btnReady);
    }
}

package vn.hqhung.demo.chess_app.components.chess_game;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import vn.hqhung.demo.chess_app.components.ChessAppFrame;
import vn.hqhung.demo.chess_app.components.ChessGameFrame;
import vn.hqhung.demo.chess_app.helpers.ResourceHelper;
import vn.hqhung.demo.common.models.GamePlayer;
import vn.hqhung.demo.common.models.GameSetting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

/**
 * @System: demo
 * @Title: Chess Game
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
@Log4j
public class ChessGamePanel extends JPanel implements MouseListener, ComponentListener {

    public boolean isBlockedChessboard;
    @Getter
    private ChessBoardPanel chessBoardPanel;

    public GameSetting gameSetting;
    @Getter
    private GamePlayer activePlayer;
    public MoveHistoryTable moveHistoryTable;
    public ChatPanel chat;
    @Getter
    private GamePlayerPanel gamePlayerPanel;
    public boolean isPlay;
    public boolean viewer;

    private JButton btnUndo;
    private JButton btnDraw;
    private JButton btnLose;
    private JButton btnExit;
    private JLabel lblMoveHistory;
    private static final URL SOUND_NEW_GAME = ChessGamePanel.class.getResource("/sounds/newGame.wav");
    private int undo, draw_lose;

    private final ChessAppFrame chessAppFrame;

    public ChessGamePanel(boolean viewer, ChessAppFrame chessAppFrame) {
        this.viewer = viewer;
        this.chessAppFrame = chessAppFrame;
        isBlockedChessboard = false;
        isPlay = false;
        setLayout(null);
        setDoubleBuffered(true);
        initComponent();
    }

    private void initComponent() {
        gameSetting = new GameSetting();
        moveHistoryTable = new MoveHistoryTable(this);
        chessBoardPanel = new ChessBoardPanel(this, gameSetting, moveHistoryTable);
        chessBoardPanel.setVisible(true);
        chessBoardPanel.setSize(ChessBoardPanel.BOARD_SIZE, ChessBoardPanel.BOARD_SIZE);
        chessBoardPanel.addMouseListener(this);
        chessBoardPanel.setLocation(new Point(180, 10));
        add(chessBoardPanel);

        gamePlayerPanel = new GamePlayerPanel(chessAppFrame);
        gamePlayerPanel.setSize(new Dimension(170, 670));
        gamePlayerPanel.setLocation(new Point(0, 0));
        add(gamePlayerPanel);

        lblMoveHistory = new JLabel("Move Log:");
        lblMoveHistory.setFont(new Font("Arial", Font.BOLD, 18));
        lblMoveHistory.setBounds(715, 10, 180, 31);
        add(lblMoveHistory);

        JScrollPane movesHistory = moveHistoryTable.getScrollPane();
        movesHistory.setSize(new Dimension(200, 480));
        movesHistory.setLocation(new Point(710, 40));
        add(movesHistory);

        chat = new ChatPanel(chessAppFrame);
        chat.setSize(new Dimension(ChessBoardPanel.BOARD_SIZE, 100));
        chat.setLocation(new Point(180, 530));
        chat.setMinimumSize(new Dimension(ChessBoardPanel.BOARD_SIZE, 100));
        add(chat);

        if (!viewer) {
            btnUndo = new JButton("Undo");
            btnUndo.setSize(new Dimension(95, 35));
            btnUndo.setLocation(new Point(710, 530));
            btnUndo.setEnabled(false);
            btnUndo.addActionListener(e -> {
                chessAppFrame.gameRunnable.undo();
                btnUndo.setEnabled(false);
            });
            add(btnUndo);

            btnDraw = new JButton("Draw");
            btnDraw.setSize(new Dimension(95, 35));
            btnDraw.setLocation(new Point(805, 530));
            btnDraw.setEnabled(false);
            btnDraw.addActionListener(e -> chessAppFrame.gameRunnable.draw());
            add(btnDraw);

            btnLose = new JButton("Lose");
            btnLose.setSize(new Dimension(95, 35));
            btnLose.setLocation(new Point(710, 570));
            btnLose.setEnabled(false);
            btnLose.addActionListener(e -> {
                chessAppFrame.gameRunnable.lose();
                endGame("You LOSE!");
                if (gamePlayerPanel.isOwner) gamePlayerPanel.btnKick.setEnabled(true);
            });
            add(btnLose);
        }

        btnExit = new JButton("Exit");
        btnExit.setSize(new Dimension(95, 35));
        btnExit.setLocation(new Point(805, 570));
        btnExit.addActionListener(e -> exit());
        add(btnExit);
    }

    public void exit() {
        ChessGameFrame playGame = (ChessGameFrame) getRootPane().getParent();
        playGame.close();
    }

    public void newGame() {
        draw_lose = 0;
        undo = 0;

        gamePlayerPanel.lblImageCurrentPlayer.setText("");
        gamePlayerPanel.lblImageOtherPlayer.setText("");

        moveHistoryTable.removeAllRows();
        gamePlayerPanel.loadListCapturePiece(null);

        if (!viewer) {
            gamePlayerPanel.btnKick.setVisible(false);
            gamePlayerPanel.btnReady.setVisible(false);
        }

        chessBoardPanel.removeAllPieces();
        chessBoardPanel.setChessPieces("", gameSetting.whitePlayer, gameSetting.blackPlayer);

        isBlockedChessboard = false;

        activePlayer = gameSetting.whitePlayer;
        if (!activePlayer.isCanMove()) {
            isBlockedChessboard = true;
        }

        chessBoardPanel.repaint();
        repaint();
    }

    public void endGame(String massage) {
        isBlockedChessboard = true;

        isPlay = false;

        if (!viewer) {
            gamePlayerPanel.btnReady.setVisible(true);
            gamePlayerPanel.btnReady.setEnabled(true);

            btnDraw.setEnabled(false);
            btnLose.setEnabled(false);
            btnUndo.setEnabled(false);
        }

        if (gamePlayerPanel.isOwner) {
            gamePlayerPanel.btnKick.setVisible(true);
        }
        JOptionPane.showMessageDialog(this, massage);
    }

    public void switchActive() {
        if (activePlayer == gameSetting.whitePlayer) {
            activePlayer = gameSetting.blackPlayer;
        } else {
            activePlayer = gameSetting.whitePlayer;
        }
    }

    public void nextMove() {
        switchActive();

        isBlockedChessboard = !activePlayer.isCanMove();
    }

    public void simulateMove(int beginX, int beginY, int endX, int endY, ChessPiece piece) {
        try {
            chessBoardPanel.select(chessBoardPanel.cells[beginX][beginY]);
            if (chessBoardPanel.activeCell.getChessPiece() != null && chessBoardPanel.activeCell.getChessPiece().getAllCellsCanMove().contains(chessBoardPanel.cells[endX][endY])) {
                chessBoardPanel.move(chessBoardPanel.cells[beginX][beginY], chessBoardPanel.cells[endX][endY], piece);
            } else {
                return;
            }

            undo++;
            draw_lose++;
            // need more 7 step to checkmate
            if (!viewer && draw_lose >= 6) {
                btnDraw.setEnabled(true);
                btnLose.setEnabled(true);
            }

            chessBoardPanel.unselect();
            nextMove();

            checkmatedOrStalemated();
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException | NullPointerException exc) {
            log.error(exc.getMessage(), exc);
        }
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    public void undo(boolean isSender) {
        if (!viewer) {
            draw_lose--;
            if ((undo % 2 == 0 && activePlayer.isWhite() && (activePlayer.isCanMove() == isSender)) || (undo > 1 && undo % 2 != 0 && !activePlayer.isWhite() && (activePlayer.isCanMove() == isSender))) {
                chessBoardPanel.undoMove();
                undo--;
                draw_lose--;
            } else {
                nextMove();
            }

            if (!viewer && draw_lose < 6) {
                btnDraw.setEnabled(false);
                btnLose.setEnabled(false);
            }

            if (undo == 0) {
                btnUndo.setEnabled(false);
            }
        } else {
            if (undo > 1 && !chessAppFrame.gameRunnable.name.equals(activePlayer.getName())) {
                chessBoardPanel.undoMove();
                undo--;
            } else {
                nextMove();
            }
        }

        chessBoardPanel.undoMove();
        undo--;
    }

    public void mousePressed(MouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON1) {
            if (!isBlockedChessboard) {
                try {
                    //get X position of mouse
                    int x = event.getX();
                    //get Y position of mouse
                    int y = event.getY();

                    ChessCell cell = chessBoardPanel.getCell(x, y);
                    // check cell active and cell click is valid
                    if (((cell == null || cell.getChessPiece() == null) && chessBoardPanel.activeCell == null) || (chessBoardPanel.activeCell == null && cell.getChessPiece() != null && cell.getChessPiece().owner != activePlayer)) {
                        return;
                    }
                    if (cell != null && cell.getChessPiece() != null && cell.getChessPiece().owner == activePlayer && cell != chessBoardPanel.activeCell) {
                        chessBoardPanel.unselect();
                        chessBoardPanel.select(cell);
                    } else if (chessBoardPanel.activeCell == cell) {
                        chessBoardPanel.unselect();
                    } else if (cell != null && chessBoardPanel.activeCell != null && chessBoardPanel.activeCell.getChessPiece() != null && chessBoardPanel.activeCell.getChessPiece().getAllCellsCanMove().contains(cell)) {
                        ChessCell begin = chessBoardPanel.activeCell;
                        chessBoardPanel.move(chessBoardPanel.activeCell, cell, null);
                        chessAppFrame.gameRunnable.sendMove(begin.posX, begin.posY, cell.posX, cell.posY, cell.getChessPiece());

                        chessBoardPanel.unselect();
                        btnUndo.setEnabled(true);

                        undo++;
                        draw_lose++;
                        if (!viewer && draw_lose >= 6) {
                            btnDraw.setEnabled(true);
                            btnLose.setEnabled(true);
                        }

                        //switch player
                        nextMove();

                        //checkmate or stalemate
                        checkmatedOrStalemated();
                    }

                } catch (NullPointerException exc) {
                    log.error("Error when click on board", exc);
                    chessBoardPanel.repaint();
                }
            }
        }
    }

    private void checkmatedOrStalemated() {
        King king;
        if (activePlayer == gameSetting.whitePlayer) {
            king = chessBoardPanel.kingWhite;
        } else {
            king = chessBoardPanel.kingBlack;
        }

        switch (king.isCheckmatedOrStalemated()) {
            case 1:
                chessAppFrame.gameRunnable.endGame();
                if (activePlayer.isWhite() == gamePlayerPanel.getCurrentPlayer().isWhite()) {
                    endGame("CHECKMATED! '" + gamePlayerPanel.getOtherPlayer().getName() + "' WIN!");
                } else {
                    if (!viewer)
                        endGame("'" + gamePlayerPanel.getOtherPlayer().getName() + "' was CHECKMATED! You WIN!");
                    else {
                        endGame("CHECKMATED! '" + gamePlayerPanel.getCurrentPlayer().getName() + "' WIN!");
                    }
                }
                isPlay = false;
                break;
            case 2:
                chessAppFrame.gameRunnable.endGame();
                endGame("DRAW!");
                isPlay = false;
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void componentResized(ComponentEvent e) {
        int size = chessBoardPanel.getHeight();

        lblMoveHistory.setLocation(new Point(185 + size + 5, 70));

        moveHistoryTable.getScrollPane().setLocation(new Point(180 + size + 5, 100));
        moveHistoryTable.getScrollPane().setSize(moveHistoryTable.getScrollPane().getWidth(), size - 100);

        chat.setLocation(new Point(180, size + 5));
        chat.setSize(new Dimension(size, getHeight() - (size + 5)));

        if (!viewer) {
            btnUndo.setLocation(new Point(180 + size + 5, size + 5));
            btnDraw.setLocation(new Point(285 + size + 5, size + 5));
            btnLose.setLocation(new Point(180 + size + 5, size + 50));
        }

        btnExit.setLocation(new Point(285 + size + 5, size + 50));
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        // do nothing
    }

    @Override
    public void componentShown(ComponentEvent e) {
        // do nothing
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // do nothing
    }
}

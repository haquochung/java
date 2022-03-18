package vn.hqhung.demo.chess_app.components.chess_game;

import lombok.Getter;
import vn.hqhung.demo.chess_app.helpers.ResourceHelper;
import vn.hqhung.demo.chess_app.models.Castling;
import vn.hqhung.demo.chess_app.models.Move;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

/**
 * @System: demo
 * @Title: Move History
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
public class MoveHistoryTable extends JTable {

    // CONFIG LAYOUT
    private static final String[] COLUMNS = new String[]{"", "White", "Black"};
    private int rowCount;

    static class MoveHistoryTableModel extends DefaultTableModel {
        MoveHistoryTableModel() {
            super();
        }

        public boolean isCellEditable(int a, int b) {
            return false;
        }
    }

    private final MoveHistoryTableModel tableModel;
    private JScrollPane scrollPane;

    // INFO
    private static final int CONVERT_NUM = 97;
    @Getter
    private final Stack<ChessPiece> capturedChessPieces = new Stack<>();
    @Getter
    private final Stack<Move> moveBackStack = new Stack<>();
    public List<String> move = new ArrayList<>();
    private boolean enterBlack = false;
    private ChessGamePanel chessGamePanel;

    public MoveHistoryTable(ChessGamePanel chessGamePanel) {
        this(new MoveHistoryTableModel());
        this.chessGamePanel = chessGamePanel;
    }

    private MoveHistoryTable(MoveHistoryTableModel tableModel) {
        super(tableModel);
        this.tableModel = tableModel;
        initialize();
    }

    private void initialize() {
        setMinimumSize(new Dimension(100, 200));
        for (String columnName : COLUMNS) {
            tableModel.addColumn(columnName);
        }
        getColumnModel().getColumn(0).setPreferredWidth(30);
        tableModel.addTableModelListener(null);

        scrollPane = new JScrollPane(this);
        scrollPane.setMaximumSize(new Dimension(100, 200));
        scrollPane.setAutoscrolls(true);
    }

    protected void addRow() {
        tableModel.addRow(new String[3]);
    }

    protected void addCastling(String value) {
        move.remove(move.size() - 1);
        if (!enterBlack) {
            tableModel.setValueAt(value, tableModel.getRowCount() - 1, 2);
        } else {
            tableModel.setValueAt(value, tableModel.getRowCount() - 1, 1);
        }
        move.add(value);
    }

    protected void addMoveToTable(String value) {
        try {
            if (!enterBlack) {
                addRow();
                rowCount = tableModel.getRowCount() - 1;
                tableModel.setValueAt(rowCount + 1, rowCount, 0);
                tableModel.setValueAt(value, rowCount, 1);
            } else {
                tableModel.setValueAt(value, rowCount, 2);
                rowCount = tableModel.getRowCount() - 1;
            }
            enterBlack = !enterBlack;
            scrollRectToVisible(getCellRect(tableModel.getRowCount() - 1, 0, true));
        } catch (ArrayIndexOutOfBoundsException exc) {
            if (rowCount > 0) {
                rowCount--;
                addMoveToTable(value);
            }
        }
    }

    public void addMove(ChessCell beginChessCell, ChessCell endChessCell, boolean registerInHistory, Castling castlingMove, boolean wasEnPassant, ChessPiece promotedChessPiece) {
        boolean wasChecked = false;
        chessGamePanel.getChessBoardPanel().isChecked = false;

        String locMove = beginChessCell.getChessPiece().getSymbol();

        if (chessGamePanel.gameSetting.upSideDown) {
            locMove += Character.toString((char) ((ChessBoardPanel.BOTTOM_BOARD - endChessCell.posX) + CONVERT_NUM));
            locMove += Integer.toString(beginChessCell.posY + 1);
        } else {
            locMove += Character.toString((char) (beginChessCell.posX + 97));
            locMove += Integer.toString(8 - beginChessCell.posY);
        }

        if (endChessCell.getChessPiece() != null) {
            locMove += "x";
            capturedChessPieces.push(endChessCell.getChessPiece());
            chessGamePanel.getGamePlayerPanel().loadListCapturePiece(capturedChessPieces);
        } else {
            locMove += "-";
        }

        if (chessGamePanel.gameSetting.upSideDown) {
            locMove += Character.toString((char) ((ChessBoardPanel.BOTTOM_BOARD - endChessCell.posX) + CONVERT_NUM));
            locMove += Integer.toString(endChessCell.posY + 1);
        } else {
            locMove += Character.toString((char) (endChessCell.posX + CONVERT_NUM));
            locMove += Integer.toString(8 - endChessCell.posY);
        }

        if (wasEnPassant) {
            locMove += "(e.p)"; // en passant
        }

        if ((!enterBlack && chessGamePanel.getChessBoardPanel().kingBlack.isChecked()) || (enterBlack && chessGamePanel.getChessBoardPanel().kingWhite.isChecked())) {
            //check if checkmated
            if ((!enterBlack && chessGamePanel.getChessBoardPanel().kingBlack.isCheckmatedOrStalemated() == 1) || (enterBlack && chessGamePanel.getChessBoardPanel().kingWhite.isCheckmatedOrStalemated() == 1)) {
                locMove += "#"; //checkmate
            } else {
                locMove += "+"; //check
                wasChecked = true;
                chessGamePanel.getChessBoardPanel().isChecked = true;
            }
        }

        switch (castlingMove) {
            case SHORT:
                addCastling("0-0");
                break;
            case LONG:
                addCastling("0-0-0");
                break;
            default:
                move.add(locMove);
                addMoveToTable(locMove);
                break;
        }

        scrollPane.scrollRectToVisible(new Rectangle(0, scrollPane.getHeight() - 2, 1, 1));

        if (registerInHistory) {
            moveBackStack.add(new Move(new ChessCell(beginChessCell), new ChessCell(endChessCell), beginChessCell.getChessPiece(), endChessCell.getChessPiece(), castlingMove, wasEnPassant, wasChecked, promotedChessPiece));
        }
    }

    public void removeAllRows() {
        for (int i = rowCount - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }
        rowCount = 0;
        tableModel.setNumRows(rowCount);
        enterBlack = false;
        move.clear();
        capturedChessPieces.clear();
        moveBackStack.clear();
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public synchronized Move getLastMoveFromHistory() {
        try {
            return moveBackStack.get(moveBackStack.size() - 1);
        } catch (ArrayIndexOutOfBoundsException exc) {
            return null;
        }
    }

    public Move undo() {
        try {
            Move lastMove = moveBackStack.pop();
            if (lastMove != null) {
                if (enterBlack) {
                    tableModel.removeRow(tableModel.getRowCount() - 1);
                    if (rowCount > 0) {
                        rowCount--;
                    }
                } else if (tableModel.getRowCount() > 0) {
                    tableModel.setValueAt("", tableModel.getRowCount() - 1, 2);
                }
                if (lastMove.getTakenChessPiece() != null) {
                    capturedChessPieces.pop();
                    chessGamePanel.getGamePlayerPanel().loadListCapturePiece(capturedChessPieces);
                }

                move.remove(move.size() - 1);
                enterBlack = !enterBlack;
            }
            return lastMove;
        } catch (EmptyStackException exc) {
            enterBlack = false;
            return null;
        } catch (ArrayIndexOutOfBoundsException exc) {
            return null;
        }
    }
}

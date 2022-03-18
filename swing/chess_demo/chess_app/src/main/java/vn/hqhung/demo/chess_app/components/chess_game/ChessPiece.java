package vn.hqhung.demo.chess_app.components.chess_game;

import lombok.Getter;
import lombok.Setter;
import vn.hqhung.demo.common.models.GamePlayer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @System: demo
 * @Title: Chess Piece
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
public abstract class ChessPiece {
    @Getter
    @Setter
    private ChessPieceType chessPieceType;

    protected GamePlayer owner;

    @Getter
    protected ChessBoardPanel chessBoard;


    protected Image imageBlack;
    protected Image imageWhite;
    protected Image orgImage;
    private Image image;

    protected ChessCell chessCell;

    public ChessPiece(ChessBoardPanel chessBoard, GamePlayer gamePlayer) {
        this.chessBoard = chessBoard;
        this.owner = gamePlayer;
    }

    void setImage() {
        if (owner.isWhite()) {
            image = imageWhite;
        } else {
            image = imageBlack;
        }
        orgImage = image;
    }

    final void draw(Graphics g) {
        if (g == null) {
            return;
        }
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Point topLeft = chessBoard.getTopLeftPoint();

        int size = ChessBoardPanel.CELL_SIZE;

        int x = (chessCell.posX * size) + topLeft.x;
        int y = (chessCell.posY * size) + topLeft.y;

        if (image != null) {
            Image tempImage = orgImage;
            BufferedImage resized = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D imageGr = resized.createGraphics();
            imageGr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            imageGr.drawImage(tempImage, 0, 0, size, size, null);
            imageGr.dispose();
            image = resized.getScaledInstance(size, size, 0);
            g2D.drawImage(image, x, y, null);
        }
    }

    /**
     * check cell can move and store to list
     *
     * @param x    position x
     * @param y    position y
     * @param list list to store cell can move
     * @return true if cell can't move
     */
    protected boolean checkAndStoreCanMoveChessCells(int x, int y, List<ChessCell> list) {
        if (isOut(x, y)) {
            return true;
        }
        if (checkChessPiece(x, y)) {
            ChessCell chessCell = chessBoard.cells[x][y];
            if (owner.isWhite()) {
                if (chessBoard.kingWhite.willBeSafeWhenMoveOtherPiece(this.chessCell, chessCell)) {
                    list.add(chessCell);
                }
            } else {
                if (chessBoard.kingBlack.willBeSafeWhenMoveOtherPiece(this.chessCell, chessCell)) {
                    list.add(chessCell);
                }
            }
            return isOtherOwner(x, y);
        }
        return true;
    }

    abstract public List<ChessCell> getAllCellsCanMove();

    protected boolean isOut(int x, int y) {
        return x < 0 || x > 7 || y < 0 || y > 7;
    }

    /**
     * check if cell will move to is contains King Piece of the other owner
     *
     * @param x position X
     * @param y position Y
     * @return true/false
     */
    protected boolean checkChessPiece(int x, int y) {
        ChessPiece chessPiece = chessBoard.cells[x][y].getChessPiece();
        if (chessPiece != null) {
            if (chessPiece.getChessPieceType() == ChessPieceType.KING) {
                return false;
            }
            return owner != chessPiece.owner;
        }
        return true;
    }

    protected boolean isOtherOwner(int x, int y) {
        ChessPiece chessPiece = chessBoard.cells[x][y].getChessPiece();
        if (chessPiece == null) {
            return false;
        }

        return owner != chessPiece.owner;
    }

    public String getSymbol() {
        return getChessPieceType().getSymbol();
    }
}

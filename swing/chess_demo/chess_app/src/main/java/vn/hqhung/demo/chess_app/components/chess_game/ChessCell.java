package vn.hqhung.demo.chess_app.components.chess_game;

import lombok.Getter;

/**
 * @System: demo
 * @Title: Chess Cell
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
public class ChessCell {
    public int posX;
    public int posY;

    @Getter
    private ChessPiece chessPiece;

    void setChessPiece(ChessPiece chessPiece) {
        if (chessPiece != null) {
            this.chessPiece = chessPiece;
            this.chessPiece.chessCell = this;
        } else {
            this.chessPiece = null;
        }
    }

    public ChessCell(int posX, int posY, ChessPiece chessPiece) {
        this.posX = posX;
        this.posY = posY;
        this.chessPiece = chessPiece;
    }

    public ChessCell(ChessCell cell) {
        this.posX = cell.posX;
        this.posY = cell.posY;
        this.chessPiece = cell.chessPiece;
    }
}

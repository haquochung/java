package vn.hqhung.demo.chess_app.models;

import lombok.Getter;
import vn.hqhung.demo.chess_app.components.chess_game.ChessBoardPanel;
import vn.hqhung.demo.chess_app.components.chess_game.ChessCell;
import vn.hqhung.demo.chess_app.components.chess_game.ChessPiece;
import vn.hqhung.demo.chess_app.components.chess_game.ChessPieceType;

/**
 * @System: demo
 * @Title: Move of Chess Piece
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
public class Move {
    @Getter
    private final ChessCell fromChessCell;
    @Getter
    private final ChessCell toChessCell;
    @Getter
    private final boolean wasChecked;
    @Getter
    private final ChessPiece movedChessPiece;
    @Getter
    private final ChessPiece takenChessPiece;
    @Getter
    private ChessPiece promotedChessPiece;
    @Getter
    private final boolean wasEnPassant;
    @Getter
    private final Castling castlingMove;
    @Getter
    private boolean wasMovePawnTwoCells;

    public Move(ChessCell fromChessCell, ChessCell toChessCell, ChessPiece movedChessPiece, ChessPiece takenChessPiece, Castling castlingMove, boolean wasEnPassant, boolean wasChecked, ChessPiece promotedChessPiece) {
        this.fromChessCell = fromChessCell;
        this.toChessCell = toChessCell;

        this.movedChessPiece = movedChessPiece;
        this.takenChessPiece = takenChessPiece;

        this.castlingMove = castlingMove;
        this.wasEnPassant = wasEnPassant;
        this.wasChecked = wasChecked;

        if (movedChessPiece.getChessPieceType() == ChessPieceType.PAWN && Math.abs(toChessCell.posX - getFromChessCell().posY) == 2) {
            this.wasMovePawnTwoCells = true;
        } else if (movedChessPiece.getChessPieceType() == ChessPieceType.PAWN && (toChessCell.posY == ChessBoardPanel.BOTTOM_BOARD || toChessCell.posY == ChessBoardPanel.TOP_BOARD) && promotedChessPiece != null) {
            // check to store promoted chess piece
            this.promotedChessPiece = promotedChessPiece;
        }
    }
}

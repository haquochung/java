package vn.hqhung.demo.chess_app.components.chess_game;


import vn.hqhung.demo.chess_app.helpers.ResourceHelper;
import vn.hqhung.demo.common.models.GamePlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * @System: demo
 * @Title: Pawn Piece
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
public final class Pawn extends ChessPiece {

    @Override
    public ChessPieceType getChessPieceType() {
        return ChessPieceType.PAWN;
    }

    public Pawn(ChessBoardPanel chessboard, GamePlayer owner) {
        super(chessboard, owner);
        imageWhite = ResourceHelper.loadImage("White_P.png");
        imageBlack = ResourceHelper.loadImage("Black_P.png");
        setImage();
    }

    @Override
    public List<ChessCell> getAllCellsCanMove() {
        List<ChessCell> list = new ArrayList<>();
        ChessCell cell;
        ChessCell cell1;
        ChessPiece chessPiece;

        int first = chessCell.posY - 1;
        int second = chessCell.posY - 2;

        if (owner.isGoDown()) {
            first = chessCell.posY + 1;
            second = chessCell.posY + 2;
        }
        if (isOut(first, first)) {
            return list;
        }

        cell = chessBoard.cells[chessCell.posX][first];
        if (cell.getChessPiece() == null) {
            if (owner.isWhite()) {
                if (chessBoard.kingWhite.willBeSafeWhenMoveOtherPiece(chessCell, cell)) {
                    list.add(cell);
                }
            } else {
                if (chessBoard.kingBlack.willBeSafeWhenMoveOtherPiece(chessCell, cell)) {
                    list.add(cell);
                }
            }

            if ((owner.isGoDown() && chessCell.posY == 1) || (!owner.isGoDown() && chessCell.posY == 6)) {
                cell1 = chessBoard.cells[chessCell.posX][second];
                if (cell1.getChessPiece() == null) {
                    if (owner.isWhite()) {
                        if (chessBoard.kingWhite.willBeSafeWhenMoveOtherPiece(chessCell, cell1)) {
                            list.add(cell1);
                        }
                    } else {
                        if (chessBoard.kingBlack.willBeSafeWhenMoveOtherPiece(chessCell, cell1)) {
                            list.add(cell1);
                        }
                    }
                }
            }
        }

        if (!isOut(chessCell.posX - 1, chessCell.posY)) {
            // capture piece
            if (chessBoard.cells[chessCell.posX - 1][first].getChessPiece() != null) {
                checkAndStoreCanMoveChessCells(chessCell.posX - 1, first, list);
            }

            // en passant
            cell1 = chessBoard.cells[chessCell.posX - 1][chessCell.posY];
            chessPiece = cell1.getChessPiece();
            if (chessPiece != null && chessBoard.pawnMovedTwoCell != null && cell1 == chessBoard.pawnMovedTwoCell.chessCell) {
                checkAndStoreCanMoveChessCells(chessCell.posX - 1, first, list);
            }
        }

        if (!isOut(chessCell.posX + 1, chessCell.posY)) {
            // capture piece
            if (chessBoard.cells[chessCell.posX + 1][first].getChessPiece() != null) {
                checkAndStoreCanMoveChessCells(chessCell.posX + 1, first, list);
            }

            // en passant
            cell1 = chessBoard.cells[chessCell.posX + 1][chessCell.posY];
            chessPiece = cell1.getChessPiece();
            if (chessPiece != null && chessBoard.pawnMovedTwoCell != null && cell1 == chessBoard.pawnMovedTwoCell.chessCell) {
                checkAndStoreCanMoveChessCells(chessCell.posX + 1, first, list);
            }
        }

        return list;
    }
}

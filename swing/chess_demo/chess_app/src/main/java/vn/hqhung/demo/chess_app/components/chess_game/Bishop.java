package vn.hqhung.demo.chess_app.components.chess_game;


import vn.hqhung.demo.chess_app.helpers.ResourceHelper;
import vn.hqhung.demo.common.models.GamePlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * @System: demo
 * @Title: Bishop Piece
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
public final class Bishop extends ChessPiece {

    @Override
    public ChessPieceType getChessPieceType() {
        return ChessPieceType.BISHOP;
    }

    public Bishop(ChessBoardPanel chessboard, GamePlayer owner) {
        super(chessboard, owner);
        imageWhite = ResourceHelper.loadImage("White_B.png");
        imageBlack = ResourceHelper.loadImage("Black_B.png");
        setImage();
    }

    @Override
    public List<ChessCell> getAllCellsCanMove() {
        List<ChessCell> list = new ArrayList<>();

        // left-up-side
        for (int x = chessCell.posX - 1, y = chessCell.posY + 1; !isOut(x, y); --x, ++y) {
            if (checkAndStoreCanMoveChessCells(x, y, list)) {
                break;
            }
        }

        // left-down-side
        for (int x = chessCell.posX - 1, y = chessCell.posY - 1; !isOut(x, y); --x, --y) {
            if (checkAndStoreCanMoveChessCells(x, y, list)) {
                break;
            }
        }

        // right-up-side
        for (int x = chessCell.posX + 1, y = chessCell.posY + 1; !isOut(x, y); ++x, ++y) {
            if (checkAndStoreCanMoveChessCells(x, y, list)) {
                break;
            }
        }

        // left-down-side
        for (int x = chessCell.posX + 1, y = chessCell.posY - 1; !isOut(x, y); ++x, --y) {
            if (checkAndStoreCanMoveChessCells(x, y, list)) {
                break;
            }
        }

        return list;
    }
}

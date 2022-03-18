package vn.hqhung.demo.chess_app.components.chess_game;

import vn.hqhung.demo.chess_app.helpers.ResourceHelper;
import vn.hqhung.demo.common.models.GamePlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * @System: demo
 * @Title: Queen Piece
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
public final class Queen extends ChessPiece {
    @Override
    public ChessPieceType getChessPieceType() {
        return ChessPieceType.QUEEN;
    }

    public Queen(ChessBoardPanel chessboard, GamePlayer owner) {
        super(chessboard, owner);
        imageWhite = ResourceHelper.loadImage("White_Q.png");
        imageBlack = ResourceHelper.loadImage("Black_Q.png");
        setImage();
    }

    @Override
    public List<ChessCell> getAllCellsCanMove() {
        List<ChessCell> list = new ArrayList<>();

        // up-side
        for (int y = chessCell.posY + 1; y <= 7; ++y) {
            if (checkAndStoreCanMoveChessCells(chessCell.posX, y, list)) {
                break;
            }
        }

        // down-side
        for (int y = chessCell.posY - 1; y >= 0; --y) {
            if (checkAndStoreCanMoveChessCells(chessCell.posX, y, list)) {
                break;
            }
        }

        // left-side
        for (int x = chessCell.posX - 1; x >= 0; --x) {
            if (checkAndStoreCanMoveChessCells(x, chessCell.posY, list)) {
                break;
            }
        }

        // right-side
        for (int x = chessCell.posX + 1; x <= 7; ++x) {
            if (checkAndStoreCanMoveChessCells(x, chessCell.posY, list)) {
                break;
            }
        }

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

        // right-down-side
        for (int x = chessCell.posX + 1, y = chessCell.posY - 1; !isOut(x, y); ++x, --y) {
            if (checkAndStoreCanMoveChessCells(x, y, list)) {
                break;
            }
        }

        return list;
    }
}

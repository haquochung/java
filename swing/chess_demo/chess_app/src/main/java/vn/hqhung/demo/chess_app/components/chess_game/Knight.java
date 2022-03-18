package vn.hqhung.demo.chess_app.components.chess_game;


import vn.hqhung.demo.chess_app.helpers.ResourceHelper;
import vn.hqhung.demo.common.models.GamePlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * @System: demo
 * @Title: Knight Piece
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
public final class Knight extends ChessPiece {
    @Override
    public ChessPieceType getChessPieceType() {
        return ChessPieceType.KNIGHT;
    }

    public Knight(ChessBoardPanel chessboard, GamePlayer owner) {
        super(chessboard, owner);
        imageWhite = ResourceHelper.loadImage("White_N.png");
        imageBlack = ResourceHelper.loadImage("Black_N.png");
        setImage();
    }

    @Override
    public List<ChessCell> getAllCellsCanMove() {
        List<ChessCell> list = new ArrayList<>();

        int newX, newY;

        // position 1
        newX = chessCell.posX - 2;
        newY = chessCell.posY + 1;
        checkAndStoreCanMoveChessCells(newX, newY, list);

        // position 2
        newX = chessCell.posX - 1;
        newY = chessCell.posY + 2;
        checkAndStoreCanMoveChessCells(newX, newY, list);

        // position 3
        newX = chessCell.posX + 1;
        newY = chessCell.posY + 2;
        checkAndStoreCanMoveChessCells(newX, newY, list);

        // position 4
        newX = chessCell.posX + 2;
        newY = chessCell.posY + 1;
        checkAndStoreCanMoveChessCells(newX, newY, list);

        // position 5
        newX = chessCell.posX + 2;
        newY = chessCell.posY - 1;
        checkAndStoreCanMoveChessCells(newX, newY, list);

        // position 6
        newX = chessCell.posX + 1;
        newY = chessCell.posY - 2;
        checkAndStoreCanMoveChessCells(newX, newY, list);

        // position 7
        newX = chessCell.posX - 1;
        newY = chessCell.posY - 2;
        checkAndStoreCanMoveChessCells(newX, newY, list);

        // position 8
        newX = chessCell.posX - 2;
        newY = chessCell.posY - 1;
        checkAndStoreCanMoveChessCells(newX, newY, list);

        return list;
    }
}



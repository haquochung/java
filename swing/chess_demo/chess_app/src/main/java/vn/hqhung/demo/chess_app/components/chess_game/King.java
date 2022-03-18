package vn.hqhung.demo.chess_app.components.chess_game;

import vn.hqhung.demo.chess_app.helpers.ResourceHelper;
import vn.hqhung.demo.common.models.GamePlayer;
import vn.hqhung.demo.chess_app.models.Move;

import java.util.ArrayList;
import java.util.List;


/**
 * @System: demo
 * @Title: King Piece
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
public final class King extends ChessPiece {
    boolean wasMotion = false;

    @Override
    public ChessPieceType getChessPieceType() {
        return ChessPieceType.KING;
    }

    public King(ChessBoardPanel chessboard, GamePlayer owner) {
        super(chessboard, owner);
        imageWhite = ResourceHelper.loadImage("White_K.png");
        imageBlack = ResourceHelper.loadImage("Black_K.png");
        setImage();
    }

    @Override
    public List<ChessCell> getAllCellsCanMove() {
        List<ChessCell> list = new ArrayList<>();
        ChessCell cell;
        ChessCell cell1;

        for (int x = chessCell.posX - 1; x <= chessCell.posX + 1; x++) {
            for (int y = chessCell.posY - 1; y <= chessCell.posY + 1; y++) {
                if (!isOut(x, y)) {
                    cell = chessBoard.cells[x][y];
                    if (chessCell == cell) {
                        continue;
                    }
                    if (checkChessPiece(x, y)) {
                        if (isSafe(cell)) {
                            list.add(cell);
                        }
                    }
                }
            }
        }

        if (!wasMotion && !isChecked()) {
            ChessPiece chessPiece = chessBoard.cells[0][chessCell.posY].getChessPiece();
            Rook rook;
            if (chessPiece != null && chessPiece.getChessPieceType() == ChessPieceType.ROOK) {
                boolean canCastling = true;
                rook = (Rook) chessPiece;
                if (!rook.wasMotion) {
                    for (int x = chessCell.posX - 1; x > 0; x--) {
                        if (chessBoard.cells[x][chessCell.posY].getChessPiece() != null) {
                            canCastling = false;
                            break;
                        }
                    }

                    cell = chessBoard.cells[chessCell.posX - 2][chessCell.posY];
                    cell1 = chessBoard.cells[chessCell.posX - 1][chessCell.posY];

                    if (canCastling && isSafe(cell) && isSafe(cell1)) {
                        list.add(cell);
                    }
                }
            }

            chessPiece = chessBoard.cells[7][chessCell.posY].getChessPiece();
            if (chessPiece != null && chessPiece.getChessPieceType() == ChessPieceType.ROOK) {
                boolean canCastling = true;
                rook = (Rook) chessPiece;
                if (!rook.wasMotion) {
                    for (int x = chessCell.posX + 1; x < 7; x++) {
                        if (chessBoard.cells[x][chessCell.posY].getChessPiece() != null) {
                            canCastling = false;
                            break;
                        }
                    }
                    cell = chessBoard.cells[chessCell.posX + 2][chessCell.posY];
                    cell1 = chessBoard.cells[chessCell.posX + 1][chessCell.posY];

                    if (canCastling && isSafe(cell) && isSafe(cell1)) {
                        list.add(cell);
                    }
                }
            }
        }
        return list;
    }

    public boolean isChecked() {
        return !isSafe(chessCell);
    }

    private boolean isEnoughPieceForCheckmate() {
        int blackKnight = 0;
        int whiteKnight = 0;
        boolean blackBishopOnBlackCell = false;
        boolean blackBishopOnWhiteCell = false;
        boolean whiteBishopOnBlackCell = false;
        boolean whiteBishopOnWhiteCell = false;

        ChessPiece chessPiece;
        for (int x = 0; x < 8; ++x) {
            for (int y = 0; y < 8; ++y) {
                chessPiece = chessBoard.cells[x][y].getChessPiece();
                if (chessPiece != null) {
                    if (chessPiece.owner.isWhite()) {
                        switch (chessPiece.getChessPieceType()) {
                            case PAWN:
                            case ROOK:
                            case QUEEN:
                                return true;
                            case BISHOP:
                                if ((x + y) % 2 == 0) {
                                    whiteBishopOnWhiteCell = true;
                                } else {
                                    whiteBishopOnBlackCell = true;
                                }
                                break;
                            case KNIGHT:
                                whiteKnight++;
                                break;
                        }
                    } else {
                        switch (chessPiece.getChessPieceType()) {
                            case PAWN:
                            case ROOK:
                            case QUEEN:
                                return true;
                            case BISHOP:
                                if ((x + y) % 2 == 0) {
                                    blackBishopOnWhiteCell = true;
                                } else {
                                    blackBishopOnBlackCell = true;
                                }
                                break;
                            case KNIGHT:
                                blackKnight++;
                                break;
                        }
                    }
                }
            }
        }

        if (whiteKnight > 1 || blackKnight > 1) {
            return true;
        }
        if ((whiteKnight > 0 && (whiteBishopOnWhiteCell || whiteBishopOnBlackCell)) || (blackKnight > 0 && (blackBishopOnWhiteCell || blackBishopOnBlackCell))) {
            return true;
        }
        if ((whiteBishopOnWhiteCell && whiteBishopOnBlackCell) || (blackBishopOnWhiteCell)) {
            return true;
        }
        return whiteBishopOnWhiteCell && blackBishopOnBlackCell;
    }

    private boolean isCheckThreeOldMove() {
        ArrayList<Move> move = new ArrayList<>(chessBoard.moveHistory.getMoveBackStack());
        for (int i = 0; i < move.size() - 1; i++) {
            int count = 1;
            if (move.get(i).isWasChecked()) {
                Move m1 = move.get(i);
                for (int j = i + 1; j < move.size(); j++) {
                    Move m2 = move.get(j);
                    if (m2.isWasChecked() && m1.getMovedChessPiece() == m2.getMovedChessPiece() && m1.getFromChessCell().posX == m2.getFromChessCell().posX && m1.getFromChessCell().posY == m2.getFromChessCell().posY && m1.getToChessCell().posX == m2.getToChessCell().posX && m1.getToChessCell().posY == m2.getToChessCell().posY) {
                        count++;
                    }
                }

                if (count > 3) {
                    return true;
                }
            }
        }

        return false;
    }

    public int isCheckmatedOrStalemated() {
        if (!isEnoughPieceForCheckmate()) {
            return 2;
        }

        if (isCheckThreeOldMove()) {
            return 2;
        }

        ChessPiece chessPiece;
        for (int x = 0; x < 8; ++x) {
            for (int y = 0; y < 8; ++y) {
                chessPiece = chessBoard.cells[x][y].getChessPiece();
                if (chessPiece != null && chessPiece.owner == owner && !chessPiece.getAllCellsCanMove().isEmpty()) {
                    return 0;
                }
            }
        }

        if (isChecked()) {
            return 1;
        } else if (getAllCellsCanMove().isEmpty()) {
            return 1;
        }

        return 2;

    }

    private int comparePiece(ChessPiece chessPiece, ChessPieceType... chessPieceTypes) {
        if (chessPiece == null || chessPiece == this) {
            return 0;
        } else if (chessPiece.owner != owner) {
            for (ChessPieceType chessPieceType : chessPieceTypes) {
                if (chessPiece.getChessPieceType() == chessPieceType) {
                    return 1;
                }
            }
            return -1;
        } else {
            return -1;
        }
    }

    private boolean isSafe(ChessCell cell) {
        ChessPiece chessPiece;
        int resultCompare;
        // Rook & Queen
        // check up-side
        for (int y = cell.posY + 1; y <= 7; ++y) {
            chessPiece = chessBoard.cells[cell.posX][y].getChessPiece();
            resultCompare = comparePiece(chessPiece, ChessPieceType.ROOK, ChessPieceType.QUEEN);
            if (resultCompare == -1) {
                break;
            } else if (resultCompare == 1) {
                return false;
            }
        }
        // check down-side
        for (int y = cell.posY - 1; y >= 0; --y) {
            chessPiece = chessBoard.cells[cell.posX][y].getChessPiece();
            resultCompare = comparePiece(chessPiece, ChessPieceType.ROOK, ChessPieceType.QUEEN);
            if (resultCompare == -1) {
                break;
            } else if (resultCompare == 1) {
                return false;
            }
        }
        // check left-side
        for (int x = cell.posX - 1; x >= 0; --x) {
            chessPiece = chessBoard.cells[x][cell.posY].getChessPiece();
            resultCompare = comparePiece(chessPiece, ChessPieceType.ROOK, ChessPieceType.QUEEN);
            if (resultCompare == -1) {
                break;
            } else if (resultCompare == 1) {
                return false;
            }
        }
        // check right-side
        for (int x = cell.posX + 1; x <= 7; ++x) {
            chessPiece = chessBoard.cells[x][cell.posY].getChessPiece();
            resultCompare = comparePiece(chessPiece, ChessPieceType.ROOK, ChessPieceType.QUEEN);
            if (resultCompare == -1) {
                break;
            } else if (resultCompare == 1) {
                return false;
            }
        }

        // Bishop & Queen
        // check left-up-side
        for (int x = cell.posX - 1, y = cell.posY + 1; !isOut(x, y); --x, ++y) {
            chessPiece = chessBoard.cells[x][y].getChessPiece();
            resultCompare = comparePiece(chessPiece, ChessPieceType.BISHOP, ChessPieceType.QUEEN);
            if (resultCompare == -1) {
                break;
            } else if (resultCompare == 1) {
                return false;
            }
        }
        // check left-down-side
        for (int x = cell.posX - 1, y = cell.posY - 1; !isOut(x, y); --x, --y) {
            chessPiece = chessBoard.cells[x][y].getChessPiece();
            resultCompare = comparePiece(chessPiece, ChessPieceType.BISHOP, ChessPieceType.QUEEN);
            if (resultCompare == -1) {
                break;
            } else if (resultCompare == 1) {
                return false;
            }
        }
        // check right-up-side
        for (int x = cell.posX + 1, y = cell.posY + 1; !isOut(x, y); ++x, ++y) //right-up
        {
            chessPiece = chessBoard.cells[x][y].getChessPiece();
            resultCompare = comparePiece(chessPiece, ChessPieceType.BISHOP, ChessPieceType.QUEEN);
            if (resultCompare == -1) {
                break;
            } else if (resultCompare == 1) {
                return false;
            }
        }
        // check right-down-side
        for (int x = cell.posX + 1, y = cell.posY - 1; !isOut(x, y); ++x, --y) //right-down
        {
            chessPiece = chessBoard.cells[x][y].getChessPiece();
            resultCompare = comparePiece(chessPiece, ChessPieceType.BISHOP, ChessPieceType.QUEEN);
            if (resultCompare == -1) {
                break;
            } else if (resultCompare == 1) {
                return false;
            }
        }


        // Knight
        int newX, newY;
        // check position 1
        newX = cell.posX - 2;
        newY = cell.posY + 1;
        if (!isOut(newX, newY)) {
            chessPiece = chessBoard.cells[newX][newY].getChessPiece();
            if (comparePiece(chessPiece, ChessPieceType.KNIGHT) == 1) {
                return false;
            }
        }
        // check position 2
        newX = cell.posX - 1;
        newY = cell.posY + 2;
        if (!isOut(newX, newY)) {
            chessPiece = chessBoard.cells[newX][newY].getChessPiece();
            if (comparePiece(chessPiece, ChessPieceType.KNIGHT) == 1) {
                return false;
            }
        }

        // check position 3
        newX = cell.posX + 1;
        newY = cell.posY + 2;
        if (!isOut(newX, newY)) {
            chessPiece = chessBoard.cells[newX][newY].getChessPiece();
            if (comparePiece(chessPiece, ChessPieceType.KNIGHT) == 1) {
                return false;
            }
        }

        // check position 4
        newX = cell.posX + 2;
        newY = cell.posY + 1;
        if (!isOut(newX, newY)) {
            chessPiece = chessBoard.cells[newX][newY].getChessPiece();
            if (comparePiece(chessPiece, ChessPieceType.KNIGHT) == 1) {
                return false;
            }
        }

        // check position 5
        newX = cell.posX + 2;
        newY = cell.posY - 1;
        if (!isOut(newX, newY)) {
            chessPiece = chessBoard.cells[newX][newY].getChessPiece();
            if (comparePiece(chessPiece, ChessPieceType.KNIGHT) == 1) {
                return false;
            }
        }

        // check position 6
        newX = cell.posX + 1;
        newY = cell.posY - 2;
        if (!isOut(newX, newY)) {
            chessPiece = chessBoard.cells[newX][newY].getChessPiece();
            if (comparePiece(chessPiece, ChessPieceType.KNIGHT) == 1) {
                return false;
            }
        }

        // check position 7
        newX = cell.posX - 1;
        newY = cell.posY - 2;
        if (!isOut(newX, newY)) {
            chessPiece = chessBoard.cells[newX][newY].getChessPiece();
            if (comparePiece(chessPiece, ChessPieceType.KNIGHT) == 1) {
                return false;
            }
        }

        // check position 8
        newX = cell.posX - 2;
        newY = cell.posY - 1;

        if (!isOut(newX, newY)) {
            chessPiece = chessBoard.cells[newX][newY].getChessPiece();
            if (comparePiece(chessPiece, ChessPieceType.KNIGHT) == 1) {
                return false;
            }
        }

        // King
        King otherKing;
        if (this == chessBoard.kingWhite) {
            otherKing = chessBoard.kingBlack;
        } else {
            otherKing = chessBoard.kingWhite;
        }

        if (cell.posX <= otherKing.chessCell.posX + 1 && cell.posX >= otherKing.chessCell.posX - 1 && cell.posY <= otherKing.chessCell.posY + 1 && cell.posY >= otherKing.chessCell.posY - 1)
            return false;

        // Pawn
        if (owner.isGoDown()) {
            newX = cell.posX - 1;
            newY = cell.posY + 1;
            if (!isOut(newX, newY)) {
                chessPiece = chessBoard.cells[newX][newY].getChessPiece();
                if (comparePiece(chessPiece, ChessPieceType.PAWN) == 1) {
                    return false;
                }
            }
            newX = cell.posX + 1;
            if (!isOut(newX, newY)) {
                chessPiece = chessBoard.cells[newX][newY].getChessPiece();
                return comparePiece(chessPiece, ChessPieceType.PAWN) != 1;
            }
        } else {
            newX = cell.posX - 1;
            newY = cell.posY - 1;
            if (!isOut(newX, newY)) {
                chessPiece = chessBoard.cells[newX][newY].getChessPiece();
                if (comparePiece(chessPiece, ChessPieceType.PAWN) == 1) {
                    return false;
                }
            }
            newX = cell.posX + 1;
            if (!isOut(newX, newY)) {
                chessPiece = chessBoard.cells[newX][newY].getChessPiece();
                return comparePiece(chessPiece, ChessPieceType.PAWN) != 1;
            }
        }

        return true;
    }

    /**
     * check King will be safe after move chess piece
     *
     * @param cellIsHere      current cell
     * @param cellWillBeThere cell will move
     * @return is safe
     */
    public boolean willBeSafeWhenMoveOtherPiece(ChessCell cellIsHere, ChessCell cellWillBeThere) {
        ChessPiece tmp = cellWillBeThere.getChessPiece();
        cellWillBeThere.setChessPiece(cellIsHere.getChessPiece());
        cellIsHere.setChessPiece(null);

        boolean result = isSafe(chessCell);

        cellIsHere.setChessPiece(cellWillBeThere.getChessPiece());
        cellWillBeThere.setChessPiece(tmp);

        return result;
    }
}

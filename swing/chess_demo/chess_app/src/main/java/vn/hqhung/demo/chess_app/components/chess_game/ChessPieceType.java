package vn.hqhung.demo.chess_app.components.chess_game;

/**
 * @System: demo
 * @Title: Chess Piece Type
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
public enum ChessPieceType {
    KING("K"), QUEEN("Q"), BISHOP("B"), KNIGHT("N"), ROOK("R"), PAWN("P");

    private final String symbol;

    public String getSymbol() {
        return symbol;
    }

    ChessPieceType(String symbol) {
        this.symbol = symbol;
    }
}

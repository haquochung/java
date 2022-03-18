package vn.hqhung.demo.chess_server.models;

/**
 * @System: demo
 * @Title: Chess Table
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/15
 */
public class ChessTable {
    public int tableId;
    public String whitePlayerName;
    public String blackPlayerName;
    public boolean state;
    public int time;

    public ChessTable(int tableId, String whitePlayerName, String blackPlayerName, boolean state, int time) {
        this.tableId = tableId;
        this.whitePlayerName = whitePlayerName;
        this.blackPlayerName = blackPlayerName;
        this.state = state;
        this.time = time;
    }
}

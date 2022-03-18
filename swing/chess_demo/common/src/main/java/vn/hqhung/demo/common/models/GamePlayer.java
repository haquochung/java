package vn.hqhung.demo.common.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @System: demo
 * @Title: Game Player
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
public class GamePlayer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private boolean isGoDown;
    @Getter
    @Setter
    private boolean canMove;
    @Getter
    private final boolean isWhite;


    public GamePlayer(String name, boolean isWhite) {
        this.name = name;
        this.isWhite = isWhite;
        this.isGoDown = false;
    }

    public GamePlayer(GamePlayer player) {
        this.name = player.name;
        this.isWhite = player.isWhite;
        this.isGoDown = player.isGoDown;
    }
}

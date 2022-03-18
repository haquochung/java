package vn.hqhung.demo.common.models;

import lombok.Getter;

import java.io.Serializable;

/**
 * @System: demo
 * @Title: Game Setting
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
public class GameSetting implements Serializable
{
    public boolean upSideDown;

    public GamePlayer whitePlayer;
    public GamePlayer blackPlayer;

    public GameSetting()
    {
        whitePlayer = new GamePlayer("",true);
        blackPlayer = new GamePlayer("",false);
    }
}

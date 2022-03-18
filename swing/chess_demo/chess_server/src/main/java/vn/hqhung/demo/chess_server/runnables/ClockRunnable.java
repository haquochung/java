package vn.hqhung.demo.chess_server.runnables;

import vn.hqhung.demo.chess_server.models.Clock;
import vn.hqhung.demo.chess_server.models.ChessGame;
import vn.hqhung.demo.common.runnables.RunnableImpl;

/**
 * @System: demo
 * @Title: Clock Runnable
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/17
 */
public class ClockRunnable implements RunnableImpl {

    private final Clock whiteClock;
    private final Clock blackClock;
    private Clock runningClock;
    private final ChessGame game;
    public boolean canRun;

    public ClockRunnable(ChessGame game) {
        this.game = game;

        whiteClock = new Clock();
        blackClock = new Clock();
    }

    public void timeOver() {
        String color;
        if (whiteClock.getLeftTime() == 0) {
            color = "White";
        } else {
            color = "Black";
        }
        game.timeOver(color);
    }

    public void setTime(int t) {
        whiteClock.init(t);
        blackClock.init(t);
    }

    public void switchClock() {
        if (runningClock == whiteClock) {
            runningClock = blackClock;
        } else {
            runningClock = whiteClock;
        }
    }

    public void run() {
        runningClock = whiteClock;
        while (canRun) {
            if (runningClock != null) {
                try {
                    if (runningClock.decrement()) {
                        game.sendTime(whiteClock.prepareString(), blackClock.prepareString());
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
                if (runningClock != null && runningClock.getLeftTime() == 0) {
                    timeOver();
                    break;
                }
            }
        }
    }
}

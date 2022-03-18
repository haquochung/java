package vn.hqhung.demo.chess_server.models;

import java.io.Serializable;

/**
 * @System: demo
 * @Title: Clock
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/17
 */
public final class Clock implements Serializable {
    private static final long serialVersionUID = 1L;

    private int timeLeft;

    public Clock() {
        init(timeLeft);
    }

    public void init(int time) {
        timeLeft = time;
    }

    public boolean decrement() {
        if (timeLeft > 0) {
            timeLeft = timeLeft - 1;
            return true;
        }
        return false;
    }

    public int getLeftTime() {
        return timeLeft;
    }

    public String prepareString() {
        String result;

        int timeMin = getLeftTime() / 60;
        int timeSec = getLeftTime() % 60;
        if (timeMin < 10) {
            result = "0" + timeMin;
        } else {
            result = Integer.toString(timeMin);
        }

        result += ":";

        if (timeSec < 10) {
            result = result + "0" + timeSec;
        } else {
            result = result + timeSec;
        }

        return result;
    }
}

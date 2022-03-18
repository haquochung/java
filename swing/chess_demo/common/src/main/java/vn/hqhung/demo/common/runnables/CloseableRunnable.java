package vn.hqhung.demo.common.runnables;

import lombok.Setter;

import java.util.List;

/**
 * @System: chess_demo
 * @Title: Socket Runnable
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/18
 */
public class CloseableRunnable implements Runnable {
    @Setter
    private List<RunnableImpl> runnableList;

    @Override
    public void run() {
        if (runnableList != null) {
            for (RunnableImpl runnable : runnableList) {
                try {
                    runnable.getCloseable().close();
                } catch (Exception ignored) {
                }
            }
            runnableList.clear();
        }
    }
}

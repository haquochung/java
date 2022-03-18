package vn.hqhung.demo.common.runnables;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @System: chess_demo
 * @Title: Demo ExecutorService
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/18
 */
public class DemoExecutorService {

    /**
     * thread available
     */
    private final int availableThreads;

    public DemoExecutorService(int availableThreads){
        this.availableThreads = availableThreads;
        executorService = Executors.newFixedThreadPool(availableThreads);
        runnableList.clear();
        closeableRunnable = new CloseableRunnable();
    }

    public boolean isShutdown() {
        return executorService.isShutdown();
    }

    public void newSession() {
        executorService = Executors.newFixedThreadPool(availableThreads);
    }

    // use to close socket when shutdown executor
    private final CloseableRunnable closeableRunnable;

    private ExecutorService executorService;

    private final List<RunnableImpl> runnableList = new ArrayList<>();

    public void execute(RunnableImpl runnable) {
        runnableList.add(runnable);
        executorService.execute(runnable);
    }

    public void stopExecutor() {
        if (!executorService.isShutdown()) {
            // close all closeable object
            closeableRunnable.setRunnableList(runnableList);
            Thread t = new Thread(closeableRunnable);
            try {
                t.setDaemon(true);
                t.start();
            } catch (Exception ignored) {
            }
            // shutdown
            executorService.shutdown();
        }
    }
}

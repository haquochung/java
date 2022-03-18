package vn.hqhung.demo.common.runnables;

import java.io.Closeable;

/**
 * @System: chess_demo
 * @Title: Runnable Custom
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/18
 */
public interface RunnableImpl extends Runnable {
    default Closeable getCloseable() {
        return null;
    }
}

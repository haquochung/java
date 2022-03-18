package vn.hqhung.demo.chess_server;

import lombok.extern.log4j.Log4j;
import vn.hqhung.demo.chess_server.components.ServerFrame;
import vn.hqhung.demo.common.runnables.DemoExecutorService;

import javax.swing.*;
import java.awt.*;

/**
 * @System: demo
 * @Title: Chess Server
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/15
 */
@Log4j
public class ChessServer {
    public static void main(String[] args) {
        /// catch exception runtime and write log
        Thread.setDefaultUncaughtExceptionHandler((thread, e) -> log.error(e.getMessage(), e));
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            log.error("Can't change theme", ex);
        }

        EventQueue.invokeLater(() -> new ServerFrame().setVisible(true));
    }

    /**
     * 1 thread use to listen connect request
     * 1 thread use to listen game access
     * 20 thread use to modify game play (maximum of chess game is 10 game & 10 time)
     */
    private static final int AVAILABLE_THREADS = 22;

    private static ChessServer instance;

    private DemoExecutorService executorService;

    public static DemoExecutorService getExecutorService() {
        if (instance == null) {
            instance = new ChessServer();
            instance.executorService = new DemoExecutorService(AVAILABLE_THREADS);
        }

        if (instance.executorService.isShutdown()) {
            instance.executorService.newSession();
        }

        return instance.executorService;
    }
}

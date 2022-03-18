package vn.hqhung.demo.chess_app;

import lombok.extern.log4j.Log4j;
import vn.hqhung.demo.chess_app.components.ChessAppFrame;
import vn.hqhung.demo.common.runnables.DemoExecutorService;

import javax.swing.*;
import java.awt.*;

@Log4j
public class ChessApp {
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

        EventQueue.invokeLater(() -> new ChessAppFrame().setVisible(true));
    }

    /**
     * 1 thread use to listen connect request
     * 1 thread use to listen game access
     */
    private static final int AVAILABLE_THREADS = 2;

    private static ChessApp instance;

    private DemoExecutorService executorService;

    public static DemoExecutorService getExecutorService() {
        if (instance == null) {
            instance = new ChessApp();
            instance.executorService = new DemoExecutorService(AVAILABLE_THREADS);
        }

        if (instance.executorService.isShutdown()) {
            instance.executorService.newSession();
        }

        return instance.executorService;
    }
}

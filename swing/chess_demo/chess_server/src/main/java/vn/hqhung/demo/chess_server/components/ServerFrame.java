package vn.hqhung.demo.chess_server.components;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.LogManager;
import vn.hqhung.demo.chess_server.ChessServer;
import vn.hqhung.demo.chess_server.helpers.ResourceHelper;
import vn.hqhung.demo.chess_server.runnables.ConnectRunnable;
import vn.hqhung.demo.chess_server.runnables.ServerRunnable;
import vn.hqhung.demo.common.router.logger.SendLogAppender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @System: demo
 * @Title: Server Layout
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/15
 */
@Log4j
public class ServerFrame extends JFrame {
    public ServerFrame() {
        initialize();
    }

    private void initialize() {
        setTitle("Chess Server - Demo");
        setIconImage(ResourceHelper.loadImage("icon.png"));
        setMinimumSize(new Dimension(450, 460));
        setPreferredSize(new Dimension(450, 460));
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);

        initComponents();
        initEvents();

        pack();

        SendLogAppender appender = new SendLogAppender(txtLog);
        LogManager.getRootLogger().addAppender(appender);
    }

    private JTextField txtIP;
    private JTextField txtPort;
    private JButton btnStart;
    private JTextArea txtLog;
    private JButton btnExit;

    private void initComponents() {
        // title
        JLabel lbTitle = new JLabel("Chess Server");
        lbTitle.setFont(new Font("Arial", Font.BOLD, 30));
        lbTitle.setBounds(120, 20, 200, 30);
        getContentPane().add(lbTitle);

        // text IP
        JLabel lbIP = new JLabel("Your IP: ");
        lbIP.setFont(new Font("Arial", Font.BOLD, 16));
        lbIP.setBounds(15, 80, 80, 25);
        getContentPane().add(lbIP);

        txtIP = new JTextField();
        txtIP.setHorizontalAlignment(JTextField.CENTER);
        txtIP.setBounds(82, 80, 130, 25);
        txtIP.setEditable(false);
        txtIP.setBackground(Color.GRAY);
        txtIP.setFocusCycleRoot(false);
        getContentPane().add(txtIP);

        // text Port
        JLabel lbPort = new JLabel("Port: ");
        lbPort.setFont(new Font("Arial", Font.BOLD, 16));
        lbPort.setBounds(15, 115, 80, 25);
        getContentPane().add(lbPort);

        txtPort = new JTextField();
        txtPort.setHorizontalAlignment(JTextField.CENTER);
        txtPort.setBounds(82, 115, 130, 25);
        txtPort.setText(String.valueOf(ServerRunnable.DEFAULT_PORT));
        getContentPane().add(txtPort);

        // button Start
        btnStart = new JButton("Start");
        btnStart.setBounds(220, 80, 130, 60);
        getContentPane().add(btnStart);

        // log
        JLabel lbProcess = new JLabel("Log:");
        lbProcess.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 14));
        lbProcess.setBounds(15, 150, 150, 25);
        getContentPane().add(lbProcess);

        txtLog = new JTextArea();
        txtLog.setLineWrap(true);
        txtLog.setWrapStyleWord(true);
        txtLog.setBounds(15, 175, 400, 205);
        txtLog.setEditable(false);

        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(txtLog);
        scroll.setBounds(15, 175, 415, 205);
        scroll.setAutoscrolls(true);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        getContentPane().add(scroll);

        btnExit = new JButton("Exit");
        btnExit.setBounds(175, 385, 100, 30);
        getContentPane().add(btnExit);
    }

    boolean isStart;

    private void initEvents() {
        ServerRunnable server = new ServerRunnable();
        txtIP.setText(server.getIp().getHostAddress());
        ConnectRunnable connect = new ConnectRunnable();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                super.windowActivated(e);
                txtPort.requestFocus();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                ChessServer.getExecutorService().stopExecutor();
            }
        });

        btnStart.addActionListener(e -> {
            if (!isStart) {
                int port;
                try {
                    port = Integer.parseInt(txtPort.getText());
                    if (port < 0 || port > 0xFFFF) {
                        JOptionPane.showMessageDialog(null, "Port is INVALID!");
                        return;
                    }
                } catch (IllegalArgumentException ex2) {
                    JOptionPane.showMessageDialog(null, "Port is INVALID!");
                    return;
                }
                txtPort.setEditable(false);
                txtPort.setBackground(Color.GRAY);
                btnExit.setEnabled(false);
                btnStart.setText("Stop");
                log.info("Chess Server is start!");
                server.update(port);
                connect.update(server.getIp(), port);
                ChessServer.getExecutorService().execute(server);
                ChessServer.getExecutorService().execute(connect);

                isStart = true;
            } else {
                ChessServer.getExecutorService().stopExecutor();
                btnExit.setEnabled(true);
                txtPort.setEditable(true);
                txtPort.setBackground(Color.WHITE);
                btnStart.setText("Start");
                log.info("Chess Server is stopped!");
                isStart = false;
            }
        });

        btnExit.addActionListener(e -> dispose());
    }
}

package vn.hqhung.demo.chess_app.components.chess_game;

import vn.hqhung.demo.chess_app.components.ChessAppFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @System: demo
 * @Title: Chat Panel
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
public class ChatPanel extends JPanel implements ActionListener {

    private JTextArea textOutput;
    private JTextField textInput;

    private final ChessAppFrame chessAppFrame;

    public ChatPanel(ChessAppFrame chessAppFrame) {
        super();
        this.chessAppFrame = chessAppFrame;
        initComponents();
    }

    private void initComponents() {
        textOutput = new JTextArea();
        textOutput.setEditable(false);
        textOutput.setLineWrap(true);
        textOutput.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(textOutput);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        textInput = new JTextField();
        textInput.addActionListener(this);
        JButton buttonSend = new JButton("Send");
        buttonSend.addActionListener(this);

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        setLayout(gbl);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weighty = 1.0;
        gbc.weightx = 0;
        gbl.setConstraints(scrollPane, gbc);
        add(scrollPane);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weighty = 0;
        gbc.weightx = 1.0;
        gbl.setConstraints(textInput, gbc);
        add(textInput);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weighty = 0;
        gbc.weightx = 0;
        gbl.setConstraints(buttonSend, gbc);
        add(buttonSend);
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

    private static final String MESSAGE_FORMAT = "[%s] %s\r\n";

    public void addMessage(String str) {
        textOutput.append(String.format(MESSAGE_FORMAT, DATE_FORMAT.format(new Date()), str));
        textOutput.setCaretPosition(textOutput.getDocument().getLength());
    }

    public void actionPerformed(ActionEvent arg0) {
        chessAppFrame.gameRunnable.sendMassage(textInput.getText());
        textInput.setText("");
    }
}

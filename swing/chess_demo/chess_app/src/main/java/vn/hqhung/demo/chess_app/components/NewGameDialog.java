package vn.hqhung.demo.chess_app.components;

import vn.hqhung.demo.chess_app.helpers.ResourceHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * @System: demo
 * @Title: New Game Layout
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */

class NewGameDialog extends JDialog implements ActionListener {

    public NewGameDialog(JFrame parent) {
        super(parent);
        setTitle("Setting Game");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(250, 280);
        setResizable(false);
        setLayout(null);
        initComponents();
        setLocationRelativeTo(parent); // center parent
        clicked = false;
    }

    private void initComponents() {

        JLabel labelConfig = new JLabel("Setting Game");
        labelConfig.setFont(new Font("Arial", Font.BOLD, 20));
        labelConfig.setBounds(10, 5, 200, 25);
        add(labelConfig);

        JLabel labelWhite = new JLabel();
        labelWhite.setIcon(new ImageIcon(ResourceHelper.loadImage("White_K.png")));
        labelWhite.setBounds(30, 30, 120, 120);
        add(labelWhite);

        JLabel labelBlack = new JLabel();
        labelBlack.setIcon(new ImageIcon(ResourceHelper.loadImage("Black_K.png")));
        labelBlack.setBounds(140, 30, 120, 120);
        add(labelBlack);

        rdWhite = new JRadioButton("White");
        rdWhite.setBounds(35, 140, 120, 25);
        rdWhite.setSelected(true);
        add(rdWhite);

        rdBlack = new JRadioButton("Black");
        rdBlack.setBounds(150, 140, 120, 25);
        add(rdBlack);

        JLabel labelTime = new JLabel("Time:");
        labelTime.setBounds(10, 175, 60, 25);
        add(labelTime);

        String[] times = {"5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60"};
        cbTime = new JComboBox<>(times);
        cbTime.setBounds(90, 175, 80, 25);
        add(cbTime);

        JLabel labelMinutes = new JLabel("minutes");
        labelMinutes.setBounds(172, 175, 50, 25);
        add(labelMinutes);

        JButton btnOK = new JButton("OK");
        btnOK.setBounds(90, 210, 60, 25);
        btnOK.addActionListener(this);
        add(btnOK);

        ButtonGroup btn = new ButtonGroup();
        btn.add(rdBlack);
        btn.add(rdWhite);
    }

    private JRadioButton rdBlack, rdWhite;
    private JComboBox<String> cbTime;

    public boolean isWhite;
    public int time;
    public boolean clicked;

    public void actionPerformed(ActionEvent e) {
        if (rdWhite.isSelected()) {
            isWhite = true;
        } else if (rdBlack.isSelected()) {
            isWhite = false;
        }

        time = Integer.parseInt((String) Objects.requireNonNull(cbTime.getSelectedItem()));
        clicked = true;
        dispose();

    }
}

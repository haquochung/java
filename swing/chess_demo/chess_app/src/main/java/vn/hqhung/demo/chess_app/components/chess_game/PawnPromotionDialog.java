package vn.hqhung.demo.chess_app.components.chess_game;

import vn.hqhung.demo.chess_app.helpers.ResourceHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @System: demo
 * @Title: Pawn Promotion Dialog
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
public class PawnPromotionDialog extends JDialog implements ActionListener {

    private JButton knightButton;
    private JButton bishopButton;
    private JButton rookButton;
    private JButton queenButton;

    public String result;
    private final String color;

    public PawnPromotionDialog(JFrame parent, String color) {
        super(parent);
        this.color = color;

        setTitle("Choose Chess Piece To Promotion");
        setMinimumSize(new Dimension(480, 120));
        setSize(new Dimension(480, 120));
        setMaximumSize(new Dimension(480, 120));
        setResizable(false);
        setLayout(new GridLayout(1, 4));

        initComponents();
    }

    private void initComponents() {
        knightButton = new JButton(new ImageIcon(ResourceHelper.loadImage(color + "_N" + ".png")));
        bishopButton = new JButton(new ImageIcon(ResourceHelper.loadImage(color + "_B" + ".png")));
        rookButton = new JButton(new ImageIcon(ResourceHelper.loadImage(color + "_R" + ".png")));
        queenButton = new JButton(new ImageIcon(ResourceHelper.loadImage(color + "_Q" + ".png")));

        result = "";

        knightButton.addActionListener(this);
        bishopButton.addActionListener(this);
        rookButton.addActionListener(this);
        queenButton.addActionListener(this);

        add(queenButton);
        add(rookButton);
        add(bishopButton);
        add(knightButton);
    }

    public void setColor(String color) {
        knightButton.setIcon(new ImageIcon(ResourceHelper.loadImage(color + "_N" + ".png")));
        bishopButton.setIcon(new ImageIcon(ResourceHelper.loadImage(color + "_B" + ".png")));
        rookButton.setIcon(new ImageIcon(ResourceHelper.loadImage(color + "_R" + ".png")));
        queenButton.setIcon(new ImageIcon(ResourceHelper.loadImage(color + "_Q" + ".png")));
    }

    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == queenButton) {
            result = "Queen";
        } else if (arg0.getSource() == rookButton) {
            result = "Rook";
        } else if (arg0.getSource() == bishopButton) {
            result = "Bishop";
        } else {
            result = "Knight";
        }

        dispose();
    }
}

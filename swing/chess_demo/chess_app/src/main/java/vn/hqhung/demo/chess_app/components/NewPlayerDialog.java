package vn.hqhung.demo.chess_app.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @System: demo
 * @Title: New Player Layout
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
public class NewPlayerDialog extends JDialog implements ActionListener {

    String name;

    JTextField txtName;

    public NewPlayerDialog(JFrame parent) {
        super(parent);
        setTitle(parent.getTitle());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setResizable(false);
        setLayout(null);

        initComponents();

        setLocationRelativeTo(parent); // center parent
    }

    private void initComponents() {
        JLabel lblName = new JLabel();
        lblName.setBounds(50, 10, 300, 50);
        lblName.setFont(new Font("Arial", Font.BOLD, 18));
        lblName.setText("Your name is:");
        add(lblName);

        txtName = new JTextField();
        txtName.setBounds(50, 60, 200, 30);
        txtName.addActionListener(this);
        add(txtName);

        JButton btnOK = new JButton("OK");
        btnOK.setBounds(100, 100, 100, 30);
        btnOK.addActionListener(this);
        add(btnOK);
    }

    public void actionPerformed(ActionEvent e) {
        if (txtName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required");
        } else if (txtName.getText().length() < 3 || txtName.getText().length() > 10) {
            JOptionPane.showMessageDialog(this, "The length of Name must between 3 and 10 character");
        } else {
            name = txtName.getText();
            dispose();
        }
    }

}

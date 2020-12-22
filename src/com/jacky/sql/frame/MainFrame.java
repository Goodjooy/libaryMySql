package com.jacky.sql.frame;

import com.jacky.sql.frame.actionListeners.SearchListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private JFrame frame;
    private JTextField label;
    private JButton button;
    /**
     *
     */
    private static final long serialVersionUID = -1493805524327085758L;

    public void createJFrameWindow() {
        JPanel panel=new JPanel();
        label = new JTextField("aaaaa");
        frame = new JFrame("hello");
        button = new JButton("close");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        //button.setBounds(300 ,0,100,100);

        frame.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        label.setBounds(-1,-1,300,100);
        frame.setBounds(600, 300, 400, 300);
        Container c=frame.getContentPane();

        panel.add(label);
        panel.add(button);
        c.add(panel);


        frame.setVisible(true);
    }

    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.createJFrameWindow();

    }
}

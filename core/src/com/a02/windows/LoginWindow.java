package com.a02.windows;

import javax.swing.*;
import java.awt.*;

public class LoginWindow {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setSize(640,360);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);


        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3,2));

        //Usuario
        JLabel userLabel = new JLabel("User");
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        panel.add(userText);

        //PW
        JLabel passwordLabel = new JLabel("Password");
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        panel.add(passwordText);

        //Buttons

        JButton loginButton = new JButton("Login");
        panel.add(loginButton);

        JButton registerButton = new JButton("Register");
        panel.add(registerButton);


        frame.add(panel);
        frame.setVisible(true);


    }
}

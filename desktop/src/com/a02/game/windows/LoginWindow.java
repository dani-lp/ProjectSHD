package com.a02.game.windows;

import com.a02.game.User;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;

import static com.a02.game.components.Utils.readSer;

public class LoginWindow extends JFrame{
    public LoginWindow() {
        //1.- Ajustes de ventana
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());   //UI Look&Feel más moderno y limpio
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Login");
        setSize(340,210);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit().createImage("core/assets/boredlion.png"));
        this.setLocationRelativeTo(null);
        setLayout(new GridLayout(5,1));

        //2.- Creación de elementos
        JPanel userLabelPanel = new JPanel(); //Panel para label de usuario
        JPanel userTFPanel = new JPanel(); //Panel para TextField de usuario
        JPanel pwdLabelPanel = new JPanel();
        JPanel pwdTFPanel = new JPanel(); //Panel para TextField de password
        JPanel buttonsPanel = new JPanel(); //Panel para los botones

        userLabelPanel.setLayout(new FlowLayout());
        userTFPanel.setLayout(new BorderLayout());
        userTFPanel.setBorder(BorderFactory.createEmptyBorder(1,20,5,20));
        pwdLabelPanel.setLayout(new FlowLayout());
        pwdTFPanel.setLayout(new BorderLayout());
        pwdTFPanel.setBorder(BorderFactory.createEmptyBorder(0,20,1,20));
        buttonsPanel.setLayout(new FlowLayout());

        JLabel userLabel = new JLabel("User:");
        JLabel pwdLabel = new JLabel("Password:");
        final JTextField userJTF = new JTextField();
        final JPasswordField pwdJTF = new JPasswordField();
        JButton cancelButton = new JButton("Cancel");
        final JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");


        //3.- Interacción
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.exit(0);
            }
        });

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (userJTF.getText().equals("")) userJTF.putClientProperty("JComponent.outline", "error");
                else userJTF.putClientProperty("JComponent.outline", "");
                if (pwdJTF.getPassword().length == 0) pwdJTF.putClientProperty("JComponent.outline", "error");
                else pwdJTF.putClientProperty("JComponent.outline", "");
                try {
                    if (checkSystem(userJTF.getText(), String.valueOf(pwdJTF.getPassword()))){
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                HashMap<String, User> users = new HashMap<>();
                                try {
                                    users = readSer("users.ser");
                                } catch (IOException | ClassNotFoundException ioException) {
                                    ioException.printStackTrace();
                                }
                                if (userJTF.getText().equals("admin")) {
                                    new SettingsWindow(new User("admin"));
                                }
                                else new SettingsWindow(users.get(userJTF.getText()));
                            }
                        });
                        dispose();
                    }
                    else pwdJTF.putClientProperty("JComponent.outline", "");
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }

            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new RegisterWindow();
                    }
                });
            }
        });

        KeyAdapter enterKA = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        };

        userJTF.addKeyListener(enterKA);
        pwdJTF.addKeyListener(enterKA);

        //4.- Introducción de componentes
        userLabelPanel.add(userLabel);
        userTFPanel.add(userJTF);
        pwdLabelPanel.add(pwdLabel);
        pwdTFPanel.add(pwdJTF);
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(loginButton);
        buttonsPanel.add(registerButton);

        add(userLabelPanel);
        add(userTFPanel);
        add(pwdLabelPanel);
        add(pwdTFPanel);
        add(buttonsPanel);
    }

    public boolean checkSystem(String user, String pwd) throws FileNotFoundException {
        HashMap<String, User> users = new HashMap<>();
        try {
            users = readSer("users.ser");
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }

        if (users.containsKey(user)) return users.get(user).getPassword().equals(pwd);
        else if (user.equals("admin") && pwd.equals("admin")) return true;
        else {
            if (!user.equals("")) JOptionPane.showMessageDialog(null,
                    "User '" + user + "' does not exist.",
                    "Invalid user", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginWindow();
            }
        });
    }
}

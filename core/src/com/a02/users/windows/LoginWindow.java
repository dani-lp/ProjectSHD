package com.a02.users.windows;

import com.a02.users.User;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.HashMap;

import static com.a02.game.Utils.readSer;

public class LoginWindow extends JFrame{
    private boolean isDark = false;
    public LoginWindow() {
        //1.- Ajustes de ventana
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());   //UI Look&Feel más moderno y limpio
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Login");
        setSize(340,230);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        URL iconURL = getClass().getResource("/boredlion.png");
        setIconImage(new ImageIcon(iconURL).getImage());
        this.setLocationRelativeTo(null);
        setLayout(new GridLayout(5,1));

        //2.- Creación de elementos
        addJMenu();

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
                                    users = readSer("data/users.ser");
                                } catch (IOException | ClassNotFoundException ioException) {
                                    ioException.printStackTrace();
                                }
                                if (userJTF.getText().equals("admin")) {
                                    new UsersWindow(isDark);
                                }
                                else new SettingsWindow(users.get(userJTF.getText()), isDark);
                            }
                        });
                        if (!userJTF.getText().equals("admin")) dispose();
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
                        new RegisterWindow(isDark);
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

        setVisible(true);
    }

    public boolean checkSystem(String user, String pwd) throws FileNotFoundException {
        HashMap<String, User> users = new HashMap<>();
        try {
            users = readSer("data/users.ser");
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

    private void refreshUI() {
        try {
            if (isDark) UIManager.setLookAndFeel(new FlatDarkLaf());   //UI Look&Feel más moderno y limpio
            else UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void addJMenu() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu fileJMenu = new JMenu("Settings");
        menuBar.add(fileJMenu);

        final JCheckBoxMenuItem themeJCBMItem = new JCheckBoxMenuItem("Dark theme");
        fileJMenu.add(themeJCBMItem);

        JMenuItem exitItem = new JMenuItem("Exit");
        fileJMenu.add(exitItem);

        themeJCBMItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isDark = themeJCBMItem.getState();
                refreshUI();
            }
        });

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}

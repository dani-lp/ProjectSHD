package com.a02.users.windows;

import com.a02.users.User;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;

import static com.a02.game.Utils.*;


public class RegisterWindow extends JFrame {
    public RegisterWindow() {
        //1.- Ajustes de ventana
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());   //UI Look&Feel más moderno y limpio
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Register");
        setSize(340,410);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit().createImage("core/assets/boredlion.png"));
        this.setLocationRelativeTo(null);
        setLayout(new GridLayout(11,1));

        //2.- Creación de componentes
        JPanel userLabelPanel = new JPanel();
        JPanel userTFPanel = new JPanel();
        JPanel pwdLabelPanel = new JPanel();
        JPanel pwdJTFPanel = new JPanel();
        JPanel nameLabelPanel = new JPanel();
        JPanel nameJTFPanel = new JPanel();
        JPanel ageLabelPanel = new JPanel();
        JPanel ageSpinnerPanel = new JPanel();
        JPanel mailLabelPanel = new JPanel();
        JPanel mailJTFPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();

        userLabelPanel.setLayout(new FlowLayout());
        userTFPanel.setLayout(new BorderLayout());
        userTFPanel.setBorder(BorderFactory.createEmptyBorder(1,20,1,20));
        pwdLabelPanel.setLayout(new FlowLayout());
        pwdJTFPanel.setLayout(new BorderLayout());
        pwdJTFPanel.setBorder(BorderFactory.createEmptyBorder(1,20,1,20));
        nameLabelPanel.setLayout(new FlowLayout());
        nameJTFPanel.setLayout(new BorderLayout());
        nameJTFPanel.setBorder(BorderFactory.createEmptyBorder(1,20,1,20));
        ageLabelPanel.setLayout(new FlowLayout());
        ageSpinnerPanel.setLayout(new BorderLayout());
        ageSpinnerPanel.setBorder(BorderFactory.createEmptyBorder(1,100,1,100));
        mailLabelPanel.setLayout(new FlowLayout());
        mailJTFPanel.setLayout(new BorderLayout());
        mailJTFPanel.setBorder(BorderFactory.createEmptyBorder(1,20,1,20));
        buttonsPanel.setLayout(new FlowLayout());

        JLabel userLabel = new JLabel("Username:");
        JLabel pwdLabel = new JLabel("Password:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel ageLabel = new JLabel("Age:");
        JLabel mailLabel = new JLabel("Email:");
        final JTextField userJTF = new JTextField();
        final JPasswordField pwdJTF = new JPasswordField();
        final JTextField nameJTF = new JTextField();
        SpinnerModel sm = new SpinnerNumberModel(0, 0, 130, 1);
        final JSpinner ageSpinner = new JSpinner(sm);
        final JTextField mailJTF = new JTextField();
        JButton cancelButton = new JButton("Cancel");
        JButton acceptButton = new JButton("Accept");

        //3.- Interacción
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        acceptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!userJTF.getText().equals("") && pwdJTF.getPassword().length != 0) {
                            if (userJTF.getText().equals("admin")) {
                                JOptionPane.showMessageDialog(null,
                                        "'admin' username is not available.",
                                        "Invalid username", JOptionPane.ERROR_MESSAGE);
                            }
                            else if (validateMail(mailJTF.getText())) {
                                User tempUser = new User();
                                tempUser.setAge((Integer) ageSpinner.getValue());
                                tempUser.setMail(mailJTF.getText());
                                tempUser.setUsername(userJTF.getText());
                                tempUser.setPassword(String.valueOf(pwdJTF.getPassword()));
                                tempUser.setName(nameJTF.getText());

                                if (JOptionPane.showConfirmDialog(null, "Register new user?\n\t" +
                                                "       Username: " + tempUser.getUsername() + "\n" +
                                                "       Name: " + tempUser.getName() + "\n" +
                                                "       Age: " + tempUser.getAge() + "\n" +
                                                "       Email: " + tempUser.getMail() + "\n",
                                        "Register new user", JOptionPane.YES_NO_OPTION) == 0) {
                                    if (addUser("users.ser", tempUser)) dispose();
                                }
                            }
                            else {
                                JOptionPane.showMessageDialog(null,
                                        "'Email' field is invalid. Enter valid email address.",
                                        "Invalid field", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "'Username' and 'Password' fields are required.",
                                    "Missing fields", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                t.start();
            }
        });


        //4.- Introducción de componentes
        userLabelPanel.add(userLabel);
        userTFPanel.add(userJTF);
        pwdLabelPanel.add(pwdLabel);
        pwdJTFPanel.add(pwdJTF);
        nameLabelPanel.add(nameLabel);
        nameJTFPanel.add(nameJTF);
        ageLabelPanel.add(ageLabel);
        ageSpinnerPanel.add(ageSpinner);
        mailLabelPanel.add(mailLabel);
        mailJTFPanel.add(mailJTF);
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(acceptButton);

        add(userLabelPanel);
        add(userTFPanel);
        add(pwdLabelPanel);
        add(pwdJTFPanel);
        add(nameLabelPanel);
        add(nameJTFPanel);
        add(ageLabelPanel);
        add(ageSpinnerPanel);
        add(mailLabelPanel);
        add(mailJTFPanel);
        add(buttonsPanel);
    }

    /**
     * Añade un usuario a un HashMap serializado.
     * @param path Ruta del archivo
     * @param user Usuario a introducir
     */
    private static boolean addUser(String path, User user) {
        HashMap<String, User> map = null;
        try {
            map = readSer(path);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (map != null) {
            if (map.containsKey(user.getUsername())) {
                JOptionPane.showMessageDialog(null,
                        "Username already exists.", "User exists", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            else map.put(user.getUsername(), user);
        }

        try {
            writeSer(path, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}

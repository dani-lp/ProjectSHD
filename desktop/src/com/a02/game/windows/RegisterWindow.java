package com.a02.game.windows;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        JTextField userJTF = new JTextField();
        JPasswordField pwdJTF = new JPasswordField();
        JTextField nameJTF = new JTextField();
        SpinnerModel sm = new SpinnerNumberModel(0, 0, 130, 1);
        JSpinner ageSpinner = new JSpinner(sm);
        JTextField mailJTF = new JTextField();
        JButton cancelButton = new JButton("Cancel"); //TODO: confirmar con un JOptionPane
        JButton acceptButton = new JButton("Accept");

        //3.- Interacción
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        acceptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

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
}

package com.a02.game.windows;

import com.a02.game.User;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

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
                if (!userJTF.getText().equals("") && pwdJTF.getPassword().length != 0) {
                    //read
                    HashMap<String, User> tempMap = new HashMap<>();
                    User tempUser;
                    try {
                        if (readSer("users.ser") != null) tempMap = readSer("users.ser");
                    } catch (IOException | ClassNotFoundException ioException) {
                        ioException.printStackTrace();
                    }

                    //rewrite
                    tempUser = new User();
                    tempUser.setAge((Integer) ageSpinner.getValue());
                    tempUser.setMail(mailJTF.getText());
                    tempUser.setUsername(userJTF.getText());
                    tempUser.setPassword(String.valueOf(pwdJTF.getPassword()));
                    tempUser.setName(nameJTF.getText());

                    try {
                        tempMap.put(tempUser.getUsername(), tempUser);
                    } catch (Exception nullPointerException) {
                        nullPointerException.printStackTrace();
                    }

                    //write
                    try {
                        writeSer("users.ser", tempMap);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                else {
                    //TODO poner en rojo
                }

                HashMap<String, User> m = new HashMap<>();
                try {
                    m = readSer("users.ser");
                } catch (IOException | ClassNotFoundException ioException) {
                    ioException.printStackTrace();
                }
                if (m != null) printMap(m);
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
    private HashMap<String, User> readSer(String path) throws IOException, ClassNotFoundException {
        FileInputStream fs = new FileInputStream(path);
        ObjectInputStream os = new ObjectInputStream(fs);

        return (HashMap<String, User>) os.readObject();
    }

    private void writeSer(String path, HashMap<String,User> map) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(map);
    }

    public static void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove();
        }
    }
}

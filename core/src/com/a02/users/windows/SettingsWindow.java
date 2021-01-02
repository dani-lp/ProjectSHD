package com.a02.users.windows;

import com.a02.game.MainGame;
import com.a02.game.Settings;
import com.a02.users.User;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SettingsWindow extends JFrame {
    public SettingsWindow(final User user){
        //1.- Ajustes de ventana
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());   //UI Look&Feel más moderno y limpio
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Settings");
        setSize(310,225);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(true);
        setIconImage(Toolkit.getDefaultToolkit().createImage("core/assets/boredlion.png"));
        this.setLocationRelativeTo(null);

        setLayout(new GridLayout(4,1));

        //2.- Creación de elementos
        JPanel modePanel = new JPanel();
        JPanel diffPanel = new JPanel();
        JPanel checkPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();

        modePanel.setLayout(new FlowLayout());
        modePanel.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        diffPanel.setLayout(new FlowLayout());
        diffPanel.setBorder(BorderFactory.createEmptyBorder(2,0,10,0));
        checkPanel.setLayout(new FlowLayout());
        checkPanel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        buttonsPanel.setLayout(new FlowLayout());

        JLabel modeLabel = new JLabel("Gamemode:");
        String[] cbEntries = {"Rondas", "Infinito", "Pacifico"};
        final JComboBox<String> modeCB = new JComboBox<>(cbEntries);
        JLabel diffLabel = new JLabel("Difficulty:");
        final JSlider diffSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 0);
        diffSlider.setPaintTrack(true);
        diffSlider.setPaintTicks(true);
        diffSlider.setPaintLabels(true);
        diffSlider.setMajorTickSpacing(2);
        diffSlider.setMinorTickSpacing(1);
        JLabel musicLabel = new JLabel("Music:");
        JLabel soundLabel = new JLabel("     Sound:");
        final JCheckBox musicCheck = new JCheckBox();
        final JCheckBox soundCheck = new JCheckBox();
        JLabel tutorialLabel = new JLabel("     Tutorial:");
        final JCheckBox tutorialCheck = new JCheckBox();
        JButton cancelButton = new JButton("Cancel");
        final JButton confirmButton = new JButton("Confirm");
        confirmButton.setEnabled(!user.getUsername().equals("admin"));
        JButton adminButton = new JButton("Users");
        adminButton.setToolTipText("Available to administrators");
        //Visible si el usuario es administrador del sistema
        adminButton.setEnabled(user.isAdmin() || user.getUsername().equals("admin"));

        //3.- Interacción
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.exit(0);
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Settings.s.setGamemode(String.valueOf(modeCB.getSelectedItem()));
                Settings.s.setDiff(getDmgMult(diffSlider.getValue()));
                Settings.s.setMusicCheck(musicCheck.isSelected());
                Settings.s.setSoundCheck(soundCheck.isSelected());
                Settings.s.setTutorialCheck(tutorialCheck.isSelected());
                Settings.s.setUsername(user.getUsername());
                MainGame.begin = true;
                dispose();
            }
        });

        adminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new UsersWindow();
            }
        });

        musicLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                musicCheck.setSelected(!musicCheck.isSelected());
            }
        });

        soundLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                soundCheck.setSelected(!soundCheck.isSelected());
            }
        });

        tutorialLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                tutorialCheck.setSelected(!tutorialCheck.isSelected());
            }
        });

        KeyAdapter enterKA = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    confirmButton.doClick();
                }
            }
        };

        //Tecla enter presiona los botones
        modeLabel.addKeyListener(enterKA);
        modeCB.addKeyListener(enterKA);
        diffLabel.addKeyListener(enterKA);
        diffSlider.addKeyListener(enterKA);
        musicLabel.addKeyListener(enterKA);
        soundLabel.addKeyListener(enterKA);
        musicCheck.addKeyListener(enterKA);
        soundCheck.addKeyListener(enterKA);
        tutorialLabel.addKeyListener(enterKA);
        tutorialCheck.addKeyListener(enterKA);
        cancelButton.addKeyListener(enterKA);
        confirmButton.addKeyListener(enterKA);

        //4.- Introducción de elementos
        diffPanel.add(diffLabel);
        diffPanel.add(diffSlider);
        checkPanel.add(musicLabel);
        checkPanel.add(musicCheck);
        checkPanel.add(soundLabel);
        checkPanel.add(soundCheck);
        checkPanel.add(tutorialLabel);
        checkPanel.add(tutorialCheck);
        modePanel.add(modeLabel);
        modePanel.add(modeCB);
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(confirmButton);
        buttonsPanel.add(adminButton);

        add(modePanel);
        add(diffPanel);
        add(checkPanel);
        add(buttonsPanel);
    }

    /**
     * Calcula el valor multiplicador de daño relativo, con valores entre 0.5 y 1.5
     * @param ogValue Valor original (0-10)
     * @return Valor transformado
     */
    public static double getDmgMult(double ogValue) {
        return round((ogValue + 5) * 0.1,1);
    }

    /**
     * Redondea con n decimales.
     * @param value Valor a redondear
     * @param precision Número de decimales
     * @return Número redondeado
     */
    private static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}

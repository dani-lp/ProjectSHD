package com.a02.game.windows;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsWindow extends JFrame {
    public SettingsWindow(){
        //1.- Ajustes de ventana
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());   //UI Look&Feel más moderno y limpio
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Settings");
        setSize(310,225);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(true);
        setIconImage(Toolkit.getDefaultToolkit().createImage("core/assets/boredlion.png"));
        this.setLocationRelativeTo(null);

        setLayout(new GridLayout(4,1));

        /*
        Dificultad (JSlider)
        Con/sin música (Checkbox)
        Con/sin sonido
        Modo inicial ? (JComboBox)
        Tutorial? (Checkbox, para tutorial)
         */

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
        JComboBox<String> modeCB = new JComboBox<>(cbEntries);
        JLabel diffLabel = new JLabel("Difficulty:");
        JSlider diffSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 0);
        diffSlider.setPaintTrack(true);
        diffSlider.setPaintTicks(true);
        diffSlider.setPaintLabels(true);
        diffSlider.setMajorTickSpacing(2);
        diffSlider.setMinorTickSpacing(1);
        JLabel musicLabel = new JLabel("Music:");
        JLabel soundLabel = new JLabel("     Sound:");
        JCheckBox musicCheck = new JCheckBox();
        JCheckBox soundCheck = new JCheckBox();
        JLabel tutorialLabel = new JLabel("     Tutorial:");
        JCheckBox tutorialCB = new JCheckBox();
        JButton cancelButton = new JButton("Cancel");
        JButton confirmButton = new JButton("Confirm");

        //3.- Interacción
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        //4.- Introducción de elementos
        diffPanel.add(diffLabel);
        diffPanel.add(diffSlider);
        checkPanel.add(musicLabel);
        checkPanel.add(musicCheck);
        checkPanel.add(soundLabel);
        checkPanel.add(soundCheck);
        checkPanel.add(tutorialLabel);
        checkPanel.add(tutorialCB);
        modePanel.add(modeLabel);
        modePanel.add(modeCB);
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(confirmButton);

        add(modePanel);
        add(diffPanel);
        add(checkPanel);
        add(buttonsPanel);
    }
}

package com.example;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class zipprogram {
    private JFrame frame;
    private JTextField fileField;
    private File selectedFile;

    public static void main(String[] args) {
        zipprogram program = new zipprogram();
        program.createUI();
    }

    public void createUI() {
        frame = new JFrame("Becsomagolás Program");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 450);
        frame.setLayout(new BorderLayout());

        // Panel a magyar rendőrségi logóhoz
        JPanel logoPanel = new JPanel();
        JLabel logoLabel = new JLabel(new ImageIcon(getClass().getResource("/rendorseg_logo.png")));
        logoPanel.add(logoLabel);
        frame.add(logoPanel, BorderLayout.NORTH);

        // Középső panel a fájl kiválasztásához és a becsomagoláshoz
        JPanel centerPanel = new JPanel(new GridLayout(2, 2));

        JLabel fileLabel = new JLabel("Fájl kiválasztása:");
        centerPanel.add(fileLabel);

        fileField = new JTextField();
        fileField.setEditable(false);
        centerPanel.add(fileField);

        // Gomb resz
        JButton browseButton = new JButton("Tallózás");
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    fileField.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        centerPanel.add(browseButton);

        JButton zipButton = new JButton("Becsomagolás");
        zipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedFile != null) {
                    try {
                        zipFileWithPassword(selectedFile, "123456");
                        JOptionPane.showMessageDialog(frame, "Becsomagolás sikeres! Jelszó: 123456");
                    } catch (ZipException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Hiba a becsomagolás során!", "Hiba", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Válassz ki egy fájlt először!");
                }
            }
        });
        centerPanel.add(zipButton);

        frame.add(centerPanel, BorderLayout.CENTER);

        // Exit gomb a program lezárásához
        JPanel exitPanel = new JPanel();
        JButton exitButton = new JButton("Kilépés");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        exitPanel.add(exitButton);
        frame.add(exitPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // Fájl becsomagolása jelszóval ZIP-be a Zip4j segítségével
    public void zipFileWithPassword(File file, String password) throws ZipException {
        ZipFile zipFile = new ZipFile(file.getParent() + "/" + file.getName() + ".zip", password.toCharArray());

        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setCompressionLevel(CompressionLevel.NORMAL);
        zipParameters.setEncryptFiles(true);
        zipParameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);

        zipFile.addFile(file, zipParameters);
    }
}

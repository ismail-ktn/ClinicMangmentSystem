package javaapplication5;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginScreen extends JFrame {

    // ===== GOLD + BLACK THEME =====
    private static final Color BLACK_BG   = new Color(18, 18, 18);
    private static final Color DARK_BLACK = new Color(25, 25, 25);
    private static final Color GOLD       = new Color(212, 175, 55);
    private static final Color SOFT_GOLD  = new Color(230, 190, 80);
    private static final Color WHITE_TEXT = new Color(235, 235, 235);

    public LoginScreen() {

        // ===== LOOK & FEEL =====
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        setTitle("Login - Clinic System");
        setSize(400, 330);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(BLACK_BG);
        main.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 12, 12, 12);

        // ===== TITLE =====
        JLabel title = new JLabel("LOGIN", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(GOLD);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        main.add(title, gbc);

        // ===== EMAIL =====
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(WHITE_TEXT);
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        main.add(emailLabel, gbc);

        gbc.gridx = 1;
        JTextField emailField = new JTextField(15);
        styleField(emailField);
        main.add(emailField, gbc);

        // ===== PASSWORD =====
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(WHITE_TEXT);
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        main.add(passLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passField = new JPasswordField(15);
        styleField(passField);
        main.add(passField, gbc);

        // ===== BUTTONS =====
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setBackground(BLACK_BG);

        JButton loginBtn  = goldFilledButton("Login");
        JButton backBtn   = goldOutlineButton("Back");

        loginBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ClinicData data = ClinicData.getInstance();
            if (data.authenticate(email, password)) {
                User user = data.getUser(email);
                dispose();

                switch (user.getRole()) {
                    case "DOCTOR":
                        new DoctorDashboard(user);
                        break;
                    case "RECEPTION":
                        new ReceptionDashboard(user);
                        break;
                    case "PHARMACY":
                        new PharmacyDashboard(user);
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> {
            dispose();
            new WelcomeScreen();
        });

        btnPanel.add(loginBtn);
        btnPanel.add(backBtn);
        main.add(btnPanel, gbc);

        add(main);
        setVisible(true);
    }

    // ===== STYLE INPUT FIELDS =====
    private void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(DARK_BLACK);
        field.setForeground(WHITE_TEXT);
        field.setCaretColor(GOLD);
        field.setBorder(BorderFactory.createLineBorder(GOLD, 1));
    }

    // ===== GOLD OUTLINE BUTTON =====
    private JButton goldOutlineButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setForeground(GOLD);
        b.setBackground(DARK_BLACK);

        b.setFocusPainted(false);
        b.setRolloverEnabled(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(true);
        b.setOpaque(true);

        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GOLD, 1),
                new EmptyBorder(10, 25, 10, 25)
        ));

        return b;
    }

    // ===== GOLD FILLED BUTTON =====
    private JButton goldFilledButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setForeground(Color.BLACK);
        b.setBackground(GOLD);

        b.setFocusPainted(false);
        b.setRolloverEnabled(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(true);
        b.setOpaque(true);
        b.setBorder(new EmptyBorder(10, 25, 10, 25));

        return b;
    }
}

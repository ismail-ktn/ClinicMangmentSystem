package javaapplication5;

import javax.swing.*;
import java.awt.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.border.EmptyBorder;

public class WelcomeScreen extends JFrame {

    // ===== GOLD + BLACK THEME =====
    private static final Color BLACK_BG   = new Color(18, 18, 18);
    private static final Color DARK_BLACK = new Color(25, 25, 25);
    private static final Color GOLD       = new Color(212, 175, 55);
    private static final Color SOFT_GOLD  = new Color(230, 190, 80);
    private static final Color WHITE_TEXT = new Color(235, 235, 235);

    public WelcomeScreen() {

        // ===== LOOK & FEEL =====
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        setTitle("Clinic Management System");
        setSize(650, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BLACK_BG);

        main.add(headerPanel(), BorderLayout.NORTH);
        main.add(centerPanel(), BorderLayout.CENTER);
        main.add(footerPanel(), BorderLayout.SOUTH);

        add(main);
        setVisible(true);
    }

    // ================= HEADER =================
    private JPanel headerPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(DARK_BLACK);
        header.setPreferredSize(new Dimension(getWidth(), 110));
        header.setBorder(new EmptyBorder(25, 20, 25, 20));

        JLabel title = new JLabel("CLINIC MANAGEMENT SYSTEM", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(GOLD);

        header.add(title, BorderLayout.CENTER);
        return header;
    }

    // ================= CENTER =================
    private JPanel centerPanel() {
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(BLACK_BG);
        center.setBorder(new EmptyBorder(20, 60, 20, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel welcome = new JLabel("Welcome to Our Clinic", JLabel.CENTER);
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        welcome.setForeground(WHITE_TEXT);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        center.add(welcome, gbc);

        JButton loginBtn  = goldOutlineButton("Login");
        JButton signupBtn = goldOutlineButton("Sign Up");
        JButton exitBtn   = goldFilledButton("Exit");

        loginBtn.addActionListener(e -> {
            dispose();
            new LoginScreen();
        });

        signupBtn.addActionListener(e -> {
            dispose();
            new SignupScreen();
        });

        exitBtn.addActionListener(e -> System.exit(0));

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        center.add(loginBtn, gbc);

        gbc.gridx = 1;
        center.add(signupBtn, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        center.add(exitBtn, gbc);

        return center;
    }

    // ================= FOOTER =================
    private JPanel footerPanel() {
        JPanel footer = new JPanel();
        footer.setBackground(DARK_BLACK);
        footer.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel copy = new JLabel("© 2025 Clinic Management System");
        copy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        copy.setForeground(SOFT_GOLD);

        footer.add(copy);
        return footer;
    }

    // ================= BUTTONS =================

    // Gold border – NO hover
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
                new EmptyBorder(12, 35, 12, 35)
        ));

        b.setPreferredSize(new Dimension(180, 42));
        return b;
    }

    // Solid gold – NO hover
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

        b.setBorder(new EmptyBorder(12, 35, 12, 35));
        b.setPreferredSize(new Dimension(180, 42));
        return b;
    }
}


package javaapplication5;

import javaapplication5.ui.WelcomeScreen;
import javax.swing.*;
import java.awt.*;

public class MainClass {
    public static void main(String[] args) {
        // Show splash screen
        showSplashScreen();
        
        // Start application
        SwingUtilities.invokeLater(() -> {
            new WelcomeScreen();
        });
    }
    
    private static void showSplashScreen() {
        JWindow splash = new JWindow();
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(18, 18, 18));
        
        // Add logo/title
        JLabel title = new JLabel("🏥 Clinic Management System", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(212, 175, 55));
        title.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
        
        JLabel loading = new JLabel("Loading...", JLabel.CENTER);
        loading.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loading.setForeground(Color.WHITE);
        loading.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        content.add(title, BorderLayout.CENTER);
        content.add(loading, BorderLayout.SOUTH);
        
        splash.setContentPane(content);
        splash.setSize(400, 200);
        splash.setLocationRelativeTo(null);
        splash.setVisible(true);
        
        // Simulate loading
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        splash.dispose();
    }
}
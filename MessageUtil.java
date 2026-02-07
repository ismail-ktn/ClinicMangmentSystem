package javaapplication5;

import javax.swing.*;
import java.awt.*;

public class MessageUtil {
    
    public static void showError(JFrame parent, String message) {
        showStyledMessage(parent, "Error", message, "ERROR");
    }
    
    public static void showSuccess(JFrame parent, String message) {
        showStyledMessage(parent, "Success", message, "SUCCESS");
    }
    
    public static void showInfo(JFrame parent, String message) {
        showStyledMessage(parent, "Information", message, "INFO");
    }
    
    public static void showWarning(JFrame parent, String message) {
        showStyledMessage(parent, "Warning", message, "WARNING");
    }
    
    private static void showStyledMessage(JFrame parent, String title, String message, String type) {
        Color bgColor, fgColor, borderColor;
        String icon;
        
        switch(type.toUpperCase()) {
            case "SUCCESS":
                bgColor = new Color(220, 255, 220);
                fgColor = new Color(0, 100, 0);
                borderColor = new Color(0, 150, 0);
                icon = "✓";
                break;
            case "ERROR":
                bgColor = new Color(255, 220, 220);
                fgColor = new Color(150, 0, 0);
                borderColor = new Color(200, 0, 0);
                icon = "✗";
                break;
            case "WARNING":
                bgColor = new Color(255, 255, 200);
                fgColor = new Color(150, 100, 0);
                borderColor = new Color(255, 200, 0);
                icon = "⚠";
                break;
            default: // INFO
                bgColor = new Color(220, 240, 255);
                fgColor = new Color(0, 80, 150);
                borderColor = new Color(70, 130, 180);
                icon = "ℹ";
        }
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 48));
        iconLabel.setForeground(fgColor);
        
        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" + message + "</div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(fgColor);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton okButton = new JButton("OK");
        okButton.setBackground(fgColor);
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        okButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(okButton);
            if (window != null) {
                window.dispose();
            }
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(bgColor);
        buttonPanel.add(okButton);
        
        panel.add(iconLabel, BorderLayout.NORTH);
        panel.add(messageLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        JOptionPane.showMessageDialog(parent, panel, title, 
            JOptionPane.PLAIN_MESSAGE, new ImageIcon());
    }
    
    public static boolean confirmDialog(JFrame parent, String message) {
        // Create custom panel
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel iconLabel = new JLabel("?", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 36));
        iconLabel.setForeground(new Color(0, 120, 200));
        
        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" + message + "</div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(new Color(240, 240, 240));
        
        final boolean[] result = new boolean[1]; // Array to hold result
        
        JButton yesButton = new JButton("Yes");
        yesButton.setBackground(new Color(0, 150, 0));
        yesButton.setForeground(Color.WHITE);
        yesButton.setFocusPainted(false);
        yesButton.addActionListener(e -> {
            result[0] = true;
            // Close the dialog
            Window window = SwingUtilities.getWindowAncestor(yesButton);
            if (window != null) {
                window.dispose();
            }
        });
        
        JButton noButton = new JButton("No");
        noButton.setBackground(new Color(200, 0, 0));
        noButton.setForeground(Color.WHITE);
        noButton.setFocusPainted(false);
        noButton.addActionListener(e -> {
            result[0] = false;
            Window window = SwingUtilities.getWindowAncestor(noButton);
            if (window != null) {
                window.dispose();
            }
        });
        
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        
        panel.add(iconLabel, BorderLayout.NORTH);
        panel.add(messageLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Create custom option pane
        Object[] options = {panel};
        JOptionPane pane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, 
            JOptionPane.DEFAULT_OPTION, null, new Object[]{});
        
        JDialog dialog = pane.createDialog(parent, "Confirmation");
        dialog.setVisible(true);
        
        // Return the result
        return result[0];
    }
    
    public static String inputDialog(JFrame parent, String message) {
        return JOptionPane.showInputDialog(parent, message);
    }
}
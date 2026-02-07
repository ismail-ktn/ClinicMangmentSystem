package javaapplication5;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class ReceptionDashboard extends JFrame {
    private User user;
    private ClinicData data;
    
    private Color gold = new Color(212,175,55);
    private Color dark = new Color(34,34,34);
    
    public ReceptionDashboard(User user) {
        this.user = user;
        this.data = ClinicData.getInstance();
        
        setTitle("Reception Dashboard");
        setSize(900,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        setupUI();
        setVisible(true);
    }
    
    private void setupUI() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Header
        JPanel header = createHeader();
        mainPanel.add(header, BorderLayout.NORTH);
        
        // Menu buttons on left
        JPanel menu = createMenu();
        mainPanel.add(menu, BorderLayout.WEST);
        
        // Center content
        JPanel center = createCenter();
        mainPanel.add(center, BorderLayout.CENTER);
        
        // Footer
        JPanel footer = createFooter();
        mainPanel.add(footer, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(dark);
        header.setBorder(new EmptyBorder(10,20,10,20));
        
        JLabel title = new JLabel("RECEPTION DASHBOARD");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(gold);
        
        JLabel welcome = new JLabel("Welcome, " + user.getFullName());
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 15));
        welcome.setForeground(gold);
        
        header.add(title, BorderLayout.WEST);
        header.add(welcome, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createMenu() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(dark);
        menu.setBorder(new EmptyBorder(20,20,20,20));
        
        // Buttons
        addMenuButton(menu, "➕ Add Patient", this::addPatient);
        addMenuButton(menu, "👥 View Patients", this::viewPatients);
        addMenuButton(menu, "📅 Add Appointment", this::addAppointment);
        addMenuButton(menu, "📋 View Appointments", this::viewAppointments);
        addMenuButton(menu, "👤 Add User", this::addUser);
        addMenuButton(menu, "👥 View Users", this::viewUsers);
        addMenuButton(menu, "🗑 Delete User", this::deleteUser);
        addMenuButton(menu, "📊 Report", this::showReport);
        addMenuButton(menu, "🚪 Logout", this::logout);
        
        return menu;
    }
    
    private JPanel createCenter() {
        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(new EmptyBorder(20,20,20,20));
        center.setBackground(dark);
        
        JLabel title = new JLabel("Clinic Management System", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(gold);
        center.add(title, BorderLayout.NORTH);
        
        // Stats panel
        JPanel stats = new JPanel(new GridLayout(1,3,15,15));
        stats.setBackground(dark);
        
        stats.add(createStatCard("Patients", data.getPatients().size()));
        stats.add(createStatCard("Appointments", data.getAppointments().size()));
        stats.add(createStatCard("Users", data.getAllUsers().size()));
        
        center.add(stats, BorderLayout.CENTER);
        
        return center;
    }
    
    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(dark);
        footer.setBorder(new EmptyBorder(10,20,10,20));
        
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        JLabel date = new JLabel(today);
        date.setFont(new Font("Segoe UI", Font.BOLD, 15));
        date.setForeground(Color.LIGHT_GRAY);
        
        JLabel info = new JLabel("Patients: " + data.getPatients().size() + 
                               " | Appointments: " + data.getAppointments().size());
        info.setFont(new Font("Segoe UI", Font.BOLD, 15));
        info.setForeground(gold);
        
        footer.add(date, BorderLayout.WEST);
        footer.add(info, BorderLayout.EAST);
        
        return footer;
    }
    
    // ============== BUTTON ACTIONS ==============
    
    // 1. ADD APPOINTMENT (WORKING)
    private void addAppointment() {
        // Get all patients
        List<Patient> patients = data.getPatients();
        if(patients.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No patients found. Add patient first!");
            return;
        }
        
        // Create dropdown for patients
        String[] patientList = new String[patients.size()];
        for(int i=0; i<patients.size(); i++) {
            Patient p = patients.get(i);
            patientList[i] = p.patientId + " - " + p.name;
        }
        
        JComboBox<String> patientCombo = new JComboBox<>(patientList);
        JTextField dateField = new JTextField(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        JTextField timeField = new JTextField("10:00");
        JTextArea reasonArea = new JTextArea(3, 20);
        reasonArea.setLineWrap(true);
        
        // Get doctors
        List<User> users = data.getAllUsers();
        String[] doctors = new String[users.size()];
        int docCount = 0;
        for(User u : users) {
            if(u.getRole().equals("DOCTOR")) {
                doctors[docCount] = u.getFullName();
                docCount++;
            }
        }
        
        if(docCount == 0) {
            doctors = new String[]{"Dr. Sharma"};
            docCount = 1;
        }
        
        String[] finalDoctors = new String[docCount];
        System.arraycopy(doctors, 0, finalDoctors, 0, docCount);
        JComboBox<String> doctorCombo = new JComboBox<>(finalDoctors);
        
        // Create form
        Object[] form = {
            "Select Patient:", patientCombo,
            "Select Doctor:", doctorCombo,
            "Date (dd-MM-yyyy):", dateField,
            "Time (HH:mm):", timeField,
            "Reason:", new JScrollPane(reasonArea)
        };
        
        int result = JOptionPane.showConfirmDialog(this, form, "Add Appointment", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if(result == JOptionPane.OK_OPTION) {
            try {
                // Get selected patient
                String selected = (String) patientCombo.getSelectedItem();
                String patientId = selected.split(" - ")[0];
                String patientName = "";
                
                for(Patient p : patients) {
                    if(p.patientId.equals(patientId)) {
                        patientName = p.name;
                        break;
                    }
                }
                
                // Create appointment
                String appId = "A" + (data.getAppointments().size() + 1);
                String dateTime = dateField.getText() + " " + timeField.getText();
                String doctor = (String) doctorCombo.getSelectedItem();
                String reason = reasonArea.getText();
                if(reason.isEmpty()) reason = "General Checkup";
                
                Appointment appointment = new Appointment(
                    appId,
                    patientId,
                    patientName,
                    doctor,
                    dateTime,
                    "Scheduled",
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                );
                
                // Save appointment
                data.addAppointment(appointment);
                
                // Show success
                JOptionPane.showMessageDialog(this, 
                    "Appointment Added!\n" +
                    "ID: " + appId + "\n" +
                    "Patient: " + patientName + "\n" +
                    "Doctor: " + doctor + "\n" +
                    "Time: " + dateTime
                );
                
                // Refresh
                refreshDashboard();
                
            } catch(Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }
    
    // 2. DELETE USER (WORKING)
    private void deleteUser() {
        // Get all users except current user
        List<User> allUsers = data.getAllUsers();
        java.util.List<String> userList = new java.util.ArrayList<>();
        
        for(User u : allUsers) {
            if(!u.getEmail().equals(user.getEmail())) {
                userList.add(u.getEmail() + " - " + u.getFullName() + " (" + u.getRole() + ")");
            }
        }
        
        if(userList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No other users found!");
            return;
        }
        
        // Create dropdown
        String[] usersArray = userList.toArray(new String[0]);
        JComboBox<String> userCombo = new JComboBox<>(usersArray);
        
        Object[] form = {
            "Select user to delete:",
            userCombo,
            "Warning: This cannot be undone!"
        };
        
        int result = JOptionPane.showConfirmDialog(this, form, "Delete User", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(result == JOptionPane.OK_OPTION) {
            String selected = (String) userCombo.getSelectedItem();
            String emailToDelete = selected.split(" - ")[0];
            
            // Confirm again
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete?\n" + selected,
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if(confirm == JOptionPane.YES_OPTION) {
                boolean deleted = data.deleteUser(emailToDelete);
                if(deleted) {
                    JOptionPane.showMessageDialog(this, "User deleted successfully!");
                    refreshDashboard();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete user!");
                }
            }
        }
    }
    
    // Other button actions (unchanged but working)
    private void addPatient() {
        JTextField name = new JTextField();
        JTextField age = new JTextField();
        JTextField phone = new JTextField();
        JTextField disease = new JTextField();
        
        Object[] form = {
            "Name:", name,
            "Age:", age,
            "Phone:", phone,
            "Disease:", disease
        };
        
        int result = JOptionPane.showConfirmDialog(this, form, "Add Patient", 
                JOptionPane.OK_CANCEL_OPTION);
        
        if(result == JOptionPane.OK_OPTION) {
            try {
                String patientId = "P" + (data.getPatients().size() + 1);
                Patient patient = new Patient(
                    patientId,
                    name.getText(),
                    Integer.parseInt(age.getText()),
                    "Male",
                    phone.getText(),
                    disease.getText(),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
                );
                
                data.addPatient(patient);
                JOptionPane.showMessageDialog(this, "Patient added! ID: " + patientId);
                refreshDashboard();
            } catch(Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
            }
        }
    }
    
    private void viewPatients() {
        List<Patient> patients = data.getPatients();
        if(patients.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No patients found!");
            return;
        }
        
        String[] columns = {"ID", "Name", "Age", "Phone", "Disease"};
        Object[][] rows = new Object[patients.size()][5];
        
        for(int i=0; i<patients.size(); i++) {
            Patient p = patients.get(i);
            rows[i][0] = p.patientId;
            rows[i][1] = p.name;
            rows[i][2] = p.age;
            rows[i][3] = p.phone;
            rows[i][4] = p.disease;
        }
        
        showTable(rows, columns, "Patients");
    }
    
    private void viewAppointments() {
        List<Appointment> appointments = data.getAppointments();
        if(appointments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No appointments found!");
            return;
        }
        
        String[] columns = {"ID", "Patient", "Doctor", "Date", "Status"};
        Object[][] rows = new Object[appointments.size()][5];
        
        for(int i=0; i<appointments.size(); i++) {
            Appointment a = appointments.get(i);
            rows[i][0] = a.appointmentId;
            rows[i][1] = a.patientName;
            rows[i][2] = a.doctorName;
            rows[i][3] = a.dateTime;
            rows[i][4] = a.status;
        }
        
        showTable(rows, columns, "Appointments");
    }
    
    private void addUser() {
        dispose();
        new SignupScreen();
    }
    
    private void viewUsers() {
        List<User> users = data.getAllUsers();
        if(users.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No users found!");
            return;
        }
        
        String[] columns = {"Name", "Email", "Phone", "Role"};
        Object[][] rows = new Object[users.size()][4];
        
        for(int i=0; i<users.size(); i++) {
            User u = users.get(i);
            rows[i][0] = u.getFullName();
            rows[i][1] = u.getEmail();
            rows[i][2] = u.getPhone();
            rows[i][3] = u.getRole();
        }
        
        showTable(rows, columns, "Users");
    }
    
    private void showReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== CLINIC REPORT ===\n");
        report.append("Date: ").append(LocalDate.now()).append("\n");
        report.append("Total Patients: ").append(data.getPatients().size()).append("\n");
        report.append("Total Appointments: ").append(data.getAppointments().size()).append("\n");
        report.append("Total Users: ").append(data.getAllUsers().size()).append("\n");
        
        JTextArea area = new JTextArea(report.toString(), 10, 40);
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Report", 
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void logout() {
        int result = JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", 
                JOptionPane.YES_NO_OPTION);
        if(result == JOptionPane.YES_OPTION) {
            dispose();
            new WelcomeScreen();
        }
    }
    
    // ============== HELPER METHODS ==============
    
    private void addMenuButton(JPanel panel, String text, Runnable action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setBackground(dark);
        button.setForeground(gold);
        button.setFocusPainted(false);
        button.setMaximumSize(new Dimension(230, 45));
        button.addActionListener(e -> action.run());
        
        panel.add(button);
        panel.add(Box.createVerticalStrut(10));
    }
    
    private JPanel createStatCard(String title, int value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        card.add(titleLabel, BorderLayout.NORTH);
        
        JLabel valueLabel = new JLabel(String.valueOf(value), SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void showTable(Object[][] data, String[] columns, String title) {
        JTable table = new JTable(data, columns);
        table.setRowHeight(25);
        
        // Center align all cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for(int i=0; i<columns.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.PLAIN_MESSAGE);
    }
    
    private void refreshDashboard() {
        getContentPane().removeAll();
        setupUI();
        revalidate();
        repaint();
    }
}
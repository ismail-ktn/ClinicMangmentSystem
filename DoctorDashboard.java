package javaapplication5;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class DoctorDashboard extends JFrame {

    private final User user;
    private final ClinicData data = ClinicData.getInstance();

    private static final Color BLACK = new Color(18,18,18);
    private static final Color DARK  = new Color(25,25,25);
    private static final Color GOLD  = new Color(212,175,55);
    private static final Color SOFT  = new Color(230,190,80);
    private static final Color WHITE = new Color(240,240,240);

    private JTable table;
    private DefaultTableModel model;

    public DoctorDashboard(User user) {
        this.user = user;
        setTitle("Doctor Dashboard - " + user.getFullName());
        setSize(1000,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BLACK);
        main.add(header(), BorderLayout.NORTH);
        main.add(body(), BorderLayout.CENTER);
        add(main);

        loadAppointments();
        setVisible(true);
    }

    private JPanel header() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(DARK);
        p.setBorder(new EmptyBorder(18,25,18,25));

        JLabel t = lbl("DOCTOR DASHBOARD",22,GOLD);
        // REMOVE "Dr. " prefix if already present in full name
        String doctorName = user.getFullName();
        if(doctorName.startsWith("Dr. ")) {
            doctorName = doctorName.substring(4); // Remove "Dr. "
        }
        JLabel d = lbl(doctorName,14,WHITE);

        p.add(t, BorderLayout.WEST);
        p.add(d, BorderLayout.EAST);
        return p;
    }

    private JPanel body() {
        JPanel p = new JPanel(new BorderLayout(20,0));
        p.setBackground(BLACK);
        p.setBorder(new EmptyBorder(20,20,20,20));
        p.add(menu(),BorderLayout.WEST);
        p.add(tablePanel(),BorderLayout.CENTER);
        return p;
    }

    private JPanel menu() {
        JPanel m = new JPanel(new GridLayout(6,1,12,12));
        m.setPreferredSize(new Dimension(240,0));
        m.setBackground(BLACK);

        m.add(btn("Write Prescription", this::writePrescription));
        m.add(btn("Send To Lab", this::sendToLab));
        m.add(btn("View Patients", this::viewPatients));
        m.add(btn("View Lab Results", this::viewLabResults));
        m.add(btn("Refresh Appointments", this::loadAppointments));
        m.add(btn("Logout", this::logout));

        return m;
    }

    private JPanel tablePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(DARK);
        p.setBorder(new EmptyBorder(10,10,10,10));

        p.add(lbl("My Appointments",16,SOFT), BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID","Patient","Date","Status","Disease","Doctor"},0){
            public boolean isCellEditable(int r,int c){return false;}
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setBackground(new Color(22,22,22));
        table.setForeground(WHITE);
        table.setSelectionBackground(new Color(50,50,50));

        DefaultTableCellRenderer c = new DefaultTableCellRenderer();
        c.setHorizontalAlignment(SwingConstants.CENTER);
        c.setForeground(WHITE);
        c.setBackground(new Color(22,22,22));
        for(int i=0;i<table.getColumnCount();i++) table.getColumnModel().getColumn(i).setCellRenderer(c);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(null);
        sp.getViewport().setBackground(new Color(22,22,22));

        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    private void loadAppointments() {
        model.setRowCount(0);
        
        // Get doctor's name (without Dr. prefix if present)
        String doctorName = user.getFullName();
        
        // Get appointments for this doctor
        List<Appointment> allAppointments = data.getAppointments();
        
        for (Appointment a : allAppointments) {
            // Debug: Print appointment details
            System.out.println("Checking appointment: " + a.doctorName + " vs " + doctorName);
            
            // Check if this appointment belongs to current doctor
            if (a.doctorName.equalsIgnoreCase(doctorName) || 
                a.doctorName.equalsIgnoreCase("Dr. " + doctorName) ||
                ("Dr. " + a.doctorName).equalsIgnoreCase(doctorName)) {
                
                // Find patient's disease
                String disease = "Unknown";
                for (Patient p : data.getPatients()) {
                    if (p.patientId.equals(a.patientId)) {
                        disease = p.disease;
                        break;
                    }
                }
                
                model.addRow(new Object[]{
                    a.appointmentId, 
                    a.patientName, 
                    a.dateTime, 
                    a.status, 
                    disease,
                    a.doctorName
                });
            }
        }
        
        if(model.getRowCount()==0) {
            // Show all appointments for debugging
            showAllAppointmentsForDebugging();
            model.addRow(new Object[]{"No appointments found","","","","",""});
        }
    }

    private void showAllAppointmentsForDebugging() {
        System.out.println("=== DEBUG: All Appointments ===");
        List<Appointment> allApps = data.getAppointments();
        for(Appointment a : allApps) {
            System.out.println("Appointment: " + a.appointmentId + 
                             " | Doctor: " + a.doctorName + 
                             " | Patient: " + a.patientName);
        }
        System.out.println("Current Doctor: " + user.getFullName());
        System.out.println("=== END DEBUG ===");
    }

    private void writePrescription() {
        int r = table.getSelectedRow();
        if (r==-1) { 
            JOptionPane.showMessageDialog(this, "Select an appointment first", "Warning", JOptionPane.WARNING_MESSAGE); 
            return; 
        }
        
        String patientName = (String) model.getValueAt(r, 1);
        String[] medicines = {"Paracetamol", "Antibiotic", "Cough Syrup", "Pain Killer", "Vitamin C"};
        
        JComboBox<String> medicineCombo = new JComboBox<>(medicines);
        JTextField dosageField = new JTextField("1 tablet");
        JTextField daysField = new JTextField("3");
        
        Object[] fields = {
            "Patient: " + patientName, "",
            "Select Medicine:", medicineCombo,
            "Dosage:", dosageField,
            "Days:", daysField
        };
        
        int result = JOptionPane.showConfirmDialog(this, fields, "Write Prescription", 
                JOptionPane.OK_CANCEL_OPTION);
        
        if(result == JOptionPane.OK_OPTION) {
            String medicine = (String) medicineCombo.getSelectedItem();
            String dosage = dosageField.getText();
            String days = daysField.getText();
            
            String prescription = "Prescription for " + patientName + ":\n" +
                                 "Medicine: " + medicine + "\n" +
                                 "Dosage: " + dosage + "\n" +
                                 "Duration: " + days + " days";
            
            JOptionPane.showMessageDialog(this, prescription, "Prescription Saved", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void sendToLab() {
        int r = table.getSelectedRow();
        if (r==-1) { 
            JOptionPane.showMessageDialog(this, "Select an appointment first", "Warning", JOptionPane.WARNING_MESSAGE); 
            return; 
        }

        String[] tests = {"Blood Test","Urine Test","X-Ray","MRI","ECG"};
        String test = (String) JOptionPane.showInputDialog(this, "Select Test", "Lab Test", 
                JOptionPane.PLAIN_MESSAGE, null, tests, tests[0]);
        
        if (test != null) {
            String patientName = (String) model.getValueAt(r, 1);
            String doctorName = user.getFullName();
            
            // Create lab test
            LabTest labTest = new LabTest(
                test, 
                patientName, 
                doctorName, 
                "Pending", 
                "ORDERED", 
                java.time.LocalDate.now().toString()
            );
            
            data.addLabTest(labTest);
            JOptionPane.showMessageDialog(this, test + " ordered successfully for " + patientName);
        }
    }

    private void viewPatients() {
        List<Patient> patients = data.getPatients();
        if(patients.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No patients found");
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
        
        showTableDialog(rows, columns, "All Patients");
    }

    private void viewLabResults() {
        List<LabTest> labTests = data.getLabTests();
        if(labTests.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No lab tests found");
            return;
        }
        
        String[] columns = {"Test", "Patient", "Doctor", "Result", "Status"};
        Object[][] rows = new Object[labTests.size()][5];
        
        for(int i=0; i<labTests.size(); i++) {
            LabTest t = labTests.get(i);
            rows[i][0] = t.testName;
            rows[i][1] = t.patientName;
            rows[i][2] = t.doctorName;
            rows[i][3] = t.result;
            rows[i][4] = t.status;
        }
        
        showTableDialog(rows, columns, "Lab Results");
    }

    private void logout() {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", 
                "Logout", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) { 
            dispose(); 
            new WelcomeScreen(); 
        }
    }

    // Helper method to show tables in dialog
    private void showTableDialog(Object[][] data, String[] columns, String title) {
        JTable table = new JTable(data, columns);
        table.setRowHeight(25);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for(int i=0; i<columns.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.PLAIN_MESSAGE);
    }

    // ===== helpers =====
    private JLabel lbl(String t, int s, Color c){ 
        JLabel l = new JLabel(t); 
        l.setFont(new Font("Segoe UI", Font.BOLD, s)); 
        l.setForeground(c); 
        return l; 
    }

    private JButton btn(String t, Runnable r){
        JButton b = new JButton(t);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(DARK); 
        b.setForeground(GOLD);
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GOLD), 
            new EmptyBorder(14, 18, 14, 18)
        ));
        b.setFocusPainted(false); 
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.addActionListener(e -> r.run());
        return b;
    }
}




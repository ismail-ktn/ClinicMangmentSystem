package javaapplication5.data;

import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.*;
import javaapplication5.model.Appointment;
import javaapplication5.model.LabTest;
import javaapplication5.model.Medicine;
import javaapplication5.model.Patient;
import javaapplication5.model.User;

public class ClinicData {
    private static ClinicData instance;
    
    // Data storage
    private Map<String, User> users = new HashMap<>();
    private List<Patient> patients = new ArrayList<>();
    private List<Appointment> appointments = new ArrayList<>();
    private List<Medicine> medicines = new ArrayList<>();
    private List<LabTest> labTests = new ArrayList<>();
    
    // File names
    private String usersFile = "users.dat";
    private String patientsFile = "patients.dat";
    private String appointmentsFile = "appointments.dat";
    
    private DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    
    public List<Appointment> getDoctorAppointments(String doctorName) {
        List<Appointment> result = new ArrayList<>();
        System.out.println("Looking for doctor: " + doctorName);
        
        for (Appointment a : appointments) {
            System.out.println("Checking: " + a.doctorName);
            // Case insensitive comparison
            if (a.doctorName.equalsIgnoreCase(doctorName) || 
                a.doctorName.equalsIgnoreCase("Dr. " + doctorName) ||
                ("Dr. " + a.doctorName).equalsIgnoreCase(doctorName)) {
                result.add(a);
            }
        }
        System.out.println("Found " + result.size() + " appointments");
        return result;
    }
    // Private constructor
    private ClinicData() {
        loadData();
        if(users.isEmpty()) {
            createSampleData();
            saveData();
        }
    }
    
    // Get instance
    public static ClinicData getInstance() {
        if(instance == null) {
            instance = new ClinicData();
        }
        return instance;
    }
    
    private void loadData() {
        loadUsers();
        loadPatients();
        loadAppointments();
    }
    
    private void loadUsers() {
        try {
            File file = new File(usersFile);
            if(!file.exists()) return;
            
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\|");
                if(parts.length == 5) {
                    User user = new User(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    users.put(parts[1], user); // Key is email
                }
            }
            scanner.close();
        } catch(Exception e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }
    
    private void loadPatients() {
        try {
            File file = new File(patientsFile);
            if(!file.exists()) return;
            
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\|");
                if(parts.length == 7) {
                    Patient patient = new Patient(
                        parts[0], // id
                        parts[1], // name
                        Integer.parseInt(parts[2]), // age
                        parts[3], // gender
                        parts[4], // phone
                        parts[5], // disease
                        parts[6]  // date
                    );
                    patients.add(patient);
                }
            }
            scanner.close();
        } catch(Exception e) {
            System.out.println("Error loading patients: " + e.getMessage());
        }
    }
    
    private void loadAppointments() {
        try {
            File file = new File(appointmentsFile);
            if(!file.exists()) return;
            
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\|");
                if(parts.length == 7) {
                    Appointment appointment = new Appointment(
                        parts[0], // id
                        parts[1], // patientId
                        parts[2], // patientName
                        parts[3], // doctorName
                        parts[4], // dateTime
                        parts[5], // status
                        parts[6]  // createdDate
                    );
                    appointments.add(appointment);
                }
            }
            scanner.close();
        } catch(Exception e) {
            System.out.println("Error loading appointments: " + e.getMessage());
        }
    }
    
    private void saveData() {
        saveUsers();
        savePatients();
        saveAppointments();
    }
    
    private void saveUsers() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(usersFile));
            for(User user : users.values()) {
                writer.println(user.getFullName() + "|" + 
                             user.getEmail() + "|" + 
                             user.getPassword() + "|" + 
                             user.getPhone() + "|" + 
                             user.getRole());
            }
            writer.close();
        } catch(Exception e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
    
    private void savePatients() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(patientsFile));
            for(Patient patient : patients) {
                writer.println(patient.patientId + "|" + 
                             patient.name + "|" + 
                             patient.age + "|" + 
                             patient.gender + "|" + 
                             patient.phone + "|" + 
                             patient.disease + "|" + 
                             patient.dateAdded);
            }
            writer.close();
        } catch(Exception e) {
            System.out.println("Error saving patients: " + e.getMessage());
        }
    }
    
    private void saveAppointments() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(appointmentsFile));
            for(Appointment appointment : appointments) {
                writer.println(appointment.appointmentId + "|" + 
                             appointment.patientId + "|" + 
                             appointment.patientName + "|" + 
                             appointment.doctorName + "|" + 
                             appointment.dateTime + "|" + 
                             appointment.status + "|" + 
                             appointment.createdDate);
            }
            writer.close();
        } catch(Exception e) {
            System.out.println("Error saving appointments: " + e.getMessage());
        }
    }
    
    // ============== SAMPLE DATA ==============
    private void createSampleData() {
        // Add sample users
        users.put("doctor@clinic.com", new User("Dr. Sharma", "doctor@clinic.com", "123", "9876543210", "DOCTOR"));
        users.put("reception@clinic.com", new User("Receptionist", "reception@clinic.com", "123", "9876543211", "RECEPTION"));
        users.put("pharmacy@clinic.com", new User("Pharmacist", "pharmacy@clinic.com", "123", "9876543212", "PHARMACY"));
        
        // Add sample patients
        if(patients.isEmpty()) {
            patients.add(new Patient("P001", "Rajesh Kumar", 35, "Male", "9876543201", "Fever", getCurrentDateTime()));
            patients.add(new Patient("P002", "Priya Singh", 28, "Female", "9876543202", "Cold", getCurrentDateTime()));
        }
        
        // Add sample appointment
        if(appointments.isEmpty()) {
            appointments.add(new Appointment("A001", "P001", "Rajesh Kumar", "Dr. Sharma", 
                getTomorrowDateTime(), "Scheduled", getCurrentDate()));
        }
    }
    
              
                           /*----------------- SERVICES -------------------*/
    
    
    // ============== USER METHODS ==============
    public boolean authenticate(String email, String password) {
        User user = users.get(email);
        return user != null && user.getPassword().equals(password);
    }
    
    public User getUser(String email) {
        return users.get(email);
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    public boolean addUser(User user) {
        if(users.containsKey(user.getEmail())) {
            return false;
        }
        users.put(user.getEmail(), user);
        saveData();
        return true;
    }
    
    public boolean deleteUser(String email) {
        if(users.containsKey(email)) {
            users.remove(email);
            saveData();
            return true;
        }
        return false;
    }
    
    // ============== PATIENT METHODS ==============
    public List<Patient> getPatients() {
        return patients;
    }
    
    public void addPatient(Patient patient) {
        patients.add(patient);
        saveData();
    }
    
    public String getNextPatientId() {
        int maxId = 0;
        for(Patient p : patients) {
            try {
                if(p.patientId.startsWith("P")) {
                    int id = Integer.parseInt(p.patientId.substring(1));
                    if(id > maxId) maxId = id;
                }
            } catch(Exception e) {
                // Skip invalid IDs
            }
        }
        return "P" + String.format("%03d", maxId + 1);
    }
    
    // ============== APPOINTMENT METHODS ==============
    public List<Appointment> getAppointments() {
        return appointments;
    }
    
    // ADD APPOINTMENT METHOD
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        saveData();
    }
    
//    public List<Appointment> getDoctorAppointments(String doctorName) {
//        List<Appointment> result = new ArrayList<>();
//        for(Appointment a : appointments) {
//            if(a.doctorName.equalsIgnoreCase(doctorName)) {
//                result.add(a);
//            }
//        }
//        return result;
//    }
    
    // ============== MEDICINE METHODS ==============
    public List<Medicine> getMedicines() {
        return medicines;
    }
    
    public void addMedicine(Medicine medicine) {
        medicines.add(medicine);
        saveData();
    }
    
    // ============== LAB TEST METHODS ==============
    public List<LabTest> getLabTests() {
        return labTests;
    }
    
    public void addLabTest(LabTest test) {
        labTests.add(test);
        saveData();
    }
    
    // ============== HELPER METHODS ==============
    private String getCurrentDateTime() {
        return LocalDateTime.now().format(dtFormat);
    }
    
    private String getCurrentDate() {
        return LocalDateTime.now().format(dateFormat);
    }
    
    private String getTomorrowDateTime() {
        return LocalDateTime.now().plusDays(1).format(dtFormat);
    }
    
    // For Pharmacy Dashboard
    public void saveAllData() {
        saveData();
    }
}


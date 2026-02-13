package javaapplication5.model;

public class Patient {
    public String patientId;
    public String name;
    public int age;
    public String gender;
    public String phone;
    public String disease;
    public String dateAdded;
    
    public Patient(String patientId, String name, int age, String gender, String phone, String disease, String dateAdded) {
        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.disease = disease;
        this.dateAdded = dateAdded;
    }
    
    @Override
    public String toString() {
        return patientId + " - " + name + " (" + age + " yrs, " + gender + ") - " + 
               disease + " - Phone: " + phone + " - Added: " + dateAdded;
    }
}

package javaapplication5;

public class User {

    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String role; // DOCTOR, RECEPTION, PHARMACY

    public User(String fullName, String email, String password, String phone, String role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }

    // ===== GETTERS =====
    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {   // ✅ FIXED
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

    // ===== toString =====
    @Override
    public String toString() {
        return fullName + " (" + role + ") - " + email;
    }
}
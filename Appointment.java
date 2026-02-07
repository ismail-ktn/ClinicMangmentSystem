package javaapplication5;
public class Appointment {
    public String appointmentId;
    public String patientId;
    public String patientName;
    public String doctorName;
    public String dateTime;
    public String status;
    public String createdDate;
    
    public Appointment(String appointmentId, String patientId, String patientName, 
                      String doctorName, String dateTime, String status, String createdDate) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.dateTime = dateTime;
        this.status = status;
        this.createdDate = createdDate;
    }
    
    @Override
    public String toString() {
        return appointmentId + " - " + patientName + " with " + doctorName + 
               " at " + dateTime + " [" + status + "] - Created: " + createdDate;
    }
}

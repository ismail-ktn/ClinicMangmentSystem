package javaapplication5.model;

public class LabTest {
    public String testName;
    public String patientName;
    public String doctorName;
    public String result;
    public String status;
    public String dateOrdered;
    
    public LabTest(String testName, String patientName, String doctorName, 
                  String result, String status, String dateOrdered) {
        this.testName = testName;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.result = result;
        this.status = status;
        this.dateOrdered = dateOrdered;
    }
    
    @Override
    public String toString() {
        return testName + " - Patient: " + patientName + " - Doctor: " + doctorName + 
               " - Result: " + result + " - Status: " + status + " - Ordered: " + dateOrdered;
    }
}
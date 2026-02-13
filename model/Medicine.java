package javaapplication5.model;

public class Medicine {
    public String name;
    public int quantity;
    public double price;
    public String expiryDate;
    
    public Medicine(String name, int quantity, double price, String expiryDate) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.expiryDate = expiryDate;
    }
    
    @Override
    public String toString() {
        return name + " - Qty: " + quantity + " - Price: ₹" + price + " - Expires: " + expiryDate;
    }
}
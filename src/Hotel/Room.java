package Hotel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Room implements Serializable {
    private int roomNumber;
    private String roomType;
    private double price;
    private boolean booked;
    private String customerName;
    private String lastActionTime;

    // Constructor for creating new rooms
    public Room(int roomNumber, String roomType, double price) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.booked = false;
        this.customerName = "";
        this.lastActionTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Constructor for loading rooms from database
    public Room(int roomNumber, String roomType, double price, boolean booked, String customerName, String lastActionTime) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.booked = booked;
        this.customerName = customerName;
        this.lastActionTime = lastActionTime;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPrice() {
        return price;
    }

    public boolean isBooked() {
        return booked;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getLastActionTime() {
        return lastActionTime;
    }

    public void setLastActionTime(String lastActionTime) {
        this.lastActionTime = lastActionTime;
    }

    public void book(String customerName) {
        this.booked = true;
        this.customerName = customerName;
        updateTimestamp("Booked");
    }

    public void cancelBooking() {
        this.booked = false;
        this.customerName = "";
        updateTimestamp("Booking Canceled");
    }

    public void checkOut() {
        this.booked = false;
        this.customerName = "";
        updateTimestamp("Checked Out");
    }

    private void updateTimestamp(String action) {
        this.lastActionTime = action + " at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    public static String getCurrentTimestamp(String action) {
        return action + " at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String toString() {
        return "Room #" + roomNumber + " (" + roomType + ") - " + (booked ? "Booked by " + customerName : "Available") + ", Price: $" + price;
    }
}

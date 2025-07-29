package Hotel;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Hotel {

    private final List<Room> rooms;

    public Hotel(int numberOfRooms) {
        DatabaseConnection.initializeDatabase();
        rooms = loadRoomsFromDatabase();
        if (rooms.isEmpty()) {
            for (int i = 1; i <= numberOfRooms; i++) {
                String type = i <= numberOfRooms / 2 ? "Standard" : "Deluxe";
                double price = type.equals("Standard") ? 100.0 : 180.0;
                Room room = new Room(i, type, price);
                rooms.add(room);
                saveRoomToDatabase(room);
            }
            System.out.println("✅ Initialized " + numberOfRooms + " rooms.");
        }
    }

    // Load rooms from the database
    private List<Room> loadRoomsFromDatabase() {
        List<Room> loadedRooms = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); // ✅ FIXED HERE
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM rooms")) {

            while (rs.next()) {
                int roomNumber = rs.getInt("roomNumber");
                String roomType = rs.getString("roomType");
                double price = rs.getDouble("price");
                String customerName = rs.getString("customerName");
                boolean booked = rs.getInt("booked") == 1;
                String lastActionTimeStr = rs.getString("lastActionTime");

                Room room = new Room(roomNumber, roomType, price);
                if (booked) {
                    room.book(customerName);
                }
                room.setLastActionTime(lastActionTimeStr);

                loadedRooms.add(room);
            }
            System.out.println("📂 Rooms loaded from database.");
        } catch (SQLException e) {
            System.out.println("❌ Failed to load rooms: " + e.getMessage());
        }
        return loadedRooms;
    }

    // Save room to the database
    public void saveRoomToDatabase(Room room) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = """
                INSERT OR REPLACE INTO rooms (roomNumber, roomType, price, customerName, booked, lastActionTime)
                VALUES (?, ?, ?, ?, ?, ?)
            """;
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, room.getRoomNumber());
            pstmt.setString(2, room.getRoomType());
            pstmt.setDouble(3, room.getPrice());
            pstmt.setString(4, room.getCustomerName());
            pstmt.setInt(5, room.isBooked() ? 1 : 0);
            pstmt.setString(6, room.getLastActionTime().toString());
            pstmt.executeUpdate();
            System.out.println("💾 Room " + room.getRoomNumber() + " saved to database.");
        } catch (SQLException e) {
            System.out.println("❌ Failed to save booking to DB: " + e.getMessage());
        }
    }

    // Show all available rooms
    public void showAvailableRooms() {
        for (Room room : rooms) {
            if (!room.isBooked()) {
                System.out.println("Room #" + room.getRoomNumber() + " - " + room.getRoomType() + " - $" + room.getPrice());
            }
        }
    }

    // Book a room
    public boolean bookRoom(int roomNumber, String customerName) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber && !room.isBooked()) {
                room.book(customerName);
                saveRoomToDatabase(room);
                System.out.println("✅ Room #" + roomNumber + " booked for " + customerName);
                return true;
            }
        }
        System.out.println("❌ Room #" + roomNumber + " is not available.");
        return false;
    }

    // Cancel a room booking
    public boolean cancelBooking(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber && room.isBooked()) {
                room.cancelBooking();
                saveRoomToDatabase(room);
                System.out.println("✅ Booking for Room #" + roomNumber + " canceled.");
                return true;
            }
        }
        System.out.println("❌ Room #" + roomNumber + " is not currently booked.");
        return false;
    }

    // Check out of a room
    public boolean checkOut(int roomNumber) {
        return cancelBooking(roomNumber);
    }

    public List<Room> getRooms() {
        return rooms;
    }
}

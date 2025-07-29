package Hotel;

import java.io.File;
import java.sql.*;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:hotel.db";

    // Always returns a fresh connection
    public static Connection getConnection() {
        try {
            String dbPath = new File("hotel.db").getAbsolutePath();
            System.out.println("📍 Database file path: " + dbPath);
            Connection conn = DriverManager.getConnection(DB_URL);
            System.out.println("✅ Database connected successfully.");
            return conn;
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
            return null;
        }
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            createRoomsTable(stmt);
            createUsersTable(stmt);
            createBookingsTable(stmt);
            insertSampleRooms(conn);
        } catch (SQLException e) {
            System.out.println("❌ Database initialization failed: " + e.getMessage());
        }
    }

    private static void createRoomsTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS rooms (
                roomNumber INTEGER PRIMARY KEY,
                roomType TEXT,
                price REAL,
                customerName TEXT,
                booked INTEGER,
                lastActionTime TEXT
            )
        """;
        stmt.executeUpdate(sql);
        System.out.println("✅ rooms table created or already exists.");
    }

    private static void createUsersTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL
            )
        """;
        stmt.executeUpdate(sql);
        System.out.println("✅ users table created or already exists.");
    }

    private static void createBookingsTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS bookings (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL,
                roomNumber INTEGER NOT NULL,
                checkinDate TEXT,
                checkoutDate TEXT,
                status TEXT  -- booked, cancelled, checked_out
            )
        """;
        stmt.executeUpdate(sql);
        System.out.println("✅ bookings table created or already exists.");
    }

    private static void insertSampleRooms(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM rooms");
            if (rs.next() && rs.getInt("count") == 0) {
                stmt.executeUpdate("INSERT INTO rooms VALUES (101, 'Single', 100.0, NULL, 0, NULL)");
                stmt.executeUpdate("INSERT INTO rooms VALUES (102, 'Double', 150.0, NULL, 0, NULL)");
                stmt.executeUpdate("INSERT INTO rooms VALUES (103, 'Suite', 250.0, NULL, 0, NULL)");
                System.out.println("🛏️ Sample rooms inserted.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to insert sample rooms: " + e.getMessage());
        }
    }

    public static boolean addRoom(int roomNumber, String roomType, double price) {
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO rooms (roomNumber, roomType, price, booked) VALUES (?, ?, ?, 0)")) {
            stmt.setInt(1, roomNumber);
            stmt.setString(2, roomType);
            stmt.setDouble(3, price);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Failed to add room: " + e.getMessage());
            return false;
        }
    }

    public static ResultSet getAllRooms() {
        try {
            Connection conn = getConnection();
            return conn.createStatement().executeQuery("SELECT * FROM rooms");
        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch rooms: " + e.getMessage());
            return null;
        }
    }

    public static boolean updateRoomBooking(int roomNumber, String customerName, boolean isBooked) {
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "UPDATE rooms SET customerName = ?, booked = ? WHERE roomNumber = ?")) {
            stmt.setString(1, customerName);
            stmt.setInt(2, isBooked ? 1 : 0);
            stmt.setInt(3, roomNumber);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Failed to update booking: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteRoom(int roomNumber) {
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM rooms WHERE roomNumber = ?")) {
            stmt.setInt(1, roomNumber);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Failed to delete room: " + e.getMessage());
            return false;
        }
    }
}

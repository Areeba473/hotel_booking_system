package Hotel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class HotelBookingSwing {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelBookingSwing().createUI());
    }

    public void createUI() {
        JFrame frame = new JFrame("Four Seasons Hotel Booking System");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Background with gradient
        JPanel backgroundPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(70, 130, 180),
                        getWidth(), getHeight(), new Color(25, 25, 112));
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Header bar
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(25, 25, 112));
        headerPanel.setPreferredSize(new Dimension(frame.getWidth(), 80));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("🏨 Four Seasons Hotel Booking");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        backgroundPanel.add(headerPanel, BorderLayout.NORTH);

        // Sidebar buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 18);

        JButton showRoomsButton = createStyledButton("Show Available Rooms", buttonFont);
        JButton bookRoomButton = createStyledButton("Book Room", buttonFont);
        JButton cancelBookingButton = createStyledButton("Cancel Booking", buttonFont);
        JButton checkOutButton = createStyledButton("Check Out", buttonFont);
        JButton searchButton = createStyledButton("Search Room", buttonFont);
        JButton logoutButton = createStyledButton("Logout", buttonFont);


        buttonPanel.add(showRoomsButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(bookRoomButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(cancelBookingButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(checkOutButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(searchButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(logoutButton);

        // Table and model
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Room #", "Status", "Customer", "Price", "Last Action Time"}, 0);
        JTable table = new JTable(model);
        styleTable(table);

        // Table card container
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(new Color(255, 255, 255, 230));
        tableCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createEmptyBorder());
        tableScroll.getViewport().setOpaque(false);
        tableCard.add(tableScroll, BorderLayout.CENTER);

        backgroundPanel.add(buttonPanel, BorderLayout.WEST);
        backgroundPanel.add(tableCard, BorderLayout.CENTER);

        frame.setContentPane(backgroundPanel);
        frame.setVisible(true);

        // Event listeners
        refreshTable(model);
        showRoomsButton.addActionListener(e -> refreshTable(model));
        bookRoomButton.addActionListener(e -> bookRoom(model));
        cancelBookingButton.addActionListener(e -> cancelBooking(model));
        checkOutButton.addActionListener(e -> checkOut(model));
        searchButton.addActionListener(e -> searchRoom(model));
        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Thank you for using the system!");
            frame.dispose();
        });
    }

    private JButton createStyledButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(new Color(30, 144, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setOpaque(true);
        button.setBorderPainted(false);

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(65, 105, 225));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(30, 144, 255));
            }
        });

        return button;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("SansSerif", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.setForeground(Color.BLACK);
        table.setBackground(new Color(255, 255, 255, 240));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setGridColor(new Color(230, 230, 250));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void refreshTable(DefaultTableModel model) {
        model.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM rooms")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("room_number"),
                        rs.getBoolean("is_booked") ? "Booked" : "Available",
                        rs.getString("customer_name"),
                        "$" + rs.getDouble("price"),
                        rs.getString("last_action_time")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void bookRoom(DefaultTableModel model) {
        String roomNumber = JOptionPane.showInputDialog(null, "Enter Room Number:");
        String customerName = JOptionPane.showInputDialog(null, "Enter Customer Name:");
        String timestamp = "Booked at " + java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE rooms SET is_booked = 1, customer_name = ?, last_action_time = ? WHERE room_number = ? AND is_booked = 0")) {
            stmt.setString(1, customerName);
            stmt.setString(2, timestamp);
            stmt.setInt(3, Integer.parseInt(roomNumber));
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Room booked successfully!");
                refreshTable(model);
            } else {
                JOptionPane.showMessageDialog(null, "Room is already booked or doesn't exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cancelBooking(DefaultTableModel model) {
        String roomNumber = JOptionPane.showInputDialog(null, "Enter Room Number to Cancel Booking:");
        String timestamp = "Booking Canceled at " + java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE rooms SET is_booked = 0, customer_name = NULL, last_action_time = ? WHERE room_number = ?")) {
            stmt.setString(1, timestamp);
            stmt.setInt(2, Integer.parseInt(roomNumber));
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Booking cancelled successfully!");
                refreshTable(model);
            } else {
                JOptionPane.showMessageDialog(null, "Room not found or already available!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkOut(DefaultTableModel model) {
        cancelBooking(model);
    }

    private void searchRoom(DefaultTableModel model) {
        String roomNumber = JOptionPane.showInputDialog(null, "Enter Room Number to Search:");
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM rooms WHERE room_number = ?")) {
            stmt.setInt(1, Integer.parseInt(roomNumber));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(null,
                        "Room #" + roomNumber +
                                "\nStatus: " + (rs.getBoolean("is_booked") ? "Booked" : "Available") +
                                "\nCustomer: " + rs.getString("customer_name") +
                                "\nPrice: $" + rs.getDouble("price"));
            } else {
                JOptionPane.showMessageDialog(null, "Room not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

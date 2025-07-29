package Hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HotelHomePageSwing {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelHomePageSwing().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Four Seasons Hotel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // Load background image (ensure the path is correct in your project structure)
        ImageIcon backgroundImage = new ImageIcon(HotelHomePageSwing.class.getResource("/Hotel/background/image.jpeg"));
        JLabel background = new JLabel(backgroundImage);
        background.setLayout(new BorderLayout());
        frame.setContentPane(background);

        // Title label
        JLabel title = new JLabel("Four Seasons Hotel", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        background.add(title, BorderLayout.NORTH);

        // Button
        JButton enterButton = new JButton("Enter Booking System");
        enterButton.setFont(new Font("Arial", Font.BOLD, 20));
        enterButton.setBackground(Color.WHITE);
        enterButton.setForeground(Color.BLACK);
        enterButton.setFocusPainted(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(enterButton);
        background.add(buttonPanel, BorderLayout.SOUTH);

        // ✅ Fix: Properly launch HotelBookingSwing UI
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close current frame
                SwingUtilities.invokeLater(() -> new HotelBookingSwing().createUI()); // Open booking system
            }
        });

        frame.setVisible(true);
    }
}

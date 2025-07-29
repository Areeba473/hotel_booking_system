package Hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Hotel.background.ActivityLogger;


public class LoginScreen extends JFrame {

    private JTextField userField;
    private JPasswordField passField;

    public LoginScreen() {
        setTitle("Login - Hotel Booking");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Gradient background
        JPanel gradientPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                Color color1 = new Color(58, 123, 213);
                Color color2 = new Color(30, 58, 138);
                GradientPaint gp = new GradientPaint(0, 0, color1, width, height, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        gradientPanel.setLayout(new GridBagLayout());
        add(gradientPanel);

        // White form panel
        JPanel formPanel = new JPanel();
        formPanel.setPreferredSize(new Dimension(350, 250));
        formPanel.setBackground(new Color(245, 245, 250));
        formPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);

        userField = new JTextField(10);
        gbc.gridx = 1;
        formPanel.add(userField, gbc);

        // Password label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);

        passField = new JPasswordField(10);
        gbc.gridx = 1;
        formPanel.add(passField, gbc);

        // Login button
        JButton loginBtn = new JButton("Login");
        loginBtn.setPreferredSize(new Dimension(100, 30));
        loginBtn.setFont(new Font("Arial", Font.BOLD, 14));
        loginBtn.setBackground(new Color(51, 102, 255));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(loginBtn, gbc);

        // Signup link
        JLabel signUpLink = new JLabel("<html><a href='#'>New user? Sign up here</a></html>");
        signUpLink.setForeground(Color.BLUE);
        signUpLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridy = 3;
        formPanel.add(signUpLink, gbc);

        gradientPanel.add(formPanel);

        // Button actions
        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Please enter username and password.");
                return;
            }

            if (UserStore.validateUser(username, password)) {
                JOptionPane.showMessageDialog(this, "✅ Login successful!");
                dispose();
                ActivityLogger.log(username, "login");

                HotelHomePageSwing.main(null);  // Replace with your next screen
            } else {
                JOptionPane.showMessageDialog(this, "❌ Invalid credentials. Try again.");
            }
        });

        signUpLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
                new SignupScreen();  // Ensure this class exists
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase(); // Optional init
        new LoginScreen();
    }
}

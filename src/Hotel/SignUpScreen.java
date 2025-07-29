package Hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Hotel.background.ActivityLogger;

class
SignupScreen extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;


    public SignupScreen() {
        setTitle("Signup - Hotel Booking");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Gradient background panel
        JPanel backgroundPanel = new JPanel() {
            @Override
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
        backgroundPanel.setLayout(new GridBagLayout());
        add(backgroundPanel);

        // White box panel (centered)
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

        usernameField = new JTextField(10);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        // Password label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(10);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Signup button
        JButton signupButton = new JButton("Sign Up");
        signupButton.setPreferredSize(new Dimension(100, 30));
        signupButton.setFont(new Font("Arial", Font.BOLD, 14));
        signupButton.setBackground(new Color(51, 102, 255));
        signupButton.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(signupButton, gbc);

        // Login link
        JLabel loginLabel = new JLabel("<html><a href='#'>Already have an account? Login here</a></html>");
        loginLabel.setForeground(Color.BLUE);
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        gbc.gridy = 3;
        formPanel.add(loginLabel, gbc);

        backgroundPanel.add(formPanel);

        // Action listeners
        signupButton.addActionListener(e -> handleSignup());
        loginLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
                new LoginScreen(); // Go back to login
            }
        });

        setVisible(true);
    }

    private void handleSignup() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Please fill in all fields.");
            return;
        }

        boolean success = UserStore.addUser(username, password);
        if (success) {
            JOptionPane.showMessageDialog(this, "✅ Signup successful! You can now log in.");
            dispose();
            ActivityLogger.log(username, "signup");

            new LoginScreen();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Signup failed. Username may already exist.");
        }
    }

    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase(); // Initialize DB
        new SignupScreen();
    }
}

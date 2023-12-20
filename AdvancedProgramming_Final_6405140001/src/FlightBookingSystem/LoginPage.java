package FlightBookingSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

interface LoginListener {
    void onLogin(String userId);
}

class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private final List<LoginListener> loginListeners = new ArrayList<>();

    public LoginPage() {
        setTitle("User Login");
        setSize(330, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        ImageIcon logoIcon = resizeImageIcon(new ImageIcon("images/agencylogo.png"), 100, 100);
        JCheckBox logo = new JCheckBox(logoIcon);
        JLabel usernameLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> login());

        panel.add(logo);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);

        add(panel);
        setVisible(true);
        
        importUserLogsFromCsvToDatabase("csvfiles/user_logs.csv");
    }

    private void login() {
        String username = usernameField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        if (authenticateUser(username, password)) {
            logUser(username, true);
            JOptionPane.showMessageDialog(this, "Login successful!");

            setVisible(false);

            SwingUtilities.invokeLater(() -> {
                for (LoginListener listener : loginListeners) {
                    listener.onLogin(username);
                }
            });
        } else {
            logUser(username, false);
            JOptionPane.showMessageDialog(this, "Login failed. Please try again.");
        }

        usernameField.setText("");
        passwordField.setText("");
    }


    private boolean authenticateUser(String username, String password) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/flight_booking";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "SELECT * FROM user WHERE email = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next(); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void logUser(String username, boolean success) {
        String logFileName = "csvfiles/user_logs.csv";

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);

        try (FileWriter writer = new FileWriter(logFileName, true)) {
            writer.append(username).append(",");
            writer.append(success ? "SUCCESS" : "FAILURE").append(",");
            writer.append(timestamp).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void importUserLogsFromCsvToDatabase(String csvFilePath) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/flight_booking";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
             BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                String username = data[0];
                String logStatus = data[1]; // Assuming log status is in the second column as 'SUCCESS' or 'FAILURE'

                // Insert data into the user_logs table
                insertUserLogIntoDatabase(connection, username, logStatus);
            }

            System.out.println("User logs import completed.");

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertUserLogIntoDatabase(Connection connection, String username, String logStatus) {
        String query = "INSERT INTO user_logs (user_email, log_status) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, logStatus);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User Log Inserted into Database");
            } else {
                System.out.println("There is an error inserting the user log.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }

    public void addLoginListener(LoginListener listener) {
        loginListeners.add(listener);
    }

   
}
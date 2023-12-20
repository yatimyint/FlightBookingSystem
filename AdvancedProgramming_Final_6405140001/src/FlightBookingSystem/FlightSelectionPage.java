package FlightBookingSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class FlightSelectionPage extends JFrame {
    private JTable flightTable;
    private JComboBox<Flight> flightComboBox;
    private JButton bookFlightButton;
    private String username;  

    public FlightSelectionPage(String username) {
        this.username = username;  
        setTitle("Flight Selection");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        flightTable = new JTable();
        flightComboBox = new JComboBox<>();
        bookFlightButton = new JButton("Book Flight");
        JButton logoutButton = new JButton("Logout");

        setLayout(new FlowLayout());

        JPanel tablePanel = new JPanel(new BorderLayout());
        JPanel selectionPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        
        tablePanel.add(new JScrollPane(flightTable), BorderLayout.CENTER);

        selectionPanel.add(new JLabel("Select Flight:"));
        selectionPanel.add(flightComboBox);

        buttonPanel.add(bookFlightButton);
        buttonPanel.add(logoutButton);

        add(tablePanel);
        add(selectionPanel);
        add(buttonPanel);

        populateFlightsComboBox();
        populateFlightsTable();

        bookFlightButton.addActionListener(e -> bookFlight());
        logoutButton.addActionListener(e -> logout());

        setVisible(true);
    }

    private void populateFlightsComboBox() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/flight_booking";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "SELECT * FROM flights";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    Flight flight = new Flight(
                            resultSet.getString("flight_id"),
                            resultSet.getString("origin"),
                            resultSet.getString("destination"),
                            resultSet.getString("date"),
                            resultSet.getString("time")
                    );
                    flightComboBox.addItem(flight);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateFlightsTable() {
        Vector<Vector<String>> data = new Vector<>();
        Vector<String> header = new Vector<>();
        header.add("Flight ID");
        header.add("Origin");
        header.add("Destination");
        header.add("Date");
        header.add("Time");

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/flight_booking", "root", "")) {
            String query = "SELECT * FROM flights";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    Vector<String> row = new Vector<>();
                    row.add(resultSet.getString("flight_id"));
                    row.add(resultSet.getString("origin"));
                    row.add(resultSet.getString("destination"));
                    row.add(resultSet.getString("date"));
                    row.add(resultSet.getString("time"));
                    data.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        flightTable.setModel(new DefaultTableModel(data, header));
    }

    private void bookFlight() {
        // Get selected flight
        Flight selectedFlight = (Flight) flightComboBox.getSelectedItem();
        if (selectedFlight != null) {
            int bookingId = generateBookingId();

            insertBooking(bookingId, username, selectedFlight.getFlightId());

            logBooking(bookingId, username, getFullNameByUsername(username), selectedFlight.getFlightId(),
                    selectedFlight.getOrigin(), selectedFlight.getDestination(),
                    selectedFlight.getDate(), selectedFlight.getTime());

            JOptionPane.showMessageDialog(this, "Booking successful! Your ticket will be shown and sent to your email shortly.");
            FlightTicket flightTicket = new FlightTicket(bookingId, username);
            flightTicket.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this, "Please select a flight.");
        }
    }

    
    private String getFullNameByUsername(String username) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/flight_booking";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "SELECT name FROM user WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("name");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Unknown User";
    }
    
    private void logBooking(int bookingId, String username, String fullName, String flightId, String originCountry, String destinationCountry, String date, String time) {
        String logFileName = "csvfiles/flight_bookings.csv";

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);

        try (FileWriter writer = new FileWriter(logFileName, true)) {
            writer.append(String.valueOf(bookingId)).append(",");
            writer.append(username).append(",");
            writer.append(fullName).append(",");
            writer.append(flightId).append(",");
            writer.append(originCountry).append(",");
            writer.append(destinationCountry).append(",");
            writer.append(date).append(",");
            writer.append(time).append(",");
            writer.append(timestamp).append("\n");
        } catch (IOException e) {
            e.printStackTrace();  
            JOptionPane.showMessageDialog(this, "Error writing to the CSV file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private int generateBookingId() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/flight_booking";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "SELECT MAX(booking_id) AS last_booking_id FROM bookings";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    int lastBookingId = resultSet.getInt("last_booking_id");
                    return lastBookingId + 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 1; 
    }


    
    private void logout() {
        dispose();  
        LoginPage loginPage = new LoginPage();
        loginPage.addLoginListener(username -> {
            if (FlightSelectionPage.this.isVisible()) {
                FlightSelectionPage.this.dispose();
            }

            SwingUtilities.invokeLater(() -> new FlightSelectionPage(username));
        });
    }


    private void insertBooking(int bookingId, String username, String flightId) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/flight_booking";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "INSERT INTO bookings (booking_id, user_id, flight_id) VALUES (?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, bookingId);

                try {
                    int userId = getUserIdByUsername(username);
                    preparedStatement.setInt(2, userId);
                } catch (SQLException e) {
                    System.out.println("Error fetching user ID: " + e.getMessage());
                    return; 
                }

                preparedStatement.setString(3, flightId);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Booking Inserted into Database");
                } else {
                    System.out.println("There is an error inserting the booking.");
                }
            }

           
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

  

    private int getUserIdByUsername(String username) throws SQLException {
        String jdbcUrl = "jdbc:mysql://localhost:3306/flight_booking";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "SELECT user_id FROM user WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("user_id");
                    }
                }
            }
        }

        throw new SQLException("User ID not found for username: " + username);
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginPage loginPage = new LoginPage();
            loginPage.addLoginListener(username -> {
                SwingUtilities.invokeLater(() -> new FlightSelectionPage(username));
            });
        });
    }

    private static class Flight {
        private String flightId;
        private String origin;
        private String destination;
        private String date;
        private String time;

        public Flight(String flightId, String origin, String destination, String date, String time) {
            this.flightId = flightId;
            this.origin = origin;
            this.destination = destination;
            this.date = date;
            this.time = time;
        }

        public String getFlightId() {
            return flightId;
        }

        public String getOrigin() {
            return origin;
        }

        public String getDestination() {
            return destination;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        @Override
        public String toString() {
            return destination + " - " + date + " - " + time;
        }
    }
}

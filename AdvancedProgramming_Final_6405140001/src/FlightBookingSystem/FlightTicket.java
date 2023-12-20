package FlightBookingSystem;

import javax.swing.*;

import java.awt.GridLayout;
import java.sql.*;

public class FlightTicket extends JFrame {
    public FlightTicket(int bookingId, String username) {
        String query = "SELECT bookings.booking_id, user.name, flights.flight_id, " +
                "flights.origin, flights.destination, flights.date, flights.time " +
                "FROM bookings " +
                "INNER JOIN user ON bookings.user_id = user.user_id " +
                "INNER JOIN flights ON bookings.flight_id = flights.flight_id " +
                "WHERE bookings.booking_id = ?";
        
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/flight_booking", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, bookingId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int displayedBookingId = resultSet.getInt("booking_id");
                String fullName = resultSet.getString("name");
                String displayedFlightId = resultSet.getString("flight_id");
                String originCountry = resultSet.getString("origin");
                String destinationCountry = resultSet.getString("destination");
                Date date = resultSet.getDate("date");
                Time time = resultSet.getTime("time");

                
                JLabel bookingIdLabel = new JLabel("Booking ID: " + displayedBookingId);
                JLabel fullNameLabel = new JLabel("Full Name: " + fullName);
                JLabel flightIdLabel = new JLabel("Flight ID: " + displayedFlightId);
                JLabel originLabel = new JLabel("Origin: " + originCountry);
                JLabel destinationLabel = new JLabel("Destination: " + destinationCountry);
                JLabel dateLabel = new JLabel("Date: " + date.toString());
                JLabel timeLabel = new JLabel("Time: " + time.toString());

                JPanel ticketPanel = new JPanel(new GridLayout(7, 1, 10, 5));
                ticketPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                ticketPanel.add(new JLabel("Booking ID: " + displayedBookingId));
                ticketPanel.add(new JLabel("Full Name: " + fullName));
                ticketPanel.add(new JLabel("Flight ID: " + displayedFlightId));
                ticketPanel.add(new JLabel("Origin: " + originCountry));
                ticketPanel.add(new JLabel("Destination: " + destinationCountry));
                ticketPanel.add(new JLabel("Date: " + date.toString()));
                ticketPanel.add(new JLabel("Time: " + time.toString()));

                add(ticketPanel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        setTitle("Flight Ticket");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); 
        
    }
}

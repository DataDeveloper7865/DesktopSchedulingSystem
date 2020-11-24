
package SoftwareII.DB;

import static SoftwareII.DB.ConnDB.DB_CONN;
import SoftwareII.Model.Appointment;
import SoftwareII.Model.Customer;
import static SoftwareII.ViewsAndControllers.LoginController.loggedUser;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AppointmentDB {
    private static final ZoneId zoneId = ZoneId.systemDefault();
    
    public static ObservableList<Appointment> getAppointmentsByWeek() {
        ObservableList<Appointment> appointmentsByWeek = FXCollections.observableArrayList();
        String getAppointmentsByWeekSQLStatement = "SELECT customer.*, appointment.* FROM customer "
                + "RIGHT JOIN appointment ON customer.customerId = appointment.customerId "
                + "WHERE start BETWEEN NOW() AND (SELECT ADDDATE(NOW(), INTERVAL 7 DAY))";
        
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(getAppointmentsByWeekSQLStatement);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                Customer selectedCustomer = CustomerDB.getActiveCustomerById(rs.getInt("customerId"));
                Appointment getWeeklyAppointments = new Appointment();
                getWeeklyAppointments.setCustomer(selectedCustomer);
                getWeeklyAppointments.setAppointmentId(rs.getInt("appointmentId"));
                getWeeklyAppointments.setCustomerId(rs.getInt("customerId"));
                getWeeklyAppointments.setUserId(rs.getInt("userId"));
                getWeeklyAppointments.setTitle(rs.getString("title"));
                getWeeklyAppointments.setDescription(rs.getString("description"));
                getWeeklyAppointments.setLocation(rs.getString("location"));
                getWeeklyAppointments.setContact(rs.getString("contact"));
                getWeeklyAppointments.setType(rs.getString("type"));
                getWeeklyAppointments.setUrl(rs.getString("url"));
                
                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = rs.getTimestamp("end").toLocalDateTime();
                ZonedDateTime startLocalTime = ZonedDateTime.ofInstant(startUTC.toInstant(ZoneOffset.UTC), zoneId);
                ZonedDateTime endLocalTime = ZonedDateTime.ofInstant(endUTC.toInstant(ZoneOffset.UTC), zoneId);
                
                getWeeklyAppointments.setStart(startLocalTime);
                getWeeklyAppointments.setEnd(endLocalTime);
                appointmentsByWeek.add(getWeeklyAppointments);
            }
        }
        catch (SQLException e) {
        }
        return appointmentsByWeek;
    }
    public static ObservableList<Appointment> getAppointmentsByMonth() {
        ObservableList<Appointment> appointmentsByMonth = FXCollections.observableArrayList();
        String getApptsByMonthSQLStatement = "SELECT customer.*, appointment.* FROM customer "
                + "RIGHT JOIN appointment ON customer.customerId = appointment.customerId "
                + "WHERE start BETWEEN NOW() AND (SELECT LAST_DAY(NOW()))";
        
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(getApptsByMonthSQLStatement);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                Customer selectedCustomer = CustomerDB.getActiveCustomerById(rs.getInt("customerId"));
                Appointment getMonthlyAppointments = new Appointment();
                getMonthlyAppointments.setCustomer(selectedCustomer);
                getMonthlyAppointments.setAppointmentId(rs.getInt("appointmentId"));
                getMonthlyAppointments.setCustomerId(rs.getInt("customerId"));
                getMonthlyAppointments.setUserId(rs.getInt("userId"));
                getMonthlyAppointments.setTitle(rs.getString("title"));
                getMonthlyAppointments.setDescription(rs.getString("description"));
                getMonthlyAppointments.setLocation(rs.getString("location"));
                getMonthlyAppointments.setContact(rs.getString("contact"));
                getMonthlyAppointments.setType(rs.getString("type"));
                getMonthlyAppointments.setUrl(rs.getString("url"));
                
                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = rs.getTimestamp("end").toLocalDateTime();
                ZonedDateTime startLocalTime = ZonedDateTime.ofInstant(startUTC.toInstant(ZoneOffset.UTC), zoneId);
                ZonedDateTime endLocalTime = ZonedDateTime.ofInstant(endUTC.toInstant(ZoneOffset.UTC), zoneId);
                
                getMonthlyAppointments.setStart(startLocalTime);
                getMonthlyAppointments.setEnd(endLocalTime);
                appointmentsByMonth.add(getMonthlyAppointments);
            }
        }
        catch (SQLException e) {
        }
        return appointmentsByMonth;
    }
    
    public static ObservableList<Appointment> getAppointmentsByUser() {
        ObservableList<Appointment> appointmentsByUser = FXCollections.observableArrayList();
        String getAppointmentsByUserSQLStatement = "SELECT user.userId, customer.customerId, appointment.start FROM user JOIN appointment ON user.userId = appointment.userId JOIN customer ON appointment.customerId = customer.customerId";
        
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(getAppointmentsByUserSQLStatement);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setUserId(rs.getInt("userId"));
                appointment.setCustomerId(rs.getInt("customerId"));
                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                ZonedDateTime startLocal = ZonedDateTime.ofInstant(startUTC.toInstant(ZoneOffset.UTC), zoneId);
                appointment.setStart(startLocal);
                appointmentsByUser.add(appointment);
            }
        }
        catch (SQLException e) {
        }
        return appointmentsByUser;
    }
    
    public Appointment getAppointmentById(int appointmentId) {
        String getAppointmentByIdSQLStatement = "SELECT customer.customerId, customer.customerName, appointment.* FROM customer "
                + "RIGHT JOIN appointment ON customer.customerId = appointment.customerId " 
                + "WHERE appointmentId = ?";
        Appointment getAppointmentById = new Appointment();
        try {
            PreparedStatement stmt = DB_CONN.prepareStatement(getAppointmentByIdSQLStatement);
            stmt.setInt(1, appointmentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Customer selectedCustomer = new Customer();
                selectedCustomer.setCustomerId(rs.getInt("customerId"));
                selectedCustomer.setCustomerName(rs.getString("customerName"));
                getAppointmentById.setCustomer(selectedCustomer);
                getAppointmentById.setCustomerId(rs.getInt("customerId"));
                getAppointmentById.setUserId(rs.getInt("userId"));
                getAppointmentById.setTitle(rs.getString("title"));
                getAppointmentById.setDescription(rs.getString("description"));
                getAppointmentById.setLocation(rs.getString("location"));
                getAppointmentById.setContact(rs.getString("contact"));
                getAppointmentById.setType(rs.getString("type"));
                getAppointmentById.setUrl(rs.getString("url"));
                
                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = rs.getTimestamp("end").toLocalDateTime();
                ZonedDateTime startLocalTime = ZonedDateTime.ofInstant(startUTC.toInstant(ZoneOffset.UTC), zoneId);
                ZonedDateTime endLocalTime = ZonedDateTime.ofInstant(endUTC.toInstant(ZoneOffset.UTC), zoneId);
                
                getAppointmentById.setStart(startLocalTime);
                getAppointmentById.setEnd(endLocalTime);
            }
        }
        catch (SQLException e) {
        }
        return getAppointmentById;
    }
    
    public static Appointment getUpcomingAppointment15min() {

        
        String getUpcomingAppointmentSQLStatement = "SELECT customer.customerName, appointment.* FROM appointment "
                + "JOIN customer ON appointment.customerId = customer.customerId "
                + "WHERE (start BETWEEN ? AND ADDTIME(?, '00:15:00'))";
        
        Appointment upcomingAppointment = new Appointment();
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(getUpcomingAppointmentSQLStatement);
            ZonedDateTime localZonedTime = ZonedDateTime.now(zoneId);
            ZonedDateTime zonedDateTimeUTC = localZonedTime.withZoneSameInstant(ZoneId.of("UTC"));
            LocalDateTime localUTC = zonedDateTimeUTC.toLocalDateTime();
            statement.setTimestamp(1, Timestamp.valueOf(localUTC));
            statement.setTimestamp(2, Timestamp.valueOf(localUTC));
            
            //edit
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerName(rs.getString("customerName"));
                upcomingAppointment.setCustomer(customer);
                upcomingAppointment.setAppointmentId(rs.getInt("appointmentId"));
                upcomingAppointment.setCustomerId(rs.getInt("customerId"));
                upcomingAppointment.setUserId(rs.getInt("userId"));
                upcomingAppointment.setTitle(rs.getString("title"));
                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                ZonedDateTime startZDT = ZonedDateTime.ofInstant(startUTC.toInstant(ZoneOffset.UTC), zoneId);
                upcomingAppointment.setStart(startZDT);
            }
        }
        catch (SQLException e) {
        }
        return upcomingAppointment;
    }
    
    public static ObservableList<Appointment> getConflictingAppointments(LocalDateTime start, LocalDateTime end) {
        ObservableList<Appointment> getOverlappingAppointments = FXCollections.observableArrayList();
        String getOverlappingAppointmentsSQLStatement = "SELECT * FROM appointment " 
                + "WHERE (start >= ? AND end <= ?) "
                + "OR (start <= ? AND end >= ?) "
                + "OR (start BETWEEN ? AND ? OR end BETWEEN ? AND ?)";
        
        try {
            LocalDateTime startLDT = start.atZone(zoneId).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            LocalDateTime endLDT = end.atZone(zoneId).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            PreparedStatement statement = DB_CONN.prepareStatement(getOverlappingAppointmentsSQLStatement);
            statement.setTimestamp(1, Timestamp.valueOf(startLDT));
            statement.setTimestamp(2, Timestamp.valueOf(endLDT));
            statement.setTimestamp(3, Timestamp.valueOf(startLDT));
            statement.setTimestamp(4, Timestamp.valueOf(endLDT));
            statement.setTimestamp(5, Timestamp.valueOf(startLDT));
            statement.setTimestamp(6, Timestamp.valueOf(endLDT));
            statement.setTimestamp(7, Timestamp.valueOf(startLDT));
            statement.setTimestamp(8, Timestamp.valueOf(endLDT));
            ResultSet rs = statement.executeQuery();
            
                while (rs.next()) {
                    Appointment overlappingAppointment = new Appointment();
                    overlappingAppointment.setAppointmentId(rs.getInt("appointmentId"));
                    overlappingAppointment.setTitle(rs.getString("title"));
                    overlappingAppointment.setDescription(rs.getString("description"));
                    overlappingAppointment.setLocation(rs.getString("location"));
                    overlappingAppointment.setContact(rs.getString("contact"));
                    overlappingAppointment.setType(rs.getString("type"));
                    overlappingAppointment.setUrl(rs.getString("url"));
                    LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                    LocalDateTime endUTC = rs.getTimestamp("end").toLocalDateTime();
                    ZonedDateTime startLocal = ZonedDateTime.ofInstant(startUTC.toInstant(ZoneOffset.UTC), zoneId);
                    ZonedDateTime endLocal = ZonedDateTime.ofInstant(endUTC.toInstant(ZoneOffset.UTC), zoneId);
                    overlappingAppointment.setStart(startLocal);
                    overlappingAppointment.setEnd(endLocal);
                    getOverlappingAppointments.add(overlappingAppointment);
                }
        }
        catch (SQLException e) {
        }
        return getOverlappingAppointments;
    } 
    
    private static int getMaxAppointmentFromAppointment() {
        int maximumAppointment = 0;
        String maxAppointmentSQLStatement = "SELECT MAX(appointmentId) FROM appointment";
        
        try {
            Statement statement = DB_CONN.createStatement();
            ResultSet rs = statement.executeQuery(maxAppointmentSQLStatement);
            
            if (rs.next()) {
                maximumAppointment = rs.getInt(1);
            }
        }
        catch (SQLException e) {
        }
        return maximumAppointment + 1;
    }

    public static Appointment addAppointmentToDatabase(Appointment appointment) {
        String addAppointmentSQLStatement = String.join(" ",
                "INSERT INTO appointment (appointmentId, customerId, userId, title, "
                            + "description, location, contact, type, url, start, end, "
                            + "createDate, createdBy, lastUpdate, lastUpdateBy) ",
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?)");
        
        int incrementedAppointmentId = getMaxAppointmentFromAppointment();
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(addAppointmentSQLStatement);
            statement.setInt(1, incrementedAppointmentId);
            statement.setObject(2, appointment.getCustomerId());
            statement.setObject(3, appointment.getUserId());
            statement.setObject(4, appointment.getTitle());
            statement.setObject(5, appointment.getDescription());
            statement.setObject(6, appointment.getLocation());
            statement.setObject(7, appointment.getContact());
            statement.setObject(8, appointment.getType());
            statement.setObject(9, appointment.getUrl());
            
            ZonedDateTime startZDT = appointment.getStart().withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime endZDT = appointment.getEnd().withZoneSameInstant(ZoneId.of("UTC"));
            statement.setTimestamp(10, Timestamp.valueOf(startZDT.toLocalDateTime()));
            statement.setTimestamp(11, Timestamp.valueOf(endZDT.toLocalDateTime()));
            
            statement.setString(12, loggedUser.getUserName());
            statement.setString(13, loggedUser.getUserName());
            statement.executeUpdate();
        }
        catch (SQLException e){
        }
        return appointment;
    }
    public static void updateAppointmentInDatabase(Appointment appointment) {
        String updateAppointmentSQLStatement = String.join(" ",
                "UPDATE appointment",
                "SET customerId=?, userId=?, title=?, description=?, location=?," +
                "contact=?, type=?, url=?, start=?, end=?, lastUpdate=NOW(), lastUpdateBy=?",
                "WHERE appointmentId=?");
        
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(updateAppointmentSQLStatement);
            statement.setObject(1, appointment.getCustomerId());
            statement.setObject(2, appointment.getUserId());
            statement.setObject(3, appointment.getTitle());
            statement.setObject(4, appointment.getDescription());
            statement.setObject(5, appointment.getLocation());
            statement.setObject(6, appointment.getContact());
            statement.setObject(7, appointment.getType());
            statement.setObject(8, appointment.getUrl());
            
            ZonedDateTime startZDT = appointment.getStart().withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime endZDT = appointment.getEnd().withZoneSameInstant(ZoneId.of("UTC"));
            statement.setTimestamp(9, Timestamp.valueOf(startZDT.toLocalDateTime()));
            statement.setTimestamp(10, Timestamp.valueOf(endZDT.toLocalDateTime()));
            
            statement.setString(11, loggedUser.getUserName());
            statement.setObject(12, appointment.getAppointmentId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
        }
    }
    public static void deleteAppointmentFromDatabase(Appointment appointment) {
        String deleteAppointmentSQLstatement = "DELETE FROM appointment WHERE appointmentId = ?";
        
        try {
            PreparedStatement statement = DB_CONN.prepareStatement(deleteAppointmentSQLstatement);
            statement.setObject(1, appointment.getAppointmentId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
        }
    }
}

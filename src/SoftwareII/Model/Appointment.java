
package SoftwareII.Model;

import SoftwareII.DB.AppointmentDB;
import SoftwareII.Exceptions.AppointmentClassException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Appointment {
    private final IntegerProperty appointmentId = new SimpleIntegerProperty();
    private final IntegerProperty customerId = new SimpleIntegerProperty();
    private final IntegerProperty userId = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty location = new SimpleStringProperty();
    private final StringProperty contact = new SimpleStringProperty();
    private final StringProperty type = new SimpleStringProperty();
    private final StringProperty url = new SimpleStringProperty();
    private ZonedDateTime start;
    private ZonedDateTime end;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    private Customer customer = new Customer();
    
    public Appointment() {
    }

    public final int getAppointmentId() {
        return appointmentId.get();
    }

    public final void setAppointmentId(int appointmentId) {
        this.appointmentId.set(appointmentId);
    }
    
    public IntegerProperty appointmentIdProperty() {
        return appointmentId;
    }

    public final int getCustomerId() {
        return customerId.get();
    }

    public final void setCustomerId(int customerId) {
        this.customerId.set(customerId);
    }
    
    public IntegerProperty customerIdProperty() {
        return customerId;
    }

    public final int getUserId() {
        return userId.get();
    }

    public final void setUserId(int userId) {
        this.userId.set(userId);
    }
    
    public IntegerProperty userIdProperty() {
        return userId;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }
    
    public StringProperty titleProperty() {
        return title;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
    
    public StringProperty descriptionProperty() {
        return description;
    }

    public String getLocation() {
        return location.get();
    }

    public void setLocation(String location) {
        this.location.set(location);
    }
    
    public StringProperty locationProperty() {
        return location;
    }

    public String getContact() {
        return contact.get();
    }

    public void setContact(String contact) {
        this.contact.set(contact);
    }
    
    public StringProperty contactProperty() {
        return contact;
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }
    
    public StringProperty typeProperty() {
        return type;
    }

    public String getUrl() {
        return url.get();
    }

    public void setUrl(String url) {
        this.url.set(url);
    }
    
    public StringProperty urlProperty() {
        return url;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public boolean inputCheck() throws AppointmentClassException {
        if (this.customer == null) {
            throw new AppointmentClassException("Please fix customer");
        }
        if (this.title.get().equals("")) {
            throw new AppointmentClassException("Please fix title");
        }
        if (this.description.get().equals("")) {
            throw new AppointmentClassException("Please fix description");
        }
        if (this.location.get().equals("")) {
            throw new AppointmentClassException("Please fix location");
        }
        if (this.contact.get().equals("")) {
            throw new AppointmentClassException("Please fix contact");
        }
        if (this.type.get().equals("")) {
            throw new AppointmentClassException("Please fix type");
        }
        if (this.url.get().equals("")) {
            throw new AppointmentClassException("Please fix url");
        }
        timeCheck();
        return true;
    }
    
    public boolean timeCheck() throws AppointmentClassException {
        LocalTime midnightLocalTime = LocalTime.MIDNIGHT;
        LocalDate appointmentStartDate = this.start.toLocalDate();
        LocalTime appointmentStartTime = this.start.toLocalTime();
        LocalDate appointmentEndDate = this.end.toLocalDate();
        LocalTime appointmentEndTime = this.end.toLocalTime();
        int weekDay = appointmentStartDate.getDayOfWeek().getValue();
        
        if (!appointmentStartDate.isEqual(appointmentEndDate)) {
            throw new AppointmentClassException("Appointments can only run 1 day");
        }
        if (weekDay == 6 || weekDay == 7) {
            throw new AppointmentClassException("Do not schedule appointment on weekend");
        }
        if (appointmentStartTime.isBefore(midnightLocalTime.plusHours(8))) {
            throw new AppointmentClassException("Offices are not open. Please use business hours.");
        }
        if (appointmentEndTime.isAfter(midnightLocalTime.plusHours(17))) {
            throw new AppointmentClassException("Offices are not open. Please use business hours.");
        }
        if (appointmentStartDate.isBefore(LocalDate.now()) || appointmentStartTime.isBefore(LocalTime.MIDNIGHT)) {
            throw new AppointmentClassException("Cannot schedule in the past. Choose another date.");
        }
        return true;
    }
    
    
    public boolean noConcurrentAppointment() throws AppointmentClassException {
        ObservableList<Appointment> overlappingAppt = AppointmentDB.getConflictingAppointments(this.start.toLocalDateTime(), this.end.toLocalDateTime());
        if (overlappingAppt.size() > 1) {
            throw new AppointmentClassException("The apppointment slot is already taken. Select another.");
        }
        return true;
    }
}

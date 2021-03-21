package scheduler.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.State;
import scheduler.model.Appointment;
import scheduler.util.TimeUtil;

import java.sql.*;

public class AppointmentDao implements DAO<Appointment> {
    /**
     * Fetches a list of all Appointments from the SQL database.
     *
     * @return an ObservableList populated with all Appointments.
     */
    @Override
    public ObservableList<Appointment> listAll() {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        PreparedStatement ps;
        ResultSet rs;
        String rawSQL = "select * from appointments as a join contacts as c on a.Contact_ID = c.Contact_ID;";

        try {
            Connection c = State.getDBConnection();
            ps = c.prepareStatement(rawSQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                allAppointments.add(
                        new Appointment(
                                rs.getInt("Appointment_ID"),
                                rs.getString("Title"),
                                rs.getString("Description"),
                                rs.getString("Location"),
                                rs.getString("Type"),
                                TimeUtil.tsToLocal(rs.getTimestamp("Start")),
                                TimeUtil.tsToLocal(rs.getTimestamp("End")),
                                rs.getString("Created_By") != null ? rs.getString("Created_By") : "",
                                rs.getString("Last_Updated_By") != null ? rs.getString("Last_Updated_By") : "",
                                rs.getInt("Customer_ID"),
                                rs.getInt("User_ID"),
                                rs.getInt("Contact_ID"),
                                rs.getString("Contact_Name")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allAppointments;
    }

    /**
     * Look up a Appointment from the SQL database.
     *
     * @param appointment a Appointment object that includes an ID that we can lookup.
     * @return a populated Appointment object if it exists in the database, otherwise an empty Appointment object.
     */
    @Override
    public Appointment find(Appointment appointment) {
        Appointment appointmentResult = new Appointment();
        PreparedStatement ps;
        ResultSet rs;
        String rawSQL = "select * from appointments as a " +
                "join contacts as c on a.Contact_ID = c.Contact_ID where Appointment_ID = ?;";

        try {
            Connection c = State.getDBConnection();
            ps = c.prepareStatement(rawSQL);
            ps.setInt(1, appointment.getID());

            rs = ps.executeQuery();
            if (rs.next()) {
                appointmentResult = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        TimeUtil.tsToLocal(rs.getTimestamp("Start")),
                        TimeUtil.tsToLocal(rs.getTimestamp("End")),
                        rs.getString("Created_By") != null ? rs.getString("Created_By") : "",
                        rs.getString("Last_Updated_By") != null ? rs.getString("Last_Updated_By") : "",
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID"),
                        rs.getString("Contact_Name")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointmentResult;
    }

    /**
     * Updates a Appointment's information.
     *
     * @param appointment the updated Appointment object.
     */
    @Override
    public void update(Appointment appointment) {
        PreparedStatement ps;
        String rawSQL = "update appointments " +
                "set Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?," +
                "Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? " +
                "where Appointment_ID = ?;";
        try {
            Connection c = State.getDBConnection();
            ps = c.prepareStatement(rawSQL);
            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType());
            ps.setTimestamp(5, TimeUtil.localToTS(appointment.getStart()));
            ps.setTimestamp(6, TimeUtil.localToTS(appointment.getEnd()));
            ps.setTimestamp(7, Timestamp.valueOf(TimeUtil.utcNow()));
            ps.setString(8, appointment.getLastUpdatedBy());
            if (appointment.getCustomerID() > 0)
                ps.setInt(9, appointment.getCustomerID());
            else
                ps.setNull(9, 0);
            if (appointment.getUserID() > 0)
                ps.setInt(10, appointment.getUserID());
            else
                ps.setNull(10, 0);
            if (appointment.getContactID() > 0)
                ps.setInt(11, appointment.getContactID());
            else
                ps.setNull(11, 0);
            ps.setInt(12, appointment.getID());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a Appointment from the SQL database.
     *
     * @param appointment the Appointment object to delete.
     */
    @Override
    public void delete(Appointment appointment) {
        PreparedStatement ps;
        String rawSQL = "delete from appointments where Appointment_ID = ?;";

        try {
            Connection c = State.getDBConnection();
            ps = c.prepareStatement(rawSQL);
            ps.setInt(1, appointment.getID());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a new Appointment into the SQL database.
     *
     * @param appointment the Appointment object to insert.
     */
    @Override
    public void add(Appointment appointment) {
        PreparedStatement ps;
        String rawSQL = "insert into appointments" +
                "(Title, Description, Location, Type, Start, End, Created_By, Last_Updated_By, Customer_ID, User_ID, Contact_ID)" +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection c = State.getDBConnection();
            ps = c.prepareStatement(rawSQL);
            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType());
            ps.setTimestamp(5, TimeUtil.localToTS(appointment.getStart()));
            ps.setTimestamp(6, TimeUtil.localToTS(appointment.getEnd()));
            ps.setString(7, appointment.getCreatedBy());
            ps.setString(8, appointment.getLastUpdatedBy());
            if (appointment.getCustomerID() > 0)
                ps.setInt(9, appointment.getCustomerID());
            else
                ps.setNull(9, 0);
            if (appointment.getUserID() > 0)
                ps.setInt(10, appointment.getUserID());
            else
                ps.setNull(10, 0);
            if (appointment.getContactID() > 0)
                ps.setInt(11, appointment.getContactID());
            else
                ps.setNull(11, 0);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

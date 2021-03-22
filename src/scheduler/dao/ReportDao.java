package scheduler.dao;

import scheduler.State;
import scheduler.util.TimeUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;

public class ReportDao {
    public String getTotalAppointments() {
        PreparedStatement ps;
        ResultSet rs;
        String rawSQL = "select Type, month(Start) as month, count(*) as total from appointments " +
                "group by Type,month(Start) order by month;";
        StringBuilder report = new StringBuilder();
        report.append("Total Appointments of Each Type by Month\n");
        int iterMonth = 0;

        try {
            Connection c = State.getDBConnection();
            ps = c.prepareStatement(rawSQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getInt("month") > iterMonth) {
                    report.append(String.format("\n%s\n\n", Month.of(rs.getInt("month"))));
                    iterMonth = rs.getInt("month");
                }
                report.append(String.format("%s: %d\n", rs.getString("Type"), rs.getInt("total")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report.toString();
    }

    public String getSchedulesByContact() {
        PreparedStatement ps;
        ResultSet rs;
        String rawSQL = "select * from appointments as a " +
                "join contacts as c on a.Contact_ID = c.Contact_ID order by a.Contact_ID;";
        StringBuilder report = new StringBuilder();
        report.append("Appointment Schedules for Each Contact\n");
        int iterContact = 0;

        try {
            Connection c = State.getDBConnection();
            ps = c.prepareStatement(rawSQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getInt("Contact_ID") > iterContact) {
                    report.append(String.format("\n%s's Appointments:\n\n", rs.getString("Contact_Name")).toUpperCase());
                    iterContact = rs.getInt("Contact_ID");
                }
                report.append(String.format("Appointment #%d:\n  Title: %s\n  Type: %s\n  Description: %s\n" +
                                "  Start Time: %s\n  End Time: %s\n  Customer ID: %d\n",
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Type"),
                        rs.getString("Description"),
                        TimeUtil.tsToLocal(rs.getTimestamp("Start")),
                        TimeUtil.tsToLocal(rs.getTimestamp("End")),
                        rs.getInt("Customer_ID")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report.toString();
    }

    public String getCustomerCountries() {
        PreparedStatement ps;
        ResultSet rs;
        String rawSQL = "select Customer_ID, Customer_Name, t.Country_ID, Country from customers as c " +
                "join first_level_divisions as f on c.Division_ID = f.Division_ID " +
                "join countries as t on f.COUNTRY_ID = t.Country_ID order by t.Country_ID;";
        StringBuilder report = new StringBuilder();
        report.append("Customers By Country\n");
        int iterCountry = 0;

        try {
            Connection c = State.getDBConnection();
            ps = c.prepareStatement(rawSQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getInt("Country_ID") > iterCountry) {
                    report.append(String.format("\n%s\n\n", rs.getString("Country").toUpperCase()));
                    iterCountry = rs.getInt("Country_ID");
                }
                report.append(String.format("#%d, %s\n", rs.getInt("Customer_ID"), rs.getString("Customer_Name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report.toString();
    }
}

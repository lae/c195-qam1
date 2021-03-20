package scheduler.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.DBConnection;
import scheduler.model.FirstLevelDivision;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FirstLevelDivisionDao implements DAO<FirstLevelDivision> {
    /**
     * Fetches a list of all FirstLevelDivisions from the SQL database.
     *
     * @return an ObservableList populated with all FirstLevelDivisions.
     */
    @Override
    public ObservableList<FirstLevelDivision> listAll() {
        ObservableList<FirstLevelDivision> allFLDs = FXCollections.observableArrayList();
        PreparedStatement ps;
        ResultSet rs;
        String rawSQL = "select * from first_level_divisions;";

        try {
            Connection c = DBConnection.get();
            ps = c.prepareStatement(rawSQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                allFLDs.add(
                        new FirstLevelDivision(
                                rs.getInt("Division_ID"),
                                rs.getString("Division"),
                                rs.getInt("COUNTRY_ID")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allFLDs;
    }

    /**
     * Look up a FirstLevelDivision from the SQL database.
     *
     * @param firstLevelDivision a FirstLevelDivision object that includes an ID that we can lookup.
     * @return a populated FirstLevelDivision object if it exists in the database, otherwise an empty FirstLevelDivision object.
     */
    @Override
    public FirstLevelDivision find(FirstLevelDivision firstLevelDivision) {
        FirstLevelDivision firstLevelDivisionResult = new FirstLevelDivision();
        PreparedStatement ps;
        ResultSet rs;
        String rawSQL = "select * from first_level_divisions where FirstLevelDivision_ID = ?;";

        try {
            Connection c = DBConnection.get();
            ps = c.prepareStatement(rawSQL);
            ps.setInt(1, firstLevelDivision.getID());

            rs = ps.executeQuery();
            if (rs.next()) {
                firstLevelDivisionResult = new FirstLevelDivision(
                        rs.getInt("Division_ID"),
                        rs.getString("Division"),
                        rs.getInt("COUNTRY_ID")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return firstLevelDivisionResult;
    }

    /**
     * Deletes a FirstLevelDivision from the SQL database.
     *
     * @param firstLevelDivision the FirstLevelDivision object to delete.
     */
    @Override
    public void delete(FirstLevelDivision firstLevelDivision) {
        PreparedStatement ps;
        String rawSQL = "delete from first_level_divisions where Division_ID = ?;";

        try {
            Connection c = DBConnection.get();
            ps = c.prepareStatement(rawSQL);
            ps.setInt(1, firstLevelDivision.getID());

            int res = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(FirstLevelDivision firstLevelDivision) {
        // do nothing.
    }

    @Override
    public void update(FirstLevelDivision firstLevelDivision) {
        // do nothing.
    }
}

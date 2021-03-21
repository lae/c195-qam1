package scheduler.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.State;
import scheduler.model.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryDao implements DAO<Country> {
    /**
     * Fetches a list of all Countries from the SQL database.
     *
     * @return an ObservableList populated with all Countries.
     */
    @Override
    public ObservableList<Country> listAll() {
        ObservableList<Country> allFLDs = FXCollections.observableArrayList();
        PreparedStatement ps;
        ResultSet rs;
        String rawSQL = "select * from countries;";

        try {
            Connection c = State.getDBConnection();
            ps = c.prepareStatement(rawSQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                allFLDs.add(
                        new Country(
                                rs.getInt("Country_ID"),
                                rs.getString("Country")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allFLDs;
    }

    /**
     * Fetches a list of all Countries that have associated First Level Divisions from the SQL database.
     *
     * @return an ObservableList populated with all usable Countries.
     */
    public ObservableList<Country> listUsable() {
        ObservableList<Country> allFLDs = FXCollections.observableArrayList();
        PreparedStatement ps;
        ResultSet rs;
        String rawSQL = "select distinct countries.* from countries join first_level_divisions on countries.Country_ID = first_level_divisions.COUNTRY_ID;";

        try {
            Connection c = State.getDBConnection();
            ps = c.prepareStatement(rawSQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                allFLDs.add(
                        new Country(
                                rs.getInt("Country_ID"),
                                rs.getString("Country")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allFLDs;
    }

    /**
     * Look up a Country from the SQL database.
     *
     * @param country a Country object that includes an ID that we can lookup.
     * @return a populated Country object if it exists in the database, otherwise an empty Country object.
     */
    @Override
    public Country find(Country country) {
        Country countryResult = new Country();
        PreparedStatement ps;
        ResultSet rs;
        String rawSQL = "select * from countries where Country_ID = ?;";

        try {
            Connection c = State.getDBConnection();
            ps = c.prepareStatement(rawSQL);
            ps.setInt(1, country.getID());

            rs = ps.executeQuery();
            if (rs.next()) {
                countryResult = new Country(
                        rs.getInt("Country_ID"),
                        rs.getString("Country")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return countryResult;
    }

    @Override
    public void update(Country country) {
        // do nothing.
    }

    /**
     * Deletes a Country from the SQL database.
     *
     * @param country the Country object to delete.
     */
    @Override
    public void delete(Country country) {
        PreparedStatement ps;
        String rawSQL = "delete from countries where Division_ID = ?;";

        try {
            Connection c = State.getDBConnection();
            ps = c.prepareStatement(rawSQL);
            ps.setInt(1, country.getID());

            int res = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Country country) {
        // do nothing.
    }
}

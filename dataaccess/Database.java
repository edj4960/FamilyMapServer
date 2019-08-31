package cs240.evanjones.server.dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private Connection conn;

    static {
        try {
            // Set up the driver for database
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println("Could not init JDBC driver - driver not found");
            e.printStackTrace();
        }
    }

    /**
     * Creates connection to database
     * @return connection object to be used by DAO's
     * @throws DataAccessException
     */
    public Connection openConnection() throws DataAccessException {
        try {
            final String CONNECTION_URL = "jdbc:sqlite:FMSDatabase.db";
            conn = DriverManager.getConnection(CONNECTION_URL);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }
        return conn;
    }

    /**
     * Closes connection with database
     * @param commit
     * @throws DataAccessException
     */
    public void closeConnection(boolean commit) throws DataAccessException {
        try {
            if (commit)
                conn.commit();
            else
                conn.rollback();

            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    /**
     * Creates tables in database
     * @throws DataAccessException
     */
    public void createTables() throws DataAccessException {
        openConnection();

        try (Statement stmt = conn.createStatement()){
            String sql = "CREATE TABLE IF NOT EXISTS Persons ( " +
                    "PersonID text not null unique, " +
                    "Descendant text not null, " +
                    "FirstName text not null, " +
                    "LastName text not null, " +
                    "Gender text not null check(Gender = 'f' or Gender = 'm'), " +
                    "Father text, " +
                    "Mother text, " +
                    "Spouse text, " +
                    "primary key(PersonID)" +
                    "); " +

                    "CREATE TABLE IF NOT EXISTS Users ( " +
                    "Username TEXT NOT NULL UNIQUE, " +
                    "Password TEXT NOT NULL, " +
                    "Email TEXT NOT NULL, " +
                    "FirstName TEXT NOT NULL, " +
                    "LastName TEXT NOT NULL, " +
                    "Gender TEXT NOT NULL CHECK(Gender = 'f' OR Gender = 'm'), " +
                    "PersonID TEXT NOT NULL, " +
                    "PRIMARY KEY(Username)" +
                    "); " +

                    "CREATE TABLE IF NOT EXISTS Events ( " +
                    "EventID TEXT NOT NULL UNIQUE, " +
                    "Descendant Text NOT NULL, " +
                    "PersonID Text NOT NULL, " +
                    "Latitude REAL, " +
                    "Longitude REAL, " +
                    "Country TEXT, " +
                    "City TEXT, " +
                    "EventType TEXT, " +
                    "Year INTEGER, " +
                    "PRIMARY KEY(EventID)" +
                    "); " +

                    "CREATE TABLE IF NOT EXISTS Authorization ( " +
                    "AuthToken TEXT NOT NULL UNIQUE, " +
                    "Username Text NOT NULL, " +
                    "PersonID Text NOT NULL, " +
                    "PRIMARY KEY(AuthToken)" +
                    ")";

            stmt.executeUpdate(sql);
            closeConnection(true);
        } catch (DataAccessException e) {
            closeConnection(false);
            throw e;
        } catch (SQLException e) {
            closeConnection(false);
            throw new DataAccessException("SQL Error encountered while creating tables");
        }
    }

    /**
     * Clears all tables in database
     * @throws DataAccessException
     */
    public void clearTables() throws DataAccessException {
        openConnection();

        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM Persons";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM Users";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM Events";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM Authorization";
            stmt.executeUpdate(sql);
            closeConnection(true);
        } catch (DataAccessException e) {
            closeConnection(false);
            throw e;
        } catch (SQLException e) {
            closeConnection(false);
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}
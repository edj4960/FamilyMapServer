package cs240.evanjones.server.dataaccess;

import java.sql.*;
import java.util.ArrayList;

import cs240.evanjones.server.model.Person;

/** Interacts with the database to add, get, or remove persons */
public class PersonDAO {
    private Connection conn;

    /** Creation of a new personDAO object */
    public PersonDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Adds person to the database
     * @param person person to be added
     * @return true or false
     */
    public boolean addPerson(Person person) throws DataAccessException {
        boolean commit = true;
        String sql = "INSERT INTO Persons (PersonID, Descendant, FirstName, LastName, Gender," +
                " Father, Mother, Spouse) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person.getPersonId());
            stmt.setString(2, person.getDescendant());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFather());
            stmt.setString(7, person.getMother());
            stmt.setString(8, person.getSpouse());

            stmt.executeUpdate();
        } catch (SQLException e) {
            commit = false;
            throw new DataAccessException("Error encountered while inserting into the database");
        }
        return commit;
    }

    /**
     * Looks up a person from the database using the getPersonId
     * @param personID ID of person of interest
     * @return the found person object
     */
    public Person getPerson(String personID) throws DataAccessException {
        Person person;
        ResultSet rs;
        String sql = "SELECT * FROM Persons WHERE PersonID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next() == true) {
                person = new Person(rs.getString("PersonID"), rs.getString("Descendant"),
                        rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Gender"));
                person.setFather(rs.getString("Father"));
                person.setMother(rs.getString("Mother"));
                person.setSpouse(rs.getString("Spouse"));
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
        }
        return null;
    }

    /**
     * Gets all persons from the database  for a particular user
     * @param username of person who person objects are being grabbed
     * @return the found person object
     */
    public ArrayList<Person> getUserPersons(String username) throws DataAccessException {
        ArrayList<Person> personArray = new ArrayList<>();
        Person person = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE Descendant = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while(rs.next() == true) {
                person = new Person(rs.getString("PersonID"), rs.getString("Descendant"),
                        rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Gender"));
                person.setFather(rs.getString("Father"));
                person.setMother(rs.getString("Mother"));
                person.setSpouse(rs.getString("Spouse"));
                personArray.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
        }
        return personArray;
    }

    /**
     * deletes a specific person from database
     * @param username of person to be deleted
     */
    public void removeUserPersons(String username) throws Exception {
        //Executing delete
        String sql = "Delete FROM Persons WHERE Descendant = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting person");
        }
    }

    /** deletes all persons from database */
    public void removeAllPersons() throws Exception {
        String sql = "Delete From Persons;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting all persons");
        }
    }
}

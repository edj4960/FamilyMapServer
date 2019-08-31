package cs240.evanjones.server.model;

/**
* A unique authorization token created when a user is able to log in.
* Connected to a Person, allows for multiple tokens per userName
*/
public class AuthToken
{
    /**Unique Auth Token*/
    private String token;
    /**Uniques User Name*/
    private String userName;
    /**Unique Person ID */
    private String personID;

    public AuthToken(String token, String username, String personID) {
        this.token = token;
        this.userName = username;
        this.personID = personID;
    }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public String getPersonID() { return personID; }

    public void setPersonID(String personID) { this.personID = personID; }
}

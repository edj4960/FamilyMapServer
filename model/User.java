package cs240.evanjones.server.model;

/**A Unique User*/
public class User {
  /**User Name*/
  private String userName;
  /**Password of User*/
  private String password;
  /**Email Address of User*/
  private String email;
  /**First Name of User*/
  private String firstName;
  /**Last Name of User*/
  private String lastName;
  /**Gender of User*/
  private String gender;
  /**Unique Person ID*/
  private String personID;

  /**
   * Creates a new user account,
   *
   * @param userName  User Name
   * @param password  Password
   * @param email     Email
   * @param firstName First Name
   * @param lastName  Last Name
   * @param gender    Gender
   * @param personID  Person ID
   */
  public User(String userName, String password, String email, String firstName,
              String lastName, String gender, String personID) {
    this.userName = userName;
    this.password = password;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
    this.personID = personID;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = this.gender;
  }

  public String getPersonID() {
    return personID;
  }

  public void setPersonID(String personID) {
    this.personID = personID;
  }
}

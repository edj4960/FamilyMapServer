package cs240.evanjones.server.requests;

/**Request to register user*/
public class RegisterRequest
{
  /**User name (non-empty)*/
  private String userName;
  /**Password of User (non-empty)*/
  private String password;
  /**Email Address of User (non-empty)*/
  private String email;
  /**First Name of User (non-empty)*/
  private String firstName;
  /**Last Name of User (non-empty)*/
  private String lastName;
  /**Gender of User (Male or Female)*/
  private String gender;

  /**Creates register request with all fields*/
  public RegisterRequest(String userName, String password, String email,
                         String firstName, String lastName, String gender) {
    this.userName = userName;
    this.password = password;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {this.userName = userName;}

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {this.password = password;}

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {this.email = email;}

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName){this.firstName = firstName;}

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {this.lastName = lastName;}

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {this.gender = gender;}
}

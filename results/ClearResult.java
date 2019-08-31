package cs240.evanjones.server.results;

/**Holds result of a Clear Service*/
public class ClearResult
{
  /** Message to give statues of clearing */
  private String message;

  /** Constructor of Clear Result object */
  public ClearResult(){ }

  public String getMessage() {return message;}

  public void setMessage(String message) {this.message = message;}
}

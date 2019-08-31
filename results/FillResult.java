package cs240.evanjones.server.results;

/**Holds result of an Fill Service*/
public class FillResult
{
  /** Message to be given */
  private String message;

  /**Constructor for a fillResult object*/
  public  FillResult() {}

  public String getMessage() {return message;}

  public void setMessage(String message) {this.message = message;}
}

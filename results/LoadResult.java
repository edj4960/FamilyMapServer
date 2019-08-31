package cs240.evanjones.server.results;

/**Holds result of an Load Service*/
public class LoadResult
{
  /** Message to return on outcome */
  private String message;

  /** Constructor of a LoadRequest object */
  public LoadResult() {}

  public String getMessage() {return message;}

  public void setMessage(String message) {this.message = message;}
}
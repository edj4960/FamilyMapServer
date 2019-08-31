package cs240.evanjones.server.requests;

/** a fill request to populate the database with data for a specific user */
public class FillRequest {
    /** Username for family data to be created */
    private String userName;
    /** Number of generations to generate */
    private Integer generations = 0;

    /** Constructor for the fillRequest object */
    public FillRequest() {}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {this.userName = userName;}

    public Integer getGenerations() { return generations; }

    public void setGenerations(Integer generations) {this.generations = generations;}
}

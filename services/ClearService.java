package cs240.evanjones.server.services;

import cs240.evanjones.server.dataaccess.Database;
import cs240.evanjones.server.results.ClearResult;

/** A service class for clearing the database */
public class ClearService {

  /** Constructor of clear service object */
  public ClearService() {}

  /**
  * Clears the database fully
  * @return ClearResult object to report the result
  */
  public ClearResult empty() throws Exception {
    Database db = new Database();
    db.createTables();
    db.clearTables();
    ClearResult result = new ClearResult();
    result.setMessage("Clear Successful");
    return result;
  }
}

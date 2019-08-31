package cs240.evanjones.server.handler;

import com.sun.net.httpserver.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import cs240.evanjones.server.model.Person;
import cs240.evanjones.server.requests.PersonRequest;
import cs240.evanjones.server.results.PersonResult;
import cs240.evanjones.server.services.PersonService;
import cs240.evanjones.server.util.Serializer;
import cs240.evanjones.server.util.StreamUtil;

/**
 * Handles the finding of persons in the database.
 */
class PersonHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                // Check to see if an "Authorization" header is present
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");
                    // Grab Request
                    String url = exchange.getRequestURI().getPath();
                    String personID = "";
                    int slashesFound = 0;
                    for (int i = 0; i < url.length(); i++) {
                        if (url.charAt(i) == '/')
                            slashesFound++;
                        else if (slashesFound == 2)
                            personID += url.charAt(i);
                    }
                    // Carry out request
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    PersonRequest request = new PersonRequest();
                    request.setPersonID(personID);
                    request.setAuthToken(authToken);
                    PersonService service = new PersonService(request);
                    PersonResult result;
                    if(request.getPersonID() == "")
                        result = service.getUserPersons();
                    else
                        result = service.getPerson();
                    // Send Result
                    String respData = "";
                    // Check if service failed
                    if(result.getMessage() != null) {
                        LinkedHashMap<String, String> map = new LinkedHashMap<>();
                        map.put("message", result.getMessage());
                         respData = Serializer.serialize(map);
                    }
                    else {
                        ArrayList<Person> persons = result.getPersonData();
                        // Check array size
                        if(persons.size() == 1) {
                            Person person = persons.get(0);
                            LinkedHashMap<String, String> map = new LinkedHashMap<>();
                            map.put("descendant", person.getDescendant());
                            map.put("personID", person.getPersonId());
                            map.put("firstName", person.getFirstName());
                            map.put("lastName", person.getLastName());
                            map.put("gender", person.getGender());
                            if(person.getFather() != null)
                                map.put("father", person.getFather());
                            if(person.getMother() != null)
                                map.put("mother", person.getMother());
                            if(person.getSpouse() != null)
                                map.put("spouse", person.getSpouse());
                            respData = Serializer.serialize(map);
                        }
                        else
                            respData = "{\"data\":" + Serializer.serializeArrayPerson(persons) + "}";
                    }
                    OutputStream respBody = exchange.getResponseBody();
                    StreamUtil.writeString(respData, respBody);
                    exchange.getResponseBody().close();
                    success = true;
                }
            }

            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

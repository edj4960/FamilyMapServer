package cs240.evanjones.server.handler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import cs240.evanjones.server.model.Event;
import cs240.evanjones.server.model.Person;
import cs240.evanjones.server.model.User;
import cs240.evanjones.server.requests.LoadRequest;
import cs240.evanjones.server.results.LoadResult;
import cs240.evanjones.server.services.LoadService;
import cs240.evanjones.server.util.Deserializer;
import cs240.evanjones.server.util.Serializer;
import cs240.evanjones.server.util.StreamUtil;

/**
 * Handles the loading of array's of data into the database.
 */
class LoadHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                // Get Request
                InputStream reqBody = exchange.getRequestBody();
                String reqData = StreamUtil.readString(reqBody);
                System.out.println(reqData);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                // Deserialize request
                Deserializer deserializer = new Deserializer();
                JsonObject jsonObj = Deserializer.deserializeString(reqData);
                // CREATE ARRAYS
                ArrayList<User> usersArray = userArrayCreate(jsonObj);
                ArrayList<Person> personsArray = personArrayCreate(jsonObj);
                ArrayList<Event> eventsArray = eventArrayCreate(jsonObj);
                // Carry out request and grab result
                LoadRequest request = new LoadRequest();
                request.setUsers(usersArray);
                request.setPersons(personsArray);
                request.setEvents(eventsArray);
                LoadService service = new LoadService();
                LoadResult result = service.loadData(request);
                // Send back results
                LinkedHashMap<String, String> map = new LinkedHashMap<>();
                map.put("message", result.getMessage());
                String respData = Serializer.serialize(map);
                OutputStream respBody = exchange.getResponseBody();
                StreamUtil.writeString(respData, respBody);
                exchange.getResponseBody().close();
                success = true;
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

    private ArrayList<User> userArrayCreate(JsonObject jsonObj){
        JsonArray users = jsonObj.get("users").getAsJsonArray();
        Iterator usersIter = users.iterator();
        ArrayList<User> usersArray = new ArrayList<>();
        while(usersIter.hasNext()) {
            JsonObject obj = (JsonObject) usersIter.next();
            User user = new User(Deserializer.jsonToString(obj, "userName"),
                    Deserializer.jsonToString(obj, "password"),
                    Deserializer.jsonToString(obj, "email"),
                    Deserializer.jsonToString(obj, "firstName"),
                    Deserializer.jsonToString(obj, "lastName"),
                    Deserializer.jsonToString(obj, "gender"),
                    Deserializer.jsonToString(obj, "personID"));
            usersArray.add(user);
        }
        return usersArray;
    }

    private ArrayList<Person> personArrayCreate(JsonObject jsonObj) {
        JsonArray persons = jsonObj.get("persons").getAsJsonArray();
        Iterator personsIter = persons.iterator();
        ArrayList<Person> personsArray = new ArrayList<>();
        while(personsIter.hasNext()) {
            JsonObject obj = (JsonObject) personsIter.next();
            Person person = new Person(Deserializer.jsonToString(obj, "personID"),
                    Deserializer.jsonToString(obj, "descendant"),
                    Deserializer.jsonToString(obj, "firstName"),
                    Deserializer.jsonToString(obj, "lastName"),
                    Deserializer.jsonToString(obj, "gender"));
            if(obj.has("father"))
                person.setFather(Deserializer.jsonToString(obj, "father"));
            if(obj.has("mother"))
                person.setMother(Deserializer.jsonToString(obj, "mother"));
            if(obj.has("spouse"))
                person.setSpouse(Deserializer.jsonToString(obj, "spouse"));
            personsArray.add(person);
        }
        return personsArray;
    }

    private ArrayList<Event> eventArrayCreate(JsonObject jsonObj) {
        JsonArray events = jsonObj.get("events").getAsJsonArray();
        Iterator eventsIter = events.iterator();
        ArrayList<Event> eventsArray = new ArrayList<>();
        while(eventsIter.hasNext()) {
            JsonObject obj = (JsonObject) eventsIter.next();
            Event event = new Event(Deserializer.jsonToString(obj, "eventID"),
                    Deserializer.jsonToString(obj, "descendant"),
                    Deserializer.jsonToString(obj, "personID"));
            if(obj.has("latitude"))
                event.setLatitude(Double.parseDouble(obj.get("latitude").toString()));
            if(obj.has("longitude"))
                event.setLongitude(Double.parseDouble(obj.get("longitude").toString()));
            if(obj.has("country"))
                event.setCountry(Deserializer.jsonToString(obj, "country"));
            if(obj.has("city"))
                event.setCity(Deserializer.jsonToString(obj, "city"));
            if(obj.has("eventType"))
                event.setEventType(Deserializer.jsonToString(obj, "eventType"));
            if(obj.has("year")) {
                event.setYear(Integer.parseInt(Deserializer.jsonToString(obj, "year")));
            }
            eventsArray.add(event);
        }
        return eventsArray;
    }
}

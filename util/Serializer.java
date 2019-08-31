package cs240.evanjones.server.util;

import com.google.gson.stream.*;
import java.io.*;
import java.util.*;

import cs240.evanjones.server.model.Event;
import cs240.evanjones.server.model.Person;

/**Converts java data to json*/
public class Serializer {

    public Serializer(){}

    /**
     * Converts a map of java data to json
     * @param javaData map of data
     * @return json String
     * @throws Exception
     */
    public static String serialize(LinkedHashMap<String, String> javaData) throws Exception {
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(sw);
        Iterator it = javaData.entrySet().iterator();
        jsonWriter.beginObject();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String value = pair.getValue().toString();
            jsonWriter.name(pair.getKey().toString());
            if(value.matches("[^a-z,A-Z]+")) {
                if(value.contains("."))
                    jsonWriter.value(Double.parseDouble(value));
                else
                    jsonWriter.value(Integer.parseInt(value));
            }
            else
                jsonWriter.value(pair.getValue().toString());
            it.remove();
        }
        jsonWriter.endObject();
        jsonWriter.flush();
        return sw.toString();
    }

    /**
     * Creates json from an array of person objects
     * @param persons
     * @return json String
     * @throws Exception
     */
    public static String serializeArrayPerson(ArrayList<Person> persons) throws Exception {
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(sw);
        Iterator personIter = persons.iterator();
        jsonWriter.beginArray();
        while(personIter.hasNext()) {
            jsonWriter.beginObject();
            Person person = (Person) personIter.next();
            jsonWriter.name("descendant");
            jsonWriter.value(person.getDescendant());

            jsonWriter.name("personID");
            jsonWriter.value(person.getPersonId());

            jsonWriter.name("firstName");
            jsonWriter.value(person.getFirstName());

            jsonWriter.name("lastName");
            jsonWriter.value(person.getLastName());

            jsonWriter.name("gender");
            jsonWriter.value(person.getGender());

            if(person.getFather() != null) {
                jsonWriter.name("father");
                jsonWriter.value(person.getFather());
            }

            if(person.getMother() != null) {
                jsonWriter.name("mother");
                jsonWriter.value(person.getMother());
            }

            if(person.getSpouse() != null) {
                jsonWriter.name("spouse");
                jsonWriter.value(person.getSpouse());
            }
            jsonWriter.endObject();
        }
        jsonWriter.endArray();
        jsonWriter.flush();
        return sw.toString();
    }

    /**
     * Creates json from an array of event objects
     * @param events
     * @return
     * @throws Exception
     */
    public static String serializeArrayEvent(ArrayList<Event> events) throws Exception {
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(sw);
        Iterator eventIter = events.iterator();
        jsonWriter.beginArray();
        while(eventIter.hasNext()) {
            jsonWriter.beginObject();
            Event event = (Event) eventIter.next();
            jsonWriter.name("descendant");
            jsonWriter.value(event.getDescendant());

            jsonWriter.name("eventID");
            jsonWriter.value(event.getEventId());

            jsonWriter.name("personID");
            jsonWriter.value(event.getPersonId());

            jsonWriter.name("latitude");
            jsonWriter.value(event.getLatitude());

            jsonWriter.name("longitude");
            jsonWriter.value(event.getLongitude());

            jsonWriter.name("country");
            jsonWriter.value(event.getCountry());

            jsonWriter.name("city");
            jsonWriter.value(event.getCity());

            jsonWriter.name("eventType");
            jsonWriter.value(event.getEventType());

            jsonWriter.name("year");
            jsonWriter.value(event.getYear());
            jsonWriter.endObject();
        }
        jsonWriter.endArray();
        jsonWriter.flush();
        return sw.toString();
    }
}

package cs240.evanjones.server.handler;

import com.sun.net.httpserver.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import cs240.evanjones.server.model.Event;
import cs240.evanjones.server.requests.EventRequest;
import cs240.evanjones.server.results.EventResult;
import cs240.evanjones.server.services.EventService;
import cs240.evanjones.server.util.Serializer;
import cs240.evanjones.server.util.StreamUtil;

/**
 * Handles the finding of events in the database.
 */
class EventHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                // Check to see if an "Authorization" header is present
                if (reqHeaders.containsKey("Authorization")) {
                    // Grab request
                    String authToken = reqHeaders.getFirst("Authorization");
                    String url = exchange.getRequestURI().getPath();
                    String eventID = "";
                    int slashesFound = 0;
                    for (int i = 0; i < url.length(); i++) {
                        if (url.charAt(i) == '/')
                            slashesFound++;
                        else if (slashesFound == 2)
                            eventID += url.charAt(i);
                    }

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    // Carry out request
                    EventRequest request = new EventRequest();
                    request.setEventID(eventID);
                    request.setAuthToken(authToken);
                    EventService service = new EventService(request);
                    EventResult result;
                    if(request.getEventID() == "")
                        result = service.getUserEvents();
                    else
                        result = service.getEvent();
                    // Create Response Data
                    String respData = "";
                    LinkedHashMap<String, String> outputMap = new LinkedHashMap<>();
                    if(result.getMessage() != null) {
                        outputMap.put("message", result.getMessage());
                        respData = Serializer.serialize(outputMap);
                    }
                    else {
                        ArrayList<Event> eventsList = result.getEventData();
                        // Serialize data either by one value or by an array of values
                        if(eventsList.size() == 1) {
                            Event event = eventsList.get(0);
                            outputMap.put("descendant", event.getDescendant());
                            outputMap.put("eventID", event.getEventId());
                            outputMap.put("personID", event.getPersonId());
                            if(event.getLatitude().toString() != null)
                                outputMap.put("latitude", event.getLatitude().toString());
                            if(event.getLongitude().toString() != null)
                                outputMap.put("longitude", event.getLatitude().toString());
                            if(event.getCountry() != null)
                                outputMap.put("country", event.getCountry());
                            if(event.getCity() != null)
                                outputMap.put("city", event.getCity());
                            if(event.getEventType() != null)
                                outputMap.put("eventType", event.getEventType());
                            if(event.getYear() != null)
                                outputMap.put("year", event.getYear().toString());
                            respData = Serializer.serialize(outputMap);
                        }
                        else
                            respData = "{\"data\":" + Serializer.serializeArrayEvent(eventsList) + "}";
                    }
                    // Send data
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

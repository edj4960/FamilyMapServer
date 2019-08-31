package cs240.evanjones.server.handler;

import com.sun.net.httpserver.*;
import java.net.*;
import java.io.*;
import java.util.LinkedHashMap;
import cs240.evanjones.server.requests.FillRequest;
import cs240.evanjones.server.results.FillResult;
import cs240.evanjones.server.services.FillService;
import cs240.evanjones.server.util.Serializer;
import cs240.evanjones.server.util.StreamUtil;

/**
 * Handles the populating of data into the database.
 */
class FillHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                // Grab request
                String url = exchange.getRequestURI().getPath();
                String username = "";
                String strGen = "";
                int slashesFound = 0;
                for(int i=0; i<url.length(); i++) {
                    if(url.charAt(i) == '/')
                        slashesFound++;
                    else if(slashesFound == 2)
                        username += url.charAt(i);
                    else if(slashesFound == 3)
                        strGen += url.charAt(i);
                }
                // Check generations requested
                if(strGen.matches("[0-9]+") || strGen == ""){
                    Integer generations = 0;
                    if(strGen != "")
                        generations = Integer.parseInt(strGen);
                    // Carry out request
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    FillRequest request = new FillRequest();
                    request.setUserName(username);
                    request.setGenerations(generations);
                    FillService service = new FillService();
                    FillResult result = service.populateDatabase(request);
                    // Send back results
                    LinkedHashMap<String, String> outputMap = new LinkedHashMap<>();
                    outputMap.put("message", result.getMessage());
                    String respData = Serializer.serialize(outputMap);
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

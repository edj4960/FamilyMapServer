package cs240.evanjones.server.handler;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.*;
import java.net.*;
import java.io.*;
import java.util.LinkedHashMap;

import cs240.evanjones.server.util.Deserializer;
import cs240.evanjones.server.requests.LoginRequest;
import cs240.evanjones.server.results.LoginResult;
import cs240.evanjones.server.services.LoginService;
import cs240.evanjones.server.util.Serializer;
import cs240.evanjones.server.util.StreamUtil;

/**
 * Handles the logging in of a user.
 */
class LoginHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println(exchange.getRequestMethod().toLowerCase());
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                InputStream reqBody = exchange.getRequestBody();
                String reqData = StreamUtil.readString(reqBody);
                System.out.println(reqData);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                // Deserialize request
                JsonObject jsonObj = Deserializer.deserializeString(reqData);
                String username = Deserializer.jsonToString(jsonObj, "userName");
                String password = Deserializer.jsonToString(jsonObj, "password");
                // Check userName and password aren't empty
                if(!username.isEmpty() && !password.isEmpty()) {
                    // Carry out request and grab result
                    LoginRequest request = new LoginRequest(username, password);
                    LoginService service = new LoginService();
                    LoginResult result = service.login(request);
                    LinkedHashMap<String, String> outputMap = new LinkedHashMap<>();
                    String respData;
                    // Check if result is success or error
                    if(result.getAuthToken() != null) {
                        outputMap.put("authToken", result.getAuthToken());
                        outputMap.put("userName", result.getUserName());
                        outputMap.put("personID", result.getPersonId());
                    }
                    else {
                        outputMap.put("message", result.getMessage());
                    }
                    respData = Serializer.serialize(outputMap);
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

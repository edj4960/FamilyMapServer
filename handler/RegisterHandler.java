package cs240.evanjones.server.handler;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.*;
import java.net.*;
import java.io.*;
import java.util.LinkedHashMap;

import cs240.evanjones.server.util.Deserializer;
import cs240.evanjones.server.util.Serializer;
import cs240.evanjones.server.requests.RegisterRequest;
import cs240.evanjones.server.results.RegisterResult;
import cs240.evanjones.server.services.RegisterService;
import cs240.evanjones.server.util.StreamUtil;

/** Handles the registration of a user */
class RegisterHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                // Grab Request
                InputStream reqBody = exchange.getRequestBody();
                String reqData = StreamUtil.readString(reqBody);
                System.out.println(reqData);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                // Deserialize request
                JsonObject jsonObj = Deserializer.deserializeString(reqData);
                String username = Deserializer.jsonToString(jsonObj, "userName");
                String password = Deserializer.jsonToString(jsonObj, "password");
                String email = Deserializer.jsonToString(jsonObj, "email");
                String firstName = Deserializer.jsonToString(jsonObj, "firstName");
                String lastName = Deserializer.jsonToString(jsonObj, "lastName");
                String gender = Deserializer.jsonToString(jsonObj, "gender");
                // Check variables are not empty
                if(!username.isEmpty() && !password.isEmpty() && !email.isEmpty() &&
                    !firstName.isEmpty() && !lastName.isEmpty() && !gender.isEmpty()) {
                    // Carry out request and grab result
                    RegisterRequest request = new RegisterRequest(username, password, email,
                                                                    firstName, lastName, gender);
                    RegisterService service = new RegisterService();
                    RegisterResult result = service.register(request);
                    LinkedHashMap<String, String> map = new LinkedHashMap<>();
                    String respData = "";
                    // Check if result is success or error
                    if(result.getAuthToken() != null) {
                        map.put("authToken", result.getAuthToken());
                        map.put("userName", result.getUserName());
                        map.put("personID", result.getPersonID());
                        Serializer serializer = new Serializer();
                        respData = Serializer.serialize(map);
                    }
                    else {
                        map.put("message", result.getMessage());
                        respData = Serializer.serialize(map);
                    }
                    // Send result
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

package cs240.evanjones.server.handler;

import com.sun.net.httpserver.*;
import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import cs240.evanjones.server.results.ClearResult;
import cs240.evanjones.server.services.ClearService;
import cs240.evanjones.server.util.Serializer;

/**
 * Handles the logging in of a user.
 */
class ClearHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                // Carry out request and grab result
                ClearService service = new ClearService();
                ClearResult result = service.empty();
                LinkedHashMap<String, String> outputMap = new LinkedHashMap<>();
                // Convert result to message
                outputMap.put("message", result.getMessage());
                String respData = Serializer.serialize(outputMap);
                // Send message to server
                OutputStream respBody = exchange.getResponseBody();
                writeString(respData, respBody);
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

    /**
     * reads a String from an InputStream.
    */
    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    /**
     * Writes a String to an OutputStream.
    */
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}

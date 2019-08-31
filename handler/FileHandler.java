package cs240.evanjones.server.handler;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.*;
import java.net.*;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cs240.evanjones.server.util.Deserializer;
import cs240.evanjones.server.requests.LoginRequest;
import cs240.evanjones.server.results.LoginResult;
import cs240.evanjones.server.services.LoginService;
import cs240.evanjones.server.util.Serializer;

/**
 * Handles file requests from user.
 */
class FileHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String filePath = "C:\\Users\\EvanJones\\MyFamilyMapServer\\server\\web\\";
        String url = exchange.getRequestURI().getPath();
        String fileName = "";

        for (int i = 0; i < url.length(); i++) {
            if (url.charAt(i) != '/')
                fileName += url.charAt(i);
            else if(url.charAt(i) == '/' && (i != 0))
                fileName += "\\";
        }
        if(fileName == "")
            fileName = "index.html";
        filePath += fileName;
        File file = new File(filePath);
        if(!file.exists()) {
            String errorFile = "C:\\Users\\EvanJones\\MyFamilyMapServer\\server\\web\\HTML\\404.html";
            file = new File(errorFile);
        }
        exchange.sendResponseHeaders(200, file.length());
        try (OutputStream os = exchange.getResponseBody()) {
            Files.copy(file.toPath(), os);
        }
    }
}

package org.apm4me.internal;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerEmbeded {

    public static void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8091), 0);
        server.createContext("/traces", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                StringBuilder response = new StringBuilder();
                response.append("<html>coucou</html>");


                exchange.sendResponseHeaders(200, response.toString().length());
                exchange.getResponseBody().write(response.toString().getBytes());
            }
        });

        server.start();
    }

}

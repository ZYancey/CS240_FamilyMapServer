package server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.util.Objects;

public class FileHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        boolean success = false;

        try {
            if (httpExchange.getRequestMethod().equalsIgnoreCase("get")) {
                Headers reqHeaders = httpExchange.getRequestHeaders();
                String urlPath = httpExchange.getRequestURI().toString();
                if (Objects.equals(urlPath, "") || Objects.equals(urlPath, "/")) {
                    System.out.println("\"" + urlPath + "\" converted to index.html");
                    urlPath = "/index.html";
                }

                String filePath = "web" + urlPath;
                File file = new File(filePath);

                OutputStream respBody = httpExchange.getResponseBody();

                if (!file.exists()) {
                    System.out.println("~~~~~404~~~~~~");
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);

                    filePath = "web/HTML/404.html";
                    file = new File(filePath);

                    Files.copy(file.toPath(), respBody);
                    httpExchange.getResponseBody().close();
                    respBody.close();

                } else {
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    Files.copy(file.toPath(), respBody);
                    System.out.println("\t" + filePath + " served to user.");
                }


                // Close the output stream.  This is how Java knows we are done
                // sending data and the response is complete.
                respBody.close();
                success = true;
            }

            if (!success) {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                httpExchange.getResponseBody().close();
            }
        } catch (IOException exception) {
            // Some kind of internal error has occurred inside the server (not the
            // client's fault), so we return an "internal server error" status code
            // to the client.
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            httpExchange.getResponseBody().close();
            exception.printStackTrace();
        }
    }
}

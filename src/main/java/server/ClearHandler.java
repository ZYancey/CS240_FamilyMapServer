package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import result.Result;
import service.ClearService;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.Locale;

public class ClearHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            JSONParser json = new JSONParser();
            Result result = new ClearService().clear();
            System.out.println(result.getMessage().toLowerCase(Locale.ROOT));
            if (!result.getMessage().contains("Fail")) {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            } else {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
            OutputStream respBody = httpExchange.getResponseBody();
            OutputStreamWriter out = new OutputStreamWriter(respBody);
            out.write(json.ObjectToJSON(result));
            out.flush();
            respBody.close();

        } catch (IOException exception) {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            httpExchange.getResponseBody().close();
            exception.printStackTrace();
        }

    }
}

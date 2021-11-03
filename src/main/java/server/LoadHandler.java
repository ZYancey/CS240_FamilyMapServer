package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.LoadRequest;
import result.Result;
import service.LoadService;

import java.io.*;
import java.net.HttpURLConnection;

public class LoadHandler implements HttpHandler {
    private Result error;

    public void handle(HttpExchange httpExchange) throws IOException {
        boolean success = false;
        JSONParser json = new JSONParser();

        try {
            if (httpExchange.getRequestMethod().equalsIgnoreCase("POST")) {
                InputStream reqBody = httpExchange.getRequestBody();
                InputStreamReader in = new InputStreamReader(reqBody);
                LoadRequest loadRequestr = json.JSONToObject(in, LoadRequest.class);
                if (validateRequestInfo(loadRequestr)) {
                    Result ar = new LoadService().load(loadRequestr);
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream respBody = httpExchange.getResponseBody();
                    OutputStreamWriter out = new OutputStreamWriter(respBody);
                    out.write(json.ObjectToJSON(ar));
                    out.flush();
                    respBody.close();
                    success = true;
                }
            }

            if(!success) {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                if(error != null) {
                    OutputStreamWriter out = new OutputStreamWriter(httpExchange.getResponseBody());
                    out.write(json.ObjectToJSON(error));
                    out.flush();
                }
                httpExchange.getResponseBody().close();
            }
        } catch (IOException exception) {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            httpExchange.getResponseBody().close();
            exception.printStackTrace();
        }
    }


    private boolean validateRequestInfo(LoadRequest loadRequest) {
        if (loadRequest.getUserList() == null) {
            error = new Result("No User array included in request");
            return false;
        }
        if (loadRequest.getPersonList() == null) {
            error = new Result("No Person array included in request");
            return false;
        }
        if (loadRequest.getEventList() == null) {
            error = new Result("No Event array included in request");
            return false;
        }
        return true;
    }
}
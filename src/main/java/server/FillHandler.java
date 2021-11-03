package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.FillRequest;
import result.Result;
import service.FillService;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class FillHandler implements HttpHandler {
    private Result error;

    public void handle(HttpExchange httpExchange) throws IOException {
        boolean success = false;
        JSONParser json = new JSONParser();

        try {
            if (httpExchange.getRequestMethod().equalsIgnoreCase("POST")) {
                FillRequest fillRequest;
                String[] params = httpExchange.getRequestURI().toString().split("/");
                if (params.length == 4) {
                    fillRequest = new FillRequest(params[2], Integer.parseInt(params[3]));
                }
                else {
                    fillRequest = new FillRequest(params[2], 0);
                }
                if (validateRequestInfo(fillRequest)) {
                    Result res = new FillService().fill(fillRequest);
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream respBody = httpExchange.getResponseBody();
                    OutputStreamWriter out = new OutputStreamWriter(respBody);
                    out.write(json.ObjectToJSON(res));
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
        } catch (IOException io) {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            httpExchange.getResponseBody().close();
            io.printStackTrace();
        }
    }

    private boolean validateRequestInfo(FillRequest fillRequest) {
        if (fillRequest.getUsername().isEmpty()) {
            error = new Result("Username empty");
            return false;
        }
        if (fillRequest.getGenerations() < 0) {
            error = new Result("Invalid generations");
            return false;
        }
        return true;
    }
}
package server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.EventRequest;
import result.EventResult;
import result.Result;
import service.EventService;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class EventHandler implements HttpHandler {
    private Result error;

    public void handle(HttpExchange httpExchange) throws IOException {
        boolean success = false;
        JSONParser json = new JSONParser();

        try {
            if (httpExchange.getRequestMethod().equalsIgnoreCase("GET")) {
                Headers reqHeaders = httpExchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");
                    EventRequest eventRequest;
                    String[] params = httpExchange.getRequestURI().toString().split("/");
                    if (params.length == 3) {
                        eventRequest = new EventRequest(authToken, params[2]);
                        if (validateRequestInfo(eventRequest)) {
                            EventResult res = new EventService().getEvent(eventRequest);
                            if (res.getMessage() != null) {
                                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                                OutputStream respBody = httpExchange.getResponseBody();
                                OutputStreamWriter out = new OutputStreamWriter(respBody);
                                out.write(json.ObjectToJSON(res));
                                out.flush();
                                respBody.close();
                            }
                            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                            OutputStream respBody = httpExchange.getResponseBody();
                            OutputStreamWriter out = new OutputStreamWriter(respBody);
                            out.write(json.ObjectToJSON(res));
                            out.flush();
                            respBody.close();
                            success = true;
                        }
                    }
                    else {
                        eventRequest = new EventRequest(authToken, "");
                        if (validateRequestInfo(eventRequest)) {
                            EventResult res = new EventService().getAll(eventRequest);
                            if (res.getMessage() != null) {
                                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                                OutputStream respBody = httpExchange.getResponseBody();
                                OutputStreamWriter out = new OutputStreamWriter(respBody);
                                out.write(json.ObjectToJSON(res));
                                out.flush();
                                respBody.close();
                            }
                            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                            OutputStream respBody = httpExchange.getResponseBody();
                            OutputStreamWriter out = new OutputStreamWriter(respBody);
                            out.write(json.ObjectToJSON(res));
                            out.flush();
                            respBody.close();
                            success = true;
                        } else {
                            EventResult res = new EventService().getAll(eventRequest);
                            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                            OutputStream respBody = httpExchange.getResponseBody();
                            OutputStreamWriter out = new OutputStreamWriter(respBody);
                            out.write(json.ObjectToJSON(res));
                            out.flush();
                            respBody.close();
                        }
                    }
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

    private boolean validateRequestInfo(EventRequest eventRequest) {
        if (eventRequest.getAuthTokenID().isEmpty()) {
            error = new Result("Error: bad auth");
            return false;
        }
        return true;
    }
}
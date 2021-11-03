package server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.PersonRequest;
import result.PersonResult;
import result.Result;
import service.PersonService;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class PersonHandler implements HttpHandler {
    private Result error;

    public void handle(HttpExchange httpExchange) throws IOException {
        boolean success = false;
        JSONParser json = new JSONParser();

        try {
            if (httpExchange.getRequestMethod().equalsIgnoreCase("GET")) {
                Headers reqHeaders = httpExchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");
                    PersonRequest pr;
                    String[] params = httpExchange.getRequestURI().toString().split("/");
                    if (params.length == 3) {
                        pr = new PersonRequest(authToken, params[2]);
                        if (validateRequestInfo(pr)) {
                            PersonResult res = new PersonService().getPerson(pr);
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
                        pr = new PersonRequest(authToken, "");
                        if (validateRequestInfo(pr)) {
                            PersonResult res = new PersonService().getAll(pr);
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
                            PersonResult res = new PersonService().getAll(pr);
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

    private boolean validateRequestInfo(PersonRequest pr) {
        if (pr.getAuthTokenID().isEmpty()) {
            error = new Result("Error: Invalid auth token");
            return false;
        }
        return true;
    }
}
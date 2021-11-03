package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.LoginRequest;
import result.AuthResult;
import result.Result;
import service.LoginService;

import java.io.*;
import java.net.HttpURLConnection;

public class LoginHandler implements HttpHandler {
    private Result error;


    public void handle(HttpExchange httpExchange) throws IOException {
        boolean success = false;
        JSONParser json = new JSONParser();

        try {
            if (httpExchange.getRequestMethod().equalsIgnoreCase("POST")) {
                InputStream reqBody = httpExchange.getRequestBody();
                InputStreamReader in = new InputStreamReader(reqBody);
                LoginRequest lr = json.JSONToObject(in, LoginRequest.class);
                if (validateRequestInfo(lr)) {
                    AuthResult ar = new LoginService().login(lr);

                    if (ar.getMessage() != null) {
                        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                        OutputStream respBody = httpExchange.getResponseBody();
                        OutputStreamWriter out = new OutputStreamWriter(respBody);
                        out.write(json.ObjectToJSON(ar));
                        out.flush();
                        respBody.close();
                    }


                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                    OutputStream respBody = httpExchange.getResponseBody();
                    OutputStreamWriter out = new OutputStreamWriter(respBody);
                    out.write(json.ObjectToJSON(ar));
                    out.flush();
                    respBody.close();
                    success = true;
                } else {
                    AuthResult ar = new LoginService().login(lr);

                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                    OutputStream respBody = httpExchange.getResponseBody();
                    OutputStreamWriter out = new OutputStreamWriter(respBody);
                    out.write(json.ObjectToJSON(ar));
                    out.flush();
                    respBody.close();
                }
            }

            if (!success) {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                if (error != null) {
                    OutputStreamWriter out = new OutputStreamWriter(httpExchange.getResponseBody());
                    out.write(json.ObjectToJSON(error));
                    out.flush();
                }
                httpExchange.getResponseBody().close();
            }
        } catch (IOException io) {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            httpExchange.getResponseBody().close();
            io.printStackTrace();
        }
    }


    private boolean validateRequestInfo(LoginRequest lr) {
        if (lr.getUsername().isEmpty()) {
            error = new Result("Username empty");
            return false;
        }
        if (lr.getPassword().isEmpty()) {
            error = new Result("Password empty");
            return false;
        }
        return true;
    }
}

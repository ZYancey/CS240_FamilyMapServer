package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.RegisterRequest;
import result.AuthResult;
import result.Result;
import service.RegisterService;

import java.io.*;
import java.net.HttpURLConnection;

public class RegisterHandler implements HttpHandler {
    /**
     * A generic Result to be returned in the event of an error.
     */
    private Result err;

    /**The handler to register a user with the server.*/
    public void handle(HttpExchange exch) throws IOException {
        boolean success = false;
        JSONParser json = new JSONParser();

        try {
            //Accept only POST methods.
            if(exch.getRequestMethod().toUpperCase().equals("POST")) {
                InputStream reqBody = exch.getRequestBody();
                InputStreamReader in = new InputStreamReader(reqBody);
                RegisterRequest rr = json.JSONToObject(in, RegisterRequest.class);
                //Check to see if the request info is valid.
                if(validateRequestBody(rr)) {
                    AuthResult ar = new RegisterService().register(rr);

                    if (ar.getMessage() != null) {
                        exch.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                        OutputStream respBody = exch.getResponseBody();
                        OutputStreamWriter out = new OutputStreamWriter(respBody);
                        out.write(json.ObjectToJSON(ar));
                        out.flush();
                        respBody.close();
                    }


                    exch.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream respBody = exch.getResponseBody();
                    OutputStreamWriter out = new OutputStreamWriter(respBody);
                    out.write(json.ObjectToJSON(ar));
                    out.flush();
                    respBody.close();
                    success = true;
                }
            }
            if(!success) {
                exch.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                if(err != null) {
                    OutputStreamWriter out = new OutputStreamWriter(exch.getResponseBody());
                    out.write(json.ObjectToJSON(err));
                    out.flush();
                }
                exch.getResponseBody().close();
            }
        } catch (IOException io) {
            exch.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exch.getResponseBody().close();
            io.printStackTrace();
        }
    }

    private boolean validateRequestBody(RegisterRequest request) {
        if (request.getUsername().isEmpty()) {
            err = new AuthResult("Username empty");
            return false;
        }
        if (request.getPassword().isEmpty()) {
            err = new AuthResult("Password empty");
            return false;
        }
        if (request.getEmail().isEmpty()) {
            err = new AuthResult("Email empty");
            return false;
        }
        if (request.getFirstName().isEmpty()) {
            err = new AuthResult("First name empty");
            return false;
        }
        if (request.getLastName().isEmpty()) {
            err = new AuthResult("Last name empty");
            return false;
        }

        String tempGender = request.getGender().toUpperCase();
        if (!tempGender.equals("M") && !tempGender.equals("F")) {
            err = new AuthResult("Gender invalid");
            return false;
        }

        return true;
    }
}

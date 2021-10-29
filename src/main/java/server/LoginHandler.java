package server;

import java.io.*;
import java.net.HttpURLConnection;

import request.LoginRequest;
import result.*;
import service.LoginService;

import com.sun.net.httpserver.*;

public class LoginHandler implements HttpHandler {
    /**A result to be returned in the event of an error.*/
    private Result error;

    /**The handler to log a user in with the server.*/
    public void handle(HttpExchange exch) throws IOException {
        boolean success = false;
        JSONParser json = new JSONParser();

        try {
            //Accept only POST methods.
            if(exch.getRequestMethod().toUpperCase().equals("POST")) {
                InputStream reqBody = exch.getRequestBody();
                InputStreamReader in = new InputStreamReader(reqBody);
                LoginRequest lr = json.JSONToObject(in, LoginRequest.class);
                //Check to see if the request info is valid.
                if(validateRequestInfo(lr)) {
                    AuthResult ar = new LoginService().login(lr);
                    exch.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream respBody = exch.getResponseBody();
                    OutputStreamWriter out = new OutputStreamWriter(respBody);
                    out.write(json.ObjectToJSON(ar));
                    out.flush();
                    respBody.close();
                    success = true;
                }
            }

            if(!success){
                exch.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                if(error != null) {
                    OutputStreamWriter out = new OutputStreamWriter(exch.getResponseBody());
                    out.write(json.ObjectToJSON(error));
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

    /**Check that the request info is valid. Return true if it is, and false if there is a mistake.*/
    private boolean validateRequestInfo(LoginRequest lr) {
        if(lr.getUsername().isEmpty()) {
            error = new Result("Username empty");
            return false;}
        if(lr.getPassword().isEmpty()) {
            error = new Result("Password empty");
            return false; }

        //At this point everything checks out.
        return true;
    }

}

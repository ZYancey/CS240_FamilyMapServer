package server;

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;

import request.*;
import result.*;
import service.*;

public class RegisterHandler implements HttpHandler {
    private AuthResult err;
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        JSONParser json = new JSONParser();

        try {
            //Accept only POST methods.
            if(exchange.getRequestMethod().toUpperCase().equals("POST")) {
                InputStream reqBody = exchange.getRequestBody();
                InputStreamReader in = new InputStreamReader(reqBody);
                RegisterRequest rr = json.JSONToObject(in, RegisterRequest.class);
                //Check to see if the request info is valid.
                if(validateRequestBody(rr)) {
                    AuthResult ar = new RegisterService().register(rr);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream respBody = exchange.getResponseBody();
                    OutputStreamWriter out = new OutputStreamWriter(respBody);
                    out.write(json.ObjectToJSON(ar));
                    out.flush();
                    respBody.close();

                    success = !ar.getMessage().contains("Fail");

                }
            }
            if(!success){
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                System.out.println("400");
                if(err != null) {
                    OutputStreamWriter out = new OutputStreamWriter(exchange.getResponseBody());
                    out.write(json.ObjectToJSON(err));
                    out.flush();
                }
                exchange.getResponseBody().close();
            }
        } catch (IOException io) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();
            io.printStackTrace();
        }
    }

    private boolean validateRequestBody(RegisterRequest request){
        if(request.getUsername().isEmpty()) { err = new AuthResult("Username empty"); return false; }
        if(request.getPassword().isEmpty()) { err = new AuthResult("Password empty"); return false; }
        if(request.getEmail().isEmpty()) { err = new AuthResult("Email empty"); return false; }
        if(request.getFirstName().isEmpty()) { err = new AuthResult("First name empty"); return false; }
        if(request.getLastName().isEmpty()) { err = new AuthResult("Last name empty"); return false; }
        if(request.getGender().equals("f") || request.getGender().equals("F")) {
            request.setGender("F");
        }
        if(request.getGender().equals("m") || request.getGender().equals("M")) {
            request.setGender("M");
        }
        if(!request.getGender().equals("M") && !request.getGender().equals("F")) {
            err = new AuthResult("Gender invalid"); return false; }

        //At this point everything checks out.
        return true;

    }
}

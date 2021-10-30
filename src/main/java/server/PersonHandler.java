package server;

import java.io.*;
import java.net.HttpURLConnection;

import request.PersonRequest;
import result.*;
import service.EventService;
import service.PersonService;

import com.sun.net.httpserver.*;

public class PersonHandler implements HttpHandler {
    /**A generic Result to be returned in the event of an error.*/
    private Result error;

    /**The handler to call for a Person or array of Persons.*/
    public void handle(HttpExchange exch) throws IOException {
        boolean success = false;
        JSONParser json = new JSONParser();

        try {
            //Accept only GET methods.
            if(exch.getRequestMethod().toUpperCase().equals("GET")) {
                Headers reqHeaders = exch.getRequestHeaders();
                //Accept only requests that have an Authorization header.
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");
                    PersonRequest pr;
                    //Determine the type of request
                    String[] params = exch.getRequestURI().toString().split("/");
                    //Return a singular Person.
                    if(params.length == 3) {
                        pr = new PersonRequest(authToken, params[2]);
                        //Check to see if the request info is valid.
                        if(validateRequestInfo(pr)) {
                            PersonResult res = new PersonService().getPerson(pr);
                            if (res.getMessage() != null) {
                                exch.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                                OutputStream respBody = exch.getResponseBody();
                                OutputStreamWriter out = new OutputStreamWriter(respBody);
                                out.write(json.ObjectToJSON(res));
                                out.flush();
                                respBody.close();
                            }
                            exch.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                            OutputStream respBody = exch.getResponseBody();
                            OutputStreamWriter out = new OutputStreamWriter(respBody);
                            out.write(json.ObjectToJSON(res));
                            out.flush();
                            respBody.close();
                            success = true;
                        }
                    }
                    //Return an array of Persons.
                    else {
                        pr = new PersonRequest(authToken, "");
                        //Check to see if the request info is valid.
                        if(validateRequestInfo(pr)) {
                            PersonResult res = new PersonService().getAll(pr);
                            if (res.getMessage() != null) {
                                exch.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                                OutputStream respBody = exch.getResponseBody();
                                OutputStreamWriter out = new OutputStreamWriter(respBody);
                                out.write(json.ObjectToJSON(res));
                                out.flush();
                                respBody.close();
                            }
                            exch.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                            OutputStream respBody = exch.getResponseBody();
                            OutputStreamWriter out = new OutputStreamWriter(respBody);
                            out.write(json.ObjectToJSON(res));
                            out.flush();
                            respBody.close();
                            success = true;
                        }else{
                            PersonResult res = new PersonService().getAll(pr);
                            exch.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                            OutputStream respBody = exch.getResponseBody();
                            OutputStreamWriter out = new OutputStreamWriter(respBody);
                            out.write(json.ObjectToJSON(res));
                            out.flush();
                            respBody.close();
                        }
                    }
                }

            }
            if(!success) {
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
    private boolean validateRequestInfo(PersonRequest pr) {
        if(pr.getAuthTokenID().isEmpty()) { error = new Result("Error: Invalid auth token"); return false; }

        return true;
    }
}
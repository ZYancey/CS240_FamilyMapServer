package server;

import java.io.*;
import java.net.HttpURLConnection;

import request.EventRequest;
import result.*;
import service.EventService;

import com.sun.net.httpserver.*;

public class EventHandler implements HttpHandler {
    /**A general Result object to return in the event of an error.*/
    private Result error;

    /**The handler to call for an Event or an array of Events.*/
    public void handle(HttpExchange exch) throws IOException {
        boolean success = false;
        JSONParser json = new JSONParser();

        try {
            //Accept only GET methods.
            if(exch.getRequestMethod().toUpperCase().equals("GET")) {
                Headers reqHeaders = exch.getRequestHeaders();
                //Accept only requests with an Authorization header.
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");
                    EventRequest er;
                    //Determine which service the request is calling for.
                    String[] params = exch.getRequestURI().toString().split("/");
                    //Return a singular Event.
                    if(params.length == 3) {
                        er = new EventRequest(authToken, params[2]);
                        //Check to see if the request info is valid.
                        if(validateRequestInfo(er)) {
                            EventResult res = new EventService().getEvent(er);
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
                    //Return an array of Events.
                    else {
                        er = new EventRequest(authToken, "");
                        //Check to see if the request info is valid.
                        if(validateRequestInfo(er)) {
                            EventResult res = new EventService().getAll(er);
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
                            EventResult res = new EventService().getAll(er);
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
    private boolean validateRequestInfo(EventRequest er) {
        if(er.getAuthTokenID().isEmpty()) { error = new Result("Error: bad auth"); return false; }

        //At this point everything checks out.
        return true;
    }
}
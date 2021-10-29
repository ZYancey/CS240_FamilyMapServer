package server;

import java.io.*;
import java.net.HttpURLConnection;

import request.FillRequest;
import result.Result;
import service.FillService;

import com.sun.net.httpserver.*;

public class FillHandler implements HttpHandler {
    /**A generic Result to be returned in the event of an error.*/
    private Result error;

    /**The handler to fill the database with new information.*/
    public void handle(HttpExchange exch) throws IOException {
        boolean success = false;
        JSONParser json = new JSONParser();

        try {
            //Accept only POST methods.
            if(exch.getRequestMethod().toUpperCase().equals("POST")) {
                FillRequest fr;
                //Determine which type of fill request is being made.
                String[] params = exch.getRequestURI().toString().split("/");
                //The fill request has a number of specific generations to be made.
                if(params.length == 4) {
                     fr = new FillRequest(params[2], Integer.parseInt(params[3]));
                }
                //The fill request came without specifying a number of generations.
                else {
                    fr = new FillRequest(params[2], 0);
                }
                //Check to see if the request info is valid.
                if(validateRequestInfo(fr)) {
                    Result res = new FillService().fill(fr);
                    exch.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream respBody = exch.getResponseBody();
                    OutputStreamWriter out = new OutputStreamWriter(respBody);
                    out.write(json.ObjectToJSON(res));
                    out.flush();
                    respBody.close();
                    success = true;
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
    private boolean validateRequestInfo(FillRequest fr) {
        if(fr.getUsername().isEmpty()) { error = new Result("Username empty"); return false; }
        if(fr.getGenerations() < 0) { error = new Result("Invalid generations"); return false; }

        //At this point everything checks out.
        return true;
    }
}
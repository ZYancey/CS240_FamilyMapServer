package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.LoadRequest;
import result.Result;
import service.LoadService;

import java.io.*;
import java.net.HttpURLConnection;

public class LoadHandler implements HttpHandler {
    /**
     * A generic Result to be returned in the event of an error.
     */
    private Result error;

    /**
     * The handler to call for the given information to be loaded into the database.
     */
    public void handle(HttpExchange exch) throws IOException {
        boolean success = false;
        JSONParser json = new JSONParser();

        try {
            //Accept only POST methods.
            if (exch.getRequestMethod().equalsIgnoreCase("POST")) {
                InputStream reqBody = exch.getRequestBody();
                InputStreamReader in = new InputStreamReader(reqBody);
                LoadRequest lr = json.JSONToObject(in, LoadRequest.class);
                //Check to see if the request info is valid.
                if (validateRequestInfo(lr)) {
                    Result ar = new LoadService().load(lr);
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

    /**
     * Check that the request info is valid. Return true if it is, and false if there is a mistake.
     */
    private boolean validateRequestInfo(LoadRequest lr) {
        if (lr.getUserList() == null) {
            error = new Result("No User array included in request");
            return false;
        }
        if (lr.getPersonList() == null) {
            error = new Result("No Person array included in request");
            return false;
        }
        if (lr.getEventList() == null) {
            error = new Result("No Event array included in request");
            return false;
        }
        return true;
    }
}
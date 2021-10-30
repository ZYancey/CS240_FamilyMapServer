package server;

import com.sun.net.httpserver.*;
import result.Result;
import service.ClearService;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.Locale;

public class ClearHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try{
            JSONParser json = new JSONParser();
            Result result = new ClearService().clear();
            System.out.println(result.getMessage().toLowerCase(Locale.ROOT));
            if(!result.getMessage().contains("Fail")){
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            }else{
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
            }
            OutputStream respBody = exchange.getResponseBody();
            OutputStreamWriter out = new OutputStreamWriter(respBody);
            out.write(json.ObjectToJSON(result));
            out.flush();
            respBody.close();

        } catch(IOException e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }

    }
}

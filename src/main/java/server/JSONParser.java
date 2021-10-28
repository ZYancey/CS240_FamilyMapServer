package server;


import java.io.*;
import com.google.gson.*;

import result.*;



public class JSONParser {
    private Gson TempSon; //IDEK what to call this variable since it gets kinda confusing
    //private LocationData locations;
    public JSONParser(){
        TempSon = new GsonBuilder().setPrettyPrinting().create();
    }

    public String ObjectToJSON(Object o){
        if(o.getClass() == PersonResult.class){
            PersonResult personR = (PersonResult)o;
            if(personR.getPerson() != null){
                return TempSon.toJson(personR.getPerson());
            }
        }
        if(o.getClass() == EventResult.class){
            EventResult eventR = (EventResult) o;
            if(eventR.getEvent() != null){
                return TempSon.toJson(eventR.getEvent());
            }
        }
        if(o.getClass() == AuthResult.class){
            AuthResult authR = (AuthResult) o;
            if(authR.getAuthToken() != null){
                return TempSon.toJson(authR.getAuthToken());
            }
        }
        return TempSon.toJson(o);
    }

    public <Object> Object JSONToObject(Reader reader, Class<Object> objectClass){
        return TempSon.fromJson(reader, objectClass);
    }

    public <Object> Object JSONToObject(String jsonString, Class<Object> objectClass){
        return TempSon.fromJson(jsonString, objectClass);
    }


}

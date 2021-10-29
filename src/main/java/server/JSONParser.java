package server;


import java.io.*;
import com.google.gson.*;

import result.*;



public class JSONParser {
    private Gson TempSon; //IDEK what to call this variable since it gets kinda confusing
    private LocationData locations;
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

    public String[] GetNames(Names type) throws IOException {
        Reader reader = null;
        String file;
        switch(type) {
            case MALE:		file = "json/mnames.json"; break;
            case FEMALE:	file = "json/fnames.json"; break;
            case SURNAME:	file = "json/snames.json"; break;
            default:		file = "json/snames.json"; break;
        }
        try {
            reader = new FileReader(new File(file));
            return TempSon.fromJson(reader, StringList.class).data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if(reader != null) {
                reader.close();
            }
        }
        return null;
    }

    /**Gets the list of locations to use when generating ancestor data.
     * @return			an array of Location objects.*/
    public Location[] GetLocations() {
        Reader reader;
        try {
            reader = new FileReader(new File("json/locations.json"));
            locations = TempSon.fromJson(reader, LocationData.class);
            return locations.data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**This class is essentially a struct for the different parts of location information to be read in.*/
    public class Location {
        /**A String with the Location's country.*/
        private String country;

        /**A String with the Location's city.*/
        private String city;

        /**A double of the Location's latitude.*/
        private float latitude;

        /**A double of the Location's longitude.*/
        private float longitude;



        /**@return		the Location's country*/
        public String getCountry() { return country; }

        /**@return 		the Location's city*/
        public String getCity() { return city; }

        /**@return		the Location's latitude*/
        public float getLatitude() { return latitude; }

        /**@return		the Location's longitude*/
        public float getLongitude() { return longitude; }
    }

    /**Essentially a struct for an array of Location objects.*/
    public class LocationData {
        /**An array of Location objects.*/
        private Location[] data;
    }

    /**A wrapper class for a String[] to allow the JSON conversion for the names.*/
    public class StringList {
        private String[] data;
    }

    /**The types of names to be read and converted from JSON files.*/
    public enum Names {
        MALE, FEMALE, SURNAME
    }
}

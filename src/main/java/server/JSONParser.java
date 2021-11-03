package server;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import result.AuthResult;
import result.EventResult;
import result.PersonResult;

import java.io.*;


public class JSONParser {
    public enum Names {
        MALE_NAME, FEMALE_NAME, LAST_NAME
    }

    private final Gson TempSon;



    public JSONParser() {
        TempSon = new GsonBuilder().setPrettyPrinting().create();
    }

    public String ObjectToJSON(Object inputObj) {
        if (inputObj.getClass() == PersonResult.class) {
            PersonResult personResult = (PersonResult) inputObj;
            if (personResult.getPerson() != null) {
                return TempSon.toJson(personResult.getPerson());
            }
        }
        if (inputObj.getClass() == EventResult.class) {
            EventResult eventResult = (EventResult) inputObj;
            if (eventResult.getEvent() != null) {
                return TempSon.toJson(eventResult.getEvent());
            }
        }
        if (inputObj.getClass() == AuthResult.class) {
            AuthResult authResult = (AuthResult) inputObj;
            if (authResult.getAuthToken() != null) {
                return TempSon.toJson(authResult.getAuthToken());
            }
        }
        return TempSon.toJson(inputObj);
    }

    public <Object> Object JSONToObject(Reader reader, Class<Object> objectClass) {
        return TempSon.fromJson(reader, objectClass);
    }


    public static class Location {
        private String country;
        private String city;
        private float latitude;
        private float longitude;


        public String getCountry() {
            return country;
        }
        public String getCity() {
            return city;
        }
        public float getLatitude() {
            return latitude;
        }
        public float getLongitude() {
            return longitude;
        }
    }
    public static class LocationData {
        private Location[] data;
    }
    public static class StringList {
        private String[] data;
    }

    public String[] GetNames(Names nameType) throws IOException {
        Reader fileReader = null;
        String filePath;

        switch (nameType) {
            case MALE_NAME:
                filePath = "json/mnames.json";
                break;
            case FEMALE_NAME:
                filePath = "json/fnames.json";
                break;
            default:
                filePath = "json/snames.json";
                break;
        }
        try {
            fileReader = new FileReader(filePath);
            return TempSon.fromJson(fileReader, StringList.class).data;
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } finally {
            if (fileReader != null) {
                fileReader.close();
            }
        }
        return null;
    }


    public Location[] GetLocations() {
        Reader fileReader;
        try {
            fileReader = new FileReader("json/locations.json");
            LocationData locations = TempSon.fromJson(fileReader, LocationData.class);
            return locations.data;
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
        return null;
    }


}

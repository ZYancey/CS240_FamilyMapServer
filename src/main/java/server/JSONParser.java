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

    private final Gson TempSon; //IDEK what to call this variable since it gets kinda confusing



    public JSONParser() {
        TempSon = new GsonBuilder().setPrettyPrinting().create();
    }

    public String ObjectToJSON(Object inputObj) {
        if (inputObj.getClass() == PersonResult.class) {
            PersonResult pResult = (PersonResult) inputObj;
            if (pResult.getPerson() != null) {
                return TempSon.toJson(pResult.getPerson());
            }
        }
        if (inputObj.getClass() == EventResult.class) {
            EventResult eResult = (EventResult) inputObj;
            if (eResult.getEvent() != null) {
                return TempSon.toJson(eResult.getEvent());
            }
        }
        if (inputObj.getClass() == AuthResult.class) {
            AuthResult aResult = (AuthResult) inputObj;
            if (aResult.getAuthToken() != null) {
                return TempSon.toJson(aResult.getAuthToken());
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
        Reader fReader = null;
        String filePath;

        switch (nameType) {
            case MALE_NAME:
                filePath = "json/mnames.json";
                break;
            case FEMALE_NAME:
                filePath = "json/fnames.json";
                break;
            case LAST_NAME:
                filePath = "json/snames.json";
                break;
            default:
                filePath = "json/snames.json";
                break;
        }
        try {
            fReader = new FileReader(new File(filePath));
            return TempSon.fromJson(fReader, StringList.class).data;
        } catch (FileNotFoundException excep) {
            excep.printStackTrace();
        } finally {
            if (fReader != null) {
                fReader.close();
            }
        }
        return null;
    }


    public Location[] GetLocations() {
        Reader fReader;
        try {
            fReader = new FileReader(new File("json/locations.json"));
            LocationData locations = TempSon.fromJson(fReader, LocationData.class);
            return locations.data;
        } catch (FileNotFoundException excep) {
            excep.printStackTrace();
        }
        return null;
    }


}

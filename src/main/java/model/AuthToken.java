package model;

public class AuthToken{
        private String eventID;
        private String associatedUsername;
        private String personID;
        private float latitude;
        private float longitude;
        private String country;
        private String city;
        private String eventType;
        private int year;

        public AuthToken(String eventID, String username, String personID, float latitude, float longitude,
                     String country, String city, String eventType, int year) {
            this.eventID = eventID;
            this.associatedUsername = username;
            this.personID = personID;
            this.latitude = latitude;
            this.longitude = longitude;
            this.country = country;
            this.city = city;
            this.eventType = eventType;
            this.year = year;
        }
}

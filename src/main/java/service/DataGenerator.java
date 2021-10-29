package service;

import server.JSONParser;
import server.JSONParser.Location;
import server.JSONParser.Names;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.logging.*;

import result.ListResult;

import model.*;

/**The DataGenerator class creates the ancestor data for the fill and register services.*/
public class DataGenerator {

    public DataGenerator() {
        rand = new Random();
        personList = new ArrayList<Person>();
        eventList = new ArrayList<Event>();
    }

    /**The Logger object to log statements on the server log.*/
    private static Logger logger;
    static { logger = Logger.getLogger("familymaptest"); }

    /**The default number of generations to create unless otherwise specified.*/
    private static int DEFAULT_GENERATIONS = 4;
    /**A reference to the current year.*/
    private static int CURRENT_YEAR = 2021;
    /**The minimum marriage age. All marriage events should take place at least this amount of years after the birth years of those getting married.*/
    private static int MIN_MARRIAGE_AGE = 18;
    /**The mimimum baptism age. All baptism events should take place at least this amount of years after the birth of the person getting baptized.*/
    private static int MIN_BAPTISM_AGE = 8;
    /**An integer representing the realistic maximum number of years after marriage that the parents would have children before.
     * The actual age of the parents would be between MIN_MARRIAGE_AGE and (MIN_MARRIAGE_AGE + MAX_PARENT_AGE).*/
    private static int MAX_PARENT_AGE = 32;
    /**An integer representing the realistic maximum age of any person. All death events should take place no further than the birth year + MAX_AGE.*/
    private static int MAX_AGE = 100;

    /**A integer to represent how many generations of data to create. This is either set to DEFAULT_GENERATIONS or specified by the end user.*/
    private int generations = 0;
    /**A Random object to generate random numbers for dates and ancestor names.*/
    private Random rand;
    /**A list of male first names to pull from when creating ancestors.*/
    private String[] men;
    /**A list of female first names to pull from when creating ancestors.*/
    private String[] women;
    /**A list of last names to pull from when creating ancestors.*/
    private String[] last;
    /**A list of locations to pull from when creating events for ancestors.*/
    private Location[] locations;
    /**An integer to track the current father's birth year.*/
    private int fatherBirthYear;
    /**An integer to track the current mother's birth year.*/
    private int motherBirthYear;
    /**An integer to track the current father's baptism year.*/
    private int fatherBaptismYear;
    /**An integer to track the current mother's baptism year.*/
    private int motherBaptismYear;
    /**An integer to track the marriage year of the current mother and father.*/
    private int marriageYear;
    /**An array list of Person objects that will be created.*/
    private ArrayList<Person> personList;
    /**An ArrayList of Event objects that will be created.*/
    private ArrayList<Event> eventList;



    /**Generate Persons and Events for the default number of generations.
     * @param start				the start of the family tree that will be made, which is usually the Person object for the current User.
     * @return 					a ListResult containing the arrays of Persons and Events that are made.*/
    public ListResult GenerateDefaultAncestorData(Person start)   {
        logger.log(Level.INFO, "Starting GenerateDefaultAncestorData.");
        JSONParser j = new JSONParser();
        try {
            //Get the list of possible names for the Persons we will generate.
            men = j.GetNames(Names.MALE);
            women = j.GetNames(Names.FEMALE);
            last = j.GetNames(Names.SURNAME);
            locations = j.GetLocations();
        } catch (IOException io) {
            logger.log(Level.SEVERE, io.getLocalizedMessage());
        }


        //We make an assumption that the average user is between 1 and 30 years old, and use the user's "birth year" to start.
        int childBirthYear = (CURRENT_YEAR - (rand.nextInt(30) + 1));
        generations = DEFAULT_GENERATIONS;

        AddNewGeneration(1, start, childBirthYear);
        personList.add(start);
        GenerateUserEvents(start, childBirthYear);
        logger.log(Level.INFO, "Exiting GenerateDefaultAncestorData.");
        Person[] per = new Person[personList.size()];
        Event[] evt = new Event[eventList.size()];
        return new ListResult(personList.toArray(per), eventList.toArray(evt));
    }

    /**Generate Persons and Events for the number of generations specified by the user.
     * @param start				the start of the family tree that will be made, which is usually the Person object for the current User.
     * @return					a ListResult containing the arrays of the Persons and Events that are made.*/
    public ListResult GenerateAncestorData(Person start, int gen)   {
        logger.log(Level.INFO, "Starting GenerateAncestorData - non default.");
        JSONParser j = new JSONParser();
        try {
            //Get the list of possible names for the Persons we will generate.
            men = j.GetNames(Names.MALE);
            women = j.GetNames(Names.FEMALE);
            last = j.GetNames(Names.SURNAME);
            locations = j.GetLocations();
        } catch (IOException io) {
            logger.log(Level.SEVERE, io.getLocalizedMessage());
        }

        //We make an assumption that the average user is between 1 and 30 years old, and use the user's "birth year" to start.
        int childBirthYear = (CURRENT_YEAR - (rand.nextInt(30) + 1));
        generations = gen;

        AddNewGeneration(1, start, childBirthYear);
        personList.add(start);
        GenerateUserEvents(start, childBirthYear);
        logger.log(Level.INFO, "Exiting GenerateAncestorData - non default.");
        Person[] per = new Person[personList.size()];
        Event[] evt = new Event[eventList.size()];
        return new ListResult(personList.toArray(per), eventList.toArray(evt));
    }

    private void AddNewGeneration(int gen, Person child, int childBirthYear)   {
        logger.log(Level.INFO, "Starting AddNewGeneration.");
        logger.log(Level.FINE, String.format("At new gen start, gen = %s and generations = %s", gen, generations));
        //Exit out of the function if we are past the number of generations that we should create.
        if(gen > generations) { return; }

        //Create ID's for the father and mother and assign them to the child.
        String fatherID = UUID.randomUUID().toString();
        String motherID = UUID.randomUUID().toString();
        child.setFatherID(fatherID);
        child.setMotherID(motherID);

        //Generate a new Person as the father.
        Person father = new Person(fatherID, child.getUsername(), men[rand.nextInt(men.length)], child.getLastName(), "M",  "", "", motherID);
        //Generate a new Person as the mother.
        Person mother = new Person(motherID, child.getUsername(), women[rand.nextInt(men.length)], last[rand.nextInt(last.length)], "F",  "", "", fatherID);
        //Insert these Person objects into the database.

        GenerateAncestorEvents(father, mother, childBirthYear);

        gen++;
        //Create a new generation based off of the father and mother.
        AddNewGeneration(gen, father, fatherBirthYear);
        AddNewGeneration(gen, mother, motherBirthYear);

        personList.add(father);
        personList.add(mother);

        logger.log(Level.INFO, "Exiting AddNewGeneration.");
    }

    private void GenerateAncestorEvents(Person father, Person mother, int childBirthYear)   {
        logger.log(Level.INFO, "Starting GenerateAncestorEvents.");
        GenerateBirthEvents(father, mother, childBirthYear);
        GenerateBaptismEvents(father, mother);
        GenerateMarriageEvent(father, mother);
        GenerateDeathEvents(father, mother);
        logger.log(Level.INFO, "Exiting GenerateAncestorEvents.");
    }

    private void GenerateBirthEvents(Person father, Person mother, int childBirthYear)   {
        logger.log(Level.FINER, "Starting GenerateBirthEvents.");

        do {
            //Generate a random year by getting a random number between 0 and 32, adding 18, and subtracting from the child birth year.
            //Keep doing until the father's birth was at least 18 years before the child.
            fatherBirthYear = (childBirthYear - (rand.nextInt(MAX_PARENT_AGE) + MIN_MARRIAGE_AGE));
        } while (fatherBirthYear > (childBirthYear - MIN_MARRIAGE_AGE) || fatherBirthYear < (childBirthYear - MAX_PARENT_AGE));

        String fBirthID = UUID.randomUUID().toString();
        int fLocationIndex = rand.nextInt(locations.length);
        eventList.add(
                new Event(
                        fBirthID,
                        father.getUsername(),
                        father.getPersonID(),
                        locations[fLocationIndex].getLatitude(),
                        locations[fLocationIndex].getLongitude(),
                        locations[fLocationIndex].getCountry(),
                        locations[fLocationIndex].getCity(),
                        "birth",
                        fatherBirthYear
                ));

        do {
            //Generate a random year by getting a random number between 0 and 32, adding 18, and subtracting from the child birth year.
            //Keep doing until the mother's birth was at least 18 years before the child.
            motherBirthYear = (childBirthYear - (rand.nextInt(MAX_PARENT_AGE) + MIN_MARRIAGE_AGE));
        } while (motherBirthYear > (childBirthYear - MIN_MARRIAGE_AGE) || motherBirthYear < (childBirthYear - MAX_PARENT_AGE));

        String mBirthID = UUID.randomUUID().toString();
        int mLocationIndex = rand.nextInt(locations.length);
        eventList.add(
                new Event(
                        mBirthID,
                        mother.getUsername(),
                        mother.getPersonID(),
                        locations[mLocationIndex].getLatitude(),
                        locations[mLocationIndex].getLongitude(),
                        locations[mLocationIndex].getCountry(),
                        locations[mLocationIndex].getCity(),
                        "birth",
                        motherBirthYear
                ));

        logger.log(Level.FINER, "Exiting GenerateBirthEvents.");
    }

    /**Generate baptism Events for the father and mother.
     * @param father			the father of the current generation.
     * @param mother			the mother of the current generation.*/
    private void GenerateBaptismEvents(Person father, Person mother)   {
        logger.log(Level.FINER, "Starting GenerateBaptismEvents.");
        //Generate baptism events for mother and father.

        do {
            fatherBaptismYear = (fatherBirthYear + rand.nextInt(MAX_AGE - MIN_BAPTISM_AGE) + MIN_BAPTISM_AGE);
        } while ((fatherBaptismYear - fatherBirthYear) > MAX_AGE);

        String fBaptismID = UUID.randomUUID().toString();
        int fLocationIndex = rand.nextInt(locations.length);
        eventList.add(
                new Event(
                        fBaptismID,
                        father.getUsername(),
                        father.getPersonID(),
                        locations[fLocationIndex].getLatitude(),
                        locations[fLocationIndex].getLongitude(),
                        locations[fLocationIndex].getCountry(),
                        locations[fLocationIndex].getCity(),
                        "baptism",
                        fatherBaptismYear
                ));

        do {
            motherBaptismYear = (motherBirthYear + rand.nextInt(MAX_AGE - MIN_BAPTISM_AGE) + MIN_BAPTISM_AGE);
        } while ((motherBaptismYear - motherBirthYear) > MAX_AGE);

        String mBaptismID = UUID.randomUUID().toString();
        int mLocationIndex = rand.nextInt(locations.length);
        eventList.add(
                new Event(
                        mBaptismID,
                        mother.getUsername(),
                        mother.getPersonID(),
                        locations[mLocationIndex].getLatitude(),
                        locations[mLocationIndex].getLongitude(),
                        locations[mLocationIndex].getCountry(),
                        locations[mLocationIndex].getCity(),
                        "baptism",
                        motherBaptismYear
                ));

        logger.log(Level.FINER, "Exiting GenerateBaptismEvents.");
    }

    /**Generate a marriage Event for the father and mother.
     * @param father			the father of the current generation.
     * @param mother			the mother of the current generation.*/
    private void GenerateMarriageEvent(Person father, Person mother)   {
        logger.log(Level.FINER, "Starting GenerateMarriageEvent.");
        //Generate marriage event.

        do {
            marriageYear = (motherBirthYear + rand.nextInt(MAX_AGE - MIN_MARRIAGE_AGE) + MIN_MARRIAGE_AGE);
        } while ((marriageYear - motherBirthYear) > MAX_AGE || (marriageYear - fatherBirthYear) > MAX_AGE);

        String fMarriageID = UUID.randomUUID().toString();
        int LocationIndex = rand.nextInt(locations.length);
        eventList.add(
                new Event(
                        fMarriageID,
                        father.getUsername(),
                        father.getPersonID(),
                        locations[LocationIndex].getLatitude(),
                        locations[LocationIndex].getLongitude(),
                        locations[LocationIndex].getCountry(),
                        locations[LocationIndex].getCity(),
                        "marriage",
                        marriageYear
                ));

        String mMarriageID = UUID.randomUUID().toString();
        eventList.add(
                new Event(
                        mMarriageID,
                        mother.getUsername(),
                        mother.getPersonID(),
                        locations[LocationIndex].getLatitude(),
                        locations[LocationIndex].getLongitude(),
                        locations[LocationIndex].getCountry(),
                        locations[LocationIndex].getCity(),
                        "marriage",
                        marriageYear
                ));

        logger.log(Level.FINER, "Exiting GenerateMarriageEvent.");
    }

    /**Generate death Events for the father and mother.
     * @param father			the father of the current generation.
     * @param mother			the mother of the current generation.*/
    private void GenerateDeathEvents(Person father, Person mother)   {
        logger.log(Level.FINER, "Starting GenerateDeathEvents.");
        //Generate death events for mother and father.

        int fatherDeathYear;
        int motherDeathYear;

        int fLastEvent = Math.max(fatherBaptismYear, marriageYear);
        int fYearsTilMax = (MAX_AGE - (fLastEvent - fatherBirthYear) + 1);
        do {
            fatherDeathYear = (fLastEvent + rand.nextInt((fYearsTilMax)));
        } while (fatherDeathYear < fatherBaptismYear || fatherDeathYear < marriageYear);

        String fDeathID = UUID.randomUUID().toString();
        int fLocationIndex = rand.nextInt(locations.length);
        eventList.add(
                new Event(
                        fDeathID,
                        father.getUsername(),
                        father.getPersonID(),
                        locations[fLocationIndex].getLatitude(),
                        locations[fLocationIndex].getLongitude(),
                        locations[fLocationIndex].getCountry(),
                        locations[fLocationIndex].getCity(),
                        "death",
                        fatherDeathYear
                ));

        int mLastEvent = Math.max(motherBaptismYear, marriageYear);
        int mYearsTilMax = (MAX_AGE - (mLastEvent - motherBirthYear) + 1);
        do {
            motherDeathYear = (mLastEvent + rand.nextInt((mYearsTilMax)));
        } while (motherDeathYear < motherBaptismYear || motherDeathYear < marriageYear);


        String mDeathID = UUID.randomUUID().toString();
        int mLocationIndex = rand.nextInt(locations.length);
        eventList.add(
                new Event(
                        mDeathID,
                        mother.getUsername(),
                        mother.getPersonID(),
                        locations[mLocationIndex].getLatitude(),
                        locations[mLocationIndex].getLongitude(),
                        locations[mLocationIndex].getCountry(),
                        locations[mLocationIndex].getCity(),
                        "death",
                        motherDeathYear
                ));

        logger.log(Level.FINER, "Exiting GenerateDeathEvents.");
    }

    private void GenerateUserEvents(Person user, int birthYear) {
        //Generate a birth event
        String BirthID = UUID.randomUUID().toString();
        int LocationIndex = rand.nextInt(locations.length);
        eventList.add(
                new Event(
                        BirthID,
                        user.getUsername(),
                        user.getPersonID(),
                        locations[LocationIndex].getLatitude(),
                        locations[LocationIndex].getLongitude(),
                        locations[LocationIndex].getCountry(),
                        locations[LocationIndex].getCity(),
                        "birth",
                        birthYear
                ));

        //Generate a baptism event
        int baptismYear;
        do {
            baptismYear = (birthYear + rand.nextInt(MAX_AGE - MIN_BAPTISM_AGE) + MIN_BAPTISM_AGE);
        } while ((baptismYear - birthYear) > MAX_AGE);

        String BaptismID = UUID.randomUUID().toString();
        int bLocationIndex = rand.nextInt(locations.length);
        eventList.add(
                new Event(
                        BaptismID,
                        user.getUsername(),
                        user.getPersonID(),
                        locations[bLocationIndex].getLatitude(),
                        locations[bLocationIndex].getLongitude(),
                        locations[bLocationIndex].getCountry(),
                        locations[bLocationIndex].getCity(),
                        "baptism",
                        baptismYear
                ));

        //Generate a death event, cause the user dies alone.
        int deathYear;

        int YearsTilMax = (MAX_AGE - (baptismYear - birthYear) + 1);
        do {
            deathYear = (baptismYear + rand.nextInt((YearsTilMax)));
        } while (deathYear < fatherBaptismYear);

        String DeathID = UUID.randomUUID().toString();
        int dLocationIndex = rand.nextInt(locations.length);
        eventList.add(
                new Event(
                        DeathID,
                        user.getUsername(),
                        user.getPersonID(),
                        locations[dLocationIndex].getLatitude(),
                        locations[dLocationIndex].getLongitude(),
                        locations[dLocationIndex].getCountry(),
                        locations[dLocationIndex].getCity(),
                        "death",
                        deathYear
                ));
    }
}
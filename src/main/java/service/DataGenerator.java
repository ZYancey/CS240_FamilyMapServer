package service;

import server.JSONParser;
import server.JSONParser.Location;
import server.JSONParser.Names;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import result.ListResult;

import model.*;

public class DataGenerator {

    public DataGenerator() {
        rand = new Random();
        personList = new ArrayList<Person>();
        eventList = new ArrayList<Event>();
    }



    private static final int DEFAULT_GENERATIONS = 4;
    private static final int CURRENT_YEAR = 2021;
    private static final int MIN_MARRIAGE_AGE = 18;
    private static final int MIN_BAPTISM_AGE = 8;
    private static final int MAX_PARENT_AGE = 32;
    private static final int MAX_AGE = 100;

    private int generations = 0;
    private final Random rand;

    /**A list of names to pull from when creating ancestors.*/
    private String[] men;
    private String[] women;
    private String[] last;
    /**A list of locations to pull from when creating events for ancestors.*/
    private Location[] locations;

    private int fatherBirthYear;
    private int motherBirthYear;
    private int fatherBaptismYear;
    private int motherBaptismYear;
    private int marriageYear;

    private final ArrayList<Person> personList;
    private final ArrayList<Event> eventList;



    /**Generate Persons and Events for the default number of generations.
     * @param start				the start of the family tree that will be made, which is usually the Person object for the current User.
     * @return 					a ListResult containing the arrays of Persons and Events that are made.*/
    public ListResult GenerateDefaultAncestorData(Person start)   {
        JSONParser j = new JSONParser();
        try {
            //Get the list of possible names for the Persons we will generate.
            men = j.GetNames(Names.MALE_NAME);
            women = j.GetNames(Names.FEMALE_NAME);
            last = j.GetNames(Names.LAST_NAME);
            locations = j.GetLocations();
        } catch (IOException ignored) {
        }


        //We make an assumption that the average user is between 1 and 30 years old, and use the user's "birth year" to start.
        int childBirthYear = (CURRENT_YEAR - (rand.nextInt(10) + 18));
        generations = DEFAULT_GENERATIONS;

        AddNewGeneration(1, start, childBirthYear);
        personList.add(start);
        GenerateUserEvents(start, childBirthYear);
        Person[] per = new Person[personList.size()];
        Event[] evt = new Event[eventList.size()];
        return new ListResult(personList.toArray(per), eventList.toArray(evt));
    }

    /**Generate Persons and Events for the number of generations specified by the user.**/
    public ListResult GenerateAncestorData(Person start, int gen)   {
        JSONParser j = new JSONParser();
        try {
            //Get the list of possible names for the Persons we will generate.
            men = j.GetNames(Names.MALE_NAME);
            women = j.GetNames(Names.FEMALE_NAME);
            last = j.GetNames(Names.LAST_NAME);
            locations = j.GetLocations();
        } catch (IOException ignored) {
        }

        //We make an assumption that the average user is between 1 and 30 years old, and use the user's "birth year" to start.
        int childBirthYear = (CURRENT_YEAR - (rand.nextInt(30) + 1));
        generations = gen;

        AddNewGeneration(1, start, childBirthYear);
        personList.add(start);
        GenerateUserEvents(start, childBirthYear);
        Person[] per = new Person[personList.size()];
        Event[] evt = new Event[eventList.size()];
        return new ListResult(personList.toArray(per), eventList.toArray(evt));
    }

    private void AddNewGeneration(int gen, Person child, int childBirthYear)   {
        //Exit out of the function if we are past the number of generations that we should create.
        if(gen > generations) { return; }

        //Create ID's for the father and mother and assign them to the child.
        String fatherID = UUID.randomUUID().toString();
        String motherID = UUID.randomUUID().toString();
        child.setFatherID(fatherID);
        child.setMotherID(motherID);


        Person father = new Person(men[rand.nextInt(men.length)], child.getLastName(), "M",  fatherID, motherID,"", "",  child.getUsername());
        Person mother = new Person(women[rand.nextInt(men.length)], last[rand.nextInt(last.length)], "F",  motherID, fatherID,"", "",  child.getUsername());

        GenerateAncestorEvents(father, mother, childBirthYear);

        gen++;
        //Create a new generation based off of the father and mother.
        AddNewGeneration(gen, father, fatherBirthYear);
        AddNewGeneration(gen, mother, motherBirthYear);

        personList.add(father);
        personList.add(mother);
    }

    private void GenerateAncestorEvents(Person father, Person mother, int childBirthYear)   {
        GenerateBirthEvents(father, mother, childBirthYear);
        GenerateBaptismEvents(father, mother);
        GenerateMarriageEvent(father, mother);
        GenerateDeathEvents(father, mother);
    }

    private void GenerateBirthEvents(Person father, Person mother, int childBirthYear)   {
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
    }

    private void GenerateBaptismEvents(Person father, Person mother)   {
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
    }

    private void GenerateMarriageEvent(Person father, Person mother)   {

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
    }

    private void GenerateDeathEvents(Person father, Person mother)   {
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
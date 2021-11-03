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
        personList = new ArrayList<>();
        eventList = new ArrayList<>();
    }

    private static final int DEFAULT_GENERATIONS = 4;
    private static final int CURRENT_YEAR = 2021;
    private static final int MIN_MARRIAGE_AGE = 18;
    private static final int MIN_GRADUATION_AGE = 16;
    private static final int MAX_AGE = 100;

    private int generations = 0;
    private final Random rand;

    private String[] men;
    private String[] women;
    private String[] last;
    private Location[] locations;

    private int fatherBirthYear;
    private int motherBirthYear;
    private int fatherGradYear;
    private int motherGradYear;
    private int marriageYear;
    private int currentGen = 1;

    private final ArrayList<Person> personList;
    private final ArrayList<Event> eventList;



    public ListResult GenerateDefaultAncestorData(Person start)   {
        JSONParser j = new JSONParser();
        try {
            men = j.GetNames(Names.MALE_NAME);
            women = j.GetNames(Names.FEMALE_NAME);
            last = j.GetNames(Names.LAST_NAME);
            locations = j.GetLocations();
        } catch (IOException ignored) {
        }



        int childBirthYear = (CURRENT_YEAR - (rand.nextInt(10) + 18));
        generations = DEFAULT_GENERATIONS;

        AddNewGeneration(1, start);
        personList.add(start);
        GenerateUserEvents(start, childBirthYear);
        Person[] per = new Person[personList.size()];
        Event[] evt = new Event[eventList.size()];
        return new ListResult(personList.toArray(per), eventList.toArray(evt));
    }

    public ListResult GenerateAncestorData(Person start, int gen)   {
        JSONParser j = new JSONParser();
        try {
            men = j.GetNames(Names.MALE_NAME);
            women = j.GetNames(Names.FEMALE_NAME);
            last = j.GetNames(Names.LAST_NAME);
            locations = j.GetLocations();
        } catch (IOException ignored) {
        }

        int childBirthYear = (CURRENT_YEAR - (rand.nextInt(30) + 1));
        generations = gen;

        AddNewGeneration(1, start);
        personList.add(start);
        GenerateUserEvents(start, childBirthYear);
        Person[] per = new Person[personList.size()];
        Event[] evt = new Event[eventList.size()];
        return new ListResult(personList.toArray(per), eventList.toArray(evt));
    }


    private void AddNewGeneration(int gen, Person child)   {
        if(gen > generations) { return; }
        if (gen >=1){
            currentGen = gen;
        }


        String fatherID = UUID.randomUUID().toString();
        String motherID = UUID.randomUUID().toString();
        child.setFatherID(fatherID);
        child.setMotherID(motherID);


        Person father = new Person(men[rand.nextInt(men.length)], child.getLastName(), "M",  fatherID, motherID,"", "",  child.getUsername());
        Person mother = new Person(women[rand.nextInt(men.length)], last[rand.nextInt(last.length)], "F",  motherID, fatherID,"", "",  child.getUsername());


        GenerateAncestorEvents(father, mother);

        gen++;

        AddNewGeneration(gen, father);
        AddNewGeneration(gen, mother);

        personList.add(father);
        personList.add(mother);
    }

    private void GenerateAncestorEvents(Person father, Person mother)   {
        GenerateBirthEvents(father, mother);
        GenerateGradEvents(father, mother);
        GenerateMarriageEvent(father, mother);
        GenerateDeathEvents(father, mother);
    }

    private void GenerateBirthEvents(Person father, Person mother)   {

        fatherBirthYear = 2000 - (24*(currentGen));

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

        motherBirthYear = 2000 - (24*(currentGen));

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

    private void GenerateGradEvents(Person father, Person mother)   {
        do {
            fatherGradYear = (fatherBirthYear + rand.nextInt(MAX_AGE - MIN_GRADUATION_AGE) + MIN_GRADUATION_AGE);
        } while ((fatherGradYear - fatherBirthYear) > MAX_AGE);

        String fGradID = UUID.randomUUID().toString();
        int fLocationIndex = rand.nextInt(locations.length);
        eventList.add(
                new Event(
                        fGradID,
                        father.getUsername(),
                        father.getPersonID(),
                        locations[fLocationIndex].getLatitude(),
                        locations[fLocationIndex].getLongitude(),
                        locations[fLocationIndex].getCountry(),
                        locations[fLocationIndex].getCity(),
                        "graduation",
                        fatherGradYear
                ));

        do {
            motherGradYear = (motherBirthYear + rand.nextInt(MAX_AGE - MIN_GRADUATION_AGE) + MIN_GRADUATION_AGE);
        } while ((motherGradYear - motherBirthYear) > MAX_AGE);

        String mGradID = UUID.randomUUID().toString();
        int mLocationIndex = rand.nextInt(locations.length);
        eventList.add(
                new Event(
                        mGradID,
                        mother.getUsername(),
                        mother.getPersonID(),
                        locations[mLocationIndex].getLatitude(),
                        locations[mLocationIndex].getLongitude(),
                        locations[mLocationIndex].getCountry(),
                        locations[mLocationIndex].getCity(),
                        "graduation",
                        motherGradYear
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

        int fLastEvent = Math.max(fatherGradYear, marriageYear);
        int fYearsTilMax = (MAX_AGE - (fLastEvent - fatherBirthYear) + 1);
        do {
            fatherDeathYear = (fLastEvent + rand.nextInt((fYearsTilMax)));
        } while (fatherDeathYear < fatherGradYear || fatherDeathYear < marriageYear);

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

        int mLastEvent = Math.max(motherGradYear, marriageYear);
        int mYearsTilMax = (MAX_AGE - (mLastEvent - motherBirthYear) + 1);
        do {
            motherDeathYear = (mLastEvent + rand.nextInt((mYearsTilMax)));
        } while (motherDeathYear < motherGradYear || motherDeathYear < marriageYear);


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
        int gradYear;
        do {
            gradYear = (birthYear + rand.nextInt(MAX_AGE - MIN_GRADUATION_AGE) + MIN_GRADUATION_AGE);
        } while ((gradYear - birthYear) > MAX_AGE);

        String GradID = UUID.randomUUID().toString();
        int gLocationIndex = rand.nextInt(locations.length);
        eventList.add(
                new Event(
                        GradID,
                        user.getUsername(),
                        user.getPersonID(),
                        locations[gLocationIndex].getLatitude(),
                        locations[gLocationIndex].getLongitude(),
                        locations[gLocationIndex].getCountry(),
                        locations[gLocationIndex].getCity(),
                        "graduation",
                        gradYear
                ));

        //Generate a death event, cause the user dies alone.
        int deathYear;

        int YearsTilMax = (MAX_AGE - (gradYear - birthYear) + 1);
        do {
            deathYear = (gradYear + rand.nextInt((YearsTilMax)));
        } while (deathYear < fatherGradYear);

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
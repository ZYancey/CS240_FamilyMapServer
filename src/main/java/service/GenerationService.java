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

public class GenerationService {

    public GenerationService() {
        seed = new Random();
        personArrayList = new ArrayList<>();
        eventArrayList = new ArrayList<>();
    }

    private static final int GENERATIONS = 4;
    private static final int YEAR = 2021;
    private static final int MIN_MARRIAGEAGE = 18;
    private static final int MIN_GRADUATIONAGE = 16;
    private static final int MAX_AGE = 100;

    private int generations = 0;
    private final Random seed;

    private String[] maleNames;
    private String[] femaleNames;
    private String[] lastNames;
    private Location[] locationList;

    private int fatherBirthYear;
    private int motherBirthYear;
    private int fatherGradYear;
    private int motherGradYear;
    private int marriageYear;
    private int currentGen = 1;

    private final ArrayList<Person> personArrayList;
    private final ArrayList<Event> eventArrayList;



    public ListResult GenerateDefaultAncestorData(Person userPerson)   {
        JSONParser json = new JSONParser();
        try {
            maleNames = json.GetNames(Names.MALE_NAME);
            femaleNames = json.GetNames(Names.FEMALE_NAME);
            lastNames = json.GetNames(Names.LAST_NAME);
            locationList = json.GetLocations();
        } catch (IOException ignored) {
        }



        int childBirthYear = (YEAR - (seed.nextInt(10) + 18));
        generations = GENERATIONS;

        AddNewGeneration(1, userPerson);
        personArrayList.add(userPerson);
        GenerateUserEvents(userPerson, childBirthYear);
        Person[] people = new Person[personArrayList.size()];
        Event[] events = new Event[eventArrayList.size()];
        return new ListResult(personArrayList.toArray(people), eventArrayList.toArray(events));
    }

    public ListResult GenerateAncestorData(Person userPerson, int genCount)   {
        JSONParser json = new JSONParser();
        try {
            maleNames = json.GetNames(Names.MALE_NAME);
            femaleNames = json.GetNames(Names.FEMALE_NAME);
            lastNames = json.GetNames(Names.LAST_NAME);
            locationList = json.GetLocations();
        } catch (IOException ignored) {
        }

        int childBirthYear = (YEAR - (seed.nextInt(30) + 1));
        generations = genCount;

        AddNewGeneration(1, userPerson);
        personArrayList.add(userPerson);
        GenerateUserEvents(userPerson, childBirthYear);
        Person[] people = new Person[personArrayList.size()];
        Event[] events = new Event[eventArrayList.size()];
        return new ListResult(personArrayList.toArray(people), eventArrayList.toArray(events));
    }


    private void GenerateUserEvents(Person user, int birthYear) {
        String BirthID = UUID.randomUUID().toString();
        int LocationIndex = seed.nextInt(locationList.length);
        eventArrayList.add(
                new Event(
                        BirthID,
                        user.getUsername(),
                        user.getPersonID(),
                        locationList[LocationIndex].getLatitude(),
                        locationList[LocationIndex].getLongitude(),
                        locationList[LocationIndex].getCountry(),
                        locationList[LocationIndex].getCity(),
                        "birth",
                        birthYear
                ));

        int gradYear;
        do {
            gradYear = (birthYear + seed.nextInt(MAX_AGE - MIN_GRADUATIONAGE) + MIN_GRADUATIONAGE);
        } while ((gradYear - birthYear) > MAX_AGE);

        String GradID = UUID.randomUUID().toString();
        int gradLocationIndex = seed.nextInt(locationList.length);
        eventArrayList.add(
                new Event(
                        GradID,
                        user.getUsername(),
                        user.getPersonID(),
                        locationList[gradLocationIndex].getLatitude(),
                        locationList[gradLocationIndex].getLongitude(),
                        locationList[gradLocationIndex].getCountry(),
                        locationList[gradLocationIndex].getCity(),
                        "graduation",
                        gradYear
                ));

        int deathYear;

        int YearsTilMax = (MAX_AGE - (gradYear - birthYear) + 1);
        do {
            deathYear = (gradYear + seed.nextInt((YearsTilMax)));
        } while (deathYear < fatherGradYear);

        String DeathID = UUID.randomUUID().toString();
        int deathLocationIndex = seed.nextInt(locationList.length);
        eventArrayList.add(
                new Event(
                        DeathID,
                        user.getUsername(),
                        user.getPersonID(),
                        locationList[deathLocationIndex].getLatitude(),
                        locationList[deathLocationIndex].getLongitude(),
                        locationList[deathLocationIndex].getCountry(),
                        locationList[deathLocationIndex].getCity(),
                        "death",
                        deathYear
                ));
    }


    private void AddNewGeneration(int genCount, Person child)   {
        if(genCount > generations) { return; }
        if (genCount >=1){
            currentGen = genCount;
        }


        String fatherID = UUID.randomUUID().toString();
        String motherID = UUID.randomUUID().toString();
        child.setFatherID(fatherID);
        child.setMotherID(motherID);


        Person father = new Person(maleNames[seed.nextInt(maleNames.length)], child.getLastName(), "M",  fatherID, motherID,"", "",  child.getUsername());
        Person mother = new Person(femaleNames[seed.nextInt(maleNames.length)], lastNames[seed.nextInt(lastNames.length)], "F",  motherID, fatherID,"", "",  child.getUsername());


        GenerateAncestorEvents(father, mother);

        genCount++;

        AddNewGeneration(genCount, father);
        AddNewGeneration(genCount, mother);

        personArrayList.add(father);
        personArrayList.add(mother);
    }

    private void GenerateAncestorEvents(Person tempFather, Person tempMother)   {
        GenerateBirthEvents(tempFather, tempMother);
        GenerateGradEvents(tempFather, tempMother);
        GenerateMarriageEvent(tempFather, tempMother);
        GenerateDeathEvents(tempFather, tempMother);
    }


    private void GenerateBirthEvents(Person father, Person mother)   {

        fatherBirthYear = 2000 - (24*(currentGen));

        String fatherBirthID = UUID.randomUUID().toString();
        int fatherLocationIndex = seed.nextInt(locationList.length);
        eventArrayList.add(
                new Event(
                        fatherBirthID,
                        father.getUsername(),
                        father.getPersonID(),
                        locationList[fatherLocationIndex].getLatitude(),
                        locationList[fatherLocationIndex].getLongitude(),
                        locationList[fatherLocationIndex].getCountry(),
                        locationList[fatherLocationIndex].getCity(),
                        "birth",
                        fatherBirthYear
                ));

        motherBirthYear = 2000 - (24*(currentGen));

        String motherBirthID = UUID.randomUUID().toString();
        int motherLocationIndex = seed.nextInt(locationList.length);
        eventArrayList.add(
                new Event(
                        motherBirthID,
                        mother.getUsername(),
                        mother.getPersonID(),
                        locationList[motherLocationIndex].getLatitude(),
                        locationList[motherLocationIndex].getLongitude(),
                        locationList[motherLocationIndex].getCountry(),
                        locationList[motherLocationIndex].getCity(),
                        "birth",
                        motherBirthYear
                ));
    }

    private void GenerateMarriageEvent(Person father, Person mother)   {

        do {
            marriageYear = (motherBirthYear + seed.nextInt(MAX_AGE - MIN_MARRIAGEAGE) + MIN_MARRIAGEAGE);
        } while ((marriageYear - motherBirthYear) > MAX_AGE || (marriageYear - fatherBirthYear) > MAX_AGE);

        String fatherMarriageID = UUID.randomUUID().toString();
        int LocationIndex = seed.nextInt(locationList.length);
        eventArrayList.add(
                new Event(
                        fatherMarriageID,
                        father.getUsername(),
                        father.getPersonID(),
                        locationList[LocationIndex].getLatitude(),
                        locationList[LocationIndex].getLongitude(),
                        locationList[LocationIndex].getCountry(),
                        locationList[LocationIndex].getCity(),
                        "marriage",
                        marriageYear
                ));

        String motherMarriageID = UUID.randomUUID().toString();
        eventArrayList.add(
                new Event(
                        motherMarriageID,
                        mother.getUsername(),
                        mother.getPersonID(),
                        locationList[LocationIndex].getLatitude(),
                        locationList[LocationIndex].getLongitude(),
                        locationList[LocationIndex].getCountry(),
                        locationList[LocationIndex].getCity(),
                        "marriage",
                        marriageYear
                ));
    }

    private void GenerateDeathEvents(Person father, Person mother)   {

        int fatherDeathYear;
        int motherDeathYear;

        int fatherLastEvent = Math.max(fatherGradYear, marriageYear);
        int fatherYearsUntilMax = (MAX_AGE - (fatherLastEvent - fatherBirthYear) + 1);
        do {
            fatherDeathYear = (fatherLastEvent + seed.nextInt((fatherYearsUntilMax)));
        } while (fatherDeathYear < fatherGradYear || fatherDeathYear < marriageYear);

        String fatherDeathID = UUID.randomUUID().toString();
        int fatherLocationIndex = seed.nextInt(locationList.length);
        eventArrayList.add(
                new Event(
                        fatherDeathID,
                        father.getUsername(),
                        father.getPersonID(),
                        locationList[fatherLocationIndex].getLatitude(),
                        locationList[fatherLocationIndex].getLongitude(),
                        locationList[fatherLocationIndex].getCountry(),
                        locationList[fatherLocationIndex].getCity(),
                        "death",
                        fatherDeathYear
                ));

        int motherLastEvent = Math.max(motherGradYear, marriageYear);
        int motherYearsUntilMax = (MAX_AGE - (motherLastEvent - motherBirthYear) + 1);
        do {
            motherDeathYear = (motherLastEvent + seed.nextInt((motherYearsUntilMax)));
        } while (motherDeathYear < motherGradYear || motherDeathYear < marriageYear);


        String motherDeathID = UUID.randomUUID().toString();
        int motherLocationIndex = seed.nextInt(locationList.length);
        eventArrayList.add(
                new Event(
                        motherDeathID,
                        mother.getUsername(),
                        mother.getPersonID(),
                        locationList[motherLocationIndex].getLatitude(),
                        locationList[motherLocationIndex].getLongitude(),
                        locationList[motherLocationIndex].getCountry(),
                        locationList[motherLocationIndex].getCity(),
                        "death",
                        motherDeathYear
                ));
    }

    private void GenerateGradEvents(Person father, Person mother)   {
        do {
            fatherGradYear = (fatherBirthYear + seed.nextInt(MAX_AGE - MIN_GRADUATIONAGE) + MIN_GRADUATIONAGE);
        } while ((fatherGradYear - fatherBirthYear) > MAX_AGE);

        String fatherGradID = UUID.randomUUID().toString();
        int fatherLocationIndex = seed.nextInt(locationList.length);
        eventArrayList.add(
                new Event(
                        fatherGradID,
                        father.getUsername(),
                        father.getPersonID(),
                        locationList[fatherLocationIndex].getLatitude(),
                        locationList[fatherLocationIndex].getLongitude(),
                        locationList[fatherLocationIndex].getCountry(),
                        locationList[fatherLocationIndex].getCity(),
                        "graduation",
                        fatherGradYear
                ));

        do {
            motherGradYear = (motherBirthYear + seed.nextInt(MAX_AGE - MIN_GRADUATIONAGE) + MIN_GRADUATIONAGE);
        } while ((motherGradYear - motherBirthYear) > MAX_AGE);

        String motherGradID = UUID.randomUUID().toString();
        int motherLocationIndex = seed.nextInt(locationList.length);
        eventArrayList.add(
                new Event(
                        motherGradID,
                        mother.getUsername(),
                        mother.getPersonID(),
                        locationList[motherLocationIndex].getLatitude(),
                        locationList[motherLocationIndex].getLongitude(),
                        locationList[motherLocationIndex].getCountry(),
                        locationList[motherLocationIndex].getCity(),
                        "graduation",
                        motherGradYear
                ));
    }

}
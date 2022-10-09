package main.java.tracker;

import main.java.tracker.Exceptions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * Provides tools to add students, add points to students, list students,
 * printing statistics and showing detailed course statistics. Contains user interface method.
 */
public class Tracker {
    private int idFreeToUse = 1000;
    private HashSet<String> usedEmails = new HashSet<>();
    HashMap<String, Integer> submissions = new HashMap<>()
    {{
        put("Java", 0);        //Put courses with submissions set to 0 here
        put("DSA", 0);
        put("Databases", 0);
        put("Spring", 0);
    }};

    private LinkedHashMap<String, Student> students = new LinkedHashMap<>();

    private Notifier notifier = new Notifier(students);
    boolean isOn = true;


    /**
     * adds points to student defined by id. Correct form of query is: [id] [Java] [DSA] [Databases] [Spring]
     * @param input string containing query
     * @throws IllegalArgumentPassedException throws this exception if the passed query
     * contains is in wrong format or not such a student is found.
     */
    void addPointsToSingleStudent(String input) throws IllegalArgumentPassedException {
        try {
            String userInput = input;

            if (! userInput.matches("\\w+ [0-9]+ [0-9]+ [0-9]+ [0-9]+")) {
                throw new IllegalArgumentPassedException("Incorrect points format");
            }

            String[] splitInput = userInput.split(" ");
            int points[] = new int[4];
            for (int i = 0; i < 4; i++) {
                points[i] = Integer.parseInt(splitInput[i + 1]);
            }


            String userId = splitInput[0];
            if (!students.containsKey(userId)) {
                throw new IllegalArgumentPassedException("No student is found for id=" + userId);
            }

            students.get(userId).addPoints(points[0], points[1], points[2], points[3]);

            //Updating number of submissions
            for (int i = 0; i < 4; i++) {
                if (points[i] > 0){
                    switch (i){
                        case 0:
                            submissions.put("Java", submissions.get("Java") + 1);
                            break;
                        case 1:
                            submissions.put("DSA", submissions.get("DSA") + 1);
                            break;
                        case 2:
                            submissions.put("Databases", submissions.get("Databases") + 1);
                            break;
                        case 3:
                            submissions.put("Spring", submissions.get("Spring") + 1);
                            break;
                    }
                }
            }

            System.out.println("Points updated");
        } catch (IllegalArgumentPassedException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * User interface for adding points to students. Works in loop till 'back' command is used.
     * For each loop execute asks user for user id and points to add.
     */
    void addPoints(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter an id and points or 'back' to return.");

        while (true) {
            try {
                String userInput = scanner.nextLine();

                if (userInput.equals("back")){
                    return;
                }

                addPointsToSingleStudent(userInput);
            } catch (IllegalArgumentPassedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * User interface for adding students. Works in loop till 'back' command is used.
     * For each loop execute asks user for user first name, last name and email.
     */
    void addStudents(){
        Scanner scanner = new Scanner(System.in);
        int total = 0;

        System.out.println("Enter student credentials or 'back' to return:");

        while (true){
            String name = scanner.nextLine();

            if (name.equals("back")){
                System.out.printf("Total %d students have been added.\n", total);
                break;
            }

            try {
                addStudent(name);

                total++;
                idFreeToUse++;
            } catch (IllegalArgumentPassedException e) {
                //just a catch
            }
        }
    }

    /**
     * Adds student to main.java.tracker.
     * @param name string consists of first name, last name and email.
     * @return student object with all information passed into params.
     * @throws IllegalArgumentPassedException Throws exception in case validation conditions (passed name or email) are wrong.
     */
    Student addStudent(String name) throws IllegalArgumentPassedException {
        String firstName, lastName, email;

        try {

            if (! name.matches(".+\\s.+\\s.+")) {
                throw new IllegalArgumentPassedException("Incorrect credentials.");
            }

            //getting first and last name from name
            firstName = name.substring(0, name.indexOf(" "));
            lastName = name.substring(name.indexOf(" ") + 1, name.lastIndexOf(" "));
            email = name.substring(name.lastIndexOf(" ") + 1);

            //validation conditions
            if (usedEmails.contains(email)){
                throw new IllegalArgumentPassedException("This email is already taken.");
            }

            if (! firstName.matches("\\w+[-']?[a-zA-Z]+") || firstName.length() < 2){
                throw new IllegalArgumentPassedException("Incorrect first name.");
            }

            if (! lastName.matches("[a-zA-Z]+([ '-]?[a-zA-Z]+)*") || lastName.length() < 2){
                throw new IllegalArgumentPassedException("Incorrect last name.");
            }

            if (! email.matches("[\\d\\.a-zA-Z]+@[\\da-zA-Z]+\\.[\\da-zA-Z]+")){
                throw new IllegalArgumentPassedException("Incorrect email.");
            }

            Student student = new Student(firstName, lastName, email);
            usedEmails.add(email);
            students.put(String.valueOf(idFreeToUse), student);
            System.out.println("The student has been added.");

            return student;

        } catch (IllegalArgumentPassedException e) {
            System.out.println(e.getMessage());
            throw e;
        }

    }

    /**
     * User interface for exiting programme.
     */
    void exit(){
        System.out.println("Bye!");

        isOn = false;
    }

    /**
     * User interface for printing user course points.
     */
    void printStudentPoints(){
        System.out.println("Enter an id or 'back' to return");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                String userInput = scanner.nextLine();
                if (userInput.equals("back")) {
                    break;
                }

                if (! students.containsKey(userInput)){
                    throw new IllegalArgumentPassedException("No student is found for id=" + userInput);
                }

                System.out.printf(userInput + " points: ");
                students.get(userInput).printPoints();

            } catch (IllegalArgumentPassedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Initiates whole main.java.tracker tools and user interface.
     */
    void init(){
        System.out.println("Learning Progress Tracker");
        Notifier notifier = new Notifier(students);
        menu();
    }

    /**
     * Prints all id used for students adding system.
     * @throws IllegalArgumentPassedException
     */
    void listStudents() throws IllegalArgumentPassedException {
        if (usedEmails.size() < 1){
            throw new IllegalArgumentPassedException("No students found.");
        }

        System.out.println("Students:");

        students.forEach((x, y) -> System.out.println(x));
    }

    /**
     * User interface for main menu. User is able to choose action to perform, by this method.
     */
    void menu(){
        Scanner scanner = new Scanner(System.in);

            while (isOn) {
                try {
                    String userInput = scanner.nextLine();
                    if (userInput.equals("exit")) {
                        exit();
                    } else if (userInput.equals("add students")) {
                        addStudents();
                    } else if (userInput.equals("back")) {
                        System.out.println("Enter 'exit' to exit the program.");
                    } else if (userInput.trim().equals("")) {
                        System.out.println("No input.");
                    } else if (userInput.trim().equals("list")) {
                        listStudents();
                    } else if (userInput.trim().equals("add points")) {
                        addPoints();
                    } else if (userInput.trim().equals("find")) {
                        printStudentPoints();
                    } else if (userInput.trim().equals("statistics")) {
                        statistics();
                    } else if (userInput.trim().equals("notify")) {
                        notifyStudents();
                    } else {
                        throw new UnknownCommandException("Unknown command!");
                    }
                } catch (UnknownCommandException e) {
                    System.out.println(e.getMessage());
                } catch (IllegalArgumentPassedException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

    }

    /**
     * Notifies all students which have completed courses and prints the total number of students notified.
     */
    public void notifyStudents(){
        notifier.setStudents(students);
        notifier.notifyStudents();
    }

    /**
     * User interface for using statistics tools.
     */
    public void statistics(){
        CourseStatistics courseStatistics = new CourseStatistics(students, submissions);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Type the name of a course to see details or 'back' to quit:");
        courseStatistics.printGeneralStatistics();


            while (true) {
                try {
                String userInput = scanner.nextLine().trim();

                if (userInput.equals("back")) {
                    return;
                }

                courseStatistics.printDetailedStatistics(userInput);
            } catch (IllegalArgumentPassedException ex){
                    System.out.println(ex.getMessage());
                }
        }

    }
}

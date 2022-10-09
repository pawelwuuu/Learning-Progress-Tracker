package main.java.tracker;

import main.java.tracker.Exceptions.IllegalArgumentPassedException;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

/**
 * Provides statistics tools. It is able to show general statistics about submission, enrolled students and so on. Also
 * it is able to print detailed statistics about some student.
 */
public class CourseStatistics{
    public static HashMap<String, Integer> maxCoursePoints = new HashMap<>()
    {{
        put("Java", 600);
        put("DSA", 400);
        put("Databases", 480);
        put("Spring", 550);
    }};
    private LinkedHashMap<String, Student> students;
    private HashMap<String, Integer> submissions;
    private HashMap<String, Integer> summedCoursesPoints = new HashMap<>()
    {{
        put("Java", 0);
        put("DSA", 0);
        put("Databases", 0);
        put("Spring", 0);
    }};

    private HashMap<String, Integer> enrolledStudents = new HashMap<>()
    {{
        put("Java", 0);
        put("DSA", 0);
        put("Databases", 0);
        put("Spring", 0);
    }};

    /**
     * Constructs statistics object.
     * @param students LinkedHashMap with students. Key - String with student id, Value - student object.
     * @param submissions HashMap with submissions. Key - String with course name, Value - Integer containing submissions of course.
     */
    public CourseStatistics(LinkedHashMap<String, Student> students, HashMap submissions) {
        this.students = students;
        this.submissions = submissions;

        for (Student student: students.values()){
            HashMap<String, Integer> points = student.getPoints();

            for (var entry: points.entrySet()){
                if(entry.getValue() > 0){
                    enrolledStudents.put(entry.getKey(), enrolledStudents.get(entry.getKey()) + 1);
                }

                summedCoursesPoints.put(entry.getKey(), points.get(entry.getKey()) + summedCoursesPoints.get(entry.getKey()));
            }
        }
    }

    /**
     * Prints detailed statistics about course.
     * @param courseName string with name of the course.
     */
    void printDetailedStatistics(String courseName) throws IllegalArgumentPassedException {
        try {
            SortedMap<String, Integer> usersPoints = getDetailedCourseStatistics(courseName);
            usersPoints = DetailedStatisticsComparator.valueSort( usersPoints);

            System.out.println(courseName);
            System.out.println("id    points    completed");

            for (var entry: usersPoints.entrySet()){
                int points = entry.getValue();
                String id = entry.getKey();
                double percentageOfCompletion = (double) points / (double) maxCoursePoints.get(courseName) * 100f;

                DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
                decimalFormatSymbols.setDecimalSeparator('.');

                DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance();
                decimalFormat.setMaximumFractionDigits(1);
                decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);

                System.out.printf("%s %d    %s%%\n",
                        id, points, Double.valueOf(decimalFormat.format(percentageOfCompletion)));
            }
        } catch (IllegalArgumentPassedException ex) { //TODO check if it is necessary.
            throw ex;
        }
    }

    /**
     * returns SortedMap with course statistics with order //TODO dokonczyc dokumentacje.
     * @param courseName
     * @return
     * @throws IllegalArgumentPassedException
     */
    SortedMap getDetailedCourseStatistics(String courseName) throws IllegalArgumentPassedException {
        SortedMap<String, Integer> usersPoints = new TreeMap<>();


        if (! summedCoursesPoints.containsKey(courseName)){
            throw new IllegalArgumentPassedException("Unknown course.");
        }

        for (var entry: students.entrySet()){

            HashMap<String, Integer> studentPoints = entry.getValue().getPoints();
            if (studentPoints.get(courseName) > 0){
                usersPoints.put(entry.getKey(), studentPoints.get(courseName));
            }
        }

        return usersPoints;
    }

    /**
     * Prints general statistics which consist of course names that are least/most popularity,
     * highest/lowest activity and easiest/hardest course. If There is no course enrolled then it will print statistics
     * with n/a mark.
     */
    void printGeneralStatistics(){
        if (! isAnyCourseEnrolled()){
            System.out.println("Most popular: n/a\n" +
                    "Least popular: n/a\n" +
                    "Highest activity: n/a\n" +
                    "Lowest activity: n/a\n" +
                    "Easiest course: n/a\n" +
                    "Hardest course: n/a");
            return;
        }

        System.out.printf("Most popular: %s\n", findMostPopular());
        System.out.printf("Least popular: %s\n", findLeastPopular());
        System.out.printf("Highest activity: %s\n", findMostActive());
        System.out.printf("Lowest activity: %s\n", findLeastActive());
        System.out.printf("Easiest course: %s\n", findEasiest());
        System.out.printf("Hardest course: %s\n", findHardest());
    }

    /**
     * Defines if any course is enrolled.
     * @return true boolean if any course is enrolled.
     */
    boolean isAnyCourseEnrolled(){
        for (int enrolledCount: enrolledStudents.values()){
            if (enrolledCount > 0){
                return true;
            }
        }

        return false;
    }

    String findEquals(String toFindEqual, Map<String, Integer> map) {
        String equals = toFindEqual;
        int value = (int) map.get(toFindEqual);

        for (var entry : map.entrySet()) {
            if (entry.getValue() == value && ! entry.getKey().equals(toFindEqual)){
                equals += " " + entry.getKey();
            }
        }

        return equals;
    }

    /**
     * Finds the hardest course. If the course has the lowest average score then it is the hardest.
     * @return string name of the hardest course.
     */
    String findHardest(){
        float highestAvarage = 99999;
        String highestAvarageCourse = "";

        for (var entry: summedCoursesPoints.entrySet()){
            if (submissions.get(entry.getKey()) == 0 || entry.getValue() == 0){
                continue;
            }

            if (highestAvarage > entry.getValue() / submissions.get(entry.getKey())){
                highestAvarage = entry.getValue() / submissions.get(entry.getKey());
                highestAvarageCourse = entry.getKey();
            }
        }

        return highestAvarageCourse;
    }

    /**
     * Finds the easiest course. If the course has the highest average score then it is the easiest.
     * @return string name of the easiest course.
     */
    String findEasiest(){
        float easiestAverage = -1;
        String easiestAverageCourse = "";

        for (var entry: summedCoursesPoints.entrySet()){
            if (submissions.get(entry.getKey()) == 0 || entry.getValue() == 0){
                continue;
            }

            float points = entry.getValue();
            float submission = submissions.get(entry.getKey());
            if (easiestAverage < points / submission) {
                easiestAverage = points / submission;
                easiestAverageCourse = entry.getKey();
            }
        }

        return easiestAverageCourse;
    }

    /**
     * Finds the most popular course. If the course has the highest amount of enrolled students then it is the most popular.
     * @return string name of the most popular course.
     */
    String findMostPopular(){
        int max = -1;
        String mostPoppularCourse = "";

        for (var entry: enrolledStudents.entrySet()){
            if (max < entry.getValue()){
                max = entry.getValue();
                mostPoppularCourse = entry.getKey();
            }
        }

        mostPoppularCourse = findEquals(mostPoppularCourse, enrolledStudents);
        return mostPoppularCourse;
    }
    /**
     * Finds the least popular course. If the course has the lowest amount of enrolled students then it is the least popular.
     * @return string name of the least popular course.
     */
    String findLeastPopular(){
        int min = 9999;
        String leastPoppularCourse = "";

        for (var entry: enrolledStudents.entrySet()){
            if (min > entry.getValue()){
                min = entry.getValue();
                leastPoppularCourse = entry.getKey();
            }
        }

        String equalCourses = findEquals(leastPoppularCourse, enrolledStudents);
        if (leastPoppularCourse.length() < equalCourses.length()){
            return "n/a";
        }

        return leastPoppularCourse;
    }
    /**
     * Finds the most active course. If the course has the highest amount of submissions then it is the most active.
     * @return string name of the most active course.
     */
    String findMostActive(){
        int max = -1;
        String mostActiveCourse = "";

        for (var entry: submissions.entrySet()){
            if (max < entry.getValue()){
                max = entry.getValue();
                mostActiveCourse = entry.getKey();
            }
        }

        mostActiveCourse = findEquals(mostActiveCourse, submissions);
        return mostActiveCourse;
    }

    /**
     * Finds the least active course. If the course has the lowest amount of submissions then it is the least active.
     * @return string name of the least active course.
     */
    String findLeastActive(){
        int min = 999999;
        String leastActiveCourse = "";

        for (var entry: submissions.entrySet()){
            if (min > entry.getValue()){
                min = entry.getValue();
                leastActiveCourse = entry.getKey();
            }
        }

        String equalCourses = findEquals(leastActiveCourse, enrolledStudents);
        if (leastActiveCourse.length() < equalCourses.length()){
            return "n/a";
        }

        return leastActiveCourse;
    }
}

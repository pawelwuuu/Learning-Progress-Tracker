package tracker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * Provides tools to notify students that have complete any courses. Contains information about already notified students
 * so none of them will be notified twice.
 */
public class Notifier {
    HashMap<String, HashSet<String>> notifiedStudents = new HashMap<>();
    LinkedHashMap<String, Student> students;

    /**
     * Constructs notifier with passed student as actual list of them.
     * @param students LinkedHash<String, Student> containing id as string and Student object.
     */
    public Notifier(LinkedHashMap<String, Student> students) {
        this.students = students;
    }

    /**
     * returns HashMap of students to notify. HashMap is student id as key and HashSet of completed courses by student as value.
     * @return HashMap containing student id and courses completed by it.
     */
    public HashMap<String, HashSet<String>> getStudentsToNotify() {
        HashMap<String, HashSet<String>> studentsToNotify = new HashMap<>();

        for (var entry: students.entrySet()){
            HashMap<String, Integer> studentPoints = entry.getValue().getPoints();
            String studentId = entry.getKey();
            HashMap<String, Integer> maxCoursePoints = CourseStatistics.maxCoursePoints;
            HashSet<String> completedCourses = new HashSet<>();



            for (var studentPointsInfo: studentPoints.entrySet()){
                if (studentPointsInfo.getValue() >= maxCoursePoints.get(studentPointsInfo.getKey())
                        && notifiedStudents.containsKey(studentId)
                        && ! notifiedStudents.get(studentId).contains(studentPointsInfo.getKey())) {

                    completedCourses.add(studentPointsInfo.getKey());
                } else if (studentPointsInfo.getValue() >= maxCoursePoints.get(studentPointsInfo.getKey())
                        && ! notifiedStudents.containsKey(studentId)) {

                    completedCourses.add(studentPointsInfo.getKey());
                }

                if (! completedCourses.isEmpty()){
                    studentsToNotify.put(studentId, completedCourses);
                }

            }
        }

        return studentsToNotify;
    }

    /**
     * Sets students to provided stutends LinkedHashMap.
     * @param students LinkedHashMap containing Students with string id as a key and student object as value.
     */
    public void setStudents(LinkedHashMap<String, Student> students) {
        this.students = students;
    }

    /**
     * notifies all students that have completed any of courses and have not been notified yet. Also prints
     * information about total notified students.
     */
    public void notifyStudents(){
        HashMap<String, HashSet<String>> studentsToNotify = getStudentsToNotify();
        int notifiedStudentsCount = studentsToNotify.size();

        for (var entry: studentsToNotify.entrySet()){
            String studentId = entry.getKey();
            HashSet<String> courseNames = entry.getValue();
            Student student = students.get(studentId);

            for (String courseName: courseNames){
                System.out.printf("To: %s\n", student.getEmail());
                System.out.printf("Re: Your Learning Progress\n");
                System.out.printf(
                        "Hello, %s %s! You have accomplished our %s course!\n",
                        student.getFirstName(), student.getLastname(), courseName);
            }

            notifiedStudents.put(studentId, courseNames);
        }

        System.out.printf("Total %d students have been notified.\n", notifiedStudentsCount);
    }
}

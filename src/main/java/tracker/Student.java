package tracker;

import java.util.HashMap;

/**
 * Represents student, contains it's first name, last name and email. Also contains information about amount of
 * points earned in detailed courses.
 */
public class Student {
    final String firstName, lastname, email;
    private HashMap<String, Integer> points = new HashMap<>()
    {{
        put("Java", 0);
        put("DSA", 0);
        put("Databases", 0);
        put("Spring", 0);
    }};

    /**
     * Constructs student with specified name and email.
     * @param firstName string containing student first name.
     * @param lastname string containing student last name.
     * @param email string containing student email.
     */
    public Student(String firstName, String lastname, String email) {
        this.firstName = firstName;
        this.lastname = lastname;
        this.email = email;
    }

    /**
     * returns first name.
     * @return string containing first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * returns last name.
     * @return string containing last name.
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * returns email.
     * @return string containing email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * returns HashMap of points with name of course as Key and Integer points as value.
     * @return hashmap containing course - points.
     */
    public HashMap<String, Integer> getPoints() {
        return points;
    }

    /**
     * Adds points to certain courses.
     * @param Java int with points to add for course Java.
     * @param DSA int with points to add for course DSA.
     * @param Databases int with points to add for course Databases.
     * @param Spring int with points to add for course Spring.
     */
    public void addPoints(int Java, int DSA, int Databases, int Spring) {
        points.put("Java", points.get("Java") + Java);
        points.put("DSA", points.get("DSA") + DSA);
        points.put("Databases", points.get("Databases") + Databases);
        points.put("Spring", points.get("Spring") + Spring);
    }

    /**
     * Prints student courses points.
     */
    public void printPoints(){
        System.out.printf("Java=%d; DSA=%d; Databases=%d; Spring=%d\n",
                points.get("Java"),
                points.get("DSA"),
                points.get("Databases"),
                points.get("Spring")
                );
    }


}

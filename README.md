# Learning Progress Tracker
This project is a simulation of a management tool for a company which offers programming courses. Company offers courses for Java, DSA, Databases and Spring. From these courses student can get points: 600 for Java, 400 for DSA, 480 for Databases, and 550 for Spring.It is build on command line interface and provides tools to add students to the data, managing points that users are getting from courses, retrieving statistics about both all courses in genral and certain course in details. Also it can notify students that have completed any courses.

## How to run it
Run the JAR file via terminal and java command.
```bash
java -jar Learning-Progress-Tracker.jar
```

## How to use it
This application is based on terminal, though you can use it only by various commands which are following:
### add students
To add students you need to use command 
```terminal
add students
```
When adding students interface will trun on you can start adding users. Users can only be added in the following form: 
```teminal
first_name last_name email 
```
Every added student get id bound to itself.

### list
If you want to get list of students id's, use list command.
```terminal
list
```
List command shows all ids that was attached to students durring creational process.

### add points
To add points to any student, you have to know it's id.
```terminal
add points
```
When adding points interface will trun on you can start adding points to users. To do that specify 5 things in following format: <br>

```terminal
student_id Java_points DSA_points Databases_points Spring_points
```

### statistics
To see general courses statistics type this command:
```terminal
statistics
```
If you rather want to see detailed statistics about some course, type it's name for example Java or DSA.

### notify
Assume that you want to notify students that have completed any courses. It is possible to do thanks to notify command.
```terminal
notify
```

Notify command will print (only once per course) all students that have completed any of available courses.

## License
Programme is released under GNU GPL license.

## Download JAR
Download jar [here](https://github.com/pawelwuuu/Learning-Progress-Tracker/releases/download/app/Learning-Progress-Tracker.jar).

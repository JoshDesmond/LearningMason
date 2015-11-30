package main;

import sim.engine.SimState;
import sim.field.continuous.Continuous2D;
import sim.field.network.Network;
import sim.util.Bag;
import sim.util.Double2D;

@SuppressWarnings("serial")
public class Students extends SimState {

    // A representation of space in a simulation. 100x100 grid with the 1.0
    // showing how the grid is broken up or something.
    public Continuous2D yard = new Continuous2D(1.0, 100, 100);
    // representation of friendships.
    public Network buddies = new Network(false);
    // Logic field/ Set students
    public int numStudents = 50;
    // Simulation fields
    double forceToSchoolMultiplier = 0.1;
    double randomMultiplier = 0.2;

    public Students(long seed) {
        super(seed);
    }

    @Override
    public void start() {
        super.start();

        yard.clear();
        buddies.clear();

        startStudentsPosition();

        // Define like/ dislike relationships:
        Bag students = buddies.getAllNodes();
        for (int i = 0; i < students.size(); i++) {
            Object student = students.get(i);

            // find a student b that the kid will like.
            Object studentB = getDifferentStudent(student);
            // Calculate friendship
            double buddieness = random.nextDouble();
            buddies.addEdge(student, studentB, new Double(buddieness));

            // now find another student b that the kid will dislike.
            studentB = getDifferentStudent(student);
            // Calculate hatred
            buddieness = random.nextDouble();
            buddies.addEdge(student, studentB, new Double(-buddieness));
        }
    }

    /** grabs a student from buddies that's different that the given student */
    private Object getDifferentStudent(Object student) {
        Bag students = buddies.getAllNodes();
        Object studentB = null;

        do { // get another student b that's different
            studentB = students.get(random.nextInt(students.numObjs));
        } while (student == studentB);

        return studentB;
    }

    /**
     * Creates numStudents students, sets their position, and adds them to
     * buddies. (And schedules them).
     */
    private void startStudentsPosition() {
        for (int i = 0; i < numStudents; i++) {
            Student student = new Student();
            yard.setObjectLocation(student,
                    new Double2D(
                            yard.getWidth() * .5 + random.nextDouble() - .5,
                            yard.getHeight() * .5 + random.nextDouble() - .5));

            schedule.scheduleRepeating(student);
            buddies.addNode(student);
        }
    }

    public static void main(String[] args) {
        doLoop(Students.class, args);
        System.exit(0);
    }

}

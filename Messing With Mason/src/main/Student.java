package main;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.network.Edge;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.MutableDouble2D;

public class Student implements Steppable {

    public static final double MAX_FORCE = 3.0;
    double friendsClose = 0; // def value
    double enemiesCloser = 0; // def value

    @Override
    public void step(SimState state) {
        // cast state to Students
        Students students = (Students) state;

        friendsClose = enemiesCloser = 0.0;

        // acquire field(s) (me) from state.
        Double2D me = students.yard.getObjectLocation(this);
        MutableDouble2D forceVector = generateForceVector(students, me);

        // Sum vector force and location to get new location.
        forceVector.addIn(me);

        // When building location, use the Double2D constructor, because you
        // don't want position being mutable because it's a hash map.
        students.yard.setObjectLocation(this, new Double2D(forceVector));
    }

    /** generate force vector using my location and state of simulation. */
    private MutableDouble2D generateForceVector(Students students,
            Double2D myLocation) {

        // Go through my buddies and determine how much I want to be near them
        MutableDouble2D forceVector = new MutableDouble2D();
        Bag listOfEdges = students.buddies.getEdges(this, null);
        int sizeOfListOfEdges = listOfEdges.size();
        for (int i = 0; i < sizeOfListOfEdges; i++) {
            Edge e = (Edge) (listOfEdges.get(i));
            double buddiness = ((Double) (e.info)).doubleValue();
            // I could be in the to() end or the from() end. getOtherNode is a
            // cute function
            // which grabs the guy at the opposite end from me.
            Double2D him = students.yard
                    .getObjectLocation(e.getOtherNode(this));
            if (buddiness >= 0) // the further I am from him the more I want to
                                // go to him
            {
                forceVector.setTo((him.x - myLocation.x) * buddiness,
                        (him.y - myLocation.y) * buddiness);
                if (forceVector.length() > MAX_FORCE) // I’m far enough away
                    forceVector.resize(MAX_FORCE);

                friendsClose += forceVector.length();
            } else // the nearer I am to him the more I want to get away from
                   // him, up to a limit
            {
                forceVector.setTo((him.x - myLocation.x) * buddiness,
                        (him.y - myLocation.y) * buddiness);
                if (forceVector.length() > MAX_FORCE) // I’m far enough away
                    forceVector.resize(0.0);
                else if (forceVector.length() > 0)
                    forceVector.resize(MAX_FORCE - forceVector.length()); // invert
                                                                          // the
                                                                          // distance

                enemiesCloser += forceVector.length();
            }

            forceVector.addIn(forceVector);
        }

        // add in a vector to the "teacher" -- the center of the yard, so we
        // don’t go too far away
        forceVector.addIn(new Double2D(
                (students.yard.width * 0.5 - myLocation.x)
                        * students.forceToSchoolMultiplier,
                (students.yard.height * 0.5 - myLocation.y)
                        * students.forceToSchoolMultiplier));

        // add a bit of randomness
        forceVector.addIn(new Double2D(
                students.randomMultiplier
                        * (students.random.nextDouble() * 1.0 - 0.5),
                students.randomMultiplier
                        * (students.random.nextDouble() * 1.0 - 0.5)));

        return forceVector;
    }

    public double getAgitation() {
        return friendsClose + enemiesCloser;
    }
}

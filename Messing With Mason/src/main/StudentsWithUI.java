package main;

import java.awt.Color;

import javax.swing.JFrame;

import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.continuous.ContinuousPortrayal2D;
import sim.portrayal.network.NetworkPortrayal2D;
import sim.portrayal.network.SimpleEdgePortrayal2D;
import sim.portrayal.network.SpatialNetwork2D;
import sim.portrayal.simple.OvalPortrayal2D;

public class StudentsWithUI extends GUIState {

    public Display2D display;
    public JFrame displayFrame;
    ContinuousPortrayal2D yardPortrayal = new ContinuousPortrayal2D();
    NetworkPortrayal2D networkPortrayal = new NetworkPortrayal2D();

    public static void main(String[] args) {
        StudentsWithUI vid = new StudentsWithUI();
        Console c = new Console(vid);
        c.setVisible(true);
    }

    public StudentsWithUI() {
        super(new Students(System.currentTimeMillis()));
    }

    public StudentsWithUI(SimState state) {
        super(state);
    }

    public static String getName() {
        return "Student Schoolyard Cliques";
    }

    @Override
    public void start() {
        super.start();
        setupPortrayals();
    }

    @Override
    public void load(SimState state) {
        super.load(state);
        setupPortrayals();
    }

    public void setupPortrayals() {
        Students students = (Students) state;
        // tell the portrayals what to portray and how to portray them
        yardPortrayal.setField(students.yard);
        yardPortrayal.setPortrayalForAll(new OvalPortrayal2D());

        // Code added to describe network visualization.
        networkPortrayal.setField(
                new SpatialNetwork2D(students.yard, students.buddies));
        networkPortrayal.setPortrayalForAll(new SimpleEdgePortrayal2D());

        // reschedule the displayer
        display.reset();
        display.setBackdrop(Color.white);
        // redraw the display
        display.repaint();
    }

    @Override
    // Called when GUI is initially created
    public void init(Controller controller) {
        super.init(controller);

        display = new Display2D(600, 600, this);
        display.setClipping(false);
        displayFrame = display.createFrame();
        displayFrame.setTitle("Schoolyard Display");
        controller.registerFrame(displayFrame); // so the frame appears in the
                                                // "Display" list
        displayFrame.setVisible(true);
        display.attach(yardPortrayal, "Yard");
        display.attach(networkPortrayal, "Buddies");
    }

    @Override
    public void quit() {
        super.quit();
        if (displayFrame != null)
            displayFrame.dispose();
        displayFrame = null;
        display = null;
    }

}

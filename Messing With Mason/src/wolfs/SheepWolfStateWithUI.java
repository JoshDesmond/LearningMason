package wolfs;

import java.awt.Color;

import javax.swing.JFrame;

import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.continuous.ContinuousPortrayal2D;
import sim.portrayal.simple.OrientedPortrayal2D;

public class SheepWolfStateWithUI extends GUIState {

    public Display2D display;
    public JFrame displayFrame;
    ContinuousPortrayal2D gridPortrayal = new ContinuousPortrayal2D();

    public static void main(String[] args) {
        SheepWolfStateWithUI vid = new SheepWolfStateWithUI();
        Console c = new Console(vid);
        c.setVisible(true);
    }

    public SheepWolfStateWithUI() {
        super(new SheepWolfState(System.currentTimeMillis()));
    }

    public SheepWolfStateWithUI(SimState state) {
        super(state);
    }

    public static String getName() {
        return "Predetor Simulation";
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
        SheepWolfState castedState = (SheepWolfState) state;
        // tell the potrayals what to portray and how to portray them
        gridPortrayal.setField(castedState.grid);
        OrientedPortrayal2D ovalPortrayal = new WolfsOrientedPortrayal(2.0,
                Color.BLACK);
        ovalPortrayal.setShape(OrientedPortrayal2D.SHAPE_COMPASS);
        ovalPortrayal.setDrawFilled(true);
        gridPortrayal.setPortrayalForAll(ovalPortrayal);

        // reschedule the displayer
        display.reset();
        display.setBackdrop(Color.green);
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
        displayFrame.setTitle("Simulation Display");
        controller.registerFrame(displayFrame); // so the frame appears in the
                                                // "Display" list
        displayFrame.setVisible(true);
        display.attach(gridPortrayal, "Grid");
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

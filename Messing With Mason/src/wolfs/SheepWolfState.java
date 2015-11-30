package wolfs;

import java.util.HashMap;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.field.continuous.Continuous2D;
import sim.util.Double2D;

public class SheepWolfState extends SimState {
    private static final long serialVersionUID = 1L;

    public static final double WIDTH = 100d;
    public static final double HEIGHT = 100d;
    public Continuous2D grid = new Continuous2D(5, WIDTH, HEIGHT);

    public static final int INIT_WOLVES = 10;
    public static final int INIT_SHEEP = 50;

    public double sheepReproductionChange = .03;
    public double wolfReproductionChance = .03;

    HashMap<Steppable, Stoppable> map = new HashMap<Steppable, Stoppable>();

    @Override
    public void start() {
        super.start();
        grid.clear();
        map.clear();

        // Initialize Sheep
        for (int i = 0; i < INIT_SHEEP; i++) {
            Sheep sheep = new Sheep(random.nextDouble());
            addNewAnimal(sheep);
        }

        // Initialize Wolves
        for (int i = 0; i < INIT_WOLVES; i++) {
            Wolf wolf = new Wolf(random.nextDouble());
            addNewAnimal(wolf);
        }
    }

    /**
     * Creates a random Double2D from between (0,0) to (WIDTH, HEIGHT)
     */
    private Double2D generateRandomPoint() {
        final double x = random.nextDouble() * WIDTH;
        final double y = random.nextDouble() * HEIGHT;
        return new Double2D(x, y);
    }

    public SheepWolfState(long seed) {
        super(seed);
    }

    public static void main(String[] args) {
        doLoop(SheepWolfState.class, args);
        System.exit(0);
    }

    public double getSheepReproductionChance() {
        return sheepReproductionChange;
    }

    public double getWolfReproductionChance() {
        return wolfReproductionChance;
    }

    public void addNewAnimal(Animal newAnimal) {
        this.grid.setObjectLocation(newAnimal, generateRandomPoint());
        this.map.put(newAnimal, schedule.scheduleRepeating(newAnimal));

    }
}

package wolfs;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.portrayal.Oriented2D;
import sim.util.Double2D;
import sim.util.MutableDouble2D;

public abstract class Animal implements Steppable, Oriented2D, Drawable {
    private static final long serialVersionUID = 1L;
    private static final double ROTATION_CONSTANT = Math.PI / 6.0;

    /** The energy of an animal is a measurement of its life */
    public int energy;
    /** Angle between 0 and 2pi representing direction facing */
    public double theta;
    /**
     * Flag indicating (if true) at the end of step(), the animal should be
     * removed.
     */
    private boolean isDead = false;

    /**
     * Creates a new animal with an energy set to be between 0 and
     * <code>gainFromFood</code>.
     * 
     * @param gainFromFood
     *            an integer representing the amount of energy this animal gains
     *            from eating
     * @param randomDouble
     *            generated double which is used to decide where in the range of
     *            0 to gainFromFood the energy will start at.
     */
    public Animal(int gainFromFood, double randomDouble) {
        this.energy = (int) (gainFromFood * randomDouble);
    }

    /**
     * creates a new animal with the given energy
     * 
     * @param energy
     *            starting energy
     */
    public Animal(int energy) {
        this.energy = energy;
    }

    @Override
    public void step(SimState state) {
        SheepWolfState simState = (SheepWolfState) state;
        move(simState);
        checkEnergyState();
        if (isDead) {
            simState.grid.remove(this);
            simState.map.get(this).stop();
        } else {
            this.reproduce(simState);
        }
    }

    /**
     * updates isDead if energy is below 1.
     */
    private void checkEnergyState() {
        if (energy <= 0)
            isDead = true;
    }

    public void move(SheepWolfState state) {
        Double2D currentLocation = state.grid.getObjectLocation(this);
        MutableDouble2D movementVector = new MutableDouble2D(1, 0);
        theta += state.random.nextDouble() * ROTATION_CONSTANT;
        theta += -state.random.nextDouble() * ROTATION_CONSTANT;
        movementVector.rotate(theta);

        // Add movement to position to get a newPosition
        movementVector.addIn(currentLocation);
        double newX = state.grid.stx(movementVector.x);
        double newY = state.grid.sty(movementVector.y);
        Double2D newLocation = new Double2D(newX, newY);
        state.grid.setObjectLocation(this, newLocation);
    }

    /**
     * Duplicates the given animal, and splits energy in half between the two.
     * 
     * @param state
     *            State to add the new animal to.
     */
    protected abstract void reproduce(SheepWolfState state);

    protected boolean reproductionCoinFlip(SheepWolfState state) {
        return (state.random.nextBoolean(getReproductionChance(state)));
    }

    @Override
    public double orientation2D() {
        // Used for portrayals to show where the animal is facing.
        return theta;
    }

    public abstract double getReproductionChance(SheepWolfState state);

    public int getEnergy() {
        return this.energy;
    }

    /**
     * Return true if a wolf can eat this. I.e. Is it a sheep?
     */
    public abstract boolean isEatableByWolfs();

    /**
     * Sets a flag indicating that this animal is dead, which will be dealt with
     * next time step is called.
     */
    public void die() {
        this.isDead = true;
    }
}

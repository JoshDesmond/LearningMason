package wolfs;

import java.awt.Color;

import sim.engine.SimState;
import sim.field.continuous.Continuous2D;
import sim.util.Bag;
import sim.util.Double2D;

public class Wolf extends Animal {
    private static final long serialVersionUID = 1L;
    public static int WOLF_GAIN_FROM_FOOD = 20;

    public Wolf(int energy) {
        super(energy);
    }

    public Wolf(double randomDouble) {
        super(WOLF_GAIN_FROM_FOOD, randomDouble);
    }

    @Override
    public double getReproductionChance(SheepWolfState state) {
        return state.getWolfReproductionChance();
    }

    @Override
    public Color getColor() {
        return Color.BLACK;
    }

    @Override
    public void move(SheepWolfState state) {
        super.move(state);
        energy = energy - 1;
    }

    @Override
    public void step(SimState state) {
        SheepWolfState simState = (SheepWolfState) state;
        catchSheep(simState);
        super.step(simState);
    }

    private void catchSheep(SheepWolfState simState) {
        Continuous2D grid = simState.grid;
        Double2D location = grid.getObjectLocation(this);

        Bag neighbors = grid
                .getObjectsAtDiscretizedLocation(grid.discretize(location));

        if (neighbors.isEmpty())
            return; // If no neighbors give up.

        for (Object a : neighbors) {
            Animal animal = (Animal) a;
            if (animal.isEatableByWolfs()) {
                this.eatSheep(animal);
                break;
            }
        }

    }

    private void eatSheep(Animal animal) {
        energy = energy + WOLF_GAIN_FROM_FOOD;
        animal.die();
    }

    @Override
    public boolean isEatableByWolfs() {
        return false;
    }

    @Override
    protected void reproduce(SheepWolfState state) {
        if (getEnergy() < 2) {
            return;
        }

        if (this.reproductionCoinFlip(state)) {
            this.energy = this.energy / 2;
            Wolf newWolf = new Wolf(this.energy);
            state.addNewAnimal(newWolf);
        }

    }

}

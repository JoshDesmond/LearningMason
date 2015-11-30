package wolfs;

import java.awt.Color;

import sim.engine.SimState;

public class Sheep extends Animal {
    private static final long serialVersionUID = 1L;
    public static final int SHEEP_GAIN_FROM_FOOD = 3;
    private static final int COUNTER_RATE = 20;

    private int counter = 0;

    public Sheep(int energy) {
        super(energy);
    }

    public Sheep(double randomDouble) {
        super(SHEEP_GAIN_FROM_FOOD, randomDouble);
    }

    @Override
    public void step(SimState state) {
        counter++;
        if (counter > COUNTER_RATE) {
            this.energy = this.energy + SHEEP_GAIN_FROM_FOOD;
            counter = 0;
        }
        super.step(state);
    }

    @Override
    public double getReproductionChance(SheepWolfState state) {
        return state.getSheepReproductionChance();
    }

    @Override
    public Color getColor() {
        return Color.WHITE;
    }

    @Override
    public boolean isEatableByWolfs() {
        return true;
    }

    @Override
    protected void reproduce(SheepWolfState state) {
        if (this.getEnergy() < 2) {
            return;
        }

        if (this.reproductionCoinFlip(state)) {
            this.energy = this.energy / 2;
            Sheep newSheep = new Sheep(this.energy);
            state.addNewAnimal(newSheep);
        }

    }
}

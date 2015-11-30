package wolfs;

import java.awt.Color;
import java.awt.Graphics2D;

import sim.portrayal.DrawInfo2D;
import sim.portrayal.SimplePortrayal2D;
import sim.portrayal.simple.OrientedPortrayal2D;

public class WolfsOrientedPortrayal extends OrientedPortrayal2D {
    private static final long serialVersionUID = 1L;

    public WolfsOrientedPortrayal(SimplePortrayal2D child) {
        super(child);
    }

    public WolfsOrientedPortrayal(double scaling, Color color) {
        super(new SimplePortrayal2D(), 0, scaling, color);
    }

    @Override
    public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
        Drawable item = (Drawable) object;
        this.paint = item.getColor();
        super.draw(object, graphics, info);
    }

}

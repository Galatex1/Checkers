import java.awt.*;

/**
 * Created by Galatex on 16.5.2016.
 */

class Figure {

    final PlayerSelect playerSelect;
    Point position;
    boolean state =  false;


    Figure(PlayerSelect p, Point point)
    {
        playerSelect = p;
        position = point;

    }

    public int stateInt()
    {
        return (!this.state ? 0 : 1);
    }


}

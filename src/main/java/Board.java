import java.awt.*;

/**
 * Created by Galatex on 16.5.2016.
 */
class Board {



    Figure[][] figures;

    Board(){}

    Figure getFigure(Point p){
        if(p.x < 0 || p.y < 0 || p.x > 7 || p.y > 7)
            return null;

        if(figures[p.x][p.y]!=null)
            return figures[p.x][p.y];
        else
            return null;
    }

    public void moveFigure(Point _old, Point _new)
    {
        figures[_new.x][_new.y] = figures[_old.x][_old.y];
        figures[_old.x][_old.y] = null;
        figures[_new.x][_new.y].position = new Point(_new.x,_new.y);
    }
}

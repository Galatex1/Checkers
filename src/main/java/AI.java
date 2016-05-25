import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Galatex on 25.5.2016.
 */

public class AI
{
    List<Figure> figures_AI = new ArrayList<>();
    List<Figure> figures_PL = new ArrayList<>();

    AI(){

    };

    List<Figure> getFigures(boolean player)
    {
        List<Figure> figures = new ArrayList<>();

        for(Figure[] fig : Manager.ref.board.figures)
        {
            for(Figure f : fig)
            {
                if(f.playerSelect.getBool() == player)
                {
                    figures.add(f);
                }
            }
        }

        return figures;
    }

    List<Point[]> getPossibleMoves( List<Figure> p_figs)
    {
        List<Point[]> list = Manager.ref.getForcedMoves();
        if(list.size() > 0)
            return list;
        else
        {
            for(Figure f: p_figs)
            {
                for(Point p : Manager.ref.checkPositions(f))
                {
                    list.add(new Point[]{f.position,p});
                }
            }
        }

        return  list;
    }


    MoveValue getMoveValue(Point[] _move)
    {
        MoveValue mV = new MoveValue(_move,0);



        return mV;
    }

}

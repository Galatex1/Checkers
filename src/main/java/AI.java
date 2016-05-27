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

        figures_AI = Manager.ref.getFigures(false);
        figures_PL = Manager.ref.getFigures(true);
    };

    void simulateTurn(){

        Board board = new Board();

        List<Point[]> list = Manager.ref.getPossibleMoves(figures_AI);
        MoveValue best_move = new MoveValue(new Point[]{},0);

        if(list.size()!=0)
        {
            for(Point[] p : list)
            {
                MoveValue mv = Manager.ref.getMoveValue(p);
                if(mv.value > best_move.value)
                    best_move = mv;
            }

        }

    }

}

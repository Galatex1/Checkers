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

        Manager.ref.getPossibleMoves(figures_AI);
        //if()

    }

}

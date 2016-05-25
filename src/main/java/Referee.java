import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Galatex on 16.5.2016.
 */

class Referee
{

    final Board board = new Board();

    public boolean player_turn = false;

    int turn_time = 120;

    int P1_figures = 0;
    int P2_figures = 0;

    final Timer timer = new Timer();

    List<Point> clicked = new ArrayList<>();
    List<Point[]> highlighted = new ArrayList<>();

    final Point[][][] POSIBLE_MOVES = {new Point[][]{new Point[]{new Point(1, 1), new Point(1, -1)},
                                                       new Point[]{new Point(1, 1), new Point(1, -1), new Point(-1, 1), new Point(-1, -1)}
                                                      },
                                         new Point[][]{
                                                       new Point[]{new Point(-1, 1), new Point(-1, -1)},
                                                       new Point[]{new Point(1, 1), new Point(1, -1), new Point(-1, 1), new Point(-1, -1)}
                                                      }
                                         };


    Referee()
    {
        //fillBoard(board);
    }

     static final int BOARD_SIZE = 8;
     static final int PLAYER_FIGURES = 12;

     static final Point[] STARTING_POSITIONS = {new Point(0, 0), new Point(0, 2), new Point(0, 4), new Point(0, 6),
            new Point(1, 1), new Point(1, 3), new Point(1, 5), new Point(1, 7),
            new Point(2, 0), new Point(2, 2), new Point(2, 4), new Point(2, 6),
            new Point(7, 1), new Point(7, 3), new Point(7, 5), new Point(7, 7),
            new Point(6, 0), new Point(6, 2), new Point(6, 4), new Point(6, 6),
            new Point(5, 1), new Point(5, 3), new Point(5, 5), new Point(5, 7)};


     void fillBoard(Board _board)
    {
        _board.figures = new Figure[BOARD_SIZE][BOARD_SIZE];

        for (int i = 0; i < PLAYER_FIGURES * 2; i++)
        {
            if (i < PLAYER_FIGURES)
                _board.figures[STARTING_POSITIONS[i].x][STARTING_POSITIONS[i].y] = new Figure(PlayerSelect.P1, STARTING_POSITIONS[i]);
            else
                _board.figures[STARTING_POSITIONS[i].x][STARTING_POSITIONS[i].y] = new Figure(PlayerSelect.P2, STARTING_POSITIONS[i]);
        }

    }

    public void startTimer()
    {
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                turn_time--;
                if(turn_time <=0)
                {
                    switchPlayers(player_turn);
                    turn_time = 120;
                }

                Manager.man.changeClock(turn_time);

            }
        }, 1000, 1000);
    }

    public void newGame()
    {
        fillBoard(board);
        P1_figures = 12;
        P2_figures = 12;
        Manager.man.updateBoard(board.figures);
        turn_time = 120;
        player_turn = false;
        clicked.clear();
        highlighted.clear();
    }

    public void loadGame(boolean turn, int time,  int p1, int p2)
    {
        P1_figures = p1;
        P2_figures = p2;
        Manager.man.updateBoard(board.figures);
        if(player_turn!=turn)
            switchPlayers(player_turn);
        turn_time = time;
        clicked.clear();
        highlighted.clear();
    }

    public void showHint()
    {
        List<Point[]> jCheck;
        List<Point[]> forced = getForcedMoves();

        if(forced.size()> 0)
        {
            Manager.man.unhighlightSquares();
            Manager.man.highlightSquare(forced.get(0)[0]);
            clicked.clear();
            highlighted.clear();
            clicked.add(forced.get(0)[0]);
            if ((jCheck = checkJump(board.getFigure(forced.get(0)[0]), forced.get(0)[0])).size() > 0)
            {
                highlighted.addAll(jCheck);
                for (Point[] po : jCheck)
                {
                    Manager.man.highlightSquare(po[1]);
                }
            }
        }
        else
        {
           JOptionPane.showMessageDialog(Manager.frame, "You are not forced to jump anywhere.", "Hint.", JOptionPane.INFORMATION_MESSAGE);
        }
    }

     void checkAvailable(Figure f, Point p)
    {

        List<Point[]> arrayList;
        if ((arrayList = getForcedMoves()).size() != 0)
        {

            for (Point[] pt : arrayList)
            {
                if (pt[0].equals(f.position) && pt[1].equals(p))
                {


                    board.moveFigure(pt[0], pt[1]);
                    Point r = new Point((pt[0].x + pt[1].x) /2,(pt[0].y + pt[1].y) /2);
                    removeFigure(r);


                    List<Point[]> jumpList;
                    //Check multiple jumps
                    if((jumpList = checkJump(f, f.position)).size() != 0)
                    {
                        //JOptionPane.showMessageDialog(Manager.frame, "You must make another jump.", "Info.", JOptionPane.INFORMATION_MESSAGE);
                        Manager.man.unhighlightSquares();
                        clicked.clear();
                        highlighted.clear();
                        highlighted.addAll(jumpList);
                        clicked.add(pt[1]);
                        for(Point[] po : jumpList)
                        {
                            Manager.man.highlightSquare(po[1]);
                        }
                        return;

                    }
                    else
                    {
                        doEvolve(f);
                        switchPlayers(player_turn);
                    }
                }
            }
        } else
        {
            for(Point po : checkPositions(f))
            {
                if(p.equals(po))
                {
                    board.moveFigure(clicked.get(0), p);
                    doEvolve(f);
                    switchPlayers(player_turn);
                    break;
                }
            }

        }
        Manager.man.unhighlightSquares();
        clicked.clear();
    }

     void doEvolve(Figure f)
    {

        if ((f.position.x == 7 || f.position.x == 0) && !f.state)
        {
            f.state = true;
            Manager.man.evolveFigure(f);
        }
    }

     void removeFigure(Point p)
    {


        board.figures[p.x][p.y] = null;

        Manager.man.removeFigure(p);

        if(!player_turn)
        {
            P2_figures--;
            if(P2_figures==0)
            {
                JOptionPane.showMessageDialog(Manager.frame, "Congratulation! Black Player Wins!", "Congratulation!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else
        {
            P1_figures--;
            if(P1_figures==0)
            {
                JOptionPane.showMessageDialog(Manager.frame, "Congratulation! White Player Wins!", "Congratulation!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

     List<Point[]> getForcedMoves()
    {

        List<Point[]> arrayList = new ArrayList<>();

        for (Figure[] f : board.figures)
        {
            for (Figure figure : f)
            {
                if (figure != null)
                {
                    if (ownerTurn(figure))
                    {
                        arrayList.addAll(checkJump(figure));
                    }
                }
            }
        }


        return arrayList;
    }

     List<Point> checkPositions(Figure f)
    {
        List<Point> array = new ArrayList<>();
        for(Point p : POSIBLE_MOVES[f.playerSelect.getInt()][f.stateInt()])
        {
            Point check = new Point(f.position.x+p.x,f.position.y+p.y);
            if(board.getFigure(check) == null)
                if (f.position.x + p.x < 8 && f.position.x + p.x >=0 && f.position.y + p.y >= 0 && f.position.y + p.y < 8)
                    array.add(check);
        }

        return array;
    }

     List<Point[]> checkJump(Figure f)
    {
        List<Point[]> array = new ArrayList<>();
        for(Point p : POSIBLE_MOVES[f.playerSelect.getInt()][f.stateInt()])
        {
            Point check = new Point(f.position.x+p.x,f.position.y+p.y);
            Point check1 = new Point(f.position.x+p.x*2,f.position.y+p.y*2);

            if(board.getFigure(check) != null && board.getFigure(check1) == null && board.getFigure(check).playerSelect.getBool() != player_turn)
                if (f.position.x + p.x*2 < 8 && f.position.x + p.x*2 >=0 && f.position.y + p.y*2 >= 0 && f.position.y + p.y*2 < 8)
                    array.add(new Point[]{f.position, check1});
        }

        return array;
    }


     final List<Point[]> array = new ArrayList<>();

     boolean listContainsPoint(List<Point[]> l, Point[] p)
    {
        for(Point[] po : l)
        {
            if(po[0].equals(p[0])&& po[1].equals(p[1]))
                return true;
        }

        return false;
    }

    boolean listContainsPoint(List<Point[]> l, Point p)
    {
        for(Point[] po : l)
        {
            if(po[0].equals(p) || po[1].equals(p))
                return true;
        }

        return false;
    }

     boolean listContainsPointSecondPos(List<Point[]> l, Point p)
    {
        for(Point[] po : l)
        {
            if(po[1].equals(p))
                return true;
        }

        return false;
    }

    boolean listContainsPointFirstPos(List<Point[]> l, Point p)
    {
        for(Point[] po : l)
        {
            if(po[0].equals(p))
                return true;
        }

        return false;
    }

     List<Point[]> checkJump(Figure f, Point _start)
    {
        cJ(f,_start);
        List<Point[]> a = new ArrayList<>();
        a.addAll(array);
        array.clear();
        return a;
    }

     void cJ(Figure f, Point _start)
    {
            for (Point p : POSIBLE_MOVES[f.playerSelect.getInt()][f.stateInt()])
            {
                Point check = new Point(_start.x + p.x, _start.y + p.y);
                Point check1 = new Point(_start.x + p.x * 2, _start.y + p.y * 2);
                if (board.getFigure(check) != null && board.getFigure(check1) == null && board.getFigure(check).playerSelect.getBool() != player_turn)
                    if (_start.x + p.x * 2 < 8 && _start.x + p.x * 2 >= 0 && _start.y + p.y * 2 >= 0 && _start.y + p.y * 2 < 8)
                    {
                        if(!listContainsPoint(array, new Point[]{_start, check1}) && !listContainsPoint(array, new Point[]{check1, _start}))
                        {
                            array.add(new Point[]{_start, check1});
                            cJ(f, check1);
                        }
                    }
            }
    }

    Point[] getPreviousPoint(List<Point[]> pos, Point p)
    {
        for(Point[] po : pos)
        {
            if(po[1].equals(p))
                return po;
        }
        Point[] ppp = {};

        return ppp;
    }

    public List<Point[]> getPath(Figure f,  Point _start, Point _end)
    {
        List<Point[]> pos = checkJump(f,_start);
        Point p = _end;
        Point[] po = getPreviousPoint(pos,p);

        List<Point[]> path = new ArrayList<>();

        while(!(po[0]).equals( _start))
        {
            path.add(po);
            p = po[0];
            po = getPreviousPoint(pos, p);

        }
        path.add(po);

        return path;
    }


     boolean ownerTurn(Figure f)
    {
        return f.playerSelect.getBool() == player_turn;
    }

    void validateClick(Point p)
    {
        if (clicked.size() == 0 && board.getFigure(p) != null && ownerTurn(board.getFigure(p)))
        {
            highlighted.clear();
            clicked.add(p);
            Manager.man.highlightSquare(p);

            List<Point[]> jCheck;

            if(getForcedMoves().size()>0)
            {
                if((jCheck = checkJump(board.getFigure(p),p)).size()>0)
                {
                    highlighted.addAll(jCheck);
                    for(Point[] po : jCheck)
                    {
                        Manager.man.highlightSquare(po[1]);
                    }
                }
            }
            else
            {
                for (Point po : checkPositions(board.getFigure(p)))
                {
                    Manager.man.highlightSquare(po);
                }
            }

        } else if (clicked.size() == 1)
        {
            if (board.getFigure(p) == null)
            {
                checkAvailable(board.getFigure(clicked.get(0)), p);
            }
             else
            {
                Manager.man.unhighlightSquares();
                clicked.clear();
            }

            Manager.man.updateBoard(board.figures);
        }
    }

    void switchPlayers(boolean current)
    {
        player_turn = !current;
        turn_time = 120;
        Manager.man.switchPlayers();
        clicked.clear();
        Manager.man.unhighlightSquares();
    }
}

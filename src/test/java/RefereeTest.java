import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Galatex on 24.5.2016.
 */
public class RefereeTest
{
    static Board _b;
    static Manager m;

    static List<Point[]> list = new ArrayList<Point[]>();

    static String[] control_save = {
            "1, 108",
            "0, 0, 0, 0",
            "0, 0, 1, 1",
            "0, 0, 1, 3",
            "0, 0, 1, 5",
            "0, 0, 1, 7",
            "0, 0, 3, 1",
            "0, 0, 3, 3",
            "0, 0, 3, 5",
            "1, 0, 3, 7",
            "0, 0, 4, 0",
            "1, 0, 4, 2",
            "1, 0, 4, 4",
            "1, 0, 5, 1",
            "1, 0, 5, 3",
            "1, 0, 5, 7",
            "1, 0, 6, 0",
            "1, 0, 6, 2",
            "1, 0, 6, 4",
            "1, 0, 6, 6",
            "1, 0, 7, 5",
            "1, 0, 7, 7"
    };



    @BeforeClass
    static public void setUp() throws Exception
    {
        _b = new Board();
        m = new Manager(false);

        list = new ArrayList<Point[]>() {{
            add(new Point[]{ new Point(4, 2),  new Point(2, 0)});
            add(new Point[]{new Point(4, 2), new Point(2, 4)});
            add(new Point[]{new Point(4, 4), new Point(2, 2)});
            add(new Point[]{new Point(4, 4), new Point(2, 6)});
        }};
    }

    @Test
    public void testNewGame() throws Exception{

        Manager.ref.fillBoard(_b);

        Manager.man.newGame();

        boolean same = true;

        for(int i = 0 ; i < 8; i++)
        {
            for(int j = 0 ; j < 8; j++)
            {
                if(Manager.ref.board.figures[i][j] != null && _b.figures[i][j]!=null)
                    same = Manager.ref.board.figures[i][j].position.equals(_b.figures[i][j].position);

                if((Manager.ref.board.figures[i][j] != null && _b.figures[i][j]==null)|| (Manager.ref.board.figures[i][j] == null && _b.figures[i][j]!=null))
                {
                    same = false;
                    break;
                }

                if(!same)
                    break;
            }
        }

        Assert.assertTrue("Expected two newly created boards to be same", same);

    }

    @Test
    public void testshowHint(){

        Manager.man.loadSave(control_save);

        Manager.ref.showHint();
        Assert.assertEquals("Function didn't return right number of Highlights", 5, Manager.ref.highlighted.size());
        Assert.assertTrue("Function didn't return right list of Highlights",
                (Manager.ref.listContainsPoint(Manager.ref.highlighted, new Point[]{new Point(4, 2), new Point(2, 0)})
                && Manager.ref.listContainsPoint(Manager.ref.highlighted, new Point[]{new Point(4, 2), new Point(2, 4)})
                && Manager.ref.listContainsPoint(Manager.ref.highlighted, new Point[]{new Point(2, 0), new Point(0, 2)})
                && Manager.ref.listContainsPoint(Manager.ref.highlighted, new Point[]{new Point(2, 4), new Point(0, 2)})
                && Manager.ref.listContainsPoint(Manager.ref.highlighted, new Point[]{new Point(2, 4), new Point(0, 6)})
        ));

    }

    @Test
    public void testGetForcedMoves() throws Exception{

        Manager.man.loadSave(control_save);

        List<Point[]> listforced = Manager.ref.getForcedMoves();

        boolean eq = true;

        if(listforced.size() == list.size())
        {
            for (int i = 0; i < listforced.size(); i++)
            {
                boolean eqq = false;

                for(Point[] p : list)
                {
                    if (listforced.get(i)[0].equals(p[0]) && listforced.get(i)[1].equals(p[1]))
                    {
                        eqq = true;
                        break;
                    }
                }

                if(!eqq)
                {
                    eq = false;
                    break;
                }
            }
        }
        else
        eq = false;


        Assert.assertTrue("Function didn't return right list of Forced moves", eq);
    }

    @Test
    public void testlistContainsPointArray()
    {
        Assert.assertTrue("Function didn't return right value, Point[] should be find", Manager.ref.listContainsPoint(list, new Point[]{new Point(4,2),new Point(2,0)}));
    }
    @Test
    public void testlistContainsPoint()
    {
        Assert.assertTrue("Function didn't return right value, Point should be find", Manager.ref.listContainsPoint(list, new Point(4,2)));

    }
    @Test
    public void testlistContainsPointSecondPos()
    {
        Assert.assertTrue("Function didn't return right value, Point should be find", Manager.ref.listContainsPointSecondPos(list, new Point(2,6)));
    }
    @Test
    public void testlistContainsPointFirstPos()
    {
        Assert.assertTrue("Function didn't return right value, Point should be find", Manager.ref.listContainsPointFirstPos(list, new Point(4,4)));
    }

    @Test
    public void testCheckPositions()
    {
        Manager.ref.newGame();

        List<Point[]> listpos = new ArrayList<>();

        for(Point p : Manager.ref.checkPositions( Manager.ref.board.figures[2][2]))
        {
            listpos.add(new Point[]{new Point(0,0),p});
        }

        Assert.assertTrue("Function didn't return right list of posible moves", (Manager.ref.listContainsPoint(listpos, new Point(3,1)) && Manager.ref.listContainsPoint(listpos, new Point(3,3))));
    }

    /*@Test
    public void testCheckJump()
    {
        Manager.man.loadSave(control_save);
        List<Point[]> listp = Manager.ref.checkJump(Manager.ref.board.figures[4][2]);

        Assert.assertTrue(" First function didn't return right list of posible jumps", (Manager.ref.listContainsPoint(listp, new Point[]{new Point(4,2), new Point(2,0)}) && Manager.ref.listContainsPoint(listp,  new Point[]{new Point(4,2), new Point(2,4)})));


        listp = Manager.ref.checkJump(Manager.ref.board.figures[4][2], Manager.ref.board.figures[4][2].position);

        Assert.assertEquals(" Second function didn't return right number of posible jumps", 5, listp.size());

        Assert.assertTrue(" Second function didn't return right list of posible jumps", (Manager.ref.listContainsPoint(listp, new Point[]{new Point(4, 2), new Point(2, 0)}) && Manager.ref.listContainsPoint(listp, new Point[]{new Point(4, 2), new Point(2, 4)})
            && Manager.ref.listContainsPoint(listp, new Point[]{new Point(2, 0), new Point(0, 2)})
            && Manager.ref.listContainsPoint(listp, new Point[]{new Point(2, 4), new Point(0, 2)})
            && Manager.ref.listContainsPoint(listp, new Point[]{new Point(2, 4), new Point(0, 6)})
        ));


    }*/

    @Test
    public void testCheckJump()
    {

        String[] save = {"1, 108",
                "0, 0, 6, 2",
                "0, 0, 6, 4",
                "0, 0, 4, 2",
                "0, 0, 4, 4",
                "0, 0, 2, 2",
                "0, 0, 2, 4",
                "0, 0, 4, 6",
                "0, 0, 2, 6",
                "1, 1, 3, 3"};

        Manager.man.loadSave(save);
        List<Point[]> listp = Manager.ref.checkJump(Manager.ref.board.figures[3][3], Manager.ref.board.figures[3][3].position);



        String values_exp = "(3,3),(5,5), (5,5),(3,3), (5,5),(7,3), (7,3),(5,5), (7,3),(5,1), (5,1),(7,3), (5,5),(3,7), (3,7),(5,5), (3,7),(1,5), (1,5),(3,7), (3,3),(5,1), (5,1),(3,3), (3,3),(1,5), (1,5),(3,3), (3,3),(1,1), (1,1),(3,3), ";
        String values_real = "";

        for(Point[] p : listp)
        {

            values_real += "("+p[0].x+","+p[0].y+"),("+p[1].x+","+p[1].y+"), ";

        }

            Assert.assertEquals("String should be equal", values_exp, values_real);
    }

    @Test
    public void testRemoveFigure(){

        Manager.man.newGame();

        Manager.ref.removeFigure(new Point(5,1));

        Assert.assertNull("Figure wasn't removed properly.",Manager.ref.board.figures[5][1]);
        Assert.assertEquals("Player2 Should only have 11 figures .",11,Manager.ref.P2_figures);
    }

    @Test
    public void testGetPreviousPoints()
    {

        List<Point[]> list = new ArrayList<Point[]>() {{
        add(new Point[]{ new Point(2, 0),  new Point(4, 2)});
        add(new Point[]{new Point(2, 4), new Point(2, 0)});
        add(new Point[]{new Point(2, 6), new Point(2, 0)});
    }};

        List<Point[]> _list = Manager.ref.getPreviousPoints(list, new Point(4, 2));

        String values_expected = "";
        String values_actual = "";
        Assert.assertEquals("String of values should be Equal", values_expected, values_actual);

    }

    /*@Test
    public void testGetPath(){


        String[] save = {"1, 108",
                "0, 0, 6, 2",
                "0, 0, 6, 4",
                "0, 0, 4, 2",
                "0, 0, 4, 4",
                "0, 0, 2, 2",
                "0, 0, 2, 4",
                "0, 0, 4, 6",
                "0, 0, 2, 6",
                "1, 1, 3, 3"};
        Manager.man.loadSave(save);

        Figure f = Manager.ref.board.figures[3][3];

         List<Path> list = Manager.ref.getPath(f, f.position, new Point(7,3));

        String values_expected = "(3,7),(1,5), (5,5),(3,7), (7,3),(5,5), (3,3),(1,5), (5,5),(3,3), (7,3),(5,5), (3,3),(1,5), (5,1),(3,3), (7,3),(5,1), ";

        String values_actual = "";

        for(Path path : list)
        {
            for(MoveValue m : path.path)
            {
                values_actual += "("+m.move_start.x+","+m.move_start.y+"),("+m.move_end.x+","+m.move_end.y+"), ";
            }
        }

       // Assert.assertEquals("list should contain 2 paths", 2, list.size());

        Assert.assertEquals("String of values should be Equal", values_expected, values_actual);
    }*/

    @Test
    public void testGetFigures()
    {
        Manager.man.newGame();

        List<Figure> list =  Manager.ref.getFigures(false);

        String values_actual = "";
        String values_expected = "(0,0), (0,2), (0,4), (0,6), (1,1), (1,3), (1,5), (1,7), (2,0), (2,2), (2,4), (2,6), ";

        for(Figure p : list)
        {
            values_actual += "("+p.position.x+","+p.position.y+"), ";
        }

        Assert.assertEquals("String of values should be Equal", values_expected, values_actual);
    }

    @Test
    public void testGetPossibleMoves()
    {
        Manager.man.newGame();

        List<Point[]> list =  Manager.ref.getPossibleMoves(Manager.ref.getFigures(false));

        String values_actual = "";
        String values_expected = "(2,0),(3,1), (2,2),(3,3), (2,2),(3,1), (2,4),(3,5), (2,4),(3,3), (2,6),(3,7), (2,6),(3,5), ";

        for(Point[] p : list)
        {
            values_actual += "("+p[0].x+","+p[0].y+"),("+p[1].x+","+p[1].y+"), ";
        }

        Assert.assertEquals("String of values should be Equal", values_expected, values_actual);

        Manager.man.loadSave(control_save);

        list =  Manager.ref.getPossibleMoves(Manager.ref.getFigures(false));

        values_actual = "";
        values_expected = "(4,2),(2,4), (4,2),(2,0), (4,4),(2,6), (4,4),(2,2), ";

        for(Point[] p : list)
        {
            values_actual += "("+p[0].x+","+p[0].y+"),("+p[1].x+","+p[1].y+"), ";
        }

        Assert.assertEquals("String of values should be Equal", values_expected, values_actual);

    }

    @After
    public void tearDown() throws Exception
    {

    }
}
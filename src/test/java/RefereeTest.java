import org.junit.*;

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

    @Test
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


    }

    @Test
    public void testRemoveFigure(){

        Manager.man.newGame();

        Manager.ref.removeFigure(new Point(5,1));

        Assert.assertNull("Figure wasn't removed properly.",Manager.ref.board.figures[5][1]);
        Assert.assertEquals("Player2 Should only have 11 figures .",11,Manager.ref.P2_figures);
    }

    @Test
    public void testGetPath(){

        Manager.man.loadSave(control_save);

        Figure f = Manager.ref.board.figures[4][2];

         List<Point[]> list = Manager.ref.getPath(f, f.position, new Point(0,2));

        for(Point[] po : list)
        {
            System.out.println("("+po[0].x+", "+po[0].y+") "+"("+po[1].x+", "+po[1].y+")");
        }


        Assert.assertTrue(" Function didn't return right list of paths", (/*Manager.ref.listContainsPoint(list, new Point[]{new Point(4, 2), new Point(2, 0)})
                &&*/ Manager.ref.listContainsPoint(list, new Point[]{new Point(2, 4), new Point(0, 2)})
                && Manager.ref.listContainsPoint(list, new Point[]{new Point(4, 2), new Point(2, 4)})
                //&& Manager.ref.listContainsPoint(list, new Point[]{new Point(2, 4), new Point(0, 2)})
        ));
    }

    @After
    public void tearDown() throws Exception
    {

    }
}
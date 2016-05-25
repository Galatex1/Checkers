import javax.swing.*;
import java.awt.*;
import java.io.*;


/**
 * Created by Galatex on 16.5.2016.
 */
public class Manager {

    public static GUI gui;
    public static Referee ref;
    public static Manager man;
    public static JFrame frame;

    Manager(boolean check){
        ref = new Referee();
        gui = new GUI();
        man = this;
        frame = new JFrame("Dialog");
        if(check)
        {
            Object[] options = {"Start new game.",
                    "Load last saved game."};
            int n = JOptionPane.showOptionDialog(frame,
                    "Do you want to start new game or load last saved game?",
                    "Welcome back.",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
            if (n == 0)
            {
                newGame();
            } else
            {
                loadSave();
            }
        }

        ref.startTimer();

    }

    public void updateBoard(final Figure[][] fig)
    {
        gui.updateGUI(fig);
    }

    public void highlightSquare(Point p)
    {
        gui.highlightSquare(p);
    }

    public void unhighlightSquares()
    {
        gui.unhighlightSquares();
    }

    public void evolveFigure(Figure fig)
    {
        gui.evolveFigure(fig);
    }

    public void removeFigure(Point p)
    {
        gui.removeFigure(p);
    }

    public void switchPlayers()
    {
        gui.switchPlayers();
    }

    public void changeClock(int c)
    {
        gui.changeClock(c);
    }

    public void newGame()
    {
        gui.resetUI();
        ref.newGame();
    }

    public void validateClick(Point p){
        ref.validateClick(p);
    }

    public void showHint(){
        ref.showHint();
    }

    public void saveGame()
    {
        PrintWriter writer = null;
        try
        {
             writer = new PrintWriter("resources/Save.txt", "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        String data = (!ref.player_turn ?"0":"1")+", "+ Integer.toString(ref.turn_time);
        writer.println(data);
        for(Figure[] fig : ref.board.figures)
        {
            for(Figure f : fig)
            {
                if(f != null)
                {
                    data = Integer.toString(f.playerSelect.getInt()) + ", ";
                    data += Integer.toString(f.stateInt()) + ", ";
                    data += Integer.toString(f.position.x) + ", ";
                    data += Integer.toString(f.position.y);
                    writer.println(data);
                }
            }
        }

        writer.close();
    }

    public void loadSave()
    {
       ref.board.figures = new Figure[Referee.BOARD_SIZE][Referee.BOARD_SIZE];
        int time = 0;
        boolean turn = false;
        int p1 = 0;
        int p2 = 0;

        try (BufferedReader br = new BufferedReader(new FileReader("resources/Save.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {

                String[] parts = line.split(", ");

                if(parts.length == 2)
                {
                    time = Integer.parseInt(parts[1]);
                    turn = Integer.parseInt(parts[0]) != 0;
                }
                else
                {
                    ref.board.figures[Integer.parseInt(parts[2])][Integer.parseInt(parts[3])] = new Figure((Integer.parseInt(parts[0])==0?PlayerSelect.P1:PlayerSelect.P2), new Point(Integer.parseInt(parts[2]),Integer.parseInt(parts[3])));
                    ref.board.figures[Integer.parseInt(parts[2])][Integer.parseInt(parts[3])].state = Integer.parseInt(parts[1]) != 0;

                    int p = Integer.parseInt(parts[0])==0 ? p1++ : p2++;
                }

                   }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        gui.resetUI();
        ref.loadGame(turn, time, p1, p2);

    }

    public void loadSave(String[] save)
    {
        ref.board.figures = new Figure[Referee.BOARD_SIZE][Referee.BOARD_SIZE];
        int time = 0;
        boolean turn = false;
        int p1 = 0;
        int p2 = 0;

        try (BufferedReader br = new BufferedReader(new FileReader("resources/Save.txt"))) {
            String line;
            for (String str : save) {

                String[] parts = str.split(", ");

                if(parts.length == 2)
                {
                    time = Integer.parseInt(parts[1]);
                    turn = Integer.parseInt(parts[0]) != 0;
                }
                else
                {
                    ref.board.figures[Integer.parseInt(parts[2])][Integer.parseInt(parts[3])] = new Figure((Integer.parseInt(parts[0])==0?PlayerSelect.P1:PlayerSelect.P2), new Point(Integer.parseInt(parts[2]),Integer.parseInt(parts[3])));
                    ref.board.figures[Integer.parseInt(parts[2])][Integer.parseInt(parts[3])].state = Integer.parseInt(parts[1]) != 0;

                    int p = Integer.parseInt(parts[0])==0 ? p1++ : p2++;
                }

            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        gui.resetUI();
        ref.loadGame(turn, time, p1, p2);

    }
}

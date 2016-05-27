import javafx.scene.image.Image;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.util.*;

/**
 * Graphical panel with board
 *
 * @author Michal Vytrhlik (05/03/2016 14:24)
 * Prepracoval Galatex 20/5/2016
 */
class GPlayBoard extends JPanel
{
    private static final int DEFAULT_BUTTON_SIZE_IN_PIXELS = 80;
    private final JButton[][] gSquares;
    private final JButton[] players_UI;
    private final java.util.List<JButton> highlated = new ArrayList<>();

    public GPlayBoard()
    {

        setLayout(new GridBagLayout());
        this.gSquares = generateNewButtonsArray();
        this.players_UI = createUI();
    }

    public void drawBoardFromLogicalForm(final Figure[][] fig)
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                setupGSquare(gSquares[i][j], fig, new Point(i, j));
            }
        }
    }

    /**
     * Vygenereuje 2 rozmerne pole policek(buttonu) a prida je na panel
     *
     * @return {@link JButton[][]} vraci nove pole tlacitek
     */
    private JButton[][] generateNewButtonsArray()
    {
        JButton[][] array = new JButton[Referee.BOARD_SIZE][Referee.BOARD_SIZE];

        for (int i = 0; i < Referee.BOARD_SIZE; i++)
        {
            for (int j = 0; j < Referee.BOARD_SIZE; j++)
            {
                final int finalJ = j;
                final int finalI = i;
                array[i][j] = new JButton();
                array[i][j].addActionListener(new ActionListener()
                {
                    private final Point coordinate = new Point(finalI, finalJ);

                    @Override
                    public void actionPerformed(final ActionEvent e)
                    {
                        Manager.man.validateClick(coordinate);
                    }
                });

                GridBagConstraints c = new GridBagConstraints();
                c.fill = GridBagConstraints.BOTH;
                c.ipady = 0;       //reset to default
                c.weighty = 0;
                c.anchor = GridBagConstraints.PAGE_START;
                c.insets = new Insets(0, 0, 0, 0);  // padding
                c.gridwidth = 1;   //1 columns wide
                c.gridheight = 1;   //1 row
                c.gridx = j;
                c.gridy = i;

                // ztrata fokusu na icone policka
                array[i][j].setFocusPainted(false);

                // nastaveni velikosti policka
                array[i][j].setPreferredSize(new Dimension(DEFAULT_BUTTON_SIZE_IN_PIXELS, DEFAULT_BUTTON_SIZE_IN_PIXELS));

                if (j % 2 == 0)
                {
                    if (i % 2 == 0)
                        array[i][j].setBackground(Color.WHITE);
                    else
                        array[i][j].setBackground(Color.BLACK);
                } else
                {

                    if (i % 2 != 0)
                        array[i][j].setBackground(Color.WHITE);
                    else
                        array[i][j].setBackground(Color.BLACK);
                }

                add(array[i][j], c);
            }
        }

        return array;
    }


    //JUnit
    //Maven

    private void setupGSquare(final JButton gSquare, final Figure[][] fig, Point p)
    {

        // ikona policka - obrazek figurky
        ImageIcon gFigurine = null;

        if (fig[p.x][p.y] != null)
        {

            if (!fig[p.x][p.y].state && fig[p.x][p.y].playerSelect == PlayerSelect.P1)
                gFigurine = constructImageFromPath("resources/PB.png");
            else if (fig[p.x][p.y].state && fig[p.x][p.y].playerSelect == PlayerSelect.P1)
                gFigurine = constructImageFromPath("resources/DB.png");
            else if (!fig[p.x][p.y].state && fig[p.x][p.y].playerSelect == PlayerSelect.P2)
                gFigurine = constructImageFromPath("resources/PW.png");
            else if (fig[p.x][p.y].state && fig[p.x][p.y].playerSelect == PlayerSelect.P2)
                gFigurine = constructImageFromPath("resources/DW.png");
        }

        // nastavi velikost ikony
        if (gFigurine != null)
        {
            gFigurine = getScaledImageIcon(gFigurine);
        } else
        {
            gFigurine = null;
        }

        // nastavi obrazek policka
        gSquare.setIcon(gFigurine);

    }

    public void highlightGSquare(Point p)
    {
        JButton gSquare = gSquares[p.x][p.y];

        gSquare.setBackground(Color.GREEN);
        highlated.add(gSquare);

        // ztrata fokusu na icone policka
        gSquare.setFocusPainted(false);
    }

    public void unhighlightGSquares()
    {
        if (highlated.size() > 0)
        {
            for (JButton aHighlated : highlated)
            {
                aHighlated.setBackground(Color.WHITE);
            }

            highlated.clear();
        }
    }

    public void evolveFigure(Figure fig)
    {
        JButton gSquare = gSquares[fig.position.x][fig.position.y];

        ImageIcon gFigurine;

        if (fig.playerSelect == PlayerSelect.P1)
        {
            gFigurine = constructImageFromPath("resources/DB.png");
        } else
        {
            gFigurine = constructImageFromPath("resources/DW.png");
        }

        // nastavi velikost ikony
        if (gFigurine != null)
        {
            gFigurine = getScaledImageIcon(gFigurine);
        } else
        {
            gFigurine = null;
        }

        // nastavi obrazek policka
        gSquare.setIcon(gFigurine);

    }

    public void removeFigure(Point p)
    {
        JButton gSquare = gSquares[p.x][p.y];

        gSquare.setIcon(null);
    }

    public void changeClock(int c)
    {
        int min = c / 60;
        int sec = c - min * 60;

		/*
            YELLOW new Color(255,255,0);
			GREEN_TO_YELLOW (2.3333333333333, 0, -1,683333333333) per sec
			YELLOW_TO_RED (0, -3,616666666666667, 0,8333333333333333) per sec
		*/

        int Red;
        int Green;
        int Blue;
        Color col;


        if (min == 1)
        {
            col = new Color(0, 255, 0);
            Red = col.getRed() + 4*(60-sec) > 255 ? 255 : col.getRed() + 4*(60-sec);
            Green = col.getGreen();
            Blue = col.getBlue() /*- 2*(60-sec) < 0 ? 0 : col.getBlue() - 2*(60-sec)*/;
            col = new Color(Red, Green, Blue);
        } else
        {
            col = new Color(255,255,0);
            Red = col.getRed();
            Green = col.getGreen() - 4*(60-sec) < 0 ? 0 : col.getGreen() - 4*(60-sec);
            Blue = col.getBlue() /*+ 1*(60-sec) > 255 ? 255 : col.getBlue() + 1*(60-sec)*/;
            col = new Color(Red, Green, Blue);
        }


        players_UI[0].setBackground(col);
        players_UI[1].setText(Integer.toString(min) + ":" + (sec < 10 ? "0" + Integer.toString(sec) : Integer.toString(sec)));
        players_UI[6].setText(Integer.toString(min) + ":" + (sec < 10 ? "0" + Integer.toString(sec) : Integer.toString(sec)));
        players_UI[7].setBackground(col);
    }

    private JButton[] createUI()
    {
        JButton[] array = new JButton[Referee.BOARD_SIZE];
        for (int i = 0; i < Referee.BOARD_SIZE; i++)
        {
            array[i] = new JButton();
            array[i].setEnabled(false);
            array[i].addActionListener(e -> {
                //Manager.man.validateClick(coordinate);

            });
            array[i].setBackground(Color.LIGHT_GRAY);

            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.ipady = 0;       //reset to default
            c.weighty = 0;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(0, 0, 0, 0);  // padding
            c.gridwidth = 1;   //1 columns wide
            c.gridheight = 1;   //1 row
            c.gridx = 8;
            c.gridy = i;

            add(array[i], c);
        }

        array[0].setBackground(/*new Color(255, 38, 50)*/new Color(0, 255, 0));
        array[1].setText("2:00");
        array[2].setText("Your Turn!");
        array[3].setText("Player 1");
        array[4].setText("Player 2");
        array[5].setText("Enemy Turn!");
        array[6].setText("2:00");
        array[7].setBackground(new Color(255, 0, 0));

        array[0].setEnabled(true);
        array[1].setEnabled(true);
        array[2].setEnabled(true);
        array[3].setEnabled(true);

        Font font = array[1].getFont();
        font = font.deriveFont(font.getSize() * 2.5f);
        array[1].setFont(font);
        array[6].setFont(font);


        return array;
    }

    public void resetUI()
    {
        players_UI[0].setBackground(/*new Color(255, 38, 50)*/new Color(0, 255, 0));
        players_UI[1].setText("2:00");
        players_UI[2].setText("Your Turn!");
        players_UI[3].setText("Player 1");
        players_UI[4].setText("Player 2");
        players_UI[5].setText("Enemy Turn!");
        players_UI[6].setText("2:00");
        players_UI[7].setBackground(new Color(255, 0, 0));

        players_UI[0].setEnabled(true);
        players_UI[1].setEnabled(true);
        players_UI[2].setEnabled(true);
        players_UI[3].setEnabled(true);

    }


    public void switchPlayers()
    {
        if (Objects.equals(players_UI[2].getText(), "Your Turn!"))
        {
            players_UI[0].setBackground(new Color(0, 255, 0));
            players_UI[1].setText("0:00");
            players_UI[2].setText("Enemy Turn!");
            players_UI[3].setText("Player 1");
            players_UI[4].setText("Player 2");
            players_UI[5].setText("Your Turn!");
            players_UI[6].setText("2:00");
            players_UI[7].setBackground(new Color(0, 255, 0));

            players_UI[0].setEnabled(false);
            players_UI[1].setEnabled(false);
            players_UI[2].setEnabled(false);
            players_UI[3].setEnabled(false);
            players_UI[4].setEnabled(true);
            players_UI[5].setEnabled(true);
            players_UI[6].setEnabled(true);
            players_UI[7].setEnabled(true);
        } else
        {
            players_UI[0].setBackground(new Color(0, 255, 0));
            players_UI[1].setText("2:00");
            players_UI[2].setText("Your Turn!");
            players_UI[3].setText("Player 1");
            players_UI[4].setText("Player 2");
            players_UI[5].setText("Enemy Turn!");
            players_UI[6].setText("0:00");
            players_UI[7].setBackground(new Color(0, 255, 0));

            players_UI[0].setEnabled(true);
            players_UI[1].setEnabled(true);
            players_UI[2].setEnabled(true);
            players_UI[3].setEnabled(true);
            players_UI[4].setEnabled(false);
            players_UI[5].setEnabled(false);
            players_UI[6].setEnabled(false);
            players_UI[7].setEnabled(false);
        }
    }

    /**
     * Ziska zmensenou verzi ikony figurky v dane velikosti.
     *
     * @param imageIcon ikona figurky
     * @return {@link ImageIcon} vraci ikonu v nove velikosti
     */
    private ImageIcon getScaledImageIcon(final ImageIcon imageIcon)
    {
        return new ImageIcon(imageIcon.getImage().getScaledInstance(GPlayBoard.DEFAULT_BUTTON_SIZE_IN_PIXELS, GPlayBoard.DEFAULT_BUTTON_SIZE_IN_PIXELS, java.awt.Image.SCALE_SMOOTH));
    }

    /**
     * Zkonstruuje objekt typu image z relativni cesty k souboru.
     *
     * @param path cesta k souboru
     * @return {@link Image} instance obrazku
     */
    public ImageIcon constructImageFromPath(final String path)
    {
        File file = new File(path);

        if (file.exists())
        {
            try
            {
                return new ImageIcon(file.toURI().toURL());
            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }

}

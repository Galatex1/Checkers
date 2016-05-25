import javax.swing.*;
import java.awt.*;

class GMenu extends JPanel
{
    private static final int DEFAULT_BUTTON_SIZE_IN_PIXELS = 70;

    public GMenu(final int size)
    {
        setLayout(new GridLayout(1,size));
        JButton[] players_UI = generateNewButtonsArray(size);

        players_UI[0].setText("New Game");
        players_UI[1].setText("Load Game");
        players_UI[2].setText("Hint");
        players_UI[3].setText("Save and Exit");



        /*this.players_UI = createUI(size);*/
    }

    private JButton[] generateNewButtonsArray(final int size)
    {
        JButton[] array = new JButton[size];

        for (int j = 0; j < size; j++)
        {
            array[j] = new JButton();
                /*array[j].addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(final ActionEvent e)
                    {
                        Manager.man.validateClick(coordinate);
                    }
                });*/


            array[j].setBackground(Color.LIGHT_GRAY);

            // ztrata fokusu na icone policka
            array[j].setFocusPainted(false);

            // nastaveni velikosti policka
            array[j].setPreferredSize(new Dimension(DEFAULT_BUTTON_SIZE_IN_PIXELS, (int)Math.floor(DEFAULT_BUTTON_SIZE_IN_PIXELS/1.5)));

            add(array[j]);
        }


        array[0].addActionListener(e -> {
            Object[] options = {"Yes",
                    "No"};
            int n = JOptionPane.showOptionDialog(Manager.frame,
                    "<html>Do you want to start new game?<br>Progress in current game will be lost!</html>",
                    "Starting new game.",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
            if(n == 0)
            {
                Manager.man.newGame();
            }
        });

        array[2].setToolTipText("<html>Shows you, where you have to jump.<br>Not always shows the best posible move!</html>");
        array[2].addActionListener(e -> Manager.man.showHint());

        array[3].addActionListener(e -> {
            Object[] options = {"Save and Quit",
                    "Quit without saving",
                    "Cancel"
                        };
            int n = JOptionPane.showOptionDialog(Manager.frame,
                    "Do you want to save the game and exit?",
                    "Ending game.",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
            if(n == 0)
            {
                Manager.man.saveGame();
                System.exit(0);
            }
            else if (n == 1)
                System.exit(0);


        });

        array[1].addActionListener(e -> {
            Object[] options = {"Yes",
                    "No"};
            int n = JOptionPane.showOptionDialog(Manager.frame,
                    "<html>Do you want to load last saved game?<br>Progress in current game will be lost!</html>",
                    "Loading last saved game.",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
            if(n == 0)
            {
                Manager.man.loadSave();
            }
        });

        return array;
    }

}

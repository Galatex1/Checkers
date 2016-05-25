import javax.swing.*;
import java.awt.*;

/**
 * Trida pro reprezentaci grafickeho rozhrani.
 *
 * @author Michal Vytrhlik (29/02/2016 19:43)
 */
class GUI extends JFrame
{
	private final GPlayBoard gPlayBoard;

	public GUI() throws HeadlessException
	{
		// inicializace menu panelu
		GMenu gMenu = new GMenu(Referee.BOARD_SIZE / 2);

		// inicializace gui desky
		gPlayBoard = new GPlayBoard();

		setIconImage(gPlayBoard.constructImageFromPath("resources/DB.png").getImage());

		JTextField newTitle = new JTextField("Checkers");
		newTitle.setBounds(80, 40, 225, 20);

		// vykresleni desky
		//gPlayBoard.drawBoardFromLogicalForm(ref.board.figures);

		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.PAGE_START;
		c.gridx = 0;
		c.gridy = 0;

		add(gMenu,c);

		c.gridx = 0;
		c.gridy = 1;
		add(gPlayBoard,c);

		c.gridx = 0;
		c.gridy = 1;

		add(newTitle,c);

		setTitle(newTitle.getText());

		setVisible(true);
		pack();

		setResizable(true);

		// vystredeni obrazovky
		setWindowCentered();

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}

	/**
	 * Posune okno na stred hlavniho monitoru.
	 */
	private void setWindowCentered()
	{
		// hlavni monitor
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

		// sirka a vyska obrazovky
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();

		// velikosti okna
		int windowWidth = getWidth();
		int windowHeight = getHeight();

		// vypocet umisteni okna
		int xWindowLocation = width / 2 - windowWidth / 2;
		int yWindowLocation = height / 2 - windowHeight / 2;

		// nastaveni umisteni
		setLocation(xWindowLocation, yWindowLocation);
	}

	public void updateGUI(final Figure[][] fig){

		gPlayBoard.drawBoardFromLogicalForm(fig);
	}

	public void highlightSquare(Point p){

		gPlayBoard.highlightGSquare(p);
	}

	public void unhighlightSquares(){

		gPlayBoard.unhighlightGSquares();
	}

	public void evolveFigure(Figure fig){

		gPlayBoard.evolveFigure(fig);
	}

	public void removeFigure(Point p)
	{
		gPlayBoard.removeFigure(p);
	}

	public void changeClock(int c)
	{
		gPlayBoard.changeClock(c);
	}

	public void switchPlayers(){

		gPlayBoard.switchPlayers();
	}

	public void resetUI(){

		gPlayBoard.resetUI();
	}
}

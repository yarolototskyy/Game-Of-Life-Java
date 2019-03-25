import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/*
 * A class that extends the JPanel class, adding the functionality
 * of painting the current generation of a Game of Life.
 */
public class BoardPanel extends JPanel{
	private GameOfLife game;
	private Color cellBorderColor;
	
	public BoardPanel(GameOfLife g, Color cellBorderColor){
		game = g;
		this.cellBorderColor = cellBorderColor;
	}
	
	public BoardPanel(GameOfLife g){
		this(g, Color.BLACK);
	}
	
	/**
	 * Paints the current state of the Game of Life board onto
	 * this panel. This method is invoked for you each time you
	 * call repaint() on either this object or on the JFrame upon
	 * which this panel is placed.
	 */
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		// TODO this is where you need to draw the current state of the board
		int numSquaresX = game.getWidth();
		int numSquaresY = game.getHeight();
		int panelWidth = getWidth();
		int panelHeight = getHeight();
		int squareSideY, squareSideX;
		squareSideX = panelWidth / numSquaresX;
		squareSideY = panelHeight / numSquaresY;
		for (int i = 0; i < numSquaresY; i++) {
			for (int j = 0; j < numSquaresX; j++) {
				if (game.isAlive(j, i)) {
					g2.setColor(Color.WHITE);
				}
				else {
					g2.setColor(Color.BLACK);
				}
				g2.fillRect(j * squareSideX, i * squareSideY, squareSideX, squareSideY);
				g2.setColor(cellBorderColor);
				g2.drawRect(j * squareSideX, i * squareSideY, squareSideX, squareSideY);
				
			}
		}

		
	}
}


public class GameOfLife {
	/* 
	 * Engine for the Game of Life 
	 * 
	 * */

	private GameBoard board;
	private int gen;
	
	//Constants that set rules of the game
	private static final int LONELINESS_UPPER_THRESHOLD = 1;
	private static final int BIRTH_VALUE = 3;
	private static final int OVERCROWDING_LOWER_THRESHOLD = 4;
	
	//Assign Cell state constants
	private static final int DEAD_CELL = Cell.DEAD;
	private static final int ALIVE_CELL = Cell.ALIVE;
			
	public String toString() {
		/* 
		 * Returns string representation of a GameOfLife board
		 * 
		 **/
		
		return board.toString();
	}
	
	public GameOfLife(int width, int height) {
		/* 
		 * Constructs a new GameOfLife with specified height and width
		 * Fills with DEAD cells
		 * 
		 **/
		
		board = new GameBoard(width, height);
		clear(); //Sets all the cells to be dead
		gen = 0;
	}
	
	public GameOfLife(int width) {
		/* 
		 * Constructs a square GameOfLife with specified side length
		 * Fills with DEAD cells
		 * 
		 **/
		
		this(width, width);
	}
	
	public GameOfLife(Cell[][] initialSetup) {
		/* 
		 * Constructs a new GameOfLife based on given preset board of Cells
		 * 
		 **/

		//Copies contents of given board into instance board, not just the reference
		this.board = new GameBoard(initialSetup[0].length, initialSetup.length);
		for (int i = 0; i < initialSetup[0].length; i++) {
			for (int j = 0; j < initialSetup.length; j++) {
				board.setPiece(i, j, initialSetup[j][i]);
			}
		}
		gen = 0;
	}
	
	public GameOfLife() {
		/* 
		 * Default constructor for GameOfLife
		 * Creates a 50 x 50 board
		 * 
		 **/

		this(50, 50);
	}
	public boolean isAlive(int x, int y) {
		/* 
		 * Returns whether cell at specified location is Alive
		 * 
		 **/

		return ((Cell)this.board.getPiece(x, y)).isAlive();
	}
	public int countLivingNeighbors(int x, int y) {
		/* 
		 * Counts the number of living neighbors cell at specified location has
		 * Returns -1 if given location is invalid
		 * 
		 **/

		//2-D array of 'shifts' that represent each of a cell's neighbors
		final int[][] NEIGHBORS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {1, 1}, {1, -1}, {-1, 1}};
		
		if (isValidCoordinate(x, y)) {
			int neighbors = 0;
			
			//Loops through each of the neighbor shifts and checks if neighboring cell is alive
			for (int[] shift : NEIGHBORS) { 
				//Makes sure it doesn't access a 'neighbor' cell outside the board
				if (this.isValidCoordinate(x + shift[0], y + shift[1]) && this.isAlive(x + shift[0], y + shift[1])) {
					neighbors++;
				}
			}
			return neighbors; //returns neighbor counter
		}
		return -1; //Return value for invalid input
	}
	
	public int countLivingCells() {
		/* 
		 * Counts the number of living cells on the board currently
		 * 
		 **/

		int numAlive = 0;
		for (int i = 0; i < board.getHeight(); i++) {
			for (int j = 0; j < board.getWidth(); j++) {
				if (((Cell)this.board.getPiece(j, i)).isAlive()) {
					numAlive++;
				}
			}
		}
		return numAlive;
	}
	
	public void randomSetup() {
		/* 
		 * Randomizes the state of the board, 50% probability of either state on each cell
		 * Resets generation counter (since it effectively starts a brand new game)
		 * 
		 **/

		this.gen = 0;
		for (int i = 0; i < board.getHeight(); i++) {
			for (int j = 0; j < board.getWidth(); j++) {
				int randomState = (int)(Math.random() * 2); //Generates either 0 or 1
				this.board.setPiece(j, i, new Cell(randomState));	
			}

		}
	}
	
	
	public void clear() {
		/* 
		 * Clears board by setting all the cells to DEAD
		 * Resets generation counter (since it effectively starts a brand new game)
		 * 
		 **/

		for (int i = 0; i < board.getHeight(); i++) {
			for (int j = 0; j < board.getWidth(); j++) {
				this.board.setPiece(j, i, new Cell(DEAD_CELL));
			}
		}
		this.gen = 0;
	}
	
	private boolean isValidCoordinate(int x, int y) {
		/* 
		 * Private helper method that checks if given location is valid for the current GameOfLife board
		 * 
		 **/

		return x >= 0 && x < board.getWidth() && y >= 0 && y < board.getHeight();
	}
	
	public void changeState(int x, int y) {
		/* 
		 * Toggles state from DEAD to ALIVE or from ALIVE to DEAD
		 * Does not do anything if location is invalid
		 * 
		 **/

		if (isValidCoordinate(x, y)) {
			if (this.isAlive(x, y)) {
				this.setCell(x, y, DEAD_CELL);
			}
			else {
				this.setCell(x, y, ALIVE_CELL);
			}
		}
	}
	
	public Cell setCell(int x, int y, int value) {
		/* 
		 * Sets cell at given location to the given value
		 * Returns cell that was replaced
		 * If location is invalid null is returned
		 * If value other than ALIVE or DEAD is given, IllegalArgumentException is thrown
		 * 
		 **/
		
		if (value != ALIVE_CELL && value != DEAD_CELL) {
			throw new IllegalArgumentException();
		}
		else if (!isValidCoordinate(x, y)) {
			return null;

		}
		Cell temp = (Cell) this.board.getPiece(x, y);
		board.setPiece(x, y, new Cell(value));
		return temp;

	}
	
	public boolean willbeAlive(int x, int y) {
		/* 
		 * Returns true if a given cell will be alive in the next generation
		 * 
		 * Any live cell with fewer than two live neighbors dies, as if by underpopulation.
		 * Any live cell with two or three live neighbors lives on to the next generation.
		 * Any live cell with more than three live neighbors dies, as if by overpopulation.
		 * Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
		 * 
		 **/
		
		if (isValidCoordinate(x, y)) {
			boolean isAlive = ((Cell) board.getPiece(x, y)).isAlive();
			int neighbors = this.countLivingNeighbors(x, y);
			
			if (!isAlive && neighbors != BIRTH_VALUE) {
				return false;
			}
			else if (!isAlive && neighbors == BIRTH_VALUE) {
				return true;
			}
			else if (isAlive && neighbors >= OVERCROWDING_LOWER_THRESHOLD) {
				return false;
			}
			else if (isAlive && neighbors <= LONELINESS_UPPER_THRESHOLD) {
				return false;
			}
			else {
				return true;
			}
		}
		return false;
	}
	
	public int getWidth() {
		/* 
		 * Returns width of GameOfLife's board
		 * 
		 **/
		
		return this.board.getWidth();
	}
	
	public int getHeight() {
		/* 
		 * Returns height of GameOfLife's board
		 * 
		 **/
		
		return this.board.getHeight();
	}
	
	public void nextGen() {
		/* 
		 * Updates the board to the next generation of the game
		 * 
		 **/
		
		//Creates array that represents next generation
		Cell[][] nextGenBoard = new Cell[this.board.getHeight()][this.board.getWidth()];
		
		//Loops through each of the current board values
		for (int i = 0; i < this.board.getHeight(); i++) {
			for (int j = 0; j < this.board.getWidth(); j++) {
				//Checks if it will be alive in the next generation
				if (this.willbeAlive(j, i)) {
					//Updates nextGenBoard with value it will hold in the next generation
					nextGenBoard[i][j] = new Cell(ALIVE_CELL);
				}
				else {
					nextGenBoard[i][j] = new Cell(DEAD_CELL);
				}
			}
		}
		
		//Updates current board with next generation board
		this.board = new GameBoard(nextGenBoard);
		this.gen++;
	}
	
	public void setDead(int x, int y) {
		/*
		 * Sets cell at given location to DEAD
		 *  
		 **/
		
		this.setCell(x, y, DEAD_CELL);
	}
	
	public void setAlive(int x, int y) {
		/*
		 * Sets cell at given location to ALIVE
		 *  
		 **/
		
		this.setCell(x, y, ALIVE_CELL);
	}
	
	public int getGen() {
		/* 
		 * Returns current generation
		 * 
		 * */
		return this.gen;
	}
}

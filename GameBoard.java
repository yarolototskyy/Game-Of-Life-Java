
public class GameBoard {
	/* 
	 * Representation of a GameBoard that stores children of the class 'GamePiece'
	 * 
	 * */
	
	private GamePiece[][] board; //2-D array that stores the actual game-board
	private int width, height;

	public GameBoard(int width, int height) {
		/* 
		 * Constructs an empty GameBoard with specified width and height
		 * 
		 * */
		if (width >= 0 && height >= 0) {
			board = new GamePiece[height][width];
			this.width = width;
			this.height = height;
		}
		else {
			throw new IllegalArgumentException();
		}
	}
	
	public GameBoard(int width) {
		/* 
		 * Constructs a square, empty GameBoard with specified side length
		 * 
		 * */
		
		this(width, width);
	}
	
	public GameBoard(GamePiece[][] board) {
		/* 
		 * Constructs an GameBoard that copies the contents of a 2-D array of GamePiece
		 * Throws IllegalArgumentException if given 2-D array does not represent a rectangular grid
		 * 
		 * */
		
		//Checks to make sure that the given 2-D array is a rectangle
		for (GamePiece[] innerarr : board) {
			if (innerarr.length != board[0].length) {
				throw new IllegalArgumentException("Please input a 2-D Array with equal length rows");
			}
		}
		
		//Copies contents of given array into a new array, not just a reference
		this.width = board[0].length;
		this.height = board.length;		
		this.board = new GamePiece[height][width];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				this.board[j][i] = board[j][i];
			}
		}
	}
	
	public int getWidth() {
		/* 
		 * Returns width of GameBoard object
		 * 
		 * */

		return this.width;
	}
	
	public int getHeight() {
		/* 
		 * Returns height of GameBoard object
		 * 
		 * */

		return this.height;
	}
	
	public String toString() {
		/* 
		 * Returns String representation of GameBoard object
		 * 
		 * */

		String result = "";
		for (int i = 0; i < this.getHeight(); i++) {
			for (int j = 0; j < this.getWidth(); j++) {
				result += (this.getPiece(j, i) + " ");
			}
			result += "\n";
		}
		
		return result;
	}
	
	public void clear() {
		/* 
		 * Clears GameBoard by setting every cell to null
		 * 
		 * */

		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				board[i][j] = null;
			}
		}
	}
	
	public GamePiece setPiece(int x, int y, GamePiece piece) {
		/* 
		 * Sets given piece onto given (x,y) location on the board, replacing piece previously at that location
		 * Returns piece that was replaced
		 * If given location is off the board, returns null
		 * 
		 * */

		if (onBoard(x, y)) {	
			GamePiece temp = board[y][x];
			board[y][x] = piece;
			return temp;
		}
		return null;
	}

	public GamePiece removePiece(int x, int y) {
		/* 
		 * Removes piece from given (x,y) location on the board by setting it to null
		 * Returns piece that was removed
		 * If given location is off the board, returns null
		 * 
		 * */

		if (onBoard(x, y)) {	
			GamePiece temp = board[y][x];
			board[y][x] = null;
			return temp;
		}
		return null;
	}
	
	public GamePiece getPiece(int x, int y) {
		/* 
		 * Returns piece on given (x,y) location on the board
		 * If given location is off the board, returns null
		 * 
		 * */

		if (onBoard(x, y)) {
			return board[y][x];
		}
		else {
			return null;
		}
	}
	
	public boolean onBoard(int x, int y) {
		/* 
		 * Returns true if given location is a valid location on GameBoard
		 * 
		 * */

		return (x < width && y < height && x >= 0 && y >= 0);
	}
	
	public boolean hasPiece(int x, int y) {
		/* 
		 * Returns true if (x, y) location is a GamePiece
		 * Returns null if (x, y) location is null
		 * */

		return (board[y][x] != null);
	}
}

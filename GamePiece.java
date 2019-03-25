
public abstract class GamePiece {
	/* 
	 * Representation of a GamePiece that stores an integer value
	 * 
	 * */
	
	private int value;
	
	public GamePiece(int value) {
		/* 
		 * Constructs a GamePiece given an integer value representation
		 * 
		 * */

		this.value = value;
	}
	
	public int getValue() {
		/* 
		 * Returns value of GamePiece
		 * 
		 * */
		return this.value;
	}
	
	public void changeState(int newState) {
		/* 
		 * Changes the value of the piece
		 * 
		 * */
		
		this.value = newState;
	}
	
	public String toString() {
		/* 
		 * Returns String representation of GamePiece (Just its value)
		 * 
		 * */
		
		return this.value + "";
	}
	
}

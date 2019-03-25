
public class Cell extends GamePiece {

	public static final int DEAD = 0;
	public static final int ALIVE = 1;
	
	
	public Cell(int value) {
		/* 
		 * Constructs Cell with given value
		 * If value is not ALIVE or DEAD throws IllegalArgumentException
		 * 
		 * */

		super(value); //Calls GamePiece constructor
		
		if (value != DEAD && value != ALIVE) { //Checks if valid value given
			throw new IllegalArgumentException();
		}
	}
	
	public String toString() {
		/* 
		 * Returns String representation of Cell
		 * 'A' if alive
		 * 'D' if dead
		 * 
		 * */

		if (super.getValue() == ALIVE) {
			return "A";
		}
		return "D";
	}
	
	public boolean isAlive() {
		/* 
		 * Returns true if a cell is alive
		 * 
		 * */
		
		return super.getValue() == ALIVE;
	}
	
	public void setAlive() {
		/* 
		 * Sets the state of this cell to ALIVE
		 * 
		 * */

		super.changeState(ALIVE);
	}
	
	public void setDead() {
		/* 
		 * Sets the state of this cell to DEAD
		 * 
		 * */

		super.changeState(DEAD);
	}	
	
}

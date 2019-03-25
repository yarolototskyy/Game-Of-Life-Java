import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.Timer;

/*
 * Displays generations of John Conway's Game of Life.
 * Allows a user of the program to step through one generation
 * at a time or to run the generations based on a timer.
 */
public class GameOfLifeDisplay extends JFrame {
	/* 
	 * Game of Life GUI that displays the game of life and allows customization
	 * 
	 * FEATURES -
	 * 
	 * 		> Can use mouse to set alive cells on the board
	 * 			>Clicking on a cell will toggle it to the opposite state
	 * 			>Clicking and dragging will allow user to "paint" alive cells on the board
	 * 
	 * 		> Tabs in the bottom of the console allow easy navigation
	 * 
	 * 		> 'Home' tab houses the basic controls for the game
	 * 			> Random Setup - randomizes board
	 * 			> Clear - clears board
	 * 			> Next Gen - Goes forward by one generation
	 * 			> Start/Stop - Allows auto-play of Game of Life
	 * 			> Speed Slider allows user to change speed of auto-play
	 * 				> 5 Speeds, each one 2.7x faster than the next
	 * 				> Slowest speed on the left, Fastest on the right
	 * 				> Allows consistent and fluid speed manipulation
	 * 
	 * 		> 'Customize' allows user to change board size and color 
	 * 			> Input board width and height into textboxes
	 * 			  **Note that inputting invalid numbers will throw an error in the console
	 *  		> Click on the small colored square to toggle desired border color
	 *  		> Click on the Create Game button to create the new board
	 * 			
	 * 		> 'Presets' tab stores cool/famous GameOfLife set-ups
	 * 			> Each will reset board size to 50x50 but keep color the same
	 * 
	 **/
	
	private JPanel contentPane;
	private JLabel txtGeneration = new JLabel();
	private GameOfLife g; 
	private BoardPanel boardPanel;
	private Color borderColor;
	private int ALIVE_CELL = Cell.ALIVE;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameOfLifeDisplay frame = new GameOfLifeDisplay();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame. Adds a button panel to the frame and
	 * initializes the usage of each button.
	 */
	public GameOfLifeDisplay() {
		g = new GameOfLife();// call an appropriate constructor

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		//Creates Mouse Event Handler that will always be attached to boardPanel
		
		MouseAdapter clickListener = new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				int numSquaresX = g.getWidth();
				int numSquaresY = g.getHeight();
				int squareSideX = boardPanel.getWidth() / numSquaresX;
				int squareSideY = boardPanel.getHeight() / numSquaresY;				
				
				if (e.getX() / squareSideX < numSquaresX && e.getY() / squareSideY < numSquaresY) {
					g.changeState(e.getX() / squareSideX, e.getY() / squareSideY);
					repaint();
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseDragged(MouseEvent e) {
				int numSquaresX = g.getWidth();
				int numSquaresY = g.getHeight();
				int panelWidth = boardPanel.getWidth();
				int panelHeight = boardPanel.getHeight();
				int squareSideY, squareSideX;
				
				squareSideX = panelWidth / numSquaresX;
				squareSideY = panelHeight / numSquaresY;				
				if (e.getX() / squareSideX < g.getWidth() && e.getY() / squareSideY < g.getHeight() && e.getX() / squareSideX >= 0 && e.getY() / squareSideY >= 0) {
					g.setCell(e.getX() / squareSideX, e.getY() / squareSideY, ALIVE_CELL);
					repaint();
				}
			}
			
			
		};
		
		
		//Creates slider that will change delay between each generation during auto-playing
		JSlider speedSlider = new JSlider(3, 7);
		
		//Sets multiplier effect slider will have between each setting
		double jSliderSpeedMultiplier = 2.7;

		speedSlider.setMajorTickSpacing(1);
		speedSlider.setPaintTicks(true);
		speedSlider.setInverted(true);
		
		/*
		 * creates a Timer and defines what will occur when
		 * it is run when the user clicks the "start" button
		 */
		Timer timer = new Timer((int)Math.pow(jSliderSpeedMultiplier, speedSlider.getValue()), new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO what should happen each time the Timer is fired off
				g.nextGen();
				txtGeneration.setText("Generation: " + g.getGen());
				repaint();
			}
			
		});

		//Adds event handler to speed slider that will change whenever user moves slider
		
		speedSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				timer.setDelay((int)Math.pow(jSliderSpeedMultiplier, speedSlider.getValue()));
			}

			
		});

		/*
		 * creates the button panel
		 */
		JPanel buttonPanel = new JPanel();
		contentPane.add(buttonPanel, BorderLayout.NORTH);
		
		/*
		 * adds a button which allows the user to step through
		 * the game one generation at a time
		 */
		JButton nextGenButton = new JButton("Next Gen");
		nextGenButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO generate and display the next generation
				g.nextGen();
				txtGeneration.setText("Generation: " + g.getGen());
				repaint();
			}
			
		});
		
		/*
		 * creates a button that allows the game to run on 
		 * a timer. The label toggles between "Start" and "Stop"
		 */
		JButton startStopButton = new JButton("Start");
		startStopButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(startStopButton.getText().equals("Start")){
					startStopButton.setText("Stop");
					// TODO start the generations 
					timer.start();
				}
				else{
					startStopButton.setText("Start");
					// TODO stop the generations
					timer.stop();
				}
				
			}
			
		});
		
		/*
		 * Creates a button clears the board 		 * 
		 */
		
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO generate and display the next generation
				g.clear();
				txtGeneration.setText("Generation: " + g.getGen());
				repaint();
				if (timer.isRunning()) {
					timer.stop();
				}
				if (startStopButton.getText().equals("Stop")) {
					startStopButton.setText("Start");
				}
			}
			
		});
		
		/*
		 * creates a button that randomizes the board 
		 */
		
		
		JButton randomButton = new JButton("Random Setup");
		randomButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO generate and display the next generation
				g.randomSetup();
				if (timer.isRunning()) {
					timer.stop();
				}
				if (startStopButton.getText().equals("Stop")) {
					startStopButton.setText("Start");
				}				
				txtGeneration.setText("Generation: " + g.getGen());
				repaint();
			}
			
		});

		/*
		 * displays the generation number
		 */
		txtGeneration.setText("Generation: 0");
		buttonPanel.add(txtGeneration);
		
		/*
		 * adds the panel which displays the Game of Life
		 * board. See the BoardPanel class for details.
		 */
		boardPanel = new BoardPanel(g);
		this.borderColor = Color.BLACK;
		contentPane.add(boardPanel, BorderLayout.CENTER);
		
		//Adds mouse listeners for click & drag events
		boardPanel.addMouseListener(clickListener);
        boardPanel.addMouseMotionListener(clickListener);
		
        //Adds tabs
		JTabbedPane tabs = new JTabbedPane();
		contentPane.add(tabs, BorderLayout.SOUTH);
		
		//Creates home panel
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(1, 1));
		panel.add(randomButton);
		panel.add(clearButton);
		panel.add(nextGenButton);
		panel.add(startStopButton);
		panel.add(speedSlider);
		panel.setPreferredSize(new Dimension(0, 10));
        
		//Adds home panel to tabs
		tabs.addTab("Home", panel);
		
		//Creates panel for the 'customize' tab
        JPanel panel2 = new JPanel(false);
        
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        panel2.setPreferredSize(new Dimension(0, 10));
        
        JFormattedTextField widthTextBox = new JFormattedTextField();
        JFormattedTextField heightTextBox = new JFormattedTextField();
        
        widthTextBox.setPreferredSize(new Dimension(35, 20));
        heightTextBox.setPreferredSize(new Dimension(35, 20));        
        
        panel2.add(new JLabel("Dimensions: "));
        panel2.add(widthTextBox);
        panel2.add(new JLabel("x "));
        panel2.add(heightTextBox);

		
		JPanel colorBox = new JPanel();
		colorBox.setPreferredSize(new Dimension(19, 19));
		colorBox.setLayout(new CardLayout());
		
		JPanel bluePanel = new JPanel();
		bluePanel.setBackground(Color.BLUE);
		JPanel yellowPanel = new JPanel();
		yellowPanel.setBackground(Color.YELLOW);
		colorBox.add(bluePanel);
		JPanel blackPanel = new JPanel();
		blackPanel.setBackground(Color.BLACK);
		colorBox.add(blackPanel);
		colorBox.add(yellowPanel);
		JPanel pinkPanel = new JPanel();
		pinkPanel.setBackground(Color.PINK);
		colorBox.add(pinkPanel);

		/*
		 * Creates a button removes current board panel and updates it with the new settings  
		 */
		
		JButton newGameButton = new JButton("Create Game");
		panel2.add(newGameButton);
		newGameButton.setPreferredSize(new Dimension(125, 19));
		newGameButton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (timer.isRunning()) {
					timer.stop();
				}
				if (startStopButton.getText().equals("Stop")) {
					startStopButton.setText("Start");
				}
				
				//Reads textboxes and creates a new game of life based on size inputs
				g = new GameOfLife(Integer.parseInt(widthTextBox.getText()), Integer.parseInt(heightTextBox.getText()));
				
				//Updates borderColor based on which is currently toggled
				borderColor = Color.BLACK;
				if (yellowPanel.isVisible()) {
					borderColor = Color.YELLOW;
				}
				else if (pinkPanel.isVisible()) {
					borderColor = Color.PINK;
				}
				else if (bluePanel.isVisible()) {
					borderColor = Color.BLUE;
				}
				
				//Removes current board panel
				contentPane.remove(boardPanel);
				
				//Creates and adds new one
				boardPanel = new BoardPanel(g, borderColor);
				contentPane.add(boardPanel, BorderLayout.CENTER);
				repaint();
				boardPanel.addMouseListener(clickListener);
		        boardPanel.addMouseMotionListener(clickListener);

				txtGeneration.setText("Generation: " + g.getGen());
				revalidate();
				repaint();
			}
		});

		panel2.add(new JLabel(" Border Color: "));

		panel2.add(colorBox);
		
		//Adds listener to colorBox so that the user can cycle by clicking on it
		colorBox.addMouseListener(new MouseAdapter() {  
	        public void mouseClicked(MouseEvent e) {              
	    		((CardLayout)(colorBox.getLayout())).next(colorBox);
	    		repaint();
	        }                 
	     });  
	
			
		panel2.add(new JLabel("  "));
		panel2.add(newGameButton);
		tabs.addTab("Customize", panel2);

		//Creates panel for 'Presets'
        JPanel presetPanel = new JPanel();
		presetPanel.setLayout(new GridLayout(2,2));
        
       /* 
        * Following code creates all the buttons for the presets
        * 
        * Preset-code cell-setting code was generated by printing corresponding
        * code to console based on user clicks on the GUI
        * 
        * */
		
		
		JButton gliderPreset = new JButton("Gosper Glider");
		gliderPreset.setPreferredSize(new Dimension(125, 20));;
		gliderPreset.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO generate and display the next generation
				if (timer.isRunning()) {
					timer.stop();
				}
				if (startStopButton.getText().equals("Stop")) {
					startStopButton.setText("Start");
				}
				

				contentPane.remove(boardPanel);
				g = new GameOfLife();
				g.setCell(2, 0, ALIVE_CELL);
				g.setCell(2, 1, ALIVE_CELL);
				g.setCell(2, 2, ALIVE_CELL);
				g.setCell(1, 2, ALIVE_CELL);
				g.setCell(0, 1, ALIVE_CELL);
				boardPanel = new BoardPanel(g, borderColor);
				contentPane.add(boardPanel, BorderLayout.CENTER);
				repaint();
				boardPanel.addMouseListener(clickListener);
		        boardPanel.addMouseMotionListener(clickListener);

				txtGeneration.setText("Generation: " + g.getGen());
				revalidate();
				repaint();
			}
			
		});

		JButton hLinePreset = new JButton("Horizontal Line");
		hLinePreset.setPreferredSize(new Dimension(125, 20));;
		hLinePreset.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO generate and display the next generation
				if (timer.isRunning()) {
					timer.stop();
				}
				if (startStopButton.getText().equals("Stop")) {
					startStopButton.setText("Start");
				}
				

				contentPane.remove(boardPanel);
				g = new GameOfLife();
				for (int i = 0; i < g.getWidth(); i++) {
					g.setCell(i, g.getHeight()/2, ALIVE_CELL);
				}
				boardPanel = new BoardPanel(g, borderColor);
				contentPane.add(boardPanel, BorderLayout.CENTER);
				repaint();
				boardPanel.addMouseListener(clickListener);
		        boardPanel.addMouseMotionListener(clickListener);

				txtGeneration.setText("Generation: " + g.getGen());
				revalidate();
				repaint();
			}
			
		});

		JButton vLinePreset = new JButton("Vertical Line");
		vLinePreset.setPreferredSize(new Dimension(125, 20));;
		vLinePreset.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO generate and display the next generation
				if (timer.isRunning()) {
					timer.stop();
				}
				if (startStopButton.getText().equals("Stop")) {
					startStopButton.setText("Start");
				}
				

				contentPane.remove(boardPanel);
				g = new GameOfLife();
				for (int i = 0; i < g.getHeight(); i++) {
					g.setCell(g.getWidth()/2, i, ALIVE_CELL);
				}
				boardPanel = new BoardPanel(g, borderColor);
				contentPane.add(boardPanel, BorderLayout.CENTER);
				repaint();
				boardPanel.addMouseListener(clickListener);
		        boardPanel.addMouseMotionListener(clickListener);

				txtGeneration.setText("Generation: " + g.getGen());
				revalidate();
				repaint();
			}
			
		});


		JButton dLinesPreset = new JButton("Diagonal Lines");
		dLinesPreset.setPreferredSize(new Dimension(125, 20));;
		dLinesPreset.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO generate and display the next generation
				if (timer.isRunning()) {
					timer.stop();
				}
				if (startStopButton.getText().equals("Stop")) {
					startStopButton.setText("Start");
				}
				

				contentPane.remove(boardPanel);
				g = new GameOfLife();
				for (int i = 0; i < g.getHeight(); i++) {
					g.setCell(i, i, ALIVE_CELL);
					g.setCell(i, g.getHeight() - i - 1, ALIVE_CELL);
					
				}
				boardPanel = new BoardPanel(g, borderColor);
				contentPane.add(boardPanel, BorderLayout.CENTER);
				repaint();
				boardPanel.addMouseListener(clickListener);
		        boardPanel.addMouseMotionListener(clickListener);

				txtGeneration.setText("Generation: " + g.getGen());
				revalidate();
				repaint();
			}
			
		});
		JButton pentadecathlonPreset = new JButton("Pentadecathlon");
		pentadecathlonPreset.setPreferredSize(new Dimension(125, 20));;
		pentadecathlonPreset.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO generate and display the next generation
				if (timer.isRunning()) {
					timer.stop();
				}
				if (startStopButton.getText().equals("Stop")) {
					startStopButton.setText("Start");
				}
				

				contentPane.remove(boardPanel);
				g = new GameOfLife();
				g.setCell(20, 24, ALIVE_CELL);
				g.setCell(21, 24, ALIVE_CELL);
				g.setCell(22, 25, ALIVE_CELL);
				g.setCell(22, 23, ALIVE_CELL);
				g.setCell(23, 24, ALIVE_CELL);
				g.setCell(24, 24, ALIVE_CELL);
				g.setCell(25, 24, ALIVE_CELL);
				g.setCell(26, 24, ALIVE_CELL);
				g.setCell(27, 23, ALIVE_CELL);
				g.setCell(27, 25, ALIVE_CELL);
				g.setCell(28, 24, ALIVE_CELL);
				g.setCell(29, 24, ALIVE_CELL);

				boardPanel = new BoardPanel(g, borderColor);
				contentPane.add(boardPanel, BorderLayout.CENTER);
				repaint();
				boardPanel.addMouseListener(clickListener);
		        boardPanel.addMouseMotionListener(clickListener);

				txtGeneration.setText("Generation: " + g.getGen());
				revalidate();
				repaint();
			}
			
		});

		JButton gosperGunPreset = new JButton("Gosper Gun");
		gosperGunPreset.setPreferredSize(new Dimension(125, 20));;
		gosperGunPreset.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO generate and display the next generation
				if (timer.isRunning()) {
					timer.stop();
				}
				if (startStopButton.getText().equals("Stop")) {
					startStopButton.setText("Start");
				}
				

				contentPane.remove(boardPanel);
				g = new GameOfLife();
				g.setCell(7, 13, ALIVE_CELL);
				g.setCell(8, 13, ALIVE_CELL);
				g.setCell(8, 14, ALIVE_CELL);
				g.setCell(7, 14, ALIVE_CELL);
				g.setCell(17, 14, ALIVE_CELL);
				g.setCell(17, 13, ALIVE_CELL);
				g.setCell(17, 15, ALIVE_CELL);
				g.setCell(18, 16, ALIVE_CELL);
				g.setCell(19, 17, ALIVE_CELL);
				g.setCell(20, 17, ALIVE_CELL);
				g.setCell(18, 12, ALIVE_CELL);
				g.setCell(19, 11, ALIVE_CELL);
				g.setCell(20, 11, ALIVE_CELL);
				g.setCell(21, 14, ALIVE_CELL);
				g.setCell(22, 12, ALIVE_CELL);
				g.setCell(23, 13, ALIVE_CELL);
				g.setCell(23, 14, ALIVE_CELL);
				g.setCell(24, 14, ALIVE_CELL);
				g.setCell(23, 15, ALIVE_CELL);
				g.setCell(22, 16, ALIVE_CELL);
				g.setCell(27, 13, ALIVE_CELL);
				g.setCell(27, 12, ALIVE_CELL);
				g.setCell(27, 11, ALIVE_CELL);
				g.setCell(28, 11, ALIVE_CELL);
				g.setCell(28, 12, ALIVE_CELL);
				g.setCell(28, 13, ALIVE_CELL);
				g.setCell(29, 14, ALIVE_CELL);
				g.setCell(29, 10, ALIVE_CELL);
				g.setCell(31, 10, ALIVE_CELL);
				g.setCell(31, 9, ALIVE_CELL);
				g.setCell(31, 14, ALIVE_CELL);
				g.setCell(31, 15, ALIVE_CELL);
				g.setCell(41, 12, ALIVE_CELL);
				g.setCell(41, 11, ALIVE_CELL);
				g.setCell(42, 11, ALIVE_CELL);
				g.setCell(42, 12, ALIVE_CELL);

				boardPanel = new BoardPanel(g, borderColor);
				contentPane.add(boardPanel, BorderLayout.CENTER);
				repaint();
				boardPanel.addMouseListener(clickListener);
		        boardPanel.addMouseMotionListener(clickListener);

				txtGeneration.setText("Generation: " + g.getGen());
				revalidate();
				repaint();
			}
			
		});

		
		presetPanel.add(gliderPreset);

		presetPanel.add(hLinePreset);

		presetPanel.add(vLinePreset);

		presetPanel.add(dLinesPreset);

		presetPanel.add(pentadecathlonPreset);
		
		presetPanel.add(gosperGunPreset);
		
		tabs.addTab("Presets", presetPanel);

	}
}

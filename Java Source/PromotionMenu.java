/*
*	PromotionMenu.java
*	Date of creation: June 13, 2018
*	Date of last modification: Sept 6, 2018
*
*	Author: Justin Underhay
*
*/

/*
	PromotionMenu.java is the class which generates/initializes the graphical menu that displays onscreen during the processPromotion static method located in the PlayChess.java file.
	Should a player move a Pawn piece to the opposite end side of the board they may promote that piece, changing it into another piece.
	The menu created displays JRadioButtons and an ImageIcon showing the current piece selected.
	A Jbutton on the menu when clicked records and can report the choice made.

	Implements ActionListener - To listen to when the 'select' JButton is clicked.

	Data members:
		pieceIcon - JLabel 			- Holds the ImageIcon representing the current selected piece
		selectionMade - boolean	 	- True if the 'select' JButton has been clicked, false otherwise
		color - char				- Color of the pieces that appear in the menu, either 'B' for black or 'W' for white	
		
*/


import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Dimension;		
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;	
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class PromotionMenu extends DisplayScreen {
	
	private JLabel pieceIcon;
	private boolean selectionMade;
	private char color;
	
	
	public PromotionMenu(char pieceColor) {
		
		//Initialize the main JPanel container 
		screen = new JPanel();
		screen.setSize(new Dimension(250,250));	
		
		//Creation and initialization of graphic objects and auxilliary data structures
		color = pieceColor;
		selectionMade = false;
		String[] pieceNames = {"Rook", "Knight", "Bishop", "Queen"};
		JRadioButton[] choices = new JRadioButton[4]; 
		ButtonGroup group = new ButtonGroup();
		pieceIcon = new JLabel(new ImageIcon("Supplemental\\" + pieceColor + "-Rook.png"));
		Color defaultBckg = new Color(255,206,153);
		
		JPanel pieceButtons = new JPanel(new GridLayout(0,1));
		JPanel selectPanel = new JPanel(new FlowLayout());
		JPanel titlePanel = new JPanel(new FlowLayout());
		JButton selectButton = new JButton("Select");
		JLabel title = new JLabel("<html><p style=\"text-align:center;\">Pawn is elligible for promotion.<br>Select promotion piece:</p></html>");
		
		//Initialize the radio buttons: add each to a button group and JPanel container, add action listeners, set action commands 
		for(int i=0; i<4; i++) {
			choices[i] = new JRadioButton(pieceNames[i]);
			choices[i].setActionCommand(pieceNames[i]);
			choices[i].addActionListener(this);
			group.add(choices[i]);
			pieceButtons.add(choices[i]);
			choices[i].setFont(new Font("Times New Roman", Font.BOLD, 16));
			choices[i].setBackground(new Color(255,206,153));
		}
		
		//To ensure a choice is made start with the Rook selected. Exactly 1 radio button will be selected at all times.
		choices[0].setSelected(true);
		selectionChoice = "Rook";
		
		//Initialize 'select' button
		selectButton.setActionCommand("Select");
		selectButton.addActionListener(this);
		selectButton.setFont(new Font("Book Antiqua", Font.BOLD, 16));
		
		//Set the layout and add all the Jpanels to the main container
		screen.setLayout(new BorderLayout());
		title.setFont(new Font("Book Antiqua", Font.BOLD, 20));
		
		//Set background colors
		titlePanel.setBackground(defaultBckg);
		selectPanel.setBackground(defaultBckg);
		selectButton.setBackground(new Color(204,230,255));
		screen.setBackground(defaultBckg);
		pieceButtons.setBackground(defaultBckg);

		//Add components to their proper containers
		selectPanel.add(selectButton);
		titlePanel.add(title);
		
		screen.add(pieceButtons,BorderLayout.WEST);
		screen.add(pieceIcon, BorderLayout.CENTER);
		screen.add(selectPanel, BorderLayout.SOUTH);
		screen.add(titlePanel, BorderLayout.NORTH);
		
	}	

	
	/*
		selectionMade() - Returns boolean determining whether the player has selected a choice.
	*/
	public boolean selectionMade() {
		return selectionMade;
	}
	
	
	
	/*
		actionPerformed(ActionEvent event) - Overridden from DisplayScreen.java, activated when the player selects
		a radio button or clicks the 'select' button and performs the appropriate action.
	*/
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equals("Select"))
			selectionMade = true;
		else {
			pieceIcon.setIcon(new ImageIcon("Supplemental\\" + color + "-" + event.getActionCommand() + ".png"));
			selectionChoice = event.getActionCommand();
		}	
	}	
}
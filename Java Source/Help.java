/*
*	Help.java
*	Date of creation: July 29, 2018
*	Date of last modification: Sept 6, 2018
*	
*	Author: Justin Underhay
*	
*/

/*
	Help.java is the GUI class representing the 'Help' screen.
	Every Help object is a JPanel containing the rules of Chess and how to interact with the program written on a JLabel.
	JButtons leading to the Welcome and play screens are also present.	
	Due to the size of the printed rules a JScrollPane controls the view of the main JPanel allowing the user to scroll the screen vertically.
		
	Inherits from DisplayScreen.java
	
	Data members:
		scrollPane - JScrollPane	- Controls presentation of main container by allowing a scrollable view of the main component 
*/


import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.BorderLayout;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


public class Help extends DisplayScreen {
	
	private JScrollPane scrollPane;
	
	
	public Help() {
		
		selectionChoice = "";
		
		//Declare/Initialize objects
		screen = new JPanel(new BorderLayout());
		JPanel buttons = new JPanel();
		JButton back = new JButton("Back");
		JButton play = new JButton("Play!");
		
		back.setActionCommand("Back");
		play.setActionCommand("Play!");
		
		back.addActionListener(this);
		play.addActionListener(this);
		
		//Get rules content
		putRules();
		
		//Initialize scrollPane and set scroll speed
		scrollPane = new JScrollPane(screen, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		
		buttons.add(back);
		buttons.add(play);
		
		//Set cosmetic properties of back and play buttons
		back.setBackground(new Color(255,206,153));
		back.setFont(new Font("Book Antiqua", Font.BOLD, 16));
		play.setBackground(new Color(255,206,153));
		play.setFont(new Font("Book Antiqua", Font.BOLD, 16));
		
		screen.add(buttons,"South");
		
		//Set background colors
		screen.setBackground(new Color(96,64,32));
		buttons.setBackground(new Color(96,64,32));

	}
	
	
	/*
		putRules() - Initializes a JLabel and fills it with rules obtained from the supplementary file 'Rules.htm' which 
		contains content and style properties written in HTML. Also sets some appearance properties.
	*/
	public void putRules() {
	
		JLabel rules = new JLabel();
		
		try {
			Scanner getRules = new Scanner(new File("Supplemental\\Rules.htm"));
			while (getRules.hasNext()) 
				rules.setText(rules.getText() + getRules.nextLine());	
			
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Cannot find the rules file: Rules.htm. Check Supplemental directory");
			System.exit(-1);
		}	
		
		rules.setBackground(new Color(96,64,32));
		rules.setForeground(new Color(217,217,217));
		
		screen.add(rules);
		
	}	
	
	
	/*
		getScrollPane() - Returns the scrollPane data member.
	*/
	public JScrollPane getScrollPane() {
		return scrollPane;
	}	
}	
/*
*	Welcome.java
*	Date of creation: July 22, 2018
*	Date of last modification: Sept 6, 2018
*	
*	Author: Justin Underhay
*	
*/

/*
	Welcome.java is the GUI class representing the 'Welcome' screen seen by users when PlayChess.java is first executed.
	The 'Welcome' screen presents 3 options to the user allowing them to play the game, see help, or quit the program.
	
	Inherits from DisplayScreen.java

*/


import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SpringLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Color;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.CompoundBorder;


public class Welcome extends DisplayScreen {
	
	
	public Welcome() {
		
		selectionChoice = "";
		
		//Set the layout for main container, SpringLayout in this case
		SpringLayout SLayout = new SpringLayout();
		screen = new JPanel(SLayout);
		
		//Declare and initialize objects
		JLabel RIcon = new JLabel(new ImageIcon("Supplemental\\B-Knight.png"));
		JLabel LIcon = new JLabel(new ImageIcon("Supplemental\\W-Bishop.png"));
		JLabel version = new JLabel("Version 1.210");
		JLabel supp = new JLabel("Author: J.J.U    Date of last modification: 2018-11-07");
		JLabel title = new JLabel("Welcome to Java Chess!");
		JButton[] buttons = new JButton[3];
		String[] names = {"Play!","Help","Quit"};	
		
		//Set title and icons appearance
		title.setFont(new Font("Book Antiqua", Font.BOLD, 32));		
		title.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
		title.setBorder(new CompoundBorder(title.getBorder(), new EmptyBorder(10,10,10,10)));
		RIcon.setBorder(new CompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), new EmptyBorder(10,10,10,10)));
		LIcon.setBorder(new CompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), new EmptyBorder(10,10,10,10)));

		//Set properties of JButtons
		for(int i=0; i<3; i++) {
			buttons[i] = new JButton(names[i]);
			buttons[i].setFont(new Font("Book Antiqua", Font.BOLD, 16));
			buttons[i].setBackground(new Color(116,75,37));
			buttons[i].setForeground(new Color(220,220,220));
			buttons[i].setActionCommand(names[i]);
			buttons[i].addActionListener(this);
			screen.add(buttons[i]);
		}
		
		//Add components to main container
		screen.add(title);
		screen.add(RIcon);
		screen.add(LIcon);
		screen.add(version);
		screen.add(supp);
		
		//Set background colors
		screen.setBackground(new Color(255,206,153));
		title.setBackground(new Color(96,64,32));
		RIcon.setBackground(new Color(96,64,32));
		LIcon.setBackground(new Color(96,64,32));
		
		//Put SpringLayout constraints to finalize component placement
		SLayout.putConstraint("West", title, 150, SpringLayout.WEST, screen);
		SLayout.putConstraint(SpringLayout.NORTH, title, 10, SpringLayout.NORTH, screen);
		
		SLayout.putConstraint(SpringLayout.NORTH, buttons[0], 100, SpringLayout.SOUTH, title);
		SLayout.putConstraint(SpringLayout.WEST, buttons[0], 300, SpringLayout.WEST, screen);
		
		SLayout.putConstraint(SpringLayout.NORTH, buttons[1], 50, SpringLayout.SOUTH, buttons[0]);
		SLayout.putConstraint(SpringLayout.WEST, buttons[1], 300, SpringLayout.WEST, screen);
		
		SLayout.putConstraint(SpringLayout.NORTH, buttons[2], 50, SpringLayout.SOUTH, buttons[1]);
		SLayout.putConstraint(SpringLayout.WEST, buttons[2], 300, SpringLayout.WEST, screen);
		
		SLayout.putConstraint("North", RIcon, 200, SpringLayout.NORTH, screen);
		SLayout.putConstraint(SpringLayout.EAST, RIcon, -80, SpringLayout.EAST, screen);
		
		SLayout.putConstraint(SpringLayout.NORTH, LIcon, 200, SpringLayout.NORTH, screen);
		SLayout.putConstraint(SpringLayout.WEST, LIcon, 80, SpringLayout.WEST, screen);
		
		SLayout.putConstraint(SpringLayout.NORTH, version, 5, SpringLayout.SOUTH, title);
		SLayout.putConstraint(SpringLayout.WEST, version, 290, SpringLayout.WEST, screen);
		
		SLayout.putConstraint(SpringLayout.SOUTH, supp, -5, SpringLayout.SOUTH, screen);
		SLayout.putConstraint(SpringLayout.WEST, supp, 190, SpringLayout.WEST, screen);
		
	}	
}	
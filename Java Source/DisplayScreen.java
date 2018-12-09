/*
*	DisplayScreen.java
*	Date of creation: July 26, 2018
*	Date of last modification: Sept 6, 2018
*
*	Author: Justin Underhay
*
*/

/*
	DisplayScreen.java is the superclass for all classes that implement visible GUI components.
	The child classes are Help.java, Welcome.java, PromotionMenu.java.
	Provides common functionality and data for these child classes.
	
	Implements ActionListener interface
	
	Data members:
		screen - JPanel				- The main container that holds all other components declared in child classes
		selectionChoice - String	- For PromotionMenu.java which registers the user's Pawn promotion choice of piece.	
*/


import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class DisplayScreen implements ActionListener {
	
	protected JPanel screen;
	protected String selectionChoice;


	/*
		getPanel() - Returns the screen JPanel member.
	*/
	public JPanel getPanel() {
		return screen;
	}	
	
	
	/*
		getChoice() - Returns the member String selectionChoice which differs in potential value based on the inheriting child class.
	*/
	public String getChoice() {
		return selectionChoice;
	}	
	
	
	/*
		reset() - Resets the selectionChoice data member to a blank String.
	*/
	public void reset() {
		selectionChoice = "";
	}	
	

	/*
		actionPerformed(ActionEvent event) - Overridden for specific functionality in child classes,
		implemented as part of the ActionListener interface.
	*/
	public void actionPerformed(ActionEvent event) { 
		selectionChoice = event.getActionCommand();
	}
	
}
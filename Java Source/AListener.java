/*
*	AListener.java
*	Date of creation: August 26, 2018
*	Date of last modification: Sept 6, 2018
*	
*	Author: Justin Underhay
*	
*/

/*
	AListener.java is the ActionListener interface implementing class for use in the 'Quit' JButton on the main JFrame.
*/


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class AListener implements ActionListener {

	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand() == "Quit") {
			if (JOptionPane.showConfirmDialog(null, "Are you sure you wish to quit?") == JOptionPane.YES_OPTION)
				System.exit(1);	
		}	
		
	}	


}
/*
*
*	MouseInputAdapter.java
*	Date of creation: April 18, 2018
*	Date of last modification: Sept 6, 2018
*	
*	Author: Justin Underhay
*
*/

/*
	MouseInputAdapter.java implements the MouseInputListener interface such that other classes can inherit and override the methods to
	listen and respond to mouse actions/events.
*/


import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;

public class MouseInputAdaptor implements MouseInputListener {

	public void mouseClicked(MouseEvent event) { }
	
	public void mouseDragged(MouseEvent event) { }
	
	public void mouseEntered(MouseEvent event) { }

	public void mouseExited(MouseEvent event) { }
	
	public void mouseMoved(MouseEvent event) { }
	
	public void mousePressed(MouseEvent event) { }
	
	public void mouseReleased(MouseEvent event) { }

}
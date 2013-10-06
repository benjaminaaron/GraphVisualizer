package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.SwingUtilities;
import view.GraphPanel;


public class MouseControl implements MouseListener{

	private GraphPanel w;
	
	public MouseControl(GraphPanel w){
		this.w = w;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {	
		boolean leftButton = true;	
		if(SwingUtilities.isRightMouseButton(arg0))
			leftButton = false;	
		w.handleMouseReleasedCoords(arg0.getX(), arg0.getY(), leftButton);
	}

	
	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}
	
}

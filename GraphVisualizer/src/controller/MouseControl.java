package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.SwingUtilities;
import view.GraphPanel;


public class MouseControl implements MouseListener{

	private GraphPanel graphPanel;
	int clickX, clickY;
	
	public MouseControl(GraphPanel graphPanel){
		this.graphPanel = graphPanel;
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		clickX = arg0.getX();
		clickY = arg0.getY();
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {		
		boolean leftButton = true;	
		if(SwingUtilities.isRightMouseButton(arg0))
			leftButton = false;	
		graphPanel.handleMouseRelease(clickX, clickY, arg0.getX(), arg0.getY(), leftButton);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	
	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}
}

package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import view.GraphPanel;


public class MouseMotionControl implements MouseMotionListener{

    private GraphPanel graphPanel;

    public MouseMotionControl(GraphPanel graphPanel){
        this.graphPanel = graphPanel;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        graphPanel.handleMousePos(e.getX(), e.getY());
    }


    @Override
    public void mouseDragged(MouseEvent e) {
    }
}

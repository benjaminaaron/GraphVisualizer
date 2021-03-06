package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.MouseControl;
import controller.MouseMotionControl;
import model.Animation.Frame;
import model.Line;
import model.Point;
import model.Animation.Timeline;


public class GraphPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private Animator animator;
    private GraphPanel thisGraphPanel = this;
    private ControlPanel controlPanel;
    private AnimationPanel animationPanel;

    private int xOffset = 650;
    private int yOffset = 80;

    private int nodeSize;
    private int nodeVertDist;

    /**
     * The index of the current animation type.
     */
    private Timeline timeline;
    private Frame currentFrame;

    private Point mouseOverPoint = null;

    private boolean showNodeID = false;
    private boolean showHelplines = false;

    //for double buffering
    private Image dbImage;
    private Graphics dbg;


    public GraphPanel() {
        MouseControl mc = new MouseControl(this);
        addMouseListener(mc);

        MouseMotionControl mmc = new MouseMotionControl(this);
        addMouseMotionListener(mmc);
    }

    public void setAnimator(Animator animator) {
        this.animator = animator;
    }

    //TODO this definitely needs a slider or some cool smart self-checking adaption...
    public void setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void setControlAndSliderPanel(ControlPanel controlPanel, AnimationPanel animationPanel) {
        this.controlPanel = controlPanel;
        nodeSize = controlPanel.getNodeSize();
        nodeVertDist = controlPanel.getNodVertDist();

        this.animationPanel = animationPanel;
    }

    public void setNodeSizeAndNodeVertDist(int nodeSize, int nodeVertDist) {
        this.nodeSize = nodeSize;
        this.nodeVertDist = nodeVertDist;
    }

//	public void updateXoffset(){	
//		xOffset = getWidth() / 2;
//	}

    public void handleMousePos(int mouseX, int mouseY) {
        if(animator.isIdle() && currentFrame != null){
            mouseOverPoint = coordsAreOnPoint(mouseX, mouseY);
            repaint();
        }
    }

    private Point coordsAreOnPoint(int x, int y){
        //point is being treated as rectangular... maybe better as the actual circles they are?
        for (Point point : currentFrame.points) {
            double boundaryLEFT = point.x + xOffset - nodeSize / 2;
            double boundaryRIGHT = boundaryLEFT + nodeSize;
            double boundaryUP = point.y + yOffset - nodeSize / 2;
            double boundaryDOWN = boundaryUP + nodeSize;
            if (x > boundaryLEFT && x < boundaryRIGHT && y > boundaryUP && y < boundaryDOWN)
                return point;
        }
        return null;
    }


    public void handleMouseRelease(int clickX, int clickY, int releaseX, int releaseY, boolean leftButton) {
        mouseOverPoint = null;
        //System.out.println("clickX: " + clickX + "  clickY: " + clickY + "  releaseX: " + releaseX + "  releaseY: " + releaseY);
        if (animator.isIdle() && currentFrame != null) {

            Point clickedPoint = coordsAreOnPoint(clickX, clickY);

            //DEACTIVATING THAT FEATURE FOR NOW, will implement it better soon

/*
            Point releasedPoint = coordsAreOnPoint(releaseX, releaseY);
            Point closestPointToRelease = null;
            double closest = 1000;
            boolean rightFromThis = true;

            if (clickedPoint != null && clickedPoint != releasedPoint) {
                for (Point point : currentFrame.points) {
                    if (point != clickedPoint) {
                        double pointDistToRelease = Math.sqrt(Math.pow(releaseX - (point.x + xOffset), 2) + Math.pow(releaseY - (point.y + yOffset), 2));
                        if (closest > pointDistToRelease) {
                            closest = pointDistToRelease;
                            closestPointToRelease = point;
                            rightFromThis = releaseX > (point.x + xOffset);
                        }
                    }
                }
            }
*/
            if(clickedPoint != null)
                controlPanel.handleNodeClicked(clickedPoint.nodeID, leftButton);

/*            if (clickedPoint != null) {
                if (clickedPoint == releasedPoint)
                    controlPanel.handleNodeClicked(clickedPoint.nodeID, leftButton);
                else
                    if (leftButton && closestPointToRelease != null) {
                        if (controlPanel.getHorizOrderIndex() == 0) {
                            controlPanel.handleNodeClicked(clickedPoint.nodeID, closestPointToRelease.nodeID, rightFromThis);
                            System.out.println("wanting to add a node under parent: " + clickedPoint.nodeID + " thats close to this child: " + closestPointToRelease.nodeID + " on the right side: " + rightFromThis);
                        } else {
                            JOptionPane.showMessageDialog(null, "placing children at specific places only makes sense when the order-index is chronological"
                                    + ", all other options overwrite the horizontal order anyway", "wrong order-modus", JOptionPane.PLAIN_MESSAGE);
                        }
                    }
            }
*/
        }
    }


    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
        showTimeline();
    }

    public void setCurrentFrame(Frame currentFrame) {
        this.currentFrame = currentFrame;
    }

    private void showTimeline() {
        if (timeline.getFrames().size() == 1) {
            currentFrame = timeline.getFrames().get(0);
            repaint();
        }
        else{
            ArrayList<Frame> frames = timeline.getFrames();
            System.out.println("size of frames: " + frames.size());
            animationPanel.enableSwitch(false);
            animator.setFrames(frames);
            animator.proceed();
        }
    }

    public void animationFinished() {
        animationPanel.enableSwitch(true);
        System.out.println("animation finished");
    }

    //double buffer setup via youtu.be/4T3WJEH7zrc
    public void paint(Graphics g) {
        if (currentFrame != null) {
            dbImage = createImage(getWidth(), getHeight());
            dbg = dbImage.getGraphics();
            paintComponent(dbg);
            g.drawImage(dbImage, 0, 0, this);
        }
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

        //g.setColor(Color.LIGHT_GRAY);
        //g.drawLine(xOffset, 0, xOffset, getHeight());

        g2d.setColor(Color.DARK_GRAY);
        for (Line line : currentFrame.lines) {
            if (!(line.target.x == 0 && line.target.y == 0)) //don't draw the lines when the target is still at 0,0 = not placed yet (rootnode can never be target)
            {
                g2d.drawLine((int) line.source.x + xOffset, (int) line.source.y + yOffset, (int) line.target.x + xOffset, (int) line.target.y + yOffset);
            }
        }
        g2d.setColor(new Color(0, 0, 128));
        for (Point point : currentFrame.points) {
            int x = (int) (point.x + xOffset);
            int y = (int) (point.y + yOffset);
            g2d.fillOval(x - nodeSize / 2, y - nodeSize / 2, nodeSize, nodeSize);

            if (showNodeID) {
                g2d.setColor(Color.GRAY);
                g2d.drawString(point.nodeID, x - 45, y + 25);
                g2d.setColor(new Color(0, 0, 128));
            }
        }

        if(mouseOverPoint != null){
            g2d.setColor(new Color(245, 245, 220));
            int x = (int) (mouseOverPoint.x + xOffset);
            int y = (int) (mouseOverPoint.y + yOffset);
            g2d.fillRect(x - 50, y - 30, 100, 20);
            g2d.setColor(new Color(193, 154, 107));
            g2d.drawRect(x - 50, y - 30, 100, 20);
            g2d.setColor(Color.GRAY);
            g2d.drawString(mouseOverPoint.nodeID, x - 45, y - 16);
        }


//        if (showHelplines) {
//            g2d.setColor(Color.RED);
//            g2d.setStroke(new BasicStroke(3));
//            for (Line helpline : currentFrame.helplines) {
//                g2d.drawLine((int) helpline.source.x + xOffset, (int) helpline.source.y + yOffset, (int) helpline.target.x + xOffset, (int) helpline.target.y + yOffset);
//            }
//        }
    }

    public void setShowNodeID(boolean onoff) {
        showNodeID = onoff;
        repaint();
    }

//    public void setShowHelplines(boolean onoff) {
//        showHelplines = onoff;
//        if (timeline != null) {
//            showTimeline();
//        }
//        System.out.println("showing helplines: " + showHelplines);
//    }
}


//// OLD
//
////edges
//g.setColor(Color.DARK_GRAY);
//for(Edge edge : graph.getEdges()){
//	int sourceX = (int) edge.getSource().getX() + xOffset;
//	int sourceY = (int) edge.getSource().getY() + yOffset;
//	int targetX = (int) edge.getTarget().getX() + xOffset;
//	int targetY = (int) edge.getTarget().getY() + yOffset;
//	g.drawLine(sourceX, sourceY, targetX, targetY);
//}
////nodes
//g.setColor(new Color(0, 0, 128));
//for(Node node : graph.getNodes()){
//	int x = (int) (node.getX() + xOffset - nodeSize / 2);
//	int y = (int) (node.getY() + yOffset - nodeSize / 2);
//	g.fillOval(x, y, nodeSize, nodeSize);
////	g.setColor(Color.BLACK);
////	g.drawString(node.getValue(), x, y + 10);
////	g.drawString(Integer.toString(node.getHorizontal()), x, y + 20);
////	g.drawString(Integer.toString(node.getVertical()), x, y + 30);
//}
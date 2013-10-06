package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.JPanel;
import controller.MouseControl;
import model.Edge;
import model.Graph;
import model.Model;
import model.Node;

public class GraphPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private Model m;
	private ControlPanel cp;
	
	private int xOffset = 450; 
	private int yOffset = 80;
	
	private final int nodeSize = 20;
	private final int verticalDist = 50;
	private final int nodeMinDist = 30;
	
	private Graph graphToShow;
	
	private Animator animate;
		
	private boolean animMode = true;
	private int animSteps = 20;
	private boolean inAnimation = false;
	private boolean inAnimSequence = false;
	private ArrayList<Graph> animSequence;
	private int animSeqSize;
	private int animPos;
	
	//for double buffering
	private Image dbImage;
	private Graphics dbg;
	
		
	public GraphPanel(Model m, ControlPanel cp){			
		this.m = m;
		this.cp = cp;
		m.setGraphVisuParams(nodeSize, verticalDist, nodeMinDist);	
			
		MouseControl mc = new MouseControl(this);
		addMouseListener(mc);
		
		animate = new Animator(this);
	}
	
	public void updateXoffset(){	
		xOffset = getWidth() / 2;
	}
	
	public void setAnimMode(boolean truefalse){
		animMode = truefalse;
	}
	
	public void handleMouseReleasedCoords(int mouseX, int mouseY, boolean leftButton){	
		if(!inAnimation)	
			if(graphToShow != null){		
				Node clickedNode = null;	
				for(Node node : graphToShow.getNodes()){
					double boundaryLEFT = node.getX() + xOffset - nodeSize/2;
					double boundaryRIGHT = boundaryLEFT + nodeSize;
					double boundaryUP = node.getY() + yOffset - nodeSize/2;
					double boundaryDOWN = boundaryUP + nodeSize;	
					if(mouseX > boundaryLEFT && mouseX < boundaryRIGHT && mouseY > boundaryUP  && mouseY < boundaryDOWN)
						clickedNode = node;		
				}	
				if(clickedNode != null && !(clickedNode.getIsRoot() && !leftButton)){ //can't delete rootnode
					m.handleNodeClicked(clickedNode, leftButton);
					if(animMode){
						cp.disableAll();
						animate(graphToShow, m.getGraph());	
					}
					else
						animateSequence(m.getAnimationBuffer());							
				}
			}
	}
	
	
	public void updateGraphToShow(Graph graph){
		graphToShow = graph;
		repaint();
	}

	public void animateSequence(ArrayList<Graph> animSequence){
		inAnimSequence = true;
		cp.disableAll();
		animSeqSize = animSequence.size();
		
		this.animSequence = animSequence;
		animPos = 0;
		nextStepInAnimSequence();
	}
	
	private void nextStepInAnimSequence(){
		animate(animSequence.get(animPos), animSequence.get(animPos + 1));
		System.out.println("  " + animSequence.get(animPos + 1).getAnimMetaInfo());
		animPos ++;
	}
	
	public void disableControlPanel(){
		cp.disableAll();
	}
	
	public void updateAnimation(){
		for(Node node : graphToShow.getNodes()){			
			node.setX(node.getX() + node.getDeltaXAnim());
			node.setY(node.getY() + node.getDeltaYAnim());		
		}	
	}
	
	public void animate(Graph fromGraph, Graph toGraph){					
		int i = 0;	
		ArrayList<Node> fromNodes = fromGraph.getNodes();
		ArrayList<Node> toNodes = toGraph.getNodes();	
		for(Node fromNode : fromNodes){
			Node toNode = toNodes.get(i);
			double deltaX = (toNode.getX() - fromNode.getX()) / animSteps;
			double deltaY = (toNode.getY() - fromNode.getY()) / animSteps;
			fromNode.setDeltaXAnim(deltaX);
			fromNode.setDeltaYAnim(deltaY);
			i ++;
		}
		updateGraphToShow(fromGraph);	
		inAnimation = true;
			
		new Thread(new Runnable(){public void run() {
			animate.triggerUpdating(animSteps);
		}}).start();
			
	}
	
	public void animationFinished(){
		inAnimation = false;
		graphToShow = m.getGraph();
		repaint();
		System.out.println("  did one animation step");
		
		if(inAnimSequence)
			if(animPos < animSeqSize - 1){
				nextStepInAnimSequence();
				inAnimation = true;
			}
			else{
				inAnimSequence = false;	
				System.out.println("  animation sequence finished");
				cp.enableAll();
			}
		else
			cp.enableAll();
		
		
	}
	

	//double buffer setup via youtu.be/4T3WJEH7zrc
	public void paint(Graphics g){
		if(graphToShow != null){
			dbImage = createImage(getWidth(), getHeight());
			dbg = dbImage.getGraphics();
			paintComponent(dbg);
			g.drawImage(dbImage, 0, 0, this);
		}
	}
	
	public void paintComponent(Graphics g){	
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
			
		if(!animMode){
			g.setColor(Color.LIGHT_GRAY);
			g.drawLine(xOffset, 0, xOffset, getHeight());
		}
			
		ArrayList<Node> nodes = graphToShow.getNodes();
		ArrayList<Edge> edges = graphToShow.getEdges();
			
		//edges
		g.setColor(Color.DARK_GRAY);
		for(int i = 0; i < edges.size(); i++){
			Node source = findNodeByID(nodes, edges.get(i).getSourceID());
			Node target = findNodeByID(nodes, edges.get(i).getTargetID());
			g.drawLine((int) source.getX() + xOffset, (int) source.getY() + yOffset, (int) target.getX() + xOffset, (int) target.getY() + yOffset);			
		}
				
		//nodes
		g.setColor(new Color(0, 0, 128));
		for(int i = 0; i < nodes.size(); i++){
			Node node = nodes.get(i);
			int x = (int) (node.getX() + xOffset - nodeSize / 2);
			int y = (int) (node.getY() + yOffset - nodeSize / 2);
//			
//			if(node.getIsRoot())
//				System.out.println("root y: " + y);
			
			g.fillOval(x, y, nodeSize, nodeSize);
//			g.setColor(Color.BLACK);
//			g.drawString(node.getValue(), x, y + 10);
//			g.drawString(Integer.toString(node.getHorizontal()), x, y + 20);
//			g.drawString(Integer.toString(node.getVertical()), x, y + 30);
		}
	}

	
	private Node findNodeByID(ArrayList<Node> nodes, String ID){
		for(int i = 0; i < nodes.size(); i++)
			if(nodes.get(i).getID().equals(ID))
				return nodes.get(i);	
		return null;
	}
	
}

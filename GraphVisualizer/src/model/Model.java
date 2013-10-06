package model;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
//import javax.swing.JOptionPane;
import view.ControlPanel;
import view.GraphPanel;


public class Model {
	private Graph graph;	
	private ArrayList<Graph> animationBuffer = new ArrayList<>();
		
	private int nodeSize;
	private int verticalDist;
	private int nodeMinDist;
	
	private GraphToolkit tools = new GraphToolkit(this);
	private GraphPanel gp;
	//private ControlPanel cp;
	
	private Exporter export = new Exporter();
	
		
	public Model(){}
	
	public void setGraphPanel(GraphPanel gp, ControlPanel cp){
		this.gp = gp;
		//this.cp = cp;
	}
	
	public void initNewGraph(){
		graph = new Graph();
		graph.addNode("rootnode");
		gp.updateGraphToShow(graph);
		System.out.println("set a new graph with only the rootnode");
	}
	
	public void initSampleGraph() {
		graph = (new SampleGraph()).getGraph();	
		Graph graphLayouted = tools.getLayout(tools.graphCopy(graph), nodeSize, verticalDist, nodeMinDist);
		gp.disableControlPanel();
		animate(graph, graphLayouted);	
		graph = graphLayouted;
		System.out.println("loaded a sample graph");
	}
	
	public void setTreeLayout(){
		tools.setBasicOrTree(true);	
		refreshGraphLayout();
		System.out.println("set to tree layout");
	}
	
	public void setBasicLayout(){
		tools.setBasicOrTree(false);
		refreshGraphLayout();
		System.out.println("set to basic layout");
	}
	
	public void refreshGraphLayout(){
		Graph newGraph = tools.getLayout(tools.graphCopy(graph), nodeSize, verticalDist, nodeMinDist);
		animate(graph, newGraph);
		graph = newGraph;	
	}
	
	public void setShortAnim(){
		gp.setAnimMode(true);
		System.out.println("set to short animation mode");
	}
	
//	public boolean allowStepByStepAnim(){
//		return tools.getMaxVertical(graph.getNodes()) > 1 && tools.getMaxHorizontal(graph.getNodes()) > 1;
//	}
	
	public void setStepByStepAnim(){
		gp.setAnimMode(false);
		System.out.println("set to step-by-step animation mode");
	}
	
	public void loadImportedGraph(Graph imported){
		graph = tools.getLayout(imported, nodeSize, verticalDist, nodeMinDist);
		gp.updateGraphToShow(graph);
		System.out.println("loaded an imported graph");
	}
	
	public void handleNodeClicked(Node clickedNode, boolean leftButton){				
		if(leftButton){	
			Node nodeAdded = new Node("", ""); //JOptionPane.showMessageDialog(null, "value of node?", "message",JOptionPane.PLAIN_MESSAGE);	
			System.out.println("added node " + nodeAdded.getValue());
			nodeAdded.setX(clickedNode.getX());
			nodeAdded.setY(clickedNode.getY());
			graph.addNode(nodeAdded);
			graph.addEdge(clickedNode.getID(), nodeAdded.getID());	
		}
		else{
			//Graph testDeletion = 
			graph = tools.deleteNode(graph, clickedNode);
//			if(tools.getMaxVertical(testDeletion.getNodes()) > 1 && tools.getMaxHorizontal(testDeletion.getNodes()) > 1 && cp.getShortAnimSelected()){
//				graph = testDeletion;
//				System.out.println("deleted node " + clickedNode.getValue() + " and everything attached to it");
//			}
//			else
//				JOptionPane.showMessageDialog(null, "deleting this node (+ everything attached) would leave rootnode with children as max. depth, "
//						+ "\nswitch to short animation mode to allow that", "deletion would make ancestry too small for step-by-step animation mode", JOptionPane.PLAIN_MESSAGE);	
		}		
		graph = tools.getLayout(tools.graphCopy(graph), nodeSize, verticalDist, nodeMinDist);
	}

	
	public void animate(Graph fromGraph, Graph toGraph){
		gp.animate(fromGraph, toGraph);	
	}
	
	public void addToAnimationBuffer(Graph interimGraph) {	
		boolean sameGraph = false;	
		ArrayList<Node> lastGraphNodes;	
		if(animationBuffer.size() > 0){
			sameGraph = true;
			lastGraphNodes = animationBuffer.get(animationBuffer.size() - 1).getNodes();
			int i = 0;
			for(Node node : interimGraph.getNodes()){
				if(node.getX() != lastGraphNodes.get(i).getX() || node.getY() != lastGraphNodes.get(i).getY())
					sameGraph = false;	
				i ++;
			}	
		}				
		if(!sameGraph)
			animationBuffer.add(interimGraph);		
	}
	
	public void clearAnimationBuffer(){
		animationBuffer.clear();
	}

	public ArrayList<Graph> getAnimationBuffer() {
		return animationBuffer;
	}
	
	public void setGraphVisuParams(int nodeSize, int verticalDist, int nodeMinDist){
		this.nodeSize = nodeSize;
		this.verticalDist = verticalDist;
		this.nodeMinDist = nodeMinDist;							
	}
	
	public Graph getGraph(){
		return graph;
	}

	
	public String exportGraph() throws FileNotFoundException{
		String filename = "GraphExport_" + new SimpleDateFormat("dd-MM-yyyy-HHmmss").format(new Date()) + ".graphml";	
		export.doExport(graph, filename);
		return filename;
	}

}

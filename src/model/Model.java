package model;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Model {
	
	private Graph graph;	
	private int horizOrderIndex = 0;
	private LayoutManager layoutManager;
	
	
	public Model(){}
	
	public void setParams(LayoutInterface layoutAlgorithm, int nodeSize, int nodeVertDist, int nodeMinHorizDist){
		layoutManager = new LayoutManager(layoutAlgorithm, nodeSize, nodeVertDist, nodeMinHorizDist);
	}
	
	public void changeNodeParams(int nodeSize, int nodeVertDist, int nodeMinHorizDist){
		layoutManager.changeNodeParams(nodeSize, nodeVertDist, nodeMinHorizDist);
		graph = layoutManager.performLayout(graph);
		System.out.println("new node paramameters applied");
	}
	
	public void setLayoutAlgorithm(LayoutInterface layoutAlgorithm){
		layoutManager.setLayoutAlgorithm(layoutAlgorithm);
		graph = layoutManager.performLayout(graph);
		System.out.println("layout change to layoutAlgorithm: " + layoutAlgorithm);
	}
	
	public void setOrderIndex(int index){
		horizOrderIndex = index;
		graph.expand(horizOrderIndex);
		graph = layoutManager.performLayout(graph);
		System.out.println("order index changed to: " + index);
	}
	
	public void initNewGraph(){
		graph = new Graph();
		graph.addNode(new Node("node_rootnode", ""));
		System.out.println("set a new graph with only the rootnode");
	}
	
	public void initSampleGraph() {
		graph = SampleGraph.getSampleGraph();
		graph.expand(horizOrderIndex);
		//System.out.println(graph.consoleShow());
		graph = layoutManager.performLayout(graph);
		System.out.println("loaded a sample graph");
	}
	
	public void loadImportedGraph(Graph imported){
		graph = imported;
		graph.expand(horizOrderIndex);
		graph = layoutManager.performLayout(imported);
		System.out.println("loaded an imported graph");
	}
	
	public void handleNodeClicked(String clickedNodeID, boolean leftButton){						
		if(leftButton)
			graph.addChildToThisParent(clickedNodeID);
		else
			graph.deleteNode(clickedNodeID);
		graph.expand(horizOrderIndex);
		graph = layoutManager.performLayout(graph);		
		//System.out.println(graph.consoleShow());
	}
	
	public void handleNodeClicked(String clickedNodeID, String closestNodeToRelease, boolean rightFromThis) {
		graph.addChildToThisParentNextToThisSibling(clickedNodeID, closestNodeToRelease, rightFromThis); 
		graph.expand(horizOrderIndex);
		graph = layoutManager.performLayout(graph);			
	}
	
	
	
	public Graph getGraph(){
		return graph;
	}
	
	public Timeline getTimeline(){	
		return graph.getTimeline();
	}
	
	public String exportGraph() throws FileNotFoundException{
		String filename = "GraphExport_" + new SimpleDateFormat("dd-MM-yyyy-HHmmss").format(new Date()) + ".graphml";	
		Exporter.doExport(graph, filename);
		return filename;
	}


}

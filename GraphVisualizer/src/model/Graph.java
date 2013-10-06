package model;

import java.util.ArrayList;

public class Graph{

	private ArrayList<Node> nodes;
	private ArrayList<Edge> edges;
	
	private String animMetaInfo;
	
	
	public Graph(){
		nodes = new ArrayList<>();
		edges = new ArrayList<>();
	}
	
	public Graph(ArrayList<Node> nodes, ArrayList<Edge> edges){
		this.nodes = nodes;
		this.edges = edges;
	}
	
	public void setAnimMetaInfo(String text){
		animMetaInfo = text;
	}
	
	public String getAnimMetaInfo(){
		return animMetaInfo;
	}
	
	
	public String addNode(String value) {
		Node newNode = new Node("", value);
		nodes.add(newNode);
		return newNode.getID();
	}
	
	public void addNode(Node node){
		nodes.add(node);
	}
	
	public void addEdge(String sourceID, String targetID){
		edges.add(new Edge("", sourceID, targetID));
	}
	
	public ArrayList<Node> getNodes(){
		return nodes;
	}
	
	public ArrayList<Edge> getEdges(){
		return edges;
	}	
	
	public String consoleShow(){
		String output = "";	
		for(Node node : nodes)
			output += node.showFull() + "\n";
//		for(Edge edge : edges)
//			output += edge.show() + "\n";	
		return output;
	}
	
}

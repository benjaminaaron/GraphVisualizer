package model;

public class Edge {
	
	private Node source;
	private Node target;
	
	public Edge(Node source, Node target){		
		this.source = source;
		this.target = target;
	}
	
	public Node getSource(){
		return source;
	}
	
	public Node getTarget(){
		return target;
	}
	
	public Line getLine(){
		return new Line(source.getPoint(), target.getPoint());
	}
	
	public String show() {
		return "Edge: [" + source.getID() + "] -> [" + target.getID() + "]";
	}
}

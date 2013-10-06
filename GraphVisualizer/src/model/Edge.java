package model;

public class Edge {
	
	private String ID;
	private String sourceID;
	private String targetID;	

	public Edge(String ID, String sourceID, String targetID){
		if(ID.equals(""))
			this.ID = "edge_" + this.hashCode();	
		else
			this.ID = ID;
		this.sourceID = sourceID;
		this.targetID = targetID;
	}	
	
	public String getID(){
		return ID;
	}
	
	public String getSourceID(){
		return sourceID;
	}
	
	public String getTargetID(){
		return targetID;
	}
	
	public String show() {
		return "[" + ID + "]: [" + sourceID + "] -> [" + targetID + "]";
	}
}

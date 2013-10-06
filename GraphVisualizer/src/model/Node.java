package model;

import java.util.ArrayList;


public class Node {

	private String value;
	private String ID;
	private String parentID;
	private ArrayList<String> children = new ArrayList<>();
	private boolean isLeaf;
	private boolean isRoot;
	private int vertical;	
	private int horizontal;
	private int totalChildren = 0;
	private boolean hasChildren = false;

	private double x;
	private double y;
	
	private double deltaXAnim = 0;
	private double deltaYAnim = 0;

	
	public Node(String ID, String value){
		if(ID.equals(""))		
			this.ID = "node_" + this.hashCode();	
		else
			this.ID = ID;
		
		if(value.equals(""))		
			this.value = this.ID;	
		else
			this.value = value;
	}

	
	public double getDeltaXAnim(){
		return deltaXAnim;
	}
	
	public void setDeltaXAnim(double deltaXAnim){
		this.deltaXAnim = deltaXAnim;
	}
	
	public double getDeltaYAnim(){
		return deltaYAnim;
	}
	
	public void setDeltaYAnim(double deltaYAnim){
		this.deltaYAnim = deltaYAnim;
	}
		
	public double getX(){
		return x;
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public double getY(){
		return y;
	}
	
	public void setY(double y){
		this.y = y;
	}	
	
	public String getValue(){
		return value;
	}
	
	public String getID(){
		return ID;
	}

	private String showPure() {
		return "[" + ID + "]: " + value;
	}
	
	private String showCoordinates(){
		return "x: " + x + " | y: " + y;// + " | radius: " + radius + " | angle: " + angle + " | color: " + color;
	}
	
	public String showFull(){
		return showPure() + " | " + showCoordinates() + " | root: " + isRoot + " | leaf: " + isLeaf + " | vertical: " + vertical + " | horizontal: " + horizontal + " | parentID: [" + parentID + "] | childrenIDs: " + listChildren() + " | totalChildren: " + totalChildren;
	}
	
	private String listChildren(){
		String buffer = "";
		for(int i = 0; i < children.size(); i++)
			buffer += "[" + children.get(i) + "], ";
		if (buffer.length() > 1) buffer = buffer.substring(0, buffer.length() - 2);
		return buffer;
	}
	

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}
	
	public String getParentID(){
		return parentID;
	}
	

	public void addChild(String childID){
		children.add(childID);
		hasChildren = true;
	}
	
	public void setChildren(ArrayList<String> childrenIDs){
		children = childrenIDs;
		hasChildren = true;
	}
	
	public ArrayList<String> getChildren(){
		return children;
	}
	
	public boolean getHasChildren(){
		return hasChildren;
	}
	
	
	public void addToTotalChildren(int i){
		totalChildren += i;
	}
	
	public int getTotalChildren(){
		return totalChildren;
	}
	
	public void setIsLeaf(boolean truefalse){
		isLeaf = truefalse;
	}
	
	public boolean getIsLeaf(){
		return isLeaf;
	}
	
	public void setIsRoot(boolean truefalse){
		isRoot = truefalse;
	}
	
	public boolean getIsRoot(){
		return isRoot;
	}
	
	public void setVertical(int vertical){
		this.vertical = vertical;
	}
	
	public int getVertical(){
		return vertical;
	}
	
	public void setHorizontal(int horizontal){
		this.horizontal = horizontal;
	}
	
	public int getHorizontal(){
		return horizontal;
	}
	
	
}

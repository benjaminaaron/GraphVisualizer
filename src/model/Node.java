package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Node {

	private String value = "";
	private String ID = "";
	private Node parent = null;
	private ArrayList<Node> children = new ArrayList<>();
	private int totalChildren = 0;
	private int vertical = -1;
	private int horizontal = -1;

	private double x = 0;
	private double y = 0;
		
	private double angle = 0;
	private double corridorStart, corridorEnd, corridorSpanning;
	
	public void setCorridor(double corridorStart, double corridorEnd){
		this.corridorStart = corridorStart;
		this.corridorEnd = corridorEnd;
		corridorSpanning = corridorEnd - corridorStart;
	}
	
	public double getCorridorStart(){
		return corridorStart;
	}
	
	public double getCorridorEnd(){
		return corridorEnd;
	}
	
	public double getCorridorSpanning(){
		return corridorSpanning;
	}
	
	public void placingForRadialPlain(int nodeVertDist) {
		
		double deltaAngle = corridorSpanning / children.size();
		double lastCorridorBorder = corridorStart;
		double nextCorridorBorder = lastCorridorBorder + deltaAngle;
				
		int i = 0;
		for(Node child : children){
			double newAngle = corridorStart + deltaAngle / 2 + deltaAngle * i;
			child.setAngle(nodeVertDist, newAngle);		
			child.setCorridor(lastCorridorBorder, nextCorridorBorder);
			
			System.out.println("just gave " + child.getID() + " the angle: " + Math.round(newAngle) + " and the corridor from: " + Math.round(lastCorridorBorder) +" to: " + Math.round(nextCorridorBorder));
			
			lastCorridorBorder = nextCorridorBorder;
			nextCorridorBorder += deltaAngle;				
			i ++;		
			child.placingForRadialPlain(nodeVertDist);
		}
	}
	
	
	public void setAngle(int nodeVertDist, double angle){
		this.angle = angle;		
		double rad = (angle / 180) * Math.PI; //conversion into radiants, could just as well do it in rad only, but for understanding its better in degrees :) and actually has sort of higher precision		
		setPos(vertical * nodeVertDist * Math.cos(rad), - vertical * nodeVertDist * Math.sin(rad));	
	}
	
	public double getAngle(){
		return angle;
	}

	
	public void resetNode(){
		//but not pos! in order to allow the short-animation to function nicely
		parent = null;
		children.clear();
		totalChildren = 0;
		vertical = -1;
		horizontal = -1;
	}
	
	public void setPos(double x, double y){
		this.x = x;
		this.y = y;
	}
	
//	public void putMyChildrenAtMyPos() {
//		for(Node child : children)
//			child.setPos(x, y);
//	}

	public Point getPoint(){
		return new Point(ID, x, y);
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}

	public void resetPos(){
		x = 0;
		y = 0;
	}
	
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

	public Node(Node node){ //minimal copy
		ID = node.getID();
		value = node.getValue();
	}
	
	public void doChildParentHandshake(Node target) {
		//target part
		target.setParent(this);
		//source part
		children.add(target);
	}
	
	
	public void swapChildren(int orderIndex){ // 1 = left, 2 = right, 3 = middle full, 4 = middle empty		
		int factor = 1;
		if(orderIndex == 1 || orderIndex == 3)
			factor = -1;
		
		class NodeComparator implements Comparator<Node> {	
			int factor;	NodeComparator(int factor){this.factor = factor;}
				@Override
			public int compare(Node node1, Node node2){
				return factor * node1.getTotalChildren() - factor * node2.getTotalChildren();
			}}
		Collections.sort(children, new NodeComparator(factor));

		/*			 
		* Example for five odd (easier) children:
		* Imagine we have raw children of [5][3][7][1][4] (the total children count as value here). 
		* Through the sorting above we are already getting either ascending [1][3][4][5][7] (for "middle empty") or descending [7][5][4][3][1] (for "middle full")
		* With <pos> we put the index on the element in the middle; integer 5/2 = 2 as index points indeed to the middle-element of the array that we created as clone of the children arraylist 
		* (In an array or arraylist the indices are like this [0][1][2][3][4], but size of this gives 5)
		* But what we want is a distribution where either the highest or the lowest are in the middle, so for this example:
		* "middle full" either [4][1][7][3][5] where the following sequence happened to the indices that allowed for that placement, starting from pos at 3: {-1 +2 -3 +4}
		* or also "middle full" would be [4][1][7][3][5] where {+1 -2 +3 -4} was applied for the indices bouncing around.
		* "middle empty" would be the exact same two indices-sequences, but the content they would place would be pulled from the ascending rather then the descending list that was sorted above
		* i employed a randomizer to choose whether or not the indices-sequence starts with +1 or -1
		* 
		* For even children sizes it works very similar - but you have another kind of freedom then the start of the indices-sequence,
		* which is choosing the middle-element, because the middle lies always between elements with even numbers
		* i employed a randomizer here as well that decides if the middle-element is placed left or right of the "real middle between elements",
		* however, once that decision is made, the start of the indices-sequence (+1 or -1) is clear, in order to finish the sequence correctly 
		*/				
		
		if(orderIndex == 3 || orderIndex == 4){	
			int size = children.size();
			Node[] array = new Node[size];
			
			int pos = size / 2; //correct for odd	
			factor = 1 - 2 * (int) Math.round(new Random().nextDouble()); //either 1 or -1
			if(size % 2 == 0){
				pos -= (int) Math.round(new Random().nextDouble()); //in case of even we either start before or after the "middle" (which lies between two elements with even numbers, not on one as with odd numbers)
				if(pos == size / 2)
					factor = -1;
				else
					factor = 1;
			}			
			array[pos] = children.get(0);
					
			for(int i = 1; i < size; i++){
				pos += factor * i;
				factor *= -1;			
				array[pos] = children.get(i);
			}			
		children.clear();
		Collections.addAll(children, array);		
		}
	}
	
	public String getValue(){
		return value;
	}
	
	public String getID(){
		return ID;
	}

	private String showPure() {
		return "[" + ID + "]: " + value ;
	}
	
	private String showCoordinates(){
		return "x: " + x + " | y: " + y;
	}
	
	public String showFull(){
		return showPure() + " | " + showCoordinates() + " | root: " + getIsLeaf() + " | leaf: " + getIsRoot() + " | vertical: " + vertical + " | horizontal: " + horizontal + " | parentID: [" + (parent == null ? "no parent" : parent.getID()) + "] | childrenIDs: " + listChildren() + " | totalChildren: " + totalChildren;
	}
	
	private String listChildren(){
		String buffer = "";
		for(Node child : children)
			buffer += "[" + child.getID() + "], ";
		if (buffer.length() > 1) buffer = buffer.substring(0, buffer.length() - 2);
		return buffer;
	}
	
	public void setParent(Node parent){
		this.parent = parent;	
	}
	
	public Node getParent(){
		return parent;
	}
	
	public ArrayList<Node> getChildren(){
		return children;
	}
	
	public void addToTotalChildren(int i){
		totalChildren += i;
	}
	
	public int getTotalChildren(){
		return totalChildren;
	}

	//simplified by Sanjo
	public boolean getIsLeaf(){
		return children.isEmpty();
	}
	
	//simplified by Sanjo
	public boolean getIsRoot(){
		return parent == null;
	}
	
	public void setPosInGraph(int vertical, int horizontal) {
		this.vertical = vertical;
		this.horizontal = horizontal;
	}
	
	public int getVertical(){
		return vertical;
	}
	
	public int getHorizontal(){
		return horizontal;
	}

	public boolean isParentOf(Node toCheck) {
		boolean temp = false;		
		for(Node child : children)
			if(child == toCheck)
				temp = true;
		return temp;
	}




	
}

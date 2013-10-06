package model;

import java.util.ArrayList;


public class GraphToolkit {
	
	private Model m;
	private boolean basicTreeToggle = true;
	
	public GraphToolkit(Model m){
		this.m = m;
	}	
	
	public void setBasicOrTree(boolean truefalse){
		basicTreeToggle = truefalse;
	}
	
	
	public Graph getLayout(Graph graph, int nodeSize, int verticalDist, int nodeMinDist){	
		m.clearAnimationBuffer();

		Graph expanded = expandStep(graph);	
		if(basicTreeToggle){
			Graph treeLayouted = treeLayout(expanded, nodeSize, verticalDist, nodeMinDist);		
			return treeLayouted;
		}
		else{
			Graph basicLayouted = basicLayout(expanded, nodeSize, verticalDist, nodeMinDist);
			return basicLayouted;		
		}	
	}

	
	// EXPAND STEP
	
	private Graph expandStep(Graph graph){
		ArrayList<Node> nodes = graph.getNodes();
		ArrayList<Edge> edges = graph.getEdges();
				
		int maxVertical = 0;
		
		//parent child handshake
		for(int i = 0; i < nodes.size(); i++){	
			Node child = nodes.get(i);
			Node parent = getParentNode(nodes, edges, child);
			if(parent == null){
				child.setParentID("none");
				child.setIsRoot(true);
			}
			else{
				child.setParentID(parent.getID());
				parent.addChild(child.getID());
			}
		}		
		
		//find leaves
		ArrayList<Node> leaves = new ArrayList<>();
		for(int i = 0; i < nodes.size(); i++){		
			if(nodes.get(i).getChildren().size() == 0){
				nodes.get(i).setIsLeaf(true);
				leaves.add(nodes.get(i));
			}
		}
			
		//vertical order
		for(int i = 0; i < leaves.size(); i++){		
			int count = 0;
			Node parent = getParentNode(nodes, edges, leaves.get(i));
			while (parent != null){
				count ++;
				parent = getParentNode(nodes, edges, parent);
			}			
			
			if(count > maxVertical) maxVertical = count;
			leaves.get(i).setVertical(count);		
			parent = getParentNode(nodes, edges, leaves.get(i));
			
			while (parent != null){
				count --;
				parent.setVertical(count);
				parent = getParentNode(nodes, edges, parent);
			}
		}		
		
		//horizontal order
		ArrayList<Node> currentHorizontal = new ArrayList<>();		
		Node rootnode = getRootNode(nodes);		
		rootnode.setHorizontal(1);
		currentHorizontal.add(rootnode);
		
		for(int i = 0; i <= maxVertical; i++){
			int horizontalCount = 0;			
			ArrayList<Node> nextHorizontal = new ArrayList<>();
			
			for(int j = 0; j < currentHorizontal.size(); j++){
				Node node = currentHorizontal.get(j);
			
				for(int k = 0; k < node.getChildren().size(); k++){
					horizontalCount ++;
					Node child = getNodeByID(nodes, node.getChildren().get(k));
					child.setHorizontal(horizontalCount);	
					nextHorizontal.add(child);
				}	
			}		
			currentHorizontal = nextHorizontal;		
		}		
		//totalchildren, maxVertical are prefilled with 0 totalChildren, now fill level1 nodes with their number of children as total children
		ArrayList<Node> maxLevelMinusOneNodes = getNodesAtLevel(nodes, maxVertical - 1);
		for(int i = 0; i < maxLevelMinusOneNodes.size(); i++)
			maxLevelMinusOneNodes.get(i).addToTotalChildren(maxLevelMinusOneNodes.get(i).getChildren().size());
	
		if(maxVertical > 1) 
			for(int i = maxVertical - 2; i >= 0; i--){
				ArrayList<Node> currentLevelNodes = getNodesAtLevel(nodes, i);
				for(int j = 0; j < currentLevelNodes.size(); j++){
					Node node1 = currentLevelNodes.get(j);
					for(int k = 0; k < node1.getChildren().size(); k++){
						Node node2 = getNodeByID(nodes, node1.getChildren().get(k));
						node1.addToTotalChildren(node2.getTotalChildren() + 1);
					}
				}
			}	
	return new Graph(nodes, edges);
	}
	
	
	// BASIC LAYOUT, OPTIONAL
	
	private Graph basicLayout(Graph graph, int nodeSize, int verticalDist, int nodeMinDist){
		ArrayList<Node> nodes = graph.getNodes();
		ArrayList<Edge> edges = graph.getEdges();		
	
		ArrayList<Node> horizontalOrdered = getHorizontalOrdered(nodes, 0);
		Node rootnode = horizontalOrdered.get(0);
		rootnode.setX(0);
		rootnode.setY(0);	
		
		int vertical = getMaxVertical(nodes);
		horizontalOrdered = getHorizontalOrdered(nodes, vertical);
		
		
		while(vertical > 0){
			int nodeCount = horizontalOrdered.size();	
			int y = vertical * verticalDist;
			int xWidth = nodeCount * nodeSize + (nodeCount - 1) * nodeMinDist - nodeSize;	
			double x = - xWidth/2;
			for(Node node : horizontalOrdered){
				node.setX(x);
				node.setY(y);		
				x += nodeSize + nodeMinDist;
			}
			addToAnimBuffer(nodes, edges, "");
			vertical --;
			horizontalOrdered = getHorizontalOrdered(nodes, vertical);	
		}	
		return new Graph(nodes, edges);
	}
	
	
	private void addToAnimBuffer(ArrayList<Node> nodes, ArrayList<Edge> edges, String metaInfo){
		Graph newAnimGraph = identicalGraphCopy(nodes, edges);
		newAnimGraph.setAnimMetaInfo(metaInfo);
		m.addToAnimationBuffer(newAnimGraph);
	}
	
	
	
	// TREE LAYOUT
	
	private Graph treeLayout(Graph graph, int nodeSize, int verticalDist, int nodeMinDist){
		ArrayList<Node> nodes = graph.getNodes();
		ArrayList<Edge> edges = graph.getEdges();	
		
		setAllYs(nodes, verticalDist);
		
		int vertical = getMaxVertical(nodes);
		ArrayList<Node> horizontal = getHorizontalOrdered(nodes, vertical);
		
		placeBottomLevelWithMinDist(horizontal, nodeSize, nodeMinDist);	
		addToAnimBuffer(nodes, edges, "placed the bottom level of nodes centered and respecting minimum distance");	
		
		ArrayList<Node> parents = new ArrayList<>();
		
		while(vertical > 0){	
			parents = collectParentsOfThisVerticalsChildren(nodes, vertical);

			placeFixedParentNodesCenteredAboveTheirChildren(nodes, parents, nodeSize);		
			addToAnimBuffer(nodes, edges, "placed the parents (fixed parents) of the node-level below centered above their respective children");
			
			ensureGapsBtwnFixedParentNodes(nodes, parents, nodeSize, nodeMinDist);
			addToAnimBuffer(nodes, edges, "ensured that those fixed parents just placed have enough space btwn to accomodate the leaves in the same level with minimum distance");
			
			placeLeavesAroundAndBtwnFixedNodes(nodes, vertical - 1, parents, nodeSize, nodeMinDist);
			addToAnimBuffer(nodes, edges, "placed the leaves between the fixed parents");
			
			vertical --;
		}
		moveAllToCenterRootnode(nodes);
		addToAnimBuffer(nodes, edges, "finally move everything so that the rootnode is centered");
		
		return new Graph(nodes, edges);
	}
	
	
	private void setAllYs(ArrayList<Node> nodes, int verticalDist){
		for(Node node : nodes)
			node.setY(node.getVertical() * verticalDist);
	}
	
	private void placeBottomLevelWithMinDist(ArrayList<Node> horizontal, int nodeSize, int nodeMinDist){
		int nodeCount = horizontal.size();	
		int xBlock = nodeCount * nodeSize + (nodeCount - 1) * nodeMinDist - nodeSize;	
		double x = - xBlock / 2;
		for(Node node : horizontal){
			node.setX(x);
			x += nodeSize + nodeMinDist;
		}
	}
	
	private ArrayList<Node> collectParentsOfThisVerticalsChildren(ArrayList<Node> nodes, int vertical){
		ArrayList<Node> horizontal = getHorizontalOrdered(nodes, vertical);
		ArrayList<Node> parents = new ArrayList<>();		
		for(Node node : horizontal)
			if(!nodeIsInList(parents, node.getParentID())){	
				Node newParent = getNodeByID(nodes, node.getParentID());
				parents.add(newParent);
			}
		return parents;
	}

	
	private void placeFixedParentNodesCenteredAboveTheirChildren(ArrayList<Node> nodes, ArrayList<Node> parents, int nodeSize){
		for(Node parent : parents){
			double firstChildX = getNodeByID(nodes, parent.getChildren().get(0)).getX();
			
			if(parent.getChildren().size() == 1)
				parent.setX(firstChildX); 
			else {
				double lastChildX = getNodeByID(nodes, parent.getChildren().get(parent.getChildren().size() - 1)).getX();		
				double childrenBlockBorderLEFT = firstChildX - nodeSize / 2;
				double childrenBlockBorderRIGHT = lastChildX + nodeSize / 2;
				double childrenBlockMIDDLE = childrenBlockBorderLEFT + (childrenBlockBorderRIGHT - childrenBlockBorderLEFT) / 2;
				parent.setX(childrenBlockMIDDLE);
			}
		}	
	}
	
	private void ensureGapsBtwnFixedParentNodes(ArrayList<Node> nodes,ArrayList<Node> parents, int nodeSize, int nodeMinDist){
		for(int i = 0; i < parents.size() - 1; i++){	
			Node checkLeft = parents.get(i);
			Node checkRight = parents.get(i + 1);
			
			double isGap = (checkRight.getX() - nodeSize / 2) - (checkLeft.getX() + nodeSize / 2);		
			int nodesBtwn = checkRight.getHorizontal() - checkLeft.getHorizontal() - 1;
			double requiredGap = nodesBtwn * nodeSize + (nodesBtwn + 1) * nodeMinDist;
			
			if(isGap < requiredGap){
				System.out.println("gap has to be increased"); //create console logging buffer. wird nicht alles angezeigt! sammeln und in Model anzeigen? oder über ControlPanel sysouten?
				double gapDiff = requiredGap - isGap;
				moving(nodes, checkLeft, - gapDiff / 2, nodeMinDist, nodeSize);
				moving(nodes, checkRight, gapDiff / 2, nodeMinDist, nodeSize);	
			}
		}
	}


	private void moving(ArrayList<Node> nodes, Node parent, double dist, int nodeMinDist, int nodeSize){			
		int horizontalPos = parent.getHorizontal(); //starts with 1, not with 0 as arraylists do!
		int verticalPos = parent.getVertical();
				
		ArrayList<Node> horizontalOrdered = getHorizontalOrdered(nodes, verticalPos);		
		ArrayList<Node> leftOrRightPortion = new ArrayList<>();
		
		String leftOrRight = "";
		
		System.out.println("> looking at node " + parent.getValue() + " with horizontal position: " + horizontalPos + " of " + horizontalOrdered.size());
				
		//fill the leftOrRightPortion with nodes from that vertical level to the left or right INCLUDING the move-causing node, only add those with children
		//corresponds with parents array in treeLayout, only pass that on?
		
		if(dist < 0){
			leftOrRight = "LEFT";
			for(int i = 0; i < horizontalPos; i ++)
				if(horizontalOrdered.get(i).getHasChildren())
					leftOrRightPortion.add(horizontalOrdered.get(i));
		}
		else{
			leftOrRight = "RIGHT";
			for(int i = horizontalPos - 1; i < horizontalOrdered.size(); i ++)
				if(horizontalOrdered.get(i).getHasChildren())
					leftOrRightPortion.add(horizontalOrdered.get(i));
		}	
		//now apply move to the identified nodes and all of their children and childrens children
		
		for(Node nodeToMove : leftOrRightPortion){
			nodeToMove.setX(nodeToMove.getX() + dist);			
			String context = (nodeToMove == parent) ? "(move-causing parent)" : "(neighbour of move-causing parent)";		
			System.out.println("moved " + nodeToMove.getValue() + " " + context + " to the " + leftOrRight + " with delta " + dist);		
			ArrayList<Node> allChildrens = getAllNodesAttachedToThisParent(nodes, nodeToMove);
			for(Node node : allChildrens){
				node.setX(node.getX() + dist);
				System.out.println("  moved " + node.getValue() + " to the " + leftOrRight + " with delta " + dist);
			}				
		}
	}
	
	
	private void placeLeavesAroundAndBtwnFixedNodes(ArrayList<Node> nodes, int vertical, ArrayList<Node> parents, int nodeSize, int nodeMinDist){
		ArrayList<Node> horizontal = getHorizontalOrdered(nodes, vertical);		

		placeLeavesLeftOfFirstFixedNode(horizontal, parents.get(0), nodeSize, nodeMinDist);
		
		//old was: placeLeavesBtwnFixedNodes(nodes, horizontal, parents, nodeSize, nodeMinDist);	
		for(int i = 0; i < parents.size() - 1; i++){
			Node previousFixedNode = parents.get(i);
			Node nextFixedNode = parents.get(i + 1);		
			ArrayList<Node> nodesBtwn = new ArrayList<>();			
			for(int k = previousFixedNode.getHorizontal(); k < nextFixedNode.getHorizontal() - 1; k++)
				nodesBtwn.add(horizontal.get(k));		
			if(nodesBtwn.size() > 0)
				placeLeavesBtwnTheseTwoFixedNodes(nodes, previousFixedNode, nextFixedNode, nodesBtwn, nodeSize, nodeMinDist);
		}	
		
		placeLeavesRightOfLastFixedNode(horizontal, parents.get(parents.size() - 1), nodeSize, nodeMinDist);
	}
	
	
	private void placeLeavesLeftOfFirstFixedNode(ArrayList<Node> horizontal, Node firstFixedNode, int nodeSize, int nodeMinDist){
		int leftestPlacedNodeIndex = firstFixedNode.getHorizontal() - 1;	
		if(leftestPlacedNodeIndex != 0)		
			for(int i = 0; i < leftestPlacedNodeIndex; i++)
				horizontal.get(i).setX(firstFixedNode.getX() - (nodeMinDist + nodeSize) * (leftestPlacedNodeIndex - i));
	}
	
	
	private void placeLeavesBtwnTheseTwoFixedNodes(ArrayList<Node> nodes, Node previousFixedNode, Node nextFixedNode, ArrayList<Node> nodesBtwn, int nodeSize, int nodeMinDist){
		int nodesRemaining = nodesBtwn.size();
		boolean onlySiblingsBtwn = false;
		
		ArrayList<Node> centered = new ArrayList<>();
		
		for(Node leaf : nodesBtwn){
			boolean leafParentAsPREVIOUSFixedNode = leaf.getParentID().equals(previousFixedNode.getParentID());
			boolean leafParentAsNEXTfixedNode = leaf.getParentID().equals(nextFixedNode.getParentID());						
			
			if(leafParentAsPREVIOUSFixedNode && leafParentAsNEXTfixedNode){
				centered.add(leaf);		//wenn das für den ersten zutrifft auch zwingend für alle folgenden nodesBtwn
				onlySiblingsBtwn = true;
			}
			else{	
				nodesRemaining --;		
				if(leafParentAsPREVIOUSFixedNode)
					leaf.setX(previousFixedNode.getX() + (nodeSize + nodeMinDist) * (nodesBtwn.size() - nodesRemaining));
				else 
					if(leafParentAsNEXTfixedNode)
						leaf.setX(nextFixedNode.getX() - (nodeSize + nodeMinDist) * (nodesRemaining + 1));
					else 
						centered.add(leaf);			
			}
		}
		
		double rightBorder, leftBorder, startLeft, gap, deltaPlacement;
			
		if(onlySiblingsBtwn){
			rightBorder = nextFixedNode.getX();
			leftBorder = previousFixedNode.getX();
			gap = rightBorder - leftBorder;
			deltaPlacement = gap / (centered.size() + 1);
			startLeft = leftBorder + deltaPlacement;	
		}
		else{
			Node parentOfNextFixedNode = getNodeByID(nodes, nextFixedNode.getParentID());
			rightBorder = getNodeByID(nodes, parentOfNextFixedNode.getChildren().get(0)).getX();
			Node parentOfPreviousFixedNode = getNodeByID(nodes, previousFixedNode.getParentID());	
			leftBorder = getNodeByID(nodes, parentOfPreviousFixedNode.getChildren().get(parentOfPreviousFixedNode.getChildren().size() - 1)).getX();		
						
			gap = rightBorder - leftBorder;		
			double middleOfGap = leftBorder + gap / 2;	
			double blockSizeOfCentered = centered.size() * nodeSize + (centered.size() - 1) * nodeMinDist;	
			startLeft = middleOfGap - blockSizeOfCentered / 2 + nodeSize / 2;	
			deltaPlacement = nodeMinDist + nodeSize;
		}
		int count = 0;
		for(Node centerLeaf : centered){
			double newX = startLeft + deltaPlacement * count;
			centerLeaf.setX(newX);
			count ++;
		}
	}
	
	
	private void placeLeavesRightOfLastFixedNode(ArrayList<Node> horizontal, Node lastFixedNode, int nodeSize, int nodeMinDist){
		int rightestPlacedNodeIndex = lastFixedNode.getHorizontal() - 1;
				
		if(rightestPlacedNodeIndex != horizontal.size() - 1){	
			int count = 1;
			for(int j = rightestPlacedNodeIndex + 1; j < horizontal.size(); j++){
				horizontal.get(j).setX(lastFixedNode.getX() + (nodeMinDist + nodeSize) * count);
				count ++;
			}
		}
	}
	
	
	private void moveAllToCenterRootnode(ArrayList<Node> nodes){
		double dist = getRootNode(nodes).getX();	
		if(dist != 0){
			for(Node node : nodes)
				node.setX(node.getX() - dist);
		}	
	}
	

	
	// HELP METHODS
	
	private boolean nodeIsInList(ArrayList<Node> existingNodes, String IDtoCheck){
		boolean temp = false;	
		for(Node node : existingNodes)
			if(node.getID().equals(IDtoCheck))
				temp = true;	
		return temp;
	}
	
	public ArrayList<Node> getAllNodesAttachedToThisParent(ArrayList<Node> nodes, Node ancestor){
		ArrayList<Node> temp = new ArrayList<>();
		for(Node toCheck : nodes)
			if(toCheck.getVertical() > ancestor.getVertical())
				if(traceLineage(nodes, ancestor, toCheck))
					temp.add(toCheck);		
		return temp;
	}
	
	private boolean traceLineage(ArrayList<Node> nodes, Node ancestor, Node toCheck){	
		boolean hasThisAncestor = false;		
		boolean rootReached = false;
		
		while(!hasThisAncestor && !rootReached){		
			toCheck = getNodeByID(nodes, toCheck.getParentID());		
			if(toCheck == ancestor) 
				hasThisAncestor = true;	
			rootReached = toCheck.getIsRoot();
		}			
		return hasThisAncestor;
	}

	public int getMaxVertical(ArrayList<Node> nodes){
		int maxVertical = 0;
		for(Node node : nodes)
			if(node.getVertical() > maxVertical)
				maxVertical = node.getVertical();	
		return maxVertical;
	}
	
	public int getMaxHorizontal(ArrayList<Node> nodes){
		int maxHorizontal = 0;
		for(int i = 0; i < getMaxVertical(nodes) + 1; i ++)
			if(getNodesAtLevel(nodes, i).size() > maxHorizontal)
				maxHorizontal = getNodesAtLevel(nodes, i).size();			
		return maxHorizontal;
	}

	private Node getParentNode(ArrayList<Node> nodes, ArrayList<Edge> edges, Node child){
		for(int i = 0; i < edges.size(); i++)
			if(edges.get(i).getTargetID().equals(child.getID()))
				return getNodeByID(nodes, edges.get(i).getSourceID());
		return null;
	}
	
	private Node getRootNode(ArrayList<Node> nodes) {
		for(Node node : nodes)
			if(node.getIsRoot())
				return node;
		return null;
	}

	private Node getNodeByID(ArrayList<Node> nodes, String ID){
		for(int i = 0; i < nodes.size(); i++)
			if(nodes.get(i).getID().equals(ID))
				return(nodes.get(i));
		return null;			
	}
	
	private ArrayList<Node> getNodesAtLevel(ArrayList<Node> nodes, int vertical){
		ArrayList<Node> temp = new ArrayList<>();
		for(int i = 0; i < nodes.size(); i++)
			if(nodes.get(i).getVertical() == vertical)
				temp.add(nodes.get(i));
		return temp;
	}
	
	private ArrayList<Node> getHorizontalOrdered(ArrayList<Node> nodes, int vertical){
		ArrayList<Node> unordered = getNodesAtLevel(nodes, vertical);
		if(unordered.size() == 0)
			return null;
		ArrayList<Node> ordered = new ArrayList<>();
		int order = 1;		
		boolean added = true;		
		while(added){		
			added = false;	
			for(Node node : unordered)
				if(node.getHorizontal() == order){
					ordered.add(node);
					added = true;
				}
			order ++;
		}	
		return ordered;
	}
	

	// CLEAN GRAPH COPY
	
	public Graph graphCopy(Graph oldGraph){	
		ArrayList<Node> oldNodes = oldGraph.getNodes();
		ArrayList<Node> freshNodes = new ArrayList<>();	
		
		ArrayList<String> IDtable = new ArrayList<>();
				
		for(int i = 0; i < oldNodes.size(); i++){
			Node oldNode = oldNodes.get(i);
			Node freshNode = new Node("", oldNode.getValue());
			freshNodes.add(freshNode);
			IDtable.add(oldNode.getID() + "," + freshNode.getID());
		}		
		ArrayList<Edge> freshEdges = new ArrayList<>();
		for(Edge edge : oldGraph.getEdges()){
			freshEdges.add(new Edge("", tableSearch(IDtable, edge.getSourceID()), tableSearch(IDtable, edge.getTargetID())));
		}		
		Graph freshGraph = new Graph(freshNodes, freshEdges);
		return freshGraph;
	}
	
	
	private String tableSearch(ArrayList<String> IDtable, String oldID){		
		for(int i = 0; i < IDtable.size(); i++)		
			if(IDtable.get(i).split(",")[0].equals(oldID))
				return IDtable.get(i).split(",")[1];	
		return null;
	}


	public Graph identicalGraphCopy(ArrayList<Node> nodes, ArrayList<Edge> edges){
		ArrayList<Node> nodesCopy = new ArrayList<>();
		for(Node node : nodes){
			Node nodeCopy = new Node(node.getID(), node.getValue());
			nodeCopy.setChildren(node.getChildren());
			nodeCopy.setHorizontal(node.getHorizontal());
			nodeCopy.setVertical(node.getVertical());
			nodeCopy.setIsLeaf(node.getIsLeaf());
			nodeCopy.setIsRoot(node.getIsRoot());
			nodeCopy.setX(node.getX());
			nodeCopy.setY(node.getY());
			nodeCopy.addToTotalChildren(node.getTotalChildren());
			nodeCopy.setParentID(node.getParentID());	
			nodesCopy.add(nodeCopy);
		}
		ArrayList<Edge> edgesCopy = new ArrayList<>();
		for(Edge edge : edges)
			edgesCopy.add(new Edge(edge.getID(), edge.getSourceID(), edge.getTargetID()));
		
		return new Graph(nodesCopy, edgesCopy);		
	}	
	
	public Graph deleteNode(Graph graph, Node thisPlusAllAttached){	
		ArrayList<Node> nodes = graph.getNodes();
		ArrayList<Edge> edges = graph.getEdges();
		
		ArrayList<Node> nodesToDelete = getAllNodesAttachedToThisParent(nodes, thisPlusAllAttached);
		nodesToDelete.add(thisPlusAllAttached);
		
		for(Node node : nodesToDelete){	
			ArrayList<Edge> bin = new ArrayList<>();	
			for(Edge edge : edges)
				if(edge.getSourceID().equals(node.getID()) || edge.getTargetID().equals(node.getID()))
					bin.add(edge);
			edges.removeAll(bin);
			nodes.remove(node);
		}	
		return new Graph(nodes, edges);
	}
	
	

}





//private ArrayList<Node> getChildren(ArrayList<Node> nodes, Node parent){
//ArrayList<Node> children = new ArrayList<>();
//for(String s : parent.getChildren())
//	children.add(getNodeByID(nodes, s));
//return children;		
//}



//private void placeLeavesBtwnFixedNodes(ArrayList<Node> nodes, ArrayList<Node> horizontal, ArrayList<Node> parents, int nodeSize, int nodeMinDist){
//for(int i = 0; i < parents.size() - 1; i++){
//	Node previousFixedNode = parents.get(i);
//	Node nextFixedNode = parents.get(i + 1);			
//	int nodesBtwn = nextFixedNode.getHorizontal() - previousFixedNode.getHorizontal() - 1;
//	int nodesRemaining = nodesBtwn;					
//	ArrayList<Node> centerLeaves = new ArrayList<>();
//	
//	System.out.println("prevFixedNode: " + previousFixedNode.getValue());
//	System.out.println("nextFixedNode: " + nextFixedNode.getValue());
//	System.out.println();
//				
//	for(int k = previousFixedNode.getHorizontal(); k < nextFixedNode.getHorizontal() - 1; k++){
//		nodesRemaining --;		
//		Node leaf = horizontal.get(k);	
//		boolean leafParentAsPREVIOUSFixedNode = leaf.getParentID().equals(previousFixedNode.getParentID());
//		boolean leafParentAsNEXTfixedNode = leaf.getParentID().equals(nextFixedNode.getParentID());			
//		
//		if(leafParentAsPREVIOUSFixedNode && leafParentAsNEXTfixedNode){
//			System.out.println("  true true is the case");
//			centerLeaves.add(leaf);
//		}
//
//		if(leafParentAsPREVIOUSFixedNode && !leafParentAsNEXTfixedNode){
//			System.out.println("  true false is the case");
//			leaf.setX(previousFixedNode.getX() + (nodeSize + nodeMinDist) * (nodesBtwn - nodesRemaining));
//		}
//		
//		if(!leafParentAsPREVIOUSFixedNode && leafParentAsNEXTfixedNode){
//			System.out.println("  false true is the case");
//			leaf.setX(nextFixedNode.getX() - (nodeSize + nodeMinDist) * (nodesRemaining + 1));
//		}
//		
//		if(!leafParentAsPREVIOUSFixedNode && !leafParentAsNEXTfixedNode){
//			System.out.println("  false false is the case");
//			centerLeaves.add(leaf);		
//		}
//	}		
//	if(centerLeaves.size() > 0){	
//		Node parentOfNextFixedNode = getNodeByID(nodes, nextFixedNode.getParentID());
//		double rightBorder = getNodeByID(nodes, parentOfNextFixedNode.getChildren().get(0)).getX() - nodeSize / 2;
//		Node parentOfPreviousFixedNode = getNodeByID(nodes, previousFixedNode.getParentID());	
//		double leftBorder = getNodeByID(nodes, parentOfPreviousFixedNode.getChildren().get(parentOfPreviousFixedNode.getChildren().size() - 1)).getX() + nodeSize / 2;		
//		double remainingGap = rightBorder - leftBorder;		
//		double middleOfGap = leftBorder + remainingGap / 2;					
//		double blockSizeOfCenterLeaves = centerLeaves.size() * nodeSize + (centerLeaves.size() - 1) * nodeMinDist;		
//		double leftBorderOfBlock = middleOfGap - blockSizeOfCenterLeaves / 2;		
//		int count = 0;
//		for(Node centerLeaf : centerLeaves){
//			double newX = leftBorderOfBlock + nodeSize / 2 + (nodeMinDist + nodeSize) * count;
//			centerLeaf.setX(newX);
//			System.out.println("  newX: " + newX + " for node: " + centerLeaf.getValue());
//			count ++;
//		}
//	}	
//}
//}



//TREE Version Sep 13


//private Graph treeLayout(Graph graph, int nodeSize, int verticalDist, int nodeMinDist){
//ArrayList<Node> nodes = graph.getNodes();
//ArrayList<Edge> edges = graph.getEdges();	
//
////inital setup with max. collions 
//
//int vertical = 0;
//ArrayList<Node> horizontalOrdered = getHorizontalOrdered(nodes, vertical);
//Node rootnode = horizontalOrdered.get(0); //or getRootNode(nodes);
//rootnode.setX(0);
//rootnode.setY(0);		
//while(horizontalOrdered != null){
//	int y = (vertical + 1) * verticalDist;		
//	for(Node parent : horizontalOrdered){
//		int childrenCount = parent.getChildren().size();		
//		int blockSize = childrenCount * nodeSize + (childrenCount - 1) * nodeMinDist - nodeSize;	
//		
//		int i = 0;
//		for(Node child : getChildren(nodes, parent)){
//			child.setX(parent.getX() - (blockSize / 2)  + (nodeSize + nodeMinDist) * i);
//			child.setY(y);					
//			i ++;
//		}	
//	}					
//	vertical ++;
//	horizontalOrdered = getHorizontalOrdered(nodes, vertical);	
//}
//
//
////collision correction
//vertical = 1;
//horizontalOrdered = getHorizontalOrdered(nodes, vertical);
//
//while(vertical < getMaxVertical(nodes)){
//
//	System.out.println();
//	System.out.println("LOOKING FOR COLLIONS IN VERTICAL LEVEL " + vertical);
//	
//	//detect collions-pairs in this vertical level
//	
//	for(int i = 0; i < horizontalOrdered.size() - 1; i++)
//		for(int j = i + 1; j < horizontalOrdered.size(); j++){
//		
//		Node parent1 = horizontalOrdered.get(i);
//		Node parent2 = horizontalOrdered.get(j);
//		
//		System.out.println();
//		System.out.println("checking parents: " + parent1.getValue() + " and " + parent2.getValue());
//		
//		if(parent1.getHasChildren() && parent2.getHasChildren()){
//			System.out.println("  have both children");
//			ArrayList<Node> children1 = getChildren(nodes, parent1);
//			ArrayList<Node> children2 = getChildren(nodes, parent2);
//			
//			double rightBorderOfParent1Children = children1.get(children1.size() - 1).getX() + nodeSize / 2 + nodeMinDist / 2;
//			double leftBorderOfParent2Children = children2.get(0).getX() - nodeSize / 2 - nodeMinDist / 2;
//			
//			double gap = leftBorderOfParent2Children - rightBorderOfParent1Children;
//			
//			if(gap < 0){
//				System.out.println("-> collision detected in children with an anti-gap of " + gap);
//				moving(nodes, parent1, gap / 2, nodeMinDist, nodeSize);
//				moving(nodes, parent2, - gap / 2, nodeMinDist, nodeSize);
//			}
//			else
//				System.out.println("  children don't collide");
//		}
//		else
//			System.out.println("  on or both has no children");
//		
//	}		
//	vertical ++;
//	horizontalOrdered = getHorizontalOrdered(nodes, vertical);
//}
//return new Graph(nodes, edges);
//}







//int vertical = 1;
//ArrayList<Node> horizontalOrdered = getHorizontalOrdered(nodes, vertical);
//
//while(horizontalOrdered != null){
//
//	boolean moreChildrenThanParents = horizontalOrdered.size() < getNodesAtLevel(nodes, vertical + 1).size(); //gleich ??
//	System.out.println(moreChildrenThanParents);
//	
//	for(Node parent : horizontalOrdered) 
//		if(parent.getChildren().size() > 0){
//			ArrayList<Node> children = getChildren(nodes, parent);
//			double childrenBlockLeft = children.get(0).getX();
//			double childrenBlockRight = children.get(children.size() - 1).getX();		
//			double childrenBlockMiddle = childrenBlockLeft + (childrenBlockRight - childrenBlockLeft) / 2;
//			
//			double parentX = parent.getX();
//			double difference = childrenBlockMiddle - parentX;
//			
//			if(difference != 0){
//				if(moreChildrenThanParents)
//					parent.setX(parentX + difference);
//				else
//					for(Node child : children){
//						child.setX(child.getX() - difference);
//					}
//			}
//	}
//
//	vertical ++;
//	horizontalOrdered = getHorizontalOrdered(nodes, vertical);		
//}
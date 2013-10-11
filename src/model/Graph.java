package model;

import java.util.ArrayList;


public class Graph {

    private ArrayList<Node> nodes = new ArrayList<>();
    private ArrayList<ArrayList<Node>> nodesLevels = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();
    private Node rootnode;
    private int maxVertical = 0;
    private ArrayList<Node> forAllAttached = new ArrayList<>();

    private Frame[] keyframesTwo = new Frame[2];
    private ArrayList<Frame> keyframesLevelwise = new ArrayList<>();
    private ArrayList<Frame> keyframesNodewise = new ArrayList<>();


    public Graph() {
    }

    public Graph(ArrayList<Node> nodes, ArrayList<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public void setFirstKeyframe() {
        addKeyframeLevelwiseAndNodewise();
    }

    public void setFirstKeyframeTwo() {
        keyframesTwo[0] = takeKeyframeNow();
    }

    public void nodeSetPos(Node node, double x, double y) {
        node.setPos(x, y);
        if (node != rootnode) //otherwise that feels like "lagging" when there is an anim-step for the rootnode-placement on 0,0 because it IS already on 0,0
        {
            keyframesNodewise.add(takeKeyframeNow());
        }
    }

    public void addHelplinesKeyframe(ArrayList<Line> helplines) {
        Frame helplineKeyframe = takeKeyframeNow();
        for (Line helpline : helplines) {
            helplineKeyframe.addHelpline(helpline);
        }
        keyframesNodewise.add(helplineKeyframe);
    }

    public void addKeyframeLevelwise() {
        keyframesLevelwise.add(takeKeyframeNow());
    }

    public void addKeyframeLevelwiseAndNodewise() {
        Frame now = takeKeyframeNow();
        keyframesLevelwise.add(now);
        keyframesNodewise.add(now);
    }

    private Frame takeKeyframeNow() {
        Frame keyframe = new Frame();
        for (Node node : nodes) {
            keyframe.addPoint(node.getPoint());
        }
        for (Edge edge : edges) {
            keyframe.addLine(edge.getLine());
        }
        return keyframe;
    }

    public Timeline getTimeline() {
        System.out.println("Graph class delivered snapshotsFine with size: " + keyframesNodewise.size());
        keyframesTwo[1] = takeKeyframeNow();
        addKeyframeLevelwise();
        return new Timeline(takeKeyframeNow(), keyframesTwo, keyframesLevelwise, keyframesNodewise);
    }

    public void clearTimeline() {
        keyframesLevelwise.clear();
        keyframesNodewise.clear();
    }

    public void resetNodesPos() {
        for (Node node : nodes) {
            node.resetPos();
        }
    }

    public Node findNodeByID(String nodeID) {
        for (Node node : nodes) {
            if (node.getID().equals(nodeID)) {
                return node;
            }
        }
        return null;
    }

    public void addChildToThisParent(String clickedNodeID) {
        Node clickedNode = findNodeByID(clickedNodeID);
        Node nodeAdded = new Node("", "");
        System.out.println("added node " + nodeAdded.getValue());
        nodeAdded.setPos(clickedNode.getX(), clickedNode.getY());
        nodes.add(nodeAdded);
        edges.add(new Edge(clickedNode, nodeAdded));
    }


    public void addChildToThisParentNextToThisSibling(String clickedNodeID, String closestNodeToReleaseID, boolean rightFromThis) {
        Node clickedNode = findNodeByID(clickedNodeID);
        Node closestNodeToRelease = findNodeByID(closestNodeToReleaseID);

        if (clickedNode.isParentOf(closestNodeToRelease)) {
            Node nodeAdded = new Node("", "");
            nodeAdded.setPos(clickedNode.getX(), clickedNode.getY());
            nodes.add(nodeAdded);


            int crucialEdgeIndex = 0;
            for (Edge edge : edges) {
                if (edge.getTarget() == closestNodeToRelease) {
                    crucialEdgeIndex = edges.indexOf(edge);
                }
            }

            if (rightFromThis) {
                crucialEdgeIndex += 1;
            }

            edges.add(crucialEdgeIndex, new Edge(clickedNode, nodeAdded));

            System.out.println("added node " + nodeAdded.getID() + " next to child " + closestNodeToRelease.getID() + " to the right side: " + rightFromThis);
        } else {
            addChildToThisParent(clickedNodeID);
        }
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void addEdge(Node source, Node target) {
        edges.add(new Edge(source, target));
    }

    public void resetNodes() {
        nodesLevels.clear();
        rootnode = null;
        maxVertical = 0;
        for (Node node : nodes) {
            node.resetNode();
        }
    }


    public void expand(int horizOrderIndex) {
        resetNodes();

        //parent child handshake
        for (Edge edge : edges) {
            edge.getSource().doChildParentHandshake(edge.getTarget());
        }

        //find rootnode
        for (Node node : nodes) {
            if (node.getIsRoot()) {
                rootnode = node;
            }
        }

        //horizontal and vertical order
        ArrayList<Node> level = new ArrayList<>();
        level.add(rootnode);
        ArrayList<Node> nextLevel = new ArrayList<>();
        int vertical = 0;
        int horizontal = 1;

        while (level.size() > 0) {
            for (Node node : level) {
                node.setPosInGraph(vertical, horizontal);
                horizontal++;
                for (Node child : node.getChildren()) {
                    nextLevel.add(child);
                }
            }
            nodesLevels.add(level);
            level = nextLevel;
            nextLevel = new ArrayList<>();
            horizontal = 1;
            vertical++;
        }
        maxVertical = nodesLevels.size() - 1;

        //total Children
        for (int i = maxVertical; i >= 0; i--) {
            for (Node node : nodesLevels.get(i)) {
                if (!node.getIsLeaf()) {
                    for (Node child : node.getChildren()) {
                        node.addToTotalChildren(child.getTotalChildren());
                    }
                    node.addToTotalChildren(node.getChildren().size());
                }
            }
        }

        // swapping
        if (horizOrderIndex != 0) {
            for (Node node : nodes) {
                if (!node.getIsLeaf()) {
                    node.swapChildren(horizOrderIndex);
                }
            }

            //rebuild nodesLevels
            nodesLevels.clear();

            level = new ArrayList<>();
            level.add(rootnode);
            nextLevel = new ArrayList<>();
            vertical = 0;
            horizontal = 1;

            while (level.size() > 0) {
                for (Node node : level) {
                    node.setPosInGraph(node.getVertical(), horizontal);
                    horizontal++;
                    for (Node child : node.getChildren()) {
                        nextLevel.add(child);
                    }
                }
                nodesLevels.add(level);
                level = nextLevel;
                nextLevel = new ArrayList<>();
                horizontal = 1;
                vertical++;
            }
        }
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public String consoleShow() {
        String output = "";
        for (Node node : nodes) {
            output += node.showFull() + "\n";
        }
        for (Edge edge : edges) {
            output += edge.show() + "\n";
        }
        return output;
    }

    public void deleteNode(String clickedNodeID) {
        Node candidate = findNodeByID(clickedNodeID);
        if (candidate != rootnode) {
            forAllAttached.clear();
            forAllAttached.add(candidate);
            getAllAttached(candidate.getChildren());
            for (Node node : forAllAttached) {
                ArrayList<Edge> bin = new ArrayList<>();
                for (Edge edge : edges) {
                    if (edge.getSource() == node || edge.getTarget() == node) {
                        bin.add(edge);
                    }
                }
                edges.removeAll(bin);
                nodes.remove(node);
            }
        } else {
            System.out.println("can't delete rootnode");
        }
    }

    //used by deleteNode & getAllNodesAttachedToThisNode
    private void getAllAttached(ArrayList<Node> children) {
        if (children.size() > 0) {
            for (Node child : children) {
                forAllAttached.add(child);
                getAllAttached(child.getChildren());
            }
        }
    }

    public ArrayList<Node> getNodesAtLevel(int vertical) {
        if (vertical >= 0) {
            return nodesLevels.get(vertical);
        } else {
            return null;
        }
    }

    public ArrayList<ArrayList<Node>> getNodesLevels() {
        return nodesLevels;
    }

    public int getMaxVertical() {
        return maxVertical;
    }

    public Node getRootnode() {
        return rootnode;
    }

    public ArrayList<Node> getAllNodesAttachedToThisNode(Node ancestor) {
        forAllAttached.clear();
        forAllAttached.add(ancestor);
        getAllAttached(ancestor.getChildren());
        return forAllAttached;
    }


}


//public Graph(Graph graph){ //plain copy, needs to be expanded again
//for(Node node : graph.getNodes())
//	nodes.add(new Node(node));
//for(Edge edge : graph.getEdges())
//	edges.add(new Edge(findNodeByID(edge.getSource().getID()), findNodeByID(edge.getTarget().getID())));
//}
//


//old expand
////vertical order
//for(Node leaf : leaves){
//	int count = 0;
//	Node parent = leaf.getParent();
//	while (parent != null){ //count up until rootnode reached (= parent is null)
//		count ++;
//		parent = parent.getParent();
//	}
//	if(count > maxVertical) maxVertical = count;		
//	
//	leaf.setVertical(count); // the leaf we started counting from is on vertical level <count>
//	parent = leaf.getParent();
//	
//	while (parent != null){ // count down from leaf upwards, rootnode will get zero //  && parent.getVertical() == -1, excluded from condition because then export() is not resetting correctly
//		count --;
//		parent.setVertical(count);
//		parent = parent.getParent();
//	}
//}
//	
////total Children
//for(int i = maxVertical; i >= 0; i--){
//	for(Node node : getNodesAtLevel(i))
//		if(!node.getIsLeaf()){
//			for(Node child : node.getChildren())
//				node.addToTotalChildren(child.getTotalChildren());					
//			node.addToTotalChildren(node.getChildren().size());
//		}
//}
//
////horizontal order
//
//if(horizOrderIndex == 0) // no order, just as they come in
//	for(int i = maxVertical; i >= 0; i--){
//		int count = 1;
//		for(Node node : getNodesAtLevel(i)){
//			node.setHorizontal(count);
//			count ++;			
//		}
//	}
//if(horizOrderIndex == 1 && nodes.size() > 1){ //if only rootnode this would crash
//	int[] horizLevelwiseCounter = new int[maxVertical];			
//
//	//mark leftest branch
//	Node current = rootnode;		
//	int vertCount = 0;
//	
//	ArrayList<Node> toCheckNext = new ArrayList<>();
//	
//	while(!current.getIsLeaf()){
//		
//		for(int i = 1; i < current.getChildren().size(); i++)
//			toCheckNext.add(current.getChildren().get(i));
//		
//		current = current.getChildren().get(0);
//		
//		current.setHorizontal(1);
//		horizLevelwiseCounter[vertCount] += 1;	
//		vertCount ++;
//	}	
//}
//

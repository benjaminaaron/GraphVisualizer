package model;

import model.Animation.Timeline;

import java.util.ArrayList;


public class Graph {

    private ArrayList<Node> nodes = new ArrayList<>();
    private ArrayList<ArrayList<Node>> nodesLevels = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();
    private Node rootnode;
    private int maxVertical = 0;
    private int maxHorizontal = 0;
    private ArrayList<Node> forCollectingAttached = new ArrayList<>();

    private Timeline timeline;

    public Graph() {}

    public void setTimeline(Timeline timeline){
        this.timeline = timeline;
    }

    public Timeline getTimeline(){
        return timeline;
    }

    public Graph(ArrayList<Node> nodes, ArrayList<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
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

    public String addChildToThisParent(String clickedNodeID) {
        Node clickedNode = findNodeByID(clickedNodeID);
        Node nodeAdded = new Node("", "");
        System.out.println("added node " + nodeAdded.getValue());
        nodeAdded.setPos(clickedNode.getX(), clickedNode.getY());
        nodes.add(nodeAdded);
        edges.add(new Edge(clickedNode, nodeAdded));

        return nodeAdded.getID();
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
            if(horizontal > maxHorizontal)
                maxHorizontal = horizontal;
            horizontal = 1;
            vertical++;
        }
        maxVertical = nodesLevels.size() - 1;
        maxHorizontal -= 1;

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
            forCollectingAttached.clear();
            forCollectingAttached.add(candidate);
            getAllAttached(candidate.getChildren());
            for (Node node : forCollectingAttached) {
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
                forCollectingAttached.add(child);
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

    public ArrayList<Node> getNodesAtSpecificHorizontalnumber(int horizontal){
        ArrayList<Node> collect = new ArrayList<>();
        for(Node node : nodes)
            if(node.getHorizontal() == horizontal)
                collect.add(node);
        return collect;
    }

    public int getMaxVertical() {
        return maxVertical;
    }

    public int getMaxHorizontal(){
        return maxHorizontal;
    }

    public Node getRootnode() {
        return rootnode;
    }

    public ArrayList<Node> getAllNodesAttachedToThisNode(Node ancestor) {
        forCollectingAttached.clear();
        forCollectingAttached.add(ancestor);
        getAllAttached(ancestor.getChildren());
        return forCollectingAttached;
    }

/*  // propably necessary later for Bottom-up ParentsStartAtChildrenPos animations...
    public ArrayList<Node> getAncestryOfThisNode(Node youngest){
        ArrayList<Node> temp = new ArrayList<>();
        temp.add(youngest);
        if(!youngest.getIsRoot()){
            Node nextHigher = youngest.getParent();
            while(nextHigher != null){
                System.out.println(nextHigher.getID());
                temp.add(nextHigher);
                nextHigher = nextHigher.getParent();
            }
        }
        return temp;
    }
*/

    public Point getGraphCenterPoint(){
        double xSum = 0;
        double ySum = 0;
        for(Node node : nodes){
            xSum += node.getX();
            ySum += node.getY();
        }
        return new Point("", xSum / nodes.size(), ySum / nodes.size());
    }
}
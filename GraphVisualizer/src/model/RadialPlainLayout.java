package model;

import java.util.ArrayList;

public class RadialPlainLayout implements LayoutInterface {
	
	ArrayList<Point> savingNodesPos;

	@Override
	public Graph performLayout(Graph graph, int nodeSize, int nodeMinHorizDist, int nodeVertDist) {				
		Node rootnode = graph.getRootnode();

		rootnode.setCorridor(0, 360);
		rootnode.placingForRadialPlain(nodeVertDist + nodeSize); //decentralized... sending an impulse into rootnode for it to pass it downwards. awesome strategy... apply elsewhere? everywhere??

		//faking the animation for levelwise and nodewise animation AFTER the actual calculation was done in the nodes themselves :) why? because otherwise Node would need to know Graph
		//in order to do the "snapshots" (=new Keyframe for levewise or nodewise) and that doesn't feel elegant at all
		savingNodesPos = new ArrayList<>();
		for(Node node : graph.getNodes()){
			savingNodesPos.add(new Point(node.getPoint()));
			node.setPos(0, 0);
		}
				
		for(ArrayList<Node> level : graph.getNodesLevels()){
			for(Node node : level){
				Point savingPointForThisNode = findPointByID(node.getID());
				graph.nodeSetPos(node, savingPointForThisNode.x, savingPointForThisNode.y);
				//node.putMyChildrenAtMyPos();
			}
			if(level.get(0) != graph.getRootnode()) //would add an unecessary keyframe for placing the rootnode at 0,0 which already is at 0,0
				graph.addKeyframeLevelwise();
		}

		return graph;
	}

	private Point findPointByID(String nodeID){
		for(Point point : savingNodesPos)
			if(point.nodeID.equals(nodeID))
				return point;
		return null;
	}

}




//private void setArcsRecursively(Node node){		
//for(Node child : node.getChildren()){
//	child.setArc(node.getArc() / node.getChildren().size());
//	setArcsRecursively(child);
//}
//}
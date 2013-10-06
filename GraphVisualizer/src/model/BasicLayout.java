package model;

import java.util.ArrayList;

public class BasicLayout implements LayoutInterface {
		
	@Override
	public Graph performLayout(Graph graph, int nodeSize, int nodeMinHorizDist, int nodeVertDist) {	
		
		int vertical = graph.getMaxVertical();
		ArrayList<Node> horizontal = graph.getNodesAtLevel(vertical);	
		
		while(vertical > 0){
			int nodeCount = horizontal.size();	
			int y = vertical * nodeVertDist;
			int xWidth = nodeCount * nodeSize + (nodeCount - 1) * nodeMinHorizDist - nodeSize;	
			double x = - xWidth / 2;
			
			for(Node node : horizontal){
				graph.nodeSetPos(node, x, y);
				x += nodeSize + nodeMinHorizDist;
			}
			vertical --;
			horizontal = graph.getNodesAtLevel(vertical);	
			graph.addKeyframeLevelwise(); //encapsulating better? problem with treeLayout pulling getNodesAtLevel twice per loop
		}		
		return graph;
	}
}

package model;

import java.util.Random;

public class RandomLayout implements LayoutInterface {

	@Override
	public Graph performLayout(Graph graph, int nodeSize, int nodeMinHorizDist, int nodeVertDist) {
		
		for(Node node : graph.getNodes())
			node.setPos(new Random().nextDouble() * 600 - 650 / 2, new Random().nextDouble() * 400 - 80 / 2); //650 and 80 are offset
			
		return graph;
	}

}

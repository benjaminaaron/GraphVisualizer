package model;

public class LayoutManager{
	
	private int layoutIndex;
	private int nodeSize;
	private int nodeVertDist;
	private int nodeMinHorizDist;

	public LayoutManager(int layoutIndex, int nodeSize, int nodeVertDist, int nodeMinHorizDist){
		this.layoutIndex = layoutIndex;
		this.nodeSize = nodeSize;
		this.nodeVertDist = nodeVertDist;
		this.nodeMinHorizDist = nodeMinHorizDist;
	}
	
	public void changeNodeParams(int nodeSize, int nodeVertDist, int nodeMinHorizDist){
		this.nodeSize = nodeSize;
		this.nodeVertDist = nodeVertDist;
		this.nodeMinHorizDist = nodeMinHorizDist;
	}
	
	public void setLayoutIndex(int index){
		layoutIndex = index;
	}
	
	public Graph performLayout(Graph graph) {	
		graph.clearTimeline();
		graph.setFirstKeyframeTwo();
		graph.resetNodesPos();
		graph.setFirstKeyframe();
		
		if(layoutIndex == 1)
			return new BasicLayout().performLayout(graph, nodeSize, nodeMinHorizDist, nodeVertDist);
		if(layoutIndex == 2)
			return new TreeLayout().performLayout(graph, nodeSize, nodeMinHorizDist, nodeVertDist);
//		if(layoutIndex == 3)
//			return new RadialLayout().performLayout(graph, nodeSize, nodeMinHorizDist, nodeVertDist);
		if(layoutIndex == 4)
			return new RandomLayout().performLayout(graph, nodeSize, nodeMinHorizDist, nodeVertDist);
		
		return null;
	}	
}

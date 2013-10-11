package model;

public class RadialPlainLayout implements LayoutInterface {

    @Override
    public Graph performLayout(Graph graph, int nodeSize, int nodeMinHorizDist, int nodeVertDist) {
        Node rootnode = graph.getRootnode();

        rootnode.setCorridor(0, 360);
        rootnode.placingForRadialPlain(nodeVertDist + nodeSize); //decentralized... sending an impulse into rootnode for it to pass it downwards. awesome strategy... apply elsewhere? everywhere??

        return new AnimationPostproduction().createNodeAndLevelwiseBasedOnLevels(graph);
    }
}


//private void setArcsRecursively(Node node){
//for(Node child : node.getChildren()){
//	child.setArc(node.getArc() / node.getChildren().size());
//	setArcsRecursively(child);
//}
//}
//
//private Point findPointByID(String nodeID){
//	for(Point point : savingNodesPos)
//		if(point.nodeID.equals(nodeID))
//			return point;
//	return null;
//}
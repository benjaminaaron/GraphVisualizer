package model.Animation;


import model.Node;

import java.util.ArrayList;

public class NodewiseRecursively extends AnimationProducerBase {

    @Override
    public Timeline produceTimeline() {
        reset();
        createZeroState();
        timeline.addKeyframe(new Frame(points, lines));
        recursion(graph.getRootnode().getChildren());
        return timeline;
    }

    private void recursion(ArrayList<Node> children){
        for(Node node : children){
            findPointByNodeID(node.getID()).setPos(node.getX(), node.getY());
            timeline.addKeyframe(new Frame(points, lines));
            if(!node.getIsLeaf())
                recursion(node.getChildren());
        }
    }
}

package model.Animation;


import model.Node;

import java.util.ArrayList;

public class NodewiseRecSiblingsCompleteFirst extends AnimationProducerBase {

    @Override
    public Timeline produceTimeline(int frameSteps) {
        reset();
        createZeroState();
        keyframes.add(new Frame(points, lines));
        recursion(graph.getRootnode().getChildren());
        return new Timeline(fillFramesBtwnKeyframes(frameSteps));
    }

    private void recursion(ArrayList<Node> children){
        for(Node node : children){
            findPointByNodeID(node.getID()).setPos(node.getX(), node.getY());
            keyframes.add(new Frame(points, lines));
        }
        for(Node node : children)
            if(!node.getIsLeaf())
                recursion(node.getChildren());
    }
}

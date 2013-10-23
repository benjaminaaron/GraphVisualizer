package model.Animation;


import model.Node;

public class HorizontalBottomUp extends AnimationProducerBase {

    @Override
    public Timeline produceTimeline(int frameSteps) {
        reset();
        createZeroState();
        keyframes.add(new Frame(points, lines));
        for(int i = graph.getMaxVertical(); i >= 0 ; i--){
            for(Node node : graph.getNodesAtLevel(i))
                findPointByNodeID(node.getID()).setPos(node.getX(), node.getY());
            keyframes.add(new Frame(points, lines));
        }
        return new Timeline(fillFramesBtwnKeyframes(frameSteps));
    }
}

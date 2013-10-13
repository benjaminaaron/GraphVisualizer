package model.Animation;


import model.Node;

public class HorizontalBottomUp extends AnimationProducerBase {

    @Override
    public Timeline produceTimeline() {
        reset();
        createZeroState();
        timeline.addKeyframe(new Frame(points, lines));
        for(int i = graph.getMaxVertical(); i >= 0 ; i--){
            for(Node node : graph.getNodesAtLevel(i))
                findPointByNodeID(node.getID()).setPos(node.getX(), node.getY());
            timeline.addKeyframe(new Frame(points, lines));
        }
        return timeline;
    }
}

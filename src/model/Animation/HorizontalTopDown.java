package model.Animation;


import model.Node;

import java.util.ArrayList;

public class HorizontalTopDown extends AnimationProducerBase {

    @Override
    public Timeline produceTimeline(int frameSteps) {
        reset();
        createZeroState();
        keyframes.add(new Frame(points, lines));
        for (ArrayList<Node> level : graph.getNodesLevels()) {
            for(Node node : level)
                findPointByNodeID(node.getID()).setPos(node.getX(), node.getY());
            if(!level.get(0).getIsRoot())
                keyframes.add(new Frame(points, lines));
        }
        return new Timeline(fillFramesBtwnKeyframes(frameSteps));
    }
}

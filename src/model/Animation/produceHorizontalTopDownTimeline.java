package model.Animation;


import model.Node;

import java.util.ArrayList;

public class ProduceHorizontalTopDownTimeline extends AnimationMainClass {

    @Override
    public Timeline produceTimeline() {
        reset();
        createZeroState();
        timeline.addKeyframe(new Frame(points, lines));
        for (ArrayList<Node> level : graph.getNodesLevels()) {
            for(Node node : level)
                findPointByNodeID(node.getID()).setPos(node.getX(), node.getY());
            if(!level.get(0).getIsRoot())
                timeline.addKeyframe(new Frame(points, lines));
        }
        return timeline;
    }
}

package model.Animation;


import model.Node;

import java.util.ArrayList;

public class ProduceHorizTDChildrenStartAtParentPosTimeline extends AnimationMainClass {

    @Override
    public Timeline produceTimeline() {
        reset();
        createZeroState();
        timeline.addKeyframe(new Frame(points, lines));
        for (ArrayList<Node> level : graph.getNodesLevels()) {
            for(Node node : level)
                for(Node everythingAttached : graph.getAllNodesAttachedToThisNode(node))
                    findPointByNodeID(everythingAttached.getID()).setPos(node.getX(), node.getY());
            if(!level.get(0).getIsRoot())
                timeline.addKeyframe(new Frame(points, lines));
        }
        return timeline;
    }
}

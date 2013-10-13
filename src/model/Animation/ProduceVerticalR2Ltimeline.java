package model.Animation;


import model.Node;

import java.util.ArrayList;

public class ProduceVerticalR2Ltimeline extends AnimationMainClass {

    @Override
    public Timeline produceTimeline() {
        reset();
        createZeroState();
        timeline.addKeyframe(new Frame(points, lines));
        int horizontalnumber = graph.getMaxHorizontal();
        ArrayList<Node> batch = graph.getNodesAtSpecificHorizontalnumber(horizontalnumber);

        System.out.println(batch.size());

        while(batch.size() > 0){
            for(Node node : batch)
                findPointByNodeID(node.getID()).setPos(node.getX(), node.getY());
            timeline.addKeyframe(new Frame(points, lines));
            horizontalnumber --;
            batch = graph.getNodesAtSpecificHorizontalnumber(horizontalnumber);
        }
        return timeline;
    }
}

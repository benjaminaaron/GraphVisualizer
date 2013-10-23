package model.Animation;


import model.Node;

import java.util.ArrayList;

public class VerticalLeftToRight extends AnimationProducerBase {

    @Override
    public Timeline produceTimeline(int frameSteps) {
        reset();
        createZeroState();
        keyframes.add(new Frame(points, lines));
        int horizontalnumber = 1;
        ArrayList<Node> batch = graph.getNodesAtSpecificHorizontalnumber(horizontalnumber);
        while(batch.size() > 0){
            for(Node node : batch)
                findPointByNodeID(node.getID()).setPos(node.getX(), node.getY());
            keyframes.add(new Frame(points, lines));
            horizontalnumber ++;
            batch = graph.getNodesAtSpecificHorizontalnumber(horizontalnumber);
        }
        return new Timeline(fillFramesBtwnKeyframes(frameSteps));
    }
}

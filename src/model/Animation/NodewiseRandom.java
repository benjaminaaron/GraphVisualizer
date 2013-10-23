package model.Animation;


import model.Node;
import model.Point;

public class NodewiseRandom extends AnimationProducerBase {

    @Override
    public Timeline produceTimeline(int frameSteps) {
        reset();
        createZeroState();
        keyframes.add(new Frame(points, lines));
        for(int i = 0; i < points.size(); i ++){
            Point randomPoint = randomPoint();
            Node correspondingNode = graph.findNodeByID(randomPoint.nodeID);
            randomPoint.setPos(correspondingNode.getX(), correspondingNode.getY());
            if(!randomPoint.nodeID.equals(graph.getRootnode().getID()))
                keyframes.add(new Frame(points, lines));
        }
        return new Timeline(fillFramesBtwnKeyframes(frameSteps));
    }
}

package model.Animation;


import model.Graph;

import java.util.ArrayList;

public interface AnimationProducer {

    public void setGraph(Graph graph);

    public void reset();

    public Timeline produceTimeline(int frameSteps);

    public ArrayList<Frame> fillFramesBtwnKeyframes(int steps);

}

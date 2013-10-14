package model.Animation;


import model.Graph;

public interface AnimationProducer {

    public void setGraph(Graph graph);

    public void reset();

    public Timeline produceTimeline();

}

package model.Animation;


import model.Graph;

public interface AnimationProducer {

    public void setGraph(Graph graph);

    public Timeline produceTimeline();

}

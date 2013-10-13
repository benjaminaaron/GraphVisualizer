package model.Animation;


import model.Graph;

public interface AnimationInterface {

    public void setGraph(Graph graph);

    public Timeline produceTimeline();

}

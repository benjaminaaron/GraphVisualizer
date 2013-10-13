package model.Animation;


public class SingleFinalFrame extends AnimationProducerBase {

    @Override
    public Timeline produceTimeline() {
        reset();
        createCurrentState();
        timeline.addKeyframe(new Frame(points, lines));
    return timeline;
    }

}

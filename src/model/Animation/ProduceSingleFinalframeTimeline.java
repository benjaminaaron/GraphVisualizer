package model.Animation;


public class ProduceSingleFinalframeTimeline extends AnimationMainClass {

    @Override
    public Timeline produceTimeline() {
        reset();
        createCurrentState();
        timeline.addKeyframe(new Frame(points, lines));
    return timeline;
    }

}

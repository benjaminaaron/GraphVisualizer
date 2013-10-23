package model.Animation;


public class SingleFinalFrame extends AnimationProducerBase {

    @Override
    public Timeline produceTimeline(int frameSteps) {
        reset();
        createCurrentState();
        return new Timeline(new Frame(points, lines)); //new frame isn't even added to keyframes of AnimationProducerBase - just thrown right into the Timeline-object
    }

}

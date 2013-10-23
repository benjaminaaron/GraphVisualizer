package model.Animation;


import model.Line;
import model.Point;

public class Short extends AnimationProducerBase {

    private Frame previousFrame = null;

    public void setPreviousFrame(Frame previousFrame){
        this.previousFrame = previousFrame;
    }

    @Override
    public Timeline produceTimeline(int frameSteps) {
        reset();

        createCurrentState();
        Frame secondFrame = new Frame(points, lines);

        if(previousFrame != null)
            keyframes.add(previousFrame);
        else{
            Frame emptyFrame = new Frame(secondFrame);
            for(Point point : emptyFrame.points)
                point.setPos(0, 0);
            for(Line line : emptyFrame.lines){
                line.source.setPos(0, 0);
                line.target.setPos(0, 0);
            }
            keyframes.add(emptyFrame);
        }
        keyframes.add(secondFrame);

        return new Timeline(fillFramesBtwnKeyframes(frameSteps));
    }
}

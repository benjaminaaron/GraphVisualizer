package model.Animation;


import model.Line;
import model.Point;

public class Short extends AnimationProducerBase {

    private Frame previousFrame = null;

    public void setPreviousFrame(Frame previousFrame){
        this.previousFrame = previousFrame;
    }

    @Override
    public Timeline produceTimeline() {
        reset();

        createCurrentState();
        Frame secondFrame = new Frame(points, lines);

        if(previousFrame != null)
            timeline.addKeyframe(previousFrame);
        else{
            Frame emptyFrame = new Frame(secondFrame);
            for(Point point : emptyFrame.points)
                point.setPos(0, 0);
            for(Line line : emptyFrame.lines){
                line.source.setPos(0, 0);
                line.target.setPos(0, 0);
            }
            timeline.addKeyframe(emptyFrame);
        }
        timeline.addKeyframe(secondFrame);

        return timeline;
    }
}

package model.Animation;


import model.Point;

public class RotateCircleTest extends AnimationProducerBase {
    @Override
    public Timeline produceTimeline() {
        reset();
        createCurrentState();
        timeline.addKeyframe(new Frame(points, lines));

        double angleDiff = 360 / points.size();
        double radius = 150;

        Point centerOfGraph = graph.getGraphCenterPoint();
        boolean isFirstFrame = true;

        for(double start = 0; start <= 360; start += 20){
            int i = 0;
            for(Point point : points){
                double radiusNow = radius - (start / 360) * 300;
                point.setPos(centerOfGraph.x + radiusNow * Math.cos(Math.toRadians(start + i * angleDiff)), centerOfGraph.y + radiusNow * Math.sin(Math.toRadians(start + i * angleDiff)));
                i++;
            }
            Frame newFrame = new Frame(points, lines);
            if(isFirstFrame){
                newFrame.timeFactor = 3;
                isFirstFrame = false;
            }
            else
                newFrame.timeFactor = 0.1 + (start / 360);
            timeline.addKeyframe(newFrame);
        }
        setAllPointsOnTheirNodePos();

        Frame lastFrame = new Frame(points, lines);
        lastFrame.timeFactor = 4;
        timeline.addKeyframe(lastFrame);
        return timeline;
    }
}

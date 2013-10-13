package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Timeline {

    private ArrayList<Frame> keyframes = new ArrayList<>();

    public Timeline() {}

    public void addKeyframe(Frame keyframe){
        keyframes.add(keyframe);
    }

    public ArrayList<Frame> getKeyframes(){
        return keyframes;
    }

    public ArrayList<Frame> placeStepsBtwnKeyframes(int steps) {
        if(keyframes.size() < 2){
            Frame emptyFrame = new Frame(keyframes.get(0));
            for(Point point : emptyFrame.points)
                point.setPos(0, 0);
            for(Line line : emptyFrame.lines){
                line.source.setPos(0, 0);
                line.target.setPos(0, 0);
            }
            keyframes.add(0, emptyFrame);
        }

        System.out.println("placeStepsBtwnKeyframes runs with keyframes of size: " + keyframes.size());

        ArrayList<Frame> extended = new ArrayList<>();

        for (int i = 0; i < keyframes.size() - 1; i++) {
            Frame fromKeyframe = new Frame(keyframes.get(i));
            extended.add(fromKeyframe);
            Frame toKeyframe = keyframes.get(i + 1);

//            if (toKeyframe.helplines.size() != 0) {
//                for (Line helpline : toKeyframe.helplines) {
//                    fromKeyframe.addHelpline(new Line(helpline));
//                }
//                toKeyframe.helplines.clear();
//            }

            //save deltas here in Timeline, not encapsulating in points/lines/frames because it's unnecessary ballast, not elegant to carry the no-longer-needed deltas along into the view-package

            ArrayList<Point> deltaPoints = new ArrayList<>();
            int j = 0;
            for (Point fromPoint : fromKeyframe.points) {
                Point toPoint = toKeyframe.points.get(j);
                double dx = (toPoint.x - fromPoint.x) / steps;
                double dy = (toPoint.y - fromPoint.y) / steps;
                deltaPoints.add(new Point("", dx, dy));
                j++;
            }
            ArrayList<Line> deltaLines = new ArrayList<>();
            j = 0;
            for (Line fromLine : fromKeyframe.lines) {
                Line toLine = toKeyframe.lines.get(j);
                double dxSource = (toLine.source.x - fromLine.source.x) / steps;
                double dySource = (toLine.source.y - fromLine.source.y) / steps;
                double dxTarget = (toLine.target.x - fromLine.target.x) / steps;
                double dyTarget = (toLine.target.y - fromLine.target.y) / steps;
                deltaLines.add(new Line(new Point("", dxSource, dySource), new Point("", dxTarget, dyTarget)));
                j++;
            }

            Frame nextKeyframe = new Frame(fromKeyframe);

            for (int k = 0; k < steps; k++) {
                j = 0;
                for (Point point : nextKeyframe.points) {
                    point.translate(deltaPoints.get(j));
                    j++;
                }
                j = 0;
                for (Line line : nextKeyframe.lines) {
                    line.translate(deltaLines.get(j));
                    j++;
                }
                extended.add(new Frame(nextKeyframe));
            }

        }
        extended.add(new Frame(keyframes.get(keyframes.size() - 1))); //the final one
        //singleFinalKeyframe = keyframes.get(keyframes.size() - 1);  // maybe?

        System.out.println("returning extended with size: " + extended.size());
        return extended;
    }

}



//    public void setupForShortAnim(){
//        if(keyframes.size() == 0){
//            Frame newStartFrame = new Frame();
//            for(Point point : newStartFrame.points)
//                newStartFrame.addPoint(new Point(point.nodeID, 0, 0));
//            for(Line line : newStartFrame.lines)
//                newStartFrame.addLine(new Line(new Point(line.getSource().nodeID, 0, 0), new Point(line.getTarget().nodeID, 0 , 0)));
//
//            keyframes.add(newStartFrame);
//        }
//    }

//old setupForShortAnim; now in Model and stored in Graph...
//        else{
//            Map<String, Point> lastFinalKeyframeHashmap = new HashMap<>();
//            for(Point point : lastFinalKeyframe.points)
//                lastFinalKeyframeHashmap.put(point.nodeID, point);
//
//            Frame finalKeyframeCOPY = new Frame(finalKeyframe);
//            for(Point point : finalKeyframeCOPY.points)
//                if(lastFinalKeyframeHashmap.containsKey(point.nodeID))
//                    point.setPos(lastFinalKeyframeHashmap.get(point.nodeID).x, lastFinalKeyframeHashmap.get(point.nodeID).y);
//
//            keyframes.add(finalKeyframeCOPY);
//        }
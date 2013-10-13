package model.Animation;

import model.Line;
import model.Point;

import java.util.ArrayList;

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
        System.out.println("placeStepsBtwnKeyframes runs with keyframes of size: " + keyframes.size());

        ArrayList<Frame> extended = new ArrayList<>();

        for (int i = 0; i < keyframes.size() - 1; i++) {
            Frame fromKeyframe = new Frame(keyframes.get(i));
            extended.add(fromKeyframe);
            Frame toKeyframe = keyframes.get(i + 1);
            double stepsFactor = toKeyframe.timeFactor;

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
                double dx = (toPoint.x - fromPoint.x) / Math.round(steps * stepsFactor);
                double dy = (toPoint.y - fromPoint.y) / Math.round(steps * stepsFactor);
                deltaPoints.add(new Point("", dx, dy));
                j++;
            }
            ArrayList<Line> deltaLines = new ArrayList<>();
            j = 0;
            for (Line fromLine : fromKeyframe.lines) {
                Line toLine = toKeyframe.lines.get(j);
                double dxSource = (toLine.source.x - fromLine.source.x) / Math.round(steps * stepsFactor);
                double dySource = (toLine.source.y - fromLine.source.y) / Math.round(steps * stepsFactor);
                double dxTarget = (toLine.target.x - fromLine.target.x) / Math.round(steps * stepsFactor);
                double dyTarget = (toLine.target.y - fromLine.target.y) / Math.round(steps * stepsFactor);
                deltaLines.add(new Line(new Point("", dxSource, dySource), new Point("", dxTarget, dyTarget)));
                j++;
            }

            Frame nextKeyframe = new Frame(fromKeyframe);

            for (int k = 0; k < Math.round(steps * stepsFactor); k++) {
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

        System.out.println("returning extended with size: " + extended.size());
        return extended;
    }
}
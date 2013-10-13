package model.Animation;

import model.Line;
import model.Point;

import java.util.ArrayList;

public class Frame {

    public double timeFactor = 1;

    public ArrayList<Point> points = new ArrayList<>();
    public ArrayList<Line> lines = new ArrayList<>();
//    public ArrayList<Line> helplines = new ArrayList<>();

    public Frame() {}

    public Frame(ArrayList<Point> pointsHandover, ArrayList<Line> linesHandover){
        for(Point point : pointsHandover)
            points.add(new Point(point));
        for(Line line : linesHandover)
            lines.add(new Line(line));
    }

    public Frame(Frame frame) { //copy-constructor
        for (Point point : frame.points) {
            points.add(new Point(point));
        }
        for (Line line : frame.lines) {
            lines.add(new Line(line));
        }
//        for (Line line : frame.helplines) {
//            helplines.add(new Line(line));
//        }
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public void addLine(Line line) {
        lines.add(line);
    }

    public Point findPointByNodeID(String nodeID){
        for(Point point : points)
            if(point.nodeID.equals(nodeID))
                return point;
        return null;
    }

    private void deleteLinesThatContainThisPoint(String nodeID){
        ArrayList<Line> bin = new ArrayList<>();
        for(Line line : lines)
             if(line.getSource().nodeID.equals(nodeID) || line.getTarget().nodeID.equals(nodeID))
                bin.add(line);
        lines.removeAll(bin);
    }

    public void deleteThisPointAndConnectedLines(String nodeID){
        points.remove(findPointByNodeID(nodeID));
        deleteLinesThatContainThisPoint(nodeID);
    }

    public boolean isSameFrameAs(Frame lastFinalKeyframe) {
        if(points.size() != lastFinalKeyframe.points.size())
            return false;
        int i = 0;
        for(Point point: points){
            Point otherPoint = lastFinalKeyframe.points.get(i);
            if(!(point.x == otherPoint.x && point.y == otherPoint.y))
                return false;
            i++;
        }
        return true;
    }


//    public void addHelpline(Line helpline) {
//        helplines.add(helpline);
//    }
}

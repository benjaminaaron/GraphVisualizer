package model.Animation;

import model.*;
import java.util.ArrayList;
import java.util.Random;

public abstract class AnimationProducerBase implements AnimationProducer {

    protected Graph graph;
    protected ArrayList<Point> points = new ArrayList<>();
    protected ArrayList<Integer> pointIndizeAlreadyChosen = new ArrayList<>();
    protected ArrayList<Line> lines = new ArrayList<>();
    protected ArrayList<Frame> keyframes = new ArrayList<>();

    public AnimationProducerBase(){};

    @Override
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    @Override
    public ArrayList<Frame> fillFramesBtwnKeyframes(int steps) {
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



    // HELP METHODS

    @Override
    public void reset(){
        keyframes.clear();
        points.clear();
        lines.clear();
        pointIndizeAlreadyChosen.clear();
    }

    protected Point randomPoint(){
        int index = new Random().nextInt(points.size());
        while(indexAlreadyTaken(index))
            index = new Random().nextInt(points.size());
        pointIndizeAlreadyChosen.add(index);
        return points.get(index);
    }

    private boolean indexAlreadyTaken(int index){
        for(int i : pointIndizeAlreadyChosen)
            if(index == i)
                return true;
        return false;
    }

    protected void createCurrentState(){
        createState(false);
    }

    protected void createZeroState(){
        createState(true);
    }

    protected void createState(boolean zeroState){
        for (Node node : graph.getNodes())
            if(zeroState)
                points.add(new Point(node.getID(), 0, 0));
            else
                points.add(new Point(node.getPoint()));
        for (Edge edge : graph.getEdges())
            lines.add(new Line(findPointByNodeID(edge.getSource().getID()), findPointByNodeID(edge.getTarget().getID())));
    }

    protected void setAllPointsOnTheirNodePos(){
        int i = 0;
        for (Node node : graph.getNodes()) {
            points.get(i).setPos(node.getX(), node.getY());
            i++;
        }
    }

    protected Point findPointByNodeID(String nodeID){
        for(Point point : points)
            if(point.nodeID.equals(nodeID))
                return point;
        return null;
    }
}

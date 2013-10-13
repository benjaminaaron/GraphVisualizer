package model.Animation;

import model.*;
import java.util.ArrayList;
import java.util.Random;

public abstract class AnimationProducerBase implements AnimationProducer {

    protected Graph graph;
    protected ArrayList<Point> points = new ArrayList<>();
    protected ArrayList<Integer> pointIndizeAlreadyChosen = new ArrayList<>();
    protected ArrayList<Line> lines = new ArrayList<>();
    protected Timeline timeline = new Timeline();

    public AnimationProducerBase(){};

    @Override
    public void setGraph(Graph graph) {
        this.graph = graph;
    }


    // HELP METHODS

    protected void reset(){
        timeline = new Timeline();
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

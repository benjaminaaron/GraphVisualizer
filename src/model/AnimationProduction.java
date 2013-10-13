package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AnimationProduction {
    private Graph graph;
    private ArrayList<Point> points = new ArrayList<>();
    private ArrayList<Integer> pointIndizeAlreadyChosen = new ArrayList<>(); //for random generator... better solution?
    private ArrayList<Line> lines = new ArrayList<>();
    private Timeline timeline = new Timeline();

    public AnimationProduction(Graph graph) {
        this.graph = graph;
    }

    public Timeline produceAnimation(int animIndex) {
        if(animIndex < 2)
            return createSingleKeyframeTimeline();
        if(animIndex == 2)
            return createHorizontalTopDownTimeline();
        if(animIndex == 3)
            return createHorizontalBottomUpTimeline();
        if(animIndex == 4)
            return createVerticalL2Rtimeline();
        if(animIndex == 5)
            return createVerticalR2Ltimeline();
        if(animIndex == 6)
            return createNodewiseTimeline();
        if(animIndex == 7)
            return createRandomNodewiseTimeline();
        return null;
    }


    public Timeline produceShortAnimation(Frame thePreviousOne) {
        timeline.addKeyframe(thePreviousOne);
        createCurrentState();
        timeline.addKeyframe(new Frame(points, lines));
        return timeline;
    }

    private Timeline createSingleKeyframeTimeline() {
        createCurrentState();
        timeline.addKeyframe(new Frame(points, lines));
        return timeline;
    }

    private Timeline createNodewiseTimeline(){
        createZeroState();
        timeline.addKeyframe(new Frame(points, lines));
        recursion(graph.getRootnode().getChildren());
        return timeline;
    }
    private void recursion(ArrayList<Node> children){
        for(Node node : children){
            findPointByNodeID(node.getID()).setPos(node.getX(), node.getY());
            timeline.addKeyframe(new Frame(points, lines));
            if(!node.getIsLeaf())
                recursion(node.getChildren());
        }
    }

    private Timeline createHorizontalTopDownTimeline() {
        createZeroState();
        timeline.addKeyframe(new Frame(points, lines));
        for (ArrayList<Node> level : graph.getNodesLevels()) {
            for(Node node : level)
                findPointByNodeID(node.getID()).setPos(node.getX(), node.getY());
            if(!level.get(0).getIsRoot())
                timeline.addKeyframe(new Frame(points, lines));
        }
        return timeline;
    }

    private Timeline createHorizontalBottomUpTimeline() {
        createZeroState();
        timeline.addKeyframe(new Frame(points, lines));
        for(int i = graph.getMaxVertical(); i >= 0 ; i--){
            for(Node node : graph.getNodesAtLevel(i))
                findPointByNodeID(node.getID()).setPos(node.getX(), node.getY());
            timeline.addKeyframe(new Frame(points, lines));
        }
        return timeline;
    }

    private Timeline createVerticalL2Rtimeline() {
        createZeroState();
        timeline.addKeyframe(new Frame(points, lines));
        int horizontalnumber = 1;
        ArrayList<Node> batch = graph.getNodesAtSpecificHorizontalnumber(horizontalnumber);
        while(batch.size() > 0){
            for(Node node : batch)
                findPointByNodeID(node.getID()).setPos(node.getX(), node.getY());
            timeline.addKeyframe(new Frame(points, lines));
            horizontalnumber ++;
            batch = graph.getNodesAtSpecificHorizontalnumber(horizontalnumber);
        }
        return timeline;
    }

    private Timeline createVerticalR2Ltimeline() {
        createZeroState();
        timeline.addKeyframe(new Frame(points, lines));
        int horizontalnumber = graph.getMaxHorizontal();
        ArrayList<Node> batch = graph.getNodesAtSpecificHorizontalnumber(horizontalnumber);

        System.out.println(batch.size());

        while(batch.size() > 0){
            for(Node node : batch)
                findPointByNodeID(node.getID()).setPos(node.getX(), node.getY());
            timeline.addKeyframe(new Frame(points, lines));
            horizontalnumber --;
            batch = graph.getNodesAtSpecificHorizontalnumber(horizontalnumber);
        }
        return timeline;
    }

    private Timeline createRandomNodewiseTimeline() {
        createZeroState();
        timeline.addKeyframe(new Frame(points, lines));
        for(int i = 0; i < points.size(); i ++){
            Point randomPoint = randomPoint();
            Node correspondingNode = graph.findNodeByID(randomPoint.nodeID);
            randomPoint.setPos(correspondingNode.getX(), correspondingNode.getY());
            if(!randomPoint.nodeID.equals(graph.getRootnode().getID()))
                timeline.addKeyframe(new Frame(points, lines));
        }
        return timeline;
    }


    // HELP METHODS

    private Point randomPoint(){
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

    private void createCurrentState(){
        createState(false);
    }

    private void createZeroState(){
        createState(true);
    }

    private void createState(boolean zeroState){
        for (Node node : graph.getNodes())
            if(zeroState)
                points.add(new Point(node.getID(), 0, 0));
            else
                points.add(new Point(node.getPoint()));
        for (Edge edge : graph.getEdges())
            lines.add(new Line(findPointByNodeID(edge.getSource().getID()), findPointByNodeID(edge.getTarget().getID())));
    }

    private Point findPointByNodeID(String nodeID){
        for(Point point : points)
            if(point.nodeID.equals(nodeID))
                return point;
        return null;
    }
}

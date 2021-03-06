package model;

import model.Animation.*;
import model.Animation.Short;
import model.Layout.*;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Model {

    private Graph graph;
    private int horizOrderIndex = 0;
    private int frameSteps = 15;
    private LayoutManager layoutManager;
    private AnimationProducer animationAlgorithm = new Short();

    private boolean inShortAnim = true;

    public Model() {}

    public void setParams(LayoutInterface layoutAlgorithm, int nodeSize, int nodeVertDist, int nodeMinHorizDist) {
        layoutManager = new LayoutManager(layoutAlgorithm, nodeSize, nodeVertDist, nodeMinHorizDist);
    }

    public void changeNodeParams(int nodeSize, int nodeVertDist, int nodeMinHorizDist) {
        layoutManager.changeNodeParams(nodeSize, nodeVertDist, nodeMinHorizDist);
        graph = layoutManager.performLayout(graph);
        animationAlgorithm.setGraph(graph);
        graph.setTimeline(animationAlgorithm.produceTimeline(frameSteps));
        System.out.println("new node paramameters applied");
    }

    public void setLayoutAlgorithm(LayoutInterface layoutAlgorithm) {
        layoutManager.setLayoutAlgorithm(layoutAlgorithm);
        graph = layoutManager.performLayout(graph);
        animationAlgorithm.setGraph(graph);
        graph.setTimeline(animationAlgorithm.produceTimeline(frameSteps));
        System.out.println("layout change to layoutAlgorithm: " + layoutAlgorithm);
    }

    public void setOrderIndex(int index) {
        horizOrderIndex = index;
        graph.expand(horizOrderIndex);
        graph = layoutManager.performLayout(graph);
        animationAlgorithm.setGraph(graph);
        graph.setTimeline(animationAlgorithm.produceTimeline(frameSteps));
        System.out.println("order index changed to: " + index);
    }

    public void setAnimationAlgorithm(AnimationProducer animationAlgorithm, int frameSteps){
        this.frameSteps = frameSteps;
        inShortAnim = animationAlgorithm.getClass().toString().equals("class model.Animation.Short");
        this.animationAlgorithm = animationAlgorithm;
        this.animationAlgorithm.setGraph(graph);
        graph.setTimeline(this.animationAlgorithm.produceTimeline(frameSteps));
    }

    public void initNewGraph() {
        graph = new Graph();
        graph.addNode(new Node("node_rootnode", ""));
        animationAlgorithm.setGraph(graph);
        graph.setTimeline(animationAlgorithm.produceTimeline(frameSteps));
        System.out.println("set a new graph with only the rootnode");
    }

    public void initSampleGraph(int index) {
        graph = SampleGraph.getSampleGraph(index);
        graph.expand(horizOrderIndex);
        //System.out.println(graph.consoleShow());
        graph = layoutManager.performLayout(graph);
        animationAlgorithm.setGraph(graph);
        graph.setTimeline(animationAlgorithm.produceTimeline(frameSteps));
        System.out.println("loaded a sample graph");
    }

    public void loadImportedGraph(Graph imported) {
        graph = imported;
        graph.expand(horizOrderIndex);
        graph = layoutManager.performLayout(imported);
        animationAlgorithm.setGraph(graph);
        graph.setTimeline(animationAlgorithm.produceTimeline(frameSteps));
        System.out.println("loaded an imported graph");
    }

    public void handleNodeClicked(String clickedNodeID, boolean leftButton) {

        Frame stateBeforeClick = null;

        if(inShortAnim){
            //save state for short-Anim
            SingleFinalFrame temp1 = new SingleFinalFrame();
            temp1.setGraph(graph);
            stateBeforeClick = temp1.produceTimeline(frameSteps).getFrames().get(0);
        }

        String addedNodeID = "";
        if (leftButton){
            addedNodeID = graph.addChildToThisParent(clickedNodeID);

            if(inShortAnim){
                Point parentPoint = stateBeforeClick.findPointByNodeID(clickedNodeID);
                Point newChild = new Point(addedNodeID, parentPoint.x, parentPoint.y);
                stateBeforeClick.addPoint(newChild);
                stateBeforeClick.addLine(new Line(parentPoint, newChild));
            }
        }
        else{
            if(inShortAnim)
                for(Node node : graph.getAllNodesAttachedToThisNode(graph.findNodeByID(clickedNodeID)))
                    stateBeforeClick.deleteThisPointAndConnectedLines(node.getID());

            graph.deleteNode(clickedNodeID);
        }

        graph.expand(horizOrderIndex);
        graph = layoutManager.performLayout(graph);

        if(inShortAnim){
            Short temp2 = new Short();
            temp2.setGraph(graph);
            temp2.setPreviousFrame(stateBeforeClick);
            graph.setTimeline(temp2.produceTimeline(frameSteps));
        }
        else{
            animationAlgorithm.setGraph(graph);
            graph.setTimeline(animationAlgorithm.produceTimeline(frameSteps));
        }
        //System.out.println(graph.consoleShow());
    }

    //TODO revisit this in the light of animationAlgorithms and mouseMotionListener to make sure the user gets the new child-node where he wants it to be
/*    public void handleNodeClicked(String clickedNodeID, String closestNodeToRelease, boolean rightFromThis) {
        graph.addChildToThisParentNextToThisSibling(clickedNodeID, closestNodeToRelease, rightFromThis);
        graph.expand(horizOrderIndex);
        graph = layoutManager.performLayout(graph);
        animationAlgorithm.setGraph(graph);
        graph.setTimeline(animationAlgorithm.produceTimeline());
    }
*/

    public Graph getGraph() {
        return graph;
    }

    public Timeline getTimeline() {
        return graph.getTimeline();
    }

    public String exportGraph() throws FileNotFoundException {
        String filename = "GraphExport_" + new SimpleDateFormat("dd-MM-yyyy-HHmmss").format(new Date()) + ".graphml";
        Exporter.doExport(graph, filename);
        return filename;
    }


}

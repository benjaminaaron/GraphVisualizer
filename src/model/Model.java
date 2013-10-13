package model;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Model {

    private Graph graph;
    private int horizOrderIndex = 0;
    private int animIndex = 1;
    private LayoutManager layoutManager;


    public Model() {
    }

    public void setParams(LayoutInterface layoutAlgorithm, int nodeSize, int nodeVertDist, int nodeMinHorizDist) {
        layoutManager = new LayoutManager(layoutAlgorithm, nodeSize, nodeVertDist, nodeMinHorizDist);
    }

    public void changeNodeParams(int nodeSize, int nodeVertDist, int nodeMinHorizDist) {
        layoutManager.changeNodeParams(nodeSize, nodeVertDist, nodeMinHorizDist);
        graph = layoutManager.performLayout(graph);
        graph.setTimeline(new AnimationProduction(graph).produceAnimation(animIndex));
        System.out.println("new node paramameters applied");
    }

    public void setLayoutAlgorithm(LayoutInterface layoutAlgorithm) {
        layoutManager.setLayoutAlgorithm(layoutAlgorithm);
        graph = layoutManager.performLayout(graph);
        graph.setTimeline(new AnimationProduction(graph).produceAnimation(animIndex));
        System.out.println("layout change to layoutAlgorithm: " + layoutAlgorithm);
    }

    public void setOrderIndex(int index) {
        horizOrderIndex = index;
        graph.expand(horizOrderIndex);
        graph = layoutManager.performLayout(graph);
        graph.setTimeline(new AnimationProduction(graph).produceAnimation(animIndex));
        System.out.println("order index changed to: " + index);
    }

    public void setAnimIndex(int index){
        animIndex = index;
        graph.setTimeline(new AnimationProduction(graph).produceAnimation(animIndex));
    }

    public void initNewGraph() {
        graph = new Graph();
        graph.addNode(new Node("node_rootnode", ""));
        graph.setTimeline(new AnimationProduction(graph).produceAnimation(animIndex));
        System.out.println("set a new graph with only the rootnode");
    }

    public void initSampleGraph(int index) {
        graph = SampleGraph.getSampleGraph(index);
        graph.expand(horizOrderIndex);
        //System.out.println(graph.consoleShow());
        graph = layoutManager.performLayout(graph);
        graph.setTimeline(new AnimationProduction(graph).produceAnimation(animIndex));
        System.out.println("loaded a sample graph");
    }

    public void loadImportedGraph(Graph imported) {
        graph = imported;
        graph.expand(horizOrderIndex);
        graph = layoutManager.performLayout(imported);
        graph.setTimeline(new AnimationProduction(graph).produceAnimation(animIndex));
        System.out.println("loaded an imported graph");
    }

    public void handleNodeClicked(String clickedNodeID, boolean leftButton) {

        //save state for short-Anim
        Frame stateBeforeClick = new AnimationProduction(graph).produceAnimation(0).getKeyframes().get(0);

        String addedNodeID = "";
        if (leftButton){
            addedNodeID = graph.addChildToThisParent(clickedNodeID);

            Point parentPoint = stateBeforeClick.findPointByNodeID(clickedNodeID);
            Point newChild = new Point(addedNodeID, parentPoint.x, parentPoint.y);
            stateBeforeClick.addPoint(newChild);
            stateBeforeClick.addLine(new Line(parentPoint, newChild));
        }
        else{
            for(Node node : graph.getAllNodesAttachedToThisNode(graph.findNodeByID(clickedNodeID)))
                stateBeforeClick.deleteThisPointAndConnectedLines(node.getID());
            graph.deleteNode(clickedNodeID);
        }

        graph.expand(horizOrderIndex);
        graph = layoutManager.performLayout(graph);

        if(animIndex == 1)
            graph.setTimeline(new AnimationProduction(graph).produceShortAnimation(stateBeforeClick));
        else
            graph.setTimeline(new AnimationProduction(graph).produceAnimation(animIndex));
        //System.out.println(graph.consoleShow());
    }

    public void handleNodeClicked(String clickedNodeID, String closestNodeToRelease, boolean rightFromThis) {
        graph.addChildToThisParentNextToThisSibling(clickedNodeID, closestNodeToRelease, rightFromThis);
        graph.expand(horizOrderIndex);
        graph = layoutManager.performLayout(graph);
        graph.setTimeline(new AnimationProduction(graph).produceAnimation(animIndex));
    }


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

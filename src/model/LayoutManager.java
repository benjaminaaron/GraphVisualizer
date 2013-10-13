package model;

public class LayoutManager {

    private LayoutInterface layoutAlgorithm; // by Sanjo, much nicer because the layouting-object is already passed on from the ControlPanel and it saves many "if index == xy" lines in the performLayout method 
    private int nodeSize;
    private int nodeVertDist;
    private int nodeMinHorizDist;

    public LayoutManager(LayoutInterface layoutAlgorithm, int nodeSize, int nodeVertDist, int nodeMinHorizDist) {
        this.layoutAlgorithm = layoutAlgorithm;
        this.nodeSize = nodeSize;
        this.nodeVertDist = nodeVertDist;
        this.nodeMinHorizDist = nodeMinHorizDist;
    }

    public void changeNodeParams(int nodeSize, int nodeVertDist, int nodeMinHorizDist) {
        this.nodeSize = nodeSize;
        this.nodeVertDist = nodeVertDist;
        this.nodeMinHorizDist = nodeMinHorizDist;
    }

    public void setLayoutAlgorithm(LayoutInterface layoutAlgorithm) {
        this.layoutAlgorithm = layoutAlgorithm;
    }

    public Graph performLayout(Graph graph) {
        graph.resetNodesPos();
        return (layoutAlgorithm == null) ? null : layoutAlgorithm.performLayout(graph, nodeSize, nodeMinHorizDist, nodeVertDist);
    }
}

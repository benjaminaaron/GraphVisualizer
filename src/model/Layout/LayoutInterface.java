package model.Layout;

import model.Graph;

public interface LayoutInterface {

    public Graph performLayout(Graph graph, int nodeSize, int nodeMinHorizDist, int nodeVertDist);

}

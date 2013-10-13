package model.Layout;

import model.Graph;
import model.Node;

public class RadialPlainLayout implements LayoutInterface {

    @Override
    public Graph performLayout(Graph graph, int nodeSize, int nodeMinHorizDist, int nodeVertDist) {
        Node rootnode = graph.getRootnode();

        rootnode.setCorridor(0, 360);
        rootnode.placingForRadialPlain(nodeVertDist + nodeSize);//decentralized... sending an impulse into rootnode for it to pass it downwards. awesome strategy... apply elsewhere? everywhere??

        return graph;
    }
}
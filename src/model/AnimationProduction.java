package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AnimationProduction {

	/* could have also named it AnimationFaker? but that doesn't sound so nice :D
     * basically it's about faking the animation (for levelwise and nodewise, but later also other modes like horizontal or recursive-logic)
	 * animation AFTER the actual position-calculation was done in the nodes themselves
	 */

    public AnimationProduction() {}


    public Graph createNodeAndLevelwiseBasedOnLevels(Graph graph) {
        Map<String, Point> savingNodesPosHashMap = new HashMap<>();

        //saving the positions
        for (Node node : graph.getNodes()) {
            savingNodesPosHashMap.put(node.getID(), new Point(node.getPoint()));
            node.setPos(0, 0);
        }
        //and putting them back into the nodes, but in a certain order and with placing keyframes at each step
        for (ArrayList<Node> level : graph.getNodesLevels()) {
            for (Node node : level) {
                Point savingPointForThisNode = savingNodesPosHashMap.get(node.getID());
                graph.nodeSetPos(node, savingPointForThisNode.x, savingPointForThisNode.y);
            }
            if (level.get(0) != graph.getRootnode()) //would add an unnecessary keyframe for placing the rootnode at 0,0 which already is at 0,0
            {
                graph.addKeyframeLevelwise();
            }
        }
        return graph;
    }

}

package model;

import java.util.ArrayList;

public class TreeLayout implements LayoutInterface {

    private Graph graph;
    private int nodeSize;
    private int nodeMinHorizDist;
    private int nodeVertDist;

    public TreeLayout() {}

    @Override
    public Graph performLayout(Graph graph, int nodeSize, int nodeMinHorizDist, int nodeVertDist) {
        this.graph = graph;
        this.nodeSize = nodeSize;
        this.nodeMinHorizDist = nodeMinHorizDist;
        this.nodeVertDist = nodeVertDist;

        int lowLevelVertical = graph.getMaxVertical();
        ArrayList<Node> currentLOWERLevelNodes = new ArrayList<>(); //in first round these are bottom level nodes
        currentLOWERLevelNodes = graph.getNodesAtLevel(lowLevelVertical);

        //placeBottomLevelWithMinDist
        double bottomY = lowLevelVertical * (nodeVertDist + nodeSize);
        double blockWidth = currentLOWERLevelNodes.size() * nodeSize + (currentLOWERLevelNodes.size() - 1) * nodeMinHorizDist - nodeSize;
        double x = -blockWidth / 2;
        for (Node node : currentLOWERLevelNodes) {
            node.setPos(x, bottomY);
            x += nodeSize + nodeMinHorizDist;
        }

        ArrayList<Node> currentUPPERLevelNodes = new ArrayList<>();
        currentUPPERLevelNodes = graph.getNodesAtLevel(lowLevelVertical - 1);
        double upperY = (lowLevelVertical - 1) * (nodeVertDist + nodeSize);


        while (currentUPPERLevelNodes != null) {

            //collect parents of this level

            ArrayList<Node> parents = new ArrayList<>();
            for (Node node : currentUPPERLevelNodes) //go through nodes one level higher, nodes that are not leaves there automatically have children in the level below
            {
                if (!node.getIsLeaf()) {
                    parents.add(node);
                }
            }

            //placing fixed parent nodes centered above their children

            for (Node parent : parents) {
                double firstChildX = parent.getChildren().get(0).getX();
                if (parent.getChildren().size() == 1) {
                    parent.setPos(firstChildX, upperY);
                } else {
                    double lastChildX = parent.getChildren().get(parent.getChildren().size() - 1).getX();
                    double childrenBlockBorderLEFT = firstChildX - nodeSize / 2;
                    double childrenBlockBorderRIGHT = lastChildX + nodeSize / 2;
                    double childrenBlockMIDDLE = childrenBlockBorderLEFT + (childrenBlockBorderRIGHT - childrenBlockBorderLEFT) / 2;
                    parent.setPos(childrenBlockMIDDLE, upperY);
                }
            }

            // ensureGapsBtwnFixedParentNodes

            ArrayList<Boolean> gapBiggerThanMin = new ArrayList<>();

            for (int i = 0; i < parents.size() - 1; i++) {
                Node checkLeft = parents.get(i);
                Node checkRight = parents.get(i + 1);
                double isGap = (checkRight.getX() - nodeSize / 2) - (checkLeft.getX() + nodeSize / 2);
                int nodesBtwn = checkRight.getHorizontal() - checkLeft.getHorizontal() - 1;
                double requiredGap = nodesBtwn * nodeSize + (nodesBtwn + 1) * nodeMinHorizDist;

/*                Line line = new Line(new Point(checkLeft.getPoint()), new Point(checkRight.getPoint()));
                ArrayList<Line> helplines = new ArrayList<>();
                helplines.add(line);
                graph.addHelplinesKeyframe(helplines);
*/


                System.out.println("isGap: " + isGap + "  requiredGap: " + requiredGap);

                if (isGap < requiredGap) { //gap has to be increased
                    double gapDiff = requiredGap - isGap;
                    moving(checkLeft, -gapDiff / 2);
                    moving(checkRight, gapDiff / 2);

/*                    Line line1 = new Line(new Point(checkLeft.getPoint()), new Point(checkRight.getPoint()));
                    Line arrowLeftUpwards = new Line(line1.source, new Point("", line1.source.x + 5, line1.source.y - 5));
                    Line arrowLeftDownwards = new Line(line1.source, new Point("", line1.source.x + 5, line1.source.y + 5));
                    Line arrowRightUpwards = new Line(line1.target, new Point("", line1.target.x - 5, line1.target.y - 5));
                    Line arrowRightDownwards = new Line(line1.target, new Point("", line1.target.x - 5, line1.target.y + 5));
                    helplines = new ArrayList<>();
                    helplines.add(line1);
                    helplines.add(arrowLeftUpwards);
                    helplines.add(arrowLeftDownwards);
                    helplines.add(arrowRightUpwards);
                    helplines.add(arrowRightDownwards);
                    graph.addHelplinesKeyframe(helplines);
 */
                    gapBiggerThanMin.add(false);
                } else {
                    gapBiggerThanMin.add(true);
                }
            }


            //placeLeavesAroundAndBtwnFixedNodes

            //placeLeavesLeftOfFirstFixedNode
            Node firstFixedNode = parents.get(0);

            int leftestPlacedNodeIndex = firstFixedNode.getHorizontal() - 1;
            if (leftestPlacedNodeIndex != 0) {
                for (int i = 0; i < leftestPlacedNodeIndex; i++) {
                    currentUPPERLevelNodes.get(i).setPos(firstFixedNode.getX() - (nodeMinHorizDist + nodeSize) * (leftestPlacedNodeIndex - i), upperY);
                }
            }

            //place those btwn fixed nodes

            for (int i = 0; i < parents.size() - 1; i++) {
                Node previousFixedNode = parents.get(i);
                Node nextFixedNode = parents.get(i + 1);
                ArrayList<Node> nodesBtwn = new ArrayList<>();
                for (int k = previousFixedNode.getHorizontal(); k < nextFixedNode.getHorizontal() - 1; k++) {
                    nodesBtwn.add(currentUPPERLevelNodes.get(k));
                }

                if (nodesBtwn.size() > 0 && gapBiggerThanMin.get(i)) { //only do the whole placement btwn fixed parents thing if there is more space then the minimum

                    int nodesRemaining = nodesBtwn.size();
                    boolean onlySiblingsBtwn = false;

                    ArrayList<Node> centered = new ArrayList<>();

                    for (Node leaf : nodesBtwn) {
                        boolean leafParentAsPREVIOUSFixedNode = leaf.getParent() == previousFixedNode.getParent();
                        boolean leafParentAsNEXTfixedNode = leaf.getParent() == nextFixedNode.getParent();

                        if (leafParentAsPREVIOUSFixedNode && leafParentAsNEXTfixedNode) {
                            centered.add(leaf);        //what's true for the first must also be true for the rest of nodesBtwn
                            onlySiblingsBtwn = true;
                        } else {
                            nodesRemaining--;
                            if (leafParentAsPREVIOUSFixedNode) {
                                leaf.setPos(previousFixedNode.getX() + (nodeSize + nodeMinHorizDist) * (nodesBtwn.size() - nodesRemaining), upperY);
                            } else if (leafParentAsNEXTfixedNode) {
                                leaf.setPos(nextFixedNode.getX() - (nodeSize + nodeMinHorizDist) * (nodesRemaining + 1), upperY);
                            } else {
                                centered.add(leaf);
                            }
                        }
                    }

                    double rightBorder, leftBorder, startLeft, gap, deltaPlacement;

                    if (onlySiblingsBtwn) {
                        rightBorder = nextFixedNode.getX();
                        leftBorder = previousFixedNode.getX();
                        gap = rightBorder - leftBorder;
                        deltaPlacement = gap / (centered.size() + 1);
                        startLeft = leftBorder + deltaPlacement;
                    } else {
                        Node parentOfNextFixedNode = nextFixedNode.getParent();
                        rightBorder = parentOfNextFixedNode.getChildren().get(0).getX();
                        Node parentOfPreviousFixedNode = previousFixedNode.getParent();
                        leftBorder = parentOfPreviousFixedNode.getChildren().get(parentOfPreviousFixedNode.getChildren().size() - 1).getX();

                        gap = rightBorder - leftBorder;
                        double middleOfGap = leftBorder + gap / 2;
                        double blockSizeOfCentered = centered.size() * nodeSize + (centered.size() - 1) * nodeMinHorizDist;
                        startLeft = middleOfGap - blockSizeOfCentered / 2 + nodeSize / 2;
                        deltaPlacement = nodeMinHorizDist + nodeSize;
                    }
                    int count = 0;
                    for (Node centerLeaf : centered) {
                        double newX = startLeft + deltaPlacement * count;
                        centerLeaf.setPos(newX, upperY);
                        count++;
                    }
                } else { //just do minimal placement then
                    System.out.println("doing minimal placement");
                    int count = 1;
                    for (Node node : nodesBtwn) {
                        double newX = previousFixedNode.getX() + count * (nodeMinHorizDist + nodeSize);
                        node.setPos(newX, upperY);
                        count++;
                    }
                }
            }

            //placeLeavesRightOfLastFixedNode
            Node lastFixedNode = parents.get(parents.size() - 1);
            int rightestPlacedNodeIndex = lastFixedNode.getHorizontal() - 1;

            if (rightestPlacedNodeIndex != currentUPPERLevelNodes.size() - 1) {
                int count = 1;
                for (int j = rightestPlacedNodeIndex + 1; j < currentUPPERLevelNodes.size(); j++) {
                    currentUPPERLevelNodes.get(j).setPos(lastFixedNode.getX() + (nodeMinHorizDist + nodeSize) * count, upperY);
                    count++;
                }
            }

            lowLevelVertical--;
            currentLOWERLevelNodes = graph.getNodesAtLevel(lowLevelVertical);
            currentUPPERLevelNodes = graph.getNodesAtLevel(lowLevelVertical - 1);
            upperY = (lowLevelVertical - 1) * (nodeVertDist + nodeSize);
        }

        //moveAllToCenterRootnode
        double dist = graph.getRootnode().getX();
        if (dist != 0) {
            for (Node node : graph.getNodes()) //exclude this from keyframeNodewise?
                node.setPos(node.getX() - dist, node.getY());
        }

        return graph;
    }


    private void moving(Node moveCause, double dist) {
        int horizontalPos = moveCause.getHorizontal(); //starts with 1, not with 0 as arraylists do

        ArrayList<Node> horizontal = graph.getNodesAtLevel(moveCause.getVertical());
        ArrayList<Node> leftOrRightPortion = new ArrayList<>();

        String leftOrRight = "";
        System.out.println("> looking at node " + moveCause.getValue() + " <" + moveCause.getID() + "> " + " with horizontal position: " + horizontalPos + " of " + horizontal.size());

        //fill the leftOrRightPortion with nodes from that vertical level to the left or right INCLUDING the move-causing node, only add those with children
        //corresponds with parents array in treeLayout, only pass that on?

        if (dist < 0) {
            leftOrRight = "LEFT";
            for (int i = 0; i < horizontalPos; i++)
            //if(!horizontal.get(i).getIsLeaf())
            {
                leftOrRightPortion.add(horizontal.get(i));
            }
        } else {
            leftOrRight = "RIGHT";
            for (int i = horizontalPos - 1; i < horizontal.size(); i++)
            //if(!horizontal.get(i).getIsLeaf())
            {
                leftOrRightPortion.add(horizontal.get(i));
            }
        }
        //now apply move to the identified nodes and all of their children and childrens children

        for (Node nodeToMove : leftOrRightPortion) {
            //nodeToMove.setPos(nodeToMove.getX() + dist, nodeToMove.getY()); //is included in the method getAllNodesAttachedToThisNode
            String context = (nodeToMove == moveCause) ? "(move-causing parent)" : "(neighbour of move-causing parent)";
            System.out.println("moved " + nodeToMove.getValue() + " <" + nodeToMove.getID() + "> " + context + " to the " + leftOrRight + " with delta " + dist);

            for (Node node : graph.getAllNodesAttachedToThisNode(nodeToMove)) {
                node.setPos(node.getX() + dist, node.getY());
                System.out.println("  moved " + node.getValue() + " <" + node.getID() + "> " + " to the " + leftOrRight + " with delta " + dist);
            }
        }
    }


}

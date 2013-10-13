package model.Layout;

import model.Graph;

public class RadialSmartLayout implements LayoutInterface {

    @Override
    public Graph performLayout(Graph graph, int nodeSize, int nodeMinHorizDist, int nodeVertDist) {

        //TODO... just how, how, how to best go aboutit... that's the question :D

        return graph;
    }

}



//old RadialSmartLayout attempts from within the node class

/*    public void smartExpand(int nodeSize, int nodeMinHorizDist, int nodeVertDist){
        if(children.size() > 1){
            //check if my children are too narrow together

            double minArcForMyChildren = (children.size() - 1) * nodeMinHorizDist + children.size() * nodeSize;
            double currentArcAvailableForMyChildren = ((2 * (vertical + 1) * nodeVertDist * Math.PI) / 360)  * corridorSpanning;

            double diff = currentArcAvailableForMyChildren - minArcForMyChildren;

            boolean tooNarrow = diff < 0;

            diff = Math.abs(diff);

            System.out.println(">> parent " + ID + " has " + (tooNarrow ? "NOT " : "") + "enough space for its children, min is: " + (nodeMinHorizDist + nodeSize));
            System.out.println("diff is: " + diff);

            if(!getIsRoot() && tooNarrow){
                System.out.println("the arc is " + String.format("%.2f", diff) + " too small for minimal placement. investigating how much space there is to expand into...");
                double maxSpaceClockwise = getMaxSpaceToMySiblingsChildren(nodeSize, nodeMinHorizDist, nodeVertDist, true);
                double maxSpaceCounterclockwise = getMaxSpaceToMySiblingsChildren(nodeSize, nodeMinHorizDist, nodeVertDist, false);

                double together = maxSpaceClockwise + maxSpaceCounterclockwise;
                System.out.println("clockwise there is an arc of " + String.format("%.2f", maxSpaceClockwise) + " available, counterclockwise it is: " + String.format("%.2f", maxSpaceCounterclockwise) + ", together that is: " + String.format("%.2f", together));


                double smaller = maxSpaceClockwise < maxSpaceCounterclockwise ? maxSpaceClockwise : maxSpaceCounterclockwise;

                boolean canStaySymmetricalAfterExpanding = (2 * smaller) > diff;
                if(canStaySymmetricalAfterExpanding)
                    System.out.println("the smaller expansion-allowance-value is enough when applied to both sides to restore at least minimal placement");
                else
                    System.out.println("minimal placement requires more space then twice the smaller expansion-allowance-value. thus it'll become assymetrical and possibly more narrow than the minimal distance");

            }
        }
    }

    private double getMaxSpaceToMySiblingsChildren(int nodeSize, int nodeMinHorizDist, int nodeVertDist, boolean clockOrCounterclockwise) {
        Node sibling = parent.getDesiredSibling(this, clockOrCounterclockwise);
        double arcOnChildrenLevel = ((2 * (sibling.getVertical() + 1) * nodeVertDist * Math.PI) / 360)  * sibling.getCorridorSpanning();
        System.out.println(sibling.getID() + " is the " + (clockOrCounterclockwise ? "clockwise" : "counterclockwise") + " sibling of " + ID + " and has " + sibling.getChildren().size() + " children / angle-corridor of " + sibling.getCorridorSpanning() + ", which yields on its children-level an arc of " + String.format("%.2f", arcOnChildrenLevel));

        if(sibling.getIsLeaf()){
            System.out.println("since this node has no children, the full corridor of " + sibling.getCorridorSpanning() + " degree can be used");
            return sibling.getCorridorSpanning();
        }
        else{
            int siblingsChildrenCount = sibling.getChildren().size();
            double arcRequiredForMinimalPlacement = (siblingsChildrenCount - 1) * nodeMinHorizDist + siblingsChildrenCount * nodeSize;

            System.out.println("arc required for minimal placement: " + arcRequiredForMinimalPlacement);

            double arcsThenFreeNextToMinimalPlacement = arcOnChildrenLevel / 2 - arcRequiredForMinimalPlacement / 2; // free on both sides next to minmal placement corridor
            System.out.println("when this nodes children would be placed on min dist, it can offer " + String.format("%.2f", arcsThenFreeNextToMinimalPlacement) + " of space from both sides");
            return arcsThenFreeNextToMinimalPlacement;
        }
    }

    private Node getDesiredSibling(Node requestingChild, boolean clockOrCounterclockwise) {
        int indexOfRequester = children.indexOf(requestingChild);
        int indexOfDesiredSibling;
        if(clockOrCounterclockwise)
            indexOfDesiredSibling = indexOfRequester == children.size() - 1 ? 0 : indexOfRequester + 1;
        else
            indexOfDesiredSibling = indexOfRequester == 0 ? children.size() - 1 : indexOfRequester - 1;
        return children.get(indexOfDesiredSibling);
    }
*/





/*//find out diffToParentAngle, doesn't work yet, based on arccos sketch...
        double a = Math.sqrt(Math.pow(x - parent.getX(), 2) + Math.pow(y - parent.getY(), 2));
        double b = vertical * nodeVertDist;
        double c = parent.getVertical() * nodeVertDist;

        double B = Math.toDegrees(Math.acos((Math.pow(a,2) + Math.pow(c,2) - Math.pow(b,2)) / (2 * a * c)));
        diffToParentAngle = 180 - B;

        System.out.println("diffToParentAngle: " + diffToParentAngle);
*/


//old stuff of testing arc btwn two test-children :)
//            Node child0 = children.get(0);
//            Node child1 = children.get(1);
//            double angleBtwn = children.get(1).getAngle() - children.get(0).getAngle();
//
//            double arc = ((2 * child0.getVertical() * nodeVertDist * Math.PI) / 360)  * angleBtwn;
//            boolean tooNarrow = arc < nodeMinHorizDist + nodeSize;
//
//            System.out.println(">> taking first two children (" + child0.getID() + ", " + child1.getID() + ") to test. angle btwn them is: " + angleBtwn);
//            System.out.println("that is an arc of: " + String.format("%.2f", arc) + " at a radius of " + (child0.getVertical() * nodeVertDist));
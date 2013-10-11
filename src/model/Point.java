package model;

public class Point {

    public String nodeID;
    public double x = 0;
    public double y = 0;

    public Point(String nodeID, double x, double y) {
        this.nodeID = nodeID;
        this.x = x;
        this.y = y;
    }

    public Point(Point point) {
        this.nodeID = point.nodeID;
        this.x = point.x;
        this.y = point.y;
    }

    public void translate(Point deltaPoint) {
        x += deltaPoint.x;
        y += deltaPoint.y;
    }
}

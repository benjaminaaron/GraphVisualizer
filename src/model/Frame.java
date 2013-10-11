package model;

import java.util.ArrayList;

public class Frame {

    public ArrayList<Point> points = new ArrayList<>();
    public ArrayList<Line> lines = new ArrayList<>();
    public ArrayList<Line> helplines = new ArrayList<>();

    public Frame() {
    }

    public Frame(Frame frame) { //copy-constructor
        for (Point point : frame.points) {
            points.add(new Point(point));
        }
        for (Line line : frame.lines) {
            lines.add(new Line(line));
        }
        for (Line line : frame.helplines) {
            helplines.add(new Line(line));
        }
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public void addLine(Line line) {
        lines.add(line);
    }

    public void addHelpline(Line helpline) {
        helplines.add(helpline);
    }
}

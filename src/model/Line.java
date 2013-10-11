package model;

public class Line {

	public Point source;
	public Point target;
	
	public Line(Point source, Point target){
		this.source = source;
		this.target = target;
	}
	
	public Line(Line line){ //copy-constructor
		this.source = new Point(line.source);
		this.target = new Point(line.target);
	}

	public Point getSource(){
		return source;
	}
	
	public Point getTarget(){
		return target;
	}
	
	public void translate(Line deltaLine){
		source.translate(deltaLine.source);
		target.translate(deltaLine.target);
	}	
}

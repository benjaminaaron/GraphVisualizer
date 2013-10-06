package model;

import java.util.ArrayList;
import java.util.Arrays;

public class Timeline {
	
	public Frame singleFinalKeyframe;
	public Frame[] keyframesTwo;
	public ArrayList<Frame> keyframesLevelwise;
	public ArrayList<Frame> keyframesNodewise;

	
	public Timeline(Frame singleFinalKeyframe, Frame[] keyframesTwo, ArrayList<Frame> keyframesLevelwise, ArrayList<Frame> keyframesNodewise){
		this.singleFinalKeyframe = singleFinalKeyframe;
		this.keyframesTwo = keyframesTwo;
		this.keyframesLevelwise = keyframesLevelwise;
		this.keyframesNodewise = keyframesNodewise;
	}
	
	public ArrayList<Frame> placeStepsBtwnKeyframes(int animIndex, int steps){
				
		ArrayList<Frame> keyframes = new ArrayList<>();
		if(animIndex == 1){
			keyframes.add(keyframesTwo[0]);
			keyframes.add(keyframesTwo[1]);
		}
		if(animIndex == 2)
			keyframes = keyframesLevelwise;
		if(animIndex == 3)
			keyframes = keyframesNodewise;	
		
		System.out.println("placeStepsBtwnKeyframes is working with keyframes of size: " + keyframes.size());
		
		ArrayList<Frame> extended = new ArrayList<>();	
				
		for(int i = 0; i < keyframes.size() - 1; i++){
			Frame fromKeyframe = new Frame(keyframes.get(i));
			extended.add(fromKeyframe);
			Frame toKeyframe = keyframes.get(i + 1);
			
			if(toKeyframe.helplines.size() != 0){
				for(Line helpline : toKeyframe.helplines)
					fromKeyframe.addHelpline(new Line(helpline));
				toKeyframe.helplines.clear();
			}
			
			//save deltas here in Timeline, not encapsulating in points/lines/frames because it's unnecessary ballast, not elegant to carry the no-longer-needed deltas along into the view-package
		
			ArrayList<Point> deltaPoints = new ArrayList<>();		
			int j = 0;
			for(Point fromPoint : fromKeyframe.points){
				Point toPoint = toKeyframe.points.get(j);
				double dx = (toPoint.x - fromPoint.x) / steps;
				double dy = (toPoint.y - fromPoint.y) / steps;		
				deltaPoints.add(new Point("", dx, dy));
				j ++;
			}			
			ArrayList<Line> deltaLines = new ArrayList<>();	
			j = 0;
			for(Line fromLine : fromKeyframe.lines){
				Line toLine = toKeyframe.lines.get(j);
				double dxSource = (toLine.source.x - fromLine.source.x) / steps;
				double dySource = (toLine.source.y - fromLine.source.y) / steps;
				double dxTarget = (toLine.target.x - fromLine.target.x) / steps;
				double dyTarget = (toLine.target.y - fromLine.target.y) / steps;				
				deltaLines.add(new Line(new Point("", dxSource, dySource), new Point("", dxTarget, dyTarget)));
				j ++;
			}
							
			Frame nextKeyframe = new Frame(fromKeyframe);
					
			for(int k = 0; k < steps; k++){					
				j = 0;
				for(Point point : nextKeyframe.points){
					point.translate(deltaPoints.get(j));
					j ++;
				}
				j = 0;
				for(Line line : nextKeyframe.lines){
					line.translate(deltaLines.get(j));
					j ++;
				}
				extended.add(new Frame(nextKeyframe));
			}			

		}
		extended.add(new Frame(keyframes.get(keyframes.size() - 1)));
		
		System.out.println("returning extended with size: " + extended.size());
		return extended;
	}
	
	
	
}

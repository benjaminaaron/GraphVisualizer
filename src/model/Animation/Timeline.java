package model.Animation;

import java.util.ArrayList;

public class Timeline {

    private ArrayList<Frame> frames = new ArrayList<>();

    public Timeline(Frame frame){
        frames.add(frame);
    }

    public Timeline(ArrayList<Frame> frames){
        this.frames = frames;
    }

    public ArrayList<Frame> getFrames(){
        return frames;
    }
}
package view;

import model.Frame;

import java.util.ArrayList;

public class Animator extends Thread {

    private GraphPanel graphPanel;
    private ArrayList<Frame> frames = new ArrayList<>();
    private int currentAnimationStep = 0;
    private boolean isPaused = false;
    private int speed = 33;

    public Animator() {

    }

    public void setGraphPanel(GraphPanel graphPanel) {
        this.graphPanel = graphPanel;
    }

    public ArrayList<Frame> getFrames() {
        return frames;
    }

    public synchronized void setFrames(ArrayList<Frame> frames) {
        this.frames = frames;
        setCurrentAnimationStep(0);
    }

    public int getCurrentAnimationStep() {
        return currentAnimationStep;
    }

    public void setCurrentAnimationStep(int currentAnimationStep) {
        this.currentAnimationStep = currentAnimationStep;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * @return No more animation work to do?
     */
    public boolean isIdle() {
        return currentAnimationStep >= frames.size();
    }

    /**
     * Pauses the animator.
     */
    public void pause() {
        isPaused = true;
    }

    /**
     * Resumes the animator.
     */
    public synchronized void proceed() {
        isPaused = false;
        notify();
    }

    /**
     * Immediately jumps to the last frame and finishes the animation.
     */
    public synchronized void finish() {
        if (!frames.isEmpty()) {
            currentAnimationStep = frames.size() - 1;
        }
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                while (isPaused || isIdle()) {
                    synchronized (this) {
                        wait();
                    }
                }

                graphPanel.setCurrentFrame(frames.get(currentAnimationStep));
                graphPanel.repaint();
                synchronized (this) {
                    currentAnimationStep++;
                }

                if (isIdle()) {
                    graphPanel.animationFinished();
                } else {
                    Thread.sleep(speed);
                }
            } catch (InterruptedException e) {
                graphPanel.repaint();
            }
        }
    }
}
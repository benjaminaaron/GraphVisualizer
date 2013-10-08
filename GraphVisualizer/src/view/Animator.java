package view;

public class Animator {
	
	private GraphPanel graphPanel;

	public Animator(GraphPanel graphPanel){
		this.graphPanel = graphPanel;
	}
	
	public void triggerUpdating(int animSteps){							
		while(animSteps > 0 && graphPanel.getAnimationAllowed() &&
              !Thread.currentThread().isInterrupted()
        ){
			try {
				Thread.sleep(graphPanel.getAnimSpeed());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
			graphPanel.updateAnimation();
			graphPanel.repaint();
			animSteps --;			
		}	
		if(animSteps == 0 || Thread.currentThread().isInterrupted())
			graphPanel.animationFinished();			
	}
}
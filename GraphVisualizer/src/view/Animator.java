package view;

public class Animator {
	
	private GraphPanel w;

	public Animator(GraphPanel w){
		this.w = w;
	}
	
	public void triggerUpdating(int animSteps){		
		
		while(animSteps > 0){		
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
						
			w.updateAnimation();
			w.repaint();
						
			animSteps --;
		}			
		w.animationFinished();		
	}	
}

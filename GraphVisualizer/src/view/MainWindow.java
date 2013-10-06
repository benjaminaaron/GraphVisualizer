package view;

import java.awt.BorderLayout;
import javax.swing.JFrame;

import model.Model;


public class MainWindow extends JFrame{

	private static final long serialVersionUID = 1L;
	private ControlPanel controlPanel;
	private GraphPanel graphPanel;
	private AnimationPanel animationPanel;
	
	public MainWindow(){
		super("simple tree-graph visualizer by benjaminaaron");
		
		setSize(1300, 600);	
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		graphPanel = new GraphPanel();	
		controlPanel = new ControlPanel(graphPanel);
		animationPanel = new AnimationPanel(graphPanel);
		graphPanel.setControlAndSliderPanel(controlPanel, animationPanel);
		
		add(graphPanel, BorderLayout.CENTER);
		add(controlPanel, BorderLayout.NORTH);	
		add(animationPanel, BorderLayout.SOUTH);
				
//        this.getRootPane().addComponentListener(new ComponentAdapter() {
//            public void componentResized(ComponentEvent e) {
//            	hub.updateXoffset();
//            }
//        }); 
		setVisible(true);	
	}
	
	public ControlPanel getControlPanel(){
		return controlPanel;
	}

	public GraphPanel getGraphPanel() {
		return graphPanel;
	}

	public AnimationPanel getAnimationPanel() {
		return animationPanel;
	}
	
}

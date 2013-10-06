package view;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

import model.Model;


public class MainWindow extends JFrame{

	private static final long serialVersionUID = 1L;

	private ControlPanel cp;
	private GraphPanel gp;
	
	public MainWindow(Model m){
		super("simple tree-graph visualizer by benjaminaaron");
		
		setLayout(new BorderLayout());
		cp = new ControlPanel(m);
		gp = new GraphPanel(m, cp);	
		add(gp, BorderLayout.CENTER);
		add(cp, BorderLayout.NORTH);		
		
		cp.setEnabled(false);
		
		setSize(900, 600);
		
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
        this.getRootPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
            	updateXoffset();
            }
        });
        
	}
	
	public void updateXoffset(){		
		gp.updateXoffset();
	}
	
	public ControlPanel getControlPanel(){
		return cp;
	}
	
	public GraphPanel getGraphPanel(){
		return gp;
	}
	
}

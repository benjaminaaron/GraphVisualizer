package view;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

import model.Model;

public class AnimationPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Model model;
	private GraphPanel graphPanel;
	
	private JButton pauseButton, stopButton;
	private JLabel animLabel, blankLabel, nodeSizeLabel, nodeVertDistLabel, nodeMinHorizDistLabel;
	private JRadioButton noAnim, shortAnim, levelByLevelAnim, nodeByNodeAnim;
	private JSlider nodeSizeSlider, nodeVertDistSlider, nodeMinHorizDistSlider; 
	private JCheckBox showNodeIDCheckbox, showHelplinesCheckbox;
	
	private int nodeSize = 20;
	private int nodeVertDist = 50;
	private int nodeMinHorizDist = 30;
	
	private int animIndex = 1;
	private boolean notPaused = true;
	
	public void setModel(Model model) {
		this.model = model;
	}
	
	public int getAnimIndex() {
		return animIndex;
	}
	
	public AnimationPanel(GraphPanel graphPanel){
		this.graphPanel = graphPanel;
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		animLabel = new JLabel("  Animation: ");
		add(animLabel);
		
		ButtonGroup animGroup = new ButtonGroup();
		noAnim = new JRadioButton("none");
		noAnim.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(noAnim.isEnabled())
					setAnimIndex(0);
			}
		});	
		shortAnim = new JRadioButton("short");
		shortAnim.setSelected(true);
		shortAnim.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(shortAnim.isEnabled())
					setAnimIndex(1);
			}
		});	
		levelByLevelAnim = new JRadioButton("levelwise");
		levelByLevelAnim.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(levelByLevelAnim.isEnabled())
					setAnimIndex(2);
			}
		});			
		
		nodeByNodeAnim = new JRadioButton("nodewise");
		nodeByNodeAnim.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(nodeByNodeAnim.isEnabled())
					setAnimIndex(3);
			}
		});
		animGroup.add(noAnim);
		animGroup.add(shortAnim);
		animGroup.add(levelByLevelAnim);
		animGroup.add(nodeByNodeAnim);
		add(noAnim);
		add(shortAnim);
		add(levelByLevelAnim);
		add(nodeByNodeAnim);
		
		pauseButton = new JButton("pause");
		pauseButton.setEnabled(false);
		pauseButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(pauseButton.isEnabled())
					togglePauseContinue();
			}
		});	
		add(pauseButton);
	
		stopButton = new JButton("stop");
		stopButton.setEnabled(false);
		stopButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(stopButton.isEnabled())
					stopAnimation();
			}
		});	
		add(stopButton);
		
		blankLabel = new JLabel("    ");
		add(blankLabel);
		
		nodeSizeLabel = new JLabel("nodeSize: " + nodeSize);
		add(nodeSizeLabel);
		
		nodeSizeSlider = new JSlider();
		nodeSizeSlider.setValue(20);
		nodeSizeSlider.setMaximum(80);	
		nodeSizeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce){
				changeParams();
			}
	        });
		add(nodeSizeSlider);
		
		nodeVertDistLabel = new JLabel("nodeVertDist: " + nodeVertDist);
		add(nodeVertDistLabel);
		
		nodeVertDistSlider = new JSlider();
		nodeVertDistSlider.setValue(50);
		nodeVertDistSlider.setMaximum(150);
		nodeVertDistSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce){
				changeParams();
			}
	        });
		add(nodeVertDistSlider);
		
		nodeMinHorizDistLabel = new JLabel("nodeMinHorizDist: " + nodeMinHorizDist);
		add(nodeMinHorizDistLabel);		
		
		nodeMinHorizDistSlider = new JSlider();
		nodeMinHorizDistSlider.setValue(30);
		nodeMinHorizDistSlider.setMaximum(150);
		nodeMinHorizDistSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce){
				changeParams();
			}
	        });
		add(nodeMinHorizDistSlider);
		
		showNodeIDCheckbox = new JCheckBox("show node-IDs");
		showNodeIDCheckbox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(showNodeIDCheckbox.isEnabled())
					setShowNodeID();
			}
		});	
		add(showNodeIDCheckbox);
		
		showHelplinesCheckbox = new JCheckBox("show helplines");
		showHelplinesCheckbox.setEnabled(false);
		showHelplinesCheckbox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(showHelplinesCheckbox.isEnabled())
					setShowHelplines();
				else
					JOptionPane.showMessageDialog(null, "helplines can only be displayed in the animation-mode 'nodewise'", "only in nodewise animation", JOptionPane.PLAIN_MESSAGE);
			}
		});	
		add(showHelplinesCheckbox);
		
		
	}
	
	
	private void changeParams(){	
		if(noAnim.isSelected()){
			nodeSize = nodeSizeSlider.getValue();
			nodeVertDist = nodeVertDistSlider.getValue();
			nodeMinHorizDist = nodeMinHorizDistSlider.getValue();
			
			nodeSizeLabel.setText("nodeSize: " + nodeSize);
			nodeVertDistLabel.setText("nodeVertDist: " + nodeVertDist); 
			nodeMinHorizDistLabel.setText("nodeMinHorizDist: " + nodeMinHorizDist);	
			
			model.changeNodeParams(nodeSize, nodeVertDist, nodeMinHorizDist);
			graphPanel.setNodeSizeAndNodeVertDist(nodeSize, nodeVertDist);
			graphPanel.setTimeline(model.getTimeline());
		}
		else
			JOptionPane.showMessageDialog(null, "can't change parameters unless in animation modus <none>", "wrong animation modus", JOptionPane.PLAIN_MESSAGE);	
	}
	
	
	private void setShowNodeID() {
		graphPanel.setShowNodeID(showNodeIDCheckbox.isSelected());
	}
	
	private void setShowHelplines(){
		graphPanel.setShowHelplines(showHelplinesCheckbox.isSelected());
	}
	
	private void togglePauseContinue(){
		if(notPaused){
			notPaused = false;
			pauseButton.setText("continue");
			graphPanel.pauseAnimation();
		}
		else{
			notPaused = true;
			pauseButton.setText("pause");
			graphPanel.continueAnimation();
		}	
	}
	
	
	private void stopAnimation(){
		graphPanel.stopAnimation();
	}
	
	private void setAnimIndex(int index){
		if(index == 3)
			showHelplinesCheckbox.setEnabled(true);
		else
			showHelplinesCheckbox.setEnabled(false);
		
		animIndex = index;
		graphPanel.setAnimIndex(index);
	}

	public void enableSwitch(boolean onoff) {
		pauseButton.setEnabled(!onoff);
		stopButton.setEnabled(!onoff);	
		
		animLabel.setEnabled(onoff);
		noAnim.setEnabled(onoff);
		shortAnim.setEnabled(onoff);
		levelByLevelAnim.setEnabled(onoff);	
		nodeByNodeAnim.setEnabled(onoff);	
		
		nodeSizeSlider.setEnabled(onoff); 
		nodeVertDistSlider.setEnabled(onoff); 
		nodeMinHorizDistSlider.setEnabled(onoff); 
		nodeSizeLabel.setEnabled(onoff);
		nodeVertDistLabel.setEnabled(onoff);
		nodeMinHorizDistLabel.setEnabled(onoff);
		
		if(!onoff)
			showHelplinesCheckbox.setEnabled(false);
		if(onoff && animIndex == 3)
			showHelplinesCheckbox.setEnabled(true);
	}





	
	
	
	
	
}

package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import model.Importer;
import model.Model;


public class ControlPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Model m;	
	
	private JLabel graphLabel, layoutLabel, animLabel, blankLabel, blankLabel2;
	private JButton newButton, sampleButton, importButton, exportButton, helpButton;
	private JRadioButton basicLayout, treeLayout, shortAnim, stepByStepAnim;
	

	public ControlPanel(Model m){
		this.m = m;
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		graphLabel = new JLabel("   Graph:   ");
		add(graphLabel);
				
		newButton = new JButton("new");
		newButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(newButton.isEnabled())
					initNewGraph();
			}
		});	
		add(newButton);


		sampleButton = new JButton("sample");
		sampleButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(sampleButton.isEnabled())
					initSampleGraph();
			}
		});	
		add(sampleButton);
		
		blankLabel = new JLabel("      ");
		add(blankLabel);
		
		importButton = new JButton("import");
		importButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(importButton.isEnabled())	
					try {
						importGraph();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			}
		});	
		add(importButton);
		

		
		layoutLabel = new JLabel("     Layout: ");
		add(layoutLabel);
		
		ButtonGroup layoutGroup = new ButtonGroup();
		basicLayout = new JRadioButton("plain");
		basicLayout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(basicLayout.isEnabled())
					setBasicLayout();
			}
		});	
		treeLayout = new JRadioButton("nice");
		treeLayout.setSelected(true);
		treeLayout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(treeLayout.isEnabled())
					setTreeLayout();
			}
		});	
		layoutGroup.add(basicLayout);
		layoutGroup.add(treeLayout);
		add(basicLayout);
		add(treeLayout);		

		
		animLabel = new JLabel("          Animation: ");
		add(animLabel);
		
		ButtonGroup animGroup = new ButtonGroup();
		shortAnim = new JRadioButton("short");
		shortAnim.setSelected(true);
		shortAnim.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(shortAnim.isEnabled())
					setShortAnim();
			}
		});	
		stepByStepAnim = new JRadioButton("step-by-step   ");
		stepByStepAnim.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(stepByStepAnim.isEnabled())
					setStepByStepAnim();
			}
		});	
		animGroup.add(shortAnim);
		animGroup.add(stepByStepAnim);
		add(shortAnim);
		add(stepByStepAnim);
		
		
		exportButton = new JButton("export");
		exportButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(exportButton.isEnabled())			
					try {
						exportGraph();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
			}
		});	
		add(exportButton);
		
		blankLabel2 = new JLabel("      ");
		add(blankLabel2);

		helpButton = new JButton("help");
		helpButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(helpButton.isEnabled())
					JOptionPane.showMessageDialog(null, "left mouse-click:     add a child to the clicked node"
							+ "\nright mouse-click:  delete a node & everything below it"
							+ "\n\nimport & export in graphml-format / e.g. yEd (freeware) uses that"
							+ "\n\ncrash-causes:"
							+ "\n- this program can only handle strict tree-structures (every node but the rootnode has exactly one parent)"
							+ "\n   at this point - importing other kind of graphs might result in funny things"
							+ "\n- switching to step-by-step animation if rootnode has not at least two grandchildren won't work"
							+ "\n   as well as deletion in step-by-step mode that would create that condition"
							+ "\n\n\nbenjaminaaron / September 2013", "help", JOptionPane.PLAIN_MESSAGE);
			}
		});	
		add(helpButton);
	}
	
	
	private void initNewGraph(){
		m.initNewGraph();
	}
	
	private void initSampleGraph(){
		m.initSampleGraph();
	}
	
	private void setBasicLayout(){
		m.setBasicLayout();
	}
	
	private void setTreeLayout(){
		m.setTreeLayout();
	}
	
	private void setShortAnim(){
		m.setShortAnim();
	}
	
//	public boolean getShortAnimSelected(){
//		return shortAnim.isSelected();
//	}
	
	private void setStepByStepAnim(){	
		//if(m.allowStepByStepAnim())
			m.setStepByStepAnim();
//		else{
//			shortAnim.setSelected(true);
//			stepByStepAnim.setSelected(false);
//			JOptionPane.showMessageDialog(null, "can't enable step-by-step animation if rootnode doesn't have grandchildren at least", "ancestry too small", JOptionPane.PLAIN_MESSAGE);
//		}
	}
	
	private void exportGraph() throws FileNotFoundException{
		String filename = m.exportGraph();		
		JOptionPane.showMessageDialog(null, "exported as file: \n" + filename, "export successful", JOptionPane.PLAIN_MESSAGE);
	}
	
	private void importGraph() throws IOException{
		Importer importer = new Importer();
		if(importer.isFileReady())
			m.loadImportedGraph(importer.convertGraphmlFileToGraph());
	}
	
	public void disableAll(){
		enableDisable(false);
	}
	
	public void enableAll(){
		enableDisable(true);
	}
	
	private void enableDisable(boolean onoff){
		graphLabel.setEnabled(onoff);
		layoutLabel.setEnabled(onoff);
		animLabel.setEnabled(onoff);
		newButton.setEnabled(onoff);
		sampleButton.setEnabled(onoff);
		importButton.setEnabled(onoff);
		exportButton.setEnabled(onoff);
		helpButton.setEnabled(onoff);
		basicLayout.setEnabled(onoff);
		treeLayout.setEnabled(onoff);
		shortAnim.setEnabled(onoff);
		stepByStepAnim.setEnabled(onoff);	
	}
}

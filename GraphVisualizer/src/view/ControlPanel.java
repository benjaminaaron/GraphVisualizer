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

import model.*;


public class ControlPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JLabel layoutLabel, animLabel, orderLabel; //graphLabel
	private JButton newButton, sampleButton, importButton, exportButton, helpButton, consoleShowButton;
	private JRadioButton basicLayout, treeLayout, radialPlainLayout, radialSmartLayout, randomLayout, chronoOrder, leftOrder, rightOrder, middleFullOrder, middleEmptyOrder;
	
	private Model model;
	private GraphPanel graphPanel;
	
	private LayoutInterface layoutAlgorithm = new TreeLayout();
	private int horizOrderIndex = 0;
	
	private int nodeSize = 20;
	private int nodeVertDist = 50;
	private int nodeMinHorizDist = 30;
	
	
	public void setModel(Model model){
		this.model = model;
		model.setParams(layoutAlgorithm, nodeSize, nodeVertDist, nodeMinHorizDist);
	}
	
	public int getNodeSize() {
		return nodeSize;
	}
	
	public int getNodVertDist() {
		return nodeVertDist;
	}
	
	public int getHorizOrderIndex() {
		return horizOrderIndex;
	}
	
	
	public ControlPanel(GraphPanel graphPanel){
		this.graphPanel = graphPanel;
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
//		graphLabel = new JLabel("  Graph: ");
//		add(graphLabel);
				
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
		

		
		layoutLabel = new JLabel("    Layout: ");
		add(layoutLabel);
		
		ButtonGroup layoutGroup = new ButtonGroup();
		basicLayout = new JRadioButton("plain");
		basicLayout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(basicLayout.isEnabled())
					setLayoutAlgorithm(0, new BasicLayout());
			}
		});	
		treeLayout = new JRadioButton("tree");
		treeLayout.setSelected(true);
		treeLayout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(treeLayout.isEnabled())
					setLayoutAlgorithm(1, new TreeLayout());
			}
		});	
		radialPlainLayout = new JRadioButton("radial plain");
		radialPlainLayout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(radialPlainLayout.isEnabled())
					setLayoutAlgorithm(2, new RadialPlainLayout());
			}
		});
		radialSmartLayout = new JRadioButton("radial smart");
		radialSmartLayout.setEnabled(false);
		radialSmartLayout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				JOptionPane.showMessageDialog(null, "in the next version :)", "coming up soon...", JOptionPane.PLAIN_MESSAGE);
//				if(radialSmartLayout.isEnabled())
//					setLayoutAlgorithm(3, new RadialSmartLayout());
			}
		});
		randomLayout = new JRadioButton("random");
		randomLayout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(randomLayout.isEnabled())
					setLayoutAlgorithm(4, new RandomLayout());
			}
		});	
		layoutGroup.add(basicLayout);
		layoutGroup.add(treeLayout);
		layoutGroup.add(radialPlainLayout);
		layoutGroup.add(radialSmartLayout);
		layoutGroup.add(randomLayout);
		add(basicLayout);
		add(treeLayout);
		add(radialPlainLayout);
		add(radialSmartLayout);
		add(randomLayout);		

		
		orderLabel = new JLabel("   |  Order: ");
		add(orderLabel);
		
		ButtonGroup orderGroup = new ButtonGroup();
		chronoOrder = new JRadioButton("chronological");
		chronoOrder.setSelected(true);
		chronoOrder.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(chronoOrder.isEnabled())
					setHorizOrderIndex(0);
			}
		});	
		leftOrder = new JRadioButton("left");
		leftOrder.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(leftOrder.isEnabled())
					setHorizOrderIndex(1);
			}
		});	
		rightOrder = new JRadioButton("right");
		rightOrder.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(rightOrder.isEnabled())
					setHorizOrderIndex(2);
			}
		});	
		middleFullOrder = new JRadioButton("middle full");
		middleFullOrder.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(middleFullOrder.isEnabled())
					setHorizOrderIndex(3);
			}
		});	
		middleEmptyOrder = new JRadioButton("middle empty");
		middleEmptyOrder.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(middleEmptyOrder.isEnabled())
					setHorizOrderIndex(4);
			}
		});	
		orderGroup.add(chronoOrder);
		orderGroup.add(leftOrder);
		orderGroup.add(rightOrder);
		orderGroup.add(middleFullOrder);
		orderGroup.add(middleEmptyOrder);
		add(chronoOrder);
		add(leftOrder);
		add(rightOrder);
		add(middleFullOrder);
		add(middleEmptyOrder);
	
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
		
		consoleShowButton = new JButton("show data");
		consoleShowButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(consoleShowButton.isEnabled())
					consoleShow();
			}
		});	
		add(consoleShowButton);

		helpButton = new JButton("help");
		helpButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(helpButton.isEnabled())
					JOptionPane.showMessageDialog(null, "left mouse-click:     add a child to the clicked node"
							+ "\nleft mouse-click and release between nodes below:   add a child at that position between it's siblings"
							+ "\nright mouse-click:  delete a node & everything attached to it"
							+ "\n\nimport & export in graphml-format / e.g. yEd (freeware) uses that"
							+ "\nin the animation mode 'nodewise' there is a first attempt to show helplines, if activated"
							+ "\nradial plain looks cool when just one rootnode-child gets looots of children - and then add another rootnode-child"
							+ "\n\ncrash-causes:"
							+ "\n- at this point this program can only handle strict tree-structures (every node but the rootnode has exactly one parent)"
							+ "\n   importing other kind of graphs might result in funny things"
							+ "\n- ... you tell me? probably quite some :)"
							+ "\n\n\nbenjaminaaron / Version 3 / October 2013", "help", JOptionPane.PLAIN_MESSAGE);
			}
		});	
		add(helpButton);
	}
	
	
	private void consoleShow() {
		String data = model.getGraph().consoleShow();
		System.out.println(data);
		JOptionPane.showMessageDialog(null, data, "graph-data", JOptionPane.PLAIN_MESSAGE);
	}
	
	public void initNewGraph(){
		model.initNewGraph();
		Frame initKeyframe = new Frame();
		initKeyframe.addPoint(new Point("node_rootnode", 0, 0));
		graphPanel.setTimeline(new Timeline(initKeyframe, null, null, null));
	}
	
	public void initSampleGraph(){
		model.initSampleGraph();
		graphPanel.setTimeline(model.getTimeline());
	}
	
	public void handleNodeClicked(String clickedNodeID, boolean leftButton){
		model.handleNodeClicked(clickedNodeID, leftButton);
		graphPanel.setTimeline(model.getTimeline());
	}
	
	public void handleNodeClicked(String clickedNodeID, String closestNodeToRelease, boolean rightFromThis) {
		model.handleNodeClicked(clickedNodeID, closestNodeToRelease, rightFromThis);
		graphPanel.setTimeline(model.getTimeline());
	}
	
	private void setLayoutAlgorithm(int index, LayoutInterface layoutAlgorithm){
		this.layoutAlgorithm = layoutAlgorithm;
		
		graphPanel.setOffset(650, index == 2 || index == 3 ? 200 : 80);
		
		model.setLayoutAlgorithm(layoutAlgorithm);
		graphPanel.setTimeline(model.getTimeline());
	}
	
	private void setHorizOrderIndex(int index){
		horizOrderIndex = index;
		model.setOrderIndex(index);
		graphPanel.setTimeline(model.getTimeline());
	}
	
	private void exportGraph() throws FileNotFoundException{
		String filename = model.exportGraph();		
		JOptionPane.showMessageDialog(null, "exported as file: \n" + filename, "export successful", JOptionPane.PLAIN_MESSAGE);
	}
	
	private void importGraph() throws IOException{		
		model.loadImportedGraph(Importer.convertGraphmlFileToGraph());
		graphPanel.setTimeline(model.getTimeline());
	}	
	
	public void enableSwitch(boolean onoff){
		//graphLabel.setEnabled(onoff);
		layoutLabel.setEnabled(onoff);
		orderLabel.setEnabled(onoff);
		newButton.setEnabled(onoff);
		sampleButton.setEnabled(onoff);
		importButton.setEnabled(onoff);
		exportButton.setEnabled(onoff);
		helpButton.setEnabled(onoff);
		consoleShowButton.setEnabled(onoff);
		basicLayout.setEnabled(onoff);
		treeLayout.setEnabled(onoff);
		radialPlainLayout.setEnabled(onoff);
		//radialSmartLayout.setEnabled(onoff);
		randomLayout.setEnabled(onoff);			
		chronoOrder.setEnabled(onoff);	
		leftOrder.setEnabled(onoff);
		rightOrder.setEnabled(onoff);	
		middleFullOrder.setEnabled(onoff);	
		middleEmptyOrder.setEnabled(onoff);
	}

}

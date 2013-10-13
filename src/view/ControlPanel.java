package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.*;

import model.*;
import model.Animation.Frame;
import model.Animation.Timeline;
import model.Layout.*;


public class ControlPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JLabel layoutLabel, orderLabel;
    private JButton newButton, importButton, exportButton, helpButton, consoleShowButton;
    private JComboBox sampleComboBox, layoutComboBox, orderComboBox;

    private Model model;
    private Animator animator;
    private GraphPanel graphPanel;

    private LayoutInterface layoutAlgorithm = new TreeLayout();
    private int horizOrderIndex = 0;

    private int nodeSize = 20;
    private int nodeVertDist = 40;
    private int nodeMinHorizDist = 30;


    public void setModel(Model model) {
        this.model = model;
        // TODO (Jonas): Remove this side effect, because when the model is injected in other objects to,
        // they might overwrite each other.
        model.setParams(layoutAlgorithm, nodeSize, nodeVertDist, nodeMinHorizDist);
    }

    public void setAnimator(Animator animator) {
        this.animator = animator;
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


    public ControlPanel(GraphPanel graphPanel) {
        this.graphPanel = graphPanel;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JLabel graphLabel = new JLabel("  Graph: ");
		add(graphLabel);

        newButton = new JButton("new");
        newButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (newButton.isEnabled()) {
                    initNewGraph();
                }
            }
        });
        add(newButton);

        JLabel sampleLabel = new JLabel("   load sample: ");
        add(sampleLabel);

        String[] animOptions = {"sample 1", "sample 2"};
        sampleComboBox = new JComboBox(animOptions);
        sampleComboBox.setSelectedItem("sample 1");
        sampleComboBox.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                switch ((String) sampleComboBox.getSelectedItem()) {
                    case "sample 1":
                        layoutComboBox.setSelectedItem("tree");
                        setLayoutAlgorithm(1, new TreeLayout());
                        initSampleGraph(1);
                        break;
                    case "sample 2":
                        layoutComboBox.setSelectedItem("radial plain");
                        setLayoutAlgorithm(2, new RadialPlainLayout());
                        initSampleGraph(2);
                        break;
                }
            }
        });
        add(sampleComboBox);

        JLabel blankLabel = new JLabel("     ");
        add(blankLabel);

        importButton = new JButton("import");
        importButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (importButton.isEnabled()) {
                    try {
                        importGraph();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        add(importButton);


        layoutLabel = new JLabel("    Layout: ");
        add(layoutLabel);


        String[] layoutOptions = {"plain", "tree", "radial plain", "random"};
        layoutComboBox = new JComboBox(layoutOptions);
        layoutComboBox.setSelectedItem("tree");
        layoutComboBox.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                switch ((String) layoutComboBox.getSelectedItem()) {
                    case "plain":
                        setLayoutAlgorithm(0, new BasicLayout());
                        break;
                    case "tree":
                        setLayoutAlgorithm(1, new TreeLayout());
                        break;
                    case "radial plain":
                        setLayoutAlgorithm(2, new RadialPlainLayout());
                        break;
                    case "random":
                        setLayoutAlgorithm(4, new RandomLayout());
                        break;
                }
            }
        });
        add(layoutComboBox);


        orderLabel = new JLabel("       Order: ");
        add(orderLabel);

        String[] orderOptions = {"chronological", "left", "right", "middle full", "middle empty"};
        orderComboBox = new JComboBox(orderOptions);
        orderComboBox.setSelectedItem("chronological");
        orderComboBox.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                switch ((String) orderComboBox.getSelectedItem()) {
                    case "chronological":
                        setHorizOrderIndex(0);
                        break;
                    case "left":
                        setHorizOrderIndex(1);
                        break;
                    case "right":
                        setHorizOrderIndex(2);
                        break;
                    case "middle full":
                        setHorizOrderIndex(3);
                        break;
                    case "middle empty":
                        setHorizOrderIndex(4);
                        break;
                }
            }
        });
        add(orderComboBox);

        JLabel blankLabel2 = new JLabel("       ");
        add(blankLabel2);

        consoleShowButton = new JButton("show data");
        consoleShowButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (consoleShowButton.isEnabled()) {
                    consoleShow();
                }
            }
        });
        add(consoleShowButton);

        exportButton = new JButton("export");
        exportButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (exportButton.isEnabled()) {
                    try {
                        exportGraph();
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        add(exportButton);

        helpButton = new JButton("help");
        helpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (helpButton.isEnabled()) {
                    JOptionPane.showMessageDialog(null, "left mouse-click:     add a child to the clicked node"
                            //+ "\nleft mouse-click and release between nodes below:   add a child at that position between it's siblings"   //feature currently inactive
                            + "\nright mouse-click:  delete a node & everything attached to it"
                            + "\n\nimport & export in graphml-format / e.g. yEd (freeware) uses that"
                            //+ "\nin the animation mode 'nodewise' there is a first attempt to show helplines, if activated"  //feature also currently inactive
                            + "\n\nat this point this program can only handle strict tree-structures (every node but the"
                            + "\nrootnode has exactly one parent) importing other kind of graphs might result in funny things"
                            , "help", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });
        add(helpButton);
    }



    private void consoleShow() {
        String data = model.getGraph().consoleShow();
        System.out.println(data);
        JOptionPane.showMessageDialog(null, data, "graph-data", JOptionPane.PLAIN_MESSAGE);
    }

    public void initNewGraph() {
        model.initNewGraph();
        Frame initKeyframe = new Frame();
        initKeyframe.addPoint(new Point("node_rootnode", 0, 0));
        Timeline blankTimeline = new Timeline();
        blankTimeline.addKeyframe(initKeyframe);
        graphPanel.setTimeline(blankTimeline);
    }

    public void initSampleGraph(int index) {
        model.initSampleGraph(index);
        graphPanel.setTimeline(model.getTimeline());
    }

    public void handleNodeClicked(String clickedNodeID, boolean leftButton) {
        model.handleNodeClicked(clickedNodeID, leftButton);
        graphPanel.setTimeline(model.getTimeline());
    }

    public void handleNodeClicked(String clickedNodeID, String closestNodeToRelease, boolean rightFromThis) {
//TODO needs reworking as mentioned in the model method
//        model.handleNodeClicked(clickedNodeID, closestNodeToRelease, rightFromThis);
//        graphPanel.setTimeline(model.getTimeline());
    }

    private void setLayoutAlgorithm(int index, LayoutInterface layoutAlgorithm) {
        this.layoutAlgorithm = layoutAlgorithm;

        graphPanel.setOffset(650, index == 2 || index == 3 ? 200 : 80);

        model.setLayoutAlgorithm(layoutAlgorithm);
        graphPanel.setTimeline(model.getTimeline());
    }

    private void setHorizOrderIndex(int index) {
        horizOrderIndex = index;
        model.setOrderIndex(index);
        graphPanel.setTimeline(model.getTimeline());
    }

    private void exportGraph() throws FileNotFoundException {
        String filename = model.exportGraph();
        JOptionPane.showMessageDialog(null, "exported as file: \n" + filename, "export successful", JOptionPane.PLAIN_MESSAGE);
    }

    private void importGraph() throws IOException {
        model.loadImportedGraph(Importer.convertGraphmlFileToGraph());
        graphPanel.setTimeline(model.getTimeline());
    }
}

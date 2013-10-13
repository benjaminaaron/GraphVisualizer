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
    private Animator animator;
    private GraphPanel graphPanel;

    private JButton pauseButton, stopButton;
    private JLabel animLabel, animSpeedLabel, blankLabel, nodeSizeLabel, nodeVertDistLabel, nodeMinHorizDistLabel, horizontalAnimLabel, verticalAnimLabel, nodewiseAnimLabel;
    private JRadioButton noAnim, shortAnim, horizontalTopDownAnim, horizontalBottomUpAnim, nodewiseAnim, verticalL2Ranim, verticalR2Lanim, randomNodewiseAnim;
    private JSlider animSpeedSlider, nodeSizeSlider, nodeVertDistSlider, nodeMinHorizDistSlider;
    private JCheckBox showNodeIDCheckbox;//, showHelplinesCheckbox;

    private int nodeSize = 20;
    private int nodeVertDist = 40;
    private int nodeMinHorizDist = 30;


    //index of the current animation-type
    private int animIndex = 1;

    public void setModel(Model model) {
        this.model = model;
    }

    public void setAnimator(Animator animator) {
        this.animator = animator;
    }

    public int getAnimIndex() {
        return animIndex;
    }

    public AnimationPanel(GraphPanel graphPanel) {
        this.graphPanel = graphPanel;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        animLabel = new JLabel("  Animation: ");
        add(animLabel);

        ButtonGroup animGroup = new ButtonGroup();
        noAnim = new JRadioButton("none");
        noAnim.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (noAnim.isEnabled()) {
                    setAnimIndex(0);
                }
            }
        });
        shortAnim = new JRadioButton("short");
        shortAnim.setSelected(true);
        shortAnim.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (shortAnim.isEnabled()) {
                    setAnimIndex(1);
                }
            }
        });

//        horizontalAnimLabel = new JLabel("  horizontal: ");
//        add(horizontalAnimLabel);

        horizontalTopDownAnim = new JRadioButton("top-down");
        horizontalTopDownAnim.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (horizontalTopDownAnim.isEnabled()) {
                    setAnimIndex(2);
                }
            }
        });
        horizontalBottomUpAnim = new JRadioButton("bottom-up");
        horizontalBottomUpAnim.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (horizontalBottomUpAnim.isEnabled()) {
                    setAnimIndex(3);
                }
            }
        });

//        verticalAnimLabel = new JLabel("  vertical: ");
//        add(verticalAnimLabel);

        verticalL2Ranim = new JRadioButton("left > right");
        verticalL2Ranim.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (verticalL2Ranim.isEnabled()) {
                    setAnimIndex(4);
                }
            }
        });
        verticalR2Lanim = new JRadioButton("left < right");
        verticalR2Lanim.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (verticalR2Lanim.isEnabled()) {
                    setAnimIndex(5);
                }
            }
        });

//        nodewiseAnimLabel = new JLabel("  nodewise: ");
//        add(nodewiseAnimLabel);

        nodewiseAnim = new JRadioButton("recursively");
        nodewiseAnim.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (nodewiseAnim.isEnabled()) {
                    setAnimIndex(6);
                }
            }
        });
        randomNodewiseAnim = new JRadioButton("random");
        randomNodewiseAnim.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (randomNodewiseAnim.isEnabled()) {
                    setAnimIndex(7);
                }
            }
        });
        animGroup.add(noAnim);
        animGroup.add(shortAnim);
        animGroup.add(horizontalTopDownAnim);
        animGroup.add(horizontalBottomUpAnim);
        animGroup.add(verticalL2Ranim);
        animGroup.add(verticalR2Lanim);
        animGroup.add(nodewiseAnim);
        animGroup.add(randomNodewiseAnim);
        add(noAnim);
        add(shortAnim);
        add(horizontalTopDownAnim);
        add(horizontalBottomUpAnim);
        add(verticalL2Ranim);
        add(verticalR2Lanim);
        add(nodewiseAnim);
        add(randomNodewiseAnim);

        pauseButton = new JButton("pause");
        pauseButton.setEnabled(false);
        pauseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (pauseButton.isEnabled()) {
                    togglePauseContinue();
                }
            }
        });
        add(pauseButton);

        stopButton = new JButton("stop");
        stopButton.setEnabled(false);
        stopButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (stopButton.isEnabled()) {
                    animator.finish();
                }
            }
        });
        add(stopButton);

        animSpeedLabel = new JLabel(" speed:");
        add(animSpeedLabel);

        animSpeedSlider = new JSlider();
        animSpeedSlider.setValue(30);
        animSpeedSlider.setMinimum(0);
        animSpeedSlider.setMaximum(58);
        animSpeedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                if (!animSpeedSlider.getValueIsAdjusting()) {
                    changeAnimSpeed();
                }
            }
        });
        add(animSpeedSlider);

        nodeSizeLabel = new JLabel("  size: " + nodeSize);
        add(nodeSizeLabel);

        nodeSizeSlider = new JSlider();
        nodeSizeSlider.setValue(20);
        nodeSizeSlider.setMaximum(80);
        nodeSizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                changeParams();
            }
        });
        add(nodeSizeSlider);

        nodeVertDistLabel = new JLabel("vert dist: " + nodeVertDist);
        add(nodeVertDistLabel);

        nodeVertDistSlider = new JSlider();
        nodeVertDistSlider.setValue(40);
        nodeVertDistSlider.setMaximum(150);
        nodeVertDistSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                changeParams();
            }
        });
        add(nodeVertDistSlider);

        nodeMinHorizDistLabel = new JLabel("horiz dist: " + nodeMinHorizDist);
        add(nodeMinHorizDistLabel);

        nodeMinHorizDistSlider = new JSlider();
        nodeMinHorizDistSlider.setValue(30);
        nodeMinHorizDistSlider.setMaximum(150);
        nodeMinHorizDistSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                changeParams();
            }
        });
        add(nodeMinHorizDistSlider);

        showNodeIDCheckbox = new JCheckBox("IDs");
        showNodeIDCheckbox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (showNodeIDCheckbox.isEnabled()) {
                    setShowNodeID();
                }
            }
        });
        add(showNodeIDCheckbox);

//        showHelplinesCheckbox = new JCheckBox("helplines");
//        showHelplinesCheckbox.setEnabled(false);
//        showHelplinesCheckbox.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                if (showHelplinesCheckbox.isEnabled()) {
//                    setShowHelplines();
//                } else {
//                    JOptionPane.showMessageDialog(null, "helplines can only be displayed in the animation-mode 'nodewise'", "only in nodewise animation", JOptionPane.PLAIN_MESSAGE);
//                }
//            }
//        });
//        add(showHelplinesCheckbox);
    }


    private void changeParams() {
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

    private void changeAnimSpeed() {
        animator.setSpeed(60 - animSpeedSlider.getValue());
    }


    private void setShowNodeID() {
        graphPanel.setShowNodeID(showNodeIDCheckbox.isSelected());
    }

//    private void setShowHelplines() {
//        graphPanel.setShowHelplines(showHelplinesCheckbox.isSelected());
//    }

    private void togglePauseContinue() {
        if (animator.isPaused()) {
            pauseButton.setText("pause");
            animator.proceed();
        } else {
            pauseButton.setText("continue");
            animator.pause();
        }
    }

    private void setAnimIndex(int index) {
        animIndex = index;
        graphPanel.setAnimIndex(index);
        model.setAnimIndex(index);
        graphPanel.setTimeline(model.getTimeline());
    }

    public void enableSwitch(boolean onoff) {
        pauseButton.setEnabled(!onoff);
        stopButton.setEnabled(!onoff);
    }


}

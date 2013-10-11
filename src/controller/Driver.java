package controller;

import model.Model;
import view.Animator;
import view.MainWindow;

public class Driver {
    public static void main(String[] args) {
        Model model = new Model();
        MainWindow mainWindow = new MainWindow();
        Animator animator = new Animator();
        animator.setGraphPanel(mainWindow.getGraphPanel());
        animator.start();
        mainWindow.getGraphPanel().setAnimator(animator);
        mainWindow.getControlPanel().setModel(model);
        mainWindow.getControlPanel().setAnimator(animator);
        mainWindow.getAnimationPanel().setModel(model);
        mainWindow.getAnimationPanel().setAnimator(animator);
        //start with sample graph instead of gray, simulates click on <sample> button
        mainWindow.getControlPanel().initSampleGraph();
    }
}

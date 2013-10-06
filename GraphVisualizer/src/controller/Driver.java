package controller;

import model.Model;
import view.MainWindow;

public class Driver {
	public static void main(String[] args) {
		Model model = new Model();	
		MainWindow mainWindow = new MainWindow();	
		mainWindow.getControlPanel().setModel(model);
		mainWindow.getAnimationPanel().setModel(model);
		//start with sample graph instead of gray, simulates click on <sample> button
		mainWindow.getControlPanel().initSampleGraph();
	}
}

package controller;

import model.Model;
import view.MainWindow;

public class Driver {

	public static void main(String[] args) {
		Model m = new Model();
		MainWindow mw = new MainWindow(m);	
		m.setGraphPanel(mw.getGraphPanel(), mw.getControlPanel());	
		m.initNewGraph();
	}

}

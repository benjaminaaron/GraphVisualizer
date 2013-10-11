package model;

import java.io.IOException;
import java.util.ArrayList;

import controller.FileChooser;

public class Importer {
	
	private static String[] graphmlFile;
		
	public static Graph convertGraphmlFileToGraph() throws IOException{
		FileChooser fc = new FileChooser();
		graphmlFile = fc.chooseFile();
		
		for(int i = 0; i < graphmlFile.length; i++)
			System.out.println(graphmlFile[i]);
		
		ArrayList<Node> nodes = new ArrayList<>();
		ArrayList<Edge> edges  = new ArrayList<>();	
		
		boolean inNodeSection = false;
		String a = String.valueOf('"');
		String nodeID = "";
		String nodeValue = "";	
		//String edgeID = "";
		String edgeSourceID = "";
		String edgeTargetID = "";		
		
		for(String s : graphmlFile){
			String editedLine = s.split("<")[1].replaceAll(">", "%").replaceAll(" ", "%");
			String[] pieces = editedLine.split("%");
			String keyword = pieces[0];		
			if(keyword.equals("node")){
				inNodeSection = true;
				nodeID = pieces[1].split(a)[1];
			}		
			if(inNodeSection && keyword.equals("y:NodeLabel")){
				inNodeSection = false;
				nodeValue = pieces[pieces.length - 1];			
				nodes.add(new Node(nodeID, nodeValue));
			}						
			if(keyword.equals("edge")){
				//edgeID = s.split(a)[1];
				edgeSourceID = s.split(a)[3];
				edgeTargetID = s.split(a)[5];
				edges.add(new Edge(findNodeByID(nodes, edgeSourceID), findNodeByID(nodes, edgeTargetID)));
			}
		}		
		return new Graph(nodes, edges);		
	}
	
	private static Node findNodeByID(ArrayList<Node> nodes, String nodeID){
		for(Node node : nodes)
			if(node.getID().equals(nodeID))
				return node;
		return null;
	}
}
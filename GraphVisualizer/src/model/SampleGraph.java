package model;

public class SampleGraph {

	Graph sample;
	
	public SampleGraph(){
		sample = new Graph();
		
		String rootID = sample.addNode("rootnode");
		
		String level1aID = sample.addNode("level1A");		
		sample.addEdge(rootID, level1aID);	
		String level1bID = sample.addNode("level1B");		
		sample.addEdge(rootID, level1bID);
		
		String level1cID = sample.addNode("level1C");
		sample.addEdge(rootID, level1cID);
		
		String level1dID = sample.addNode("level1D");		
		sample.addEdge(rootID, level1dID);
	
		
		String level2hID = sample.addNode("level2H");
		sample.addEdge(level1aID, level2hID);

		
		String level2aID = sample.addNode("level2A");
		sample.addEdge(level1bID, level2aID);
		String level2bID = sample.addNode("level2B");
		sample.addEdge(level1bID, level2bID);
		String level2cID = sample.addNode("level2C");
		sample.addEdge(level1bID, level2cID);
		
		
		String level2dID = sample.addNode("level2D");
		sample.addEdge(level1cID, level2dID);
		
		
		String level2eID = sample.addNode("level2E");
		sample.addEdge(level1dID, level2eID);
		String level2fID = sample.addNode("level2F");
		sample.addEdge(level1dID, level2fID);
		String level2gID = sample.addNode("level2G");
		sample.addEdge(level1dID, level2gID);	
		
		String level3aID = sample.addNode("level3A");
		sample.addEdge(level2bID, level3aID);
		String level3bID = sample.addNode("level3B");
		sample.addEdge(level2bID, level3bID);	
		String level3cID = sample.addNode("level3C");
		sample.addEdge(level2bID, level3cID);	
		
		String level3dID = sample.addNode("level3D");
		sample.addEdge(level2fID, level3dID);	
		String level3eID = sample.addNode("level3E");
		sample.addEdge(level2fID, level3eID);	
		
		String level3fID = sample.addNode("level3F");
		sample.addEdge(level2gID, level3fID);
	}
	
	public Graph getGraph(){
		return sample;
	}
}

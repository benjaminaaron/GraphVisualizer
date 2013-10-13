package model;

public class SampleGraph {


    public static Graph getSampleGraph(int index) {
        if(index == 1)
            return Sample1();
        else
            return Sample2();
    }

    private static Graph Sample1(){
        Graph sample = new Graph();

        Node rootnode = new Node("", "rootnode");
        sample.addNode(rootnode);

        Node level1a = new Node("", "1a");
        sample.addNode(level1a);
        sample.addEdge(rootnode, level1a);
        Node level1b = new Node("", "1b");
        sample.addNode(level1b);
        sample.addEdge(rootnode, level1b);
        Node level1c = new Node("", "1c");
        sample.addNode(level1c);
        sample.addEdge(rootnode, level1c);

        Node level2a = new Node("", "2a");
        sample.addNode(level2a);
        sample.addEdge(level1a, level2a);
        Node level2b = new Node("", "2b");
        sample.addNode(level2b);
        sample.addEdge(level1a, level2b);

        Node level3a = new Node("", "3a");
        sample.addNode(level3a);
        sample.addEdge(level2b, level3a);

        return sample;
    }

    private static Graph Sample2(){
        Graph sample = new Graph();

        Node rootnode = new Node("", "rootnode");
        sample.addNode(rootnode);

        Node A1 = new Node("", "");
        sample.addNode(A1);
        sample.addEdge(rootnode, A1);

        Node B1 = new Node("", "");
        sample.addNode(B1);
        sample.addEdge(A1, B1);
        Node B2 = new Node("", "");
        sample.addNode(B2);
        sample.addEdge(A1, B2);
        Node B3 = new Node("", "");
        sample.addNode(B3);
        sample.addEdge(A1, B3);
        Node B4 = new Node("", "");
        sample.addNode(B4);
        sample.addEdge(A1, B4);
        Node B5 = new Node("", "");
        sample.addNode(B5);
        sample.addEdge(A1, B5);
        Node B6 = new Node("", "");
        sample.addNode(B6);
        sample.addEdge(A1, B6);

        Node A2 = new Node("", "");
        sample.addNode(A2);
        sample.addEdge(rootnode, A2);

        Node C1 = new Node("", "");
        sample.addNode(C1);
        sample.addEdge(A2, C1);
        Node C2 = new Node("", "");
        sample.addNode(C2);
        sample.addEdge(A2, C2);
        Node C3 = new Node("", "");
        sample.addNode(C3);
        sample.addEdge(A2, C3);

        Node A3 = new Node("", "");
        sample.addNode(A3);
        sample.addEdge(rootnode, A3);

        return sample;
    }
}

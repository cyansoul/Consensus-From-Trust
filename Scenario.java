package hw4;

import hw4.node.Node;
import hw4.node.NodeFactory;

import java.util.HashSet;

public class Scenario {
    Integer numRounds;
    Integer numNodes;
    boolean[][] trusted;
    boolean[] malicious;
    Node[] nodes;
    HashSet<Integer> validTxIds = new HashSet<Integer>();
    NodeFactory maliciousNodeFactory;
    Integer numMaliciousNodes;
    Double p_graph;
    Double p_txDistribution;
    Double p_malicious;

    //may be null
    public String graphFileName;

    //may be null
    public String txFileName;

    NodeFactory compliantNodeFactory;

    Scenario(NodeFactory compliantNodeFactory, NodeFactory maliciousNodeFactory, int numNodes) {
        this.compliantNodeFactory = compliantNodeFactory;
        this.maliciousNodeFactory = maliciousNodeFactory;
        this.numNodes = numNodes;
        this.nodes = new Node[numNodes];
        this.trusted = new boolean[numNodes][numNodes]; // trusted[i][j] is true iff i trusts j
        this.malicious = new boolean[numNodes];
    }

    public Integer getNumRounds() {
        return numRounds;
    }

    int getNumHonestNodes() {
        return numNodes - numMaliciousNodes;
    }

    public double getPGraph() {
        return p_graph;
    }

    public double getPMalicious() {
        return p_malicious;
    }

    public double getPTxDistribution() {
        return p_txDistribution;
    }
}

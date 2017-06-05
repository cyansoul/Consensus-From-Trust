package hw4;

import hw4.node.NodeFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

public class ScenarioGenerator {

    public static Scenario generate(NodeFactory compliantNodeFactory, NodeFactory maliciousNodeFactory, double p_graph, double p_malicious, double p_txDistribution, int numNodes, int numTx, int numRounds) {
        Random random = new Random();

        Scenario scenario = new Scenario(compliantNodeFactory, maliciousNodeFactory, numNodes);
        scenario.numMaliciousNodes = 0;
        scenario.numRounds = numRounds;

        // pick which node are malicious and which are compliant
        for (int i = 0; i < numNodes; i++) {
            boolean isMalicious = random.nextDouble() < p_malicious;
            if (isMalicious) {
                scenario.nodes[i] = maliciousNodeFactory.newNode(scenario);
                scenario.numMaliciousNodes++;
            } else {
                scenario.nodes[i] = compliantNodeFactory.newNode(scenario);
            }
            scenario.malicious[i] = isMalicious;
        }

        // initialize random follow graph
        for (int i = 0; i < numNodes; i++) {
            for (int j = 0; j < numNodes; j++) {
                if (i == j) continue;
                if(random.nextDouble() < p_graph) { // p_graph is .1, .2, or .3
                    scenario.trusted[i][j] = true;
                }
            }
        }

        // notify all node of their followees
        for (int i = 0; i < numNodes; i++)
            scenario.nodes[i].setTrustedNodes(Arrays.copyOf(scenario.trusted[i], scenario.trusted[i].length));

        // initialize a set of 500 valid Transactions with random ids

        for (int i = 0; i < numTx; i++) {
            int r = random.nextInt();
            scenario.validTxIds.add(r);
        }
        // distribute the 500 Transactions throughout the node, to initialize
        // the starting state of Transactions each node has heard. The distribution
        // is random with probability p_txDistribution for each Transaction-Node pair.
        for (int i = 0; i < numNodes; i++) {
            HashSet<Transaction> pendingTransactions = new HashSet<Transaction>();
            for(Integer txID : scenario.validTxIds) {
                if (random.nextDouble() < p_txDistribution) // p_txDistribution is .01, .05, or .10.
                    pendingTransactions.add(new Transaction(txID));
            }
            scenario.nodes[i].setInitialTransactions(Collections.unmodifiableSet(pendingTransactions));
        }

        return scenario;
    }
}

package hw4;

import hw4.node.NodeFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ScenarioParser  {
    private static final int NUM_TXS = 500;
    private static final int NUM_NODES = 100;

    public static Scenario parse(NodeFactory compliantNodeFactory, NodeFactory maliciousNodeFactory, String graphFilePath, String txFilePath, int numRounds) throws IOException {
        Scenario scenario = new Scenario(compliantNodeFactory, maliciousNodeFactory, NUM_NODES);
        scenario.numRounds = numRounds;

        File graphFile = new File(graphFilePath);
        File txFile = new File(txFilePath);

        scenario.graphFileName = graphFile.getName();
        String[] graphFileNameTokens = scenario.graphFileName.replace(".txt", "").split("_");
        scenario.p_graph = Double.parseDouble(graphFileNameTokens[0])/100.0;
        scenario.p_malicious = Double.parseDouble(graphFileNameTokens[1])/100.0;

        scenario.txFileName = txFile.getName();
        String[] txFileNameTokens = scenario.txFileName.replace(".txt", "").split("percent_");
        scenario.p_txDistribution = Double.parseDouble(txFileNameTokens[0])/100.0;

        BufferedReader br = new BufferedReader(new FileReader(graphFile));
        try {
            String[] line = br.readLine().split(" ");
            for(String s : line)
                scenario.malicious[Integer.parseInt(s)] = true;

            br.readLine();

            for(int i = 0; i < 100; i++) {
                line = br.readLine().split(" ");
                for(String s : line)
                    scenario.trusted[i][Integer.parseInt(s)] = true;
            }
        } finally {
            br.close();
        }

        scenario.numMaliciousNodes = 0;
        // pick which node are malicious and which are compliant
        for (int i = 0; i < NUM_NODES; i++) {
            if(scenario.malicious[i]) {
                scenario.nodes[i] = maliciousNodeFactory.newNode(scenario);
                scenario.numMaliciousNodes++;
            }
            else {
                scenario.nodes[i] = compliantNodeFactory.newNode(scenario);
            }
        }

        // notify all node of their followees
        for (int i = 0; i < NUM_NODES; i++)
            scenario.nodes[i].setTrustedNodes(Arrays.copyOf(scenario.trusted[i], scenario.trusted[i].length));

        HashMap<Integer, Set<Transaction>> allTx = new HashMap<Integer, Set<Transaction>>();
        for (int i = 0; i < NUM_NODES; i++)
            allTx.put(i, new HashSet());

        br = new BufferedReader(new FileReader(txFile));
        try {
            for(int i = 0; i < NUM_TXS; i++) {
                int txID = Integer.parseInt(br.readLine());
                scenario.validTxIds.add(txID);
                String[] line = br.readLine().split(" ");
                for(String s : line)
                    allTx.get(Integer.parseInt(s)).add(new Transaction(txID));
            }
        } finally {
            br.close();
        }

        for (int i = 0; i < NUM_NODES; i++) {
            scenario.nodes[i].setInitialTransactions(Collections.unmodifiableSet(allTx.get(i)));
        }

        return scenario;
    }
}

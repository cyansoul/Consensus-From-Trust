package hw4;

import hw4.node.Candidate;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.util.*;

public class ScenarioRunner {
    public Scenario scenario;

    public Integer maxNodesInConsensus;
    public Integer numTransactionsInConsensus;

    public ScenarioRunner(Scenario scenario) {
        this.scenario = scenario;
    }

    public ScenarioRunner run() {
        for (int round = 0; round < scenario.numRounds; round++) {
            runOneRound();
        }
        printResults();
        return this;
    }

    public void runOneRound() {
        // gather all the proposals into a map. The key is the index of the node receiving
        // proposals. The value is a Candidate object containing transaction being proposed and
        // the index # of the node proposing the transaction.
        HashMap<Integer, Set<Candidate>> allProposals = new HashMap();

        for (int i = 0; i < scenario.nodes.length; i++) {
            Set<Transaction> proposals = scenario.nodes[i].getProposalsForTrustingNodes();
            for (Transaction tx : proposals) {
                if (!scenario.validTxIds.contains(tx.id))
                    continue; // ensure that each tx is actually valid

                for (int j = 0; j < scenario.nodes.length; j++) {
                    if(!scenario.trusted[j][i]) continue; // tx only matters if j trusts i

                    Set<Candidate> candidateList = allProposals.get(j);
                    if (candidateList == null) {
                        candidateList = new HashSet();
                        allProposals.put(j, candidateList);
                    }

                    candidateList.add(new Candidate(tx, i));
                }
            }
        }

        // Distribute the Proposals to their intended recipients as Candidates
        for (int i = 0; i < scenario.nodes.length; i++) {
            if (allProposals.containsKey(i)) {
                scenario.nodes[i].receiveFromTrustedNodes(allProposals.get(i));
            }
        }
    }

    public void printResults() {
        System.out.println("Running test with parameters: malicious.type=" + scenario.maliciousNodeFactory.getClass().getName() + "  numNodes = " + scenario.numNodes + " p_graph=" + scenario.p_graph + " p_malicious=" + scenario.p_malicious + " p_txDistribution=" + scenario.p_txDistribution + " numRounds=" + scenario.numRounds +" #malicious=" + scenario.numMaliciousNodes);

        HashMap<Set<Transaction>, Integer> consensus = new HashMap();
        for (int i = 0; i < scenario.numNodes; i++) {
            if (scenario.malicious[i]) continue;
            Set<Transaction> transactions = new HashSet(scenario.nodes[i].getProposalsForTrustingNodes());
            Integer count = consensus.get(transactions);
            if (count == null) {
                count = 0;
            }
            count++;
            consensus.put(transactions, count);
        }

        Map.Entry<Set<Transaction>, Integer> max = Collections.max(consensus.entrySet(), new Comparator<Map.Entry<Set<Transaction>, Integer>>() {
            @Override
            public int compare(Map.Entry<Set<Transaction>, Integer> o1, Map.Entry<Set<Transaction>, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        maxNodesInConsensus = max.getValue();
        numTransactionsInConsensus = max.getKey().size();

        System.out.println(maxNodesInConsensus + " of " + (scenario.getNumHonestNodes()) + " honest nodes reach consensus on " + numTransactionsInConsensus + " transactions.\n");
    }

    public boolean areAllNodesInConsensus() {
        return maxNodesInConsensus == (scenario.getNumHonestNodes());
    }

    public boolean areAllTxsInConsensus() {
        return scenario.validTxIds.size() == numTransactionsInConsensus;
    }

    public ScenarioRunner writeStatistics(PrintWriter wr) {
        wr.println(StringUtils.join(getLine(), ", "));
        return this;
    }

    public static List<String> getHeaders() {
        return new ArrayList(Arrays.asList("MaliciousNode", "Graph File", "Tx File", "Num Nodes", "P_Graph", "P_Malicious", "P_TxDistribution", "Num Rounds", "Num Tx in Consensus", "Num Nodes in Consensus", "All Nodes in Consensus?", "All Tx in Consensus?"));
    }

    public List<String> getLine() {
        return Arrays.asList(scenario.maliciousNodeFactory.toString(), scenario.graphFileName, scenario.txFileName,
                Integer.toString(scenario.numNodes), Integer.toString((int) (scenario.p_graph*100)) + " %", Integer.toString((int) (scenario.p_malicious*100)) + " %", Integer.toString((int) (scenario.p_txDistribution*100)) + " %",
                Integer.toString(scenario.numRounds), Integer.toString(numTransactionsInConsensus), Integer.toString(maxNodesInConsensus),
                areAllNodesInConsensus() ? "Yes" : "No", areAllTxsInConsensus() ? "Yes" : "No");
    }
}

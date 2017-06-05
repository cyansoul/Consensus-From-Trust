package hw4;

import hw4.node.*;
import junit.framework.TestCase;
import org.apache.commons.lang3.StringUtils;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BasicTests extends TestCase {
    public List<NodeFactory> BASIC_COMPLIANT_NODE_FACTORIES = Arrays.asList(CompliantNode.FACTORY);
    public List<NodeFactory> BASIC_MALICIOUS_NODE_FACTORIES = Arrays.asList(MaliciousNode0.FACTORY, MaliciousNode1.FACTORY,  MaliciousNode3.FACTORY);

    public List<String> BASIC_GRAPH_FILES = Arrays.asList(
            "10_10_1",
            "10_20_1",
            "10_30_1",
            "10_40_1",
            "20_10_1",
            "20_20_1",
            "20_30_1",
            "30_10_1",
            "30_20_1",
            "30_30_1",
            "30_40_1"
    );

    public List<String> BASIC_TX_FILES = Arrays.asList(
            "1percent_1",
            "5percent_1",
            "10percent_1"
    );


    public void test0() throws Exception {
        PrintWriter wr = new PrintWriter(new FileWriter("test0_" + DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm").format(LocalDateTime.now()) + ".csv"));
        wr.println(StringUtils.join(ScenarioRunner.getHeaders(), ","));
        List<ScenarioRunner> runners = new ArrayList();
        for (NodeFactory cnf : BASIC_COMPLIANT_NODE_FACTORIES) {
            for (NodeFactory mnf : BASIC_MALICIOUS_NODE_FACTORIES) {
                for (String graphFile : BASIC_GRAPH_FILES) {
                    for (String txFile : BASIC_TX_FILES) {
                        Scenario scenario = ScenarioParser.parse(cnf, mnf, "files/" + graphFile + ".txt", "files/" + txFile + ".txt", 10);
                        ScenarioRunner runner = new ScenarioRunner(scenario);
                        runner.run();
                        runner.writeStatistics(wr);
                        runners.add(runner);
                    }
                }
            }
        }
        wr.println();
        writeStatistics(wr, runners);
        wr.close();
    }


    public void writeStatistics(PrintWriter wr, List<ScenarioRunner> runners) {
        int totalNodes = 0;
        int totalNodesInConsensus = 0;

        int totalTxs = 0;
        int totalTxsInConsensus = 0;

        int numScenarios_AllNodesInConsensus = 0;
        int numScenarios_AllTxsInConsensus = 0;
        for (ScenarioRunner r : runners) {
            if (r.areAllNodesInConsensus()) {
                numScenarios_AllNodesInConsensus++;
            }

            if (r.areAllTxsInConsensus()) {
                numScenarios_AllTxsInConsensus++;
            }

            totalTxs += r.scenario.validTxIds.size();
            totalTxsInConsensus += r.numTransactionsInConsensus;

            totalNodes += r.scenario.numNodes;
            totalNodesInConsensus += r.maxNodesInConsensus;
        }

        int rawScore = (int) (totalTxsInConsensus*100.0/totalTxs + totalNodesInConsensus*100.0/totalNodes)/2;

        wr.println("# Scenarios: Total=" + runners.size() +
                " AllNodesInConsensus=" +
                numScenarios_AllNodesInConsensus +
                " (" + (numScenarios_AllNodesInConsensus*100/runners.size()) + "%) " +
                " AllTxsInConsensus=" +
                numScenarios_AllTxsInConsensus +
                " (" + (numScenarios_AllTxsInConsensus*100/runners.size()) + "%)");

        wr.println("Raw Score: " + rawScore + "%");
    }
}

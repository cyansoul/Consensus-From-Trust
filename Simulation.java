// Example of a Simulation. This test runs the nodes on a random graph.
// At the end, it will print out the Transaction ids which each node
// believes consensus has been reached upon. You can use this simulation to
// test your node. You will want to try creating some deviant node and
// mixing them in the network to fully test.
package hw4;

import hw4.node.*;


public class Simulation {

   public static void main(String[] args) {
      NodeFactory cnf = CompliantNode.FACTORY;

      for (NodeFactory mf : new NodeFactory[] {MaliciousNode3.FACTORY}) {
         // cnf, mf, p_graph, p_malicious, p_txDistribution, numNodes, numTx, numRounds
          Scenario scenario = ScenarioGenerator.generate(cnf, mf, .1, .1, .01, 100, 500, 50);
         new ScenarioRunner(scenario).run();
      }
   }

}
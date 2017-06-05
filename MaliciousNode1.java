package hw4.node;

import hw4.*;

import java.util.HashSet;
import java.util.Set;

//This malicious node proposes a new, randomly selected set of nodes each round among the transactions it has received so far
public class MaliciousNode1 implements Node {
   public static final NodeFactory FACTORY = new NodeFactory() {
      @Override
      public Node newNode(Scenario scenario) {
         return new MaliciousNode1(scenario);
      }

      @Override
      public String toString() {
         return "MalOne";
      }
   };

   private Set<Transaction> union = new HashSet();
   private Scenario scenario;

   public MaliciousNode1(Scenario scenario) {
      this.scenario = scenario;
   }

   public void setTrustedNodes(boolean[] trustedNodeFlag) {

   }

   @Override
   public void setInitialTransactions(Set<Transaction> initialTransactions) {
      this.union.addAll(initialTransactions);
   }

   @Override
   public Set<Transaction> getProposalsForTrustingNodes() {
      HashSet<Transaction> randomTransactions = new HashSet<Transaction>();
      double p = .7; // take 70% of transactions
      for (Transaction tx : union) {
         if (Math.random() < p)
            randomTransactions.add(tx);
      }
      return randomTransactions;
   }

   @Override
   public void receiveFromTrustedNodes(Set<Candidate> candidates) {
      for(Candidate candidate : candidates) {
         this.union.add(candidate.tx);
      }
   }
}

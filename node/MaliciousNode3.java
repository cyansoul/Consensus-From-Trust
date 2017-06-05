package hw4.node;

import hw4.*;
import hw4.Transaction;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

// This malicious node only broadcasts its transactions once: two rounds before the end
public class MaliciousNode3 implements Node {
   public static final NodeFactory FACTORY = new NodeFactory() {
      @Override
      public Node newNode(Scenario scenario) {
         return new MaliciousNode3(scenario);
      }

      @Override
      public String toString() {
         return "MalOne";
      }
   };

   private int round = 0;
   private Set<Transaction> initialTransactions;
   private Scenario scenario;

   public MaliciousNode3(Scenario scenario) {
      this.scenario = scenario;
   }

   @Override
   public void setTrustedNodes(boolean[] trustedNodeFlag) {

   }

   public void setInitialTransactions(Set<Transaction> s) {
      this.initialTransactions = new HashSet(s);
   }

   @Override
   public Set<Transaction> getProposalsForTrustingNodes() {
      this.round++;

      if (this.round == scenario.getNumRounds() - 2) {
         return initialTransactions;
      } else {
         return Collections.EMPTY_SET;
      }
   }

   public void receiveFromTrustedNodes(Set<Candidate> candidates) {

   }


}

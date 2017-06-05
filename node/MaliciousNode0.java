package hw4.node;

import hw4.*;

import java.util.Collections;
import java.util.Set;

// This malicious node does nothing.
public class MaliciousNode0 implements Node {
    public static final NodeFactory FACTORY = new NodeFactory() {
        @Override
        public Node newNode(Scenario scenario) {
            return new MaliciousNode0(scenario);
        }

        @Override
        public String toString() {
            return "MalDoNothing";
        }
    };

    private Scenario scenario;

    public MaliciousNode0(Scenario scenario) {
    }

    @Override
    public void setTrustedNodes(boolean[] trustedNodeFlag) {

    }

    @Override
    public void setInitialTransactions(Set<Transaction> initialTransactions) {

    }

    @Override
    public Set<Transaction> getProposalsForTrustingNodes() {
        return Collections.EMPTY_SET;
    }

    @Override
    public void receiveFromTrustedNodes(Set<Candidate> candidates) {

    }
}

package hw4.node;

import hw4.Scenario;
import hw4.Transaction;

import java.util.HashSet;
import java.util.Set;

public class CompliantNode implements Node {
	public static final NodeFactory FACTORY = new NodeFactory() {
		@Override
		public Node newNode(Scenario scenario) {
			return new CompliantNode(scenario);
		}

		@Override
		public String toString() {
			return "CompliantNode";
		}
	};

	private boolean[] Nodes;
	private Set<Transaction> initialTx = new HashSet();
	private Scenario scenario;

	public CompliantNode(Scenario scenario) {
		this.scenario = scenario;
	}

	@Override
	public void setTrustedNodes(boolean[] trustedNodeFlag) {
		this.Nodes = trustedNodeFlag;
	}

	@Override
	public void setInitialTransactions(Set<Transaction> initialTransactions) {
		this.initialTx.addAll(initialTransactions);
	}

	@Override
	public Set<Transaction> getProposalsForTrustingNodes() {
		return initialTx;
	}

	@Override
	public void receiveFromTrustedNodes(Set<Candidate> candidates) {
          for (Candidate candidate : candidates) {
            initialTx.add(candidate.tx);
        }
	}
}
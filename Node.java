package hw4.node;

import hw4.Transaction;

import java.util.Set;

public interface Node {

    // {@code trustees[i]} is true if and only if this node trusts node {@code i}
    void setTrustedNodes(boolean[] trustedNodeFlag);

    // receive initial set of transactions
    void setInitialTransactions(Set<Transaction> initialTransactions);

    /**
     * @return proposals to send to nodes that trust this node.
     * REMEMBER: After final round, behavior of
     *         {@code getProposalsForTrustingNodes} changes and it should return
     *         the transactions upon which consensus has been reached.
     */
    Set<Transaction> getProposalsForTrustingNodes();

    //receive candidates from trusted nodes
    void receiveFromTrustedNodes(Set<Candidate> candidates);
}
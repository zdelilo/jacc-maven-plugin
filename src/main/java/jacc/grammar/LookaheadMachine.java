// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package jacc.grammar;

public abstract class LookaheadMachine extends Machine {

    public LookaheadMachine(Grammar grammar) {
        super(grammar);
    }

    public abstract int[] getLookaheadAt(int st, int i);
}

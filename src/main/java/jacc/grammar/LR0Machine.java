// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package jacc.grammar;

import jacc.util.BitSet;

public class LR0Machine extends LookaheadMachine {

    int[] allTokens;

    public LR0Machine(Grammar grammar) {
        super(grammar);
        int numTs = grammar.getNumTs();
        allTokens = BitSet.make(numTs);
        for (int i=0; i<numTs; i++) {
            BitSet.set(allTokens, i);
        }
    }

    public int[] getLookaheadAt(int st, int i) {
        return allTokens;
    }

    public void display(java.io.PrintWriter out) {
        super.display(out);
        out.print("Lookahead set is {");
        out.print(grammar.displaySymbolSet(allTokens, numNTs));
        out.println("}");
    }
}

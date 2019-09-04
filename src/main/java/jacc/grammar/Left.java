// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package jacc.grammar;

import jacc.util.BitSet;

public final class Left extends Analysis {
    private Grammar  grammar;
    private int      numNTs;
    private int[][]  left;

    public Left(Grammar grammar) {
        super(grammar.getComponents());
        this.grammar  = grammar;
        this.numNTs   = grammar.getNumNTs();
        left          = new int[numNTs][];
        for (int i=0; i<numNTs; i++) {
            left[i] = BitSet.make(numNTs);
            BitSet.set(left[i],i);
        }
        bottomUp();
    }

    protected boolean analyze(int c) {
        boolean changed = false;
        Grammar.Prod[] prods = grammar.getProds(c);
        for (int k=0; k<prods.length; k++) {
            int[] rhs = prods[k].getRhs();
            if (rhs.length>0 && grammar.isNonterminal(rhs[0])) {
                if (BitSet.addTo(left[c], left[rhs[0]])) {
                    changed = true;
                }
            }
        }
        return changed;
    }

    public int[] at(int i) {
        return left[i];
    }

    public void display(java.io.PrintWriter out) {
        out.println("Left nonterminal sets:");
        for (int i=0; i<left.length; i++) {
            out.print(" Left(" + grammar.getSymbol(i) + "): {");
            out.print(grammar.displaySymbolSet(left[i], 0));
            out.println("}");
        }
    }
}

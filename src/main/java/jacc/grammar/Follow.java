// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package jacc.grammar;

import jacc.util.BitSet;

public final class Follow extends Analysis {
    private Grammar  grammar;
    private Nullable nullable;
    private First    first;
    private int      numNTs;
    private int      numTs;
    private int[][]  follow;

    public Follow(Grammar grammar, Nullable nullable, First first) {
        super(grammar.getComponents());
        this.grammar  = grammar;
        this.nullable = nullable;
        this.first    = first;
        this.numNTs   = grammar.getNumNTs();
        this.numTs    = grammar.getNumTs();
        follow        = new int[numNTs][];
        for (int i=0; i<numNTs; i++) {
            follow[i] = BitSet.make(numTs);
        }
        BitSet.set(follow[0], numTs-1);
        topDown();
    }

    protected boolean analyze(int c) {
        boolean changed      = false;
        Grammar.Prod[] prods = grammar.getProds(c);
        for (int k=0; k<prods.length; k++) {
            int[] rhs = prods[k].getRhs();
            int   l   = 0;
            for (; l<rhs.length; l++) {
                if (grammar.isNonterminal(rhs[l])) {
                    int m = l+1;
                    for (; m<rhs.length; m++) {
                        if (grammar.isTerminal(rhs[m])) {
                            if (BitSet.addTo(follow[rhs[l]], rhs[m]-numNTs)) {
                                changed = true;
                            }
                            break;
                        } else {
                            if (BitSet.addTo(follow[rhs[l]],
                                             first.at(rhs[m]))) {
                                changed = true;
                            }
                            if (!nullable.at(rhs[m])) {
                                break;
                            }
                        }
                    }
                    if (m>=rhs.length) {
                        if (BitSet.addTo(follow[rhs[l]], follow[c])) {
                            changed = true;
                        }
                    }
                }
            }
        }
        return changed;
    }

    public int[] at(int i) {
        return follow[i];
    }

    public void display(java.io.PrintWriter out) {
        out.println("Follow sets:");
        for (int i=0; i<follow.length; i++) {
            out.print(" Follow(" + grammar.getSymbol(i) + "): {");
            out.print(grammar.displaySymbolSet(this.at(i), numNTs));
            out.println("}");
        }
    }
}

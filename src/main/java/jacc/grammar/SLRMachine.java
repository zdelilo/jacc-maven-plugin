// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package jacc.grammar;

import jacc.util.IntSet;

public class SLRMachine extends LookaheadMachine {
    // For convenience, we cache the following fields from grammar:
    private Follow follow;

    public SLRMachine(Grammar grammar) {
        super(grammar);
        this.follow = grammar.getFollow();
        calcLookahead();
    }


    private int[][][] laReds;

    public int[] getLookaheadAt(int st, int i) {
        return laReds[st][i];
    }

    private void calcLookahead() {
        laReds = new int[numStates][][];
        for (int i=0; i<numStates; i++) {
            IntSet its = getItemsAt(i);
            int[]  rs  = getReducesAt(i);
            laReds[i]  = new int[rs.length][];
            for (int j=0; j<rs.length; j++) {
                int lhs      = items.getItem(its.at(rs[j])).getLhs();
                laReds[i][j] = follow.at(lhs);
            }
        }
    }

    public void display(java.io.PrintWriter out) {
        super.display(out);
        for (int i=0; i<numStates; i++) {
            IntSet its = getItemsAt(i);
            int[]  rs  = getReducesAt(i);
            if (rs.length>0) {
                out.println("In state " + i + ":");
                for (int j=0; j<rs.length; j++) {
                    out.print(" Item: ");
                    items.getItem(its.at(rs[j])).display(out);
                    out.println();
                    out.print("  Lookahead: {");
                    out.print(grammar.displaySymbolSet(laReds[i][j], numNTs));
                    out.println("}");
                }
            }
        }
    }
}

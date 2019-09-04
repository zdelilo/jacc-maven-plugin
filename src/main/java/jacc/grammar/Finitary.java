// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package jacc.grammar;

final public class Finitary extends Analysis {
    private boolean[] finitary;
    private boolean[] consider;
    private Grammar   grammar;
    private int       numNTs;

    public Finitary(Grammar grammar) {
        super(grammar.getComponents());
        this.grammar = grammar;
        this.numNTs  = grammar.getNumNTs();
        finitary = new boolean[numNTs];
        consider = new boolean[numNTs];
        for (int i=0; i<numNTs; i++) {
            finitary[i] = false;
            consider[i] = true;
        }
        bottomUp();
    }

    protected boolean analyze(int c) {
        boolean changed = false;
        if (consider[c]) {
            int blocked = 0;
            Grammar.Prod[] prods = grammar.getProds(c);
            for (int k=0; k<prods.length; k++) {
                int[] rhs = prods[k].getRhs();
                int   l   = 0;
                while (l<rhs.length && this.at(rhs[l])) {
                    l++;
                }
                if (l>=rhs.length) {
                    finitary[c] = true;
                    consider[c] = false;
                    changed     = true;
                    break;
                } else if (!consider[rhs[l]]) {
                    blocked++;
                }
            }
            if (blocked==prods.length) {
                consider[c] = false;
            }
        }
        return changed;
    }

    public boolean at(int i) {
        return grammar.isTerminal(i) || finitary[i];
    }

    public void display(java.io.PrintWriter out) {
        out.print("Finitary = {");
        int count = 0;
        for (int i=0; i<numNTs; i++) {
            if (this.at(i)) {
                if (count>0) {
                    out.print(", ");
                }
                out.print(grammar.getSymbol(i).getName());
                count++;
            }
        }
        out.println("}");
    }
}

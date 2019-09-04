// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package jacc.grammar;

import jacc.util.BitSet;
import jacc.util.IntSet;
import jacc.util.SCC;
import jacc.util.Interator;

public class LALRMachine extends LookaheadMachine {
    // For convenience, we cache the following fields from grammar:
    protected Nullable nullable;
    protected First    first;

    public LALRMachine(Grammar grammar) {
        super(grammar);
        this.nullable = grammar.getNullable();
        this.first    = grammar.getFirst();
        predState     = SCC.invert(succState, numStates);
        calcGotoLA();
        calcLookahead();
    }

    private int[][] predState;

    private int numGotos;

    private int[] stateFirstGoto;

    private int[] gotoSource;

    private int[] gotoTrans;

    private int[][] gotoLA;

    private int[][] gotoTargets;

    private int[][][] laReds;

    public int[] getLookaheadAt(int st, int i) {
        return laReds[st][i];
    }

    private void calcGotoLA() {
        // Start by calculating the number of gotos, and storing them
        // in a table.
        stateFirstGoto = new int[numStates];
        numGotos       = 0;
        for (int st=0; st<numStates; st++) {
            stateFirstGoto[st] = numGotos;
            numGotos          += getGotosAt(st).length;
        }
        gotoSource  = new int[numGotos];
        gotoTrans   = new int[numGotos];
        int count   = 0;
        for (int st=0; st<numStates; st++) {
            int[] gotos = getGotosAt(st);
            for (int i=0; i<gotos.length; i++) {
                gotoSource[count] = st;
                gotoTrans[count]  = gotos[i];
                count++;
            }
        }

        // Now we calculate the targets and the immediate first
        // sets for each goto.
        gotoLA      = new int[numGotos][];
        gotoTargets = new int[numGotos][];
        for (int g=0; g<numGotos; g++) {
            calcTargets(g);
        }

        // Now we've identified the dependencies between gotos, sort
        // them into components and do a fix point iteration to get
        // the final lookaheads at each one.

        int[][] comps = SCC.get(gotoTargets);
        for (int c=0; c<comps.length; c++) {
            int[] comp = comps[c];
            boolean changed = true;
            while (changed) {
                changed = false;
                for (int i=0; i<comp.length; i++) {
                    int[] ts = gotoTargets[comp[i]];
                    for (int j=0; j<ts.length; j++) {
                        if (BitSet.addTo(gotoLA[comp[i]], gotoLA[ts[j]])) {
                            changed = true;
                        }
                    }
                }
            }
        }
    }

    private void calcTargets(int g) {
        int    st  = gotoSource[g];
        int    st1 = gotoTrans[g];
        int    nt  = getEntry(st1);
        IntSet its = getItemsAt(st1);
        int    sz  = its.size();
        int[]  fs  = BitSet.make(numTs);
        IntSet ts  = IntSet.empty();
        for (int j=0; j<sz; j++) {
            LR0Items.Item it  = items.getItem(its.at(j));
            int           lhs = it.getLhs();
            int           pos = it.getPos();
            if (lhs>=0) {
                int[] rhs = it.getProd().getRhs();
                if (pos>0 && rhs[--pos]==nt) {
                    if (calcFirsts(fs, it).canReduce()) {
                        findTargets(ts, st, lhs, rhs, pos);
                    }
                }
            } else if (pos>0) {
                BitSet.set(fs, numTs-1);
            }
        }
        gotoLA[g]      = fs;
        gotoTargets[g] = ts.toArray();
    }

    private LR0Items.Item calcFirsts(int[] fs, LR0Items.Item it) {
        while (it.canGoto()) {
            int sym = it.getNextSym();
            if (grammar.isTerminal(sym)) {
                BitSet.addTo(fs,sym-numNTs);
                break;
            } else {
                BitSet.union(fs, first.at(sym));
                if (!nullable.at(sym)) {
                    break;
                }
                it = items.getItem(it.getNextItem());
            }
        }
        if (it.canAccept()) {
            BitSet.set(fs,numTs-1);
        }
        return it;
    }

    private void findTargets(IntSet ts, int st, int lhs, int[] rhs, int pos) {
        if (pos==0) {
            int[] gotos = getGotosAt(st);
            for (int i=0; i<gotos.length; i++) {
                if (getEntry(gotos[i])==lhs) {
                    ts.add(stateFirstGoto[st]+i);
                    break;
                }
            }
        } else {
            if (entry[st]==rhs[--pos]) {
                for (int i=0; i<predState[st].length; i++) {
                    findTargets(ts, predState[st][i], lhs, rhs, pos);
                }
            }
        }
    }

    private void calcLookahead() {
        // Fill out the entries of laRed to record lookaheads for
        // reduce items in individual states.
        laReds = new int[numStates][][];
        for (int st=0; st<numStates; st++) {
            int[]  rs  = getReducesAt(st);
            IntSet its = getItemsAt(st);
            laReds[st] = new int[rs.length][];
            for (int j=0; j<rs.length; j++) {
                LR0Items.Item it = items.getItem(its.at(rs[j]));
                int   lhs        = it.getLhs();
                int[] rhs        = it.getProd().getRhs();
                int[] lookahead  = BitSet.make(numTs);
                lookBack(lookahead, st, lhs, rhs, rhs.length);
                laReds[st][j]    = lookahead;
            }
        }
    }

    private void lookBack(int[] la, int st, int lhs, int[] rhs, int pos) {
        if (pos==0) {
            int[] gotos = getGotosAt(st);
            for (int i=0; i<gotos.length; i++) {
                if (getEntry(gotos[i])==lhs) {
                    BitSet.union(la, gotoLA[stateFirstGoto[st]+i]);
                    return;
                }
            }
        } else {
            if (entry[st]==rhs[--pos]) {
                for (int i=0; i<predState[st].length; i++) {
                    lookBack(la, predState[st][i], lhs, rhs, pos);
                }
            }
        }
    }

    public void display(java.io.PrintWriter out) {
        super.display(out);

        // Display lookahead information for each goto.
        for (int g=0; g<numGotos; g++) {
            out.println("Goto #"+g
                        + ", in state "
                        + gotoSource[g]
                        + " on symbol "
                        + grammar.getSymbol(getEntry(gotoTrans[g]))
                        + " to state "
                        + gotoTrans[g]);
            out.print("  Lookahead: {");
            out.print(grammar.displaySymbolSet(gotoLA[g], numNTs));
            out.println("}");
            out.print("  Targets  : {");
            for (int j=0; j<gotoTargets[g].length; j++) {
                if (j>0) {
                    out.print(", ");
                }
                out.print(gotoTargets[g][j]);
            }
            out.println("}");
        }

        // Display lookahead information for each reduce item.
        for (int st=0; st<numStates; st++) {
            int[]  rs  = getReducesAt(st);
            if (rs.length>0) {
                out.println("State " + st + ": ");
                IntSet its = getItemsAt(st);
                for (int j=0; j<rs.length; j++) {
                    LR0Items.Item it = items.getItem(its.at(rs[j]));
                    out.print("  Item     : ");
                    it.display(out);
                    out.println();
                    out.print("  Lookahead: {");
                    out.print(grammar.displaySymbolSet(laReds[st][j],
                                                       numNTs));
                    out.println("}");
                }
            }
        }
    }
}

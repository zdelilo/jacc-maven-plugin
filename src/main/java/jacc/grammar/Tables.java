// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package jacc.grammar;

import jacc.util.IntSet;
import jacc.util.BitSet;
import jacc.util.Interator;

public class Tables {

    protected LookaheadMachine machine;

    protected Resolver resolver;

    // For convenience, we cache the following fields from the
    // underlying grammar:
    protected int numNTs;
    protected int numTs;

    public Tables(LookaheadMachine machine, Resolver resolver) {
        this.machine    = machine;
        this.resolver   = resolver;
        Grammar grammar = machine.getGrammar();
        this.numNTs     = grammar.getNumNTs();
        this.numTs      = grammar.getNumTs();
        int numStates   = machine.getNumStates();
        this.action     = new byte[numStates][];
        this.arg        = new int[numStates][];
        this.prodUsed   = new boolean[numNTs][];
        this.prodUnused = 0;
        for (int i=0; i<numNTs; i++) {
            prodUsed[i] = new boolean[grammar.getProds(i).length];
            prodUnused += prodUsed[i].length;
        }
        for (int i=0; i<numStates; i++) {
            fillTablesAt(i);
        }
    } 

    public final static byte NONE   = 0;

    public final static byte SHIFT  = 1;

    public final static byte REDUCE = 2;

    protected byte[][] action;

    protected int[][] arg;

    private boolean[][] prodUsed;

  
    private int         prodUnused;

    public LookaheadMachine getMachine() {
        return machine;
    }

    public byte[] getActionAt(int st) {
        return action[st];
    }

    public int[] getArgAt(int st) {
        return arg[st];
    }

    public int getProdUnused() {
        return prodUnused;
    }

    public boolean[] getProdsUsedAt(int nt) {
        return prodUsed[nt];
    }

    public void setShift(int st, int tok, int to) {
        action[st][tok] = SHIFT;
        arg[st][tok]    = to;
    }

    public void setReduce(int st, int tok, int num) {
        action[st][tok] = REDUCE;
        arg[st][tok]    = num;
    }

    private void fillTablesAt(int st) {
        action[st]   = new byte[numTs];      // all initialized to NONE
        arg[st]      = new int[numTs];
        int[] shifts = machine.getShiftsAt(st);
        int[] rs     = machine.getReducesAt(st);

        // Enter shifts into table.
        for (int i=0; i<shifts.length; i++) {
            setShift(st, machine.getEntry(shifts[i])-numNTs, shifts[i]);
        }
        // Enter reduces into table.
        for (int i=0; i<rs.length; i++) {
            Interator bts = BitSet.interator(machine.getLookaheadAt(st,i), 0);
            while (bts.hasNext()) {
                int tok = bts.next();
                switch (action[st][tok]) {
                    case NONE:
                        setReduce(st, tok, rs[i]);
                        break;
                    case SHIFT:
                        resolver.srResolve(this, st, tok, rs[i]);
                        break;
                    case REDUCE:
                        resolver.rrResolve(this, st, tok, rs[i]);
                        break;
                }
            }
        }

        // Register which productions are actually used
        LR0Items items = machine.getItems();
        IntSet   its   = machine.getItemsAt(st);
        for (int i=0; i<rs.length; i++) {
            for (int j=0; j<numTs; j++) {
                if (action[st][j]==REDUCE && arg[st][j]==rs[i]) {
                    // Under normal circumstances, every reduction
                    // will be used at least once ... however, it is
                    // possible that uses of a reduce step in the machine
                    // have been eliminated when a conflict was resolved.
                    LR0Items.Item it = items.getItem(its.at(rs[i]));
                    int lhs    = it.getLhs();
                    int prodNo = it.getProdNo();
                    if (!prodUsed[lhs][prodNo]) {
                        prodUsed[lhs][prodNo] = true;
                        prodUnused--;
                    }
                    break;
                }
            }
        }
    }
}

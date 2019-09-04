// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package jacc;

import jacc.grammar.Grammar;
import jacc.grammar.LR0Items;
import jacc.grammar.LookaheadMachine;
import jacc.grammar.Resolver;
import jacc.grammar.Tables;
import jacc.util.IntSet;

public class JaccResolver extends Resolver {
    private LookaheadMachine machine;

    public JaccResolver(LookaheadMachine machine) {
        this.machine = machine;
        conflicts    = new Conflicts[machine.getNumStates()];
    }

    private int numSRConflicts = 0;

    private int numRRConflicts = 0;

    private Conflicts[] conflicts;

    public int getNumSRConflicts() {
        return numSRConflicts;
    }

    public int getNumRRConflicts() {
        return numRRConflicts;
    }

    public String getConflictsAt(int st) {
        return Conflicts.describe(machine, st, conflicts[st]);
    }

    public void srResolve(Tables tables, int st, int tok, int redNo) {
        Grammar        grammar = machine.getGrammar();
        Grammar.Symbol sym     = grammar.getTerminal(tok);
        IntSet         its     = machine.getItemsAt(st);
        LR0Items       items   = machine.getItems();
        Grammar.Prod   prod    = items.getItem(its.at(redNo)).getProd();

        if ((sym instanceof JaccSymbol) && (prod instanceof JaccProd)) {
            JaccSymbol jsym  = (JaccSymbol)sym;
            JaccProd   jprod = (JaccProd)prod;
            switch (Fixity.which(jprod.getFixity(), jsym.getFixity())) {
                case Fixity.LEFT:   // Choose reduce
                    tables.setReduce(st, tok, redNo);
                    return;
                case Fixity.RIGHT:  // Choose shift, which is already in
                    return;         // the table, so nothing more to do.
            }
        }
        conflicts[st]
            = Conflicts.sr(tables.getArgAt(st)[tok], redNo, sym, conflicts[st]);
        numSRConflicts++;
    }

    public void rrResolve(Tables tables, int st, int tok, int redNo) {
        Grammar        grammar = machine.getGrammar();
        int            redNo0  = tables.getArgAt(st)[tok];
        IntSet         its     = machine.getItemsAt(st);
        LR0Items       items   = machine.getItems();
        Grammar.Prod   prod0   = items.getItem(its.at(redNo0)).getProd();
        Grammar.Prod   prod    = items.getItem(its.at(redNo)).getProd();
        Grammar.Symbol sym     = grammar.getTerminal(tok);

        if (prod.getSeqNo()<prod0.getSeqNo()) {
            tables.setReduce(st, tok, redNo);
        }
        conflicts[st] = Conflicts.rr(redNo0, redNo, sym, conflicts[st]);
        numRRConflicts++;
    }
}

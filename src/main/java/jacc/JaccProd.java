// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package jacc;

import jacc.grammar.Grammar;
import compiler.Position;


public class JaccProd extends Grammar.Prod {
    private Fixity       fixity;
    private JaccSymbol[] prodSyms;
    private Position     actPos;
    private String       action;


    public JaccProd(Fixity fixity, JaccSymbol[] prodSyms,
                    Position actPos, String action, int seqNo) {
        super(new int[prodSyms.length], seqNo);
        this.fixity   = fixity;
        this.prodSyms = prodSyms;
        this.actPos   = actPos;
        this.action   = action;
    }

    public String getLabel() {
        return Integer.toString(getSeqNo());
    }

    public void fixup() {
        int[] rhs = getRhs();
        for (int i=0; i<prodSyms.length; i++) {
            rhs[i] = prodSyms[i].getTokenNo();
        }
    }

    public Fixity getFixity() {
        if (fixity==null) {
            for (int i=prodSyms.length-1; i>=0; i--) {
                Fixity f = prodSyms[i].getFixity();
                if (f!=null) {
                    return f;
                }
            }
        }
        return fixity;
    }

    public Position getActionPos() {
        return actPos;
    }

    public String getAction() {
        return action;
    }
}

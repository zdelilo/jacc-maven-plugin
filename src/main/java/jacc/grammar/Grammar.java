// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package jacc.grammar;

import jacc.util.SCC;
import jacc.util.BitSet;
import jacc.util.Interator;

public class Grammar {

    public static class Symbol {
        protected String name;
        public Symbol(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
        public String toString() {
            return name;
        }
    }

    public static class Prod {
        protected int[] rhs;
        private   int   seqNo;
        public Prod(int[] rhs, int seqNo) {
            this.rhs   = rhs;
            this.seqNo = seqNo;
        }
        public int[] getRhs() {
            return rhs;
        }
        public int getSeqNo() {
            return seqNo;
        }
        public String getLabel() {
            return null;
        }
    }

    private Symbol[]  symbols;

    private Prod[][]  prods;

    public Grammar(Symbol[] symbols, Prod[][] prods)
      throws Exception {
        validate(symbols, prods);
        this.symbols = symbols;
        numSyms      = symbols.length;
        this.prods   = prods;
        numNTs       = prods.length;
        numTs        = numSyms - numNTs;
        calcDepends();
        comps        = SCC.get(depends, revdeps,numNTs);
    }

    private int numSyms;

    private int numNTs;

    private int numTs;

    private int[][]   comps;

    public int getNumSyms() {
        return numSyms;
    }

    public int getNumNTs() {
        return numNTs;
    }

    public int getNumTs() {
        return numTs;
    }

    public Symbol getSymbol(int i) {
        return symbols[i];
    }

    public Symbol getStart() {
        return symbols[0];
    }

    public Symbol getEnd() {
        return symbols[numSyms-1];
    }

    public Symbol getNonterminal(int i) {
        return symbols[i];
    }

    public Symbol getTerminal(int i) {
        return symbols[numNTs+i];
    }

    public boolean isNonterminal(int n) {
        return 0<=n && n<numNTs;
    }

    public boolean isTerminal(int n) {
        return numNTs<=n && n<numSyms;
    }

    public int getNumProds() {
        int tot = 0;
        for (int i=0; i<prods.length; i++) {
            tot += prods[i].length;
        }
        return tot;
    }

    public Prod[] getProds(int i) {
        return prods[i];
    }

    public int[][] getComponents() {
        return comps;
    }

    public static void validate(Symbol[] symbols, Prod[][] prods)
      throws Exception {
        //-----------------------------------------------------------------
        // Validate symbols:

        if (symbols==null || symbols.length==0) {
            throw new Exception("No symbols specified");
        }
        for (int i=0; i<symbols.length; i++) {
            if (symbols[i]==null) {
                throw new Exception("Symbol " + i + " is null");
            }
        }
        int numSyms = symbols.length;

        //-----------------------------------------------------------------
        // Validate productions:

        if (prods==null || prods.length==0) {
            throw new Exception("No nonterminals specified");
        }
        if (prods.length>numSyms) {
            throw new Exception("To many nonterminals specified");
        }
        if (prods.length==numSyms) {
            throw new Exception("No terminals specified");
        }
        for (int i=0; i<prods.length; i++) {
            if (prods[i]==null || prods[i].length==0) {
                throw new Exception("Nonterminal " + symbols[i] +
                                    " (number " + i + ") has no productions");
            }
            for (int j=0; j<prods[i].length; j++) {
                int[] rhs = prods[i][j].getRhs();
                if (rhs==null) {
                    throw new Exception("Production " +
                                        j + " for symbol " + symbols[i] +
                                        " (number " + i + ") is null");
                }
                for (int k=0; k<rhs.length; k++) {
                    if (rhs[k]<0 || rhs[k]>=numSyms-1) {
                        throw new Exception("Out of range symbol " + rhs[k] +
                                            " in production " + j +
                                            " for symbol " + symbols[i] +
                                            " (number " + i + ")");
                    }
                }
            }
        }
    }

    //---------------------------------------------------------------------
    // Dependency calculations:

    private int[][] depends;

    private int[][] revdeps;

    private void calcDepends() {
        int[][] deps = new int[numNTs][];
        int[]   nts  = BitSet.make(numNTs);
        depends      = new int[numNTs][];

        for (int i=0; i<numNTs; i++) {
            deps[i] = BitSet.make(numNTs);
        }
        for (int i=0; i<numNTs; i++) {
            BitSet.clear(nts);
            for (int j=0; j<prods[i].length; j++) {
                int[] rhs = prods[i][j].getRhs();
                for (int k=0; k<rhs.length; k++) {
                    if (isNonterminal(rhs[k])) {
                        BitSet.set(deps[rhs[k]], i);
                        BitSet.set(nts, rhs[k]);
                    }
                }
            }
            depends[i] = BitSet.members(nts);
        }

        revdeps = new int[numNTs][];
        for (int i=0; i<numNTs; i++) {
            revdeps[i] = BitSet.members(deps[i]);
        }
    }

    //---------------------------------------------------------------------
    // Cache points for standard analyses:

    private Nullable nullable;

    public Nullable getNullable() {
        if (nullable==null) {
            nullable = new Nullable(this);
        }
        return nullable;
    }

    private Finitary finitary;

    public Finitary getFinitary() {
        if (finitary==null) {
            finitary = new Finitary(this);
        }
        return finitary;
    }

    private Left left;

    public Left getLeft() {
        if (left==null) {
            left = new Left(this);
        }
        return left;
    }

    private First first;

    public First getFirst() {
        if (first==null) {
            first = new First(this, getNullable());
        }
        return first;
    }

    private Follow follow;

    public Follow getFollow() {
        if (follow==null) {
            follow = new Follow(this, getNullable(), getFirst());
        }
        return follow;
    }

    //---------------------------------------------------------------------
    // Display utilities:

    public void display(java.io.PrintWriter out) {
        for (int i=0; i<numNTs; i++) {
            out.println(symbols[i].getName());
            String punc = " = ";
            for (int j=0; j<prods[i].length; j++) {
                int[] rhs = prods[i][j].getRhs();
                out.print(punc);
                out.print(displaySymbols(rhs, "/* empty */", " "));
                out.println();
                punc = " | ";
            }
            out.println(" ;");
        }
    }

    public void displayAnalyses(java.io.PrintWriter out) {
        if (nullable==null) {
            out.println("No nullable analysis");
        } else {
            nullable.display(out);
        }
        if (finitary==null) {
            out.println("No finitary analysis");
        } else {
            finitary.display(out);
        }
        if (left==null) {
            out.println("No left analysis");
        } else {
            left.display(out);
        }
        if (first==null) {
            out.println("No first analysis");
        } else {
            first.display(out);
        }
        if (follow==null) {
            out.println("No follow analysis");
        } else {
            follow.display(out);
        }
    }

    public void displayDepends(java.io.PrintWriter out) {
        out.println("Dependency information:");
        for (int i=0; i<numNTs; i++) {
            out.print(" " + symbols[i] + ": calls {");
            out.print(displaySymbols(depends[i],"",", "));
            out.print("}, called from {");
            out.print(displaySymbols(revdeps[i],"",", "));
            out.println("}");
        }
    }

    public String displaySymbols(int[] syms, String empty, String between) {
        return displaySymbols(syms, 0, syms.length, empty, between);
    }

    public String displaySymbols(int[] syms, int lo, int hi,
                                 String empty, String between) {
        if (syms==null || lo>=hi) {
            return empty;
        } else {
            StringBuffer buf = new StringBuffer();
            buf.append(symbols[syms[lo]].getName());
            for (int k=lo+1; k<hi; k++) {
                buf.append(between);
                buf.append(symbols[syms[k]].getName());
            }
            return buf.toString();
        }
    }

    public String displaySymbolSet(int[] s, int offset) {
        StringBuffer buf = new StringBuffer();
        int count        = 0;
        Interator mems   = BitSet.interator(s, offset);
        while (mems.hasNext()) {
            if (count++ != 0) {
                buf.append(", ");
            }
            buf.append(symbols[mems.next()].getName());
        }
        return buf.toString();
    }
}

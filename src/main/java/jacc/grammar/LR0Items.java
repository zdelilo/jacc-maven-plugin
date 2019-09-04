// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package jacc.grammar;

public class LR0Items {

    private Grammar grammar;

    public LR0Items(Grammar grammar) {
        this.grammar = grammar;
        int numNTs   = grammar.getNumNTs();
        numItems     = 2;               // Number of special items
        firstKernel  = new int[numNTs][];
        for (int i=0; i<numNTs; i++) {
            Grammar.Prod[] prods = grammar.getProds(i);
            firstKernel[i]       = new int[prods.length];
            for (int j=0; j<prods.length; j++) {
                int len           = prods[j].getRhs().length;
                firstKernel[i][j] = numItems;
                numItems         += (len==0 ? 1 : len);
            }
        }
        items       = new Item[numItems];
        numItems    = 0;
        new Item(-1,0,0);                   // Represents S' -> _ S $
        new Item(-1,0,1);                   // Represents S' -> S _ $
        for (int i=0; i<numNTs; i++) {
            Grammar.Prod[] prods = grammar.getProds(i);
            for (int j=0; j<prods.length; j++) {
                int[] rhs = prods[j].getRhs();
                for (int k=1; k<rhs.length; k++) {
                    new Item(i, j, k);
                }
                new Item(i, j, rhs.length);
            }
        }
    }

    private int numItems;

    private Item[] items;

    private int[][] firstKernel;

    public int getNumItems() {
        return numItems;
    }

    public Item getItem(int i) {
        return items[i];
    }

    public int getStartItem() {
        return 0;
    }

    public int getEndItem() {
        return 1;
    }

    public int getFirstKernel(int symNo, int prodNo) {
        return firstKernel[symNo][prodNo];
    }

    public void displayAllItems(java.io.PrintWriter out) {
        out.println("Items:");
        for (int i=0; i<items.length; i++) {
            out.print(i + ": ");
            items[i].display(out);
            out.println();
        }
    }

    public class Item {
        private int itemNo;
        private int lhs;
        private int prodNo;
        private int pos;

        private Item(int lhs, int prodNo, int pos) {
            this.itemNo       = numItems;
            this.lhs          = lhs;
            this.prodNo       = prodNo;
            this.pos          = pos;
            items[numItems++] = this;
        }

        public int getNo() {
            return itemNo;
        }

        public int getLhs() {
            return lhs;
        }

        public int getProdNo() {
            return prodNo;
        }

        public int getSeqNo() {
            return getProd().getSeqNo();
        }

        public Grammar.Prod getProd() {
            return grammar.getProds(lhs)[prodNo];
        }

        public int getPos() {
            return pos;
        }

        public boolean canGoto() {
            if (lhs<0) {
                return (pos==0);
            } else {
                return (pos!=getProd().getRhs().length);
            }
        }

        public boolean canReduce() {
            return (lhs>=0) && (pos==getProd().getRhs().length);
        }

        public boolean canAccept() {
            return (lhs<0) && (pos==1);
        }

        public int getNextItem() {
            if (lhs>=0) {
                return itemNo+1;
            } else {
                return 1;
            }
        }

        public int getNextSym() {
            if (lhs>=0) {
                return grammar.getProds(lhs)[prodNo].getRhs()[pos];
            } else {
                return 0;                   // the start symbol
            }
        }
        
        public void display(java.io.PrintWriter out) {
            if (lhs<0) {
                if (pos==0) {
                    out.print("$accept : _" + grammar.getStart() +
                              " " + grammar.getEnd());
                } else {
                    out.print("$accept : " + grammar.getStart() +
                              "_" + grammar.getEnd());
                }
                return;
            }
            out.print(grammar.getSymbol(lhs));
            out.print(" : ");
            Grammar.Prod prod = grammar.getProds(lhs)[prodNo];
            int[]        rhs  = prod.getRhs();
            out.print(grammar.displaySymbols(rhs, 0, pos, "", " "));
            out.print("_");
            if (pos<rhs.length) {
                out.print(grammar.displaySymbols(rhs, pos, rhs.length, "", " "));
            }
            String label = prod.getLabel();
            if (label!=null) {
                out.print("    (");
                out.print(label);
                out.print(')');
            }
        }
    }
}

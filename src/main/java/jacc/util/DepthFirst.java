// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package jacc.util;

public abstract class DepthFirst {
    private   Interator seq;
    protected int[][]   adjs;
    private   int[]     visited;
    DepthFirst(Interator seq, int[][] adjs) {
        this.seq  = seq;
        this.adjs = adjs;
        visited   = BitSet.make(adjs.length);
    }

    protected void search() {
        while (seq.hasNext()) {
            if (visit(seq.next())) {
                doneTree();
            }
        }
    }

    private boolean visit(int i) {
        if (BitSet.addTo(visited,i)) {
            int[] adj = adjs[i];
            for (int j=0; j<adj.length; j++) {
                visit(adj[j]);
            }
            doneVisit(i);
            return true;
        } else {
            return false;
        }
    }

    void doneVisit(int i) {
        // Do nothing
    }

    void doneTree() {
        // Do nothing
    }
}

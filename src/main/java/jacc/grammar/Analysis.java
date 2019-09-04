// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package jacc.grammar;

public abstract class Analysis {

    private int[][] comps;

    protected Analysis(int[][] comps) {
        this.comps = comps;
    }

    protected void bottomUp() {
        for (int i=0; i<comps.length; i++) {
            analyzeComponent(comps[i]);
        }
    }

    protected void topDown() {
        for (int i=comps.length; i-- >0; ) {
            analyzeComponent(comps[i]);
        }
    }

    private void analyzeComponent(int[] comp) {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int j=0; j<comp.length; j++) {
                changed |= analyze(comp[j]);
            }
        }
    }

    protected abstract boolean analyze(int i);
}

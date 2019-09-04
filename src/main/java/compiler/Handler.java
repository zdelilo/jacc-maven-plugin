// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package compiler;

public abstract class Handler {

    private int numDiagnostics = 0;
    public int getNumDiagnostics() {
        return numDiagnostics;
    }


    private int numFailures = 0;
    public int getNumFailures() {
        return numFailures;
    }


    public void report(Diagnostic d) {
        numDiagnostics++;
        if (d instanceof Failure) {
            numFailures++;
        }
        respondTo(d);
    }

    protected abstract void respondTo(Diagnostic d);

    public void reset() {
        numDiagnostics = 0;
        numFailures    = 0;
    }
}


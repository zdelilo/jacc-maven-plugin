// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package compiler;

public class SimpleHandler extends Handler {

    protected void respondTo(Diagnostic d) {
        Position pos  = d.getPos();
        if (d instanceof Warning) {
            System.err.print("WARNING: ");
        } else {
            System.err.print("ERROR: ");
        }
        if (pos!=null) {
            System.err.println(pos.describe());
        }
        String txt = d.getText();
        if (txt!=null) {
            System.err.println(txt);
        }
        System.err.println();
    }
}

// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package compiler;


public abstract class Source extends Phase {

    public Source(Handler handler) {
        super(handler);
    }


    public abstract String describe();


    public abstract String readLine();


    public abstract int getLineNo();


    public String getLine(int lineNo) {
        return null;
    }


    public void close() {
        // Do nothing!
    }
}

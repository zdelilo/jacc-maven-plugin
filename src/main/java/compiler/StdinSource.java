// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package compiler;


public class StdinSource extends Source {

    public StdinSource(Handler handler) {
        super(handler);
    }

 
    public String describe() {
        return "standard input";
    }


    private boolean foundEOF = false;


    private int lineNumber = 0;


    private StringBuffer buf = new StringBuffer();


    public String readLine() {
        if (foundEOF) {
            return null;
        }
        lineNumber++;
        buf.setLength(0);
        for (;;) {
            int c = 0;
            try {
                c = System.in.read();
            } catch (Exception e) {
                report(new Failure("Error in input stream"));
            }
            if (c=='\n') {
                break;
            } else if (c<0) {
                foundEOF = true;
                break;
            }
            buf.append((char)c);
        }
        return buf.toString();
    }

    public int getLineNo() {
        return lineNumber;
    }
}

// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package jacc;

import compiler.Handler;
import compiler.Source;


public class DebugLexer extends JaccLexer {
    public DebugLexer(Handler handler, Source source) {
        super(handler, source);
    }

    public int nextToken() {
        int tok = super.nextToken();
        System.out.println("Token " + tok + " >" + getLexeme() + "<");
        return tok;
    }
}

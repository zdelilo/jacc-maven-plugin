// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package compiler;


public abstract class Phase {
    private Handler handler;

 
    protected Phase(Handler handler) {
        this.handler = handler;
    }

   
    public Handler getHandler() {
        return handler;
    }

    
    public void report(Diagnostic d) {
        if (handler!=null) {
            handler.report(d);
        }
    }
}

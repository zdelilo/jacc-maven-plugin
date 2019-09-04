// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package compiler;

public abstract class Diagnostic extends Exception {
    private String text;
    public String getText() {
        return text;
    }
    private Position position;
    public Position getPos() {
        return position;
    }


    protected String crossRef;
    public String getCrossRef() {
        return null;
    }

 
    public Diagnostic(String text) {
        this.text = text;
    } 


    public Diagnostic(Position position) {
        this.position = position;
    } 

    public Diagnostic(Position position, String text) {
        this.position = position;
        this.text     = text;
    } 
}

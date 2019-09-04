// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package compiler;

public class Failure extends Diagnostic {
    public Failure(String text) {
        super(text);
    } 

    public Failure(Position position) {
        super(position);
    } 

  
    public Failure(Position position, String text) {
        super(position, text);
    } 
}

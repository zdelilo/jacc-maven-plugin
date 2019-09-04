// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package compiler;


public class Warning extends Diagnostic {

    public Warning(String text) {
        super(text);
    } 


    public Warning(Position position) {
        super(position);
    } 


    public Warning(Position position, String text) {
        super(position, text);
    } 
}

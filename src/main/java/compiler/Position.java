// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package compiler;

public abstract class Position {

    public abstract String describe();

    public int getColumn() {
        return 0;
    }

    public int getRow() {
        return 0;
    }

    public abstract Position copy();
}

// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package compiler;


public class SourcePosition extends Position {
    private Source source;
    private int    row;
    private int    column;

    public SourcePosition(Source source, int row, int column) {
        this.source = source;
        this.row    = row;
        this.column = column;
    }

    public SourcePosition(Source source) {
        this(source, 0, 0);
    }


    public Source getSource() {
        return source;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void updateCoords(int row, int column) {
        this.row    = row;
        this.column = column;
    }

    public String describe() {
        StringBuffer buf = new StringBuffer();
        if (source!=null) {
            buf.append('"');
            buf.append(source.describe());
            buf.append('"');
            if (row>0) {
                buf.append(", ");
            }
         }
         if (row>0) {
            buf.append("line ");
            buf.append(row);
        }
        String line = source.getLine(row);
        if (line!=null) {
            buf.append('\n');
            buf.append(line);
            buf.append('\n');
            for (int i=0; i<column; i++) {
                buf.append(' ');
            }
            buf.append('^');
        }
        return (buf.length()==0) ? "input" : buf.toString();
    }

   
    public Position copy() {
        return new SourcePosition(source, row, column);
    }
}

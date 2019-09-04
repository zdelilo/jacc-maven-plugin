// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package compiler;

import java.io.Reader;
import java.io.IOException;

public class JavaSource extends Source {
    private Reader input;
    private int    tabwidth;
    private String description;

    private final static int DEFAULT_TABWIDTH = 8;

    public JavaSource(Handler handler,
                      String description,
                      Reader input,
                      int tabwidth) {
        super(handler);
        this.description = description;
        this.input       = input;
        this.tabwidth    = tabwidth;
    }

    public JavaSource(Handler handler,
                      String description,
                      Reader input) {
        this(handler, description, input, DEFAULT_TABWIDTH);
    }

    public JavaSource(String description, Reader input) {
        this(null, description, input);
    }

    public String describe() {
        return description;
    }

    // ---------------------------------------------------------------------
    // Character level input: eliminate trailing ^Z at EOF

    private int c0;                             // First input character
    private int c1 = 0;                         // Lookahead
    private int lineNumber = 0;                 // Line number

    private void skip() throws IOException {
        c0 = c1;
        if (c0!=(-1)) {
            c1 = input.read();
            if (c0==26 && c1==(-1)) {
                c0 = c1;
            }
        }
    }

    // ---------------------------------------------------------------------
    // Line level input: deal with unicode escapes and separate lines

    private StringBuffer buf;               // Buffer for input lines

    public String readLine() {
        if (input==null) {                  // Return null when done
            return null;
        }

        if (buf==null) {                    // Allocate or clear buffer
            buf = new StringBuffer();
        } else {
            buf.setLength(0);
        }

        try {
            if (lineNumber++ == 0) {        // Set lookahead character
                skip();                     // for first input line.
                skip();
            }
            if (c0==(-1)) {                 // File ends at the beginning
                // TODO: is it really safe to close here (or to omit the
                // close altogether)?  The issue is that we don't want the
                // lines of text for this source to be thrown away prematurely.
                //close();
                return null;                // of a line?
            }

            while (c0!=(-1) && c0!='\n' && c0!='\r') {
                if (c0=='\\') {
                    skip();
                    if (c0=='u') {          // Unicode escapes
                        do {
                            skip();
                        } while (c0=='u');
                        int n = 0;
                        int i = 0;
                        int d = 0;
                        while (i<4 && c0!=(-1) &&
                               (d=Character.digit((char)c0,16))>=0) {
                            n  = (n<<4) + d;
                            i++;
                            skip();
                        }
                        if (i!=4) {
                            report(new Warning(
                                "Error in Unicode escape sequence"));
                        } else {
                            buf.append((char)n);
                        }
                    } else {
                        buf.append('\\');
                        if (c0==(-1)) {
                            break;
                        } else {
                            buf.append((char)c0);
                        }
                        skip();
                    }
                } else if (c0=='\t' && tabwidth>0) {  // Expand tabs
                    int n = tabwidth - (buf.length() % tabwidth);
                    for (; n>0; n--) {
                        buf.append(' ');
                    }
                    skip();
                } else {
                    buf.append((char)c0);
                    skip();
                }
            }
            if (c0=='\r') {        // Skip CR, LF, CRLF
                skip();
            }
            if (c0 =='\n') {
                skip();
            }
        } catch (IOException e) {
            close();
        }

        return buf.toString();
    }

    public int getLineNo() {
        return lineNumber;
    }

    public void close() {
        if (input!=null) {
            try {
                input.close();
            }
            catch (IOException e) {
                // Should I complain?
            }
            input = null;
            buf   = null;
        }
    }
}

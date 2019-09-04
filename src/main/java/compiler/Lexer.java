// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package compiler;

public abstract class Lexer extends Phase {
    protected int    token;
    protected String lexemeText;

    public Lexer(Handler handler) {
        super(handler);
    }

    public Lexer(Handler handler, int firstToken) {
        super(handler);
        token = firstToken;
    }

    public abstract int nextToken();

    public int getToken() {
        return token;
    }

    public String getLexeme() {
        return lexemeText;
    }

    public abstract Position getPos();

    public boolean match(int token) {
        if (token==this.token) {
            nextToken();
            return true;
        }
        return false;
    }

    public abstract void close();
}

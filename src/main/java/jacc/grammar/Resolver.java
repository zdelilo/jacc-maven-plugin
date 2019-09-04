// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package jacc.grammar;

public abstract class Resolver {

    public abstract void srResolve(Tables tables, int st, int tok, int redNo);

    public abstract void rrResolve(Tables tables, int st, int tok, int redNo);
}

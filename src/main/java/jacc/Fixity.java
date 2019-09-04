// Copyright (c) Mark P Jones, OGI School of Science & Engineering
// Subject to conditions of distribution and use; see LICENSE for details
// April 24 2004 01:01 AM
// 

package jacc;

public class Fixity {

    public static final int LEFT   = 1;


    public static final int NONASS = 2;


    public static final int RIGHT  = 3;


    private int assoc;


    private int prec;

    private Fixity(int assoc, int prec) {
        this.assoc = assoc;
        this.prec  = prec;
    }


    public static Fixity left(int prec) {
        return new Fixity(LEFT, prec);
    }


    public static Fixity nonass(int prec) {
        return new Fixity(NONASS,prec);
    }


    public static Fixity right(int prec) {
        return new Fixity(RIGHT,prec);
    }


    public int getAssoc() {
        return assoc;
    }

   
    public int getPrec() {
        return prec;
    }


    public static int which(Fixity l, Fixity r) {
        if (l!=null && r!=null) {
            if (l.prec > r.prec) {
                return LEFT;
            } else if (l.prec < r.prec) {
                return RIGHT;
            } else if (l.assoc==LEFT && r.assoc==LEFT) {
                return LEFT;
            } else if (l.assoc==RIGHT && r.assoc==RIGHT) {
                return RIGHT;
            }
        }
        return NONASS;
    }

  
    public boolean equalsFixity(Fixity that) {
        return (this.assoc==that.assoc) && (this.prec==that.prec);
    }


    public boolean equals(Object o) {
        if (o instanceof Fixity) {
            return equalsFixity((Fixity)o);
        }
        return false;
    }
}

// %COPYRIGHT!
// %LICENSE!
// %DISTRIBUTION_DATE!
// 

package jacc;

import java.io.PrintWriter;
import compiler.Handler;


public class DotOutput extends Output {
    public DotOutput(Handler handler, JaccJob job) {
        super(handler, job);
        tables.analyzeRows();
    }


    private String stateName(int st) {
        return (st<0) ? "accept" : ("state"+st);
    }


    private void edgesFor(PrintWriter out, int st, int[] dsts) {
        for (int i=0; i<dsts.length; i++) {
            int    dst = dsts[i];
            int    sym = machine.getEntry(dst);
            String txt = grammar.getSymbol(sym).getName();
            out.println(stateName(st) + " -> " + stateName(dst)
                     +"[label=\"" + txt + "\"];");
        }
    }


    public void write(PrintWriter out) {
        datestamp(out);
        out.println("digraph " + settings.getInterfaceName() + " {");
        out.println("node [shape=box];");
        for (int st=0; st<numStates; st++) {
            String label   = "State " + st;
            int[]  reduces = machine.getReducesAt(st);
            if (reduces.length>0) {
                String punc = "\\nreduces: {";
                for (int i=0; i<reduces.length; i++) {
                    int rn = machine.reduceItem(st,reduces[i]).getSeqNo();
                    label  = label + punc + rn;
                    punc   = ", ";
                }
                label = label + "}";
            }
            out.println(stateName(st)
                      + "[label=\"" + label + "\"];");
            edgesFor(out, st, machine.getShiftsAt(st));
            edgesFor(out, st, machine.getGotosAt(st));
        }
        out.println("}");
    }
}

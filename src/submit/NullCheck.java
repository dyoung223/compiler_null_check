package submit;

// some useful things to import. add any additional imports you need.
import java.util.*;
import joeq.Compiler.Quad.*;
import joeq.Compiler.Quad.Operand.*;
import flow.*;

/**
 * Skeleton class for implementing a faint variable analysis
 * using the Flow.Analysis interface.
 */
public class NullCheck implements Flow.Analysis {
    

    /**
     * Class for the dataflow objects in the Faintness analysis.
     * You are free to change this class or move it to another file.
     */
    public static class MyDataflowObject implements Flow.DataflowObject {
        private Set<String> set;
        public static Set<String> universalSet;
        /**
         * Methods from the Flow.DataflowObject interface.
         * See Flow.java for the meaning of these methods.
         * These need to be filled in.
         */
        public void setToTop() {
                set = new TreeSet<String>(universalSet);
        }
        public void setToBottom() {
                set.clear();
        }
        public void meetWith (Flow.DataflowObject o) {
                MyDataflowObject a = (MyDataflowObject) o;
                Iterator<String> it = set.iterator();
                while(it.hasNext()) {
                        String reg = it.next();
                        if (!a.set.contains(reg))
                                it.remove();
                }
        }
        public void copy (Flow.DataflowObject o) {
                MyDataflowObject a = (MyDataflowObject) o;
                set = new TreeSet<String>(a.set);
        }
        @Override
        public boolean equals(Object o)
        {
            if (o instanceof MyDataflowObject)
            {
                MyDataflowObject a = (MyDataflowObject) o;
                return set.equals(a.set);
            }
            return false;
        }

        public void killVar(String reg) {
                set.remove(reg);
        }
        public void genVar(String reg) {
                set.add(reg);
        }

        public MyDataflowObject(){
                set = new TreeSet<String>();
        }


        /**
         * toString() method for the dataflow objects which is used
         * by postprocess() below.  The format of this method must
         * be of the form "[REG0, REG1, REG2, ...]", where each REG is
         * the identifier of a register, and the list of REGs must be sorted.
         * See src/test/TestFaintness.out for example output of the analysis.
         * The output format of your reaching definitions analysis must
         * match this exactly.
         */
        @Override
        public String toString() { return set.toString(); }
    }

    /**
     * Dataflow objects for the interior and entry/exit points
     * of the CFG. in[ID] and out[ID] store the entry and exit
     * state for the input and output of the quad with identifier ID.
     *
     * You are free to modify these fields, just make sure to
     * preserve the data printed by postprocess(), which relies on these.
     */
    private MyDataflowObject[] in, out;
    private MyDataflowObject entry, exit;

    /**
     * This method initializes the datflow framework.
     *
     * @param cfg  The control flow graph we are going to process.
     */
    public void preprocess(ControlFlowGraph cfg) {
        // get the amount of space we need to allocate for the in/out arrays.
        QuadIterator qit = new QuadIterator(cfg);
        int max = 0;
        while (qit.hasNext()) {
            int id = qit.next().getID();
            if (id > max)
                max = id;
        }
        max += 1;

        // allocate the in and out arrays.
        in = new MyDataflowObject[max];
        out = new MyDataflowObject[max];

        // initialize the contents of in and out.
        qit = new QuadIterator(cfg);
        while (qit.hasNext()) {
            int id = qit.next().getID();
            in[id] = new MyDataflowObject();
            out[id] = new MyDataflowObject();
        }

        // initialize the entry and exit points.
        entry = new MyDataflowObject();
        exit = new MyDataflowObject();

        /************************************************
         * Your remaining initialization code goes here *
         ************************************************/
        transferfn.val = new MyDataflowObject();
        qit = new QuadIterator(cfg);
        MyDataflowObject.universalSet = new TreeSet<String>();

        int numargs = cfg.getMethod().getParamTypes().length;
        for (int i = 0; i < numargs; i++) {
            MyDataflowObject.universalSet.add("R"+i);
        }

        while (qit.hasNext()) {
                Quad q = qit.next();
                for (RegisterOperand def : q.getDefinedRegisters()) {
                        MyDataflowObject.universalSet.add(def.getRegister().toString());
                }
                for (RegisterOperand use : q.getUsedRegisters()) {
                        MyDataflowObject.universalSet.add(use.getRegister().toString());
                }
        }

        qit = new QuadIterator(cfg);
        while (qit.hasNext()) {
            int id = qit.next().getID();
            in[id].setToTop();
            out[id].setToTop();
        }

        entry.setToBottom();
        //DEBUG to remove ////////////////////////////////////////////////////////////
        qit = new QuadIterator(cfg);
        while (qit.hasNext()) {
            Quad q = qit.next();
            System.out.println(q);
        }
        //////////////////////////////////////////////////////////////////
    }

    /**
     * This method is called after the fixpoint is reached.
     * It must print out the dataflow objects associated with
     * the entry, exit, and all interior points of the CFG.
     * Unless you modify in, out, entry, or exit you shouldn't
     * need to change this method.
     *
     * @param cfg  Unused.
     */
    public void postprocess (ControlFlowGraph cfg) {
        System.out.print(cfg.getMethod().getName().toString());
        QuadIterator qit = new QuadIterator(cfg);
        System.out.println("QID: " + "entry" + " out: " + entry.toString());
        while (qit.hasNext()) {
            Quad q = qit.next();
            Operator op = q.getOperator();
            int id = q.getID();
            System.out.println("QID: " + id + " in: " + in[id].toString());
            System.out.println("Out: " + out[id].toString());
            
            if (!(op instanceof Operator.NullCheck)){
                continue;
            }
            if (in[id].set.contains(Operator.NullCheck.getSrc(q).toString()) ) {
                //System.out.print(" " + id);
                System.out.print("QID: " + id);
                System.out.print("Register: " + Operator.NullCheck.getSrc(q).toString());
                
            }
        }
        System.out.println("QID: " + "exit" + " out: " + exit.toString());
        System.out.println("");
    }

    /**
     * Other methods from the Flow.Analysis interface.
     * See Flow.java for the meaning of these methods.
     * These need to be filled in.
     */
    public boolean isForward () { return true; }
    public Flow.DataflowObject getEntry() {
        Flow.DataflowObject result = newTempVar();
        result.copy(entry);
        return result;
    }
    public Flow.DataflowObject getExit() {
        Flow.DataflowObject result = newTempVar();
        result.copy(exit);
        return result;
    }
    public void setEntry(Flow.DataflowObject value) {
        entry.copy(value);
    }
    public void setExit(Flow.DataflowObject value) {
        exit.copy(value);
    }
    public Flow.DataflowObject getIn(Quad q) {
        Flow.DataflowObject result = newTempVar();
        result.copy(in[q.getID()]);
        return result;
    }
    public Flow.DataflowObject getOut(Quad q) {
        Flow.DataflowObject result = newTempVar();
        result.copy(out[q.getID()]);
        return result;
    }
    public void setIn(Quad q, Flow.DataflowObject value) {
        System.out.println("In being set to: " + value.toString());
        in[q.getID()].copy(value);
    }
    public void setOut(Quad q, Flow.DataflowObject value) {
        out[q.getID()].copy(value);
    }
    public Flow.DataflowObject newTempVar() {
        MyDataflowObject temp = new MyDataflowObject();
        temp.setToTop();
        return temp;
    }

    private TransferFunction transferfn = new TransferFunction ();
    public void processQuad(Quad q) {
        transferfn.val.copy(out[q.getID()]);
        transferfn.visitQuad(q);
        in[q.getID()].copy(transferfn.val);
    }

    /* The QuadVisitor that actually does the computation */
    public static class TransferFunction extends QuadVisitor.EmptyVisitor {
        MyDataflowObject val;
        @Override
        public void visitNullCheck (Quad q) {
            String src = Operator.NullCheck.getSrc(q).toString();
            val.genVar(src);
        }

        @Override
        public void visitQuad(Quad q) {
            Operator op = q.getOperator();
            if (op instanceof Operator.NullCheck){
                visitNullCheck(q);
                return;
            }

            for (RegisterOperand def : q.getDefinedRegisters()) {
                val.killVar(def.getRegister().toString());
            }
        }
    }

}


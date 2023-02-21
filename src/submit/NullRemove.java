package submit;

// some useful things to import. add any additional imports you need.
import joeq.Compiler.Quad.*;
import joeq.Compiler.Quad.Operand.*;


public class NullRemove extends NullCheck{
    @Override
    public void postprocess (ControlFlowGraph cfg) {
        QuadIterator qit = new QuadIterator(cfg);
        while (qit.hasNext()) {
            Quad q = qit.next();
            Operator op = q.getOperator();
            int id = q.getID();
            
            if (!(op instanceof Operator.NullCheck)){
                continue;
            }
            
            for (RegisterOperand use : q.getUsedRegisters()) {
                if (in[id].set.contains(use.getRegister().toString())) {
                    qit.remove();
                }
            }
        }
    }
}

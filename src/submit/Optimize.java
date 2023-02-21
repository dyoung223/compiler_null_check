package submit;

import flow.*;
import java.util.List;
import joeq.Class.jq_Class;
import joeq.Main.Helper;

public class Optimize {
  /*
   * optimizeFiles is a list of names of class that should be optimized, e.g. test.SkipList
   * Extra credit: if nullCheckOnly is true, disable all optimizations except "remove redundant NULL_CHECKs."
   */
  public static void optimize(List<String> optimizeFiles, boolean nullCheckOnly) {
    MySolver solver = new MySolver();
    Flow.Analysis analysis;
    try{
      Object analysis_obj = Class.forName("submit.NullRemove").newInstance();
      analysis = (Flow.Analysis) analysis_obj;
    }
    catch (Exception ex) {
      System.out.println("ERROR: Could not load class submit.NullRemove as Analysis: " + ex.toString());
      return;
    }
    solver.registerAnalysis(analysis);
    
    for (String file : optimizeFiles) {
      jq_Class cls = (jq_Class) Helper.load(file);
      // Find redundant NULL_CHECKs for each class
      // Hint: use MySolver and register a new Analysis
      Helper.runPass(cls, solver);
    }
  }
}

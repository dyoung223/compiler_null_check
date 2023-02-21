package submit;

import flow.MySolver;
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
    for (int i = 0; i < optimizeFiles.size(); i++) {
      jq_Class cls = (jq_Class) Helper.load(optimizeFiles.get(i));
      // Run your optimization on each classes
      // Hint: use MySolver and register a new Analysis
    }
  }
}

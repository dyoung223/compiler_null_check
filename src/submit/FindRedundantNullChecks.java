package submit;

import flow.MySolver;
import joeq.Class.jq_Class;
import joeq.Main.Helper;

public class FindRedundantNullChecks {

  /*
   * args is an array of class names, e.g. test.SkipList
   * Method should print out a list of quad ids of redundant null checks for each function
   * Please refer to src/test/NullTest.basic.out or src/test/SkipList.basic.out
   */
  public static void main(String[] args) {
    MySolver solver = new MySolver();
    for (int i = 0; i < args.length; i++) {
      jq_Class cls = (jq_Class) Helper.load(args[i]);
      // Find redundant NULL_CHECKs for each class
      // Hint: use MySolver and register a new Analysis
    }
  }
}

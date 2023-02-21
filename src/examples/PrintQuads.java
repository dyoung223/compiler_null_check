package examples;

import joeq.Class.jq_Class;
import joeq.Compiler.Quad.PrintCFG;
import joeq.Main.Helper;

class PrintQuads {

  public static void main(String[] args) {
    jq_Class[] classes = new jq_Class[args.length];
    for (int i = 0; i < classes.length; i++) classes[i] = (jq_Class) Helper.load(args[i]);

    for (int i = 0; i < classes.length; i++) {
      System.out.println("Class: " + classes[i].getName());
      Helper.runPass(classes[i], new PrintCFG());
    }
  }
}

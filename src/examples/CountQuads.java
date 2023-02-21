package examples;

import joeq.Class.PrimordialClassLoader;
import joeq.Class.jq_Class;
import joeq.Compiler.Quad.Quad;
import joeq.Compiler.Quad.QuadVisitor;
import joeq.Main.Helper;

class CountQuads {

  public static void main(String[] args) {
    PrimordialClassLoader.loader.addToClasspath("lib/rt.jar");

    jq_Class[] classes = new jq_Class[args.length];
    for (int i = 0; i < classes.length; i++) classes[i] = (jq_Class) Helper.load(args[i]);

    for (int i = 0; i < classes.length; i++) {
      System.out.println("Class: " + classes[i].getName());
      QuadCounter qc = new QuadCounter();
      Helper.runPass(classes[i], qc);
      System.out.println(classes[i].getName() + " has " + qc.count + " quads");
    }
  }

  public static class QuadCounter extends QuadVisitor.EmptyVisitor {
    public int count = 0;

    @Override
    public void visitQuad(Quad q) {
      count++;
    }
  }
}

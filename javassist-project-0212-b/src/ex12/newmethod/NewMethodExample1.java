package ex12.newmethod;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public class NewMethodExample1 {
   static String workDir   = System.getProperty("user.dir");
   static String inputDir  = workDir + File.separator + "classfiles";
   static String outputDir = workDir + File.separator + "output";
   static String _L_ = System.lineSeparator();

   public static void main(String[] args) {
      try {
         ClassPool pool = ClassPool.getDefault();
         pool.insertClassPath(inputDir);
         CtClass cc = pool.get("target.Point");
         String block1 = "public void xmove(int dx) { " + _L_ //
               + "  x += dx; " + _L_ //
               + "}";
         System.out.println("[DBG] BLOCK1: ");
         System.out.println(block1);
         CtMethod newMethod = CtNewMethod.make(block1, cc);
         cc.addMethod(newMethod);
         cc.writeFile(outputDir);
         System.out.println("[DBG] write output to: " + outputDir);
      } catch (NotFoundException | CannotCompileException | IOException e) {
         e.printStackTrace();
      }
   }
}

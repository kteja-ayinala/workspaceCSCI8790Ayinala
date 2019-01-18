package ex03.define;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;

public class DefineNewClass {
   static String WORK_DIR = System.getProperty("user.dir");
   static String OUTPUT_DIR = WORK_DIR + File.separator + "output";

   public static void main(String[] args) {
      try {
         ClassPool pool = ClassPool.getDefault();

         CtClass cc = pool.makeClass("Point3");
         System.out.println("[DBG] make class: " + cc.getName());

         cc.writeFile(OUTPUT_DIR);
         System.out.println("[DBG] write output to: " + OUTPUT_DIR);

         CtClass ccInterface = pool.makeInterface("IPoint");
         System.out.println("[DBG] make interface: " + ccInterface.getName());

         ccInterface.writeFile(OUTPUT_DIR);
         System.out.println("[DBG] write output to: " + OUTPUT_DIR);

      } catch (CannotCompileException | IOException e) {
         e.printStackTrace();
      }
   }
}

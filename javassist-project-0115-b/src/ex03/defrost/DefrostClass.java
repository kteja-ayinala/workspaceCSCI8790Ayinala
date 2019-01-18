package ex03.defrost;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class DefrostClass {
   static String WORK_DIR = System.getProperty("user.dir");
   static String OUTPUT_DIR = WORK_DIR + File.separator + "output";

   public static void main(String[] args) {
      try {
         ClassPool pool = ClassPool.getDefault();
         pool.insertClassPath(OUTPUT_DIR);
         System.out.println("[DBG] class path: " + OUTPUT_DIR);

         CtClass ccPoint2 = pool.makeClass("Point2");
         ccPoint2.writeFile(OUTPUT_DIR);
         System.out.println("[DBG] write output to: " + OUTPUT_DIR);
         System.out.println("[DBG]\t new class: " + ccPoint2.getName());

         CtClass ccRectangle2 = pool.makeClass("Rectangle2");
         ccRectangle2.writeFile(OUTPUT_DIR);
         System.out.println("[DBG] write output to: " + OUTPUT_DIR);
         System.out.println("[DBG]\t new class: " + ccRectangle2.getName());

         ccRectangle2.defrost();
         System.out.println("[DBG] modifications of the class definition will be permitted.");

         ccRectangle2.setSuperclass(ccPoint2);
         System.out.println("[DBG] set super class, " + ccRectangle2.getName() + " -> " + ccPoint2.getName());

         ccRectangle2.writeFile(OUTPUT_DIR);
         System.out.println("[DBG] write output to: " + OUTPUT_DIR);
      } catch (NotFoundException | CannotCompileException | IOException e) {
         e.printStackTrace();
      }
   }

   static void insertClassPath(ClassPool pool) throws NotFoundException {
      String strClassPath = OUTPUT_DIR;
      pool.insertClassPath(strClassPath);
      System.out.println("[DBG] insert classpath: " + strClassPath);
   }
}

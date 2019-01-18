package ex02.setsuper;

import java.io.File;
import java.io.IOException;

import ex02.util.UtilMenu;
import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import target.Rectangle;

public class SetSuperclass {
   static String _S = File.separator;
   static String WORK_DIR = System.getProperty("user.dir");
   // static String CLASSPATH_DIR = WORK_DIR + _S + "classfiles";
   static String OUTPUT_DIR = WORK_DIR + _S + "output";
   

   public static void main(String[] args) {
      try {
         while (true) {
            UtilMenu.showMenuOptions();
            switch (UtilMenu.getOption()) {
            case 1:
               System.out.println("Enter two class names:");
               String[] clazNames = UtilMenu.getArguments();
               setSuperClass(clazNames[0], clazNames[1]);
               break;
            default:
               break;
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   static void setSuperClass(String clazSub, String clazSuper) {
      try {
         ClassPool pool = ClassPool.getDefault();
         insertClassPathRunTimeClass(pool);

         CtClass ctClazSub = pool.get("target." + clazSub);
         CtClass ctClazSuper = pool.get("target." + clazSuper);
         ctClazSub.setSuperclass(ctClazSuper);
         System.out.println("[DBG] set superclass: " //
               + ctClazSub.getSuperclass().getName() //
               + ", subclass: " + ctClazSub.getName());

         ctClazSub.writeFile(OUTPUT_DIR);
         System.out.println("[DBG] write output to: " + OUTPUT_DIR);
      } catch (NotFoundException | CannotCompileException | IOException e) {
         e.printStackTrace();
      }
   }

   static void insertClassPathRunTimeClass(ClassPool pool) throws NotFoundException {
      Rectangle rectangle = new Rectangle();
      Class<?> runtimeObject = rectangle.getClass();
      ClassClassPath classPath = new ClassClassPath(runtimeObject);
      pool.insertClassPath(classPath);
      System.out.println("[DBG] insert classpath: " + classPath.toString());
   }

   /*static void insertClassPath(ClassPool pool) throws NotFoundException {
      pool.insertClassPath(CLASSPATH_DIR);
      System.out.println("[DBG] insert classpath: " + CLASSPATH_DIR);
   }*/

   /*static void setSuperclass(CtClass curClass, String superClass, ClassPool pool) //
         throws NotFoundException, CannotCompileException {
      curClass.setSuperclass(pool.get(superClass));
      System.out.println("[DBG] set superclass: " //
            + curClass.getSuperclass().getName() //
            + ", subclass: " + curClass.getName());
   }*/
}

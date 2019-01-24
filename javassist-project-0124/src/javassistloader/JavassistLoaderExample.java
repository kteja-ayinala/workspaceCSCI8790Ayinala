package javassistloader;

import java.io.File;
import java.lang.reflect.Method;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;

public class JavassistLoaderExample {
   private static final String WORK_DIR = System.getProperty("user.dir");
   private static final String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
   private static final String TARGET_POINT = "target.Point";
   private static final String TARGET_RECTANGLE = "target.Rectangle";

   public static void main(String[] args) {
      try {
         ClassPool cp = ClassPool.getDefault();
         cp.insertClassPath(INPUT_DIR);
         System.out.println("[DBG] insert classpath: " + INPUT_DIR);

         CtClass cc = cp.get(TARGET_RECTANGLE);
         cc.setSuperclass(cp.get(TARGET_POINT));
         CtMethod m1 = cc.getDeclaredMethod("getVal");
         m1.insertBefore("{ " //
               + "move(10, 20);" //
               + "System.out.println(\"[TR] getX result : \" + getX()); }");

         Loader cl = new Loader(cp);
         Class<?> c = cl.loadClass(TARGET_RECTANGLE);
         Object rect = c.newInstance();
         System.out.println("[DBG] Created a Rectangle object.");

         Class<?> rectClass = rect.getClass();
         Method m = rectClass.getDeclaredMethod("getVal", new Class[] {});
         System.out.println("[DBG] Called getDeclaredMethod.");
         Object invoker = m.invoke(rect, new Object[] {});
         System.out.println("[DBG] getVal result: " + invoker);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   /*static void insertClassPath(ClassPool pool) throws NotFoundException {
      String strClassPath = WORK_DIR + File.separator + "classfiles";
      pool.insertClassPath(strClassPath);
      System.out.println("[DBG] insert classpath: " + strClassPath);
   }*/
}

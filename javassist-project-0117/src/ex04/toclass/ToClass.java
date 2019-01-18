package ex04.toclass;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;
import target.Hello;

public class ToClass {
   public static void main(String[] args) {
      try {
         // Hello orig = new Hello(); // java.lang.LinkageError

         ClassPool cp = ClassPool.getDefault();
         CtClass cc = cp.get("target.Hello");
         CtMethod m = cc.getDeclaredMethod("say");
         m.insertBefore("{ System.out.println(\"[TR] Hello.say: \" + i); }");

         CtConstructor declaredConstructor = cc.getDeclaredConstructor(new CtClass[0]);
         declaredConstructor.insertAfter("{ " //
               + "System.out.println(\"[TR] After calling a constructor: \" + i); }");

         Class<?> c = cc.toClass();
         Hello h = (Hello) c.newInstance();
         h.say();
      } catch (NotFoundException | CannotCompileException | //
            InstantiationException | IllegalAccessException e) {
         System.out.println(e.toString());
      }
   }
}

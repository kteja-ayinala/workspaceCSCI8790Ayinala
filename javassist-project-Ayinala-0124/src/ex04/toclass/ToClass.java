package ex04.toclass;

import ex04.util.UtilMenu;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;

public class ToClass {
	public static void main(String[] args) {
		try {
			// Hello orig = new Hello(); // java.lang.LinkageError

			ClassPool cp = ClassPool.getDefault();

			while (true) {
				UtilMenu.showMenuOptions();
				switch (UtilMenu.getOption()) {
				case 1:
					System.out.println("Enter two class names:");
					String[] clazNames = UtilMenu.getArguments();
					if (clazNames.length != 2) {
						System.out.println("[WRN]: Invalid Input");
						continue;
					} else {
						for (String clz : clazNames) {
							CtClass cc = cp.get("target." + clz);
							CtConstructor declaredConstructor = cc.getDeclaredConstructor(new CtClass[0]);
							declaredConstructor.insertAfter("{ " //
									+ "System.out.println(\"[TR] After calling a constructor: \" + id); }");

							Class<?> c = cc.toClass();
							c.newInstance();
						}
					}
					break;
				default:
					break;
				}
			}

		} catch (NotFoundException | CannotCompileException | //
				InstantiationException | IllegalAccessException e) {
			System.out.println(e.toString());
		}
	}
}

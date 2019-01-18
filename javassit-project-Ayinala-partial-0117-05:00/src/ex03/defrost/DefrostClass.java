package ex03.defrost;

import java.io.File;
import java.io.IOException;

import ex03.util.UtilMenu;
import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import target.Rectangle;

public class DefrostClass {
	static String WORK_DIR = System.getProperty("user.dir");
	static String OUTPUT_DIR = WORK_DIR + File.separator + "output";

	public static void main(String[] args) throws CannotCompileException, NotFoundException {
		String arg0 = null;
		String arg1 = null;
		int argswithcommon = 0;
		String superclass = null;
		String subclass = null;
		try {
			while (true) {
				UtilMenu.showMenuOptions();
				switch (UtilMenu.getOption()) {
				case 1:
					System.out.println("Enter two class names:");
					String[] clazNames = UtilMenu.getArguments();
					if (clazNames.length != 2) {
						System.out.println("Not enough arguments");
						return;
					}
					for (int i = 0; i < clazNames.length; i++) {
						System.out.println("[DBG] Arg[" + i + "]=" + clazNames[i]);
						arg0 = clazNames[0];
						arg1 = clazNames[1];
						if (clazNames[i].startsWith("Common")) {
							argswithcommon = argswithcommon + 1;
							superclass = "target." + clazNames[i];
							System.out.println(superclass);
						} else {
							subclass = "target." + clazNames[i];
							System.out.println(subclass);
						}
					}
					if (argswithcommon == 1) {
						subclass = subclass;
						superclass = superclass;

					} else if (argswithcommon == 2) {
						if (arg0.length() > arg1.length()) {
							superclass = "target." + arg0;
							subclass = "target." + arg1;
						} else {
							superclass = "target." + arg1;
							subclass = "target." + arg0;
						}
					} else {
						superclass = "target." + arg0;
						subclass = "target." + arg1;
					}

					System.out.println("[DBG] write output to: " + OUTPUT_DIR);
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ClassPool pool = ClassPool.getDefault();
			pool.insertClassPath(OUTPUT_DIR);
			System.out.println("[DBG] class path: " + OUTPUT_DIR);

			CtClass ccPoint2 = pool.makeClass(superclass);
			ccPoint2.writeFile(OUTPUT_DIR);
			System.out.println("[DBG] write output to: " + OUTPUT_DIR);
			System.out.println("[DBG]\t new class: " + ccPoint2.getName());

			CtClass ccRectangle2 = pool.makeClass(subclass);
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
		setSuperClass(subclass, superclass);
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
}

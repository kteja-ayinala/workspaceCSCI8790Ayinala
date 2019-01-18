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
	static String OUTPUT_DIR = WORK_DIR + _S + "output";

	public static void main(String[] args) {
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
						System.out.println("[WRN] Invalid Input");
						continue;
					}
					for (int i = 0; i < clazNames.length; i++) {
						System.out.println("[DBG] Arg[" + i + "]=" + clazNames[i]);
						arg0 = clazNames[0];
						arg1 = clazNames[1];
						if (clazNames[i].startsWith("Common")) {
							argswithcommon = argswithcommon + 1;
							superclass = clazNames[i];
							System.out.println(superclass);
						} else {
							subclass = clazNames[i];
							System.out.println(subclass);
						}
					}

					if (argswithcommon == 1) {
						if (arg0.length() > arg1.length()) {
							superclass = arg0;
							subclass = arg1;
						} else {
							superclass = arg1;
							subclass = arg0;
						}
					} else {
						superclass = arg0;
						subclass = arg1;
					}
					ClassPool pool = ClassPool.getDefault();
					pool.insertClassPath(OUTPUT_DIR);
					System.out.println("[DBG] class path: " + OUTPUT_DIR);

					CtClass ccPoint2 = pool.makeClass(arg0);
					ccPoint2.writeFile(OUTPUT_DIR);
					System.out.println("[DBG] write output to: " + OUTPUT_DIR);
					System.out.println("[DBG]\t new class: " + ccPoint2.getName());

					CtClass ccRectangle2 = pool.makeClass(arg1);
					ccRectangle2.writeFile(OUTPUT_DIR);
					System.out.println("[DBG] write output to: " + OUTPUT_DIR);
					System.out.println("[DBG]\t new class: " + ccRectangle2.getName());

					ccRectangle2.defrost();
					System.out.println("[DBG] modifications of the class definition will be permitted.");

					ccRectangle2.setSuperclass(ccPoint2);
					System.out
							.println("[DBG] set super class, " + ccRectangle2.getName() + " -> " + ccPoint2.getName());

					ccRectangle2.writeFile(OUTPUT_DIR);
					System.out.println("[DBG] write output to: " + OUTPUT_DIR);
					break;
				default:
					break;
				}
			}

		} catch (NotFoundException | CannotCompileException | IOException e) {
			e.printStackTrace();
		}

	}

	static void insertClassPath(ClassPool pool) throws NotFoundException {
		String strClassPath = WORK_DIR + _S + "target";
		pool.insertClassPath(strClassPath);
		System.out.println("[DBG] insert classpath: " + strClassPath);
	}
}

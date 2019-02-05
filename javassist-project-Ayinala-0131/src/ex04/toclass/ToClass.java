package ex04.toclass;

import java.io.File;
import java.io.IOException;

import ex04.util.UtilMenu;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;

public class ToClass {
	static String _S = File.separator;
	static String WORK_DIR = System.getProperty("user.dir");
	static String OUTPUT_DIR = WORK_DIR + _S + "output";

	public static void main(String[] args) throws IOException {
		try {
			while (true) {
				UtilMenu.showMenuOptions();
				switch (UtilMenu.getOption()) {
				case 1:
					System.out.println("Enter  class and two field names:");
					String[] input = UtilMenu.getArguments();
					if (input.length != 3) {
						System.out.println("[WRN]: Invalid Input");
						continue;
					} else {
						ClassPool cp = ClassPool.getDefault();
						CtClass cc = cp.get("target." + input[0]);
//						int id = Integer.parseInt(input[1]);
//						String name = input[2];
						String variable = null;
						if (input[0].contains("A")) {
							variable = "A";
						} else {
							variable = "B";
						}
						String arg1 = "id" + variable;
						String arg2 = "name" + variable;
						// System.out.println(" " + arg2 + "=" + name + ";");
						// System.out.println(" " + arg1 + "=" + id);
						// System.out.println("System.out.println(\"[TR] id:\"
						// +" + arg1 + ");");
						// System.out.println("System.out.println(\"[TR] name:\"
						// +" + arg2 + "); }");
						CtConstructor declaredConstructor = cc.getDeclaredConstructor(new CtClass[0]);
						declaredConstructor.insertAfter("{ " //
								// + " " + arg1 + "=" + id + ";"
								// + " " + arg2 + "=" + name + ";"
								+ "System.out.println(\"[TR] id:\" +" + arg1 + ");"
								+ "System.out.println(\"[TR] name:\" +" + arg2 + "); }");
						Class<?> c = cc.toClass();
						c.newInstance();
						cc.writeFile(OUTPUT_DIR);
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
